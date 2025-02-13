package com.gregtechceu.gtceu.common.item.tool.behavior;

import com.gregtechceu.gtceu.api.item.datacomponents.AoESymmetrical;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.item.tool.behavior.ToolBehaviorType;
import com.gregtechceu.gtceu.data.tools.GTToolBehaviors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ItemAbilities;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class GrassPathBehavior implements IToolBehavior<GrassPathBehavior> {

    public static final GrassPathBehavior INSTANCE = create();
    public static final Codec<GrassPathBehavior> CODEC = Codec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, GrassPathBehavior> STREAM_CODEC = StreamCodec
            .unit(INSTANCE);

    protected GrassPathBehavior() {/**/}

    protected static GrassPathBehavior create() {
        return new GrassPathBehavior();
    }

    @NotNull
    @Override
    public InteractionResult onItemUse(UseOnContext context) {
        if (context.getClickedFace() == Direction.DOWN)
            return InteractionResult.PASS;

        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();

        ItemStack stack = player.getItemInHand(hand);
        AoESymmetrical aoeDefinition = ToolHelper.getAoEDefinition(stack);

        Set<BlockPos> blocks;
        // only attempt to till if the center block is tillable
        if (level.getBlockState(pos.above()).isAir() && isBlockPathConvertible(stack, level, player, pos, context)) {
            if (aoeDefinition == AoESymmetrical.none()) {
                blocks = ImmutableSet.of(pos);
            } else {
                HitResult rayTraceResult = ToolHelper.getPlayerDefaultRaytrace(player);

                if (rayTraceResult == null)
                    return InteractionResult.PASS;
                if (rayTraceResult.getType() != HitResult.Type.BLOCK)
                    return InteractionResult.PASS;
                if (!(rayTraceResult instanceof BlockHitResult blockHitResult))
                    return InteractionResult.PASS;
                if (blockHitResult.getDirection() == null)
                    return InteractionResult.PASS;

                blocks = getPathConvertibleBlocks(stack, aoeDefinition, level, player, rayTraceResult);
                blocks.add(blockHitResult.getBlockPos());
            }
        } else
            return InteractionResult.PASS;

        boolean pathed = false;
        for (BlockPos blockPos : blocks) {
            BlockState newState = getFlattened(level.getBlockState(blockPos),
                    new UseOnContext(player, hand, context.getHitResult().withPosition(blockPos)));
            if (newState == null) {
                continue;
            }
            pathed |= level.setBlock(blockPos, newState, Block.UPDATE_ALL);
            if (!player.isCreative()) {
                ToolHelper.damageItem(context.getItemInHand(), context.getPlayer());
            }
            if (stack.isEmpty())
                break;
        }

        if (pathed) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHOVEL_FLATTEN,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public static Set<BlockPos> getPathConvertibleBlocks(ItemStack stack, AoESymmetrical aoeDefinition, Level world,
                                                         Player player, HitResult rayTraceResult) {
        return ToolHelper.iterateAoE(stack, aoeDefinition, world, player, rayTraceResult,
                GrassPathBehavior.INSTANCE::isBlockPathConvertible);
    }

    protected boolean isBlockPathConvertible(ItemStack stack, Level level, Player player, BlockPos pos,
                                             UseOnContext context) {
        if (level.getBlockState(pos.above()).isAir()) {
            BlockState state = level.getBlockState(pos);
            BlockState newState = state.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false);
            return newState != null && newState != state;
        }
        return false;
    }

    protected BlockState getFlattened(BlockState unFlattenedState, UseOnContext context) {
        return unFlattenedState.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, Item.TooltipContext Level, @NotNull List<Component> tooltip,
                               @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.gtceu.tool.behavior.grass_path"));
    }

    @Override
    public ToolBehaviorType<GrassPathBehavior> getType() {
        return GTToolBehaviors.PATH;
    }
}

package com.gregtechceu.gtceu.common.item.tool.behavior;

import com.gregtechceu.gtceu.api.item.datacomponents.AoESymmetrical;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.item.tool.behavior.ToolBehaviorType;
import com.gregtechceu.gtceu.data.tools.GTToolBehaviors;

import net.minecraft.core.BlockPos;
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

public class LogStripBehavior implements IToolBehavior<LogStripBehavior> {

    public static final LogStripBehavior INSTANCE = create();
    public static final Codec<LogStripBehavior> CODEC = Codec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, LogStripBehavior> STREAM_CODEC = StreamCodec
            .unit(INSTANCE);

    protected LogStripBehavior() {/**/}

    protected static LogStripBehavior create() {
        return new LogStripBehavior();
    }

    @NotNull
    @Override
    public InteractionResult onItemUse(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();

        ItemStack stack = player.getItemInHand(hand);
        AoESymmetrical aoeDefinition = ToolHelper.getAoEDefinition(stack);

        Set<BlockPos> blocks;
        // only attempt to strip if the center block is strippable
        if (isBlockStrippable(stack, level, player, pos, context)) {
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

                blocks = getStrippableBlocks(stack, aoeDefinition, level, player, rayTraceResult);
                blocks.add(blockHitResult.getBlockPos());
            }
        } else
            return InteractionResult.PASS;

        boolean stripped = false;
        for (BlockPos blockPos : blocks) {
            stripped |= level.setBlock(blockPos,
                    getStripped(level.getBlockState(blockPos),
                            new UseOnContext(player, hand, context.getHitResult().withPosition(blockPos))),
                    Block.UPDATE_ALL);
            if (!player.isCreative()) {
                ToolHelper.damageItem(context.getItemInHand(), context.getPlayer());
            }
            if (stack.isEmpty())
                break;
        }

        if (stripped) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AXE_STRIP,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public static Set<BlockPos> getStrippableBlocks(ItemStack stack, AoESymmetrical aoeDefinition, Level Level,
                                                    Player player, HitResult rayTraceResult) {
        return ToolHelper.iterateAoE(stack, aoeDefinition, Level, player, rayTraceResult,
                LogStripBehavior.INSTANCE::isBlockStrippable);
    }

    protected boolean isBlockStrippable(ItemStack stack, Level level, Player player, BlockPos pos,
                                        UseOnContext context) {
        BlockState state = level.getBlockState(pos);
        BlockState newState = state.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false);
        return newState != null && newState != state;
    }

    protected BlockState getStripped(BlockState unscrapedState, UseOnContext context) {
        return unscrapedState.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, Item.TooltipContext Level, @NotNull List<Component> tooltip,
                               @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.gtceu.tool.behavior.strip_log"));
    }

    @Override
    public ToolBehaviorType<LogStripBehavior> getType() {
        return GTToolBehaviors.STRIP_LOG;
    }
}

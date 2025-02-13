package com.gregtechceu.gtceu.client.renderer.machine;

import com.gregtechceu.gtceu.common.blockentity.KineticMachineBlockEntity;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

/**
 * @author KilaBash
 * @date 2023/3/31
 * @implNote KineticMachineRenderer
 */
public interface IKineticMachineRenderer extends IRenderer {

    default boolean isInvalid(BlockEntity te) {
        return !te.hasLevel() || te.getBlockState().getBlock() == Blocks.AIR;
    }

    default BlockState getRenderedBlockState(KineticMachineBlockEntity te) {
        return te.getBlockState();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    default void render(BlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer,
                        int combinedLight, int combinedOverlay) {
        if (isInvalid(blockEntity)) return;
        if (blockEntity instanceof KineticMachineBlockEntity kineticMachineBlockEntity) {
            renderSafe(kineticMachineBlockEntity, partialTicks, stack, buffer, combinedLight, combinedOverlay);
        }
    }

    @OnlyIn(Dist.CLIENT)
    default void renderSafe(KineticMachineBlockEntity te, float partialTicks, PoseStack ms,
                            MultiBufferSource bufferSource, int light, int overlay) {
        if (Backend.canUseInstancing(te.getLevel())) return;
        BlockState state = getRenderedBlockState(te);
        RenderType type = getRenderType(te, state);
        if (type != null) {
            KineticBlockEntityRenderer.renderRotatingBuffer(te, getRotatedModel(te, state), ms,
                    bufferSource.getBuffer(type), light);
        }
    }

    @OnlyIn(Dist.CLIENT)
    default RenderType getRenderType(KineticMachineBlockEntity te, BlockState state) {
        return ItemBlockRenderTypes.getChunkRenderType(state);
    }

    @OnlyIn(Dist.CLIENT)
    default SuperByteBuffer getRotatedModel(KineticMachineBlockEntity te, BlockState state) {
        return CachedBufferer.block(KineticBlockEntityRenderer.KINETIC_BLOCK, state);
    }
}

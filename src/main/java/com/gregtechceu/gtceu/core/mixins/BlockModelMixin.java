package com.gregtechceu.gtceu.core.mixins;

import com.gregtechceu.gtceu.client.model.SpriteOverrider;

import com.lowdragmc.lowdraglib.client.model.ModelFactory;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;

import com.mojang.datafixers.util.Either;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

/**
 * @author KilaBash
 * @date 2023/2/19
 * @implNote BlockModelMixin
 */
@Mixin(BlockModel.class)
public class BlockModelMixin {

    @Shadow
    public String name;
    @Unique
    private static final ThreadLocal<SpriteOverrider> gtceu$spriteOverriderThreadLocal = ThreadLocal
            .withInitial(() -> null);

    // We want to remap our materials
    @Inject(method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Z)Lnet/minecraft/client/resources/model/BakedModel;",
            at = @At(value = "HEAD"))
    private void beforeBake(ModelBaker baker, BlockModel model, Function<Material, TextureAtlasSprite> spriteGetter,
                            ModelState state, boolean guiLight3d,
                            CallbackInfoReturnable<BakedModel> cir) {
        if (spriteGetter instanceof SpriteOverrider spriteOverrider) {
            gtceu$spriteOverriderThreadLocal.set(spriteOverrider);
        }
    }

    @Inject(method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Z)Lnet/minecraft/client/resources/model/BakedModel;",
            at = @At(value = "RETURN"))
    private void afterBake(ModelBaker baker, BlockModel model, Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState state, boolean guiLight3d,
                           CallbackInfoReturnable<BakedModel> cir) {
        if (spriteGetter instanceof SpriteOverrider) {
            gtceu$spriteOverriderThreadLocal.remove();
        }
    }

    // We want to remap our materials
    @Inject(method = "findTextureEntry", at = @At(value = "HEAD"), cancellable = true)
    private void remapTextureEntry(String name, CallbackInfoReturnable<Either<Material, String>> cir) {
        SpriteOverrider overrider = gtceu$spriteOverriderThreadLocal.get();
        if (overrider != null && overrider.override().containsKey(name)) {
            var mat = overrider.override().get(name);
            if (mat != null) {
                cir.setReturnValue(ModelFactory.parseBlockTextureLocationOrReference(mat.toString()));
            }
        }
    }
}

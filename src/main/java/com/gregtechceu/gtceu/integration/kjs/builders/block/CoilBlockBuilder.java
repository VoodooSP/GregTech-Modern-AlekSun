package com.gregtechceu.gtceu.integration.kjs.builders.block;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.block.SimpleCoilType;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.integration.kjs.builders.RendererBlockItemBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Accessors(chain = true, fluent = true)
public class CoilBlockBuilder extends BlockBuilder {

    @Setter
    public transient int temperature = 0, level = 0, energyDiscount = 1, tier = 0;
    @NotNull
    public transient Supplier<@Nullable Material> material = () -> null;
    @Setter
    public transient ResourceLocation texture = ResourceLocation.withDefaultNamespace("missingno");

    public CoilBlockBuilder(ResourceLocation i) {
        super(i);
    }

    public CoilBlockBuilder coilMaterial(@NotNull Supplier<@Nullable Material> material) {
        this.material = material;
        return this;
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {}

    @Override
    protected ItemBuilder getOrCreateItemBuilder() {
        return itemBuilder == null ? (itemBuilder = new RendererBlockItemBuilder(id)) : itemBuilder;
    }

    @Override
    public Block createObject() {
        SimpleCoilType coilType = new SimpleCoilType(this.id.getPath(), temperature, level, energyDiscount, tier,
                material, texture);
        CoilBlock result = new CoilBlock(this.createProperties(), coilType);
        GTCEuAPI.HEATING_COILS.put(coilType, () -> result);
        return result;
    }
}

package com.gregtechceu.gtceu.integration.kjs.events;

import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.api.worldgen.bedrockfluid.BedrockFluidDefinition;

import net.minecraft.resources.ResourceLocation;

import dev.latvian.mods.kubejs.event.KubeEvent;

import java.util.function.Consumer;

public class GTFluidVeinEventJS implements KubeEvent {

    public GTFluidVeinEventJS() {}

    public void add(ResourceLocation id, Consumer<BedrockFluidDefinition.Builder> consumer) {
        BedrockFluidDefinition.Builder builder = BedrockFluidDefinition.builder(id);
        consumer.accept(builder);
        builder.register();
    }

    public void remove(ResourceLocation id) {
        GTRegistries.BEDROCK_FLUID_DEFINITIONS.remove(id);
    }

    public void modify(ResourceLocation id, Consumer<BedrockFluidDefinition> consumer) {
        consumer.accept(GTRegistries.BEDROCK_FLUID_DEFINITIONS.get(id));
    }
}

package com.gregtechceu.gtceu.integration.kjs.builders;

import com.gregtechceu.gtceu.api.registry.registrate.BuilderBase;
import com.gregtechceu.gtceu.api.worldgen.IWorldGenLayer;
import com.gregtechceu.gtceu.api.worldgen.SimpleWorldGenLayer;

import net.minecraft.resources.ResourceLocation;

import dev.latvian.mods.kubejs.level.ruletest.AnyMatchRuleTest;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Accessors(fluent = true, chain = true)
public class WorldGenLayerBuilder extends BuilderBase<SimpleWorldGenLayer> {

    public transient List<IWorldGenLayer.RuleTestSupplier> targets = new ObjectArrayList<>();
    public transient List<ResourceLocation> dimensions = new ObjectArrayList<>();

    public WorldGenLayerBuilder(ResourceLocation id, Object... args) {
        super(id, args);
    }

    @Override
    public SimpleWorldGenLayer register() {
        this.value = new SimpleWorldGenLayer(
                this.id.getPath(),
                () -> new AnyMatchRuleTest(targets.stream().map(IWorldGenLayer.RuleTestSupplier::get).toList()),
                Set.copyOf(dimensions));
        return value;
    }

    public WorldGenLayerBuilder targets(IWorldGenLayer.RuleTestSupplier... targets) {
        Collections.addAll(this.targets, targets);
        return this;
    }

    public WorldGenLayerBuilder dimensions(ResourceLocation... dimension) {
        this.dimensions.addAll(Arrays.asList(dimension));
        return this;
    }
}

package com.gregtechceu.gtceu.data.worldgen;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.worldgen.BiomeWeightModifier;
import com.gregtechceu.gtceu.api.worldgen.modifier.BiomePlacement;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.block.GTBlocks;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

/**
 * @author KilaBash
 * @date 2023/3/26
 * @implNote GTPlacements
 */
public class GTPlacements {

    public static final ResourceKey<PlacedFeature> RUBBER_CHECKED = ResourceKey.create(Registries.PLACED_FEATURE,
            GTCEu.id("rubber_checked"));
    public static final ResourceKey<PlacedFeature> RED_GRANITE_BLOB = ResourceKey.create(Registries.PLACED_FEATURE,
            GTCEu.id("red_granite_blob"));
    public static final ResourceKey<PlacedFeature> MARBLE_BLOB = ResourceKey.create(Registries.PLACED_FEATURE,
            GTCEu.id("marble_blob"));

    public static void bootstrap(BootstrapContext<PlacedFeature> ctx) {
        HolderGetter<ConfiguredFeature<?, ?>> featureLookup = ctx.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<Biome> biomeLookup = ctx.lookup(Registries.BIOME);

        PlacementUtils.register(ctx, RUBBER_CHECKED, featureLookup.getOrThrow(GTConfiguredFeatures.RUBBER),
                new BiomePlacement(List.of(
                        new BiomeWeightModifier(() -> biomeLookup.getOrThrow(CustomTags.IS_SWAMP), 50))),
                PlacementUtils.countExtra(0, ConfigHolder.INSTANCE.worldgen.rubberTreeSpawnChance, 1),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome(),
                PlacementUtils.filteredByBlockSurvival(GTBlocks.RUBBER_SAPLING.get()));

        PlacementUtils.register(ctx, RED_GRANITE_BLOB, featureLookup.getOrThrow(GTConfiguredFeatures.RED_GRANITE_BLOB),
                RarityFilter.onAverageOnceEvery(10),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-8), VerticalAnchor.top()));
        PlacementUtils.register(ctx, MARBLE_BLOB, featureLookup.getOrThrow(GTConfiguredFeatures.MARBLE_BLOB),
                RarityFilter.onAverageOnceEvery(10),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-8), VerticalAnchor.top()));
    }
}

package com.gregtechceu.gtceu.data.tag;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BiomeTagsLoader extends BiomeTagsProvider {

    public BiomeTagsLoader(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture,
                           @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, GTCEu.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CustomTags.HAS_RUBBER_TREE).addTag(Tags.Biomes.IS_SWAMP).addTag(BiomeTags.IS_FOREST)
                .addTag(BiomeTags.IS_JUNGLE);
    }
}

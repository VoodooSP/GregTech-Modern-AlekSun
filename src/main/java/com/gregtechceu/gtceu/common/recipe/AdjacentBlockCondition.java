package com.gregtechceu.gtceu.common.recipe;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.data.recipe.GTRecipeConditions;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class AdjacentBlockCondition extends RecipeCondition {

    public static final MapCodec<AdjacentBlockCondition> CODEC = RecordCodecBuilder
            .mapCodec(instance -> RecipeCondition.isReverse(instance)
                    .apply(instance, AdjacentBlockCondition::new));
    public final static AdjacentBlockCondition INSTANCE = new AdjacentBlockCondition();

    public AdjacentBlockCondition(boolean isReverse) {
        super(isReverse);
    }

    @Override
    public RecipeConditionType<?> getType() {
        return GTRecipeConditions.ADJACENT_BLOCK;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.adjacent_block.tooltip");
    }

    @Override
    public boolean test(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        var blockA = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(recipe.data.getString("blockA")));
        var blockB = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(recipe.data.getString("blockB")));
        boolean hasBlockA = false, hasBlockB = false;
        var level = recipeLogic.machine.self().getLevel();
        var pos = recipeLogic.machine.self().getPos();
        for (Direction side : GTUtil.DIRECTIONS) {
            if (side.getAxis() != Direction.Axis.Y) {
                var block = level.getBlockState(pos.relative(side));
                if (block.getBlock() == blockA) hasBlockA = true;
                if (block.getBlock() == blockB) hasBlockB = true;
                if (hasBlockA && hasBlockB) return true;
            }
        }
        return false;
    }

    @Override
    public RecipeCondition createTemplate() {
        return new AdjacentBlockCondition();
    }
}

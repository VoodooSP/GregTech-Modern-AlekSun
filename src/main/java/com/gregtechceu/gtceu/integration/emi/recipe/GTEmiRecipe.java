package com.gregtechceu.gtceu.integration.emi.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.integration.GTRecipeWidget;

import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import org.jetbrains.annotations.Nullable;

public class GTEmiRecipe extends ModularEmiRecipe<WidgetGroup> {

    final GTRecipeTypeEmiCategory category;
    final RecipeHolder<GTRecipe> recipe;

    public GTEmiRecipe(GTRecipeTypeEmiCategory category, RecipeHolder<GTRecipe> recipe) {
        super(() -> new GTRecipeWidget(recipe));
        this.category = category;
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe.id();
    }
}

package com.gregtechceu.gtceu.integration.emi.oreprocessing;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.material.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

import java.util.ArrayList;
import java.util.List;

import static com.gregtechceu.gtceu.api.material.material.properties.PropertyKey.ORE;
import static com.gregtechceu.gtceu.data.recipe.GTRecipeTypes.*;

public class GTOreProcessingEmiCategory extends EmiRecipeCategory {

    public static final GTOreProcessingEmiCategory CATEGORY = new GTOreProcessingEmiCategory();

    public GTOreProcessingEmiCategory() {
        super(GTCEu.id("ore_processing_diagram"), EmiStack.of(Items.IRON_ORE));
    }

    public static void registerDisplays(EmiRegistry registry) {
        for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (mat.hasProperty(ORE) && !mat.hasFlag(MaterialFlags.NO_ORE_PROCESSING_TAB)) {
                registry.addRecipe(new GTEmiOreProcessing(mat));
            }
        }
    }

    public static void registerWorkStations(EmiRegistry registry) {
        List<MachineDefinition> registeredMachines = new ArrayList<>();
        GTRecipeType[] validTypes = new GTRecipeType[] {
                MACERATOR_RECIPES, ORE_WASHER_RECIPES, THERMAL_CENTRIFUGE_RECIPES, CENTRIFUGE_RECIPES,
                CHEMICAL_BATH_RECIPES, ELECTROMAGNETIC_SEPARATOR_RECIPES, SIFTER_RECIPES
        };
        for (MachineDefinition machine : GTRegistries.MACHINES) {
            if (machine.getRecipeTypes() != null) {
                for (GTRecipeType type : machine.getRecipeTypes()) {
                    for (GTRecipeType validType : validTypes) {
                        if (type == validType && !registeredMachines.contains(machine)) {
                            registry.addWorkstation(CATEGORY, EmiStack.of(machine.asStack()));
                            registeredMachines.add(machine);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Component getName() {
        return Component.translatable("gtceu.jei.ore_processing_diagram");
    }
}

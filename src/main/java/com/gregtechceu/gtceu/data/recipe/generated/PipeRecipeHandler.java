package com.gregtechceu.gtceu.data.recipe.generated;

import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.material.material.properties.FluidPipeProperties;
import com.gregtechceu.gtceu.api.material.material.properties.IMaterialProperty;
import com.gregtechceu.gtceu.api.material.material.properties.ItemPipeProperties;
import com.gregtechceu.gtceu.api.material.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.material.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.gregtechceu.gtceu.data.item.GTItems;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.NO_SMASHING;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.data.material.GTMaterials.Iron;
import static com.gregtechceu.gtceu.data.recipe.GTRecipeTypes.*;

public class PipeRecipeHandler {

    public static void init(RecipeOutput provider) {
        pipeTinyFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandler::processPipeTiny);
        pipeSmallFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandler::processPipeSmall);
        pipeNormalFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandler::processPipeNormal);
        pipeLargeFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandler::processPipeLarge);
        pipeHugeFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandler::processPipeHuge);

        pipeQuadrupleFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandler::processPipeQuadruple);
        pipeNonupleFluid.executeHandler(provider, PropertyKey.FLUID_PIPE, PipeRecipeHandler::processPipeNonuple);

        pipeSmallItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandler::processPipeSmall);
        pipeNormalItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandler::processPipeNormal);
        pipeLargeItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandler::processPipeLarge);
        pipeHugeItem.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandler::processPipeHuge);

        pipeSmallRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandler::processRestrictivePipe);
        pipeNormalRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE,
                PipeRecipeHandler::processRestrictivePipe);
        pipeLargeRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandler::processRestrictivePipe);
        pipeHugeRestrictive.executeHandler(provider, PropertyKey.ITEM_PIPE, PipeRecipeHandler::processRestrictivePipe);
    }

    private static void processRestrictivePipe(TagPrefix pipePrefix, Material material, ItemPipeProperties property,
                                               RecipeOutput provider) {
        TagPrefix unrestrictive;
        if (pipePrefix == pipeSmallRestrictive) unrestrictive = pipeSmallItem;
        else if (pipePrefix == pipeNormalRestrictive) unrestrictive = pipeNormalItem;
        else if (pipePrefix == pipeLargeRestrictive) unrestrictive = pipeLargeItem;
        else if (pipePrefix == pipeHugeRestrictive) unrestrictive = pipeHugeItem;
        else return;

        ASSEMBLER_RECIPES.recipeBuilder("assemble_" + material.getName() + "_" + pipePrefix.name)
                .inputItems(unrestrictive, material)
                .inputItems(ring, Iron, 2)
                .outputItems(pipePrefix, material)
                .duration(20)
                .EUt(VA[ULV])
                .save(provider);

        VanillaRecipeHelper.addShapedRecipe(provider,
                FormattingUtil.toLowerCaseUnder(pipePrefix.toString() + "_" + material.getName()),
                ChemicalHelper.get(pipePrefix, material), "PR", "Rh",
                'P', new UnificationEntry(unrestrictive, material), 'R', ChemicalHelper.get(ring, Iron));
    }

    private static void processPipeTiny(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                        RecipeOutput provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_tiny_pipe")
                .inputItems(ingot, material, 1)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_TINY)
                .outputItems(GTUtil.copyAmount(2, pipeStack))
                .duration((int) (material.getMass()))
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_tiny_pipe_dust")
                    .inputItems(dust, material, 1)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_TINY)
                    .outputItems(GTUtil.copyAmount(2, pipeStack))
                    .duration((int) (material.getMass()))
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("tiny_%s_pipe", material.getName()),
                    GTUtil.copyAmount(2, pipeStack), " s ", "hXw",
                    'X', new UnificationEntry(plate, material));
        }
    }

    private static void processPipeSmall(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                         RecipeOutput provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_small_pipe")
                .inputItems(ingot, material, 1)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_SMALL)
                .outputItems(pipeStack)
                .duration((int) (material.getMass()))
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_small_pipe_dust")
                    .inputItems(dust, material, 1)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_SMALL)
                    .outputItems(pipeStack)
                    .duration((int) (material.getMass()))
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("small_%s_pipe", material.getName()),
                    pipeStack, "wXh",
                    'X', new UnificationEntry(plate, material));
        }
    }

    private static void processPipeNormal(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                          RecipeOutput provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_pipe")
                .inputItems(ingot, material, 3)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_NORMAL)
                .outputItems(pipeStack)
                .duration((int) material.getMass() * 3)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_pipe_dust")
                    .inputItems(dust, material, 3)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_NORMAL)
                    .outputItems(pipeStack)
                    .duration((int) material.getMass() * 3)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("medium_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h",
                    'X', new UnificationEntry(plate, material));
        }
    }

    private static void processPipeLarge(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                         RecipeOutput provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_large_pipe")
                .inputItems(ingot, material, 6)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_LARGE)
                .outputItems(pipeStack)
                .duration((int) material.getMass() * 6)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_large_pipe_dust")
                    .inputItems(dust, material, 6)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_LARGE)
                    .outputItems(pipeStack)
                    .duration((int) material.getMass() * 6)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("large_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h", "XXX",
                    'X', new UnificationEntry(plate, material));
        }
    }

    private static void processPipeHuge(TagPrefix pipePrefix, Material material, IMaterialProperty<?> property,
                                        RecipeOutput provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack pipeStack = ChemicalHelper.get(pipePrefix, material);
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_huge_pipe")
                .inputItems(ingot, material, 12)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_HUGE)
                .outputItems(pipeStack)
                .duration((int) material.getMass() * 24)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_huge_pipe_dust")
                    .inputItems(dust, material, 12)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_PIPE_HUGE)
                    .outputItems(pipeStack)
                    .duration((int) material.getMass() * 24)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        } else if (plateDouble.doGenerateItem(material)) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("huge_%s_pipe", material.getName()),
                    pipeStack, "XXX", "w h", "XXX",
                    'X', new UnificationEntry(plateDouble, material));
        }
    }

    private static void processPipeQuadruple(TagPrefix pipePrefix, Material material, FluidPipeProperties property,
                                             RecipeOutput provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack smallPipe = ChemicalHelper.get(pipeSmallFluid, material);
        ItemStack quadPipe = ChemicalHelper.get(pipePrefix, material);
        VanillaRecipeHelper.addShapedRecipe(provider, String.format("quadruple_%s_pipe", material.getName()),
                quadPipe, "XX", "XX",
                'X', smallPipe);

        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_quadruple_pipe")
                .inputItems(GTUtil.copyAmount(4, smallPipe))
                .circuitMeta(4)
                .outputItems(quadPipe)
                .duration(30)
                .EUt(VA[ULV])
                .save(provider);
    }

    private static void processPipeNonuple(TagPrefix pipePrefix, Material material, FluidPipeProperties property,
                                           RecipeOutput provider) {
        if (material.hasProperty(PropertyKey.WOOD)) return;
        ItemStack smallPipe = ChemicalHelper.get(pipeSmallFluid, material);
        ItemStack nonuplePipe = ChemicalHelper.get(pipePrefix, material);
        VanillaRecipeHelper.addShapedRecipe(provider, String.format("nonuple_%s_pipe", material.getName()),
                nonuplePipe, "XXX", "XXX", "XXX",
                'X', smallPipe);

        PACKER_RECIPES.recipeBuilder("package_" + material.getName() + "_nonuple_pipe")
                .inputItems(GTUtil.copyAmount(9, smallPipe))
                .circuitMeta(9)
                .outputItems(nonuplePipe)
                .duration(40)
                .EUt(VA[ULV])
                .save(provider);
    }

    private static int getVoltageMultiplier(Material material) {
        return material.getBlastTemperature() >= 2800 ? VA[LV] : VA[ULV];
    }
}

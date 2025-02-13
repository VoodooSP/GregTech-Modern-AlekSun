package com.gregtechceu.gtceu.data.material;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.material.material.properties.BlastProperty.GasTier;

import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.data.material.GTMaterials.*;

public class GCyMMaterials {

    public static void register() {
        TantalumCarbide = new Material.Builder(GTCEu.id("tantalum_carbide"))
                .ingot(4).fluid()
                .color(0x999900).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Tantalum, 1, Carbon, 1)
                .blastTemp(4120, GasTier.MID, GTValues.VA[GTValues.EV], 1200)
                .buildAndRegister();

        HSLASteel = new Material.Builder(GTCEu.id("hsla_steel"))
                .ingot(3).fluid()
                .color(0x686868).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_SPRING)
                .components(Invar, 2, Vanadium, 1, Titanium, 1, Molybdenum, 1)
                .blastTemp(1711, GasTier.LOW, GTValues.VA[GTValues.HV], 1000)
                .buildAndRegister();

        MolybdenumDisilicide = new Material.Builder(GTCEu.id("molybdenum_disilicide"))
                .ingot(2).fluid()
                .color(0x564A84).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_SPRING, GENERATE_RING, GENERATE_PLATE, GENERATE_LONG_ROD)
                .components(Molybdenum, 1, Silicon, 2)
                .blastTemp(2300, GasTier.MID, GTValues.VA[GTValues.EV], 800)
                .buildAndRegister();

        Zeron100 = new Material.Builder(GTCEu.id("zeron_100"))
                .ingot(5).fluid()
                .color(0x294972).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Iron, 10, Nickel, 2, Tungsten, 2, Niobium, 1, Cobalt, 1)
                .blastTemp(3693, GasTier.MID, GTValues.VA[GTValues.EV], 1000)
                .buildAndRegister();

        WatertightSteel = new Material.Builder(GTCEu.id("watertight_steel"))
                .ingot(4).fluid()
                .color(0x2B4B56).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME)
                .components(Iron, 7, Aluminium, 4, Nickel, 2, Chromium, 1, Sulfur, 1)
                .blastTemp(3850, GasTier.MID, GTValues.VA[GTValues.EV], 800)
                .buildAndRegister();

        IncoloyMA956 = new Material.Builder(GTCEu.id("incoloy_ma_956"))
                .ingot(5).fluid()
                .color(0x2D9B66).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME)
                .components(VanadiumSteel, 4, Manganese, 2, Aluminium, 5, Yttrium, 2)
                .blastTemp(3652, GasTier.MID, GTValues.VA[GTValues.EV], 800)
                .buildAndRegister();

        MaragingSteel300 = new Material.Builder(GTCEu.id("maraging_steel_300"))
                .ingot(4).fluid()
                .color(0x505B6E).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_ROD, GENERATE_FRAME)
                .components(Iron, 16, Titanium, 1, Aluminium, 1, Nickel, 4, Cobalt, 2)
                .blastTemp(4000, GasTier.HIGH, GTValues.VA[GTValues.EV], 1000)
                .buildAndRegister();

        HastelloyX = new Material.Builder(GTCEu.id("hastelloy_x"))
                .ingot(5).fluid()
                .color(0x5784B8).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_FRAME)
                .components(Nickel, 8, Iron, 3, Tungsten, 4, Molybdenum, 2, Chromium, 1, Niobium, 1)
                .blastTemp(4200, GasTier.HIGH, GTValues.VA[GTValues.EV], 900)
                .buildAndRegister();

        Stellite100 = new Material.Builder(GTCEu.id("stellite_100"))
                .ingot(4).fluid()
                .color(0xCFCFEE).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Iron, 4, Chromium, 3, Tungsten, 2, Molybdenum, 1)
                .blastTemp(3790, GasTier.HIGH, GTValues.VA[GTValues.EV], 1000)
                .buildAndRegister();

        TitaniumCarbide = new Material.Builder(GTCEu.id("titanium_carbide"))
                .ingot(3).fluid()
                .color(0x90092F).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Titanium, 1, Carbon, 1)
                .blastTemp(3430, GasTier.MID, GTValues.VA[GTValues.EV], 1000)
                .buildAndRegister();

        TitaniumTungstenCarbide = new Material.Builder(GTCEu.id("titanium_tungsten_carbide"))
                .ingot(6).fluid()
                .color(0x680B0B).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(TitaniumCarbide, 2, TungstenCarbide, 1)
                .blastTemp(3800, GasTier.HIGH, GTValues.VA[GTValues.EV], 1000)
                .buildAndRegister();

        HastelloyC276 = new Material.Builder(GTCEu.id("hastelloy_c_276"))
                .ingot(6).fluid()
                .color(0xAB2F2F).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_FRAME)
                .components(Nickel, 12, Molybdenum, 8, Chromium, 7, Tungsten, 1, Cobalt, 1, Copper, 1)
                .blastTemp(3800, GasTier.HIGH, GTValues.VA[GTValues.EV], 1000)
                .buildAndRegister();
    }
}

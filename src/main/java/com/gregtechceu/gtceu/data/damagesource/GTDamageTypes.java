package com.gregtechceu.gtceu.data.damagesource;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.damagesource.DamageTypeData;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

/**
 * @author KilaBash
 * @date 2023/7/19
 * @implNote GTDamageTypes
 */
public class GTDamageTypes {

    public static final DamageTypeData EXPLOSION = new DamageTypeData.Builder()
            .simpleId("explosion")
            .tag(DamageTypeTags.IS_EXPLOSION)
            .build();
    public static final DamageTypeData HEAT = new DamageTypeData.Builder()
            .simpleId("heat")
            .tag(DamageTypeTags.IS_FIRE, DamageTypeTags.BYPASSES_ARMOR)
            .build();
    public static final DamageTypeData CHEMICAL = new DamageTypeData.Builder()
            .simpleId("chemical")
            .tag(DamageTypeTags.BYPASSES_ARMOR)
            .build();
    public static final DamageTypeData ELECTRIC = new DamageTypeData.Builder()
            .simpleId("electric")
            .tag(DamageTypeTags.IS_LIGHTNING)
            .build();
    public static final DamageTypeData RADIATION = new DamageTypeData.Builder()
            .simpleId("radiation")
            .tag(DamageTypeTags.BYPASSES_ARMOR)
            .build();
    public static final DamageTypeData TURBINE = new DamageTypeData.Builder()
            .simpleId("turbine")
            .tag(DamageTypeTags.BYPASSES_ARMOR)
            .build();

    public static void init() {}

    public static void bootstrap(BootstrapContext<DamageType> ctx) {
        DamageTypeData.allInNamespace(GTCEu.MOD_ID).forEach(data -> data.register(ctx));
    }
}

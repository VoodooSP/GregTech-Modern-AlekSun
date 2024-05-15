package com.gregtechceu.gtceu.api.capability;

import com.gregtechceu.gtceu.config.ConfigHolder;

import net.neoforged.neoforge.energy.IEnergyStorage;

public class FeCompat {

    /**
     * Conversion ratio used by energy converters
     */
    public static int ratio(boolean nativeToEu) {
        return nativeToEu ? ConfigHolder.INSTANCE.compat.energy.feToEuRatio :
                ConfigHolder.INSTANCE.compat.energy.euToFeRatio;
    }

    /**
     * Converts eu to native energy, using specified ratio
     * 
     * @return amount of native energy
     */
    public static int toFe(long eu, int ratio) {
        return (int) toFeLong(eu, ratio);
    }

    /**
     * Converts eu to native energy, using specified ratio, and returns as a long.
     * Can be used for overflow protection.
     * 
     * @return amount of native energy
     */
    public static long toFeLong(long eu, int ratio) {
        return eu * ratio;
    }

    /**
     * Converts eu to native energy, using a specified ratio, and with a specified upper bound.
     * This can be useful for dealing with int-overflows when converting from a long to an int.
     * 
     * @return amount of native energy
     */
    public static long toFeBounded(long eu, int ratio, int max) {
        return Math.min(max, toFeLong(eu, ratio));
    }

    /**
     * Converts native energy to eu, using specified ratio
     * 
     * @return amount of eu
     */
    public static long toEu(long nat, int ratio) {
        return nat / ratio;
    }

    /**
     * Inserts energy to the storage. EU -> FE conversion is performed.
     * 
     * @return amount of EU inserted
     */
    public static long insertEu(IEnergyStorage storage, long amountEU) {
        int euToNativeRatio = ratio(false);
        long nativeSent = storage.receiveEnergy(toFe(amountEU, euToNativeRatio), true);
        return toEu(storage.receiveEnergy((int) (nativeSent - (nativeSent % euToNativeRatio)), false), euToNativeRatio);
    }

    /**
     * Extracts energy from the storage. EU -> FE conversion is performed.
     * 
     * @return amount of EU extracted
     */
    public static long extractEu(IEnergyStorage storage, long amountEU) {
        int euToNativeRatio = ratio(false);
        long extract = storage.extractEnergy(toFe(amountEU, euToNativeRatio), true);
        return toEu(storage.extractEnergy((int) (extract - (extract % euToNativeRatio)), false), euToNativeRatio);
    }
}

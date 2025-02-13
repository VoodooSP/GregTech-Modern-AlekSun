package com.gregtechceu.gtceu.data.datagen;

import com.gregtechceu.gtceu.common.registry.GTRegistration;
import com.gregtechceu.gtceu.data.lang.LangHandler;
import com.gregtechceu.gtceu.data.tag.TagsHandler;

import com.tterrag.registrate.providers.ProviderType;

/**
 * @author KilaBash
 * @date 2023/3/19
 * @implNote GTRegistrateDatagen
 */
public class GTRegistrateDatagen {

    public static void init() {
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagsHandler::initItem);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagsHandler::initBlock);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, TagsHandler::initFluid);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, TagsHandler::initEntity);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::init);
    }
}

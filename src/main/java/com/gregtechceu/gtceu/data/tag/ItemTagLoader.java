package com.gregtechceu.gtceu.data.tag;

import com.gregtechceu.gtceu.api.material.ChemicalHelper;
import com.gregtechceu.gtceu.api.material.material.MarkerMaterials.Color;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import com.gregtechceu.gtceu.data.item.GTItems;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import com.tterrag.registrate.providers.RegistrateTagsProvider;

import static com.gregtechceu.gtceu.api.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.data.material.GTMaterials.*;

public class ItemTagLoader {

    public static void init(RegistrateTagsProvider<Item> provider) {
        create(provider, lens, Color.White, GTItems.MATERIAL_ITEMS.get(lens, Glass).get(),
                GTItems.MATERIAL_ITEMS.get(lens, NetherStar).get());
        create(provider, lens, Color.LightBlue, GTItems.MATERIAL_ITEMS.get(lens, Diamond).get());
        create(provider, lens, Color.Red, GTItems.MATERIAL_ITEMS.get(lens, Ruby).get());
        create(provider, lens, Color.Green, GTItems.MATERIAL_ITEMS.get(lens, Emerald).get());
        create(provider, lens, Color.Blue, GTItems.MATERIAL_ITEMS.get(lens, Sapphire).get());
        create(provider, lens, Color.Purple, GTItems.MATERIAL_ITEMS.get(lens, Amethyst).get());

        create(provider, CustomTags.PISTONS, Items.PISTON, Items.STICKY_PISTON);

        create(provider, dye, Color.Brown, GTItems.MATERIAL_ITEMS.get(dust, MetalMixture).get());

        // add treated wood stick to vanilla sticks tag
        // noinspection DataFlowIssue ChemicalHelper#getTag can't return null with treated wood rod
        provider.addTag(Tags.Items.RODS_WOODEN)
                .add(TagEntry.element(GTItems.MATERIAL_ITEMS.get(TagPrefix.rod, TreatedWood).getId()));

        // todo match ae2 certus quartz tag
        // OreDictionary.registerUnificationEntry("crystalCertusQuartz", ChemicalHelper.get(TagPrefix.gem,
        // GTMaterials.CertusQuartz));

        // add treated and untreated wood plates to vanilla planks tag
        provider.addTag(ItemTags.PLANKS)
                .add(TagEntry.element(GTItems.MATERIAL_ITEMS.get(plate, TreatedWood).getId()))
                .add(TagEntry.element(GTItems.MATERIAL_ITEMS.get(plate, Wood).getId()));

        provider.addTag(CustomTags.CIRCUITS)
                .addTag(CustomTags.ULV_CIRCUITS)
                .addTag(CustomTags.LV_CIRCUITS)
                .addTag(CustomTags.MV_CIRCUITS)
                .addTag(CustomTags.HV_CIRCUITS)
                .addTag(CustomTags.EV_CIRCUITS)
                .addTag(CustomTags.IV_CIRCUITS)
                .addTag(CustomTags.LuV_CIRCUITS)
                .addTag(CustomTags.ZPM_CIRCUITS)
                .addTag(CustomTags.UV_CIRCUITS)
                .addTag(CustomTags.UHV_CIRCUITS)
                .addOptionalTag(CustomTags.UEV_CIRCUITS.location())
                .addOptionalTag(CustomTags.UIV_CIRCUITS.location())
                .addOptionalTag(CustomTags.UXV_CIRCUITS.location())
                .addOptionalTag(CustomTags.OpV_CIRCUITS.location())
                .addOptionalTag(CustomTags.MAX_CIRCUITS.location());

        provider.addTag(CustomTags.BATTERIES)
                .addTag(CustomTags.ULV_BATTERIES)
                .addTag(CustomTags.LV_BATTERIES)
                .addTag(CustomTags.MV_BATTERIES)
                .addTag(CustomTags.HV_BATTERIES)
                .addTag(CustomTags.EV_BATTERIES)
                .addTag(CustomTags.IV_BATTERIES)
                .addTag(CustomTags.LuV_BATTERIES)
                .addTag(CustomTags.ZPM_BATTERIES)
                .addTag(CustomTags.UV_BATTERIES)
                .addTag(CustomTags.UHV_BATTERIES);
    }

    private static void create(RegistrateTagsProvider<Item> provider, TagPrefix prefix, Material material,
                               Item... rls) {
        create(provider, ChemicalHelper.getTag(prefix, material), rls);
    }

    public static void create(RegistrateTagsProvider<Item> provider, TagKey<Item> tagKey, ResourceLocation... rls) {
        var builder = provider.addTag(tagKey);
        for (ResourceLocation rl : rls) {
            builder.addOptional(rl);
        }
    }

    public static void create(RegistrateTagsProvider<Item> provider, TagKey<Item> tagKey, Item... rls) {
        var builder = provider.addTag(tagKey);
        for (Item item : rls) {
            builder.add(BuiltInRegistries.ITEM.getResourceKey(item).get());
        }
    }

    private static ResourceLocation rl(String name) {
        return ResourceLocation.parse(name);
    }
}

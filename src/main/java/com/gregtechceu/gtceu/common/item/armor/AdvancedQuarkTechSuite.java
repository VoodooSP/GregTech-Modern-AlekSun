package com.gregtechceu.gtceu.common.item.armor;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IElectricItem;
import com.gregtechceu.gtceu.api.item.armor.ArmorComponentItem;
import com.gregtechceu.gtceu.api.item.armor.ArmorUtils;
import com.gregtechceu.gtceu.api.item.datacomponents.GTArmor;
import com.gregtechceu.gtceu.data.tag.GTDataComponents;
import com.gregtechceu.gtceu.utils.input.KeyBind;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class AdvancedQuarkTechSuite extends QuarkTechSuite implements IJetpack {

    // A replacement for checking the current world time, to get around the gamerule that stops it
    private long timer = 0L;
    private List<Pair<NonNullList<ItemStack>, List<Integer>>> inventoryIndexMap;

    public AdvancedQuarkTechSuite(int energyPerUse, long capacity, int tier) {
        super(ArmorItem.Type.CHESTPLATE, energyPerUse, capacity, tier);
    }

    @Override
    public void onArmorTick(Level world, Player player, ItemStack item) {
        IElectricItem cont = GTCapabilityHelper.getElectricItem(item);
        if (cont == null) {
            return;
        }

        GTArmor data = item.getOrDefault(GTDataComponents.ARMOR_DATA, new GTArmor());
        boolean hoverMode = data.hover();
        byte toggleTimer = data.toggleTimer();
        boolean canShare = data.canShare();
        boolean jetpackEnabled = data.enabled();

        String messageKey = null;
        if (toggleTimer == 0) {
            if (KeyBind.JETPACK_ENABLE.isKeyDown(player)) {
                jetpackEnabled = !jetpackEnabled;
                messageKey = "metaarmor.jetpack.flight." + (jetpackEnabled ? "enable" : "disable");
                final boolean finalJetpackEnabled = jetpackEnabled;
                item.update(GTDataComponents.ARMOR_DATA, new GTArmor(), data1 -> data1.setEnabled(finalJetpackEnabled));
            } else if (KeyBind.ARMOR_HOVER.isKeyDown(player)) {
                hoverMode = !hoverMode;
                messageKey = "metaarmor.jetpack.hover." + (hoverMode ? "enable" : "disable");
                final boolean finalHoverMode = hoverMode;
                item.update(GTDataComponents.ARMOR_DATA, new GTArmor(), data1 -> data1.setHover(finalHoverMode));
            } else if (KeyBind.ARMOR_CHARGING.isKeyDown(player)) {
                canShare = !canShare;
                if (canShare && cont.getCharge() == 0) { // Only allow for charging to be enabled if charge is nonzero
                    messageKey = "metaarmor.qts.share.error";
                    canShare = false;
                } else {
                    messageKey = "metaarmor.qts.share." + (canShare ? "enable" : "disable");
                }
                final boolean finalCanShare = canShare;
                item.update(GTDataComponents.ARMOR_DATA, new GTArmor(), data1 -> data1.setCanShare(finalCanShare));
            }

            if (messageKey != null) {
                toggleTimer = 5;
                if (!world.isClientSide) player.displayClientMessage(Component.translatable(messageKey), true);
            }
        }

        if (toggleTimer > 0) toggleTimer--;

        final boolean finalCanShare = canShare;
        final boolean finalHoverMode = hoverMode;
        final byte finalToggleTimer = toggleTimer;
        final boolean finalJetpackEnabled = jetpackEnabled;
        item.update(GTDataComponents.ARMOR_DATA, new GTArmor(),
                data1 -> data1.setCanShare(finalCanShare)
                        .setHover(finalHoverMode)
                        .setToggleTimer(finalToggleTimer)
                        .setEnabled(finalJetpackEnabled));

        performFlying(player, jetpackEnabled, hoverMode, item);

        if (player.isOnFire())
            player.extinguishFire();

        // Charging mechanics
        if (canShare && !world.isClientSide) {
            // Check for new things to charge every 5 seconds
            if (timer % 100 == 0)
                inventoryIndexMap = ArmorUtils.getChargeableItem(player, cont.getTier());

            if (inventoryIndexMap != null && !inventoryIndexMap.isEmpty()) {
                // Charge all inventory slots
                for (int i = 0; i < inventoryIndexMap.size(); i++) {
                    Pair<NonNullList<ItemStack>, List<Integer>> inventoryMap = inventoryIndexMap.get(i);
                    Iterator<Integer> inventoryIterator = inventoryMap.getSecond().iterator();
                    while (inventoryIterator.hasNext()) {
                        int slot = inventoryIterator.next();
                        IElectricItem chargable = GTCapabilityHelper.getElectricItem(inventoryMap.getFirst().get(slot));

                        // Safety check the null, it should not actually happen. Also don't try and charge itself
                        if (chargable == null || chargable == cont) {
                            inventoryIterator.remove();
                            continue;
                        }

                        long attemptedChargeAmount = chargable.getTransferLimit() * 10;

                        // Accounts for tick differences when charging items
                        if (chargable.getCharge() < chargable.getMaxCharge() && cont.canUse(attemptedChargeAmount) &&
                                timer % 10 == 0) {
                            long delta = chargable.charge(attemptedChargeAmount, cont.getTier(), true, false);
                            if (delta > 0) {
                                cont.discharge(delta, cont.getTier(), true, false, false);
                            }
                            if (chargable.getCharge() == chargable.getMaxCharge()) {
                                inventoryIterator.remove();
                            }
                            player.inventoryMenu.sendAllDataToRemote();
                        }
                    }

                    if (inventoryMap.getSecond().isEmpty())
                        inventoryIndexMap.remove(inventoryMap);
                }
            }
        }

        timer++;
        if (timer == Long.MAX_VALUE)
            timer = 0;
    }

    @Override
    public void addInfo(ItemStack itemStack, List<Component> lines) {
        GTArmor data = itemStack.getOrDefault(GTDataComponents.ARMOR_DATA, new GTArmor());

        Component state = data.enabled() ? Component.translatable("metaarmor.hud.status.enabled") :
                Component.translatable("metaarmor.hud.status.disabled");
        lines.add(Component.translatable("metaarmor.hud.engine_enabled", state));

        state = data.canShare() ? Component.translatable("metaarmor.hud.status.enabled") :
                Component.translatable("metaarmor.hud.status.disabled");
        lines.add(Component.translatable("metaarmor.energy_share.tooltip", state));
        lines.add(Component.translatable("metaarmor.energy_share.tooltip.guide"));

        state = data.hover() ? Component.translatable("metaarmor.hud.status.enabled") :
                Component.translatable("metaarmor.hud.status.disabled");
        lines.add(Component.translatable("metaarmor.hud.hover_mode", state));
        super.addInfo(itemStack, lines);
    }

    @Override
    public InteractionResultHolder<ItemStack> onRightClick(Level world, Player player, InteractionHand hand) {
        ItemStack armor = player.getItemInHand(hand);
        if (armor.getItem() instanceof ArmorComponentItem && player.isShiftKeyDown()) {
            GTArmor data = armor.getOrDefault(GTDataComponents.ARMOR_DATA, new GTArmor());
            boolean canShare = data.canShare();
            IElectricItem cont = GTCapabilityHelper.getElectricItem(armor);
            if (cont == null) {
                return InteractionResultHolder.fail(armor);
            }

            canShare = !canShare;
            if (!world.isClientSide) {
                if (canShare && cont.getCharge() == 0) {
                    player.sendSystemMessage(Component.translatable("metaarmor.energy_share.error"));
                } else if (canShare) {
                    player.sendSystemMessage(Component.translatable("metaarmor.energy_share.enable"));
                } else {
                    player.sendSystemMessage(Component.translatable("metaarmor.energy_share.disable"));
                }
            }

            canShare = canShare && (cont.getCharge() != 0);
            final boolean finalCanShare = canShare;
            armor.update(GTDataComponents.ARMOR_DATA, new GTArmor(), data1 -> data1.setCanShare(finalCanShare));
            return InteractionResultHolder.success(armor);
        } else {
            return super.onRightClick(world, player, hand);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawHUD(ItemStack item, GuiGraphics guiGraphics) {
        addCapacityHUD(item, this.HUD);
        IElectricItem cont = GTCapabilityHelper.getElectricItem(item);
        if (cont == null) return;
        if (!cont.canUse(energyPerUse)) return;
        GTArmor data = item.get(GTDataComponents.ARMOR_DATA);
        if (data != null) {
            Component status = data.enabled() ?
                    Component.translatable("metaarmor.hud.status.enabled") :
                    Component.translatable("metaarmor.hud.status.disabled");
            Component result = Component.translatable("metaarmor.hud.engine_enabled", status);
            this.HUD.newString(result);

            status = data.canShare() ?
                    Component.translatable("metaarmor.hud.status.enabled") :
                    Component.translatable("metaarmor.hud.status.disabled");
            this.HUD.newString(Component.translatable("mataarmor.hud.supply_mode", status));

            status = data.hover() ?
                    Component.translatable("metaarmor.hud.status.enabled") :
                    Component.translatable("metaarmor.hud.status.disabled");
            this.HUD.newString(Component.translatable("metaarmor.hud.hover_mode", status));
        }
        this.HUD.draw(guiGraphics);
        this.HUD.reset();
    }

    /*
     * @Override
     * public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source,
     * double damage, EntityEquipmentSlot equipmentSlot) {
     * int damageLimit = Integer.MAX_VALUE;
     * IElectricItem item = armor.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
     * if (item == null) {
     * return new ArmorProperties(0, 0, damageLimit);
     * }
     * if (energyPerUse > 0) {
     * damageLimit = (int) Math.min(damageLimit, 25.0D * item.getCharge() / (energyPerUse * 250.0D));
     * }
     * return new ArmorProperties(8, getDamageAbsorption() * getAbsorption(armor), damageLimit);
     * }
     *
     * @Override
     * public boolean handleUnblockableDamage(EntityLivingBase entity, @NotNull ItemStack armor, DamageSource source,
     * double damage, EntityEquipmentSlot equipmentSlot) {
     * return source != DamageSource.FALL && source != DamageSource.DROWN && source != DamageSource.STARVE;
     * }
     */

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot,
                                            ArmorMaterial.Layer layer) {
        return GTCEu.id("textures/armor/advanced_quark_tech_suite_1.png");
    }

    @Override
    public double getDamageAbsorption() {
        return 1.5D;
    }

    @Override
    public boolean canUseEnergy(@NotNull ItemStack stack, int amount) {
        IElectricItem container = getIElectricItem(stack);
        if (container == null)
            return false;
        return container.canUse(amount);
    }

    @Override
    public void drainEnergy(@NotNull ItemStack stack, int amount) {
        IElectricItem container = getIElectricItem(stack);
        if (container == null)
            return;
        container.discharge(amount, tier, true, false, false);
    }

    @Override
    public boolean hasEnergy(@NotNull ItemStack stack) {
        IElectricItem container = getIElectricItem(stack);
        if (container == null)
            return false;
        return container.getCharge() > 0;
    }

    private static IElectricItem getIElectricItem(@NotNull ItemStack stack) {
        return GTCapabilityHelper.getElectricItem(stack);
    }

    @Override
    public double getSprintEnergyModifier() {
        return 6.0D;
    }

    @Override
    public double getSprintSpeedModifier() {
        return 2.4D;
    }

    @Override
    public double getVerticalHoverSpeed() {
        return 0.45D;
    }

    @Override
    public double getVerticalHoverSlowSpeed() {
        return 0.0D;
    }

    @Override
    public double getVerticalAcceleration() {
        return 0.15D;
    }

    @Override
    public double getVerticalSpeed() {
        return 0.9D;
    }

    @Override
    public double getSidewaysSpeed() {
        return 0.21D;
    }

    @Override
    public ParticleOptions getParticle() {
        return null;
    }

    @Override
    public float getFallDamageReduction() {
        return 8f;
    }
}

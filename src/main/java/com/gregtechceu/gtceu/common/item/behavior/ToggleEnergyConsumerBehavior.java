package com.gregtechceu.gtceu.common.item.behavior;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IElectricItem;
import com.gregtechceu.gtceu.api.item.component.IAddInformation;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.item.component.IItemLifeCycle;
import com.gregtechceu.gtceu.data.tag.GTDataComponents;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ToggleEnergyConsumerBehavior implements IInteractionItem, IItemLifeCycle, IAddInformation {

    private final int energyUsagePerTick;

    public ToggleEnergyConsumerBehavior(int energyUsagePerTick) {
        this.energyUsagePerTick = energyUsagePerTick;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack item, Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            IElectricItem electricItem = GTCapabilityHelper.getElectricItem(itemStack);
            boolean isItemActive = isItemActive(itemStack);
            if (isItemActive) {
                setItemActive(itemStack, false);
            } else if (electricItem != null && drainActivationEnergy(electricItem, true)) {
                setItemActive(itemStack, true);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    private boolean drainActivationEnergy(IElectricItem electricItem, boolean simulate) {
        return electricItem.discharge(energyUsagePerTick, electricItem.getTier(), true, false, simulate) >=
                energyUsagePerTick;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        IElectricItem electricItem = GTCapabilityHelper.getElectricItem(stack);
        if (isItemActive(stack) && electricItem != null) {
            boolean shouldRemainActive = drainActivationEnergy(electricItem, false);
            if (!shouldRemainActive) {
                setItemActive(stack, false);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("behavior.toggle_energy_consumer.tooltip"));
    }

    public static boolean isItemActive(ItemStack itemStack) {
        return itemStack.getOrDefault(GTDataComponents.ACTIVE, false);
    }

    public static void setItemActive(ItemStack itemStack, boolean isActive) {
        itemStack.set(GTDataComponents.ACTIVE, isActive);
    }
}

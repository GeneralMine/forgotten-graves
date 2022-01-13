package me.mgin.graves.events;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketEnums.DropRule;
import me.mgin.graves.config.GravesConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class TrinketDropHandler {
	/**
	 * TrinketDropCallback handler; this method prevents Trinkets from handling item
	 * drops unless enableGraves is set to false.
	 *
	 * @param rule
	 * @param stack
	 * @param ref
	 * @param entity
	 * @return DropRule
	 */
	public static DropRule handleTrinketDrop(DropRule rule, ItemStack stack, SlotReference ref, LivingEntity entity) {
		// Prevent Trinkets from handling dropInventory
		if (GravesConfig.getConfig().mainSettings.enableGraves) {
			return DropRule.KEEP;
		}

		return rule;
	}
}

package me.mgin.graves.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import me.mgin.graves.api.GravesApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.event.GameEvent;

public class TrinketsCompat implements GravesApi {
	/**
	 * Retrieve a list containing items occupying the trinket slots.
	 *
	 * @param player
	 * @return List<ItemStack>
	 */
	@Override
	public List<ItemStack> getInventory(PlayerEntity player) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
		List<ItemStack> itemStacks = new ArrayList<>();

		if (component.isPresent()) {
			component.get().forEach((ref, itemStack) -> {
				itemStacks.add(itemStack);
			});
		}

		return itemStacks;
	}

	/**
	 * Equips all items within a list of ItemStacks into the trinket slots.
	 *
	 * @param inventory
	 *            - List<ItemStack>
	 * @param player
	 */
	@Override
	public void setInventory(List<ItemStack> inventory, PlayerEntity player) {
		for (int i = 0; i < inventory.size(); i++) {
			equipItem(inventory.get(i), player, i);
		}
	}

	/**
	 * Retrieve the amount of trinket slots available.
	 *
	 * @param player
	 * @return int
	 */
	@Override
	public int getInventorySize(PlayerEntity player) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
		var slotWrapper = new Object() {
			int slots = 0;
		};

		if (component.isPresent())
			component.get().forEach((ref, itemStack) -> {
				slotWrapper.slots++;
			});

		return slotWrapper.slots;
	}

	/**
	 * Remove all items from the trinket slots.
	 *
	 * @param player
	 */
	public static void clearInventory(PlayerEntity player) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);

		if (component.isPresent()) {
			component.get().forEach((ref, itemStack) -> {
				TrinketInventory inventory = ref.inventory();
				inventory.setStack(ref.index(), ItemStack.EMPTY);
			});
		}
	}

	/**
	 * Equips an item based on a given index; this is meant to be used with
	 * setInventory. The index is based on each TrinketSlot -- not group.
	 *
	 * @param item
	 * @param player
	 * @param index
	 */
	public static void equipItem(ItemStack item, PlayerEntity player, int index) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);

		if (component.isPresent()) {
			int currentSlot = 0;
			for (var group : component.get().getInventory().values()) {
				for (TrinketInventory inventory : group.values()) {
					for (int i = 0; i < inventory.size(); i++) {
						if (currentSlot == index) {
							SlotReference ref = new SlotReference(inventory, index);
							if (TrinketSlot.canInsert(item, ref, player)) {
								ItemStack newStack = item.copy();
								inventory.setStack(i, newStack);
								SoundEvent soundEvent = item.getEquipSound();
								if (!item.isEmpty() && soundEvent != null) {
									player.emitGameEvent(GameEvent.EQUIP);
									player.playSound(soundEvent, 1.0F, 1.0F);
								}
								item.setCount(0);
							}
						}
						currentSlot += 1;
					}
				}
			}
		}
	}
}

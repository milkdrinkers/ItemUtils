/*
 * MIT No Attribution
 *
 * Copyright 2025 darksaid98
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.milkdrinkers.itemutil;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * The {@link InventoryUtils} class provides utilities for
 * manipulating and checking inventories with both vanilla
 * and custom items from supported plugins.
 */
@SuppressWarnings("unused")
public final class InventoryUtils {
    private InventoryUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Checks if the inventory contains the specified custom item by item ID.
     *
     * @param inventory the inventory to check
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     * @return true if the inventory contains the item
     */
    public static boolean contains(@NotNull InventoryHolder inventory, @NotNull String itemId) {
        return contains(inventory, itemId, 1);
    }

    /**
     * Checks if the inventory contains at least the specified amount of the custom item.
     *
     * @param inventory the inventory to check
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     * @param amount    the minimum amount required
     * @return true if the inventory contains at least the specified amount
     */
    public static boolean contains(@NotNull InventoryHolder inventory, @NotNull String itemId, int amount) {
        return countItems(inventory, itemId) >= amount;
    }

    /**
     * Checks if the inventory contains the specified ItemStack.
     *
     * @param inventory the inventory to check
     * @param item      the item stack to check for
     * @return true if the inventory contains the item
     * @see Inventory#contains(ItemStack)
     */
    public static boolean contains(@NotNull InventoryHolder inventory, @NotNull ItemStack item) {
        return inventory.getInventory().contains(item);
    }

    /**
     * Checks if the inventory contains at least the specified amount of the ItemStack.
     *
     * @param inventory the inventory to check
     * @param item      the item stack to check for
     * @param amount    the minimum amount required
     * @return true if the inventory contains at least the specified amount
     * @see Inventory#contains(ItemStack, int)
     */
    public static boolean contains(@NotNull InventoryHolder inventory, @NotNull ItemStack item, int amount) {
        return inventory.getInventory().contains(item, amount);
    }

    /**
     * Checks if the inventory contains the specified material.
     *
     * @param inventory the inventory to check
     * @param material  the material to check for
     * @return true if the inventory contains the material
     * @see Inventory#contains(Material)
     */
    public static boolean contains(@NotNull InventoryHolder inventory, @NotNull Material material) {
        return inventory.getInventory().contains(material);
    }

    /**
     * Checks if the inventory contains at least the specified amount of the material.
     *
     * @param inventory the inventory to check
     * @param material  the material to check for
     * @param amount    the minimum amount required
     * @return true if the inventory contains at least the specified amount
     * @see Inventory#contains(Material, int)
     */
    public static boolean contains(@NotNull InventoryHolder inventory, @NotNull Material material, int amount) {
        return inventory.getInventory().contains(material, amount);
    }

    /**
     * Counts the total amount of the specified custom item in the inventory.
     *
     * @param inventory the inventory to check
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     * @return the total amount of the item
     */
    public static int countItems(@NotNull InventoryHolder inventory, @NotNull String itemId) {
        return Arrays.stream(inventory.getInventory().getContents())
            .filter(Objects::nonNull)
            .filter(item -> itemId.equals(ItemUtils.parse(item)))
            .mapToInt(ItemStack::getAmount)
            .sum();
    }

    /**
     * Counts the total amount of the specified ItemStack in the inventory.
     *
     * @param inventory the inventory to check
     * @param item      the item stack to count
     * @return the total amount of the item
     */
    public static int countItems(@NotNull InventoryHolder inventory, @NotNull ItemStack item) {
        return Arrays.stream(inventory.getInventory().getContents())
            .filter(Objects::nonNull)
            .filter(stack -> stack.isSimilar(item))
            .mapToInt(ItemStack::getAmount)
            .sum();
    }

    /**
     * Counts the total amount of the specified material in the inventory.
     *
     * @param inventory the inventory to check
     * @param material  the material to count
     * @return the total amount of the material
     */
    public static int countItems(@NotNull InventoryHolder inventory, @NotNull Material material) {
        return Arrays.stream(inventory.getInventory().getContents())
            .filter(Objects::nonNull)
            .filter(item -> item.getType() == material)
            .mapToInt(ItemStack::getAmount)
            .sum();
    }

    /**
     * Adds an item to the inventory by item ID.
     *
     * @param inventory the inventory to add to
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     * @param amount    the amount to add
     * @return a map of leftover items that couldn't fit
     * @see Inventory#addItem(ItemStack...)
     */
    public static @NotNull Map<Integer, ItemStack> addItem(@NotNull InventoryHolder inventory, @NotNull String itemId, int amount) {
        final Optional<ItemStack> optional = ItemUtils.parseOptional(itemId);
        if (optional.isEmpty())
            return new HashMap<>();

        final ItemStack item = optional.get();
        item.setAmount(amount);
        return inventory.getInventory().addItem(item);
    }

    /**
     * Adds multiple items to the inventory.
     *
     * @param inventory the inventory to add to
     * @param items     the items to add
     * @return a map of leftover items that couldn't fit
     * @see Inventory#addItem(ItemStack...)
     */
    public static @NotNull Map<Integer, ItemStack> addItem(@NotNull InventoryHolder inventory, @NotNull ItemStack... items) {
        return inventory.getInventory().addItem(items);
    }

    /**
     * Removes the specified amount of the custom item from the inventory.
     *
     * @param inventory the inventory to remove from
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     * @param amount    the amount to remove
     * @return a map of items that couldn't be removed (if insufficient quantity)
     * @see Inventory#removeItem(ItemStack...)
     */
    public static @NotNull Map<Integer, ItemStack> removeItem(@NotNull InventoryHolder inventory, @NotNull String itemId, int amount) {
        int remaining = amount;
        final ItemStack[] contents = inventory.getInventory().getContents();

        for (int i = 0; i < contents.length && remaining > 0; i++) {
            final ItemStack item = contents[i];
            if (item != null && itemId.equals(ItemUtils.parse(item))) {
                int itemAmount = item.getAmount();
                if (itemAmount <= remaining) {
                    remaining -= itemAmount;
                    inventory.getInventory().setItem(i, null);
                } else {
                    item.setAmount(itemAmount - remaining);
                    remaining = 0;
                }
            }
        }

        return new HashMap<>();
    }

    /**
     * Removes items from the inventory.
     *
     * @param inventory the inventory to remove from
     * @param items     the items to remove
     * @return a map of items that couldn't be removed
     * @see Inventory#removeItem(ItemStack...)
     */
    public static @NotNull Map<Integer, ItemStack> removeItem(@NotNull InventoryHolder inventory, @NotNull ItemStack... items) {
        return inventory.getInventory().removeItem(items);
    }

    /**
     * Gets the first empty slot index in the inventory.
     *
     * @param inventory the inventory to check
     * @return the first empty slot index, or -1 if no empty slots
     * @see Inventory#firstEmpty()
     */
    public static int firstEmpty(@NotNull InventoryHolder inventory) {
        return inventory.getInventory().firstEmpty();
    }

    /**
     * Gets the first slot index containing the specified custom item.
     *
     * @param inventory the inventory to check
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     * @param amount    the item amount to match
     * @return the first slot index containing the item, or -1 if not found
     * @apiNote This method will only match items that share the exact {@link Material} and item amount.
     * @see org.bukkit.inventory.Inventory#first(ItemStack)
     */
    public static int first(@NotNull InventoryHolder inventory, @NotNull String itemId, int amount) {
        final ItemStack[] contents = inventory.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            final ItemStack item = contents[i];
            if (item != null && itemId.equals(ItemUtils.parse(item)) && item.getAmount() == amount) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the first slot index containing the specified ItemStack.
     *
     * @param inventory the inventory to check
     * @param item      the item stack to find
     * @return the first slot index containing the item, or -1 if not found
     * @apiNote This method will only match items that share the exact {@link Material} and item amount.
     * @see org.bukkit.inventory.Inventory#first(ItemStack)
     */
    public static int first(@NotNull InventoryHolder inventory, @NotNull ItemStack item) {
        return inventory.getInventory().first(item);
    }

    /**
     * Gets the first slot index containing the specified material.
     *
     * @param inventory the inventory to check
     * @param material  the material to find
     * @return the first slot index containing the material, or -1 if not found
     * @apiNote This method will only match items that share the exact {@link Material} and item amount.
     * @see org.bukkit.inventory.Inventory#first(Material)
     */
    public static int first(@NotNull InventoryHolder inventory, @NotNull Material material) {
        return inventory.getInventory().first(material);
    }

    /**
     * Gets all slot indices containing the specified custom item.
     *
     * @param inventory the inventory to check
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     * @return a map of slot indices to ItemStacks
     * @see org.bukkit.inventory.Inventory#all(ItemStack)
     */
    public static @NotNull Map<Integer, ? extends ItemStack> all(@NotNull InventoryHolder inventory, @NotNull String itemId) {
        final Map<Integer, ItemStack> slots = new HashMap<>();
        final ItemStack[] contents = inventory.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            final ItemStack item = contents[i];
            if (item != null && itemId.equals(ItemUtils.parse(item))) {
                slots.put(i, item);
            }
        }
        return slots;
    }

    /**
     * Gets all slot indices containing the specified ItemStack.
     *
     * @param inventory the inventory to check
     * @param item      the item stack to find
     * @return a map of slot indices to ItemStacks
     * @see org.bukkit.inventory.Inventory#all(ItemStack)
     */
    public static @NotNull Map<Integer, ? extends ItemStack> all(@NotNull InventoryHolder inventory, @NotNull ItemStack item) {
        return inventory.getInventory().all(item);
    }

    /**
     * Gets all slot indices containing the specified material.
     *
     * @param inventory the inventory to check
     * @param material  the material to find
     * @return a map of slot indices to ItemStacks
     * @see org.bukkit.inventory.Inventory#all(Material)
     */
    public static @NotNull Map<Integer, ? extends ItemStack> all(@NotNull InventoryHolder inventory, @NotNull Material material) {
        return inventory.getInventory().all(material);
    }

    /**
     * Checks if the inventory is empty.
     *
     * @param inventory the inventory to check
     * @return true if the inventory is empty
     */
    public static boolean isEmpty(@NotNull InventoryHolder inventory) {
        return inventory.getInventory().isEmpty();
    }

    /**
     * Checks if the inventory is full.
     *
     * @param inventory the inventory to check
     * @return true if the inventory is full
     */
    public static boolean isFull(@NotNull InventoryHolder inventory) {
        return firstEmpty(inventory) == -1;
    }

    /**
     * Gets the number of empty slots in the inventory.
     *
     * @param inventory the inventory to check
     * @return the number of empty slots
     */
    public static int getEmptySlots(@NotNull InventoryHolder inventory) {
        return (int) Arrays.stream(inventory.getInventory().getContents())
            .filter(item -> item == null || item.getType() == Material.AIR)
            .count();
    }

    /**
     * Gets the number of occupied slots in the inventory.
     *
     * @param inventory the inventory to check
     * @return the number of occupied slots
     */
    public static int getOccupiedSlots(@NotNull InventoryHolder inventory) {
        return inventory.getInventory().getSize() - getEmptySlots(inventory);
    }

    /**
     * Clears the inventory completely.
     *
     * @param inventory the inventory to clear
     */
    public static void clear(@NotNull InventoryHolder inventory) {
        inventory.getInventory().clear();
    }

    /**
     * Clears all instances of the specified custom item from the inventory.
     *
     * @param inventory the inventory to clear from
     * @param itemId    the item id (supports namespaced custom items from item plugins like nexo, itemsadder, etc)
     */
    public static void clear(@NotNull InventoryHolder inventory, @NotNull String itemId) {
        final ItemStack[] contents = inventory.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            final ItemStack item = contents[i];
            if (item != null && itemId.equals(ItemUtils.parse(item))) {
                inventory.getInventory().clear(i);
            }
        }
    }

    /**
     * Clears all instances of the specified ItemStack from the inventory.
     *
     * @param inventory the inventory to clear from
     * @param item      the item stack to clear
     */
    public static void clear(@NotNull InventoryHolder inventory, @NotNull ItemStack item) {
        inventory.getInventory().remove(item);
    }

    /**
     * Clears all instances of the specified material from the inventory.
     *
     * @param inventory the inventory to clear from
     * @param material  the material to clear
     */
    public static void clear(@NotNull InventoryHolder inventory, @NotNull Material material) {
        inventory.getInventory().remove(material);
    }

    /**
     * Gets a copy of the inventory contents as a list.
     *
     * @param inventory the inventory to copy from
     * @return a list containing copies of all items
     */
    public static @NotNull List<ItemStack> getContents(@NotNull InventoryHolder inventory) {
        return Arrays.stream(inventory.getInventory().getContents())
            .filter(Objects::nonNull)
            .map(ItemStack::clone)
            .toList();
    }

    /**
     * Gets all non-null items from the inventory.
     *
     * @param inventory the inventory to get items from
     * @return a list of all non-null ItemStacks
     */
    public static @NotNull List<ItemStack> getNonNullContents(@NotNull InventoryHolder inventory) {
        return Arrays.stream(inventory.getInventory().getContents())
            .filter(Objects::nonNull)
            .filter(item -> item.getType() != Material.AIR)
            .toList();
    }

    /**
     * Finds items in the inventory matching the given predicate.
     *
     * @param inventory the inventory to search
     * @param predicate the condition to match
     * @return a list of matching ItemStacks
     */
    public static @NotNull List<ItemStack> findItems(@NotNull InventoryHolder inventory, @NotNull Predicate<ItemStack> predicate) {
        return Arrays.stream(inventory.getInventory().getContents())
            .filter(Objects::nonNull)
            .filter(predicate)
            .toList();
    }

    /**
     * Finds item slots in the inventory matching the given predicate.
     *
     * @param inventory the inventory to search
     * @param predicate the condition to match
     * @return a list of slot indices
     */
    public static @NotNull List<Integer> findSlots(@NotNull InventoryHolder inventory, @NotNull Predicate<ItemStack> predicate) {
        ItemStack[] contents = inventory.getInventory().getContents();
        return IntStream.range(0, contents.length)
            .filter(i -> contents[i] != null && predicate.test(contents[i]))
            .boxed()
            .toList();
    }

    /**
     * Swaps items between two slots in the inventory.
     *
     * @param inventory the inventory to modify
     * @param slot1     the first slot
     * @param slot2     the second slot
     */
    public static void swap(@NotNull InventoryHolder inventory, int slot1, int slot2) {
        final ItemStack item1 = inventory.getInventory().getItem(slot1);
        final ItemStack item2 = inventory.getInventory().getItem(slot2);
        inventory.getInventory().setItem(slot1, item2);
        inventory.getInventory().setItem(slot2, item1);
    }

    /**
     * Sorts the inventory contents by material type.
     *
     * @param inventory  the inventory to sort
     * @param comparator the comparator to use for sorting
     */
    public static void sort(@NotNull InventoryHolder inventory, Comparator<ItemStack> comparator) {
        final List<ItemStack> items = getNonNullContents(inventory);
        items.sort(comparator);
        inventory.getInventory().clear();
        inventory.getInventory().setContents(items.toArray(new ItemStack[0]));
    }
}
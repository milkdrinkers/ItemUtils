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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The {@link ItemUtils} class provides utils for
 * provisioning {@literal &} interacting with item stack
 * from other plugins {@literal &} vanilla.
 */
@SuppressWarnings("unused")
public final class ItemUtils {
    private static final List<ItemProvider> providers = List.of(ItemProvider.values());

    private ItemUtils() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    /**
     * Gets a cached list of all {@link ItemProvider}'s.
     *
     * @return item providers
     */
    @SuppressWarnings("unused")
    public static @NotNull List<ItemProvider> getProviders() {
        return providers;
    }

    /**
     * Gets the item provider from item namespace or fallbacks to {@link ItemProvider#VANILLA}.
     *
     * @param itemId the item id
     * @return an item provider
     */
    @SuppressWarnings("unused")
    public static ItemProvider getProvider(final @NotNull String itemId) {
        final Optional<ItemProvider> itemProvider = getProviders().stream()
            .filter(provider -> provider.isUsingNamespace(itemId))
            .findFirst();

        return itemProvider.orElse(ItemProvider.VANILLA);
    }

    /**
     * Gets the item provider from item stack or fallbacks to {@link ItemProvider#VANILLA}.
     *
     * @param itemStack the item stack
     * @return an item provider
     */
    @SuppressWarnings("unused")
    public static @NotNull ItemProvider getProvider(final @NotNull ItemStack itemStack) {
        final Optional<ItemProvider> itemProvider = getProviders().stream()
            .filter(provider -> provider.parseItem(itemStack) != null)
            .findFirst();

        return itemProvider.orElse(ItemProvider.VANILLA);
    }

    /**
     * Creates an item stack from the item id, if the item id
     * returns true for {@link #exists(String)}.
     *
     * @param itemId the item id
     * @return item stack or null
     * @apiNote returns {@link ItemProvider#parseItem(String)}
     */
    @SuppressWarnings("unused")
    public static @Nullable ItemStack parse(final @NotNull String itemId) {
        final ItemProvider provider = getProvider(itemId);
        return provider.parseItem(itemId);
    }

    /**
     * Creates an item stack from the item id, if the item id
     * returns true for {@link #exists(String)}.
     *
     * @param itemId the item id
     * @return item stack or null
     * @apiNote returns {@link ItemProvider#parseItem(String)}
     * @see #parse(String)
     */
    @SuppressWarnings("unused")
    public static @NotNull Optional<ItemStack> parseOptional(final @NotNull String itemId) {
        return Optional.ofNullable(parse(itemId));
    }

    /**
     * Gets an item id from the item stack, if the item stack
     * returns true for {@link ItemProvider#parseItem(ItemStack)}.
     *
     * @param itemStack the item stack
     * @return item id
     * @apiNote returns {@link ItemProvider#parseItem(ItemStack)}
     * @implNote The returned item id is in the format of {@code "namespace:item_id"} or {@code "plugin:item_id"} for the supported plugins.
     */
    @SuppressWarnings("unused")
    public static @NotNull String parse(final @NotNull ItemStack itemStack) {
        final ItemProvider provider = getProvider(itemStack);
        return Objects.requireNonNull(provider.parseItem(itemStack));
    }

    /**
     * Check if an item/material exists with this item id.
     *
     * @param itemId the item id
     * @return boolean
     * @apiNote returns {@link ItemProvider#isValidItem(String)}
     */
    @SuppressWarnings("unused")
    public static boolean exists(final @NotNull String itemId) {
        final ItemProvider provider = getProvider(itemId);
        return provider.isValidItem(itemId);
    }

    /**
     * Strip the namespace out of the item id. {@code E.g. "minecraft:stone" will return "stone", "nexo:golden_apple" will return "golden_apple"}
     *
     * @param itemId the item id
     * @return stripped item id
     */
    @SuppressWarnings("unused")
    public static @NotNull String stripNamespace(final @NotNull String itemId) {
        final ItemProvider provider = getProvider(itemId);
        return provider.stripNamespace(itemId);
    }

    /**
     * Set the display name name of the item stack.
     *
     * @param itemStack the item stack
     * @param name      the display name name
     * @return item stack with display name set
     * @apiNote The name is set stripping italic decoration on the component.
     * @see org.bukkit.inventory.meta.ItemMeta#displayName(Component)
     */
    public static ItemStack setName(final @NotNull ItemStack itemStack, @NotNull Component name) {
        itemStack.editMeta(itemMeta -> itemMeta.displayName(name.decoration(TextDecoration.ITALIC, false)));
        return itemStack;
    }

    /**
     * Set the lore of the item stack.
     *
     * @param itemStack the item stack
     * @param lore      the lore
     * @return item stack with lore set
     * @apiNote The lore is set stripping italic decoration on the components.
     * @see org.bukkit.inventory.meta.ItemMeta#lore(List)
     */
    public static ItemStack setLore(final @NotNull ItemStack itemStack, @NotNull Collection<Component> lore) {
        itemStack.editMeta(itemMeta -> itemMeta.lore(lore
            .stream()
            .map(loreEntry -> loreEntry.decoration(TextDecoration.ITALIC, false))
            .toList()
        ));
        return itemStack;
    }

    /**
     * Set the lore of the item stack.
     *
     * @param itemStack the item stack
     * @param lore      the lore
     * @return item stack with lore set
     * @apiNote The lore is set stripping italic decoration on the components.
     * @see org.bukkit.inventory.meta.ItemMeta#lore(List)
     */
    public static ItemStack setLore(final @NotNull ItemStack itemStack, @NotNull Component... lore) {
        return setLore(itemStack, Arrays.stream(lore).toList());
    }
}
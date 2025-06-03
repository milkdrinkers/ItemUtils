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

import com.nexomc.nexo.api.NexoItems;
import dev.lone.itemsadder.api.CustomStack;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * An {@link ItemProvider} contains logic for accessing
 * and using item api from multiple plugins and vanilla.
 */
public enum ItemProvider {
    ORAXEN("Oraxen", List.of("oraxen")),
    NEXO("Nexo", List.of("nexo", "oraxen")),
    ITEMSADDER("ItemsAdder", List.of("itemsadder")),
    VANILLA(List.of("minecraft")); // Order matters here! The order is important for iterating the enum.

    private final String pluginName;
    private final List<String> namespaces;

    @SuppressWarnings("unused")
    ItemProvider(final List<String> namespaces) {
        this.pluginName = "";
        this.namespaces = namespaces;
    }

    @SuppressWarnings("unused")
    ItemProvider(final String name, final List<String> namespaces) {
        this.pluginName = name;
        this.namespaces = namespaces;
    }

    /**
     * Returns the Plugin Name for this {@link ItemProvider}. This is empty on the {@link ItemProvider#VANILLA} provider.
     *
     * @return name
     */
    public final String getPluginName() {
        return pluginName;
    }

    /**
     * Get all valid namespaces for this {@link ItemProvider}.
     *
     * @return list of namespaces
     */
    public final List<String> getNamespaces() {
        return namespaces;
    }

    /**
     * Check if this {@link ItemProvider} is available/loaded.
     *
     * @return boolean
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public final boolean isLoaded() {
        return isVanilla() || Bukkit.getPluginManager().isPluginEnabled(getPluginName());
    }

    /**
     * Check if the item id contains a namespace for this {@link ItemProvider}.
     *
     * @param itemId the item id
     * @return boolean
     */
    public final boolean isUsingNamespace(final String itemId) {
        if (!isLoaded())
            return false;

        // A item with no prefix is using vanilla namespace
        if (isVanilla())
            return true;

        // Special check for Vanilla (items without a namespace)
        return getNamespaces()
            .stream()
            .anyMatch(namespace -> itemId.startsWith("%s:".formatted(namespace)));
    }

    /**
     * Strip the namespace out of the item id.
     *
     * @param itemId the item id
     * @return stripped item id
     */
    public final String stripNamespace(final String itemId) {
        String tempItemId = itemId;
        for (final String namespace : getNamespaces()) {
            tempItemId = tempItemId.replace("%s:".formatted(namespace), "");
        }
        return tempItemId;
    }

    /**
     * Parse this item id into an item stack.
     *
     * @param itemId the item id
     * @return item stack or null
     */
    public final @Nullable ItemStack parseItem(final String itemId) {
        if (!isLoaded())
            return null;

        final String cleanItemId = stripNamespace(itemId);

        if (!isValidItem(itemId))
            return null;

        return switch (this) {
            case VANILLA -> {
                try {
                    final @Nullable Material material = Material.matchMaterial(cleanItemId);
                    if (material == null)
                        yield null;

                    yield new ItemStack(material, 1);
                } catch (IllegalArgumentException _ignored) {
                    yield null;
                }
            }
            case ORAXEN -> OraxenItems.getItemById(cleanItemId).build();
            case NEXO -> Objects.requireNonNull(NexoItems.itemFromId(cleanItemId)).build();
            case ITEMSADDER -> CustomStack.getInstance(cleanItemId).getItemStack();
        };
    }

    /**
     * Check whether this item id maps to an existing item.
     *
     * @param itemId the item id
     * @return boolean
     */
    public final boolean isValidItem(final String itemId) {
        if (!isLoaded())
            return false;

        final String cleanItemId = stripNamespace(itemId);

        switch (this) {
            case VANILLA -> {
                return Material.matchMaterial(cleanItemId) != null;
            }
            case ORAXEN -> {
                return OraxenItems.exists(cleanItemId);
            }
            case NEXO -> {
                return NexoItems.exists(cleanItemId);
            }
            case ITEMSADDER -> {
                return CustomStack.isInRegistry(cleanItemId);
            }
        }

        return false;
    }


    /**
     * Parse an {@link ItemStack} into a item id.
     *
     * @param itemStack an item stack
     * @return item id or null if the item stack cannot be parsed into an item id
     * @apiNote The returned item id is in the format of "namespace:item_id" or "plugin:item_id" for the supported plugins.
     */
    public final @Nullable String parseItem(final ItemStack itemStack) {
        if (!isLoaded())
            return null;

        // Iterates item providers in the order they are defined, to see if we can parse the item stack
        switch (this) {
            case VANILLA -> {
                return itemStack.getType().getKey().asString();
            }
            case ORAXEN -> {
                if (!OraxenItems.exists(itemStack))
                    return null;

                return "oraxen:" + OraxenItems.getIdByItem(itemStack);
            }
            case NEXO -> {
                final @Nullable String stackId = NexoItems.idFromItem(itemStack);
                if (stackId == null)
                    return null;

                return "nexo:" + NexoItems.idFromItem(itemStack);
            }
            case ITEMSADDER -> {
                final @Nullable CustomStack stack = CustomStack.byItemStack(itemStack);
                if (stack == null)
                    return null;

                return "itemsadder:" + stack.getId();
            }
        }

        return null;
    }

    private boolean isVanilla() {
        return this == VANILLA;
    }
}
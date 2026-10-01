package com.github.srl_flyt.jeisearchalias.jei;

import com.github.srl_flyt.jeisearchalias.config.AliasConfig;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IIngredientAliasRegistration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

@JeiPlugin
public class JeiSearchAliasPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("jei_search_alias", "main");
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        registerDefaultItemIds(registration);
        registerConfigAliases(registration);
    }

    private static void registerDefaultItemIds(IIngredientAliasRegistration registration) {
        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceKey<Item> key = entry.getKey();
            Item item = entry.getValue();
            String itemId = key.location().getPath();
            if (item == Items.AIR) {
                continue;
            }
            registration.addAlias(item, itemId);
        }
    }

    private static void registerConfigAliases(IIngredientAliasRegistration registration) {
        Map<String, List<String>> aliases = AliasConfig.load();
        for (Map.Entry<String, List<String>> entry : aliases.entrySet()) {
            String itemId = entry.getKey();
            Item item = findItem(itemId);
            if (item == null || item == Items.AIR){
                System.err.println("[JEI Search Alias] " + "Item not found: " + itemId);
                continue;
            }
            for (String alias : entry.getValue()) {
                registration.addAlias(item, alias);
            }
        }
    }

    private static Item findItem(String itemId) {
        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) {
            System.err.println("[JEI Search Alias] " + "Invalid item ID: " + itemId);
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(id);
    }
}
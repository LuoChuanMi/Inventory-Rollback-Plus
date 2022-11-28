package me.danjono.inventoryrollback.inventory;

import com.nuclyon.technicallycoded.inventoryrollback.InventoryRollbackPlus;
import hamsteryds.nereusopus.utils.EnchantmentUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class EnchantUtil {

    private static final NamespacedKey enchantsInfoKey = new NamespacedKey(InventoryRollbackPlus.getInstance(), "enchantsInfo");

    public static ItemStack saveEnchants(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        if (!itemMeta.hasEnchants()) {
            return itemStack;
        }
        Map<Enchantment, Integer> map = itemMeta.getEnchants();
        if (map.isEmpty()) {
            return itemStack;
        }
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        pdc.set(enchantsInfoKey, PersistentDataType.STRING, enchantToString(map));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack loadEnchants(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        String enchantString = pdc.get(enchantsInfoKey, PersistentDataType.STRING);
        if (enchantString == null) {
            return itemStack;
        }
        Map<Enchantment, Integer> enchants = stringToEnchant(enchantString);
        if (enchants.isEmpty()) {
            return itemStack;
        }
        pdc.remove(enchantsInfoKey);
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static String enchantToString(Map<Enchantment, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey().getKey().getKey()).append(":").append(entry.getValue()).append(",");
        }
        return sb.toString();
    }

    private static Map<Enchantment, Integer> stringToEnchant(String string) {
        Map<Enchantment, Integer> map = new HashMap<>();
        String[] split = string.split(",");
        for (String s : split) {
            String[] split1 = s.split(":");
            map.put(EnchantmentUtils.fromID(split1[0]), Integer.parseInt(split1[1]));
        }
        return map;
    }

}

package me.imdanix.model;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class NmdPlugin extends JavaPlugin implements Listener {
    private Map<Material, ModelMap> modelsByType;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reload();
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void reload() {
        reloadConfig();
        modelsByType = new EnumMap<>(Material.class);
        ConfigurationSection config = getConfig();
        int count = 0;
        for (String typeStr : config.getKeys(false)) {
            Material type = Material.getMaterial(typeStr.toUpperCase(Locale.ROOT));
            if (type == null) {
                getLogger().warning("Type '" + typeStr + "' doesn't exist, skipping");
                continue;
            }
            ModelMap models = new ModelMap();
            ConfigurationSection typeCfg = config.getConfigurationSection(typeStr);
            for (String valueStr : typeCfg.getKeys(false)) {
                Integer value = parseInteger(valueStr);
                if (value == null) {
                    getLogger().warning("Model '" + valueStr + "' of '" + typeStr + "' is not an integer value, skipping");
                    continue;
                }
                ConfigurationSection modelCfg = typeCfg.getConfigurationSection(valueStr);
                String name = modelCfg.getString("name");
                if (name == null) {
                    getLogger().warning("Model '" + valueStr + "' of '" + typeStr + "' has no name attached, skipping");
                    continue;
                }
                ++count;
                models.put(name, value, modelCfg.getBoolean("ignore-color"), modelCfg.getBoolean("ignore-case"));
            }
            if (!models.isEmpty()) modelsByType.put(type, models);
        }
        getLogger().info("Loaded " + count +" named CustomModelData values");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvil(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null || !result.hasItemMeta()) return;
        ModelMap models = modelsByType.get(result.getType());
        if (models == null) return;
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta.hasDisplayName()) {
            Integer value = models.getByName(resultMeta.getDisplayName());
            if (value != null) {
                resultMeta.setCustomModelData(value);
                result.setItemMeta(resultMeta);
                return;
            }
        }
        ItemStack origin = event.getInventory().getItem(0);
        if (origin == null || !origin.hasItemMeta()) return;
        ItemMeta originMeta = origin.getItemMeta();
        if (originMeta.hasDisplayName() && models.getByName(originMeta.getDisplayName()) != null) {
            resultMeta.setCustomModelData(null);
            result.setItemMeta(resultMeta);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        reload();
        sender.sendMessage(ChatColor.GREEN + "NamedModelData was successfully reloaded");
        return true;
    }

    private static Integer parseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

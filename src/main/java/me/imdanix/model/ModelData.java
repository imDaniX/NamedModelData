package me.imdanix.model;

import org.bukkit.ChatColor;

public class ModelData {
    private final String name;
    private final Integer value;
    private final boolean ignoreColor;
    private final boolean ignoreCase;

    public ModelData(String name, Integer value, boolean ignoreColor, boolean ignoreCase) {
        this.name = name;
        this.value = value;
        this.ignoreColor = ignoreColor;
        this.ignoreCase = ignoreCase;
    }

    public Integer getValue() {
        return value;
    }

    public boolean isValid(String name) {
        if (ignoreColor) {
            name = ChatColor.stripColor(name);
        }
        return ignoreCase ? this.name.equalsIgnoreCase(name) : this.name.equals(name);
    }
}

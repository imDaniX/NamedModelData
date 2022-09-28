package me.imdanix.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ModelMap {
    private final Map<String, Collection<ModelData>> namesToData;
    private final Set<Integer> values;

    public ModelMap() {
        namesToData = new HashMap<>();
        values = new HashSet<>();
    }

    public void put(String name, Integer value, boolean ignoreColor, boolean ignoreCase) {
        namesToData
                .computeIfAbsent(name.toLowerCase(Locale.ROOT), key -> new ArrayList<>())
                .add(new ModelData(name, value, ignoreColor, ignoreCase));
        values.add(value);
    }

    public Integer getByName(String name) {
        Collection<ModelData> models = namesToData.getOrDefault(name.toLowerCase(Locale.ROOT), Collections.emptyList());
        for (ModelData model : models) {
            if (model.isValid(name)) return model.getValue();
        }
        return null;
    }

    public boolean containsValue(Integer value) {
        return value != null && values.contains(value);
    }

    public boolean isEmpty() {
        return namesToData.isEmpty();
    }
}

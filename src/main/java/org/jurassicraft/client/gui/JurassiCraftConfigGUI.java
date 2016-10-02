package org.jurassicraft.client.gui;

import net.ilexiconn.llibrary.client.gui.config.ConfigCategory;
import net.ilexiconn.llibrary.client.gui.config.ConfigGUI;
import net.ilexiconn.llibrary.client.gui.config.ConfigProperty;
import net.ilexiconn.llibrary.client.gui.config.property.BooleanConfigProperty;
import net.ilexiconn.llibrary.client.gui.config.property.IntRangeConfigProperty;
import net.ilexiconn.llibrary.client.gui.config.property.StringConfigProperty;
import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.config.JurassiCraftConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class JurassiCraftConfigGUI extends ConfigGUI {
    private Map<Field, Property> properties = new HashMap<>();

    public JurassiCraftConfigGUI(GuiScreen parent) {
        super(parent, JurassiCraft.INSTANCE, null);
        ConfigHandler.INSTANCE.loadConfigForID(JurassiCraft.MODID);
        for (Field field : JurassiCraftConfig.class.getDeclaredFields()) {
            field.setAccessible(true);
            ConfigEntry entry = field.getDeclaredAnnotation(ConfigEntry.class);
            if (entry != null) {
                ConfigCategory category = null;
                List<ConfigProperty> properties = null;
                for (ConfigCategory c : this.categories) {
                    if (c.getName().equals(entry.category())) {
                        category = c;
                        properties = c.getProperties();
                        break;
                    }
                }
                if (category == null) {
                    properties = new ArrayList<>();
                    category = new ConfigCategory(entry.category(), properties);
                    this.categories.add(category);
                }
                String name = entry.name().length() == 0 ? field.getName() : entry.name();
                Class<?> type = field.getType();
                try {
                    if (type == int.class || type == Integer.class) {
                        Property property = new Property(name, String.valueOf(field.get(JurassiCraft.CONFIG)), Property.Type.INTEGER);
                        properties.add(new IntRangeConfigProperty(property));
                        this.properties.put(field, property);
                    } else if (type == String.class) {
                        Property property = new Property(name, String.valueOf(field.get(JurassiCraft.CONFIG)), Property.Type.STRING);
                        properties.add(new StringConfigProperty(property));
                        this.properties.put(field, property);
                    } else if (type == boolean.class || type == Boolean.class) {
                        Property property = new Property(name, String.valueOf(field.get(JurassiCraft.CONFIG)), Property.Type.BOOLEAN);
                        properties.add(new BooleanConfigProperty(property));
                        this.properties.put(field, property);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (Map.Entry<Field, Property> entry : this.properties.entrySet()) {
            Property property = entry.getValue();
            if (property.hasChanged()) {
                Property.Type type = property.getType();
                Object value = null;
                if (type == Property.Type.INTEGER) {
                    value = property.getInt();
                } else if (type == Property.Type.BOOLEAN) {
                    value = property.getBoolean();
                } else if (type == Property.Type.STRING) {
                    value = property.getString();
                }
                try {
                    entry.getKey().set(JurassiCraft.CONFIG, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        ConfigHandler.INSTANCE.saveConfigForID(JurassiCraft.MODID);
    }
}

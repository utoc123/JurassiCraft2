package org.jurassicraft.client.gui;

import net.ilexiconn.llibrary.client.gui.config.ConfigCategory;
import net.ilexiconn.llibrary.client.gui.config.ConfigGUI;
import net.ilexiconn.llibrary.client.gui.config.ConfigProperty;
import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.config.JurassiCraftConfig;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class JurassiCraftConfigGUI extends ConfigGUI {
    public JurassiCraftConfigGUI(GuiScreen parent) {
        super(parent, JurassiCraft.INSTANCE, null);
        for (Field field : JurassiCraftConfig.class.getDeclaredFields()) {
            field.setAccessible(true);
            ConfigEntry entry = field.getDeclaredAnnotation(ConfigEntry.class);
            if (entry != null) {
                ConfigCategory category = null;
                Map<String, ConfigProperty<?>> properties = null;
                for (ConfigCategory c : this.categories) {
                    if (c.getName().equals(entry.category())) {
                        category = c;
                        properties = c.getProperties();
                        break;
                    }
                }
                if (category == null) {
                    properties = new HashMap<>();
                    category = new ConfigCategory(entry.category(), properties);
                    this.categories.add(category);
                }
                properties.put(entry.name().length() == 0 ? field.getName() : entry.name(), new ConfigProperty<>(new IValueAccess<Object>() {
                    @Override
                    public void accept(Object value) {
                        try {
                            field.set(JurassiCraft.CONFIG, value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public Object get() {
                        try {
                            return field.get(JurassiCraft.CONFIG);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }, field.getType() == int.class || field.getType() == Integer.class ? Property.Type.INTEGER : field.getType() == String.class ? Property.Type.STRING : Property.Type.BOOLEAN));
            }
        }
    }
}

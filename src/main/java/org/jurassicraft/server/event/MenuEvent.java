package org.jurassicraft.server.event;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.jurassicraft.JurassiCraft;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MenuEvent
{

    public static ResourceLocation splashes = new ResourceLocation(JurassiCraft.MODID, "lang/splash.txt");
    public static ResourceLocation[] panorama = new ResourceLocation[]
            {
                    new ResourceLocation(JurassiCraft.MODID, "textures/gui/panorama/south.png"),
                    new ResourceLocation(JurassiCraft.MODID, "textures/gui/panorama/west.png"),
                    new ResourceLocation(JurassiCraft.MODID, "textures/gui/panorama/north.png"),
                    new ResourceLocation(JurassiCraft.MODID, "textures/gui/panorama/east.png"),
                    new ResourceLocation(JurassiCraft.MODID, "textures/gui/panorama/up.png"),
                    new ResourceLocation(JurassiCraft.MODID, "textures/gui/panorama/down.png")
            };

    private void setGuiProperties(Class clazz, Object menu, Object value, String path) throws Exception
    {
        Field field = ReflectionHelper.findField(clazz, path);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(menu, value);
    }

    @SubscribeEvent
    public void openMainMenu(GuiOpenEvent event)
    {
        if (event.gui instanceof GuiMainMenu)
        {
            GuiMainMenu menu = (GuiMainMenu) event.gui;
            try
            {
                setGuiProperties(GuiMainMenu.class, menu, panorama, "titlePanoramaPaths");
                setGuiProperties(GuiMainMenu.class, menu, splashes, "splashTexts");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

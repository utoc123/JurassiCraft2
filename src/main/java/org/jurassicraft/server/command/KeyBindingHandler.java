package org.jurassicraft.server.command;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindingHandler {
    public static KeyBinding MICRORAPTOR_DISMOUNT = new KeyBinding("key.microraptor_dismount", Keyboard.KEY_C, "JurassiCraft");

    public static void init() {
        ClientRegistry.registerKeyBinding(MICRORAPTOR_DISMOUNT);
    }
}

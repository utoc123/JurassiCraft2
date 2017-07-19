package org.jurassicraft.server.command;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindingHandler {

    public static KeyBinding microraptor_off;

    public static void init() {
        microraptor_off = new KeyBinding("key.microraptor_off", Keyboard.KEY_R, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(microraptor_off);

    }
}

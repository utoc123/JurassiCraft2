package org.jurassicraft.client.gui.app;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.gui.PaleoPadGui;
import org.jurassicraft.server.capability.PlayerDataCapability;
import org.jurassicraft.server.capability.PlayerDataCapabilityImplementation;
import org.jurassicraft.server.paleopad.App;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiApp
{
    protected App app;

    protected static final Minecraft mc = Minecraft.getMinecraft();

    public GuiApp(App app)
    {
        this.app = app;
    }

    public List<GuiButton> buttons = Lists.newArrayList();

    private boolean requestShutdown;

    public void requestShutdown()
    {
        this.requestShutdown = true;

        PlayerDataCapability playerData = PlayerDataCapabilityImplementation.get(Minecraft.getMinecraft().thePlayer);
        playerData.closeApp(app);
    }

    public boolean doesRequestShutdown()
    {
        return requestShutdown;
    }

    public abstract void render(int mouseX, int mouseY, PaleoPadGui gui);

    protected void renderButtons(int mouseX, int mouseY, PaleoPadGui gui)
    {
        for (GuiButton button : buttons)
        {
            button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    public void keyPressed(int key)
    {
    }

    public void mouseClicked(int mouseX, int mouseY, PaleoPadGui gui)
    {
    }

    public abstract void actionPerformed(GuiButton button);

    public abstract void init();

    public abstract ResourceLocation getTexture(PaleoPadGui gui);

    public App getApp()
    {
        return app;
    }

    public void update()
    {
    }
}

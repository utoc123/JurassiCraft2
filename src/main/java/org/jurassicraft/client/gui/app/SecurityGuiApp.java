package org.jurassicraft.client.gui.app;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.gui.PaleoPadGui;
import org.jurassicraft.server.entity.data.JCPlayerData;
import org.jurassicraft.server.paleopad.App;
import org.jurassicraft.server.paleopad.SecurityApp;

import java.util.List;

public class SecurityGuiApp extends GuiApp
{
    private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.MODID, "textures/gui/paleo_pad/apps/security.png");

    public SecurityGuiApp(App app)
    {
        super(app);
    }

    @Override
    public void render(int mouseX, int mouseY, PaleoPadGui gui)
    {
        super.renderButtons(mouseX, mouseY, gui);

        SecurityApp app = (SecurityApp) getApp();

        EntityPlayer player = mc.thePlayer;
        World world = mc.theWorld;

        JCPlayerData data = JCPlayerData.getPlayerData(player);

        List<BlockPos> cameras = data.getCameras();
    }

    @Override
    public void actionPerformed(GuiButton button)
    {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, PaleoPadGui gui)
    {
        ScaledResolution dimensions = new ScaledResolution(mc);
        mouseX -= dimensions.getScaledWidth() / 2 - 115;
        mouseY -= 65;
    }

    @Override
    public void init()
    {
    }

    @Override
    public ResourceLocation getTexture(PaleoPadGui gui)
    {
        return texture;
    }
}

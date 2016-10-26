package org.jurassicraft.server.plant;

import com.jcraft.jorbis.Block;
import org.jurassicraft.server.block.BlockHandler;

/**
 * Created by Codyr on 25/10/2016.
 */
public class WoollyStalkedBegoniaPlant extends Plant {

    @Override
    public String getName() { return "Woolly-stalked Begonia"; }

    @Override
    public net.minecraft.block.Block getBlock() {
        return BlockHandler.WOOLLY_STALKED_BEGONIA;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }

}

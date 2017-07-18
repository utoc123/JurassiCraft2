package org.jurassicraft.server.plugin.jei.category.ingredient;

import org.jurassicraft.server.dinosaur.Dinosaur;

public class BoneInput {
    public final Dinosaur dinosaur;
    public final String bone;

    public BoneInput(Dinosaur dinosaur, String bone) {
        this.dinosaur = dinosaur;
        this.bone = bone;
    }
}

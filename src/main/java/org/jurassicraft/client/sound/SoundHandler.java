package org.jurassicraft.client.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jurassicraft.JurassiCraft;

public class SoundHandler
{
    public static final SoundEvent TROODONS_AND_RAPTORS = create("troodons_and_raptors");
    public static final SoundEvent JURASSICRAFT_THEME = create("jurassicraft_theme");
    public static final SoundEvent DONT_MOVE_A_MUSCLE = create("dont_move_a_muscle");

    public static final SoundEvent STOMP = create("stomp");
    public static final SoundEvent FEEDER = create("feeder");
    public static final SoundEvent CAR_MOVE = create("car_move");

    public static final SoundEvent BRACHIOSAURUS_LIVING = create("brachiosaurus_living");
    public static final SoundEvent BRACHIOSAURUS_HURT = create("brachiosaurus_hurt");
    public static final SoundEvent BRACHIOSAURUS_DEATH = create("brachiosaurus_death");

    public static final SoundEvent DODO_DEATH = create("dodo_death");
    public static final SoundEvent DODO_HURT = create("dodo_hurt");
    public static final SoundEvent DODO_LIVING = create("dodo_living");

    public static final SoundEvent RUGOPS_DEATH = create("rugops_death");
    public static final SoundEvent RUGOPS_HURT = create("rugops_hurt");
    public static final SoundEvent RUGOPS_LIVING = create("rugops_living");

    public static final SoundEvent HYPSILOPHODON_HURT = create("hypsilophodon_hurt");
    public static final SoundEvent HYPSILOPHODON_LIVING = create("hypsilophodon_living");

    public static final SoundEvent PARASAUROLOPHUS_LIVING = create("parasaurolophus_living");
    public static final SoundEvent PARASAUROLOPHUS_CALL = create("parasaurolophus_call");
    public static final SoundEvent PARASAUROLOPHUS_DEATH = create("parasaurolophus_death");
    public static final SoundEvent PARASAUROLOPHUS_HURT = create("parasaurolophus_hurt");

    public static final SoundEvent TRICERATOPS_LIVING = create("triceratops_living");
    public static final SoundEvent TRICERATOPS_DEATH = create("triceratops_death");
    public static final SoundEvent TRICERATOPS_HURT = create("triceratops_hurt");

    public static final SoundEvent STEGOSAURUS_LIVING = create("stegosaurus_living");
    public static final SoundEvent STEGOSAURUS_HURT = create("stegosaurus_hurt");
    public static final SoundEvent STEGOSAURUS_DEATH = create("stegosaurus_death");

    public static final SoundEvent DILOPHOSAURUS_LIVING = create("dilophosaurus_living");
    public static final SoundEvent DILOPHOSAURUS_HURT = create("dilophosaurus_hurt");
    public static final SoundEvent DILOPHOSAURUS_DEATH = create("dilophosaurus_death");

    public static final SoundEvent CARNOTAURUS_LIVING = create("carnotaurus_living");
    public static final SoundEvent CARNOTAURUS_HURT = create("carnotaurus_hurt");
    public static final SoundEvent CARNOTAURUS_DEATH = create("carnotaurus_death");

    public static final SoundEvent GALLIMIMUS_LIVING = create("gallimimus_living");
    public static final SoundEvent GALLIMIMUS_DEATH = create("gallimimus_death");
    public static final SoundEvent GALLIMIMUS_HURT = create("gallimimus_hurt");

    public static final SoundEvent SPINOSAURUS_LIVING = create("spinosaurus_living");
    public static final SoundEvent SPINOSAURUS_HURT = create("spinosaurus_hurt");
    public static final SoundEvent SPINOSAURUS_DEATH = create("spinosaurus_death");

    public static final SoundEvent PTERANODON_LIVING = create("pteranodon_living");
    public static final SoundEvent PTERANODON_HURT = create("pteranodon_hurt");
    public static final SoundEvent PTERANODON_DEATH = create("pteranodon_death");
    public static final SoundEvent PTERANODON_CALL = create("pteranodon_call");

    public static final SoundEvent INDOMINUS_LIVING = create("indominus_living");
    public static final SoundEvent INDOMINUS_DEATH = create("indominus_death");
    public static final SoundEvent INDOMINUS_HURT = create("indominus_hurt");
    public static final SoundEvent INDOMINUS_ROAR = create("indominus_roar");
    public static final SoundEvent INDOMINUS_BREATHING = create("indominus_breathing");

    public static final SoundEvent ANKYLOSAURUS_LIVING = create("ankylosaurus_living");
    public static final SoundEvent ANKYLOSAURUS_HURT = create("ankylosaurus_hurt");

    public static final SoundEvent HERRERASAURUS_LIVING = create("herrerasaurus_living");
    public static final SoundEvent HERRERASAURUS_DEATH = create("herrerasaurus_death");

    public static final SoundEvent TYRANNOSAURUS_BREATHING = create("tyrannosaurus_breathing");
    public static final SoundEvent TYRANNOSAURUS_DEATH = create("tyrannosaurus_death");
    public static final SoundEvent TYRANNOSAURUS_HURT = create("tyrannosaurus_hurt");
    public static final SoundEvent TYRANNOSAURUS_ROAR = create("tyrannosaurus_roar");
    public static final SoundEvent TYRANNOSAURUS_LIVING = create("tyrannosaurus_living");

    public static final SoundEvent VELOCIRAPTOR_LIVING = create("velociraptor_living");
    public static final SoundEvent VELOCIRAPTOR_HURT = create("velociraptor_hurt");
    public static final SoundEvent VELOCIRAPTOR_BREATHING = create("velociraptor_breathing");
    public static final SoundEvent VELOCIRAPTOR_CALL = create("velociraptor_call");
    public static final SoundEvent VELOCIRAPTOR_DEATH = create("velociraptor_death");

    public static SoundEvent create(String name)
    {
        ResourceLocation resource = new ResourceLocation(JurassiCraft.MODID, name);
        SoundEvent event = new SoundEvent(resource);
        GameRegistry.register(event, resource);
        return event;
    }
}

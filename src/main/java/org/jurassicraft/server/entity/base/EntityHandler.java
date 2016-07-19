package org.jurassicraft.server.entity.base;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.dinosaur.BrachiosaurusDinosaur;
import org.jurassicraft.server.dinosaur.DilophosaurusDinosaur;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.dinosaur.GallimimusDinosaur;
import org.jurassicraft.server.dinosaur.ParasaurolophusDinosaur;
import org.jurassicraft.server.dinosaur.TriceratopsDinosaur;
import org.jurassicraft.server.dinosaur.TyrannosaurusDinosaur;
import org.jurassicraft.server.dinosaur.VelociraptorDinosaur;
import org.jurassicraft.server.entity.VenomEntity;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.entity.item.MuralEntity;
import org.jurassicraft.server.entity.item.PaddockSignEntity;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;
import org.jurassicraft.server.entity.vehicle.modules.SeatEntity;
import org.jurassicraft.server.period.TimePeriod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EntityHandler
{
    public static final Dinosaur BRACHIOSAURUS = new BrachiosaurusDinosaur();
    public static final Dinosaur DILOPHOSAURUS = new DilophosaurusDinosaur();
    public static final Dinosaur GALLIMIMUS = new GallimimusDinosaur();
    public static final Dinosaur PARASAUROLOPHUS = new ParasaurolophusDinosaur();
    public static final Dinosaur TRICERATOPS = new TriceratopsDinosaur();
    public static final Dinosaur TYRANNOSAURUS = new TyrannosaurusDinosaur();
    public static final Dinosaur VELOCIRAPTOR = new VelociraptorDinosaur();

    private static final List<Dinosaur> DINOSAURS = new ArrayList<>();
    private static final HashMap<TimePeriod, List<Dinosaur>> DINOSAUR_PERIODS = new HashMap<>();

    private static int entityId;

    private static ProgressManager.ProgressBar dinosaurProgress;

    public static List<Dinosaur> getDinosaursFromSeaLampreys()
    {
        List<Dinosaur> marineDinos = new ArrayList<>();

        for (Dinosaur dino : getRegisteredDinosaurs())
        {
            if (dino.isMarineAnimal() && !(dino instanceof Hybrid))
            {
                marineDinos.add(dino);
            }
        }

        return marineDinos;
    }

    public static void init()
    {
        registerDinosaur(VELOCIRAPTOR);
        registerDinosaur(BRACHIOSAURUS);
        registerDinosaur(DILOPHOSAURUS);
        registerDinosaur(GALLIMIMUS);
        registerDinosaur(PARASAUROLOPHUS);
        registerDinosaur(TRICERATOPS);
        registerDinosaur(TYRANNOSAURUS);

        dinosaurProgress = ProgressManager.push("Loading dinosaurs", DINOSAURS.size());

        initDinosaurs();

        ProgressManager.pop(dinosaurProgress);

        registerEntity(AttractionSignEntity.class, "Attraction Sign");
        registerEntity(PaddockSignEntity.class, "Paddock Sign");
        registerEntity(MuralEntity.class, "Mural");
        registerEntity(VenomEntity.class, "Venom");

        registerEntity(JeepWranglerEntity.class, "Jeep Wrangler");
        registerEntity(SeatEntity.class, "Vehicle Seat");

//        registerEntity(DinosaurEggEntity.class, "Dinosaur Egg");
//        registerEntity(HelicopterBaseEntity.class, "Helicopter base");
//        registerEntity(HelicopterSeatEntity.class, "Helicopter seat Do not spawn please, like really don't");
    }

    private static void initDinosaurs()
    {
        for (Dinosaur dinosaur : DINOSAURS)
        {
            dinosaurProgress.step(dinosaur.getName());

            dinosaur.init();

            if (!(dinosaur instanceof Hybrid) && dinosaur.shouldRegister())
            {
                TimePeriod period = dinosaur.getPeriod();

                List<Dinosaur> dinoList = DINOSAUR_PERIODS.get(period);

                if (dinoList != null)
                {
                    dinoList.add(dinosaur);

                    DINOSAUR_PERIODS.remove(period);
                    DINOSAUR_PERIODS.put(period, dinoList);
                }
                else
                {
                    List<Dinosaur> newDinoList = new ArrayList<>();
                    newDinoList.add(dinosaur);

                    DINOSAUR_PERIODS.put(period, newDinoList);
                }
            }

            Class<? extends DinosaurEntity> clazz = dinosaur.getDinosaurClass();

            registerEntity(clazz, dinosaur.getName());
        }
    }

    private static void registerEntity(Class<? extends Entity> entity, String name)
    {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        EntityRegistry.registerModEntity(entity, formattedName, entityId++, JurassiCraft.INSTANCE, 1024, 1, true);
    }

    public static void registerDinosaur(Dinosaur dinosaur)
    {
        DINOSAURS.add(dinosaur);
    }

    public static Dinosaur getDinosaurById(int id)
    {
        if (id >= DINOSAURS.size() || id < 0)
        {
            return null;
        }

        return DINOSAURS.get(id);
    }

    public static int getDinosaurId(Dinosaur dinosaur)
    {
        return DINOSAURS.indexOf(dinosaur);
    }

    public static List<Dinosaur> getDinosaursFromAmber()
    {
        List<Dinosaur> amberDinos = new ArrayList<>();

        for (Dinosaur dino : getRegisteredDinosaurs())
        {
            if (!dino.isMarineAnimal() && !(dino instanceof Hybrid))
            {
                amberDinos.add(dino);
            }
        }

        return amberDinos;
    }

    public static List<Dinosaur> getDinosaurs()
    {
        return DINOSAURS;
    }

    public static List<Dinosaur> getRegisteredDinosaurs()
    {
        List<Dinosaur> dinosaurs = new ArrayList<>();

        for (Dinosaur dinosaur : EntityHandler.DINOSAURS)
        {
            if (dinosaur.shouldRegister())
            {
                dinosaurs.add(dinosaur);
            }
        }

        return dinosaurs;
    }

    public static List<Dinosaur> getPrehistoricDinosaurs()
    {
        List<Dinosaur> dinosaurs = new ArrayList<>();

        for (Dinosaur dinosaur : EntityHandler.DINOSAURS)
        {
            if (dinosaur.shouldRegister() && !(dinosaur instanceof Hybrid))
            {
                dinosaurs.add(dinosaur);
            }
        }

        return dinosaurs;
    }

    public static List<Dinosaur> getDinosaursFromPeriod(TimePeriod period)
    {
        return DINOSAUR_PERIODS.get(period);
    }

    public static Dinosaur getDinosaurByClass(Class<? extends DinosaurEntity> clazz)
    {
        for (Dinosaur dino : DINOSAURS)
        {
            if (dino.getDinosaurClass().equals(clazz))
            {
                return dino;
            }
        }

        return null;
    }
}

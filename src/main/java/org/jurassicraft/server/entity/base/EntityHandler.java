package org.jurassicraft.server.entity.base;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.configuration.JCConfigurations;
import org.jurassicraft.server.dinosaur.BrachiosaurusDinosaur;
import org.jurassicraft.server.dinosaur.DilophosaurusDinosaur;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.dinosaur.GallimimusDinosaur;
import org.jurassicraft.server.dinosaur.ParasaurolophusDinosaur;
import org.jurassicraft.server.dinosaur.TriceratopsDinosaur;
import org.jurassicraft.server.dinosaur.TyrannosaurusDinosaur;
import org.jurassicraft.server.dinosaur.VelociraptorDinosaur;
import org.jurassicraft.server.dinosaur.disabled.AchillobatorDinosaur;
import org.jurassicraft.server.dinosaur.disabled.AnkylosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.ApatosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.BaryonyxDinosaur;
import org.jurassicraft.server.dinosaur.disabled.CarnotaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.CearadactylusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.ChasmosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.CoelacanthDinosaur;
import org.jurassicraft.server.dinosaur.disabled.CompsognathusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.CorythosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.DimorphodonDinosaur;
import org.jurassicraft.server.dinosaur.disabled.DodoDinosaur;
import org.jurassicraft.server.dinosaur.disabled.DunkleosteusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.EdmontosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.GiganotosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.HerrerasaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.HypsilophodonDinosaur;
import org.jurassicraft.server.dinosaur.disabled.IndominusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.LambeosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.LeaellynasauraDinosaur;
import org.jurassicraft.server.dinosaur.disabled.LeptictidiumDinosaur;
import org.jurassicraft.server.dinosaur.disabled.LudodactylusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.MajungasaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.MamenchisaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.MegapiranhaDinosaur;
import org.jurassicraft.server.dinosaur.disabled.MetriacanthosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.MicroceratusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.MoganopterusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.OrnithomimusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.OthnieliaDinosaur;
import org.jurassicraft.server.dinosaur.disabled.PachycephalosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.ProtoceratopsDinosaur;
import org.jurassicraft.server.dinosaur.disabled.PteranodonDinosaur;
import org.jurassicraft.server.dinosaur.disabled.RugopsDinosaur;
import org.jurassicraft.server.dinosaur.disabled.SegisaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.SpinosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.StegosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.TherizinosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.TroodonDinosaur;
import org.jurassicraft.server.dinosaur.disabled.TropeognathusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.TylosaurusDinosaur;
import org.jurassicraft.server.dinosaur.disabled.VelociraptorBlueDinosaur;
import org.jurassicraft.server.dinosaur.disabled.VelociraptorCharlieDinosaur;
import org.jurassicraft.server.dinosaur.disabled.VelociraptorDeltaDinosaur;
import org.jurassicraft.server.dinosaur.disabled.VelociraptorEchoDinosaur;
import org.jurassicraft.server.dinosaur.disabled.ZhenyuanopterusDinosaur;
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

public class EntityHandler
{
    public static final Dinosaur BRACHIOSAURUS = new BrachiosaurusDinosaur();
    public static final Dinosaur DILOPHOSAURUS = new DilophosaurusDinosaur();
    public static final Dinosaur GALLIMIMUS = new GallimimusDinosaur();
    public static final Dinosaur PARASAUROLOPHUS = new ParasaurolophusDinosaur();
    public static final Dinosaur TRICERATOPS = new TriceratopsDinosaur();
    public static final Dinosaur TYRANNOSAURUS = new TyrannosaurusDinosaur();
    public static final Dinosaur VELOCIRAPTOR = new VelociraptorDinosaur();
    public static final Dinosaur DODO = new DodoDinosaur();
    public static final Dinosaur ACHILLOBATOR = new AchillobatorDinosaur();
    public static final Dinosaur ANKYLOSAURUS = new AnkylosaurusDinosaur();
    public static final Dinosaur CARNOTAURUS = new CarnotaurusDinosaur();
    public static final Dinosaur COELACANTH = new CoelacanthDinosaur();
    public static final Dinosaur COMPSOGNATHUS = new CompsognathusDinosaur();
    public static final Dinosaur DUNKLEOSTEUS = new DunkleosteusDinosaur();
    public static final Dinosaur GIGANOTOSAURUS = new GiganotosaurusDinosaur();
    public static final Dinosaur HYPSILOPHODON = new HypsilophodonDinosaur();
    public static final Dinosaur INDOMINUS = new IndominusDinosaur();
    public static final Dinosaur MAJUNGASAURUS = new MajungasaurusDinosaur();
    public static final Dinosaur PTERANODON = new PteranodonDinosaur();
    public static final Dinosaur RUGOPS = new RugopsDinosaur();
    public static final Dinosaur SEGISAURUS = new SegisaurusDinosaur();
    public static final Dinosaur SPINOSAURUS = new SpinosaurusDinosaur();
    public static final Dinosaur STEGOSAURUS = new StegosaurusDinosaur();
    public static final Dinosaur LEPTICTIDIUM = new LeptictidiumDinosaur();
    public static final Dinosaur MICROCERATUS = new MicroceratusDinosaur();
    public static final Dinosaur APATOSAURUS = new ApatosaurusDinosaur();
    public static final Dinosaur OTHNIELIA = new OthnieliaDinosaur();
    public static final Dinosaur DIMORPHODON = new DimorphodonDinosaur();
    public static final Dinosaur TYLOSAURUS = new TylosaurusDinosaur();
    public static final Dinosaur LUDODACTYLUS = new LudodactylusDinosaur();
    public static final Dinosaur PROTOCERATOPS = new ProtoceratopsDinosaur();
    public static final Dinosaur TROPEOGNATHUS = new TropeognathusDinosaur();
    public static final Dinosaur LEAELLYNASAURA = new LeaellynasauraDinosaur();
    public static final Dinosaur HERRERASAURUS = new HerrerasaurusDinosaur();
    public static final Dinosaur BLUE = new VelociraptorBlueDinosaur();
    public static final Dinosaur DELTA = new VelociraptorDeltaDinosaur();
    public static final Dinosaur CHARLIE = new VelociraptorCharlieDinosaur();
    public static final Dinosaur ECHO = new VelociraptorEchoDinosaur();
    public static final Dinosaur THERIZINOSAURUS = new TherizinosaurusDinosaur();
    public static final Dinosaur MEGAPIRANHA = new MegapiranhaDinosaur();
    public static final Dinosaur BARYONYX = new BaryonyxDinosaur();
    public static final Dinosaur CEARADACTYLUS = new CearadactylusDinosaur();
    public static final Dinosaur MAMENCHISAURUS = new MamenchisaurusDinosaur();
    public static final Dinosaur CHASMOSAURUS = new ChasmosaurusDinosaur();
    public static final Dinosaur CORYTHOSAURUS = new CorythosaurusDinosaur();
    public static final Dinosaur EDMONTOSAURUS = new EdmontosaurusDinosaur();
    public static final Dinosaur LAMBEOSAURUS = new LambeosaurusDinosaur();
    public static final Dinosaur METRIACANTHOSAURUS = new MetriacanthosaurusDinosaur();
    public static final Dinosaur MOGANOPTERUS = new MoganopterusDinosaur();
    public static final Dinosaur ORNITHOMIMUS = new OrnithomimusDinosaur();
    public static final Dinosaur ZHENYUANOPTERUS = new ZhenyuanopterusDinosaur();
    public static final Dinosaur TROODON = new TroodonDinosaur();
    public static final Dinosaur PACHYCEPHALOSAURUS = new PachycephalosaurusDinosaur();
    private static List<Dinosaur> dinosaurs = new ArrayList<>();
    private static HashMap<TimePeriod, List<Dinosaur>> dinosaursFromPeriod = new HashMap<>();
    private static int entityId;

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
        registerDinosaur(ACHILLOBATOR);
        registerDinosaur(ANKYLOSAURUS);
        registerDinosaur(BRACHIOSAURUS);
        registerDinosaur(CARNOTAURUS);
        registerDinosaur(COELACANTH);
        registerDinosaur(COMPSOGNATHUS);
        registerDinosaur(DILOPHOSAURUS);
        registerDinosaur(DUNKLEOSTEUS);
        registerDinosaur(GALLIMIMUS);
        registerDinosaur(GIGANOTOSAURUS);
        registerDinosaur(INDOMINUS);
        registerDinosaur(MAJUNGASAURUS);
        registerDinosaur(PARASAUROLOPHUS);
        registerDinosaur(PTERANODON);
        registerDinosaur(RUGOPS);
        registerDinosaur(SEGISAURUS);
        registerDinosaur(SPINOSAURUS);
        registerDinosaur(STEGOSAURUS);
        registerDinosaur(TRICERATOPS);
        registerDinosaur(TYRANNOSAURUS);
        registerDinosaur(HYPSILOPHODON);
        registerDinosaur(DODO);
        registerDinosaur(LEPTICTIDIUM);
        registerDinosaur(MICROCERATUS);
        registerDinosaur(APATOSAURUS);
        registerDinosaur(OTHNIELIA);
        registerDinosaur(DIMORPHODON);
        registerDinosaur(TYLOSAURUS);
        registerDinosaur(LUDODACTYLUS);
        registerDinosaur(PROTOCERATOPS);
        registerDinosaur(TROPEOGNATHUS);
        registerDinosaur(LEAELLYNASAURA);
        registerDinosaur(HERRERASAURUS);
        registerDinosaur(BLUE);
        registerDinosaur(CHARLIE);
        registerDinosaur(DELTA);
        registerDinosaur(ECHO);
        registerDinosaur(THERIZINOSAURUS);
        registerDinosaur(MEGAPIRANHA);
        registerDinosaur(BARYONYX);
        registerDinosaur(CEARADACTYLUS);
        registerDinosaur(MAMENCHISAURUS);
        registerDinosaur(CHASMOSAURUS);
        registerDinosaur(CORYTHOSAURUS);
        registerDinosaur(EDMONTOSAURUS);
        registerDinosaur(LAMBEOSAURUS);
        registerDinosaur(METRIACANTHOSAURUS);
        registerDinosaur(MOGANOPTERUS);
        registerDinosaur(ORNITHOMIMUS);
        registerDinosaur(ZHENYUANOPTERUS);
        registerDinosaur(TROODON);
        registerDinosaur(PACHYCEPHALOSAURUS);

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

    private static void registerEntity(Class<? extends Entity> entity, String name)
    {
        String formattedName = name.toLowerCase().replaceAll(" ", "_");

        EntityRegistry.registerModEntity(entity, formattedName, entityId++, JurassiCraft.INSTANCE, 1024, 1, true);
    }

    public static void registerDinosaur(Dinosaur dinosaur)
    {
        dinosaur.init();

        dinosaurs.add(dinosaur);

        if (!(dinosaur instanceof Hybrid) && dinosaur.shouldRegister())
        {
            TimePeriod period = dinosaur.getPeriod();

            List<Dinosaur> dinoList = dinosaursFromPeriod.get(period);

            if (dinoList != null)
            {
                dinoList.add(dinosaur);

                dinosaursFromPeriod.remove(period);
                dinosaursFromPeriod.put(period, dinoList);
            }
            else
            {
                List<Dinosaur> newDinoList = new ArrayList<>();
                newDinoList.add(dinosaur);

                dinosaursFromPeriod.put(period, newDinoList);
            }
        }

        Class<? extends DinosaurEntity> clazz = dinosaur.getDinosaurClass();

        registerEntity(clazz, dinosaur.getName());

        if (dinosaur.shouldRegister() && !(dinosaur instanceof Hybrid) && JCConfigurations.shouldSpawnJurassiCraftMobs())
        {
            if (dinosaur.isMarineAnimal())
            {
                EntityRegistry.addSpawn(clazz, 5, 1, 2, EnumCreatureType.WATER_CREATURE, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.RIVER);
                EntitySpawnPlacementRegistry.setPlacementType(clazz, EntityLiving.SpawnPlacementType.IN_WATER);
            }
            else
            {
                EntityRegistry.addSpawn(clazz, 5, 1, 2, EnumCreatureType.CREATURE, Iterators.toArray(Iterators.filter(Biome.REGISTRY.iterator(), Predicates.notNull()), Biome.class));
                EntitySpawnPlacementRegistry.setPlacementType(clazz, EntityLiving.SpawnPlacementType.ON_GROUND);
            }
        }
    }

    public static Dinosaur getDinosaurById(int id)
    {
        if (id >= dinosaurs.size() || id < 0)
        {
            return null;
        }

        return dinosaurs.get(id);
    }

    public static int getDinosaurId(Dinosaur dinosaur)
    {
        return dinosaurs.indexOf(dinosaur);
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
        return dinosaurs;
    }

    public static List<Dinosaur> getRegisteredDinosaurs()
    {
        List<Dinosaur> reg = new ArrayList<>();

        for (Dinosaur dino : dinosaurs)
        {
            if (dino.shouldRegister())
            {
                reg.add(dino);
            }
        }

        return reg;
    }

    public static List<Dinosaur> getDinosaursFromPeriod(TimePeriod period)
    {
        return dinosaursFromPeriod.get(period);
    }

    public static Dinosaur getDinosaurByClass(Class<? extends DinosaurEntity> clazz)
    {
        for (Dinosaur dino : dinosaurs)
        {
            if (dino.getDinosaurClass().equals(clazz))
            {
                return dino;
            }
        }

        return null;
    }
}

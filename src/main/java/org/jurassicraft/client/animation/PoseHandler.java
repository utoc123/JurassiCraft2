package org.jurassicraft.client.animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.util.ListHashMap;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.animation.dto.AnimationsDTO;
import org.jurassicraft.client.animation.dto.DinosaurRenderDefDTO;
import org.jurassicraft.client.animation.dto.PoseDTO;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PoseHandler
{
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(DinosaurRenderDefDTO.class, new DinosaurRenderDefDTO.DinosaurDeserializer()).create();

    private Map<GrowthStage, PreloadedModelData> modelData;

    public PoseHandler(Dinosaur dinosaur)
    {
        String name = dinosaur.getName().toLowerCase().replaceAll(" ", "_");
        this.modelData = new EnumMap<>(GrowthStage.class);
        URI dinoDirURI;

        try
        {
            dinoDirURI = new URI("/assets/jurassicraft/models/entities/" + name + "/");
        }
        catch (URISyntaxException urise)
        {
            JurassiCraft.INSTANCE.getLogger().fatal("Illegal URI /assets/jurassicraft/models/entities/" + name + "/", urise);
            return;
        }

        for (GrowthStage growth : GrowthStage.values())
        {
            try
            {
                GrowthStage fileGrowthStage = growth;

                if (!dinosaur.useAllGrowthStages())
                {
                    if (growth == GrowthStage.ADOLESCENT)
                    {
                        fileGrowthStage = GrowthStage.ADULT;
                    }
                    else if (growth == GrowthStage.JUVENILE)
                    {
                        fileGrowthStage = GrowthStage.INFANT;
                    }
                }

                this.modelData.put(growth, loadDinosaur(dinoDirURI, name, fileGrowthStage));
            }
            catch (Exception e)
            {
                JurassiCraft.INSTANCE.getLogger().fatal("Failed to parse growth state " + growth + " for dinosaur " + name, e);
                this.modelData.put(growth, new PreloadedModelData());
            }
        }
    }

    /**
     * Loads a specific growth state
     *
     * @param dinoDir the base directory
     * @param name    the name of the dino
     * @param growth  the growthstate to load
     * @throws IOException
     */
    private PreloadedModelData loadDinosaur(URI dinoDir, String name, GrowthStage growth) throws IOException
    {
        String growthName = growth.name().toLowerCase(Locale.ROOT);
        URI growthSensitiveDir = dinoDir.resolve(growthName + "/");
        URI definitionFile = growthSensitiveDir.resolve(name + "_" + growthName + ".json");
        InputStream dinoDef = TabulaModelHandler.class.getResourceAsStream(definitionFile.toString());

        if (dinoDef == null)
        {
            throw new IllegalArgumentException("No model definition for the dino " + name + " with grow-state " + growth + " exists. Expected at " + definitionFile);
        }

        try
        {
            Reader reader = new InputStreamReader(dinoDef);
            AnimationsDTO rawAnimations = GSON.fromJson(reader, AnimationsDTO.class);
            PreloadedModelData data = getPosedModels(growthSensitiveDir, rawAnimations);
            JurassiCraft.INSTANCE.getLogger().debug("Successfully loaded " + name + "(" + growth + ") from " + definitionFile);

            reader.close();

            return data;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the posed models from the set of animations defined. Illegal poses (e.g. where the file doesn't exist) will be skipped and not show up in the map.
     *
     * @param anims the read animations
     * @returns the posed models
     */
    private PreloadedModelData getPosedModels(URI dinoDirURI, AnimationsDTO anims)
    {
        // Check if the file is legal: -> at least one pose for the IDLE
        // animation
        if (anims == null || anims.poses == null || anims.poses.get(Animations.IDLE.name()) == null || anims.poses.get(Animations.IDLE.name()).length == 0)
        {
            throw new IllegalArgumentException("Animation files must define at least one pose for the IDLE animation");
        }
        // Collect all needed resources
        List<String> posedModelResources = new ArrayList<String>();
        for (PoseDTO[] poses : anims.poses.values())
        {
            if (poses == null)
            {
                continue; // Pending comma in the map, ignoring
            }
            for (PoseDTO pose : poses)
            {
                if (pose == null)
                {
                    continue; // Pending comma in the list, ignoring
                }
                if (pose.pose == null)
                {
                    throw new IllegalArgumentException("Every pose must define a pose file");
                }
                String resolvedRes = resolve(dinoDirURI, pose.pose);
                int index = posedModelResources.indexOf(resolvedRes);
                if (index == -1)
                { // Not in the list
                    pose.index = posedModelResources.size();
                    posedModelResources.add(resolvedRes);
                }
                else
                { // Already in there
                    pose.index = index;
                }
            }
        }
        assert (posedModelResources.size() > 0); // anims.poses.get(Animations.IDLE.get().getId()).length
        // != 0
        AdvancedModelRenderer[][] posedCubes = new AdvancedModelRenderer[posedModelResources.size()][];
        Map<Animation, int[][]> animationSequences = new ListHashMap<Animation, int[][]>();
        // find all names we need
        DinosaurModel mainModel = JabelarAnimationHandler.getTabulaModel(posedModelResources.get(0), 0);
        if (mainModel == null)
        {
            throw new IllegalArgumentException("Couldn't load the model from " + posedModelResources.get(0));
        }
        String[] cubeNameArray = mainModel.getCubeNamesArray();
        int numParts = cubeNameArray.length;
        // load the models from the resource files
        for (int i = 0; i < posedModelResources.size(); i++)
        {
            String resource = posedModelResources.get(i);
            DinosaurModel theModel = JabelarAnimationHandler.getTabulaModel(resource, 0);
            if (theModel == null)
            {
                throw new IllegalArgumentException("Couldn't load the model from " + resource);
            }
            AdvancedModelRenderer[] cubes = new AdvancedModelRenderer[numParts];
            for (int partIndex = 0; partIndex < numParts; partIndex++)
            {
                String cubeName = cubeNameArray[partIndex];
                AdvancedModelRenderer cube = theModel.getCube(cubeName);
                if (cube == null)
                {
                    JurassiCraft.INSTANCE.getLogger().error("Could not retrieve cube " + cubeName + " (" + partIndex + ") from the model " + resource);
                }
                cubes[partIndex] = cube;
            }
            posedCubes[i] = cubes;
        }
        // load the animations sequences
        for (Map.Entry<String, PoseDTO[]> entry : anims.poses.entrySet())
        {
            Animation animations = Animations.valueOf(entry.getKey()).get();
            PoseDTO[] poses = entry.getValue();
            int[][] poseSequence = new int[poses.length][2];
            for (int i = 0; i < poses.length; i++)
            {
                poseSequence[i][0] = poses[i].index;
                poseSequence[i][1] = poses[i].time;
            }
            animationSequences.put(animations, poseSequence);
        }
        return new PreloadedModelData(posedCubes, animationSequences);
    }

    private String resolve(URI dinoDirURI, String posePath)
    {
        URI uri = dinoDirURI.resolve(posePath);
        return uri.toString();
    }

    public JabelarAnimationHandler createAnimationHandler(DinosaurEntity entity, DinosaurModel model, GrowthStage growth, boolean useInertialTweens)
    {
        PreloadedModelData growthModel = modelData.get(growth);
        return new JabelarAnimationHandler(entity, model, growthModel.models, growthModel.animations, useInertialTweens);
    }

    public Map<Animation, int[][]> getAnimations(GrowthStage growthStage)
    {
        return modelData.get(growthStage).animations;
    }

    public int getAnimationLength(Animation animation, GrowthStage growthStage)
    {
        Map<Animation, int[][]> animations = modelData.get(growthStage).animations;

        int duration = 0;

        if (animation != null)
        {
            int[][] poses = animations.get(animation);

            if (poses != null)
            {
                for (int[] pose : poses)
                {
                    for (int tween : pose)
                    {
                        duration += tween;
                    }
                }
            }
        }

        return duration;
    }

    private class PreloadedModelData
    {
        public PreloadedModelData()
        {
            this(null, null);
        }

        public PreloadedModelData(AdvancedModelRenderer[][] renderers, Map<Animation, int[][]> animations)
        {
            if (renderers == null)
            {
                renderers = new AdvancedModelRenderer[0][];
            }

            if (animations == null)
            {
                animations = new LinkedHashMap<Animation, int[][]>();
            }

            this.models = renderers;
            this.animations = animations;
        }

        AdvancedModelRenderer[][] models;
        Map<Animation, int[][]> animations;
    }
}

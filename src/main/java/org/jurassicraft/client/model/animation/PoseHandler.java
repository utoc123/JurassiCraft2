package org.jurassicraft.client.model.animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.util.ListHashMap;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.dto.AnimationsDTO;
import org.jurassicraft.client.model.animation.dto.DinosaurRenderDefDTO;
import org.jurassicraft.client.model.animation.dto.PoseDTO;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;
import org.jurassicraft.server.tabula.TabulaModelHelper;

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

public class PoseHandler {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(DinosaurRenderDefDTO.class, new DinosaurRenderDefDTO.DinosaurDeserializer()).create();

    private Map<GrowthStage, ModelData> modelData;

    public PoseHandler(Dinosaur dinosaur) {
        String name = dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        this.modelData = new EnumMap<>(GrowthStage.class);
        URI dinosaurResourceURI;

        try {
            dinosaurResourceURI = new URI("/assets/jurassicraft/models/entities/" + name + "/");
        } catch (URISyntaxException e) {
            JurassiCraft.INSTANCE.getLogger().fatal("Illegal URI /assets/jurassicraft/models/entities/" + name + "/", e);
            return;
        }

        for (GrowthStage growth : GrowthStage.values()) {
            try {
                GrowthStage actualGrowth = growth;

                if (!dinosaur.doesSupportGrowthStage(actualGrowth)) {
                    actualGrowth = GrowthStage.ADULT;
                }

                if (this.modelData.containsKey(actualGrowth)) {
                    this.modelData.put(growth, this.modelData.get(actualGrowth));
                } else {
                    ModelData loaded = this.loadModelData(dinosaurResourceURI, name, actualGrowth);

                    this.modelData.put(growth, loaded);

                    if (actualGrowth != growth) {
                        this.modelData.put(actualGrowth, loaded);
                    }
                }
            } catch (Exception e) {
                JurassiCraft.INSTANCE.getLogger().fatal("Failed to parse growth stage " + growth + " for dinosaur " + name, e);
                this.modelData.put(growth, new ModelData());
            }
        }
    }

    private ModelData loadModelData(URI resourceURI, String name, GrowthStage growth) {
        String growthName = growth.name().toLowerCase(Locale.ROOT);
        URI growthSensitiveDir = resourceURI.resolve(growthName + "/");
        URI definitionFile = growthSensitiveDir.resolve(name + "_" + growthName + ".json");
        InputStream modelIn = TabulaModelHelper.class.getResourceAsStream(definitionFile.toString());

        if (modelIn == null) {
            throw new IllegalArgumentException("No model definition for the dino " + name + " with grow-state " + growth + " exists. Expected at " + definitionFile);
        }

        try {
            Reader reader = new InputStreamReader(modelIn);
            AnimationsDTO rawAnimations = GSON.fromJson(reader, AnimationsDTO.class);
            ModelData data = this.loadModelData(growthSensitiveDir, rawAnimations);
            JurassiCraft.INSTANCE.getLogger().debug("Successfully loaded " + name + "(" + growth + ") from " + definitionFile);

            reader.close();

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ModelData loadModelData(URI resourceURI, AnimationsDTO animationsDefinition) {
        if (animationsDefinition == null || animationsDefinition.poses == null || animationsDefinition.poses.get(DinosaurAnimation.IDLE.name()) == null || animationsDefinition.poses.get(DinosaurAnimation.IDLE.name()).length == 0) {
            throw new IllegalArgumentException("Animation files must define at least one pose for the IDLE animation");
        }

        List<String> posedModelResources = new ArrayList<>();

        for (PoseDTO[] poses : animationsDefinition.poses.values()) {
            if (poses == null) {
                continue;
            }

            for (PoseDTO pose : poses) {
                if (pose == null) {
                    continue;
                }

                if (pose.pose == null) {
                    throw new IllegalArgumentException("Every pose must define a pose file");
                }

                String resolvedRes = this.resolve(resourceURI, pose.pose);
                int index = posedModelResources.indexOf(resolvedRes);

                if (index == -1) {
                    pose.index = posedModelResources.size();
                    posedModelResources.add(resolvedRes);
                } else {
                    pose.index = index;
                }
            }
        }

        Map<Animation, int[][]> animations = new ListHashMap<>();

        for (Map.Entry<String, PoseDTO[]> entry : animationsDefinition.poses.entrySet()) {
            Animation animation = DinosaurAnimation.valueOf(entry.getKey()).get();
            PoseDTO[] poses = entry.getValue();
            int[][] poseSequence = new int[poses.length][2];

            for (int i = 0; i < poses.length; i++) {
                poseSequence[i][0] = poses[i].index;
                poseSequence[i][1] = poses[i].time;
            }

            animations.put(animation, poseSequence);
        }

        if (FMLCommonHandler.instance().getSide().isClient()) {
            PosedCuboid[][] posedCuboids = new PosedCuboid[posedModelResources.size()][];
            DinosaurModel mainModel = JabelarAnimationHandler.loadModel(posedModelResources.get(0), 0);

            if (mainModel == null) {
                throw new IllegalArgumentException("Couldn't load the model from " + posedModelResources.get(0));
            }

            String[] cubeIdentifierArray = mainModel.getCubeIdentifierArray();
            int partCount = cubeIdentifierArray.length;

            for (int i = 0; i < posedModelResources.size(); i++) {
                String resource = posedModelResources.get(i);
                DinosaurModel model = JabelarAnimationHandler.loadModel(resource, 0);

                if (model == null) {
                    throw new IllegalArgumentException("Couldn't load the model from " + resource);
                }

                PosedCuboid[] pose = new PosedCuboid[partCount];

                for (int partIndex = 0; partIndex < partCount; partIndex++) {
                    String identifier = cubeIdentifierArray[partIndex];
                    AdvancedModelRenderer cube = model.getCubeByIdentifier(identifier);

                    if (cube == null) {
                        JurassiCraft.INSTANCE.getLogger().error("Could not retrieve cube " + identifier + " (" + partIndex + ") from the model " + resource);
                    }

                    pose[partIndex] = new PosedCuboid(cube);
                }

                posedCuboids[i] = pose;
            }

            return new ModelData(posedCuboids, animations);
        }

        return new ModelData(animations);
    }

    private String resolve(URI dinoDirURI, String posePath) {
        URI uri = dinoDirURI.resolve(posePath);
        return uri.toString();
    }

    public JabelarAnimationHandler createAnimationHandler(DinosaurEntity entity, DinosaurModel model, GrowthStage growthStage, boolean useInertialTweens) {
        ModelData growthModel = this.modelData.get(growthStage);

        if (!entity.getDinosaur().doesSupportGrowthStage(growthStage)) {
            growthModel = this.modelData.get(growthStage);
        }

        return new JabelarAnimationHandler(entity, model, growthModel.poses, growthModel.animations, useInertialTweens);
    }

    public Map<Animation, int[][]> getAnimations(GrowthStage growthStage) {
        return this.modelData.get(growthStage).animations;
    }

    public int getAnimationLength(Animation animation, GrowthStage growthStage) {
        Map<Animation, int[][]> animations = this.modelData.get(growthStage).animations;

        int duration = 0;

        if (animation != null) {
            int[][] poses = animations.get(animation);

            if (poses != null) {
                for (int[] pose : poses) {
                    for (int tween : pose) {
                        duration += tween;
                    }
                }
            }
        }

        return duration;
    }

    public boolean hasAnimation(Animation animation, GrowthStage growthStage) {
        return this.modelData.get(growthStage).animations.get(animation) != null;
    }

    private class ModelData {
        @SideOnly(Side.CLIENT)
        PosedCuboid[][] poses;

        Map<Animation, int[][]> animations;

        public ModelData() {
            this(null);
        }

        public ModelData(PosedCuboid[][] cuboids, Map<Animation, int[][]> animations) {
            this(animations);

            if (cuboids == null) {
                cuboids = new PosedCuboid[0][];
            }

            this.poses = cuboids;
        }

        public ModelData(Map<Animation, int[][]> animations) {
            if (animations == null) {
                animations = new LinkedHashMap<>();
            }

            this.animations = animations;
        }
    }
}

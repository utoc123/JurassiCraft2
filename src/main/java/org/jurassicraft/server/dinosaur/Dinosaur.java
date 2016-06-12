package org.jurassicraft.server.dinosaur;

import net.ilexiconn.llibrary.client.model.tabula.container.TabulaCubeContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.util.ResourceLocation;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.PoseHandler;
import org.jurassicraft.server.api.GrowthStageGenderContainer;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;
import org.jurassicraft.server.entity.base.SleepingSchedule;
import org.jurassicraft.server.period.TimePeriod;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import javax.vecmathimpl.Matrix4d;
import javax.vecmathimpl.Vector3d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Dinosaur implements Comparable<Dinosaur>
{
    private final Map<GrowthStage, List<ResourceLocation>> overlays = new HashMap<>();
    private final Map<GrowthStage, ResourceLocation> maleTextures = new HashMap<>();
    private final Map<GrowthStage, ResourceLocation> femaleTextures = new HashMap<>();
    private final Map<GrowthStageGenderContainer, ResourceLocation> eyelidTextures = new HashMap<>();
    private final Map<GrowthStage, List<ResourceLocation>> rareVariantTextures = new HashMap<>();

    private String name;
    private Class<? extends DinosaurEntity> dinoClazz;
    private int primaryEggColorMale, primaryEggColorFemale;
    private int secondaryEggColorMale, secondaryEggColorFemale;
    private TimePeriod timePeriod;
    private double babyHealth, adultHealth;
    private double babyStrength, adultStrength;
    private double babySpeed, adultSpeed;
    private float babySizeX, adultSizeX;
    private float babySizeY, adultSizeY;
    private float babyEyeHeight, adultEyeHeight;
    private double attackSpeed = 1.0;
    private boolean shouldRegister = true;
    private boolean isMarineAnimal;
    private boolean isMammal;
    private int storage;
    private int overlayCount;
    private Diet diet;
    private SleepingSchedule sleepingSchedule = SleepingSchedule.DIURNAL;
    private String[] bones;
    private int maximumAge;
    private String headCubeName;
    private boolean isImprintable;

    private float scaleInfant;
    private float scaleAdult;

    private float offsetX;
    private float offsetY;
    private float offsetZ;

    private TabulaModelContainer modelAdult;
    private TabulaModelContainer modelInfant;
    private TabulaModelContainer modelJuvenile;
    private TabulaModelContainer modelAdolescent;

    private boolean usePosesForWalkingAnim = false;

    private String[] rareVariants = new String[0];
    private PoseHandler poseHandler;

    private boolean defendOwner;

    public void init()
    {
        String formattedName = getName().toLowerCase().replaceAll(" ", "_");

        this.modelAdult = parseModel("adult");

        for (GrowthStage stage : GrowthStage.values())
        {
            if (stage != GrowthStage.ADULT)
            {
                if (this.doesSupportGrowthStage(stage))
                {
                    this.setModelContainer(stage, parseModel(stage.name().toLowerCase()));
                }
                else
                {
                    this.setModelContainer(stage, this.modelAdult);
                }
            }
        }

        String baseTextures = "textures/entities/" + formattedName + "/";

        for (GrowthStage growthStage : GrowthStage.values())
        {
            String growthStageName = growthStage.name().toLowerCase();

            if (!this.doesSupportGrowthStage(growthStage))
            {
                growthStageName = GrowthStage.ADULT.name().toLowerCase();
            }

            if (this instanceof Hybrid)
            {
                String baseName = baseTextures + formattedName + "_" + growthStageName;

                ResourceLocation hybridTexture = new ResourceLocation(JurassiCraft.MODID, baseName + ".png");

                maleTextures.put(growthStage, hybridTexture);
                femaleTextures.put(growthStage, hybridTexture);

                ResourceLocation eyelidTexture = new ResourceLocation(JurassiCraft.MODID, baseName + "_eyelid.png");
                eyelidTextures.put(new GrowthStageGenderContainer(growthStage, false), eyelidTexture);
                eyelidTextures.put(new GrowthStageGenderContainer(growthStage, true), eyelidTexture);
            }
            else
            {
                maleTextures.put(growthStage, new ResourceLocation(JurassiCraft.MODID, baseTextures + formattedName + "_male_" + growthStageName + ".png"));
                femaleTextures.put(growthStage, new ResourceLocation(JurassiCraft.MODID, baseTextures + formattedName + "_female_" + growthStageName + ".png"));
                eyelidTextures.put(new GrowthStageGenderContainer(growthStage, true), new ResourceLocation(JurassiCraft.MODID, baseTextures + formattedName + "_male_" + growthStageName + "_eyelid.png"));
                eyelidTextures.put(new GrowthStageGenderContainer(growthStage, false), new ResourceLocation(JurassiCraft.MODID, baseTextures + formattedName + "_female_" + growthStageName + "_eyelid.png"));
            }

            List<ResourceLocation> variantsForGrowthStage = rareVariantTextures.get(growthStage);

            if (variantsForGrowthStage == null)
            {
                variantsForGrowthStage = new ArrayList<>();
            }

            for (String variant : getRareVariants())
            {
                variantsForGrowthStage.add(new ResourceLocation(JurassiCraft.MODID, baseTextures + formattedName + "_" + variant + "_" + growthStageName + ".png"));
            }

            rareVariantTextures.put(growthStage, variantsForGrowthStage);

            List<ResourceLocation> overlaysForGrowthStage = new ArrayList<>();

            for (int i = 1; i <= getOverlayCount(); i++)
            {
                overlaysForGrowthStage.add(new ResourceLocation(JurassiCraft.MODID, baseTextures + formattedName + "_overlay_" + growthStageName + "_" + i + ".png"));
            }

            overlays.put(growthStage, overlaysForGrowthStage);
        }

        this.poseHandler = new PoseHandler(this);
    }

    protected TabulaModelContainer parseModel(String growthStage)
    {
        String formattedName = getName().toLowerCase().replaceAll(" ", "_");
        String modelPath = "/assets/jurassicraft/models/entities/" + formattedName + "/" + growthStage + "/" + formattedName + "_" + growthStage + "_idle";

        try
        {
            return TabulaModelHelper.loadTabulaModel(modelPath);
        }
        catch (Exception e)
        {
            JurassiCraft.INSTANCE.getLogger().fatal("Couldn't load model " + modelPath, e);
        }

        return null;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDinosaurClass(Class<? extends DinosaurEntity> clazz)
    {
        this.dinoClazz = clazz;
    }

    public void setEggColorMale(int primary, int secondary)
    {
        this.primaryEggColorMale = primary;
        this.secondaryEggColorMale = secondary;
    }

    public void setEggColorFemale(int primary, int secondary)
    {
        this.primaryEggColorFemale = primary;
        this.secondaryEggColorFemale = secondary;
    }

    public void setTimePeriod(TimePeriod timePeriod)
    {
        this.timePeriod = timePeriod;
    }

    public void setHealth(double baby, double adult)
    {
        this.babyHealth = baby;
        this.adultHealth = adult;
    }

    public void setStrength(double baby, double adult)
    {
        this.babyStrength = baby;
        this.adultStrength = adult;
    }

    public void setSpeed(double baby, double adult)
    {
        this.babySpeed = baby;
        this.adultSpeed = adult;
    }

    public void setSizeX(float baby, float adult)
    {
        this.babySizeX = baby;
        this.adultSizeX = adult;
    }

    public void setSizeY(float baby, float adult)
    {
        this.babySizeY = baby;
        this.adultSizeY = adult;
    }

    public void setEyeHeight(float baby, float adult)
    {
        this.babyEyeHeight = baby;
        this.adultEyeHeight = adult;
    }

    public void disableRegistry()
    {
        this.shouldRegister = false;
    }

    public void setMarineAnimal(boolean marineAnimal)
    {
        this.isMarineAnimal = marineAnimal;
    }

    public void setMammal(boolean isMammal)
    {
        this.isMammal = isMammal;
    }

    public void setAttackSpeed(double attackSpeed)
    {
        this.attackSpeed = attackSpeed;
    }

    public void setStorage(int storage)
    {
        this.storage = storage;
    }

    public void setOverlayCount(int count)
    {
        this.overlayCount = count;
    }

    public void setDiet(Diet diet)
    {
        this.diet = diet;
    }

    public void setSleepingSchedule(SleepingSchedule sleepingSchedule)
    {
        this.sleepingSchedule = sleepingSchedule;
    }

    public void setBones(String... bones)
    {
        this.bones = bones;
    }

    public void setUsePosesForWalkingAnim(boolean usePosesForWalkingAnim)
    {
        this.usePosesForWalkingAnim = usePosesForWalkingAnim;
    }

    public void setRareVariants(String... rareVariants)
    {
        this.rareVariants = rareVariants;
    }

    public String getName()
    {
        return name;
    }

    public Class<? extends DinosaurEntity> getDinosaurClass()
    {
        return dinoClazz;
    }

    public int getEggPrimaryColorMale()
    {
        return primaryEggColorMale;
    }

    public int getEggSecondaryColorMale()
    {
        return secondaryEggColorMale;
    }

    public int getEggPrimaryColorFemale()
    {
        return primaryEggColorFemale;
    }

    public int getEggSecondaryColorFemale()
    {
        return secondaryEggColorFemale;
    }

    public TimePeriod getPeriod()
    {
        return timePeriod;
    }

    public double getBabyHealth()
    {
        return babyHealth;
    }

    public double getAdultHealth()
    {
        return adultHealth;
    }

    public double getBabySpeed()
    {
        return babySpeed;
    }

    public double getAdultSpeed()
    {
        return adultSpeed;
    }

    public double getBabyStrength()
    {
        return babyStrength;
    }

    public double getAdultStrength()
    {
        return adultStrength;
    }

    public float getBabySizeX()
    {
        return babySizeX;
    }

    public float getBabySizeY()
    {
        return babySizeY;
    }

    public float getAdultSizeX()
    {
        return adultSizeX;
    }

    public float getAdultSizeY()
    {
        return adultSizeY;
    }

    public float getBabyEyeHeight()
    {
        return babyEyeHeight;
    }

    public float getAdultEyeHeight()
    {
        return adultEyeHeight;
    }

    public int getMaximumAge()
    {
        return maximumAge;
    }

    public ResourceLocation getMaleTexture(GrowthStage stage)
    {
        return maleTextures.get(stage);
    }

    public ResourceLocation getFemaleTexture(GrowthStage stage)
    {
        return femaleTextures.get(stage);
    }

    public double getAttackSpeed()
    {
        return attackSpeed;
    }

    public boolean shouldRegister()
    {
        return shouldRegister;
    }

    protected String getDinosaurTexture(String subtype)
    {
        String dinosaurName = getName().toLowerCase().replaceAll(" ", "_");

        String texture = "jurassicraft:textures/entities/" + dinosaurName + "/" + dinosaurName;

        if (subtype.length() > 0)
        {
            texture += "_" + subtype;
        }

        return texture + ".png";
    }

    public boolean getUsePosesForWalkAnim()
    {
        return this.usePosesForWalkingAnim;
    }

    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }

    protected int fromDays(int days)
    {
        return (days * 24000) / 8;
    }

    @Override
    public int compareTo(Dinosaur dinosaur)
    {
        return this.getName().compareTo(dinosaur.getName());
    }

    public boolean isMarineAnimal()
    {
        return isMarineAnimal;
    }

    public boolean isMammal()
    {
        return isMammal;
    }

    public int getLipids()
    {
        return 1500;
    }

    public int getMinerals()
    {
        return 1500;
    }

    public int getVitamins()
    {
        return 1500;
    }

    public int getProximates() // TODO
    {
        return 1500;
    }

    public int getStorage()
    {
        return storage;
    }

    public ResourceLocation getOverlayTexture(GrowthStage stage, int overlay)
    {
        return overlays.containsKey(stage) ? overlays.get(stage).get(overlay) : null;
    }

    public int getOverlayCount()
    {
        return overlayCount;
    }

    public ResourceLocation getEyelidTexture(DinosaurEntity entity)
    {
        return eyelidTextures.get(new GrowthStageGenderContainer(entity.getGrowthStage(), entity.isMale()));
    }

    public Diet getDiet()
    {
        return diet;
    }

    public SleepingSchedule getSleepingSchedule()
    {
        return sleepingSchedule;
    }

    public String[] getBones()
    {
        return bones;
    }

    @Override
    public boolean equals(Object object)
    {
        return object instanceof Dinosaur && ((Dinosaur) object).getName().equalsIgnoreCase(getName());

    }

    public void setMaximumAge(int age)
    {
        this.maximumAge = age;
    }

    public void setHeadCubeName(String headCubeName)
    {
        this.headCubeName = headCubeName;
    }

    public String getHeadCubeName()
    {
        return headCubeName;
    }

    public double[] getCubePosition(String cubeName, GrowthStage stage)
    {
        TabulaModelContainer model = getModelContainer(stage);

        TabulaCubeContainer cube = TabulaModelHelper.getCubeByName(cubeName, model);

        if (cube != null)
        {
            return cube.getPosition();
        }

        return new double[] { 0.0, 0.0, 0.0 };
    }

    public double[] getParentedCubePosition(String cubeName, GrowthStage stage, float rot)
    {
        TabulaModelContainer model = getModelContainer(stage);

        TabulaCubeContainer cube = TabulaModelHelper.getCubeByName(cubeName, model);

        if (cube != null)
        {
            return getTransformation(getParentRotationMatrix(model, cube, true, false, rot))[0];
        }

        return new double[] { 0.0, 0.0, 0.0 };
    }

    public static Matrix4d getParentRotationMatrix(TabulaModelContainer model, TabulaCubeContainer cube, boolean includeParents, boolean ignoreSelf, float rot)
    {
        List<TabulaCubeContainer> parentCubes = new ArrayList<>();

        do
        {
            if (ignoreSelf)
            {
                ignoreSelf = false;
            }
            else
            {
                parentCubes.add(cube);
            }
        }
        while (includeParents && cube.getParentIdentifier() != null && (cube = TabulaModelHelper.getCubeByIdentifier(cube.getParentIdentifier(), model)) != null);

        Matrix4d mat = new Matrix4d();
        mat.setIdentity();
        Matrix4d transform = new Matrix4d();

        transform.rotY(rot / 180 * Math.PI);
        mat.mul(transform);

        for (int i = parentCubes.size() - 1; i >= 0; i--)
        {
            cube = parentCubes.get(i);
            transform.setIdentity();
            transform.setTranslation(new Vector3d(cube.getPosition()));
            mat.mul(transform);

            double rotX = cube.getRotation()[0];
            double rotY = cube.getRotation()[1];
            double rotZ = cube.getRotation()[2];

            transform.rotZ(rotZ / 180 * Math.PI);
            mat.mul(transform);
            transform.rotY(rotY / 180 * Math.PI);
            mat.mul(transform);
            transform.rotX(rotX / 180 * Math.PI);
            mat.mul(transform);
        }

        return mat;
    }

    private static double[][] getTransformation(Matrix4d matrix)
    {
        double sinRotationAngleY, cosRotationAngleY, sinRotationAngleX, cosRotationAngleX, sinRotationAngleZ, cosRotationAngleZ;

        sinRotationAngleY = -matrix.m20;
        cosRotationAngleY = Math.sqrt(1 - sinRotationAngleY * sinRotationAngleY);

        if (Math.abs(cosRotationAngleY) > 0.0001)
        {
            sinRotationAngleX = matrix.m21 / cosRotationAngleY;
            cosRotationAngleX = matrix.m22 / cosRotationAngleY;
            sinRotationAngleZ = matrix.m10 / cosRotationAngleY;
            cosRotationAngleZ = matrix.m00 / cosRotationAngleY;
        }
        else
        {
            sinRotationAngleX = -matrix.m12;
            cosRotationAngleX = matrix.m11;
            sinRotationAngleZ = 0;
            cosRotationAngleZ = 1;
        }

        double rotationAngleX = epsilon(Math.atan2(sinRotationAngleX, cosRotationAngleX)) / Math.PI * 180;
        double rotationAngleY = epsilon(Math.atan2(sinRotationAngleY, cosRotationAngleY)) / Math.PI * 180;
        double rotationAngleZ = epsilon(Math.atan2(sinRotationAngleZ, cosRotationAngleZ)) / Math.PI * 180;
        return new double[][] { { epsilon(matrix.m03), epsilon(matrix.m13), epsilon(matrix.m23) }, { rotationAngleX, rotationAngleY, rotationAngleZ } };
    }

    private static double epsilon(double x)
    {
        return x < 0 ? x > -0.0001 ? 0 : x : x < 0.0001 ? 0 : x;
    }

    public double[] getHeadPosition(GrowthStage stage, float rot)
    {
        return getParentedCubePosition(getHeadCubeName(), stage, rot);
    }

    public TabulaModelContainer getModelContainer(GrowthStage stage)
    {
        switch (stage)
        {
            case INFANT:
                return modelInfant;
            case JUVENILE:
                return modelJuvenile;
            case ADOLESCENT:
                return modelAdolescent;
            default:
                return modelAdult;
        }
    }

    private void setModelContainer(GrowthStage stage, TabulaModelContainer model)
    {
        switch (stage)
        {
            case INFANT:
                modelInfant = model;
            case JUVENILE:
                modelJuvenile = model;
            case ADOLESCENT:
                modelAdolescent = model;
            default:
                modelAdult = model;
        }
    }

    public void setScale(float scaleAdult, float scaleInfant)
    {
        this.scaleInfant = scaleInfant;
        this.scaleAdult = scaleAdult;
    }

    public void setOffset(float x, float y, float z)
    {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }

    public void setImprintable(boolean imprintable)
    {
        this.isImprintable = imprintable;
    }

    public void setDefendOwner(boolean defendOwner)
    {
        this.defendOwner = defendOwner;
    }

    public double getScaleInfant()
    {
        return scaleInfant;
    }

    public double getScaleAdult()
    {
        return scaleAdult;
    }

    public float getOffsetX()
    {
        return offsetX;
    }

    public float getOffsetY()
    {
        return offsetY;
    }

    public float getOffsetZ()
    {
        return offsetZ;
    }

    public String[] getRareVariants()
    {
        return rareVariants;
    }

    public ResourceLocation getRareVariantTexture(int rareVariant, GrowthStage growthStage)
    {
        return rareVariantTextures.get(growthStage).get(rareVariant - 1);
    }

    public PoseHandler getPoseHandler()
    {
        return poseHandler;
    }

    public boolean doesSupportGrowthStage(GrowthStage stage)
    {
        return stage == GrowthStage.ADULT;
    }

    public boolean isImprintable()
    {
        return isImprintable;
    }

    public boolean shouldDefendOwner()
    {
        return defendOwner;
    }
}

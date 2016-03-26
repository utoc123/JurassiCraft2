package org.jurassicraft.server.entity.base;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.entity.disease.*;

/*
 * Most likely will be renamed "Disease Helper" or some other boring name, but while in prototype we'll keep the fancy name
 */
public class PandorasBox
{
    private DinosaurEntity dinosaur;
    
    private Disease disease;
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    public PandorasBox(DinosaurEntity dinosaur) 
    {
        this.dinosaur = dinosaur;
        disease = setDisease(null);
        
    }
    
    public void update()
    {
        if(!dinosaur.isDead && !dinosaur.isCarcass() && dinosaur.isHealthy())
        {
            Random chaos = new Random();
            int chance = chaos.nextInt(200)+1; // 1 to 200
            contractDiseaseChance(chance);
        }
        
        else if(!dinosaur.isHealthy())
        {
            disease.affects();
        }
    }

    /*
     * Chance to contract a random disease or to remain healthy. Deadlier diseases have a lower
     * range of numbers compared to more begin diseases. Remaining healthy has the highest 
     * chance
     */
    public void contractDiseaseChance(int chance)
    {
        if(chance <= 100)                      //50% chance to remain healthy
        {
            
        }
        else if(chance > 100 && chance <= 118) //9% chance to contract Bumblefoot
        {
            setDisease(new BumblefootDisease(dinosaur));
            dinosaur.setHealthy(false);
            LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
        }
        else if(chance > 118 && chance <= 136) //9% chance to contract (x)Infestation
        {
            if(dinosaur.getDinosaur().isMarineAnimal())
            {
                setDisease(new LouseInfestDisease(dinosaur));
                dinosaur.setHealthy(false);
                LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
            }
            else
            {
                setDisease(new TickInfestDisease(dinosaur));
                dinosaur.setHealthy(false);
                LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
            }
        }
        else if(chance > 136 && chance <= 154) //9% chance to contract stomach ulcers
        {
            setDisease(new StomachUlcerDisease(dinosaur));
            dinosaur.setHealthy(false);
            LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
        }
        else if(chance > 154 && chance <= 172) //9% chance to contract tapeworms
        {
            setDisease(new TapewormDisease(dinosaur));
            dinosaur.setHealthy(false);
            LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
        }
        else if(chance > 172 && chance <= 190) //9% chance to contract Gastric Poisoning/Rabies
        {
            if(dinosaur.getDinosaur().getDiet().doesEatPlants())
            {
                setDisease(new GastricPoisoningDisease(dinosaur));
                dinosaur.setHealthy(false);
                LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
            }
            else
            {
                setDisease(new RabiesDisease(dinosaur));
                dinosaur.setHealthy(false);
                LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
            }
        }
        else if(chance > 190 && chance <= 198) //3% chance to contract Cancer
        {
            setDisease(new CancerDisease(dinosaur));
            dinosaur.setHealthy(false);
            LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
        }
        else                                   //1% chance to contract BlackDeath
        {
            setDisease(new BlackDeathDisease(dinosaur));
            dinosaur.setHealthy(false);
            LOGGER.info(dinosaur.getName() + " has contracted: " + disease.getName());
        }
    }

    public Disease getDisease()
    {
        return disease;
    }

    public Disease setDisease(Disease disease)
    {
        return this.disease = disease;
    }

}

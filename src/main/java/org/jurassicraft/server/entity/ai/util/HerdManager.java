package org.jurassicraft.server.entity.ai.util;

import net.minecraft.util.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2016 TimelessModdingTeam
 */
public class HerdManager
{
    // Blocks
    public static final int MIN_SIZE = 3;

    // We do this on a fairly frequent basis to identify all the clusters
    public static final long REBALANCE_DELAY_TICKS = 40;

    private static final Logger LOGGER = LogManager.getLogger();

    private static HerdManager instance;

    // For now we have one Herd per class of critter.  This needs to change when we have
    // different herds because of different herd leaders.
    private final Map<Class, Herd> herds = new HashMap<Class, Herd>();
    private long nextRebalance = 0;

    //===============================================

    // Returns the current instance of the herd manager
    public static HerdManager getInstance()
    {
        if (instance == null)
        {
            instance = new HerdManager();
        }

        return instance;
    }

    /**
     * Adds a dinosaur to the herd manager.
     *
     * @param dinosaur The dinosaur to add.
     */
    public void add(DinosaurEntity dinosaur)
    {
        Herd herd = herds.get(dinosaur.getClass());
        if (herd == null)
        {
            herd = new Herd();
            herds.put(dinosaur.getClass(), herd);
        }

        herd.add(dinosaur);
    }

    /**
     * Removes the dinosaur from the herd manager.
     *
     * @param dinosaur The dinosaur to remove.
     */
    public void remove(DinosaurEntity dinosaur)
    {
        Herd species = herds.get(dinosaur.getClass());
        if (species != null)
        {
            species.remove(dinosaur);
        }
    }

    /**
     * Provides the herd for the particular dinosaur.
     * @param dinosaur The dino.
     * @return The herd.
     */
    public Herd getHerd(DinosaurEntity dinosaur)
    {
        return herds.get(dinosaur.getClass());
    }

    /**
     * Returns the location this dinosaur is supposed to move to either within
     * the herd it is already in, or to a herd nearby.
     *
     * @param dinosaur The dinosaur we want to find the destination for.
     * @return The block pos to move to, or null for stay in place.
     */
    public BlockPos getWanderLocation(DinosaurEntity dinosaur)
    {
        // TODO:  Move the rebalance check to a general update tick handler
        long now = dinosaur.getEntityWorld().getTotalWorldTime();
        if (now > nextRebalance)
        {
            rebalanceAll();
            nextRebalance = now + REBALANCE_DELAY_TICKS;
        }

        // Basic design:
        // 1) Ask the herd for the center of the appropriate cluster
        // 2) See if we are within the "area of the cluster."  Note, this is expected to change
        //    based on cluster state ( e.g. IDLE, PANIC )
        // 3) If not within area of the cluster, compute an interest point based
        //    the species.

        Herd herd = herds.get(dinosaur.getClass());
        if (herd != null)
        {
            Cluster cluster = herd.getCluster(dinosaur);

            if (cluster == null)
            {
                // Not inside a cluster, let's find one to move to.
                //cluster = herd.getLargestCluster();
                cluster = herd.getNearestCluster(dinosaur);
            }

            if (cluster != null)
            {
                // Note, the outer radius might change over time.
                int outerRadius = cluster.getOuterRadius(dinosaur);
                BlockPos center = cluster.getCenter();

                // Okay, we want to move closer.
                // What is the edge of the cluster
                int distanceToCenter = (int) Math.sqrt(center.distanceSq(dinosaur.getPosition()));
                if (distanceToCenter < outerRadius)
                {
                    return null;
                }

                // Let's move some number of body widths toward the center
                int someDist = Math.round(dinosaur.width * 12);

                return AIUtils.computePosToward(dinosaur.getPosition(), center, someDist);
                //LOGGER.info("id=" + dinosaur.getEntityId() + ", start=" + dinosaur.getPosition() + ", center=" + center + ", target=" + target +
                //            ", dtc=" + distanceToCenter + ", somedist=" + someDist + ", outer=" + outerRadius + ", diff=" + diffDist);
            }

            //LOGGER.info("no cluster.");
            return null;
        }

        return null;
    }

    /**
     * Updates all the herds in the list.
     */
    public void rebalanceAll()
    {
        //LOGGER.info("!!! Rebalancing");
        for (Herd species : herds.values())
        {
            species.rebalance();
        }

        //LOGGER.info(this);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Class, Herd> herd : herds.entrySet())
        {
            builder.append("[ species=").append(herd.getKey().getSimpleName());
            builder.append(", clusters=").append(herd.getValue().numClusters());
            builder.append(", noise=").append(herd.getValue().noiseSize());
            builder.append(" ]\n");
            if (herd.getValue().numClusters() > 0)
            {
                builder.append("  [ ");
                for (Cluster cluster : herd.getValue().clusters)
                {
                    builder.append(" #:").append(cluster.dinosaurs.size());
                    for (DinosaurEntity dino : cluster.dinosaurs)
                    {
                        builder.append(", ").append(dino.getPosition());
                    }
                    builder.append("\n");
                }
                builder.append(" ]\n");
            }
            builder.append("  noise=[ ");
            for (DinosaurEntity dino : herd.getValue().noise)
            {
                builder.append(", ").append(dino.getPosition());
            }
            builder.append(" ]\n");
        }
        return builder.toString();
    }

    //=============================================================================================

    // Takes care of all the members of a species.
    public class Herd
    {
        private LinkedList<Cluster> clusters = new LinkedList<Cluster>();
        private LinkedList<DinosaurEntity> noise = new LinkedList<DinosaurEntity>();
        private final LinkedList<DinosaurEntity> allDinosaurs = new LinkedList<DinosaurEntity>();

        /**
         * Returns the cluster this dinosaur is in, or null if not in a cluster.
         * @param dinosaur The dinosaur we are looking for.
         * @return Cluster or null.
         */
        public Cluster getCluster(DinosaurEntity dinosaur)
        {
            for (Cluster cluster : clusters)
            {
                if (cluster.contains(dinosaur))
                {
                    return cluster;
                }
            }
            return null;
        }

        /**
         * @return Returns the largest cluster.
         */
        public Cluster getLargestCluster()
        {
            Cluster largest = null;
            for (Cluster cluster : clusters)
            {
                if (largest == null || cluster.size() > largest.size())
                {
                    largest = cluster;
                }
            }
            return largest;
        }

        /**
         * @return Returns the nearest cluster. Note, this computationally expensive.
         */
        public Cluster getNearestCluster(DinosaurEntity dinosaur)
        {
            Cluster nearest = null;
            double closestDistanceSq = Double.MAX_VALUE;
            for (Cluster cluster : clusters)
            {
                double distanceSq = dinosaur.getPosition().distanceSq(cluster.getCenter());
                if (nearest == null || distanceSq < closestDistanceSq)
                {
                    nearest = cluster;
                    closestDistanceSq = distanceSq;
                }
            }
            return nearest;
        }

        // =======================

        void add(DinosaurEntity dinosaur)
        {
            allDinosaurs.add(dinosaur);

            // Add to a cluster if we can find one.
            LinkedList<Cluster> belongsTo = new LinkedList<Cluster>();
            for (Cluster cluster : clusters)
            {
                if (cluster.withinProximity(dinosaur))
                {
                    belongsTo.add(cluster);
                }
            }

            // If we didn't find anything, then add it to noise
            if (belongsTo.size() == 0)
            {
                //LOGGER.info("Added to noise: " + dinosaur.getClass().getSimpleName());
                noise.add(dinosaur);
                return;
            }

            // Merge them if we have more than one
            Cluster main = belongsTo.poll();
            Cluster toMerge;
            while ((toMerge = belongsTo.poll()) != null)
            {
                main.mergeFrom(toMerge);
                clusters.remove(toMerge);
            }
        }

        void remove(DinosaurEntity dinosaur)
        {
            allDinosaurs.remove(dinosaur);

            for (Cluster cluster : clusters)
            {
                if (cluster.remove(dinosaur))
                {
                    return;
                }
            }
        }

        BlockPos getClusterCenter(DinosaurEntity dinosaur)
        {
            for (Cluster cluster : clusters)
            {
                if (cluster.contains(dinosaur))
                {
                    return cluster.getCenter();
                }
            }

            // Choose the largest
            Cluster largest = null;
            for (Cluster cluster : clusters)
            {
                if (largest == null || cluster.size() > largest.size())
                {
                    largest = cluster;
                }
            }

            return (largest != null) ? largest.getCenter() : null;
        }

        int numClusters()
        {
            return clusters.size();
        }

        int noiseSize()
        {
            return noise.size();
        }

        void rebalance()
        {
            // For now this is a hack!  We recompute the entire thing instead of just updating
            noise = new LinkedList<DinosaurEntity>(allDinosaurs);
            clusters = cluster(noise);

            //LOGGER.info("After rebalance #clusters: " + _clusters.size());

            // Now, prune out all the tiny clusters
            Iterator<Cluster> iter = clusters.iterator();
            while (iter.hasNext())
            {
                Cluster cluster = iter.next();
                if (cluster.size() < MIN_SIZE)
                {
                    //LOGGER.info("Merging in cluster of size: " + cluster.size());
                    cluster.drainTo(noise);
                    iter.remove();
                }
            }
        }

        int getIntersectDistance(DinosaurEntity dinosaur)
        {
            double factor = 1 + Math.sqrt(clusters.size());
            if (factor < 1.5)
                factor = 1.5;
            return (int)Math.round(dinosaur.width * factor);
        }
    }

    //=============================================================================================

    // Manages a set of these
    public class Cluster
    {
        // We use linked list because we traverse and don't need random access
        private LinkedList<DinosaurEntity> dinosaurs = new LinkedList<DinosaurEntity>();


        /**
         * Gets the "center" (average) of the cluster,
         *
         * @return The center.
         */
        public BlockPos getCenter()
        {
            if (center != null)
                return center;

            double totalX = 0.0F;
            double totalY = 0.0F;
            double totalZ = 0.0F;
            int count = 0;

            for (DinosaurEntity dino : dinosaurs)
            {
                BlockPos pos = dino.getPosition();
                totalX += pos.getX();
                totalY += pos.getY();
                totalZ += pos.getZ();
                count += 1;
            }

            center = new BlockPos(totalX / count, totalY / count, totalZ / count);
            return center;
        }

        /**
         * @return The desired outer radius from the cluster.
         */
        public int getOuterRadius(DinosaurEntity dinosaur)
        {
            // For Micro, make this larger.

            double factor = 3 + Math.sqrt(size());
            double width = dinosaur.width > 1.0 ? dinosaur.width : 1.0;
            return (int) Math.round(width * factor);
        }

        //=====================================================

        /**
         * Is the dinosaur withinProximity to other dinosaurs?
         *
         * @param dinosaur The dinosaur in question.
         * @return True if withinProximity to a cluster.
         */
        boolean withinProximity(DinosaurEntity dinosaur)
        {
            for (DinosaurEntity entity : dinosaurs)
            {
                if (inProximity(dinosaur, entity))
                {
                    return true;
                }
            }
            return false;
        }

        /**
         * Is the dinosaur in the cluster?
         *
         * @param dinosaur The dinosaur.
         * @return True if in the list.
         */
        boolean contains(DinosaurEntity dinosaur)
        {
            return dinosaurs.contains(dinosaur);
        }


        /**
         * The dinosaur to the cluster.
         *
         * @param dinosaur The dinosaur to add.
         */
        void add(DinosaurEntity dinosaur)
        {
            dinosaurs.add(dinosaur);
        }

        /**
         * Remove the element if it exists.
         *
         * @param dinosaur The element to remove.
         * @return True if removed.
         */
        boolean remove(DinosaurEntity dinosaur)
        {
            return dinosaurs.remove(dinosaur);
        }

        // Bring in the content from another cluster
        void mergeFrom(Cluster other)
        {
            dinosaurs.addAll(other.dinosaurs);
            other.dinosaurs.clear();
        }

        int size()
        {
            return dinosaurs.size();
        }

        void drainTo(Collection<DinosaurEntity> sink)
        {
            sink.addAll(dinosaurs);
            dinosaurs.clear();
        }

        /**
         * Adds this entity and all adjacent entities.
         * @param dinosaur The entity to add
         * @param dinosaurs The remaining entities that are available to add.
         */
        void addWithAdjacents(DinosaurEntity dinosaur, LinkedList<DinosaurEntity> dinosaurs)
        {
            // Recursively add all adjacents
            List<DinosaurEntity> allProximates = extractProximates(dinosaur, dinosaurs);
            this.dinosaurs.add(dinosaur);
            for ( DinosaurEntity close : allProximates)
            {
                addWithAdjacents(close, dinosaurs);
            }
        }

        private BlockPos center = null;
    }

    //===============================================

    /**
     * Goes through all the dinosaurs in the list, putting them into clusters.
     * They may be in clusters of size 1.
     * @param dinosaurs The dinosaurs to cluster.
     * @return A list of clusters.
     */
    private LinkedList<Cluster> cluster(LinkedList<DinosaurEntity> dinosaurs)
    {
        DinosaurEntity dino;
        LinkedList<Cluster> clusters = new LinkedList<Cluster>();

        while ((dino = dinosaurs.poll()) != null)
        {
            Cluster cluster = new Cluster();
            clusters.add(cluster);
            cluster.addWithAdjacents(dino, dinosaurs);
        }

        return clusters;
    }

    /**
     * Returns a list of all entities that are in close proximity
     * @param dinosaur The entity we are examining.
     * @param dinosaurs The remaining entities that might be close.
     * @return A list of entities close to the one we are examining.
     */
    private LinkedList<DinosaurEntity> extractProximates(DinosaurEntity dinosaur, LinkedList<DinosaurEntity> dinosaurs)
    {
        LinkedList<DinosaurEntity> proximates = new LinkedList<DinosaurEntity>();
        Iterator<DinosaurEntity> iter = dinosaurs.iterator();
        while (iter.hasNext())
        {
            DinosaurEntity tmp = iter.next();
            if (inProximity(dinosaur, tmp))
            {
                proximates.add(tmp);
                iter.remove();
            }
        }

        //LOGGER.info("extractProximates inDinos=" + dinosaurs.size() + ", extracted=" + proximates.size() );
        return proximates;
    }

    //=============================================================================================

    // Used for the
    private static boolean inProximity(DinosaurEntity lhs, DinosaurEntity rhs)
    {
        return lhs.getPosition().distanceSq(rhs.getPosition()) < speciesDistanceSq(lhs);
    }

    /**
     * How far apart two dinos can be tobe considered "in proximity"
     * @param dinosaur The entity from which to extract the proximity number.
     * @return The square of the allowable proximity.
     */
    private static int speciesDistanceSq(DinosaurEntity dinosaur)
    {
        // Microceratus - 0.4 - So, micro's need to be 1.4 blocks away to make a cluster!
        // Apatosaurus - 6.5 -
        double radius = dinosaur.width * 3;
        if (radius < 4)
        {
            radius = 4;
        }

        int distance = (int) Math.round(radius * radius);

        // Minimum to deal with things like the Microceratus
        if (distance < 6)
            distance = 6;

        return distance;
    }

}

/*
    - For cluster they are in proximity
    - When moving toward they move some number of points
    - When wandering, only move so far away
 */

/*
    Performance guidelines
    - We expect more rebalancing than addition removal, so maximize for that
 */

/*

We use DBSCAN to find clusters.

====

DBSCAN(D, eps, MinPts) {
   C = 0
   for each point P in dataset D {
      if P is visited
         continue next point
      mark P as visited
      NeighborPts = regionQuery(P, eps)
      if sizeof(NeighborPts) < MinPts
         mark P as NOISE
      else {
         C = next cluster
         expandCluster(P, NeighborPts, C, eps, MinPts)
      }
   }
}

expandCluster(P, NeighborPts, C, eps, MinPts) {
   add P to cluster C
   for each point P' in NeighborPts {
      if P' is not visited {
         mark P' as visited
         NeighborPts' = regionQuery(P', eps)
         if sizeof(NeighborPts') >= MinPts
            NeighborPts = NeighborPts joined with NeighborPts'
      }
      if P' is not yet member of any cluster
         add P' to cluster C
   }
}

regionQuery(P, eps)
   return all points within P's eps-neighborhood (including P)

=================================

The tricky bits is when updating.

Is there an easy way to merge clusters?  Can we start with cluster that isn't


 */
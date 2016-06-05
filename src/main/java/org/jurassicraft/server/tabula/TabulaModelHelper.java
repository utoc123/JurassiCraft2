package org.jurassicraft.server.tabula;

import net.ilexiconn.llibrary.client.model.tabula.container.TabulaCubeContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaCubeGroupContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;

import java.util.ArrayList;
import java.util.List;

public class TabulaModelHelper
{
    public static TabulaCubeContainer getCubeByName(String name, TabulaModelContainer model)
    {
        List<TabulaCubeContainer> allCubes = getAllCubes(model);

        for (TabulaCubeContainer cube : allCubes)
        {
            if (cube.getName().equals(name))
            {
                return cube;
            }
        }

        return null;
    }

    public static TabulaCubeContainer getCubeByIdentifier(String identifier, TabulaModelContainer model)
    {
        List<TabulaCubeContainer> allCubes = getAllCubes(model);

        for (TabulaCubeContainer cube : allCubes)
        {
            if (cube.getIdentifier().equals(identifier))
            {
                return cube;
            }
        }

        return null;
    }

    public static List<TabulaCubeContainer> getAllCubes(TabulaModelContainer model)
    {
        List<TabulaCubeContainer> cubes = new ArrayList<>();

        for (TabulaCubeGroupContainer cubeGroup : model.getCubeGroups())
        {
            cubes.addAll(traverse(cubeGroup));
        }

        for (TabulaCubeContainer cube : model.getCubes())
        {
            cubes.addAll(traverse(cube));
        }

        return cubes;
    }

    private static List<TabulaCubeContainer> traverse(TabulaCubeGroupContainer group)
    {
        List<TabulaCubeContainer> retCubes = new ArrayList<>();

        for (TabulaCubeContainer child : group.getCubes())
        {
            retCubes.addAll(traverse(child));
        }

        for (TabulaCubeGroupContainer child : group.getCubeGroups())
        {
            retCubes.addAll(traverse(child));
        }

        return retCubes;
    }

    private static List<TabulaCubeContainer> traverse(TabulaCubeContainer cube)
    {
        List<TabulaCubeContainer> retCubes = new ArrayList<>();

        retCubes.add(cube);

        for (TabulaCubeContainer child : cube.getChildren())
        {
            retCubes.addAll(traverse(child));
        }

        return retCubes;
    }
}
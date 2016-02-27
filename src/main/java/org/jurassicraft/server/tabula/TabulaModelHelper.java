package org.jurassicraft.server.tabula;

import com.google.gson.Gson;
import net.ilexiconn.llibrary.client.model.tabula.CubeGroup;
import net.ilexiconn.llibrary.client.model.tabula.CubeInfo;
import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TabulaModelHelper
{
    private static Gson gson = new Gson();

    public static JsonTabulaModel parseModel(String path) throws Exception
    {
        if (!path.endsWith(".tbl"))
        {
            path += ".tbl";
        }

        try
        {
            ZipInputStream inputStream = new ZipInputStream(TabulaModelHelper.class.getResourceAsStream(path));
            ZipEntry entry;
            JsonTabulaModel parseTabulaModel = null;

            while ((entry = inputStream.getNextEntry()) != null)
            {
                if (entry.getName().equals("model.json"))
                {
                    parseTabulaModel = parseModel(inputStream);
                    break;
                }
            }

            return parseTabulaModel;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonTabulaModel parseModel(InputStream stream)
    {
        return gson.fromJson(new InputStreamReader(stream), JsonTabulaModel.class);
    }

    public static CubeInfo getCubeByName(String name, JsonTabulaModel model)
    {
        List<CubeInfo> allCubes = getAllCubes(model);

        for (CubeInfo cube : allCubes)
        {
            if (cube.name.equals(name))
            {
                return cube;
            }
        }

        return null;
    }

    public static CubeInfo getCubeByIdentifier(String identifier, JsonTabulaModel model)
    {
        List<CubeInfo> allCubes = getAllCubes(model);

        for (CubeInfo cube : allCubes)
        {
            if (cube.identifier.equals(identifier))
            {
                return cube;
            }
        }

        return null;
    }

    public static List<CubeInfo> getAllCubes(JsonTabulaModel model)
    {
        List<CubeInfo> cubes = new ArrayList<CubeInfo>();

        for (CubeGroup cubeGroup : model.getCubeGroups())
        {
            cubes.addAll(traverse(cubeGroup));
        }

        for (CubeInfo cube : model.getCubes())
        {
            cubes.addAll(traverse(cube));
        }

        return cubes;
    }

    private static List<CubeInfo> traverse(CubeGroup group)
    {
        List<CubeInfo> retCubes = new ArrayList<CubeInfo>();

        for (CubeInfo child : group.cubes)
        {
            retCubes.addAll(traverse(child));
        }

        for (CubeGroup child : group.cubeGroups)
        {
            retCubes.addAll(traverse(child));
        }

        return retCubes;
    }

    private static List<CubeInfo> traverse(CubeInfo cube)
    {
        List<CubeInfo> retCubes = new ArrayList<CubeInfo>();

        retCubes.add(cube);

        for (CubeInfo child : cube.children)
        {
            retCubes.addAll(traverse(child));
        }

        return retCubes;
    }

    public static CubeInfo copy(CubeInfo original)
    {
        CubeInfo copy = new CubeInfo();

        copy.children = original.children;
        copy.position = original.position;
        copy.rotation = original.rotation;
        copy.identifier = original.identifier;
        copy.parentIdentifier = original.parentIdentifier;
        copy.dimensions = original.dimensions;
        copy.hidden = original.hidden;
        copy.mcScale = original.mcScale;
        copy.offset = original.offset;
        copy.txOffset = original.txOffset;
        copy.txMirror = original.txMirror;
        copy.opacity = original.opacity;
        copy.name = original.name;
        copy.scale = original.scale;

        return copy;
    }
}
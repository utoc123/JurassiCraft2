package org.jurassicraft.server.storagedisc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class StorageTypeRegistry
{
    private static Map<String, Supplier<? extends IStorageType>> storageTypes = new HashMap<>();

    public static void init()
    {
        register("DinoDNA", DinosaurDNAStorageType::new);
        register("PlantDNA", PlantDNAStorageType::new);
    }

    private static void register(String id, Supplier<? extends IStorageType> storageType)
    {
        storageTypes.put(id, Objects.requireNonNull(storageType));
    }

    public static IStorageType getStorageType(String id)
    {
        if (id == null || id.isEmpty())
        {
            id = "DinoDNA";
        }

        return storageTypes.get(id).get();
    }
}

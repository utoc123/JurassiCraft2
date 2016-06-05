package org.jurassicraft.server.storagedisc;

import com.google.common.base.Supplier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum StorageTypeRegistry
{
    INSTANCE;

    private Map<String, Supplier<? extends StorageType>> storageTypes = new HashMap<>();

    public void init()
    {
        register("DinoDNA", DinosaurDNAStorageType::new);
        register("PlantDNA", PlantDNAStorageType::new);
    }

    private void register(String id, Supplier<? extends StorageType> storageType)
    {
        storageTypes.put(id, Objects.requireNonNull(storageType));
    }

    public StorageType getStorageType(String id)
    {
        if (id == null || id.isEmpty())
        {
            id = "DinoDNA";
        }

        return storageTypes.get(id).get();
    }
}

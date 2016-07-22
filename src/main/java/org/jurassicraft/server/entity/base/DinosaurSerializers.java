package org.jurassicraft.server.entity.base;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.io.IOException;

public class DinosaurSerializers {
    public static final DataSerializer<DinosaurEntity.Order> ORDER = new DataSerializer<DinosaurEntity.Order>() {
        @Override
        public void write(PacketBuffer buf, DinosaurEntity.Order value) {
            buf.writeByte(value.ordinal());
        }

        @Override
        public DinosaurEntity.Order read(PacketBuffer buf) throws IOException {
            return DinosaurEntity.Order.values()[buf.readByte()];
        }

        @Override
        public DataParameter<DinosaurEntity.Order> createKey(int id) {
            return new DataParameter<>(id, this);
        }
    };

    public static void register() {
        DataSerializers.registerSerializer(ORDER);
    }
}

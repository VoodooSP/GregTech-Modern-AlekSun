package com.gregtechceu.gtceu.api.recipe.content;

import net.minecraft.network.RegistryFriendlyByteBuf;

import com.mojang.serialization.Codec;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author KilaBash
 * @date 2022/06/22
 * @implNote SerializerLong
 */
public class SerializerLong implements IContentSerializer<Long> {

    public static SerializerLong INSTANCE = new SerializerLong();

    private SerializerLong() {}

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buf, Long content) {
        buf.writeVarLong(content);
    }

    @Override
    public Long fromNetwork(RegistryFriendlyByteBuf buf) {
        return buf.readVarLong();
    }

    @Override
    public Codec<Long> codec() {
        return Codec.LONG;
    }

    @Override
    public Long of(Object o) {
        if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof Number) {
            return ((Number) o).longValue();
        } else if (o instanceof CharSequence) {
            return NumberUtils.toLong(o.toString(), 1);
        }
        return 0L;
    }

    @Override
    public Long defaultValue() {
        return 0L;
    }
}

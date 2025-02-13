package com.gregtechceu.gtceu.api.recipe.content;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

import com.mojang.serialization.Codec;

import java.util.Map;
import java.util.Optional;

public class SerializerBlockState implements IContentSerializer<BlockState> {

    public static SerializerBlockState INSTANCE = new SerializerBlockState();

    private SerializerBlockState() {}

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buf, BlockState content) {
        buf.writeVarInt(BuiltInRegistries.BLOCK.getId(content.getBlock()));
        Map<Property<?>, Comparable<?>> values = content.getValues();
        if (!values.isEmpty()) {
            buf.writeBoolean(true);

            for (Map.Entry<Property<?>, Comparable<?>> entry : values.entrySet()) {
                buf.writeUtf(entry.getKey().getName());
                buf.writeUtf(((Property) entry.getKey()).getName(entry.getValue()));
            }
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public BlockState fromNetwork(RegistryFriendlyByteBuf buf) {
        Block block = BuiltInRegistries.BLOCK.byId(buf.readVarInt());
        BlockState blockState = block.defaultBlockState();
        if (buf.readBoolean()) {
            StateDefinition<Block, BlockState> stateDefinition = block.getStateDefinition();
            Map<Property<?>, Comparable<?>> values = blockState.getValues();

            for (int i = 0; i < values.size(); ++i) {
                String propertyName = buf.readUtf();
                String propertyValueName = buf.readUtf();
                Property<?> property = stateDefinition.getProperty(propertyName);
                if (property != null) {
                    Optional<? extends Comparable<?>> value = property.getValue(propertyValueName);
                    value.ifPresent(comparable -> ((StateHolder) blockState).setValue(property, comparable));
                }
            }
        }
        return blockState;
    }

    @Override
    public Codec<BlockState> codec() {
        return BlockState.CODEC;
    }

    @Override
    public BlockState of(Object o) {
        if (o instanceof BlockState state) {
            return state;
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState defaultValue() {
        return Blocks.AIR.defaultBlockState();
    }
}

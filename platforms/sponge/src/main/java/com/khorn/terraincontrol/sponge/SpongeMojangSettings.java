package com.khorn.terraincontrol.sponge;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.biome.BiomeType;

import java.util.ArrayList;
import java.util.List;

import com.khorn.terraincontrol.LocalMaterialData;
import com.khorn.terraincontrol.configuration.WeightedMobSpawnGroup;
import com.khorn.terraincontrol.configuration.standard.MojangSettings;
import net.minecraft.world.biome.Biome;

public class SpongeMojangSettings implements MojangSettings {

    private final BiomeType type;

    public SpongeMojangSettings(BiomeType type) {
        super();
        this.type = type;
    }

    @Override
    public float getTemperature() {
        return (float) this.type.getTemperature();
    }

    @Override
    public float getWetness() {
        return (float) this.type.getHumidity();
    }

    @Override
    public float getSurfaceHeight() {
        return ((Biome) this.type).getBaseHeight();
    }

    @Override
    public float getSurfaceVolatility() {
        return ((Biome) this.type).getHeightVariation();
    }

    @Override
    public SpongeMaterialData getSurfaceBlock() {
        return new SpongeMaterialData((BlockType) ((Biome) this.type).topBlock.getBlock());
    }

    @Override
    public SpongeMaterialData getGroundBlock() {
        return new SpongeMaterialData((BlockType) ((Biome) this.type).fillerBlock.getBlock());
    }

    @Override
    public List<WeightedMobSpawnGroup> getMobSpawnGroup(EntityCategory entityCategory) {
        // TODO Look at Forge IMPL's MobSpawnGroupHelper
        return new ArrayList<>();
    }
}

package com.khorn.terraincontrol.sponge;

import org.spongepowered.api.world.biome.BiomeType;

import com.khorn.terraincontrol.BiomeIds;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.configuration.BiomeConfig;
import com.khorn.terraincontrol.sponge.generator.TXBiome;

public class SpongeBiome implements LocalBiome {

    private final BiomeType biome;
    private final boolean custom;

    private final BiomeIds biomeIds;
    private final BiomeConfig biomeConfig;

    public SpongeBiome(BiomeType biome, BiomeConfig config, BiomeIds ids) {

        this.biome = biome;
        this.biomeConfig = config;
        this.biomeIds = ids;

        this.custom = biome instanceof TXBiome;

    }

    @Override
    public boolean isCustom() {

        return this.custom;

    }

    @Override
    public String getName() {

        return biome.getName();

    }

    @Override
    public BiomeIds getIds() {

        return this.biomeIds;

    }

    @Override
    public float getTemperatureAt(int x, int y, int z) {

        return (float) this.biome.getTemperature();

    }

    @Override
    public BiomeConfig getBiomeConfig() {

        return this.biomeConfig;

    }

    @Override
    public String toString() {

        return this.getName();

    }

}

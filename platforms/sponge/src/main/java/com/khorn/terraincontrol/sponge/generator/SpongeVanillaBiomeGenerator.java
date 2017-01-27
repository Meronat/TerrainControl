package com.khorn.terraincontrol.sponge.generator;

import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.generator.biome.OutputType;
import com.khorn.terraincontrol.generator.biome.VanillaBiomeGenerator;

public class SpongeVanillaBiomeGenerator extends VanillaBiomeGenerator {

    public SpongeVanillaBiomeGenerator(LocalWorld world) {

        super(world);

    }

    @Override
    public int[] getBiomes(int[] biomeArray, int x, int z, int xSize, int zSize, OutputType type) {

        return new int[0];
    }

    @Override
    public void cleanupCache() {

    }
}

package com.khorn.terraincontrol.sponge;

import java.io.File;

import com.khorn.terraincontrol.LocalMaterialData;
import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.TerrainControlEngine;
import com.khorn.terraincontrol.exception.InvalidConfigException;
import com.khorn.terraincontrol.util.minecraftTypes.DefaultMaterial;

public class SpongeEngine extends TerrainControlEngine {

    private final TXPlugin plugin;

    public SpongeEngine(TXPlugin plugin) {

        super(new SpongeLogger(plugin.getLogger()));
        this.plugin = plugin;

    }

    @Override
    public File getGlobalObjectsDirectory() {

        return null;
    }

    @Override
    public File getTCDataFolder() {

        return null;
    }

    @Override
    public LocalWorld getWorld(String name) {

        return null;
    }

    @Override
    public LocalMaterialData readMaterial(String name) throws InvalidConfigException {

        return null;
    }

    @Override
    public LocalMaterialData toLocalMaterialData(DefaultMaterial defaultMaterial, int blockData) {

        return null;
    }
}

package com.khorn.terraincontrol.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.property.block.LightEmissionProperty;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.gen.PopulatorObject;
import org.spongepowered.api.world.gen.PopulatorObjects;
import org.spongepowered.api.world.gen.populator.Dungeon;
import org.spongepowered.api.world.gen.type.BiomeTreeTypes;
import org.spongepowered.api.world.gen.type.MushroomTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.khorn.terraincontrol.BiomeIds;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.LocalMaterialData;
import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.configuration.BiomeConfig;
import com.khorn.terraincontrol.configuration.BiomeLoadInstruction;
import com.khorn.terraincontrol.configuration.ConfigProvider;
import com.khorn.terraincontrol.configuration.WorldConfig;
import com.khorn.terraincontrol.customobjects.CustomObjectStructureCache;
import com.khorn.terraincontrol.exception.BiomeNotFoundException;
import com.khorn.terraincontrol.generator.SpawnableObject;
import com.khorn.terraincontrol.generator.biome.BiomeGenerator;
import com.khorn.terraincontrol.sponge.generator.structure.TXMansionGen;
import com.khorn.terraincontrol.sponge.generator.structure.TXMineshaftGen;
import com.khorn.terraincontrol.sponge.generator.structure.TXNetherFortressGen;
import com.khorn.terraincontrol.sponge.generator.structure.TXOceanMonumentGen;
import com.khorn.terraincontrol.sponge.generator.structure.TXRareBuildingGen;
import com.khorn.terraincontrol.sponge.generator.structure.TXStrongholdGen;
import com.khorn.terraincontrol.sponge.generator.structure.TXVillageGen;
import com.khorn.terraincontrol.util.ChunkCoordinate;
import com.khorn.terraincontrol.util.NamedBinaryTag;
import com.khorn.terraincontrol.util.minecraftTypes.DefaultBiome;
import com.khorn.terraincontrol.util.minecraftTypes.TreeType;
import com.sun.org.apache.bcel.internal.generic.POP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenFossils;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class SpongeWorld implements LocalWorld {

    private World world;
    private WorldConfig worldConfig;
    private String name;
    private ConfigProvider settings;
    private BiomeGenerator biomeGenerator;

    private CustomObjectStructureCache structureCache;

    private TXStrongholdGen strongholdGen;
    private TXVillageGen villageGen;
    private TXMineshaftGen mineshaftGen;
    private TXRareBuildingGen rareBuildingGen;
    private TXMansionGen mansionGen;
    private TXNetherFortressGen netherFortressGen;
    private TXOceanMonumentGen oceanMonumentGen;

    private static int nextBiomeId = DefaultBiome.values().length;

    private static final int MAX_BIOMES_COUNT = 1024;
    private static final int MAX_SAVED_BIOMES_COUNT = 256;
    private static final int STANDARD_WORLD_HEIGHT = 128;

    private final Map<String, LocalBiome> biomeNames = new HashMap<>();

    public SpongeWorld(String name) {

        this.name = name;

    }

    @Override
    public LocalBiome createBiomeFor(BiomeConfig biomeConfig, BiomeIds requestedBiomeIds) {

        SpongeBiome biome;

        if (biomeConfig.defaultSettings.isCustomBiome) {

            // TODO ?

        }

        return null;
    }

    @Override
    public int getMaxBiomesCount() {

        return MAX_BIOMES_COUNT;

    }

    @Override
    public int getMaxSavedBiomesCount() {

        return MAX_SAVED_BIOMES_COUNT;

    }

    @Override
    public int getFreeBiomeId() {

        return nextBiomeId++;

    }

    @Override
    public SpongeBiome getBiomeById(int id) throws BiomeNotFoundException {
        LocalBiome biome = this.settings.getBiomeByIdOrNull(id);
        if (biome == null)
        {
            throw new BiomeNotFoundException(id, Arrays.asList(this.settings.getBiomeArray()));
        }
        return (SpongeBiome) biome;
    }

    @Override
    public SpongeBiome getBiomeByIdOrNull(int id) {

        return (SpongeBiome) this.settings.getBiomeByIdOrNull(id);
    }

    @Override
    public LocalBiome getBiomeByName(String name) throws BiomeNotFoundException {
        LocalBiome biome = this.biomeNames.get(name);

        if (biome == null) {
            throw new BiomeNotFoundException(name, this.biomeNames.keySet());
        }

        return biome;
    }

    @Override
    public Collection<? extends BiomeLoadInstruction> getDefaultBiomes() {
        // Loop through all default biomes and create the default settings for them
        List<BiomeLoadInstruction> standardBiomes = new ArrayList<>();
        for (DefaultBiome defaultBiome : DefaultBiome.values()) {
            int id = defaultBiome.Id;
            BiomeLoadInstruction instruction = defaultBiome.getLoadInstructions(new SpongeMojangSettings((BiomeType) Biome.getBiome(id)), STANDARD_WORLD_HEIGHT);
            standardBiomes.add(instruction);
        }

        return standardBiomes;
    }

    @Override
    public BiomeGenerator getBiomeGenerator() {
        return this.biomeGenerator;
    }

    @Override
    public LocalBiome getBiome(int x, int z) throws BiomeNotFoundException {
        if (this.settings.getWorldConfig().populateUsingSavedBiomes) {
            return getSavedBiome(x, z);
        } else {
            return getCalculatedBiome(x, z);
        }
    }

    @Override
    public LocalBiome getSavedBiome(int x, int z) throws BiomeNotFoundException {

        return getBiomeById(Biome.getIdForBiome((Biome) this.world.getBiome(x,64,  z)));

    }

    @Override
    public LocalBiome getCalculatedBiome(int x, int z) {

        return null;
    }

    @Override
    public void prepareDefaultStructures(int chunkX, int chunkZ, boolean dry) {
        WorldConfig worldConfig = this.settings.getWorldConfig();

        /*
        TODO Do this
        if (worldConfig.strongholdsEnabled) {
            this.strongholdGen.generate(this.world, chunkX, chunkZ, null);
        }
        if (worldConfig.mineshaftsEnabled) {
            this.mineshaftGen.generate(this.world, chunkX, chunkZ, null);
        }
        if (worldConfig.villagesEnabled && dry) {
            this.villageGen.generate(this.world, chunkX, chunkZ, null);
        }
        if (worldConfig.rareBuildingsEnabled) {
            this.rareBuildingGen.generate(this.world, chunkX, chunkZ, null);
        }
        if (worldConfig.netherFortressesEnabled) {
            this.netherFortressGen.generate(this.world, chunkX, chunkZ, null);
        }
        if (worldConfig.oceanMonumentsEnabled) {
            this.oceanMonumentGen.generate(this.world, chunkX, chunkZ, null);
        }
        if (worldConfig.mansionsEnabled) {
            this.mansionGen.generate(this.world, chunkX, chunkZ, null);
        }*/

    }

    @Override
    public boolean placeDungeon(Random rand, int x, int y, int z) {

        Optional<Chunk> optionalChunk = this.world.getChunk(x, y, z);

        if (optionalChunk.isPresent()) {

            Dungeon.builder().build().populate(this.world, optionalChunk.get(), rand);
            return true;

        }

        return false;

    }

    @Override
    public boolean placeFossil(Random rand, ChunkCoordinate chunkCoord) {

        // TODO Sponge should create a fossil populator
        return new WorldGenFossils().generate((net.minecraft.world.World) this.world, rand, new BlockPos(chunkCoord.getBlockX(), 0, chunkCoord.getBlockZ()));

    }

    @Override
    public boolean placeTree(TreeType type, Random rand, int x, int y, int z) {
        PopulatorObject populatorObject = null;

        if (type == TreeType.Tree) {
            populatorObject = PopulatorObjects.OAK;
        } else if (type == TreeType.BigTree) {
            populatorObject = PopulatorObjects.MEGA_OAK;
        } else if (type == TreeType.Forest || type == TreeType.Birch) {
            // TODO Forest is set to be deprecated
            populatorObject = PopulatorObjects.BIRCH;
        } else if (type == TreeType.TallBirch) {
            populatorObject = PopulatorObjects.MEGA_BIRCH;
        } else if (type == TreeType.HugeMushroom) {
            if (rand.nextBoolean()) {
                populatorObject = PopulatorObjects.BROWN;
            } else {
                populatorObject = PopulatorObjects.RED;
            }
        } else if (type == TreeType.HugeRedMushroom) {
            populatorObject = PopulatorObjects.RED;
        } else if (type == TreeType.HugeBrownMushroom) {
            populatorObject = PopulatorObjects.BROWN;
        } else if (type == TreeType.SwampTree) {
            populatorObject = PopulatorObjects.SWAMP;
        } else if (type == TreeType.Taiga1) {
            populatorObject = PopulatorObjects.POINTY_TAIGA;
        } else if (type == TreeType.Taiga2) {
            populatorObject = PopulatorObjects.TALL_TAIGA;
        } else if (type == TreeType.JungleTree) {
            populatorObject = PopulatorObjects.MEGA_JUNGLE;
        } else if (type == TreeType.GroundBush) {
            populatorObject = PopulatorObjects.JUNGLE_BUSH;
        } else if (type == TreeType.CocoaTree) {
            populatorObject = PopulatorObjects.JUNGLE;
        } else if (type == TreeType.Acacia) {
            populatorObject = PopulatorObjects.SAVANNA;
        } else if (type == TreeType.DarkOak) {
            populatorObject = PopulatorObjects.CANOPY;
        } else if (type == TreeType.HugeTaiga1) {
            populatorObject = PopulatorObjects.MEGA_POINTY_TAIGA;
        } else if (type == TreeType.HugeTaiga2) {
            populatorObject = PopulatorObjects.MEGA_TALL_TAIGA;
        } else {
            throw new RuntimeException("Failed to handle tree of type " + type.toString());
        }

        if (!populatorObject.canPlaceAt(this.world, x, y, z)) {
            return false;
        }

        populatorObject.placeObject(this.world, rand, x, y, z);

        return true;

    }

    @Override
    public boolean placeDefaultStructures(Random rand, ChunkCoordinate chunkCoord) {

        return false;
    }

    @Override
    public SpawnableObject getMojangStructurePart(String name) {

        return null;
    }

    @Override
    public void replaceBlocks(ChunkCoordinate chunkCoord) {

    }

    @Override
    public void placePopulationMobs(LocalBiome biome, Random random, ChunkCoordinate chunkCoord) {

    }

    @Override
    public void startPopulation(ChunkCoordinate chunkCoord) {

    }

    @Override
    public void endPopulation() {

    }

    @Override
    public LocalMaterialData getMaterial(int x, int y, int z) {

        return null;
    }

    @Override
    public boolean isEmpty(int x, int y, int z) {

        return false;
    }

    @Override
    public void setBlock(int x, int y, int z, LocalMaterialData material) {

    }

    @Override
    public void attachMetadata(int x, int y, int z, NamedBinaryTag tag) {

    }

    @Override
    public NamedBinaryTag getMetadata(int x, int y, int z) {

        return null;

    }

    @Override
    public int getLiquidHeight(int x, int z) {
        for (int y = getHighestBlockYAt(x, z) - 1; y > 0; y--) {
            LocalMaterialData material = getMaterial(x, y, z);
            if (material.isLiquid()) {
                return y + 1;
            } else if (material.isSolid()) {
                // Failed to find a liquid
                return -1;
            }
        }
        return -1;
    }

    @Override
    public int getSolidHeight(int x, int z) {

        for (int y = getHighestBlockYAt(x, z) - 1; y > 0; y--) {
            LocalMaterialData material = getMaterial(x, y, z);
            if (material.isSolid()) {
                return y + 1;
            }
        }
        return -1;
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {

        // TODO WAIT FOR SPONGE PULL REQUEST https://github.com/SpongePowered/SpongeAPI/pull/1416

        return 256;

    }

    @Override
    public int getLightLevel(int x, int y, int z) {

        Optional<LightEmissionProperty> light = this.world.getLocation(x, y, z).getProperty(LightEmissionProperty.class);

        if (light.isPresent()) {
            return light.get().getValue();
        }

        return 0;

    }

    @Override
    public boolean isLoaded(int x, int y, int z) {

        Optional<Chunk> optionalChunk = this.world.getChunkAtBlock(x, y, z);

        if (optionalChunk.isPresent()) {
            return optionalChunk.get().isLoaded();
        }

        return false;

    }

    @Override
    public ConfigProvider getConfigs() {

        return this.settings;

    }

    @Override
    public CustomObjectStructureCache getStructureCache() {

        return this.structureCache;

    }

    @Override
    public String getName() {
        return this.world.getName();
    }

    @Override
    public long getSeed() {
        return this.world.getProperties().getSeed();
    }

    @Override
    public int getHeightCap() {
        return this.settings.getWorldConfig().worldHeightCap;
    }

    @Override
    public int getHeightScale() {
        return this.settings.getWorldConfig().worldHeightScale;
    }

}

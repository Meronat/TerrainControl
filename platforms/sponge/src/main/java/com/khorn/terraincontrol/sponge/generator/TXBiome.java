package com.khorn.terraincontrol.sponge.generator;

import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.BiomeType;

public class TXBiome implements BiomeType {

    /**
     * Gets the temperature of this biome.
     *
     * @return The temperature
     */
    @Override
    public double getTemperature() {

        return 0;
    }

    /**
     * Gets the humidity of this biome.
     *
     * @return The humidity
     */
    @Override
    public double getHumidity() {

        return 0;
    }

    /**
     * Gets the default generation settings of this biome for the given world.
     *
     * @param world The world the settings are being made for
     * @return The default generation settings
     */
    @Override
    public BiomeGenerationSettings createDefaultGenerationSettings(World world) {

        return null;
    }

    /**
     * Gets the unique identifier of this {@link CatalogType}. The identifier is
     * case insensitive, thus there cannot be another instance with a different
     * character case. The id of this instance must remain the same for the
     * entire duration of its existence. The identifier can be formatted however
     * needed.
     *
     * <p>A typical id format follows the pattern of <code>`modId:name`</code>
     * or <code>`minecraft:name`</code>. However the prefix may be omitted for
     * default/vanilla minecraft types.</p>
     *
     * @return The unique identifier of this dummy type
     */
    @Override
    public String getId() {

        return null;
    }

    /**
     * Gets the human-readable name of this individual {@link CatalogType}. This
     * name is not guaranteed to be unique. This value should not be used for
     * serialization.
     *
     * @return The human-readable name of this dummy type
     */
    @Override
    public String getName() {

        return null;
    }
}

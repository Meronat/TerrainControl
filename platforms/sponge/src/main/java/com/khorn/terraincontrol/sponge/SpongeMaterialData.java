package com.khorn.terraincontrol.sponge;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.GravityAffectedProperty;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.MatterProperty.Matter;

import java.util.Optional;

import com.khorn.terraincontrol.LocalMaterialData;
import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.util.helpers.BlockHelper;
import com.khorn.terraincontrol.util.minecraftTypes.DefaultMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class SpongeMaterialData implements LocalMaterialData {

    private BlockState state;

    public SpongeMaterialData(BlockType type) {

        this.state = type.getDefaultState();

    }

    public SpongeMaterialData(BlockState state) {

        this.state = state;

    }

    /**
     * Gets the name of this material. If a {@link #toDefaultMaterial()
     * DefaultMaterial is available,} that name is used, otherwise it's up to
     * the mod that provided this block to name it. Block data is appended to
     * the name, separated with a colon, like "WOOL:2".
     *
     * @return The name of this material.
     */
    @Override
    public String getName() {
        return this.state.getName();
    }

    /**
     * Gets the internal block id. At the moment, all of Minecraft's vanilla
     * materials have a static id, but this can change in the future. Mods
     * already have dynamic ids.
     *
     * @return The internal block id.
     */
    @Override
    public int getBlockId() {
        // TODO Is it possible to do this in Sponge?
        return Block.getIdFromBlock((Block) this.state.getType());
    }

    /**
     * Gets the internal block data. Block data represents things like growth
     * stage and rotation.
     *
     * @return The internal block data.
     */
    @Override
    public byte getBlockData() {
        // TODO Is there a way to do this in Sponge
        return (byte) ((Block) this.state.getType()).getMetaFromState((IBlockState) state);
    }

    /**
     * Gets whether this material is a liquid, like water or lava.
     *
     * @return True if this material is a liquid, false otherwise.
     */
    @Override
    public boolean isLiquid() {
        Optional<MatterProperty> optionalMatter = this.state.getProperty(MatterProperty.class);

        return optionalMatter.isPresent() && optionalMatter.get().getValue().equals(Matter.LIQUID);
    }

    /**
     * Gets whether this material is solid. If there is a
     * {@link #toDefaultMaterial() DefaultMaterial available}, this property is
     * defined by {@link DefaultMaterial#isSolid()}. Otherwise, it's up to the
     * mod that provided this block to say whether it's solid or not.
     *
     * @return True if this material is solid, false otherwise.
     */
    @Override
    public boolean isSolid() {
        Optional<MatterProperty> optionalMatter = this.state.getProperty(MatterProperty.class);

        return optionalMatter.isPresent() && optionalMatter.get().getValue().equals(Matter.SOLID);
    }

    /**
     * Gets whether this material is air. This is functionally equivalent to
     * {@code isMaterial(DefaultMaterial.AIR)}, but may yield better
     * performance.
     *
     * @return True if this material is air, false otherwise.
     */
    @Override
    public boolean isAir() {
        return this.state.getType().equals(BlockTypes.AIR);
    }

    /**
     * Gets the default material belonging to this material. The block data will
     * be lost. If the material is not one of the vanilla Minecraft materials,
     * {@link DefaultMaterial#UNKNOWN_BLOCK} is returned.
     *
     * @return The default material.
     */
    @Override
    public DefaultMaterial toDefaultMaterial() {
        // TODO Not sure if this is right
        return DefaultMaterial.getMaterial(getBlockId());
    }

    /**
     * Gets whether snow can fall on this block.
     *
     * @return True if snow can fall on this block, false otherwise.
     */
    @Override
    public boolean canSnowFallOn() {
        return this.toDefaultMaterial().canSnowFallOn();
    }

    /**
     * Gets whether the block is of the given material. Block data is ignored,
     * as {@link DefaultMaterial} doesn't include block data.
     *
     * @param material The material to check.
     * @return True if this block is of the given material, false otherwise.
     */
    @Override
    public boolean isMaterial(DefaultMaterial material) {
        // TODO Not sure if this is right
        return this.getBlockId() == material.id;
    }

    /**
     * Gets an instance with the same material as this object, but with the
     * given block data. This instance is not modified.
     *
     * @param newData The new block data.
     * @return An instance with the given block data.
     */
    @Override
    public LocalMaterialData withBlockData(int newData) {
        // TODO
        return this;
    }

    /**
     * Gets an instance with the same material as this object, but the default
     * block data of the material. This instance is not modified.
     *
     * @return An instance with the default block data.
     */
    @Override
    public LocalMaterialData withDefaultBlockData() {
        // TODO
        return this;
    }

    /**
     * Gets the hashCode of the material, based on only the block id. No
     * hashCode returned by this method may be the same as any hashCode returned
     * by {@link #hashCode()}.
     *
     * @return The unique hashCode.
     */
    @Override
    public int hashCodeWithoutBlockData() {
        return this.getBlockId();
    }

    /**
     * Gets a new material that is rotated 90 degrees. North -> west -> south ->
     * east. If this material cannot be rotated, the material itself is
     * returned.
     *
     * @return The rotated material.
     */
    @Override
    public LocalMaterialData rotate() {
        // Try to rotate
        // TODO
        return this;
    }

    /**
     * Gets whether this material falls down when no other block supports this
     * block, like gravel and sand do.
     *
     * @return True if this material can fall, false otherwise.
     */
    @Override
    public boolean canFall() {
        Optional<GravityAffectedProperty> gravity = this.state.getProperty(GravityAffectedProperty.class);

        return gravity.isPresent() && gravity.get().getValue();
    }

    @Override
    public int hashCode() {
        // From 4096 to 69632 when there are 4096 block ids
        return TerrainControl.SUPPORTED_BLOCK_IDS + getBlockId() * 16 + getBlockData();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SpongeMaterialData)) {
            return false;
        }
        SpongeMaterialData other = (SpongeMaterialData) obj;
        if (!this.state.equals(other.state)) {
            return false;
        }
        return true;
    }

}

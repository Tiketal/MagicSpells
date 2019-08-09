package com.nisovin.magicspells.materials;

import org.bukkit.block.data.BlockData;

/**
 * Represents a block ignoring block data
 *
 */
public class MagicBlockAnyDataMaterial extends MagicBlockMaterial {
	
	public MagicBlockAnyDataMaterial(BlockData data) {
		super(data);
	}
	
	@Override
	public boolean equals(BlockData data) {
		return data.getMaterial() == this.data.getMaterial();
	}
}

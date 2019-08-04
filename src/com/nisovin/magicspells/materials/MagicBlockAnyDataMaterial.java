package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

// TODO
@Deprecated
public class MagicBlockAnyDataMaterial extends MagicBlockMaterial {

	public MagicBlockAnyDataMaterial(Material type) {
		super(type);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MagicBlockAnyDataMaterial) {
			MagicBlockAnyDataMaterial m = (MagicBlockAnyDataMaterial)o;
			return this.getMaterial() == m.getMaterial();
		} else {
			return false;
		}
	}
	
	@Override
	public boolean equals(BlockData data) {
		return this.type == data.getMaterial();
	}
}

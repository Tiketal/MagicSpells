package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

public class MagicBlockRandomMaterial extends MagicBlockMaterial {
	MagicMaterial[] materials;
	
	public MagicBlockRandomMaterial(MagicMaterial[] materials) {
		super((BlockData)null);
		this.materials = materials;
	}
	
	@Override
	public Material getMaterial() {
		return materials[ItemNameResolver.rand.nextInt(materials.length)].getMaterial();
	}
	
	@Override
	public BlockData getBlockData() {
		return ((MagicBlockMaterial)materials[ItemNameResolver.rand.nextInt(materials.length)]).getBlockData();
	}
	
	@Override
	public void setBlock(Block block, boolean applyPhysics) {
		MagicMaterial material = materials[ItemNameResolver.rand.nextInt(materials.length)];
		BlockState state = block.getState();
		state.setType(material.getMaterial());
		state.setBlockData(((MagicBlockMaterial)material).getBlockData());
		state.update(true, applyPhysics);
	}
	
	@Override
	public boolean equals(BlockData data) {
		for (MagicMaterial m : materials) {
			if (((MagicBlockMaterial)m).equals(data)) return true;
		}
		return false;
	}
	
}
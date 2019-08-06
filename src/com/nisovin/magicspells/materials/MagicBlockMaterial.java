package com.nisovin.magicspells.materials;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;

public class MagicBlockMaterial extends MagicMaterial {
	BlockData data;
	
	public MagicBlockMaterial(BlockData data) {
		this.data = data;
	}
	
	public MagicBlockMaterial(Material type) {
		this(type.createBlockData());
	}
	
	@Override
	public Material getMaterial() {
		return data.getMaterial();
	}
	
	@Override
	public BlockData getBlockData() {
		return data;
	}
	
	@Override
	public void setBlock(Block block, boolean applyPhysics) {
		BlockState state = block.getState();
		state.setType(getMaterial());
		state.setBlockData(getBlockData());
		state.update(true, applyPhysics);
	}

	@Override
	public FallingBlock spawnFallingBlock(Location location) {
		return location.getWorld().spawnFallingBlock(location, getBlockData());
	}

	@Override
	public ItemStack toItemStack(int quantity) {
		return new ItemStack(data.getMaterial(), quantity);
	}

	@Override
	public boolean equals(ItemStack itemStack) {
		return itemStack.getType() == data.getMaterial();
	}
}
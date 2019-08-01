package com.nisovin.magicspells.materials;

import java.util.Random;

import org.bukkit.Material;


public interface ItemNameResolver {

	static Random rand = new Random();

	@Deprecated
	public ItemTypeAndData resolve(String string);
	
	public MagicMaterial resolveItem(String string);
	
	public MagicMaterial resolveBlock(String string);
	
	public class ItemTypeAndData {
		public Material type = Material.AIR;
		public short data = 0;
	}
	
}

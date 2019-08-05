package com.nisovin.magicspells.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;

public class BlockUtils {
	private static HashSet<Material> pathables = new HashSet<>();
	static {
		Collections.addAll(pathables, Material.AIR,
				Material.ACACIA_SAPLING,
				Material.BIRCH_SAPLING,
				Material.DARK_OAK_SAPLING,
				Material.JUNGLE_SAPLING,
				Material.OAK_SAPLING,
				Material.SPRUCE_SAPLING,
				Material.WATER,
				Material.POWERED_RAIL,
				Material.DETECTOR_RAIL,
				Material.TALL_GRASS,
				Material.DEAD_BUSH,
				Material.DANDELION,
				Material.POPPY,
				Material.BROWN_MUSHROOM,
				Material.RED_MUSHROOM,
				Material.TORCH,
				Material.FIRE,
				Material.REDSTONE_WIRE,
				Material.WHEAT,
				Material.ACACIA_SIGN,
				Material.BIRCH_SIGN,
				Material.DARK_OAK_SIGN,
				Material.JUNGLE_SIGN,
				Material.OAK_SIGN,
				Material.SPRUCE_SIGN,
				Material.LADDER,
				Material.RAIL,
				Material.ACACIA_WALL_SIGN,
				Material.BIRCH_WALL_SIGN,
				Material.DARK_OAK_WALL_SIGN,
				Material.JUNGLE_WALL_SIGN,
				Material.OAK_WALL_SIGN,
				Material.SPRUCE_WALL_SIGN,
				Material.LEVER,
				Material.STONE_PRESSURE_PLATE,
				Material.ACACIA_PRESSURE_PLATE,
				Material.BIRCH_PRESSURE_PLATE,
				Material.DARK_OAK_PRESSURE_PLATE,
				Material.JUNGLE_PRESSURE_PLATE,
				Material.OAK_PRESSURE_PLATE,
				Material.SPRUCE_PRESSURE_PLATE,
				Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
				Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
				Material.REDSTONE_TORCH,
				Material.REDSTONE_WALL_TORCH,
				Material.STONE_BUTTON,
				Material.SNOW,
				Material.SUGAR_CANE,
				Material.VINE,
				Material.LILY_PAD,
				Material.NETHER_WART_BLOCK,
				Material.BLACK_CARPET,
				Material.BLUE_CARPET,
				Material.BROWN_CARPET,
				Material.CYAN_CARPET,
				Material.GRAY_CARPET,
				Material.GREEN_CARPET,
				Material.LIGHT_BLUE_CARPET,
				Material.LIGHT_GRAY_CARPET,
				Material.LIME_CARPET,
				Material.MAGENTA_CARPET,
				Material.ORANGE_CARPET,
				Material.PINK_CARPET,
				Material.PURPLE_CARPET,
				Material.RED_CARPET,
				Material.WHITE_CARPET,
				Material.YELLOW_CARPET);
	}

	public static boolean isTransparent(Spell spell, Block block) {
		return spell.getLosTransparentBlocks().contains(block.getType());
	}
	
	public static Block getTargetBlock(Spell spell, LivingEntity entity, int range) {
		try {
			if (spell != null) {
				return entity.getTargetBlock(spell.getLosTransparentBlocks(), range);
			} else {
				return entity.getTargetBlock(MagicSpells.getTransparentBlocks(), range);				
			}
		} catch (IllegalStateException e) {
			return null;
		}
	}
	
	public static List<Block> getLastTwoTargetBlock(Spell spell, LivingEntity entity, int range) {
		try {
			return entity.getLastTwoTargetBlocks(spell.getLosTransparentBlocks(), range);
		} catch (IllegalStateException e) {
			return null;
		}
	}
	
	public static void setTypeAndData(Block block, Material material, byte data, boolean physics) {
		block.setType(material);
		block.setBlockData(material.createBlockData(), physics); // TODO: not correct
	}
	
	public static void setBlockFromFallingBlock(Block block, FallingBlock fallingBlock, boolean physics) {
		// block.setTypeIdAndData(fallingBlock.getBlockId(), fallingBlock.getBlockData(), physics);
		block.setType(fallingBlock.getBlockData().getMaterial());
		block.setBlockData(fallingBlock.getBlockData().clone(), physics);
	}
	
	public static int getWaterLevel(Block block) {
		return block.getBlockData() instanceof Levelled?
				((Levelled)block.getBlockData()).getLevel() : 1;
	}
	
	public static int getGrowthLevel(Block block) {
		return block.getBlockData() instanceof Ageable?
				((Ageable)block.getBlockData()).getAge() : 0;
	}
	
	public static void setGrowthLevel(Block block, int level) {
		((Ageable)block.getBlockData()).setAge(level);
	}
	
	public static int getWaterLevel(BlockState blockState) {
		return ((Levelled)blockState.getBlockData()).getLevel();
	}
	
	public static boolean isPathable(Block block) {
		return isPathable(block.getType());
	}
	
	public static boolean isPathable(Material material) {
		return pathables.contains(material);
	}
	
	public static boolean isSafeToStand(Location location) {
		return 
				isPathable(location.getBlock()) && 
				isPathable(location.add(0, 1, 0).getBlock()) && 
				(!isPathable(location.subtract(0, 2, 0).getBlock()) || !isPathable(location.subtract(0, 1, 0).getBlock()));
	}

	public static boolean isGeneralType(Material type, String endsWith) {
		return type.name().toUpperCase().endsWith(endsWith.toUpperCase());
	}
	
}
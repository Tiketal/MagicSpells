package com.nisovin.magicspells.util;

import java.util.Arrays;

import java.util.LinkedList;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import com.nisovin.magicspells.MagicSpells;

public class EntityData {

	private EntityType entityType;
	private boolean flag = false;
	private String var1 = null; // null means use default
	private String var2 = null;
	private String var3 = null;
	
	public EntityData(String type) {
		type = type.toLowerCase();
		
		if (type.startsWith("baby ")) {
			flag = true;
			type = type.replace("baby ", "");
		}
		
		// player
		if (type.equals("human") || type.equals("player")) {
			type = "player";
			
		// zombie villager
		} else if (type.startsWith("zombie ")) {
			String[] split = type.split(" ");
			String prof = split[1];
			String biome = split[2];

			if (prof.startsWith("green")) {
				var1 = "nitwit";
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = prof;
				} catch (Exception e) {
					MagicSpells.error("Invalid zombie profession: " + prof);
				}
			}
			
			try {
				Villager.Type.valueOf(biome.toUpperCase());
				var2 = biome;
			} catch (Exception e) {
				MagicSpells.error("Invalid village biome: " + biome);
			}
			
			type = "zombie_villager";
		} else if (type.endsWith(" zombie")) {
			String[] split = type.split(" ");
			String prof = split[0];
			String biome = split[1];
			
			if (prof.startsWith("green")) {
				var1 = "nitwit";
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = prof;
				} catch (Exception e) {
					MagicSpells.error("Invalid zombie profession: " + prof);
				}
			}

			try {
				Villager.Type.valueOf(biome.toUpperCase());
				var2 = biome;
			} catch (Exception e) {
				MagicSpells.error("Invalid village biome: " + biome);
			}
			type = "zombie_villager";
			
		// creeper
		} else if (type.equals("powered creeper")) {
			type = "creeper";
			flag = true;
			
		// villager
		} else if (type.startsWith("villager ")) {
			String[] split = type.split(" ");
			String prof = split[1];
			String biome = split[2];
			
			if (prof.startsWith("green")) {
				var1 = "nitwit";
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = prof;
				} catch (Exception e) {
					MagicSpells.error("Invalid villager profession: " + prof);
				}
			}

			try {
				Villager.Type.valueOf(biome.toUpperCase());
				var2 = biome;
			} catch (Exception e) {
				MagicSpells.error("Invalid village biome: " + biome);
			}
			type = "villager";
		} else if (type.endsWith(" villager")) {
			String[] split = type.split(" ");
			String prof = split[1];
			String biome = split[2];
			
			var1 = "none";
			if (prof.startsWith("green")) {
				var1 = "nitwit";
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = prof;
				} catch (Exception e) {
					MagicSpells.error("Invalid villager profession: " + prof);
				}
			}
			
			var2 = "plains";
			try {
				Villager.Type.valueOf(biome.toUpperCase());
				var2 = biome;
			} catch (Exception e) {
				MagicSpells.error("Invalid village biome: " + biome);
			}
			type = "villager";
			
		// sheep
		} else if (type.endsWith(" sheep")) {
			String color = type.replace(" sheep", "").replace(" ", "_");
			
			if (color.equals("random")) {
				var1 = "random";
			} else {
				try {
					DyeColor.valueOf(color.toUpperCase());
					var1 = color;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid sheep color: " + color);
				}
			}
			type = "sheep";
			
		// rabbit
		} else if (type.endsWith(" rabbit")) {
			String rabbitType = type.replace(" rabbit", "").replace(' ', '_');
			if (rabbitType.equals("blackwhite")) {
				rabbitType = "black_and_white";
			} else if (rabbitType.equals("saltpepper")) {
				rabbitType = "salt_and_pepper";
			} else if (rabbitType.equals("killer")) {
				rabbitType = "the_killer_rabbit";
			}
			
			if (rabbitType.equals("random")) {
				var1 = "random";
			} else {
				try {
					Rabbit.Type.valueOf(rabbitType.toUpperCase());
					var1 = rabbitType;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid rabbit type: " + rabbitType);
				}
			}
			type = "rabbit";
			
		// wolf
		} else if (type.startsWith("wolf ")) {
			String color = type.replace("wolf ", "").replace(' ', '_');
			
			if (color.equals("angry")) {
				var1 = color;
			} else {
				try {
					DyeColor.valueOf(color.toUpperCase());
					var1 = color;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid collar color: " + color);
				}
			}
			type = "wolf";
		
		// pig
		} else if (type.equals("saddled pig")) {
			var1 = "saddled";
			type = "pig";
			
		// horse
		} else if (type.contains("horse")) {
			LinkedList<String> data = new LinkedList<String>(Arrays.asList(type.split(" ")));
			if (data.get(0).equals("horse")) {
				data.remove();
				type = "horse";
			} else if (data.size() >= 2 && data.get(1).equals("horse")) {
				String t = data.remove();
				if (t.equals("skeleton") || t.equals("skeletal")) {
					type = "skeleton_horse";
				} else if (t.equals("zombie") || t.equals("undead")) {
					type = "zombie_horse";
				} else {
					type = "horse";
				}
			}
			while (data.size() > 0) {
				String d = data.remove();
				if (d.equals("iron")) {
					var1 = "1";
				} else if (d.equals("gold")) {
					var1 = "2";
				} else if (d.equals("diamond")) {
					var1 = "3";
				}
			}
			
		// foxes
		} else if (type.endsWith(" fox")) {
			String foxType = type.replace(" fox", "");
			if (foxType.equals("random")) {
				var1 = "random";
			} else {
				try {
					Fox.Type.valueOf(foxType.toUpperCase());
					var1 = foxType;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid fox type: " + foxType);
				}
			}
			type = "fox";
			
		// mooshroom
		} else if (type.endsWith(" mooshroom") || type.endsWith(" mushroom_cow")) {
			String cowType = null;
			if (type.endsWith(" mooshroom")) {
				cowType = type.replace(" mooshroom", "");
			} else if (type.endsWith(" mushroom_cow")) {
				cowType = type.replace(" mushroom_cow", "");
			}
			
			if (cowType.equals("random")) {
				var1 = "random";
			} else {
				try {
					MushroomCow.Variant.valueOf(cowType.toUpperCase());
					var1 = cowType;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid cow type: " + cowType);
				}
			}
			
			type = "mushroom_cow";
			
		// parrot
		} else if (type.endsWith(" parrot")) {
			String color = type.replaceAll(" parrot", "");
			
			if (color.equals("random")) {
				var1 = "random";
			} else {
				try {
					Parrot.Variant.valueOf(color.toUpperCase());
					var1 = color;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid parrot color: " + color);
				}
			}
			
			type = "parrot";
			
		// tropical fish color tropical_fish color pattern
		} else if (type.contains("tropical_fish") || type.contains("tropicalfish")) {
			String[] data = type.split(" ");
			
			// base color
			try {
				DyeColor.valueOf(data[0]);
				var1 = data[0];
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid base color: " + data[0]);
			}
			
			// pattern color
			try {
				DyeColor.valueOf(data[1]);
				var2 = data[1];
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid pattern color: " + data[1]);
			}
			
			// pattern
			try {
				DyeColor.valueOf(data[2]);
				var3 = data[2];
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid pattern: " + data[2]);
			}
			
		// llama
		} else if (type.contains("llama")) {
			LinkedList<String> data = new LinkedList<>(Arrays.asList(type.split(" ")));
			
			// color
			String color = null;
			try {
				color = data.remove();
				Llama.Color.valueOf(color.toUpperCase());
				var1 = color;
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid llama color: " + color);
			}
			
			// type
			if (data.get(0).equals("llama")) {
				data.remove();
				type = "llama";
				
			} else if (data.size() >= 2 && data.get(1).equals("llama")) {
				String t = data.remove();
				if (t.equals("trader")) {
					type = "trader_llama";
				}
			}
			
			// decor/carpet
			while (data.size() > 0) {
				String d = data.remove();
				try {
					if (!d.contains("carpet")) {
						throw new IllegalArgumentException();
					}
					Material.valueOf(d);
					var2 = d;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid llama decor: " + d);
				}
			}
			type = "llama";
			
		// panda
		} else if (type.endsWith(" panda")) {
			String gene = type.replace(" panda", "");
			
			try {
				Panda.Gene.valueOf(gene.toUpperCase());
				var1 = gene;
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid panda gene: " + gene);
			}
			type = "panda";
			
		} else if (type.endsWith(" shulker")) {
			String[] data = type.split(" ");
			
			if (data[0].equals("random")) {
				var1 = "random";
			} else {
				try {
					DyeColor.valueOf(data[0]);
					var3 = data[0];
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid color: " + data[0]);
				}
			}
			type = "shulker";

		
		// two worded
		} else if (type.equals("irongolem")) {
			type = "iron_golem";
		} else if (type.equals("magmacube")) {
			type = "magma_cube";
		} else if (type.equals("cavespider")) {
			type = "cave_spider";
		} else if (type.equals("wanderingtrader")) {
			type = "wandering_trader";
		} else if (type.equals("polarbear")) {
			type = "polar_bear";
			
		// different aliases
		} else if (type.equals("dragon")) {
			type = "ender_dragon";
		} else if (type.equals("snowgolem")) {
			type = "snowman";
			
		// block and item
		} else if (type.startsWith("block") || type.startsWith("fallingblock")) {
			String data = type.split(" ")[1];
			var1 = data;
			type = "falling_block";
		} else if (type.startsWith("item")) {
			String data = type.split(" ")[1];
			var1 = data;
			type = "item";
		}
		
		// cats
		if (type.startsWith("cat ")) {
			String[] data = type.replace("cat ", "").split(" ");
			String catType = data[0];
			
			if (catType.equals("allblack")) {
				catType = "all_black";
			
			} else if (catType.equals("britishshorthair") || catType.equals("british")) {
				catType = "british_shorthair";
			}
			
			String color = "red";
			if (data.length > 1) {
				color = data[1];
			}
			
			// cat type
			var1 = "tabby";
			if (catType.equals("random")) {
				var1 = "random";
			} else {
				try {
					Cat.Type.valueOf(catType.toUpperCase());
					var1 = catType;
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid cat type: " + catType);
				}
			}
			
			var2 = "red";
			try {
				DyeColor.valueOf(color.toUpperCase());
				var2 = color;
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid collar color: " + color);
			}
			
			type = "cat";
		}
		
		// slime and magma cube size
		if (type.equals("slime") || type.equals("lavaslime")) {
			var1 = "1";
		} else if (type.startsWith("slime") || type.startsWith("magmacube") || type.startsWith("lavaslime")) {
			String[] data = type.split(" ");
			type = data[0];
			if (type.equals("magmacube")) type = "lavaslime";
			
			var1 = "1";
			try {
				Integer.parseInt(data[1]);
				var1 = data[1];
			} catch (NumberFormatException e) {
				MagicSpells.error("Invalid slime size: " + data[1]);
			}
		}
		
		// phantom size
		if (type.startsWith("phantom")) {
			var1 = "1";
			
		} else if (type.startsWith("phantom")) {
			String[] data = type.split(" ");
			type = data[0];
			
			var1 = "1";
			try {
				Integer.parseInt(data[1]);
				var1 = data[1];
			} catch (NumberFormatException e) {
				MagicSpells.error("Invalid phantom size: " + data[1]);
			}
		}
		
		// parse entity type
		if (type.equals("player")) {
			entityType = EntityType.PLAYER;
		} else {
			try {
				entityType = EntityType.valueOf(type.toUpperCase().replace(" ", "_"));
			} catch (Exception e) {
				entityType = null;
			}
		}
	}
	
	public EntityType getType() {
		return entityType;
	}
	
	public boolean getFlag() {
		return flag;
	}
	
	public String getVar1() {
		return var1;
	}
	
	public String getVar2() {
		return var2;
	}
	
	public String getVar3() {
		return var3;
	}
	
	public Entity spawn(Location loc) {
		Entity entity = loc.getWorld().spawnEntity(loc, entityType);
		if (entity instanceof Ageable && flag) {
			((Ageable)entity).setBaby();
		}
		if (entityType == EntityType.ZOMBIE) {
			((Zombie)entity).setBaby(flag);
		} else if (entityType == EntityType.ZOMBIE_VILLAGER) {
			if (var1 != null) {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.valueOf(var1.toUpperCase()));
			}
			if (var2 != null) {
//				((ZombieVillager)entity).setVillagerType(Villager.Type.valueOf(var2.toUpperCase()));
			}
		} else if (entityType == EntityType.CREEPER) {
			if (flag) {
				((Creeper)entity).setPowered(true);
			}
		} else if (entityType == EntityType.WOLF) {
			if (var1 != null) {
				if (var1.equals("random")) {
					((Wolf)entity).setCollarColor(DyeColor.values()[Util.getRandomInt(DyeColor.values().length)]);
					
				} else if (var1.equals("angry")) {
					((Wolf)entity).setAngry(true);
					
				} else {
					((Wolf)entity).setCollarColor(DyeColor.valueOf(var1.toUpperCase()));
				}
			}
			
		} else if (entityType == EntityType.CAT) {
			// var1 = type
			if (var1 != null) {
				((Cat)entity).setCatType(Cat.Type.valueOf(var1.toUpperCase()));
			}
			
			// var2 = collar color
			if (var2 != null) {
				if (var2.equals("random")) {
					((Cat)entity).setCollarColor(DyeColor.values()[Util.getRandomInt(DyeColor.values().length)]);
					
				} else {
					((Cat)entity).setCollarColor(DyeColor.valueOf(var2.toUpperCase()));
				}
			}
		} else if (entityType == EntityType.VILLAGER) {
			if (var1 != null) {
				((Villager)entity).setProfession(Villager.Profession.valueOf(var1.toUpperCase()));
			}
			if (var2 != null) {
				((Villager)entity).setVillagerType(Villager.Type.valueOf(var2.toUpperCase()));
			}
		} else if (entityType == EntityType.SLIME) {
			((Slime)entity).setSize(Integer.parseInt(var1));
		} else if (entityType == EntityType.MAGMA_CUBE) {
			((MagmaCube)entity).setSize(Integer.parseInt(var1));
		} else if (entityType == EntityType.PIG) {
			((Pig)entity).setSaddle(var1.equals("saddled"));
		} else if (entityType == EntityType.SHEEP) {
			if (var1 != null) {
				((Sheep)entity).setColor(DyeColor.valueOf(var1.toUpperCase()));
			}
			
		} else if (entityType == EntityType.RABBIT) {
			if (var1 != null) {
				if (var1.equals("random")) {
					((Rabbit)entity).setRabbitType(Rabbit.Type.values()[Util.getRandomInt(Rabbit.Type.values().length)]);
				} else {
					((Rabbit)entity).setRabbitType(Rabbit.Type.valueOf(var1.toUpperCase()));
				}
			}
			
		} else if (entityType == EntityType.FOX) {
			if (var1 != null) {
				if (var1.equals("random")) {
					((Fox)entity).setFoxType(Fox.Type.values()[Util.getRandomInt(Fox.Type.values().length)]);
				} else {
					((Fox)entity).setFoxType(Fox.Type.valueOf(var1.toUpperCase()));
				}
			}
			
		} else if (entityType == EntityType.MUSHROOM_COW) {
			if (var1 != null) {
				if (var1.equals("random")) {
					((MushroomCow)entity).setVariant(MushroomCow.Variant.values()[Util.getRandomInt(MushroomCow.Variant.values().length)]);
				} else {
					((MushroomCow)entity).setVariant(MushroomCow.Variant.valueOf(var1.toUpperCase()));
				}
			}
			
		} else if (entityType == EntityType.PARROT) {
			if (var1 != null) {
				((Parrot)entity).setVariant(Parrot.Variant.valueOf(var1.toUpperCase()));
			}
			
		} else if (entityType == EntityType.TROPICAL_FISH) {
			if (var1 != null) {
				((TropicalFish)entity).setBodyColor(DyeColor.valueOf(var1.toUpperCase()));
			}
			if (var2 != null) {
				((TropicalFish)entity).setPatternColor(DyeColor.valueOf(var2.toUpperCase()));
			}
			if (var3 != null) {
				((TropicalFish)entity).setPattern(TropicalFish.Pattern.valueOf(var3.toUpperCase()));
			}
			
		} else if (entityType == EntityType.LLAMA) {
			if (var1 != null) {
				((Llama)entity).setColor(Llama.Color.valueOf(var1.toUpperCase()));
			}
			if (var2 != null) {
				if (var2.equals("random")) {
					Material[] carpets = {
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
							Material.YELLOW_CARPET
					};
					((Llama)entity).getInventory().setDecor(new ItemStack(carpets[Util.getRandomInt(carpets.length)]));
				} else {
					((Llama)entity).getInventory().setDecor(new ItemStack(Material.getMaterial(var1.toUpperCase())));
				}
			}
			
		} else if (entityType == EntityType.PANDA) {
			if (var1 != null) {
				((Panda)entity).setMainGene(Panda.Gene.valueOf(var1.toUpperCase()));
			}
			
		} else if (entityType == EntityType.PHANTOM) {
			((Phantom)entity).setSize(Integer.parseInt(var1));
			
		} else if (entityType == EntityType.SHULKER) {
			if (var1 != null) {
				if (var1.equals("random")) {
					((Shulker)entity).setColor(DyeColor.values()[Util.getRandomInt(DyeColor.values().length)]);
				} else {
					((Shulker)entity).setColor(DyeColor.valueOf(var1.toUpperCase()));
				}
			}
			
		}
		return entity;
	}
}

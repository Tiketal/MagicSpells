package com.nisovin.magicspells.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import com.nisovin.magicspells.MagicSpells;

// TODO add 1.9+ mobs
public class EntityData {

	private EntityType entityType;
	private boolean flag = false;
	private String var1 = "";
	private String var2 = "";
	private int var3 = 0;
	
	public EntityData(String type) {		
		if (type.startsWith("baby ")) {
			flag = true;
			type = type.replace("baby ", "");
		}
		
		// player
		if (type.equalsIgnoreCase("human") || type.equalsIgnoreCase("player")) {
			type = "player";
			
		// zombie villager
		} else if (type.toLowerCase().startsWith("zombie ")) {
			String[] split = type.toLowerCase().split(" ");
			String prof = split[1];
			String biome = split[2];
			if (prof.toLowerCase().startsWith("green")) {
				var1 = "nitwit";
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = prof;
				} catch (Exception e) {
					MagicSpells.error("Invalid zombie profession: " + prof);
					var1 = "none";
				}
			}
			
			try {
				Villager.Type.valueOf(biome.toUpperCase());
				var2 = biome;
			} catch (Exception e) {
				MagicSpells.error("Invalid village biome: " + biome);
				var2 = "plains";
			}
			
			type = "zombie_villager";
		} else if (type.toLowerCase().endsWith(" zombie")) {
			String[] split = type.toLowerCase().split(" ");
			String prof = split[0];
			String biome = split[1];
			if (prof.toLowerCase().startsWith("green")) {
				var1 = "nitwit";
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = prof;
				} catch (Exception e) {
					MagicSpells.error("Invalid zombie profession: " + prof);
					var1 = "none";
				}
			}

			try {
				Villager.Type.valueOf(biome.toUpperCase());
				var2 = biome;
			} catch (Exception e) {
				MagicSpells.error("Invalid village biome: " + biome);
				var2 = "plains";
			}
			type = "zombie_villager";
			
		// creeper
		} else if (type.equalsIgnoreCase("powered creeper")) {
			type = "creeper";
			flag = true;
			
		// villager
		} else if (type.toLowerCase().startsWith("villager ")) {
			String[] split = type.toLowerCase().split(" ");
			String prof = split[1];
			String biome = split[2];
			if (prof.toLowerCase().startsWith("green")) {
				var1 = "nitwit";
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = prof;
				} catch (Exception e) {
					MagicSpells.error("Invalid villager profession: " + prof);
					var1 = "none";
				}
			}

			try {
				Villager.Type.valueOf(biome.toUpperCase());
				var2 = biome;
			} catch (Exception e) {
				MagicSpells.error("Invalid village biome: " + biome);
				var2 = "plains";
			}
			type = "villager";
		} else if (type.toLowerCase().endsWith(" villager")) {
			String[] split = type.toLowerCase().split(" ");
			String prof = split[1];
			String biome = split[2];
			if (prof.toLowerCase().startsWith("green")) {
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
				var2 = "plains";
			}
			type = "villager";
			
		// sheep
		} else if (type.toLowerCase().endsWith(" sheep")) {
			String color = type.toLowerCase().replace(" sheep", "").replace(" ", "_");
			if (color.equalsIgnoreCase("random")) {
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
		} else if (type.toLowerCase().endsWith(" rabbit")) {
			String rabbitType = type.toLowerCase().replace(" rabbit", "").replace(' ', '_');
			if (rabbitType.equals("blackwhite")) {
				rabbitType = "black_and_white";
			} else if (rabbitType.equals("saltpepper")) {
				rabbitType = "salt_and_pepper";
			} else if (rabbitType.equals("killer")) {
				rabbitType = "the_killer_rabbit";
			}
			try {
				Rabbit.Type.valueOf(rabbitType.toUpperCase());
				var1 = rabbitType;
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid rabbit type: " + rabbitType);
			}
			type = "rabbit";
			
		// wolf
		} else if (type.toLowerCase().startsWith("wolf ")) {
			String color = type.toLowerCase().replace("wolf ", "").replace(' ', '_');
			var1 = "";
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
		} else if (type.toLowerCase().equalsIgnoreCase("saddled pig")) {
			var1 = "saddled";
			type = "pig";
			
		// horse
		} else if (type.toLowerCase().contains("horse")) {
			List<String> data = new ArrayList<String>(Arrays.asList(type.split(" ")));
			if (data.get(0).equalsIgnoreCase("horse")) {
				data.remove(0);
				type = "horse";
			} else if (data.size() >= 2 && data.get(1).equalsIgnoreCase("horse")) {
				String t = data.remove(0).toLowerCase();
				if (t.equals("skeleton") || t.equals("skeletal")) {
					type = "skeleton_horse";
				} else if (t.equals("zombie") || t.equals("undead")) {
					type = "zombie_horse";
				} else {
					type = "horse";
				}
				data.remove(0);
			}
			while (data.size() > 0) {
				String d = data.remove(0);
				if (d.equalsIgnoreCase("iron")) {
					var3 = 1;
				} else if (d.equalsIgnoreCase("gold")) {
					var3 = 2;
				} else if (d.equalsIgnoreCase("diamond")) {
					var3 = 3;
				}
			}
		
		// two worded
		} else if (type.equalsIgnoreCase("irongolem")) {
			type = "iron_golem";
		} else if (type.equalsIgnoreCase("mooshroom")) {
			type = "mushroom_cow";
		} else if (type.equalsIgnoreCase("magmacube")) {
			type = "magma_cube";
		} else if (type.equalsIgnoreCase("cavespider")) {
			type = "cave_spider";
			
		// different aliases
		} else if (type.equalsIgnoreCase("dragon")) {
			type = "ender_dragon";
		} else if (type.equalsIgnoreCase("snowgolem")) {
			type = "snowman";
			
		// block and item
		} else if (type.toLowerCase().startsWith("block") || type.toLowerCase().startsWith("fallingblock")) {
			String data = type.split(" ")[1].replace(' ', '_');
			try {
				if (Material.valueOf(data.toUpperCase()).isBlock()) {
					var1 = data;
				} else {
					throw new IllegalArgumentException();
				}
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid block type: " + data);
			}
			type = "falling_block";
		} else if (type.toLowerCase().startsWith("item")) {
			String data = type.split(" ")[1].replace(' ', '_');
			try {
				Material.valueOf(data.toUpperCase());
				var1 = data;
			} catch (IllegalArgumentException e) {
				MagicSpells.error("Invalid item type: " + data);
			}
			type = "item";
		}
		
		// cats
		if (type.toLowerCase().startsWith("cat ")) {
			String[] data = type.toLowerCase().replace("cat ", "").split(" ");
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
			var1 = data[1];
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
	
	public int getVar3() {
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
			if (var1.equals("random")) {
				
			} else {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.valueOf(var1.toUpperCase()));
			}
		} else if (entityType == EntityType.CREEPER) {
			if (flag) {
				((Creeper)entity).setPowered(true);
			}
		} else if (entityType == EntityType.WOLF) {
			if (var1.equals("angry")) {
				((Wolf)entity).setAngry(true);
			}
		} else if (entityType == EntityType.CAT) {
			if (var1.equals("random")) {
				
			} else {
				((Cat)entity).setCatType(Cat.Type.valueOf(var1.toUpperCase()));
			}
		} else if (entityType == EntityType.VILLAGER) {
			if (var1.equals("random")) {
				
			} else {
				((Villager)entity).setProfession(Villager.Profession.valueOf(var1.toUpperCase()));
			}
		} else if (entityType == EntityType.SLIME) {
			((Slime)entity).setSize(Integer.parseInt(var1));
		} else if (entityType == EntityType.MAGMA_CUBE) {
			((MagmaCube)entity).setSize(Integer.parseInt(var1));
		} else if (entityType == EntityType.PIG) {
			((Pig)entity).setSaddle(var1.equals("saddled"));
		} else if (entityType == EntityType.SHEEP) {
			((Sheep)entity).setColor(DyeColor.valueOf(var1.toUpperCase()));
		} else if (entityType == EntityType.RABBIT) {
			if (var1.equals("random")) {
				
			} else {
				((Rabbit)entity).setRabbitType(Rabbit.Type.valueOf(var1.toUpperCase()));
			}
		}
		return entity;
	}
}

package com.nisovin.magicspells.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import com.nisovin.magicspells.MagicSpells;

public class EntityData {

	private EntityType entityType;
	private boolean flag = false;
	private int var1 = 0;
	private int var2 = 0;
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
			String prof = type.toLowerCase().replace("zombie ", "");
			if (prof.matches("^[0-5]$")) {
				var1 = Integer.parseInt(prof);
			} else if (prof.toLowerCase().startsWith("green")) {
				var1 = 5;
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = getIdFromProfession(prof.toUpperCase());
				} catch (Exception e) {
					MagicSpells.error("Invalid zombie profession: " + prof);
				}
			}
			type = "zombie_villager";
		} else if (type.toLowerCase().endsWith(" zombie")) {
			String prof = type.toLowerCase().replace(" zombie", "");
			if (prof.toLowerCase().startsWith("green")) {
				var1 = 5;
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = getIdFromProfession(prof.toUpperCase());
				} catch (Exception e) {
					MagicSpells.error("Invalid zombie profession: " + prof);
				}
			}
			type = "zombie_villager";
			
		// creeper
		} else if (type.equalsIgnoreCase("powered creeper")) {
			type = "creeper";
			flag = true;
			
		// villager
		} else if (type.toLowerCase().startsWith("villager ")) {
			String prof = type.toLowerCase().replace("villager ", "");
			if (prof.matches("^[0-5]$")) {
				var1 = Integer.parseInt(prof);
			} else if (prof.toLowerCase().startsWith("green")) {
				var1 = 5;
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = getIdFromProfession(prof.toUpperCase());
				} catch (Exception e) {
					MagicSpells.error("Invalid villager profession: " + prof);
				}
			}
			type = "villager";
		} else if (type.toLowerCase().endsWith(" villager")) {
			String prof = type.toLowerCase().replace(" villager", "");
			if (prof.toLowerCase().startsWith("green")) {
				var1 = 5;
			} else {
				try {
					Villager.Profession.valueOf(prof.toUpperCase());
					var1 = getIdFromProfession(prof.toUpperCase());
				} catch (Exception e) {
					MagicSpells.error("Invalid villager profession: " + prof);
				}
			}
			type = "villager";
			
		// sheep
		} else if (type.toLowerCase().endsWith(" sheep")) {
			String color = type.toLowerCase().replace(" sheep", "");
			if (color.equalsIgnoreCase("random")) {
				var1 = -1;
			} else {
				try {
					var1 = getIdFromDyeColor(color.toUpperCase().replace(" ", "_"));
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid sheep color: " + color);
				}
			}
			type = "sheep";
			
		// rabbit
		} else if (type.toLowerCase().endsWith(" rabbit")) {
			String rabbitType = type.toLowerCase().replace(" rabbit", "");
			var1 = 0;
			if (rabbitType.equals("white")) {
				var1 = 1;
			} else if (rabbitType.equals("black")) {
				var1 = 2;
			} else if (rabbitType.equals("blackwhite")) {
				var1 = 3;
			} else if (rabbitType.equals("gold")) {
				var1 = 4;
			} else if (rabbitType.equals("saltpepper")) {
				var1 = 5;
			} else if (rabbitType.equals("killer")) {
				var1 = 99;
			}
			type = "rabbit";
			
		// wolf
		} else if (type.toLowerCase().startsWith("wolf ")) {
			String color = type.toLowerCase().replace("wolf ", "");
			if (color.equals("angry")) {
				var1 = -1;
			} else if (color.matches("[0-9a-fA-F]+")) {
				var1 = Integer.parseInt(color, 16);
			}
			type = "wolf";
		
		// pig
		} else if (type.toLowerCase().equalsIgnoreCase("saddled pig")) {
			var1 = 1;
			type = "pig";
			
		// horse
		} else if (type.toLowerCase().contains("horse")) {
			List<String> data = new ArrayList<String>(Arrays.asList(type.split(" ")));
			var1 = 0;
			var2 = 0;
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
				if (d.matches("^[0-9]+$")) {
					var2 = Integer.parseInt(d);
				} else if (d.equalsIgnoreCase("iron")) {
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
		} else if (type.toLowerCase().contains("ocelot")) {
			type = type.toLowerCase().replace("ocelot", "ozelot");
		} else if (type.equalsIgnoreCase("snowgolem")) {
			type = "snowman";
			
		// block and item
		} else if (type.toLowerCase().startsWith("block") || type.toLowerCase().startsWith("fallingblock")) {
			String data = type.split(" ")[1];
			if (data.contains(":")) {
				String[] subdata = data.split(":");
				var1 = Integer.parseInt(subdata[0]);
				var2 = Integer.parseInt(subdata[1]);
			} else {
				var1 = Integer.parseInt(data);
			}
			type = "falling_block";
		} else if (type.toLowerCase().startsWith("item")) {
			String data = type.split(" ")[1];
			if (data.contains(":")) {
				String[] subdata = data.split(":");
				var1 = Integer.parseInt(subdata[0]);
				var2 = Integer.parseInt(subdata[1]);
			} else {
				var1 = Integer.parseInt(data);
			}
			type = "item";
		}
		
		// ocelot types
		if (type.toLowerCase().matches("ozelot [0-3]")) {
			var1 = Integer.parseInt(type.split(" ")[1]);
			type = "ozelot";
		} else if (type.toLowerCase().equals("ozelot random") || type.toLowerCase().equals("random ozelot")) {
			var1 = -1;
			type = "ozelot";
		}
		
		// slime and magma cube size
		if (type.equals("slime") || type.equals("lavaslime")) {
			var1 = 1;
		} else if (type.startsWith("slime") || type.startsWith("magmacube") || type.startsWith("lavaslime")) {
			String[] data = type.split(" ");
			type = data[0];
			if (type.equals("magmacube")) type = "lavaslime";
			var1 = Integer.parseInt(data[1]);
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
	
	public int getVar1() {
		return var1;
	}
	
	public int getVar2() {
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
			if (var1 == 0) {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.FARMER);
			} else if (var1 == 1) {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.LIBRARIAN);
			} else if (var1 == 2) {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.CLERIC);
			} else if (var1 == 3) {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.TOOLSMITH);
			} else if (var1 == 4) {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.BUTCHER);
			} else if (var1 == 5) {
				((ZombieVillager)entity).setVillagerProfession(Villager.Profession.NITWIT);
			} // TODO: add other professions
		} else if (entityType == EntityType.CREEPER) {
			if (flag) {
				((Creeper)entity).setPowered(true);
			}
		} else if (entityType == EntityType.WOLF) {
			if (var1 == -1) {
				((Wolf)entity).setAngry(true);
			}
		} else if (entityType == EntityType.OCELOT) {
			if (var1 == 0) {
				((Ocelot)entity).setCatType(Ocelot.Type.WILD_OCELOT);
			} else if (var1 == 1) {
				((Ocelot)entity).setCatType(Ocelot.Type.BLACK_CAT);
			} else if (var1 == 2) {
				((Ocelot)entity).setCatType(Ocelot.Type.RED_CAT);
			} else if (var1 == 3) {
				((Ocelot)entity).setCatType(Ocelot.Type.SIAMESE_CAT);
			} // TODO: change to cat
		} else if (entityType == EntityType.VILLAGER) {
			if (var1 == 0) {
				((Villager)entity).setProfession(Villager.Profession.FARMER);
			} else if (var1 == 1) {
				((Villager)entity).setProfession(Villager.Profession.LIBRARIAN);
			} else if (var1 == 2) {
				((Villager)entity).setProfession(Villager.Profession.CLERIC);
			} else if (var1 == 3) {
				((Villager)entity).setProfession(Villager.Profession.TOOLSMITH);
			} else if (var1 == 4) {
				((Villager)entity).setProfession(Villager.Profession.BUTCHER);
			} else if (var1 == 5) {
				((Villager)entity).setProfession(Villager.Profession.NITWIT);
			} // TODO: add other professions
		} else if (entityType == EntityType.SLIME) {
			((Slime)entity).setSize(var1);
		} else if (entityType == EntityType.MAGMA_CUBE) {
			((MagmaCube)entity).setSize(var1);
		} else if (entityType == EntityType.PIG) {
			((Pig)entity).setSaddle(var1 == 1);
		} else if (entityType == EntityType.SHEEP) {
			DyeColor c = getDyeColorFromId(var1);
			if (c != null) {
				((Sheep)entity).setColor(c);
			}
		} else if (entityType == EntityType.RABBIT) {
			if (var1 == 0) {
				((Rabbit)entity).setRabbitType(Rabbit.Type.BROWN);
			} else if (var1 == 1) {
				((Rabbit)entity).setRabbitType(Rabbit.Type.WHITE);
			} else if (var1 == 2) {
				((Rabbit)entity).setRabbitType(Rabbit.Type.BLACK);
			} else if (var1 == 3) {
				((Rabbit)entity).setRabbitType(Rabbit.Type.BLACK_AND_WHITE);
			} else if (var1 == 4) {
				((Rabbit)entity).setRabbitType(Rabbit.Type.GOLD);
			} else if (var1 == 5) {
				((Rabbit)entity).setRabbitType(Rabbit.Type.SALT_AND_PEPPER);
			} else if (var1 == 99) {
				((Rabbit)entity).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
			}
		}
		return entity;
	}
	
	private int getIdFromProfession(String prof) {
		switch (prof) {
		case "FARMER":
			return 0;
		case "LIBRARIAN":
			return 1;
		case "PRIEST":
			return 2;
		case "BLACKSMITH":
			return 3;
		case "BUTCHER":
			return 4;
		case "NITWIT":
			return 5;
		default:
			return 0;
		}
	}
	
	private int getIdFromDyeColor(String color) {
		Map<DyeColor, Integer> colors = new HashMap<DyeColor, Integer>();

		int i = 0;
		for (DyeColor c : DyeColor.values()) {
			colors.put(c, i);
			i++;
		}
		
		return colors.get(DyeColor.valueOf(color));
	}
	
	private DyeColor getDyeColorFromId(int id) {
		Map<Integer, DyeColor> colors = new HashMap<Integer, DyeColor>();
		
		int i = 0;
		for (DyeColor c : DyeColor.values()) {
			colors.put(i, c);
			i++;
		}
		
		return colors.get(id);
	}
}

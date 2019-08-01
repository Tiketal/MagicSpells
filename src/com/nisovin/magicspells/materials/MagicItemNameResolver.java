package com.nisovin.magicspells.materials;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nisovin.magicspells.MagicSpells;

public class MagicItemNameResolver implements ItemNameResolver {

	Map<String, Material> materialMap = new HashMap<String, Material>();
//	Map<String, MaterialData> materialDataMap = new HashMap<String, MaterialData>();
	Random rand = new Random();
	
	public MagicItemNameResolver() {
		for (Material mat : Material.values()) {
			materialMap.put(mat.name().toLowerCase(), mat);
		}
		
		File file = new File(MagicSpells.getInstance().getDataFolder(), "itemnames.yml");
		if (!file.exists()) {
			MagicSpells.getInstance().saveResource("itemnames.yml", false);
		}
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
			for (String s : config.getKeys(false)) {
				Material m = materialMap.get(config.getString(s).toLowerCase());
				if (m != null) {
					materialMap.put(s.toLowerCase(), m);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Material> toAdd = new HashMap<String, Material>();
		for (String s : materialMap.keySet()) {
			if (s.contains("_")) {
				toAdd.put(s.replace("_", ""), materialMap.get(s));
			}
		}
		materialMap.putAll(toAdd);
	}
	
	@Override
	public ItemTypeAndData resolve(String string) {
		if (string == null || string.isEmpty()) return null;
		ItemTypeAndData item = new ItemTypeAndData();
		if (string.contains(":")) {
			String[] split = string.split(":");
			Material mat = Material.getMaterial(split[0].toUpperCase());
			if (mat == null) return null;
			item.type = mat;
			if (split[1].matches("[0-9]+")) {
				item.data = Short.parseShort(split[1]);
			} else {
				return null;
			}
		} else {
			Material mat = Material.getMaterial(string.toUpperCase());
			if (mat == null) return null;
			item.type = mat;
		}
		return item;
	}
	
	@Override
	public MagicMaterial resolveItem(String string) {
		if (string == null || string.isEmpty()) return null;
		
		// first check for predefined material datas
		/*MaterialData matData = materialDataMap.get(string.toLowerCase());
		if (matData != null) {
			if (matData.getItemType().isBlock()) {
				return new MagicBlockMaterial(matData);
			} else {
				return new MagicItemMaterial(matData);
			}
		}*/
		
		// split type and data
		// <type>:<data> or <type> <data>
		String stype;
		String sdata;
		if (string.contains(":")) {
			String[] split = string.split(":", 2);
			stype = split[0].toLowerCase();
			sdata = split[1].toLowerCase();
		} else if (string.contains(" ")) {
			String[] split = string.split(" ", 2);
			sdata = split[0].toLowerCase();
			stype = split[1].toLowerCase();
		} else {
			stype = string.toLowerCase();
			sdata = "";
		}
		
		Material type = materialMap.get(stype);
		if (type == null) {
			return null;
		}
		
		if (type.isBlock()) {
			return new MagicBlockMaterial(type, resolveBlockData(type, sdata));
		} else {
			
//TODO			if (sdata.equals("*")) return new MagicItemAnyDataMaterial(type);
			short durability = 0;
			try {
				durability = Short.parseShort(sdata);
			} catch (NumberFormatException e) {}
			return new MagicItemMaterial(type, durability);
		}
	}
	
	@Override
	public MagicMaterial resolveBlock(String string) {
		if (string == null || string.isEmpty()) return null;
		
		if (string.contains("|")) {
			return resolveRandomBlock(string);
		}
		
		String stype;
		String sdata;
		if (string.contains(":")) {
			String[] split = string.split(":", 2);
			stype = split[0].toLowerCase();
			sdata = split[1];
		} else {
			stype = string.toLowerCase();
			sdata = "";
		}
		
		Material type = materialMap.get(stype);
		if (type == null) {
			return null;
		}
		
		if (type.isBlock()) {
			if (sdata.equals("*")) {
// TODO ex: *_log, *_wood
			} else {
				return new MagicBlockMaterial(type, resolveBlockData(type, sdata));
			}
		} else {
			return null;
		}
		
		return null;
	}
	
	private MagicMaterial resolveRandomBlock(String string) {
		List<MagicMaterial> materials = new ArrayList<MagicMaterial>();
		String[] strings = string.split("\\|");
		for (String s : strings) {
			MagicMaterial mat = resolveBlock(s.trim());
			if (mat != null) {
				materials.add(mat);
			}
		}
		return new MagicBlockRandomMaterial(materials.toArray(new MagicMaterial[materials.size()]));
	}
	
	// TODO: block data
	/*private MagicBlockMaterial resolveBlockData(Material type, String sdata) {
		if (type == Material.LOG || type == Material.SAPLING || type == Material.WOOD) {
			return getTree(sdata);
		} else if (type == Material.LEAVES) {
			return getLeaves(sdata);
		} else if (type == Material.WOOL) {
			return getWool(sdata);
		} else if (sdata.matches("^[0-9]+$")) {
			return new MaterialData(type, Byte.parseByte(sdata));
		} else {
			return new MaterialData(type);
		}
	}*/
	
	/*private MaterialData resolveItemData(Material type, String sdata) {
		if (type == Material.INK_SACK) {
			return getDye(sdata);
		} else {
			return null;
		}
	}*/
	
	// TODO
	private BlockData resolveBlockData(Material type, String data) {
		BlockData bd = type.createBlockData();
		
		if (bd instanceof Orientable) {
			
		} else if (bd instanceof Directional) {
			
		} else if (bd instanceof Ageable) {
			
		}
		
		return bd;
	}
	
	/*private MagicMaterial resolveUnknown(String stype, String sdata) {
		try {
			int type = Integer.parseInt(stype);
			if (sdata.equals("*")) {
				return new MagicUnknownAnyDataMaterial(type);
			} else {
				short data = ((sdata == null || sdata.isEmpty()) ? 0 : Short.parseShort(sdata));
				return new MagicUnknownMaterial(type, data);
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}*/
	
	// TODO
	/*
	private Dye getDye(String data) {
		Dye dye = new Dye();
		dye.setColor(getDyeColor(data));
		return dye;
	}
	
	private Wool getWool(String data) {
		return new Wool(getDyeColor(data));
	}
	
	private DyeColor getDyeColor(String data) {
		if (data != null && data.equalsIgnoreCase("random")) {
			return DyeColor.values()[rand.nextInt(DyeColor.values().length)];
		} else {
			DyeColor color = DyeColor.WHITE;
			if (data != null && data.length() > 0) {
				data = data.replace("_", "").replace(" ", "").toLowerCase();
				for (DyeColor c : DyeColor.values()) {
					if (data.equals(c.name().replace("_", "").toLowerCase())) {
						color = c;
						break;
					}
				}
			}
			return color;
		}
	}
	
	private MagicBlockMaterial getTree(String data) {
		Material mat = null;
		BlockData bd = null;
		if (data != null && data.length() > 0) {
			String[] split = data.split("[: ]");
			if (split.length >= 1) {
				mat = getTreeMaterial(split[0]);
			}
			if (split.length >= 2) {
				bd = getOrientation(mat, split[1]);
			}
		}
		return new MagicBlockMaterial(mat, bd);
	}
	
	private BlockData getOrientation(Material mat, String data) {
		BlockData bd = mat.createBlockData();
		((Orientable)bd).setAxis(Axis.Y);
		
		if (data.equalsIgnoreCase("east") || data.equalsIgnoreCase("west")) {
			((Orientable)bd).setAxis(Axis.X);
		} else if (data.equalsIgnoreCase("north") || data.equalsIgnoreCase("south")) {
			((Orientable)bd).setAxis(Axis.Z);
		} else if (data.equalsIgnoreCase("random")) {
			int r = rand.nextInt(3);
			if (r == 0) {
				((Orientable)bd).setAxis(Axis.X);
			} else if (r == 1) {
				((Orientable)bd).setAxis(Axis.Z);
			}
		}
		
		return bd;
	}
	
	private Material getTreeMaterial(String data) {
		data = data.toUpperCase();
		if (data.contains("RANDOM")) {
			data = data.replace("RANDOM", 
					TreeSpecies.values()[rand.nextInt(TreeSpecies.values().length)].toString());
		}
		return Material.getMaterial(data);
	}
	
	private Material getLeaves(String data) {
		TreeSpecies species = getTreeSpecies(data);
		if (species == TreeSpecies.GENERIC) {
			data = "OAK";
		}
		
		return Material.getMaterial(data.toUpperCase()+ '_' + "_LEAVES");
	}
	
	private TreeSpecies getTreeSpecies(String data) {
		if (data.equalsIgnoreCase("random")) {
			return TreeSpecies.values()[rand.nextInt(TreeSpecies.values().length)];
		}
		
		TreeSpecies species = null;
		try {
			species = TreeSpecies.valueOf(data.toUpperCase());
		} catch (IllegalArgumentException e) {}
		
		if (species == null) {
			return TreeSpecies.GENERIC;
		}
		return species;
	}*/

}

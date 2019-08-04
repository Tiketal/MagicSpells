package com.nisovin.magicspells.materials;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nisovin.magicspells.MagicSpells;

public class MagicItemNameResolver implements ItemNameResolver {

	Map<String, Material> materialMap = new HashMap<String, Material>();
//	Map<String, MaterialData> materialDataMap = new HashMap<String, MaterialData>();
	Random rand = new Random();
	
	public MagicItemNameResolver() {
		// associate string name with Material enum
		for (Material mat : Material.values()) {
			materialMap.put(mat.name().toLowerCase(), mat);
		}
		
		// load item aliases
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
		
		// add underscore-less aliases
		Map<String, Material> toAdd = new HashMap<String, Material>();
		for (String s : materialMap.keySet()) {
			if (s.contains("_")) {
				toAdd.put(s.replace("_", ""), materialMap.get(s));
			}
		}
		materialMap.putAll(toAdd);
	}
	
	private static Pattern pattern = Pattern.compile(
			"([\\w|\\*]+)(?:\\((\\w+:\\w+(?:,\\w+:\\w+)*)\\))*", // ([\w|\*]+)(?:\((\w+:\w+(?:,\w+:\w+)*)\))*
			Pattern.CASE_INSENSITIVE
			);
	
	/**
	 * Parses and resolves strings of the form 
	 * {@literal <material name>}{(state:data{,state:data}*)}?
	 * @param string
	 * @return MagicMaterial equivalent
	 */
	@Override
	public MagicMaterial resolveItem(String string) {
		if (string == null || string.isEmpty()) return null;
		
		
		// first check for predefined material datas - UNUSED
		/*MaterialData matData = materialDataMap.get(string.toLowerCase());
		if (matData != null) {
			if (matData.getItemType().isBlock()) {
				return new MagicBlockMaterial(matData);
			} else {
				return new MagicItemMaterial(matData);
			}
		}*/
		
		Matcher matcher = pattern.matcher(string);
		if (!matcher.matches()) return null;
		
		// split type and data
		String stype = matcher.group(1).toLowerCase();
		String sdata = matcher.group(2); // can be null if empty
		if (sdata == null) sdata = "";
		
		// check for correct material
		Material type = materialMap.get(stype);
		if (type == null) {
			return null;
		}
		
		// create MagicMaterial equivalent
		if (type.isBlock()) {
			return new MagicBlockMaterial(type, resolveBlockData(type, sdata));
		} else {
			
//TODO			if (sdata.equals("*")) return new MagicItemAnyDataMaterial(type);
			
			// items with durability/data values
			short durability = 0;
			try {
				durability = Short.parseShort(sdata);
			} catch (NumberFormatException e) {}
			return new MagicItemMaterial(type, durability);
		}
	}
	
	/**
	 * Parses and resolves strings of the form
	 * {@literal <material name>}{(state:data{,state:data}*)}?
	 * @param string
	 * @return MagicMaterial equivalent
	 */
	@Override
	public MagicMaterial resolveBlock(String string) {
		if (string == null || string.isEmpty()) return null;
		
		// random blocks
		if (string.contains("|")) {
			return resolveRandomBlock(string);
		}
		
		Matcher matcher = pattern.matcher(string);
		if (!matcher.matches()) return null;
		
		// split type and data
		String stype = matcher.group(1).toLowerCase();
		String sdata = matcher.group(2); // can be null if empty
		if (sdata == null) sdata = "";
		
		// wildcards ex: *_log or *_wood TODO
		
		
		// check for correct material
		Material type = materialMap.get(stype);
		if (type == null) {
			return null;
		}

		// create MagicMaterial
		if (type.isBlock()) {
			return new MagicBlockMaterial(type, resolveBlockData(type, sdata));
		} else {
			return null;
		}
		
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
	
	/**
	 * Parse block data if applicable
	 * @param type
	 * @param data in the form: state:data{,state:data}*
	 * @return
	 */
	private BlockData resolveBlockData(Material type, String data) {
		BlockData bd = type.createBlockData();
		if (data.isEmpty()) return bd;
		
		// parse data
		String[] split = data.split(",");
		String[] args = null;
		
		for (String tag : split) {
			args = tag.split(":");
			
			try {
				if (args[0].equalsIgnoreCase("age") 
						&& bd instanceof Ageable) {
					
					int arg1 = Integer.parseInt(split[1]);
					((Ageable)bd).setAge(arg1);
					
				} else if ((args[0].equalsIgnoreCase("orientation") 
						|| args[0].equalsIgnoreCase("orient") 
						|| args[0].equalsIgnoreCase("direction")
						|| args[0].equalsIgnoreCase("dir"))) {

					BlockFace dir = BlockFace.valueOf(args[1].toUpperCase());
					if (bd instanceof Directional) {
						((Directional)bd).setFacing(dir);
						
					} else if (bd instanceof Orientable) {
						if (dir == BlockFace.UP || dir == BlockFace.DOWN) {
							((Orientable)bd).setAxis(Axis.Y);
							
						} else if (dir == BlockFace.EAST || dir == BlockFace.WEST) {
							((Orientable)bd).setAxis(Axis.X);
							
						} else if (dir == BlockFace.NORTH || dir == BlockFace.SOUTH) {
							((Orientable)bd).setAxis(Axis.Z);
							
						}
					}
					
				} else if (args[0].equalsIgnoreCase("level")
						&& bd instanceof Levelled) {
					int lvl = Integer.parseInt(args[1]);
					((Levelled)bd).setLevel(lvl);
					
				} else if ((args[0].equalsIgnoreCase("lit") || args[0].equalsIgnoreCase("light"))
						&& bd instanceof Lightable) {
					boolean lit = Boolean.parseBoolean(args[1]);
					((Lightable)bd).setLit(lit);
					
				} else if ((args[0].equalsIgnoreCase("water") || args[0].equalsIgnoreCase("waterlogged"))
						&& bd instanceof Waterlogged) {
					boolean water = Boolean.parseBoolean(args[1]);
					((Waterlogged)bd).setWaterlogged(water);;
				
				} else if (args[0].equalsIgnoreCase("moisture")
						&& bd instanceof Farmland) {
					int moisture = Integer.parseInt(args[1]);
					((Farmland)bd).setMoisture(moisture);
					
				} else if ((args[0].equalsIgnoreCase("powered") || args[0].equalsIgnoreCase("power"))
						&& bd instanceof Powerable) {
					boolean powered = Boolean.parseBoolean(args[1]);
					((Powerable)bd).setPowered(powered);
					
				} else {
					// error message: invalid tag
					MagicSpells.error("Invalid block data: " + tag);
				}
			} catch (Exception e) {
				// error message: invalid tag
				MagicSpells.error("Invalid block data: " + tag);
			}
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

package com.nisovin.magicspells.materials;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
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
			"([\\w|\\*]+)(?:(\\[\\w+=\\w+(?:,\\w+=\\w+)*\\]))*", // ([\w|\*]+)(?:(\[\w+=\w+(?:,\w+=\w+)*\]))*
			Pattern.CASE_INSENSITIVE
			);
	
	/**
	 * Parses and resolves strings of the form 
	 * {@literal <material name>}{[state=data{,state=data}*]}?
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
			MagicSpells.error("Invalid type: " + stype);
			return null;
		}
		
		// create MagicMaterial equivalent
		if (type.isBlock()) {
			if (sdata != null) {
				try {
					return new MagicBlockMaterial(type.createBlockData(sdata));
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid block data: " + sdata);
				}
			}
			return new MagicBlockMaterial(type.createBlockData());
		} else {
			
//TODO			if (sdata.equals("*")) return new MagicItemAnyDataMaterial(type);
			
			// items with durability/data values
			short damage = 0;
			if (sdata != null) {
				sdata = sdata.replaceAll("[\\[\\]]", "");
				String[] split;
				for (String tag : sdata.split(",")) {
					split = tag.split("=");
					if (split[0].equalsIgnoreCase("damage") || split[0].startsWith("dura")) {
						try {
							damage = Short.parseShort(split[1]);
						} catch (NumberFormatException e) {}
					}
				}
			}
			return new MagicItemMaterial(type, damage);
		}
	}
	
	/**
	 * Parses and resolves strings of the form
	 * {@literal <material name>}{[state=data{,state=data}*]}?
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
		String sdata = matcher.group(2);
		
		// check for correct material
		Material type = materialMap.get(stype);
		if (type == null) {
			return null;
		}

		// create MagicMaterial
		if (type.isBlock()) {
			if (sdata != null) {
				try {
					return new MagicBlockMaterial(type.createBlockData(sdata));
				} catch (IllegalArgumentException e) {
					MagicSpells.error("Invalid block data: " + sdata);
				}
			}
			return new MagicBlockMaterial(type.createBlockData());
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
}

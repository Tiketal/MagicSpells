package com.nisovin.magicspells.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts Bukkit enums to Minecraft names
 *
 */
public class BukkitToMinecraft {
	private final static Map<String, String> POTION_EFFECTS = new HashMap<>();
	
	static {
		
	}
	
	public static String toMinecraftPotionEffect(String string) {
		return POTION_EFFECTS.get(string);
	}
}

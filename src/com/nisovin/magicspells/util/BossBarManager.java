package com.nisovin.magicspells.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarManager {

	Map<String, BossBar> bars = new HashMap<String, BossBar>();
	
	public void setPlayerBar(Player player, String title, double percent, String color, String style) {
		BossBar bar = bars.get(player.getName());
		if (bar == null) {
			bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', title), getBarColor(color), getBarStyle(style));
			bars.put(player.getName(), bar);
		}
		bar.setTitle(ChatColor.translateAlternateColorCodes('&', title));
		bar.setProgress(percent);
		bar.addPlayer(player);
	}
	
	private BarColor getBarColor(String color) {
		try {
			return BarColor.valueOf(color.toUpperCase());
		} catch (IllegalArgumentException e) {
			return BarColor.PURPLE;
		}
	}
	
	private BarStyle getBarStyle(String style) {
		style = style.replace(" ", "_");
		try {
			return BarStyle.valueOf(style.toUpperCase());
		} catch (IllegalArgumentException e) {
			return BarStyle.SOLID;
		}
	}

	public void removePlayerBar(Player player) {
		BossBar bar = bars.remove(player.getName());
		if (bar != null) {
			bar.removeAll();
		}
	}

	public void turnOff() {
		for (BossBar bar : bars.values()) {
			bar.removeAll();
		}
		bars.clear();
	}

}

package com.nisovin.magicspells.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarManager_V1_9 implements BossBarManager {

	Map<String, BossBar> bars = new HashMap<String, BossBar>();
	
	@Override
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
		color = color.toUpperCase();
		
		switch (color) {
		case "BLUE":
			return BarColor.BLUE;
		case "GREEN":
			return BarColor.GREEN;
		case "PINK":
			return BarColor.PINK;
		case "RED":
			return BarColor.RED;
		case "WHITE":
			return BarColor.WHITE;
		case "YELLOW":
			return BarColor.YELLOW;
		default:
			return BarColor.PURPLE;
		}
	}
	
	private BarStyle getBarStyle(String style) {
		style = style.replace(" ", "_");
		style = style.toUpperCase();
		
		switch (style) {
		case "SEGMENTED_10":
			return BarStyle.SEGMENTED_10;
		case "SEGMENTED_12":
			return BarStyle.SEGMENTED_12;
		case "SEGMENTED_20":
			return BarStyle.SEGMENTED_20;
		case "SEGMENTED_6":
			return BarStyle.SEGMENTED_6;
		default:
			return BarStyle.SOLID;
		}
	}

	@Override
	public void removePlayerBar(Player player) {
		BossBar bar = bars.remove(player.getName());
		if (bar != null) {
			bar.removeAll();
		}
	}

	@Override
	public void turnOff() {
		for (BossBar bar : bars.values()) {
			bar.removeAll();
		}
		bars.clear();
	}

}

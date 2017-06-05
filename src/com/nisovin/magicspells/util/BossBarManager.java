package com.nisovin.magicspells.util;

import org.bukkit.entity.Player;

public interface BossBarManager {

	public void setPlayerBar(Player player, String title, double percent, String color, String style);
	
	public void removePlayerBar(Player player);
	
	public void turnOff();
	
}

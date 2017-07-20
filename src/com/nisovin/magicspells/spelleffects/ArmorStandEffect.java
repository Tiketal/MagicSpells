package com.nisovin.magicspells.spelleffects;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.util.Util;

public class ArmorStandEffect extends SpellEffect {
	
	ItemStack item;
	int duration;
	
	@Override
	public void loadFromString(String string) {
		String[] split = Util.splitParams(string);
		item = Util.getItemStackFromString(split[0]);
		duration = Integer.parseInt(split[1]);
	}

	@Override
	protected void loadFromConfig(ConfigurationSection config) {
		item = Util.getItemStackFromString(config.getString("item", "stone"));
		duration = config.getInt("duration", 5);
	}
	
	@Override
	public void playEffectLocation(Location location) {
		Location loc = location.clone();
		loc.setY(loc.getY() - 1.75);
		MagicSpells.getVolatileCodeHandler().spawnCosmeticArmorStand(loc, item, duration);
	}
	
	@Override
	protected void playEffectLine(Location location1, Location location2) {
		Util.setLocationFacingFromVector(location1, location2.toVector());
		super.playEffectLine(location1, location2);
	}
}

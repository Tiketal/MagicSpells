package com.nisovin.magicspells.spelleffects;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.nisovin.magicspells.MagicSpells;

public class SoundEffect extends SpellEffect {
	
	String sound = "entity.item.pickup";
	float volume = 1.0F;
	float pitch = 1.0F;
	String category = "master";

	@Override
	public void loadFromString(String string) {
		if (string != null && string.length() > 0) {
			String[] data = string.split(" ");
			sound = data[0];
			if (data.length > 1) {
				volume = Float.parseFloat(data[1]);
			}
			if (data.length > 2) {
				pitch = Float.parseFloat(data[2]);
			}
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection config) {
		sound = config.getString("sound", sound);
		volume = (float)config.getDouble("volume", volume);
		pitch = (float)config.getDouble("pitch", pitch);
		category = config.getString("category", category);
	}
	
	@Override
	public void playEffectLocation(Location location) {
		MagicSpells.getVolatileCodeHandler().playSound(location, sound, volume, pitch, category);
	}
}

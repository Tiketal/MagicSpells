package com.nisovin.magicspells.spelleffects;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.materials.MagicItemMaterial;
import com.nisovin.magicspells.materials.MagicMaterial;

class ItemSprayEffect extends SpellEffect {

	MagicMaterial mat;
	int num = 15;
	int duration = 10;
	float force = 1.0F;

	@Override
	public void loadFromString(String string) {
		if (string != null) {
			String[] data = string.split(" ");
			Material type = Material.REDSTONE;
			if (data.length >= 1) {
				type = Material.getMaterial(data[0]);
			}
			mat = new MagicItemMaterial(type);
			if (data.length >= 2) {
				try {
					num = Integer.parseInt(data[1]);
				} catch (NumberFormatException e) {
				}
			}
			if (data.length >= 3) {
				try {
					duration = Integer.parseInt(data[2]);
				} catch (NumberFormatException e) {
				}
			}
			if (data.length >= 4) {
				try {
					force = Float.parseFloat(data[3]);
				} catch (NumberFormatException e) {
				}
			}
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection config) {
		mat = MagicSpells.getItemNameResolver().resolveItem(config.getString("type", "redstone"));
		num = config.getInt("quantity", num);
		duration = config.getInt("duration", duration);
		force = (float)config.getDouble("force", force);
	}
	
	@Override
	public void playEffectLocation(Location location) {
		if (mat == null) return;
		
		// spawn items
		Random rand = new Random();
		Location loc = location.clone().add(0, 1, 0);
		final Item[] items = new Item[num];
		for (int i = 0; i < num; i++) {
			items[i] = loc.getWorld().dropItem(loc, mat.toItemStack(0));
			items[i].setVelocity(new Vector((rand.nextDouble()-.5) * force, (rand.nextDouble()-.5) * force, (rand.nextDouble()-.5) * force));
			items[i].setPickupDelay(duration * 2);
		}
		
		// schedule item deletion
		MagicSpells.scheduleDelayedTask(new Runnable() {
			public void run() {
				for (int i = 0; i < items.length; i++) {
					items[i].remove();
				}
			}
		}, duration);
	}
	
}

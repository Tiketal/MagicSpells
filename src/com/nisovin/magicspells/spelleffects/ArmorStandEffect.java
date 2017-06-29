package com.nisovin.magicspells.spelleffects;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

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
		
		//final Entity entity = MagicSpells.getVolatileCodeHandler().spawnCosmeticArmorStand(loc, item);
		final Entity entity = loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		
		((ArmorStand)entity).setVisible(false);
		((ArmorStand)entity).setHeadPose(new EulerAngle(Math.toRadians(loc.getPitch()), 0, 0));
		((ArmorStand)entity).setGravity(false);
		((ArmorStand)entity).setMarker(true);
		((ArmorStand)entity).setInvulnerable(true);
		((ArmorStand)entity).setHelmet(item);
		
		MagicSpells.scheduleDelayedTask(new Runnable() {
			public void run() {
				entity.remove();
			}
		}, duration);
	}
	
	@Override
	protected void playEffectLine(Location location1, Location location2) {
		double dx = location1.getX() - location2.getX();
		double dy = location1.getY() - location2.getY();
		double dz = location1.getZ() - location2.getZ();
		
		// calculate the pitch of the vector for subsequent bounces
		float pitch = (float)(-1 * ((Math.toDegrees(Math.atan2(Math.sqrt(dz*dz + dx * dx), dy) + Math.PI) - (float)270)));
		float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) + (float)90);
		
		Location loc1 = location1.clone();
		loc1.setPitch(pitch);
		loc1.setYaw(yaw);
		
		super.playEffectLine(loc1, location2);
	}
}

package com.nisovin.magicspells.spelleffects;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import com.nisovin.magicspells.MagicSpells;

import java.util.Random;

class ParticlesEffect extends SpellEffect {
	
	String name = "poof";
	float xSpread = 0.2F;
	float ySpread = 0.2F;
	float zSpread = 0.2F;
	float xData = 0F;
	float yData = 0F;
	float zData = 0F;
	float speed = 0.2F;
	int count = 5;
	float yOffset = 0F;
	int renderDistance = 32;

	@Override
	public void loadFromString(String string) {
		if (string != null && !string.isEmpty()) {
			String[] data = string.split(" ");
			
			if (data.length >= 1) {
				name = data[0];
			}
			if (data.length >= 2) {
				xSpread = Float.parseFloat(data[1]);
				zSpread = xSpread;
			}
			if (data.length >= 3) {
				ySpread = Float.parseFloat(data[2]);
			}
			if (data.length >= 4) {
				speed = Float.parseFloat(data[3]);
			}
			if (data.length >= 5) {
				count = Integer.parseInt(data[4]);
			}
			if (data.length >= 6) {
				yOffset = Float.parseFloat(data[5]);
			}
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection config) {
		name = config.getString("particle-name", name);
		xSpread = (float)config.getDouble("horiz-spread", xSpread);
		ySpread = (float)config.getDouble("vert-spread", ySpread);
		zSpread = xSpread;
		xData = (float)config.getDouble("horiz-spread", xSpread);
		speed = (float)config.getDouble("speed", speed);
		count = config.getInt("count", count);
		if (count == 0) {
			xData = (float)config.getDouble("x-data");
			yData = (float)config.getDouble("y-data");
			zData = (float)config.getDouble("z-data");
		}
		yOffset = (float)config.getDouble("y-offset", yOffset);
		renderDistance = config.getInt("render-distance", renderDistance);
	}

	@Override
	public void playEffectLocation(Location location) {
		if (count == 0) {
			double randomX, randomY, randomZ;
			Location loc = location.clone();
			Random random = new Random();
			
			for (int i = 0; i < count; i++) {
				// random spread
				randomX = xSpread * random.nextGaussian();
				randomY = ySpread * random.nextGaussian();
				randomZ = zSpread * random.nextGaussian();
				
				// set location
				loc.setX(randomX + location.getX());
				loc.setY(randomY + location.getY());
				loc.setZ(randomZ + location.getZ());
				
				// spawn particle
				MagicSpells.getVolatileCodeHandler().playParticleEffect(loc, name, xData, yData, zData, speed, count, renderDistance, yOffset);
			}
		} else {
			MagicSpells.getVolatileCodeHandler().playParticleEffect(location, name, xSpread, ySpread, zSpread, speed, count, renderDistance, yOffset);
		}
	}
}

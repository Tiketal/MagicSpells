package com.nisovin.magicspells.spelleffects;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.events.SpellCastEvent;

public class ParticlesPersonalEffect extends SpellEffect {
	
	String name = "explode";
	float xSpread = 0.2F;
	float ySpread = 0.2F;
	float zSpread = 0.2F;
	float speed = 0.2F;
	int count = 5;
	float yOffset = 0F;
	int renderDistance = 32;
	
	Player player;

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
			startGetCasterListener();
		}
	}

	@Override
	public void loadFromConfig(ConfigurationSection config) {
		name = config.getString("particle-name", name);
		xSpread = (float)config.getDouble("horiz-spread", xSpread);
		ySpread = (float)config.getDouble("vert-spread", ySpread);
		zSpread = xSpread;
		speed = (float)config.getDouble("speed", speed);
		count = config.getInt("count", count);
		yOffset = (float)config.getDouble("y-offset", yOffset);
		renderDistance = config.getInt("render-distance", renderDistance);
		startGetCasterListener();
	}

	@Override
	public void playEffectLocation(Location location) {
		MagicSpells.getVolatileCodeHandler().playParticleEffect(player, location, name, xSpread, ySpread, zSpread, speed, count, renderDistance, yOffset);
	}
	
	private void startGetCasterListener() {
		MagicSpells.registerEvents(new Listener() {
				@EventHandler(ignoreCancelled=true)
				public void onSpellCast(SpellCastEvent event) {
					Map<EffectPosition, List<SpellEffect>> effects = event.getSpell().getEffects();
					
					if (effects == null) return;
					
					Player caster = event.getCaster();
					ParticlesPersonalEffect thisEffect = ParticlesPersonalEffect.this;
					
					List<SpellEffect> spellEffects;
					
					for (EffectPosition pos : effects.keySet()) {
						spellEffects = effects.get(pos);
						
						if (spellEffects == null) continue;
						
						for (SpellEffect effect : spellEffects) {
							if (thisEffect.equals(effect)) {
								thisEffect.player = caster;
								return;
							}
						}
					}
				}
			}
		);
	}

}

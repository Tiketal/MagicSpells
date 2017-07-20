package com.nisovin.magicspells.spells.instant;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Subspell;
import com.nisovin.magicspells.events.SpellTargetEvent;
import com.nisovin.magicspells.spelleffects.EffectPosition;
import com.nisovin.magicspells.spells.InstantSpell;
import com.nisovin.magicspells.util.BoundingBox;
import com.nisovin.magicspells.util.MagicConfig;

public class BeamSpell extends InstantSpell {
	
	float beamWidth;
	float beamSpread;
	float yOffset;
	float maxDistance;
	float interval;
	boolean stopOnHitEntity;
	boolean stopOnHitGround;
	String spellNameToCast;
	Subspell spell;
	
	Random rand = new Random();
	
	public BeamSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		beamWidth = getConfigFloat("beam-width", 2);
		beamSpread = getConfigFloat("beam-spread", 0F);
		yOffset = getConfigFloat("y-offset", 0);
		maxDistance = getConfigFloat("max-distance", 50);
		interval = getConfigFloat("interval", 0.25f);
		stopOnHitEntity = getConfigBoolean("stop-on-hit-entity", false);
		stopOnHitGround = getConfigBoolean("stop-on-hit-ground", true);
		spellNameToCast = getConfigString("spell", "");
		
		if (interval < 0.01) interval = 0.01f;
	}
	
	@Override
	public void initialize() {
		super.initialize();
		spell = new Subspell(spellNameToCast);
		if (!spell.process()) {
			MagicSpells.error("Beam Spell '" + internalName + "' has invalid spell defined");
		}
	}

	@Override
	public PostCastAction castSpell(Player player, SpellCastState state, float power, String[] args) {
		if (state == SpellCastState.NORMAL) {
			
			//float distSq = maxDistance * maxDistance;
			
			Vector start = player.getEyeLocation().toVector();
			start = start.add(new Vector(0, yOffset, 0));
			Vector dir = player.getEyeLocation().getDirection();
			if (beamSpread > 0) {
				dir.add(new Vector(rand.nextFloat() * beamSpread, rand.nextFloat() * beamSpread, rand.nextFloat() * beamSpread));
				dir.subtract(new Vector(rand.nextFloat() * beamSpread, rand.nextFloat() * beamSpread, rand.nextFloat() * beamSpread));
			}
			dir = dir.multiply(interval);
			Vector pos = start.clone();
			
			BoundingBox box;
			List<LivingEntity> entities = player.getWorld().getLivingEntities();
			entities.remove(player);
			float d = 0;
			boolean hit = false;
			while (d < maxDistance) {
				d += interval;
				pos.add(dir);
				
				if (stopOnHitEntity) {
					box = new BoundingBox(pos.toLocation(player.getWorld()), beamWidth);
					for (LivingEntity e : entities) {
						if (!e.equals(player) && !e.isDead() && box.contains(e.getLocation().add(0, 0.8, 0)) && (validTargetList == null || validTargetList.canTarget(e))) {
								SpellTargetEvent event = new SpellTargetEvent(this, player, e, power);
								Bukkit.getPluginManager().callEvent(event);
								if (!event.isCancelled()) {
									spell.castAtEntity(player, event.getTarget(), event.getPower());
									playSpellEffects(EffectPosition.TARGET, event.getTarget());
									hit = true;
									break;
								}
						}
					}
				}
				
				if (stopOnHitGround && !isTransparent(pos.toLocation(player.getWorld()).getBlock())) {
					break;
				}
				playSpellEffects(EffectPosition.SPECIAL, pos.toLocation(player.getWorld()));
				if (hit == true) break;
			}
			
			if (!stopOnHitEntity) {
				box = new BoundingBox(start.toLocation(player.getWorld()), pos.toLocation(player.getWorld()));
				box.expand(beamWidth);
				for (LivingEntity e : entities) {
					if (!e.equals(player) && !e.isDead() && box.contains(e) && (validTargetList == null || validTargetList.canTarget(e))) {
					double dist = pointLineDist(start, pos, e.getLocation().add(0, 0.8, 0).toVector());
						if (dist < beamWidth/2) {
							SpellTargetEvent event = new SpellTargetEvent(this, player, e, power);
							Bukkit.getPluginManager().callEvent(event);
							if (!event.isCancelled()) {
								spell.castAtEntity(player, event.getTarget(), event.getPower());
								playSpellEffects(EffectPosition.TARGET, event.getTarget());
							}
						}
					}
				}
			}
			
			playSpellEffects(EffectPosition.CASTER, player);
		}
		
		return PostCastAction.HANDLE_NORMALLY;
	}
	
	double pointLineDist(Vector p1, Vector p2, Vector p0) {
		Vector v1 = p2.clone().subtract(p1);
		Vector v2 = p1.clone().subtract(p0);
		Vector v3 = v1.clone().crossProduct(v2);
		return v3.length() / v1.length();
	}

}

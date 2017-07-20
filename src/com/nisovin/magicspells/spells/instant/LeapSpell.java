package com.nisovin.magicspells.spells.instant;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.spelleffects.EffectPosition;
import com.nisovin.magicspells.spells.InstantSpell;
import com.nisovin.magicspells.util.MagicConfig;

public class LeapSpell extends InstantSpell {
	
	private double forwardVelocity;
	private double upwardVelocity;
	private double velocity;
	private boolean cancelDamage;
	private boolean clientOnly;
	private boolean leapFacing;
	private boolean leapAtCursor;
	
	private HashSet<Player> jumping;
	
	public LeapSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		forwardVelocity = getConfigInt("forward-velocity", 40) / 10D;
		upwardVelocity = getConfigInt("upward-velocity", 15) / 10D;
		cancelDamage = getConfigBoolean("cancel-damage", true);
		clientOnly = getConfigBoolean("client-only", true);
		leapFacing = getConfigBoolean("leap-facing", true);
		if (leapFacing) {
			leapAtCursor = getConfigBoolean("leap-at-cursor", false);
			
			if (leapAtCursor) {
				velocity = getConfigInt("velocity", 1) / 10D;
			}
		}
		
		if (cancelDamage) {
			jumping = new HashSet<Player>();
		}
	}

	@Override
	public PostCastAction castSpell(final Player player, SpellCastState state, final float power, String[] args) {
		if (state == SpellCastState.NORMAL) {
			Vector v = null;
			final Location old = player.getLocation();
			
			if (leapFacing) {
				v = player.getLocation().getDirection();
			}
			
			if (leapFacing) {
				if (leapAtCursor) {
					v.normalize().multiply(velocity);
				} else {
					v.setY(0).normalize().multiply(forwardVelocity*power).setY(upwardVelocity*power);
				}
				leap(player, v);
			} else if (!leapFacing) {
				MagicSpells.scheduleDelayedTask(new Runnable() {
					public void run() {
						Location current = player.getLocation();
						Vector newV = new Vector(current.getX() - old.getX(), current.getY() - old.getY(), current.getZ() - old.getZ());
						newV.setY(0).normalize().multiply(forwardVelocity*power).setY(upwardVelocity*power);
						leap(player, newV);
					}
				}, 1);
			}
				
			if (cancelDamage) {
				jumping.add(player);
			}
			
			playSpellEffects(EffectPosition.CASTER, player);
		}
		
		return PostCastAction.HANDLE_NORMALLY;
	}
	
	private void leap(Player player, Vector v) {
		if (clientOnly) {
			MagicSpells.getVolatileCodeHandler().setClientVelocity(player, v);
		} else {
			player.setVelocity(v);
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		if (cancelDamage && event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player && jumping.contains((Player)event.getEntity())) {
			event.setCancelled(true);
			jumping.remove((Player)event.getEntity());
			playSpellEffects(EffectPosition.TARGET, event.getEntity().getLocation());
		}
	}
}

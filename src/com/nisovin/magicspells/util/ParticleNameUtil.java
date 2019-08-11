package com.nisovin.magicspells.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Particle;

public class ParticleNameUtil {
	private ParticleNameUtil() {}

	static Map<String, Particle> particleMap = new HashMap<String, Particle>();
	
	public static Particle getParticleFromName(String name) {
		for (Particle p : Particle.values()) {
			if (p.name().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return particleMap.get(name.toLowerCase());
	}
	
	static {
		particleMap.put("explode", Particle.EXPLOSION_NORMAL);
		particleMap.put("largeexplode", Particle.EXPLOSION_LARGE);
		particleMap.put("hugeexplosion", Particle.EXPLOSION_HUGE);
		particleMap.put("fireworksspark", Particle.FIREWORKS_SPARK);
		particleMap.put("bubble", Particle.WATER_BUBBLE);
		particleMap.put("splash", Particle.WATER_SPLASH);
		particleMap.put("wake", Particle.WATER_WAKE);
		particleMap.put("suspended", Particle.SUSPENDED);
		particleMap.put("depthsuspend", Particle.SUSPENDED_DEPTH);
		particleMap.put("crit", Particle.CRIT);
		particleMap.put("magiccrit", Particle.CRIT_MAGIC);
		particleMap.put("smoke", Particle.SMOKE_NORMAL);
		particleMap.put("largesmoke", Particle.SMOKE_LARGE);
		particleMap.put("spell", Particle.SPELL);
		particleMap.put("instantspell", Particle.SPELL_INSTANT);
		particleMap.put("mobspell", Particle.SPELL_MOB);
		particleMap.put("mobspellambient", Particle.SPELL_MOB_AMBIENT);
		particleMap.put("witchmagic", Particle.SPELL_WITCH);
		particleMap.put("dripwater", Particle.DRIP_WATER);
		particleMap.put("driplava", Particle.DRIP_LAVA);
		particleMap.put("angryvillager", Particle.VILLAGER_ANGRY);
		particleMap.put("happyvillager", Particle.VILLAGER_HAPPY);
		particleMap.put("townaura", Particle.TOWN_AURA);
		particleMap.put("note", Particle.NOTE);
		particleMap.put("portal", Particle.PORTAL);
		particleMap.put("enchantmenttable", Particle.ENCHANTMENT_TABLE);
		particleMap.put("flame", Particle.FLAME);
		particleMap.put("lava", Particle.LAVA);
		particleMap.put("reddust", Particle.REDSTONE);
		particleMap.put("snowballpoof", Particle.SNOWBALL);
		particleMap.put("slime", Particle.SLIME);
		particleMap.put("heart", Particle.HEART);
		particleMap.put("barrier", Particle.BARRIER);
		particleMap.put("cloud", Particle.CLOUD);
		particleMap.put("snowshovel", Particle.SNOW_SHOVEL);
		particleMap.put("droplet", Particle.WATER_DROP);
		particleMap.put("mobappearance", Particle.MOB_APPEARANCE);
		particleMap.put("endrod", Particle.END_ROD);
		particleMap.put("dragonbreath", Particle.DRAGON_BREATH);
		particleMap.put("damageindicator", Particle.DAMAGE_INDICATOR);
		particleMap.put("sweepattack", Particle.SWEEP_ATTACK);
		// TODO: add other particles
	}
	
	static Map<String, String> minecraftParticleMap = new HashMap<>();
	
	static {
		minecraftParticleMap.put("angryVillager", "angry_villager");
		minecraftParticleMap.put("villager_angry", "angry_villager");
		minecraftParticleMap.put("villagerangry", "angry_villager");
		minecraftParticleMap.put("block_crack", "block");
		minecraftParticleMap.put("block_dust", "block");
		minecraftParticleMap.put("blockcrack", "block");
		minecraftParticleMap.put("blockdust", "block");
		minecraftParticleMap.put("damageIndicator", "damage_indicator");
		minecraftParticleMap.put("drip_lava", "dripping_lava");
		minecraftParticleMap.put("dripLava", "dripping_lava");
		minecraftParticleMap.put("drip_water", "dripping_water");
		minecraftParticleMap.put("dripWater", "dripping_water");
		minecraftParticleMap.put("dragonbreath", "dragon_breath");
		minecraftParticleMap.put("droplet", "rain");
		minecraftParticleMap.put("water_drop", "rain");
		minecraftParticleMap.put("droplet", "rain");
		minecraftParticleMap.put("enchantment_table", "enchant");
		minecraftParticleMap.put("enchantementtable", "enchant");
		minecraftParticleMap.put("endRod", "end_rod");
		minecraftParticleMap.put("explode", "poof");
		minecraftParticleMap.put("snowshovel", "poof");
		minecraftParticleMap.put("explosion_normal", "poof");
		minecraftParticleMap.put("fallingdust", "falling_dust");
		minecraftParticleMap.put("fireworksSpark", "firework");
		minecraftParticleMap.put("fireworks_spark", "firework");
		minecraftParticleMap.put("happyVillager", "happy_villager");
		minecraftParticleMap.put("villager_happy", "happy_villager");
		minecraftParticleMap.put("hugeexplosion", "explosion_emitter");
		minecraftParticleMap.put("explosion_huge", "explosion_emitter");
		minecraftParticleMap.put("icon_crack", "item");
		minecraftParticleMap.put("iconcrack", "item");
		minecraftParticleMap.put("instantSpell", "instant_effect");
		minecraftParticleMap.put("spell_instant", "instant_effect");
		minecraftParticleMap.put("largeexplode", "explosion");
		minecraftParticleMap.put("largeexplosion", "explosion");
		minecraftParticleMap.put("explosion_large", "explosion");
		minecraftParticleMap.put("largesmoke", "large_smoke");
		minecraftParticleMap.put("smoke_large", "large_smoke");
		minecraftParticleMap.put("crit_magic", "enchanted_hit");
		minecraftParticleMap.put("magiccrit", "enchanted_hit");
		minecraftParticleMap.put("mobappearance", "elder_guardian");
		minecraftParticleMap.put("mob_appearance", "elder_guardian");
		minecraftParticleMap.put("mobSpell", "entity_effect");
		minecraftParticleMap.put("spell_mob", "entity_effect");
		minecraftParticleMap.put("mobSpellAmbient", "ambient_entity_effect");
		minecraftParticleMap.put("spell_mob_ambient", "ambient_entity_effect");
		minecraftParticleMap.put("reddust", "dust");
		minecraftParticleMap.put("redstone", "dust");
		minecraftParticleMap.put("slime", "item_slime");
		minecraftParticleMap.put("snowballpoof", "item_snowball");
		minecraftParticleMap.put("snowball", "item_snowball");
		minecraftParticleMap.put("spell", "effect");
		minecraftParticleMap.put("suspend", "underwater");
		minecraftParticleMap.put("suspended", "underwater");
		minecraftParticleMap.put("sweepattack", "sweep_attack");
		minecraftParticleMap.put("totem", "totem_of_undying");
		minecraftParticleMap.put("townaura", "mycelium");
		minecraftParticleMap.put("town_aura", "mycelium");
		minecraftParticleMap.put("wake", "fishing");
		minecraftParticleMap.put("water_wake", "fishing");
		minecraftParticleMap.put("witchMagic", "witch");
		minecraftParticleMap.put("spell_witch", "witch");
		minecraftParticleMap.put("water_splash", "splash");
		minecraftParticleMap.put("water_bubble", "bubble");
	}
	
	/**
	 * Attempts to translate the name into the Minecraft
	 * namespace equivalent. Returns null if unnecessary.
	 * @param raw
	 * @return the Minecraft namespace equivalent or null if unnecessary
	 */
	public static String toMinecraftParticle(String raw) {
		return minecraftParticleMap.get(raw.toLowerCase());
	}
	
}

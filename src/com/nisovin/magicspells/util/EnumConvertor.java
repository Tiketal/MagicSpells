package com.nisovin.magicspells.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Particle;
import org.bukkit.potion.PotionEffectType;

public class EnumConvertor {
	private EnumConvertor() {}

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
	
	public static String toMinecraftParticle(String raw) {
		String result = raw.toLowerCase();
		return result.replaceAll("angryVillager|villager_angry|villagerangry", "angry_villager")
				.replaceAll("block_crack|block_dust|blockcrack|blockdust", "block")
				.replaceAll("damageIndicator", "damage_indicator")
				.replaceAll("drip_lava|dripLava", "dripping_lava")
				.replaceAll("drip_water|dripWater", "dripping_water")
				.replaceAll("dragonbreath", "dragon_breath")
				.replaceAll("droplet|water_drop|waterdrop", "rain")
				.replaceAll("enchantment_table|enchantementtable", "enchant")
				.replaceAll("endRod", "end_rod")
				.replaceAll("explode|snowshovel|explosion_normal", "poof")
				.replaceAll("fallingdust", "falling_dust")
				.replaceAll("fireworksSpark|fireworks_spark", "firework")
				.replaceAll("happyVillager|villager_happy", "happy_villager")
				.replaceAll("hugeexplosion|explosion_huge", "explosion_emitter")
				.replaceAll("icon_crack|iconcrack", "item")
				.replaceAll("instantSpell|spell_instant", "instant_effect")
				.replaceAll("largeexplode|largeexplosion|explosion_large", "explosion")
				.replaceAll("largesmoke|smoke_large", "large_smoke")
				.replaceAll("crit_magic|magiccrit", "enchanted_hit")
				.replaceAll("mobappearance|mob_appearance", "elder_guardian")
				.replaceAll("mobSpell|spell_mob", "entity_effect")
				.replaceAll("mobSpellAmbient|spell_mob_ambient", "ambient_entity_effect")
				.replaceAll("reddust|redstone", "dust")
				.replaceAll("slime", "item_slime")
				.replaceAll("snowballpoof|snowball", "item_snowball")
				.replaceAll("spell", "effect")
				.replaceAll("suspend|suspended", "underwater")
				.replaceAll("sweepattack", "sweep_attack")
				.replaceAll("totem", "totem_of_undying")
				.replaceAll("townaura|town_aura", "mycelium")
				.replaceAll("wake|water_wake", "fishing")
				.replaceAll("witchMagic|spell_witch", "witch")
				.replaceAll("water_splash", "splash")
				.replaceAll("water_bubble", "bubble");
	}
	
	static Map<String, PotionEffectType> potionMap = new HashMap<>();
	
	static {
		//TODO add potion effects with same names
		potionMap.put("slowness", PotionEffectType.SLOW);
		potionMap.put("haste", PotionEffectType.FAST_DIGGING);
		potionMap.put("mining_fatigue", PotionEffectType.SLOW_DIGGING);
		potionMap.put("miningfatigue", PotionEffectType.SLOW_DIGGING);
		potionMap.put("strength", PotionEffectType.INCREASE_DAMAGE);
		potionMap.put("instant_health", PotionEffectType.HEAL);
		potionMap.put("instanthealth", PotionEffectType.HEAL);
		potionMap.put("instant_damage", PotionEffectType.HARM);
		potionMap.put("instantdamage", PotionEffectType.HARM);
		potionMap.put("jump_boost", PotionEffectType.JUMP);
		potionMap.put("jumpboost", PotionEffectType.JUMP);
		potionMap.put("nausea", PotionEffectType.CONFUSION);
		potionMap.put("fireresistance", PotionEffectType.FIRE_RESISTANCE);
		potionMap.put("waterbreathing", PotionEffectType.WATER_BREATHING);
		potionMap.put("nightvision", PotionEffectType.NIGHT_VISION);
		potionMap.put("hunger", PotionEffectType.HUNGER);
		potionMap.put("healthboost", PotionEffectType.HEALTH_BOOST);
		potionMap.put("bad_luck", PotionEffectType.UNLUCK);
		potionMap.put("badluck", PotionEffectType.UNLUCK);
		potionMap.put("slowfalling", PotionEffectType.SLOW_FALLING);
		potionMap.put("conduitpower", PotionEffectType.CONDUIT_POWER);
		potionMap.put("dolphinsgrace", PotionEffectType.DOLPHINS_GRACE);
		potionMap.put("badomen", PotionEffectType.BAD_OMEN);
	}
	
	public static PotionEffectType getPotionEffectFromName(String name) {
		return potionMap.get(name.toLowerCase());
	}
	
}

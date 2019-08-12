package com.nisovin.magicspells.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.Spellbook;
import com.nisovin.magicspells.materials.MagicBlockMaterial;
import com.nisovin.magicspells.materials.MagicMaterial;

public class Util {

	public static Map<String, ItemStack> predefinedItems = new HashMap<String, ItemStack>();
	
	private static Random random = new Random();
	public static int getRandomInt(int bound) {
		return random.nextInt(bound);
	}
	
	public static ItemStack getItemStackFromString(String string) {
		try {
			if (predefinedItems.containsKey(string)) return predefinedItems.get(string).clone();

			ItemStack item;
			String s = string;
			String name = null;
			String[] lore = null;
			HashMap<Enchantment, Integer> enchants = null;
			int color = -1;
			if (s.contains("|")) {
				String[] temp = s.split("\\|");
				s = temp[0];
				if (temp.length == 1) {
					name = "";
				} else {
					name = ChatColor.translateAlternateColorCodes('&', temp[1].replace("__", " "));
					if (temp.length > 2) {
						lore = Arrays.copyOfRange(temp, 2, temp.length);
						for (int i = 0; i < lore.length; i++) {
							lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i].replace("__", " "));
						}
					}
				}
			}
			if (s.contains(";")) {
				String[] temp = s.split(";", 2);
				s = temp[0];
				enchants = new HashMap<Enchantment, Integer>();
				if (temp[1].length() > 0) {
					String[] split = temp[1].split("\\+");
					for (int i = 0; i < split.length; i++) {
						String[] enchantData = split[i].split("-");
						Enchantment ench = Util.getEnchantmentType(enchantData[0]);
						if (ench != null && enchantData[1].matches("[0-9]+")) {
							enchants.put(ench, Integer.parseInt(enchantData[1]));
						}
					}
				}
			}
			if (s.contains("#")) {
				String[] temp = s.split("#");
				s = temp[0];
				if (temp[1].matches("[0-9A-Fa-f]+")) {
					color = Integer.parseInt(temp[1], 16);
				}
			}
			MagicMaterial mat = MagicSpells.getItemNameResolver().resolveItem(s);
			if (mat != null) {
				item = mat.toItemStack();
			} else {
				return null;
			}
			
			if (name != null || lore != null || color >= 0) {
				try {
					ItemMeta meta = item.getItemMeta();
					if (name != null) {
						meta.setDisplayName(name);
					}
					if (lore != null) {
						meta.setLore(Arrays.asList(lore));
					}
					if (color >= 0 && meta instanceof LeatherArmorMeta) {
						((LeatherArmorMeta)meta).setColor(Color.fromRGB(color));
					}
					item.setItemMeta(meta);
				} catch (Exception e) {
					MagicSpells.error("Failed to process item meta for item: " + s);
				}
			}
			if (enchants != null) {
				if (enchants.size() > 0) {
					item.addUnsafeEnchantments(enchants);
				} else {
					item = MagicSpells.getVolatileCodeHandler().addFakeEnchantment(item);
				}
			}
			return item;
		} catch (Exception e) {
			MagicSpells.handleException(e);
			return null;
		}
	}
	
	public static ItemStack getItemStackFromConfig(ConfigurationSection config) {
		try {
			if (!config.contains("type")) return null;
			
			// basic item
			MagicMaterial material = MagicSpells.getItemNameResolver().resolveItem(config.getString("type"));
			if (material == null) return null;
			ItemStack item = material.toItemStack();
			ItemMeta meta = item.getItemMeta();
			
			// name and lore
			if (config.contains("name") && config.isString("name")) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("name")));
			}
			if (config.contains("lore")) {
				if (config.isList("lore")) {
					List<String> lore = config.getStringList("lore");
					for (int i = 0; i < lore.size(); i++) {
						lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
					}
					meta.setLore(lore);
				} else if (config.isString("lore")) {
					List<String> lore = new ArrayList<String>();
					lore.add(ChatColor.translateAlternateColorCodes('&', config.getString("lore")));
					meta.setLore(lore);
				}
			}
			
			// enchants
			boolean emptyEnchants = false;
			if (config.contains("enchants") && config.isList("enchants")) {
				List<String> enchants = config.getStringList("enchants");
				for (String enchant : enchants) {
					String[] data = enchant.split(" ");
					Enchantment e = Util.getEnchantmentType(data[0]);
					if (e != null) {
						int level = 0;
						if (data.length > 1) {
							try {
								level = Integer.parseInt(data[1]);
							} catch (NumberFormatException ex) {						
							}
						}
						if (meta instanceof EnchantmentStorageMeta) {
							((EnchantmentStorageMeta)meta).addStoredEnchant(e, level, true);
						} else {
							meta.addEnchant(e, level, true);
						}
					}
				}
				if (enchants.size() == 0) {
					emptyEnchants = true;
				}
			}
			
			// armor and potion color; uses hex values
			if (config.contains("color") && config.isString("color")
					&& (meta instanceof LeatherArmorMeta
						|| meta instanceof PotionMeta)) {
				if (meta instanceof LeatherArmorMeta) {
					try {
						int color = Integer.parseInt(config.getString("color").replace("#", ""), 16);
						((LeatherArmorMeta)meta).setColor(Color.fromRGB(color));
					} catch (NumberFormatException e) {}
				} else {
					try {
						int color = Integer.parseInt(config.getString("color").replace("#", ""), 16);
						((PotionMeta)meta).setColor(Color.fromRGB(color));
					} catch (NumberFormatException e) {}
				}
			}
			
			// potion effects
			if (config.contains("potioneffects") && config.isList("potioneffects") && meta instanceof PotionMeta) {
				((PotionMeta)meta).clearCustomEffects();
				List<String> potionEffects = config.getStringList("potioneffects");
				for (String potionEffect : potionEffects) {
					String[] data = potionEffect.split(" ");
					PotionEffectType t = Util.getPotionEffectType(data[0]);
					if (t != null) {
						int level = 0;
						if (data.length > 1) {
							try {
								level = Integer.parseInt(data[1]);
							} catch (NumberFormatException ex) {						
							}
						}
						int duration = 600;
						if (data.length > 2) {
							try {
								duration = Integer.parseInt(data[2]);
							} catch (NumberFormatException ex) {						
							}
						}
						boolean ambient = false;
						if (data.length > 3 && (data[3].equalsIgnoreCase("true") || data[3].equalsIgnoreCase("yes") || data[3].equalsIgnoreCase("ambient"))) {
							ambient = true;
						}
						((PotionMeta)meta).addCustomEffect(new PotionEffect(t, duration, level, ambient), true);
					}
				}
			}
			
			// skull owner
			if (config.contains("skullowner") && config.isString("skullowner") && meta instanceof SkullMeta) {
				((SkullMeta)meta).setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(config.getString("skullowner"))));
			}
			
			// flower pot
			/*if (config.contains("flower") && item.getType() == Material.FLOWER_POT && meta instanceof BlockStateMeta) {
				MagicMaterial flower = MagicSpells.getItemNameResolver().resolveBlock(config.getString("flower"));
				BlockState state = ((BlockStateMeta)meta).getBlockState();
				MaterialData data = state.getData();
				if (data instanceof FlowerPot) {
					((FlowerPot)data).setContents(new MaterialData(flower.getMaterial()));
				}
				state.setData(data);
				((BlockStateMeta)meta).setBlockState(state);
			}*/
			
			// repair cost
			if (config.contains("repaircost") && config.isInt("repaircost") && meta instanceof Repairable) {
				((Repairable)meta).setRepairCost(config.getInt("repaircost"));
			}
			
			// written book
			if (meta instanceof BookMeta) {
				if (config.contains("title") && config.isString("title")) {
					((BookMeta)meta).setTitle(ChatColor.translateAlternateColorCodes('&', config.getString("title")));
				}
				if (config.contains("author") && config.isString("author")) {
					((BookMeta)meta).setAuthor(ChatColor.translateAlternateColorCodes('&', config.getString("author")));
				}
				if (config.contains("pages") && config.isList("pages")) {
					List<String> pages = config.getStringList("pages");
					for (int i = 0; i < pages.size(); i++) {
						pages.set(i, ChatColor.translateAlternateColorCodes('&', pages.get(i)));
					}
					((BookMeta)meta).setPages(pages);
				}
			}
			
			// banner
			if (meta instanceof BannerMeta) {
				if (config.contains("patterns") && config.isList("patterns")) {
					List<String> patterns = config.getStringList("patterns");
					for (String patternData : patterns) {
						if (patternData.contains(" ")) {
							String[] split = patternData.split(" ");
							DyeColor color = null;
							for (DyeColor c : DyeColor.values()) {
								if (c != null && c.name().replace("_", "").toLowerCase().equals(split[0].toLowerCase())) {
									color = c;
									break;
								}
							}
							PatternType pattern = PatternType.getByIdentifier(split[1]);
							if (pattern == null) {
								for (PatternType p : PatternType.values()) {
									if (p != null && p.name().equalsIgnoreCase(split[1])) {
										pattern = p;
										break;
									}
								}
							}
							if (color != null && pattern != null) {
								((BannerMeta)meta).addPattern(new Pattern(color, pattern));
							}
						}
					}
				}
			}
			
			// custom model data
			if (config.contains("custom-model")) {
				meta.setCustomModelData(config.getInt("custom-model"));
			}
			
			// set meta
			item.setItemMeta(meta);
			
			// hide tooltip
			if (config.getBoolean("hide-tooltip", MagicSpells.hidePredefinedItemTooltips())) {
				item = MagicSpells.getVolatileCodeHandler().hideTooltipCrap(item);
			}
			
			// unbreakable
			if (config.getBoolean("unbreakable", false)) {
				item.getItemMeta().setUnbreakable(true);
				item = MagicSpells.getVolatileCodeHandler().setUnbreakable(item);
			}
			
			// empty enchant
			if (emptyEnchants) {
				item = MagicSpells.getVolatileCodeHandler().addFakeEnchantment(item);
			}
			
			// attributes
			if (config.contains("attributes")) {
				Set<String> attrs = config.getConfigurationSection("attributes").getKeys(false);
				String[] attrNames = new String[attrs.size()];
				String[] attrTypes = new String[attrs.size()];
				double[] attrAmounts = new double[attrs.size()];
				int[] attrOperations = new int[attrs.size()];
				String[] attrSlots = new String[attrs.size()];
				int i = 0;
				for (String attrName : attrs) {
					String[] attrData = config.getString("attributes." + attrName).split(" ");
					String attrType = attrData[0];
					double attrAmt = 1;
					try {
						attrAmt = Double.parseDouble(attrData[1]);
					} catch (NumberFormatException e) {}
					int attrOp = 0; // add number
					if (attrData.length > 2) {
						if (attrData[2].toLowerCase().startsWith("mult")) {
							attrOp = 1; // multiply percent
						} else if (attrData[2].toLowerCase().contains("add") && attrData[2].toLowerCase().contains("perc")) {
							attrOp = 2; // add percent
						}
					}
					String attrSlot = null;
					if (attrData.length > 3) {
						attrSlot = attrData[3];
					}
					if (attrType != null) {
						attrNames[i] = attrName;
						attrTypes[i] = attrType;
						attrAmounts[i] = attrAmt;
						attrOperations[i] = attrOp;
						attrSlots[i] = attrSlot;
					}
					i++;
				}
				item = MagicSpells.getVolatileCodeHandler().addAttributes(item, attrNames, attrTypes, attrAmounts, attrOperations, attrSlots);
			}
			
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setLoreData(ItemStack item, String data) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if (meta.hasLore()) {
			lore = meta.getLore();
			if (lore.size() > 0) {
				for (int i = 0; i < lore.size(); i++) {
					String s = ChatColor.stripColor(lore.get(i));
					if (s.startsWith("MS$:")) {
						lore.remove(i);
						break;
					}
				}
			}
		} else {
			lore = new ArrayList<String>();
		}
		lore.add(ChatColor.BLACK.toString() + ChatColor.MAGIC.toString() + "MS$:" + data);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static String getLoreData(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null && meta.hasLore()) {
			List<String> lore = meta.getLore();
			if (lore.size() > 0) {
				for (int i = 0; i < lore.size(); i++) {
					String s = ChatColor.stripColor(lore.get(lore.size() - 1));
					if (s.startsWith("MS$:")) {
						return s.substring(4);
					}
				}
			}
		}
		return null;
	}
	
	public static void removeLoreData(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if (meta.hasLore()) {
			lore = meta.getLore();
			if (lore.size() > 0) {
				boolean removed = false;
				for (int i = 0; i < lore.size(); i++) {
					String s = ChatColor.stripColor(lore.get(i));
					if (s.startsWith("MS$:")) {
						lore.remove(i);
						removed = true;
						break;
					}
				}
				if (removed) {
					if (lore.size() > 0) {
						meta.setLore(lore);
					} else {
						meta.setLore(null);
					}
					item.setItemMeta(meta);
				}
			}
		}
	}

	static Map<String, EntityType> entityTypeMap = new HashMap<String, EntityType>();
	static {
		for (EntityType type : EntityType.values()) {
			if (type != null && type.name() != null) {
				entityTypeMap.put(type.name().toLowerCase(), type);
				entityTypeMap.put(type.name().toLowerCase().replace("_", ""), type);
			}
		}
		entityTypeMap.put("zombiepig", EntityType.PIG_ZOMBIE);
		entityTypeMap.put("mooshroom", EntityType.MUSHROOM_COW);
		entityTypeMap.put("golem", EntityType.IRON_GOLEM);
		entityTypeMap.put("snowgolem", EntityType.SNOWMAN);
		entityTypeMap.put("dragon", EntityType.ENDER_DRAGON);
		Map<String, EntityType> toAdd = new HashMap<String, EntityType>();
		for (String s : entityTypeMap.keySet()) {
			toAdd.put(s + "s", entityTypeMap.get(s));
		}
		entityTypeMap.putAll(toAdd);
		entityTypeMap.put("endermen", EntityType.ENDERMAN);
		entityTypeMap.put("wolves", EntityType.WOLF);
	}
	
	public static EntityType getEntityType(String type) {
		if (type.equalsIgnoreCase("player")) return EntityType.PLAYER;
		return entityTypeMap.get(type.toLowerCase());
	}
	
	static Map<String, PotionEffectType> potionMap = new HashMap<>();
	static {
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
		potionMap.put("resistance", PotionEffectType.DAMAGE_RESISTANCE);
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
	
	public static PotionEffectType getPotionEffectType(String type) {
		if (type == null) return null;
		
		// get potion
		PotionEffectType effect = PotionEffectType.getByName(type.toUpperCase());
		if (effect == null) {
			effect = potionMap.get(type.toLowerCase());// minecraft/special potion effect names
		}
		
		// invalid potion effect
		if (effect == null) {
			MagicSpells.error("Invalid potion effect: " + type);
		}
		return effect;
	}
	
	static Map<String, Enchantment> enchantmentMap = new HashMap<>();
	static {
		enchantmentMap.put("ARROW_DAMAGE", Enchantment.ARROW_DAMAGE);
		enchantmentMap.put("ARROW_FIRE", Enchantment.ARROW_FIRE);
		enchantmentMap.put("ARROW_INFINITE", Enchantment.ARROW_INFINITE);
		enchantmentMap.put("ARROW_KNOCKBACK", Enchantment.ARROW_KNOCKBACK);
		enchantmentMap.put("BINDING_CURSE", Enchantment.BINDING_CURSE);
		enchantmentMap.put("CHANNELING", Enchantment.CHANNELING);
		enchantmentMap.put("DAMAGE_ALL", Enchantment.DAMAGE_ALL);
		enchantmentMap.put("DAMAGE_ARTHROPODS", Enchantment.DAMAGE_ARTHROPODS);
		enchantmentMap.put("DAMAGE_UNDEAD", Enchantment.DAMAGE_UNDEAD);
		enchantmentMap.put("DEPTH_STRIDER", Enchantment.DEPTH_STRIDER);
		enchantmentMap.put("DIG_SPEED", Enchantment.DIG_SPEED);
		enchantmentMap.put("DURABILITY", Enchantment.DURABILITY);
		enchantmentMap.put("FIRE_ASPECT", Enchantment.FIRE_ASPECT);
		enchantmentMap.put("FROST_WALKER", Enchantment.FROST_WALKER);
		enchantmentMap.put("IMPALING", Enchantment.IMPALING);
		enchantmentMap.put("KNOCKBACK", Enchantment.KNOCKBACK);
		enchantmentMap.put("LOOT_BONUS_BLOCKS", Enchantment.LOOT_BONUS_BLOCKS);
		enchantmentMap.put("LOOT_BONUS_MOBS", Enchantment.LOOT_BONUS_MOBS);
		enchantmentMap.put("LOYALTY", Enchantment.LOYALTY);
		enchantmentMap.put("LUCK", Enchantment.LUCK);
		enchantmentMap.put("LURE", Enchantment.LURE);
		enchantmentMap.put("MENDING", Enchantment.MENDING);
		enchantmentMap.put("MULTISHOT", Enchantment.MULTISHOT);
		enchantmentMap.put("OXYGEN", Enchantment.OXYGEN);
		enchantmentMap.put("PIERCING", Enchantment.PIERCING);
		enchantmentMap.put("PROTECTION_ENVIRONMENTAL", Enchantment.PROTECTION_ENVIRONMENTAL);
		enchantmentMap.put("PROTECTION_EXPLOSIONS", Enchantment.PROTECTION_EXPLOSIONS);
		enchantmentMap.put("PROTECTION_FALL", Enchantment.PROTECTION_FALL);
		enchantmentMap.put("PROTECTION_FIRE", Enchantment.PROTECTION_FIRE);
		enchantmentMap.put("PROTECTION_PROJECTILE", Enchantment.PROTECTION_PROJECTILE);
		enchantmentMap.put("QUICK_CHARGE", Enchantment.QUICK_CHARGE);
		enchantmentMap.put("RIPTIDE", Enchantment.RIPTIDE);
		enchantmentMap.put("SILK_TOUCH", Enchantment.SILK_TOUCH);
		enchantmentMap.put("SWEEPING_EDGE", Enchantment.SWEEPING_EDGE);
		enchantmentMap.put("THORNS", Enchantment.THORNS);
		enchantmentMap.put("VANISHING_CURSE", Enchantment.VANISHING_CURSE);
		enchantmentMap.put("WATER_WORKER", Enchantment.WATER_WORKER);
	}
	
	public static Enchantment getEnchantmentType(String type) {
		if (type == null) return null;
		
		// is in the Enchantment class
		Enchantment enchant = enchantmentMap.get(type.toUpperCase());
		
		if (enchant == null) {
			// try to find in Enchantments as minecraft key
			try {
				enchant = Enchantment.getByKey(NamespacedKey.minecraft(type.toLowerCase()));
			} catch (Exception e) {}
		}
		
		// invalid enchantment
		if (enchant == null) {
			MagicSpells.error("Invalid enchantment: " + type);
		}
		
		return enchant;
	}
	
	public static void sendFakeBlockChange(Player player, Block block, MagicMaterial mat) {
		player.sendBlockChange(block.getLocation(), ((MagicBlockMaterial)mat).getBlockData());
	}
	
	public static void restoreFakeBlockChange(Player player, Block block) {
		player.sendBlockChange(block.getLocation(), block.getBlockData());
	}
	
	public static void setFacing(Player player, Vector vector) {
		Location loc = player.getLocation();
		setLocationFacingFromVector(loc, vector);
		player.teleport(loc);
	}
	
	public static void setLocationFacingFromVector(Location location, Vector vector) {
		double yaw = getYawOfVector(vector);
		double pitch = Math.toDegrees(-Math.asin(vector.getY()));				
		location.setYaw((float)yaw);
		location.setPitch((float)pitch);
	}
	
	public static double getYawOfVector(Vector vector) {
		return Math.toDegrees(Math.atan2(-vector.getX(), vector.getZ()));
	}
	
	public static boolean arrayContains(int[] array, int value) {
		for (int i : array) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

	public static boolean arrayContains(String[] array, String value) {
		for (String i : array) {
			if (i.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean arrayContains(Object[] array, Object value) {
		for (Object i : array) {
			if (i != null && i.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static String arrayJoin(String[] array, char with) {
		if (array == null || array.length == 0) {
			return "";
		}
		int len = array.length;
		StringBuilder sb = new StringBuilder(16 + len * 8);
		sb.append(array[0]);
		for (int i = 1; i < len; i++) {
			sb.append(with);
			sb.append(array[i]);
		}
		return sb.toString();
	}
	
	public static String listJoin(List<String> list) {
		if (list == null || list.size() == 0) {
			return "";
		}
		int len = list.size();
		StringBuilder sb = new StringBuilder(len * 12);
		sb.append(list.get(0));
		for (int i = 1; i < len; i++) {
			sb.append(' ');
			sb.append(list.get(i));
		}
		return sb.toString();
	}
	
	public static String[] splitParams(String string, int max) {
		String[] words = string.trim().split(" ");
		if (words.length <= 1) {
			return words;
		}
		ArrayList<String> list = new ArrayList<String>();		
		char quote = ' ';
		String building = "";
		
		for (String word : words) {
			if (word.length() == 0) continue;
			if (max > 0 && list.size() == max - 1) {
				if (!building.isEmpty()) building += " ";
				building += word;
			} else if (quote == ' ') {
				if (word.length() == 1 || (word.charAt(0) != '"' && word.charAt(0) != '\'')) {
					list.add(word);
				} else {
					quote = word.charAt(0);
					if (quote == word.charAt(word.length() - 1)) {
						quote = ' ';
						list.add(word.substring(1, word.length() - 1));
					} else {
						building = word.substring(1);
					}
				}
			} else {
				if (word.charAt(word.length() - 1) == quote) {
					list.add(building + " " + word.substring(0, word.length() - 1));
					building = "";
					quote = ' ';
				} else {
					building += " " + word;
				}
			}
		}
		if (!building.isEmpty()) {
			list.add(building);
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String[] splitParams(String string) {
		return splitParams(string, 0);
	}
	
	public static String[] splitParams(String[] split, int max) {
		return splitParams(arrayJoin(split, ' '), max);
	}
	
	public static String[] splitParams(String[] split) {
		return splitParams(arrayJoin(split, ' '), 0);
	}
	
	public static List<String> tabCompleteSpellName(CommandSender sender, String partial) {
		List<String> matches = new ArrayList<String>();
		if (sender instanceof Player) {
			Spellbook spellbook = MagicSpells.getSpellbook((Player)sender);
			for (Spell spell : spellbook.getSpells()) {
				if (spellbook.canTeach(spell)) {
					if (spell.getName().toLowerCase().startsWith(partial)) {
						matches.add(spell.getName());
					} else {
						String[] aliases = spell.getAliases();
						if (aliases != null && aliases.length > 0) {
							for (String alias : aliases) {
								if (alias.toLowerCase().startsWith(partial)) {
									matches.add(alias);
								}
							}
						}
					}
				}
			}
		} else if (sender.isOp()) {
			for (Spell spell : MagicSpells.spells()) {
				if (spell.getName().toLowerCase().startsWith(partial)) {
					matches.add(spell.getName());
				} else {
					String[] aliases = spell.getAliases();
					if (aliases != null && aliases.length > 0) {
						for (String alias : aliases) {
							if (alias.toLowerCase().startsWith(partial)) {
								matches.add(alias);
							}
						}
					}
				}
			}
		}
		if (matches.size() > 0) {
			return matches;
		}
		return null;
	}
	
	public static boolean removeFromInventory(Inventory inventory, ItemStack item) {
		int amt = item.getAmount();
		ItemStack[] items = inventory.getContents();
		for (int i = 0; i < 36; i++) {
			if (items[i] != null && item.isSimilar(items[i])) {
				if (items[i].getAmount() > amt) {
					items[i].setAmount(items[i].getAmount() - amt);
					amt = 0;
					break;
				} else if (items[i].getAmount() == amt) {
					items[i] = null;
					amt = 0;
					break;
				} else {
					amt -= items[i].getAmount();
					items[i] = null;
				}
			}
		}
		if (amt == 0) {
			inventory.setContents(items);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean addToInventory(Inventory inventory, ItemStack item, boolean stackExisting, boolean ignoreMaxStack) {
		int amt = item.getAmount();
		ItemStack[] items = inventory.getContents();
		if (stackExisting) {
			for (int i = 0; i < 36; i++) {
				if (items[i] != null && item.isSimilar(items[i])) {
					if (items[i].getAmount() + amt <= items[i].getMaxStackSize()) {
						items[i].setAmount(items[i].getAmount() + amt);
						amt = 0;
						break;
					} else {
						int diff = items[i].getMaxStackSize() - items[i].getAmount();
						items[i].setAmount(items[i].getMaxStackSize());
						amt -= diff;
					}
				}
			}
		}
		if (amt > 0) {
			for (int i = 0; i < 36; i++) {
				if (items[i] == null) {
					if (amt > item.getMaxStackSize() && !ignoreMaxStack) {
						items[i] = item.clone();
						items[i].setAmount(item.getMaxStackSize());
						amt -= item.getMaxStackSize();
					} else {
						items[i] = item.clone();
						items[i].setAmount(amt);
						amt = 0;
						break;
					}
				}
			}
		}
		if (amt == 0) {
			inventory.setContents(items);
			return true;
		} else {
			return false;
		}
	}
	
	public static void rotateVector(Vector v, float degrees) {
		double rad = Math.toRadians(degrees);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		double x = (v.getX() * cos) - (v.getZ() * sin);
		double z = (v.getX() * sin) + (v.getZ() * cos);
		v.setX(x);
		v.setZ(z);
	}
	
	public static boolean downloadFile(String url, File file) {
		try {
			URL website = new URL(url);
		    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		    FileOutputStream fos = new FileOutputStream(file);
		    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		    fos.close();
		    rbc.close();
		    return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void createFire(Block block, int age) {
		block.setType(Material.FIRE);
		BlockData bd = Material.FIRE.createBlockData();
		((Ageable)bd).setAge(age);
		block.setBlockData(bd);
	}
	
	static Map<EntityType, Material> spawnEggMap = new HashMap<>();
	static {
		Material mat = null;
		for (EntityType type : EntityType.values()) {
			mat = Material.getMaterial(type.toString() + '_' + "SPAWN_EGG");
			if (mat != null) {
				spawnEggMap.put(type, mat);
			}
		}
	}
	
	public static ItemStack getEggItemForEntityType(EntityType type) {
		return new ItemStack(spawnEggMap.get(type));
	}
	
	public static String getStringNumber(double number, int places) {
		if (places < 0) return number+"";
		if (places == 0) return (int)Math.round(number) + "";
		int x = (int)Math.pow(10, places);
		return ((double)Math.round(number * x) / x) + "";
	}
	
	private static Map<String, String> uniqueIds = new HashMap<String, String>();
	
	public static String getUniqueId(Player player) {
		String uid = player.getUniqueId().toString().replace("-", "");
		uniqueIds.put(player.getName(), uid);
		return uid;
	}
	
	public static String getUniqueId(String playerName) {
		if (uniqueIds.containsKey(playerName)) {
			return uniqueIds.get(playerName);
		}
		Player player = Bukkit.getPlayerExact(playerName);
		if (player != null) {
			return getUniqueId(player);
		}
		return null;
	}
	
	public static void setItemDamage(ItemStack item, int damage) {
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof Damageable) {
			((Damageable)meta).setDamage(damage);
		}
		item.setItemMeta(meta);
	}
	
	public static int getItemDamage(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof Damageable) {
			return ((Damageable)meta).getDamage();
		}
		return 0;
	}
	
}

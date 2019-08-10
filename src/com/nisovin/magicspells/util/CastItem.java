package com.nisovin.magicspells.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nisovin.magicspells.MagicSpells;

public class CastItem {
	private Material type = Material.AIR;
	private short data = 0;
	private String name = "";
	private Map<Enchantment, Integer> enchants = null;
	
	public CastItem() {
	}
	
	public CastItem(Material type) {
		this.type = type;
	}
	
	public CastItem(Material type, short data) {
		this.type = type;
		if (MagicSpells.ignoreCastItemDurability(type)) {
			this.data = 0;
		} else {
			this.data = data;
		}
	}
	
	public CastItem(ItemStack item) {
		if (item == null) {
			this.type = Material.AIR;
			this.data = 0;
		} else {
			this.type = item.getType();
			if (this.type == Material.AIR || MagicSpells.ignoreCastItemDurability(type)) {
				this.data = 0;
			} else {
				this.data = (short)Util.getItemDamage(item);
			}
			if (this.type != Material.AIR && !MagicSpells.ignoreCastItemNames() && item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				if (meta.hasDisplayName()) {
					if (MagicSpells.ignoreCastItemNameColors()) {
						this.name = ChatColor.stripColor(meta.getDisplayName());
					} else {
						this.name = meta.getDisplayName();
					}
				}
			}
			if (this.type != Material.AIR && !MagicSpells.ignoreCastItemEnchants()) {
				enchants = item.getEnchantments();
			}
		}
	}
	
	public CastItem(String string) {
		String s = string;
		if (s.contains("|")) {
			String[] temp = s.split("\\|");
			s = temp[0];
			if (!MagicSpells.ignoreCastItemNames() && temp.length > 1) {
				if (MagicSpells.ignoreCastItemNameColors()) {
					name = ChatColor.stripColor(temp[1]);
				} else {
					name = temp[1];
				}
			}
		}
		if (s.contains(";")) {
			String[] temp = s.split(";");
			s = temp[0];
			if (!MagicSpells.ignoreCastItemEnchants()) {
				String[] split = temp[1].split("\\+");
				enchants = new HashMap<>();
				for (int i = 0; i < split.length; i++) {
					String[] enchantData = split[i].split("-");
					enchants.put(
							Enchantment.getByKey(NamespacedKey.minecraft(enchantData[0].toLowerCase())),
							Integer.parseInt(enchantData[1]));
				}
			}
		}
		if (s.contains(":")) {
			String[] split = s.split(":");
			this.type = Material.getMaterial(split[0].toUpperCase());
			if (MagicSpells.ignoreCastItemDurability(type)) {
				this.data = 0;
			} else {
				this.data = Short.parseShort(split[1]);
			}
		} else {
			this.type = Material.getMaterial(s.toUpperCase());
			this.data = 0;
		}
	}
	
	public Material getItemType() {
		return this.type;
	}
	
	public boolean equals(CastItem i) {
		return i.type == this.type && i.data == this.data && (MagicSpells.ignoreCastItemNames() || i.name.equals(this.name)) && (MagicSpells.ignoreCastItemEnchants() || compareEnchants(this.enchants, i.enchants));
	}
	
	public boolean equals(ItemStack i) {
		return i.getType() == type && Util.getItemDamage(i) == data
				&& (MagicSpells.ignoreCastItemNames() || namesEqual(i)) && (MagicSpells.ignoreCastItemEnchants() || compareEnchants(this.enchants, i.getEnchantments()));
	}
	
	private boolean namesEqual(ItemStack i) {
		String n = null;
		if (i.hasItemMeta()) {
			ItemMeta meta = i.getItemMeta();
			if (meta.hasDisplayName()) {
				if (MagicSpells.ignoreCastItemNameColors()) {
					n = ChatColor.stripColor(meta.getDisplayName());
				} else {
					n = meta.getDisplayName();
				}
			}
		}
		if (n == null && (name == null || name.isEmpty())) return true;
		if (n == null || name == null) return false;
		return n.equals(name);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof CastItem) {
			return equals((CastItem)o);
		} else if (o instanceof ItemStack) {
			return equals((ItemStack)o);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		String s;
		if (data == 0) {
			s = type+"";
		} else {
			s = type + ":" + data;
		}
		if (enchants != null) {
			s += ";";
			int i = 0;
			for (Enchantment e : enchants.keySet()) {
				s += e.toString() + "-" + enchants.get(e);
				if (i < enchants.size()-1) {
					s += "+";
				}
				i++;
			}
		}
		if (name != null && !name.isEmpty()) {
			s += "|" + name;
		}
		return s;
	}
	
	private boolean compareEnchants(Map<Enchantment, Integer> o1, Map<Enchantment, Integer> o2) {
		if (o1 == null && o2 == null) return true;
		if (o1 == null || o2 == null) return false;
		if (o1.size() != o2.size()) return false;
		return o1.equals(o2);
	}
	
}

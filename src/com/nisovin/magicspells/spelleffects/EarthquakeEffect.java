package com.nisovin.magicspells.spelleffects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.materials.MagicBlockMaterial;
import com.nisovin.magicspells.materials.MagicMaterial;

public class EarthquakeEffect extends SpellEffect {

	int horizRadius;
	int vertRadius;
	int horizRadiusSq;
	int vertRadiusSq;
	float velocity;
	
	@Override
	public void loadFromString(String string) {
		String[] split = string.split(" ");
		horizRadius = Integer.parseInt(split[0]);
		horizRadiusSq = horizRadius * horizRadius;
		vertRadius = Integer.parseInt(split[1]);
		vertRadiusSq = vertRadius * vertRadius;
		velocity = Float.parseFloat(split[2]);
		registerRemoval();
	}

	@Override
	protected void loadFromConfig(ConfigurationSection config) {
		horizRadius = config.getInt("horizontal-radius", 1);
		horizRadiusSq = horizRadius * horizRadius;
		vertRadius = config.getInt("vertical-radius", 1);
		vertRadiusSq = vertRadius * vertRadius;
		velocity = (float)config.getDouble("velocity", 0.5);
		registerRemoval();
	}
	
	private void registerRemoval() {
		MagicSpells.registerEvents(new FallingBlockListener());
		MagicSpells.scheduleRepeatingTask(new Runnable() {
			public void run() {
				if (fallingBlocks.size() > 0) {
					Iterator<FallingBlock> iter = fallingBlocks.iterator();
					while (iter.hasNext()) {
						if (!iter.next().isValid()) {
							iter.remove();
						}
					}
				}
			}
		}, 600, 600);
	}
	
	Set<FallingBlock> fallingBlocks = new HashSet<FallingBlock>();
	
	@Override
	public void playEffectLocation(Location target) {
		int centerX = target.getBlockX();
		int centerY = target.getBlockY();
		int centerZ = target.getBlockZ();

		List<Block> blocksToThrow = new ArrayList<Block>();
		
		for (int y = centerY - vertRadius; y <= centerY + vertRadius; y++) {
			for (int x = centerX - horizRadius; x <= centerX + horizRadius; x++) {
				for (int z = centerZ - horizRadius; z <= centerZ + horizRadius; z++) {
					Block b = target.getWorld().getBlockAt(x, y, z);
					if (b.getRelative(BlockFace.UP).getType() != Material.AIR) continue;
					if (b.getType() != Material.BEDROCK && b.getType() != Material.AIR && b.getType().isSolid()) {
						blocksToThrow.add(b);
					}
				}
			}
		}
		
		for (Block b : blocksToThrow) {
			MagicMaterial mat = new MagicBlockMaterial(b.getState().getData());
			Location l = new Location(target.getWorld(), b.getX() + 0.5, b.getY() + 1, b.getZ() + 0.5);
			FallingBlock fb = mat.spawnFallingBlock(l);
			fb.setDropItem(false);
			Vector v = null;
			v = new Vector(0, velocity, 0);
			v.setY(v.getY() + ((Math.random() - .5) / 4));
			if (v != null) fb.setVelocity(v);
			fallingBlocks.add(fb);
		}
		
	}
	
	class FallingBlockListener implements Listener {
		@EventHandler
		public void onBlockLand(EntityChangeBlockEvent event) {
			boolean removed = fallingBlocks.remove(event.getEntity());
			if (removed) event.setCancelled(true);
		}
	}
}

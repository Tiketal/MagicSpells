package com.nisovin.magicspells.spells.targeted;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.nisovin.magicspells.spells.TargetedEntitySpell;
import com.nisovin.magicspells.spells.TargetedSpell;
import com.nisovin.magicspells.util.MagicConfig;
import com.nisovin.magicspells.util.TargetInfo;

public class SwitchHealthSpell extends TargetedSpell implements TargetedEntitySpell {

	boolean requireGreaterHealthPercent;
	boolean requireLesserHealthPercent;
	
	public SwitchHealthSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		requireGreaterHealthPercent = getConfigBoolean("require-greater-health-percent", false);
		requireLesserHealthPercent = getConfigBoolean("require-lesser-health-percent", false);
	}

	@Override
	public PostCastAction castSpell(Player player, SpellCastState state, float power, String[] args) {
		if (state == SpellCastState.NORMAL) {
			TargetInfo<LivingEntity> target = getTargetedEntity(player, power);
			if (target == null) {
				return noTarget(player);
			}
			boolean ok = switchHealth(player, target.getTarget());
			if (!ok) {
				return noTarget(player);
			}
			sendMessages(player, target.getTarget());
			return PostCastAction.NO_MESSAGES;
		}
		return PostCastAction.HANDLE_NORMALLY;
	}
	
	boolean switchHealth(Player caster, LivingEntity target) {
		if (caster.isDead() || target.isDead()) return false;
		double casterPct = caster.getHealth() / caster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double targetPct = target.getHealth() / target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		if (requireGreaterHealthPercent && casterPct < targetPct) return false;
		if (requireLesserHealthPercent && casterPct > targetPct) return false;
		caster.setHealth(targetPct * caster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		target.setHealth(casterPct * target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		playSpellEffects(caster, target);
		return true;
	}

	@Override
	public boolean castAtEntity(Player caster, LivingEntity target, float power) {
		if (!validTargetList.canTarget(caster, target)) {
			return false;
		}
		return switchHealth(caster, target);
	}

	@Override
	public boolean castAtEntity(LivingEntity target, float power) {
		return false;
	}

}

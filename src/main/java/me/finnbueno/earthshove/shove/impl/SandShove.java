package me.finnbueno.earthshove.shove.impl;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.EarthAbility;
import me.finnbueno.earthshove.shove.ShoveConfiguration;
import me.finnbueno.earthshove.util.PotionConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Finn Bon
 */
public class SandShove extends ShoveConfiguration {
	@Override
	public double defaultDamage() {
		return 2;
	}
	@Override
	public double defaultKnockback() {
		return .2;
	}
	@Override
	public List<PotionConfiguration> defaultPotionEffects() {
		return Arrays.asList(
			new PotionConfiguration(PotionEffectType.BLINDNESS, 40, 2),
			new PotionConfiguration(PotionEffectType.CONFUSION, 30, 2)
		);
	}

	@Override
	public void playSound(Location location) {
		EarthAbility.playSandbendingSound(location);
	}

	@Override
	public boolean isMaterialValid(Material material) {
		return EarthAbility.isSand(material);
	}

	@Override
	public boolean canUse(BendingPlayer player) {
		return player.canSandbend();
	}
}

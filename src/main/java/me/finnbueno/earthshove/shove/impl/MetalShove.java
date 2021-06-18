package me.finnbueno.earthshove.shove.impl;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.EarthAbility;
import me.finnbueno.earthshove.shove.ShoveConfiguration;
import me.finnbueno.earthshove.util.PotionConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

/**
 * @author Finn Bon
 */
public class MetalShove extends ShoveConfiguration {
	@Override
	public double defaultDamage() {
		return 5;
	}
	@Override
	public double defaultKnockback() {
		return .3;
	}
	@Override
	public List<PotionConfiguration> defaultPotionEffects() {
		return Collections.emptyList();
	}

	@Override
	public void playSound(Location location) {
		EarthAbility.playMetalbendingSound(location);
	}

	@Override
	public boolean isMaterialValid(Material material) {
		return EarthAbility.isMetal(material);
	}

	@Override
	public boolean canUse(BendingPlayer player) {
		return player.canMetalbend();
	}
}

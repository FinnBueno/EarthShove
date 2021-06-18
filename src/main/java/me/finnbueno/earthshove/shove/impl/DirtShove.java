package me.finnbueno.earthshove.shove.impl;

import com.projectkorra.projectkorra.BendingPlayer;
import me.finnbueno.earthshove.shove.ShoveConfiguration;
import me.finnbueno.earthshove.util.PotionConfiguration;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Finn Bon
 */
public class DirtShove extends ShoveConfiguration {
	@Override
	public double defaultDamage() {
		return 3;
	}
	@Override
	public double defaultKnockback() {
		return .3;
	}
	@Override
	public List<PotionConfiguration> defaultPotionEffects() {
		return Collections.singletonList(
			new PotionConfiguration(PotionEffectType.BLINDNESS, 15, 2)
		);
	}

	@Override
	public boolean isMaterialValid(Material material) {
		return Arrays.asList(
			Material.DIRT,
			Material.COARSE_DIRT,
			Material.PODZOL,
			Material.MYCELIUM,
			Material.GRASS_BLOCK,
			Material.GRASS_PATH
		).contains(material);
	}

	@Override
	public boolean canUse(BendingPlayer player) {
		return true;
	}
}

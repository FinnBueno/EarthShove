package me.finnbueno.earthshove.shove.impl;

import com.projectkorra.projectkorra.BendingPlayer;
import me.finnbueno.earthshove.shove.ShoveConfiguration;
import me.finnbueno.earthshove.util.PotionConfiguration;
import org.bukkit.Material;

import java.util.*;

/**
 * @author Finn Bon
 */
public class StoneShove extends ShoveConfiguration {
	@Override
	public double defaultDamage() {
		return 4;
	}
	@Override
	public double defaultKnockback() {
		return .4;
	}
	@Override
	public List<PotionConfiguration> defaultPotionEffects() {
		return Collections.emptyList();
	}

	@Override
	public boolean isMaterialValid(Material material) {
		return Arrays.asList(
			Material.STONE,
			Material.GRAVEL,
			Material.CLAY,
			Material.COAL_ORE,
			Material.REDSTONE_ORE,
			Material.LAPIS_ORE,
			Material.DIAMOND_ORE,
			Material.EMERALD_ORE,
			Material.NETHERRACK,
			Material.COBBLESTONE,
			Material.STONE_SLAB,
			Material.COBBLESTONE_SLAB,
			Material.ANDESITE,
			Material.GRANITE,
			Material.DIORITE,
			Material.BASALT,
			Material.ANCIENT_DEBRIS,
			Material.BLACKSTONE,
			Material.GILDED_BLACKSTONE
		).contains(material);
	}

	@Override
	public boolean canUse(BendingPlayer player) {
		return true;
	}
}

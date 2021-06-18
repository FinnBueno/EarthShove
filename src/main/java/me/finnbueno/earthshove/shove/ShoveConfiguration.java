package me.finnbueno.earthshove.shove;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import me.finnbueno.earthshove.util.ConfigValue;
import me.finnbueno.earthshove.util.PotionConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Finn Bon
 */
public abstract class ShoveConfiguration {

	@ConfigValue
	private List<PotionConfiguration> potionEffects = defaultPotionEffects();
	@ConfigValue
	private double damage = defaultDamage();
	@ConfigValue
	private double knockback = defaultKnockback();

	public abstract double defaultDamage();
	public abstract double defaultKnockback();
	public abstract List<PotionConfiguration> defaultPotionEffects();
	public abstract boolean isMaterialValid(Material material);
	public abstract boolean canUse(BendingPlayer player);

	public void playSound(Location location) {
		EarthAbility.playEarthbendingSound(location);
	}

	public void apply(LivingEntity target, CoreAbility cause) {
		potionEffects.forEach(pe -> pe.apply(target));
		DamageHandler.damageEntity(target, damage, cause);
		Vector away = GeneralMethods.getDirection(cause.getPlayer().getLocation(), target.getLocation());
		away.setY(away.getY() + .15);
		target.setVelocity(target.getVelocity().add(away.multiply(knockback)));
	}
}

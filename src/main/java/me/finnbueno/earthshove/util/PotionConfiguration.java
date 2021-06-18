package me.finnbueno.earthshove.util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Finn Bon
 */
public class PotionConfiguration {

	private PotionEffectType type;
	private int duration;
	private int amplifier;

	public PotionConfiguration(PotionEffectType type, int duration, int amplifier) {
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public PotionConfiguration(String type, int duration, int amplifier) {
		this(PotionEffectType.getByName(type), duration, amplifier);
	}

	public void apply(LivingEntity target) {
		target.addPotionEffect(new PotionEffect(type, duration, amplifier, true, false, false));
	}

	public PotionEffectType getType() {
		return type;
	}

	public int getDuration() {
		return duration;
	}

	public int getAmplifier() {
		return amplifier;
	}
}

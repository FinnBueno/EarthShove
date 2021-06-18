package me.finnbueno.earthshove.shove;

import me.finnbueno.earthshove.shove.impl.DirtShove;
import me.finnbueno.earthshove.shove.impl.MetalShove;
import me.finnbueno.earthshove.shove.impl.SandShove;
import me.finnbueno.earthshove.shove.impl.StoneShove;

/**
 * @author Finn Bon
 */
public enum ShoveType {
	SAND(new SandShove()), DIRT(new DirtShove()), STONE(new StoneShove()), METAL(new MetalShove());

	private final ShoveConfiguration shoveConfig;

	ShoveType(ShoveConfiguration shoveConfig) {
		this.shoveConfig = shoveConfig;
	}

	public ShoveConfiguration getConfig() {
		return shoveConfig;
	}

	public String getName() {
		return name().charAt(0) + name().toLowerCase().substring(1);
	}
}

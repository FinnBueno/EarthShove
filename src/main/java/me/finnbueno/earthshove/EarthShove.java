package me.finnbueno.earthshove;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * @author Finn Bon
 */
public class EarthShove extends EarthAbility implements AddonAbility, ComboAbility {

	public EarthShove(Player player) {
		super(player);
	}

	public void progress() {

	}

	public boolean isSneakAbility() {
		return false;
	}

	public boolean isHarmlessAbility() {
		return false;
	}

	public long getCooldown() {
		return 0;
	}

	public String getName() {
		return null;
	}

	public Location getLocation() {
		return null;
	}

	public void load() {

	}

	public void stop() {

	}

	public String getAuthor() {
		return null;
	}

	public String getVersion() {
		return null;
	}

	public Object createNewComboInstance(Player player) {
		return null;
	}

	public ArrayList<ComboManager.AbilityInformation> getCombination() {
		return null;
	}
}

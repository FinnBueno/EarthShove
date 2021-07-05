package me.finnbueno.earthshove;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.configuration.Config;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.finnbueno.earthshove.util.ConfigAdapter;
import me.finnbueno.earthshove.util.ConfigValue;
import me.finnbueno.earthshove.shove.ShoveConfiguration;
import me.finnbueno.earthshove.shove.ShoveType;
import me.finnbueno.earthshove.util.PotionConfiguration;
import me.finnbueno.earthshove.util.PotionConfigurationAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Finn Bon
 */
public class EarthShove extends EarthAbility implements AddonAbility, ComboAbility {

	private static final Map<Class<?>, ConfigAdapter<?>> ADAPTERS = Map.of(
		PotionConfiguration.class, new PotionConfigurationAdapter()
	);

	private Permission perm;

	private static Map<ShoveType, ShoveConfiguration> SHOVE_TYPES = Arrays.stream(ShoveType.values())
		.collect(Collectors.toMap(Function.identity(), ShoveType::getConfig));

	@ConfigValue
	private static long COOLDOWN = 3000;
	@ConfigValue
	private static double RANGE = 3.5;
	@ConfigValue
	private static int MAX_ANGLE = 80;
	@ConfigValue
	private static int MAX_HILL = 2;

	private static final double RADIUS_STEP = .5;

	private final ShoveConfiguration shove;
	private final Block source;
	private double radius;
	private final Set<Entity> affectedEntities;

	public EarthShove(Player player) {
		super(player);
		this.radius = RADIUS_STEP;
		this.affectedEntities = new HashSet<>();
		this.source = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		this.shove = findType();

		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}

		if (!isEarthbendable(this.source)) {
			return;
		}

		if (this.shove == null) {
			return;
		}

		bPlayer.addCooldown(this);
		start();
	}

	public void progress() {
		if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
			remove();
			return;
		}

		if (this.radius % 1 == 0) {
			this.shove.playSound(getLocation());
		}

		playParticles();

		GeneralMethods.getEntitiesAroundPoint(player.getLocation(), this.radius).stream()
			.filter(e -> e instanceof LivingEntity)
			.filter(e -> e.getUniqueId() != player.getUniqueId())
			.filter(e -> !affectedEntities.contains(e))
			.filter(e -> determineAngle(e) <= MAX_ANGLE)
			.forEach(e -> {
				affectedEntities.add(e);
				this.shove.apply(e, this);
			});

		this.radius += RADIUS_STEP;
		if (this.radius > RANGE) {
			remove();
		}
	}

	private void playParticles() {
		double maxRise = .2;
		double heightStep = maxRise / RANGE;
		double halfAngle = MAX_ANGLE / 2.0;
		double angleStep = 45;
		double yaw = player.getLocation().getYaw() + 90;
		for (double angle = yaw - halfAngle; angle < yaw + halfAngle; angle += angleStep / this.radius) {
			double radians = Math.toRadians(angle);
			double x = Math.cos(radians) * radius;
			double z = Math.sin(radians) * radius;
			Location loc = player.getLocation().add(x, heightStep * radius, z);
			if (!bringToGround(loc)) {
				continue;
			}
			ParticleEffect.BLOCK_CRACK.display(loc, 5, 0.15, 0.05, 0.15, this.source.getBlockData());
		}
	}

	private boolean bringToGround(Location loc) {
		if (GeneralMethods.isSolid(loc.getBlock())) {
			// move it up
			return moveUp(loc);
		} else if (!GeneralMethods.isSolid(loc.getBlock().getRelative(BlockFace.DOWN))) {
			// move it down
			return moveDown(loc);
		}
		return true;
	}

	private boolean moveDown(Location loc) {
		int step = 0;
		while (!GeneralMethods.isSolid(loc.getBlock().getRelative(BlockFace.DOWN))) {
			loc.subtract(0, 1, 0);
			step++;
			if (step > MAX_HILL) {
				return false;
			}
		}
		return true;
	}

	private boolean moveUp(Location loc) {
		int step = 0;
		while (GeneralMethods.isSolid(loc.getBlock())) {
			loc.add(0, 1, 0);
			step++;
			if (step > MAX_HILL) {
				return false;
			}
		}
		return true;
	}

	private double determineAngle(Entity e) {
		Vector toTarget = GeneralMethods.getDirection(player.getLocation(), e.getLocation()).normalize();
		return Math.toDegrees(player.getLocation().getDirection().angle(toTarget));
	}

	private ShoveConfiguration findType() {
		return SHOVE_TYPES.values().stream()
			.filter(
				sc ->
					sc.canUse(bPlayer) &&
					sc.isMaterialValid(this.source.getType())
			).findFirst().orElse(null);
	}

	public boolean isSneakAbility() {
		return true;
	}

	public boolean isHarmlessAbility() {
		return false;
	}

	public long getCooldown() {
		return COOLDOWN;
	}

	public String getName() {
		return "EarthShove";
	}

	public Location getLocation() {
		return player.getLocation();
	}

	public void load() {
		perm = new Permission("bending.ability." + getName());
		perm.setDefault(PermissionDefault.TRUE);
		Bukkit.getServer().getPluginManager().addPermission(perm);

		String basePath = String.format("ExtraAbilities.%s.%s.%s.", getAuthor(), getElement().getName(), getName());
		Config config = ConfigManager.defaultConfig;
		FileConfiguration fc = config.get();

		try {
			Field[] fields = getClass().getDeclaredFields();
			setFields(basePath, fc, fields, this);

			// handle shove types manually because it's a pretty complex scenario
			for (Map.Entry<ShoveType, ShoveConfiguration> entry : SHOVE_TYPES.entrySet()) {
				ShoveType type = entry.getKey();
				ShoveConfiguration shoveConfiguration = entry.getValue();
				String shoveBasePath = String.format("%s.Types.%s.", basePath, type.getName());
				Field[] shoveFields = shoveConfiguration.getClass().getSuperclass().getDeclaredFields();
				setFields(shoveBasePath, fc, shoveFields, shoveConfiguration);
			}

		} catch (IllegalAccessException e){
			e.printStackTrace();
		}
		config.save();

		config = ConfigManager.languageConfig;
		config.get().addDefault(
			String.format("Abilities.%s.Combo.%s.Description", getElement().getName(), getName()),
			"Shove the earth in front of you to disorientate and push back your enemy. This ability has different effects depending on the material used."
		);
		config.get().addDefault(
			String.format("Abilities.%s.Combo.%s.Instructions", getElement().getName(), getName()),
			"Shockwave (Hold Shift) > EarthBlast (Left Click) > EarthBlast (Left Click)"
		);
		config.save();
	}

	private void setFields(String basePath, FileConfiguration fileConfiguration, Field[] fields, Object setOn) throws IllegalAccessException {
		for (Field field : fields) {
			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}

			ConfigValue cf = field.getAnnotation(ConfigValue.class);
			if (cf == null) {
				continue;
			}

			field.setAccessible(true);

			String path = basePath + (cf.value().length() == 0 ? formatFieldName(field.getName()) : cf.value());
			Object value = field.get(setOn);

			if (value == null) {
				throw new IllegalArgumentException(String.format("Field %s has no default value!", field.getName()));
			}

			LinkedList<Class<?>> typeList = new LinkedList<>();
			fileConfiguration.addDefault(path, handleDefaultValue(value, typeList));
			value = fileConfiguration.get(path);
			field.set(setOn, handleConfigValue(value, typeList));
		}
	}

	private Object handleConfigValue(Object value, LinkedList<Class<?>> typeList) {
		Class<?> type = typeList.removeFirst();
		if (value instanceof Collection) {
			return ((Collection<?>)value).stream().map(o -> handleConfigValue(o, typeList)).collect(Collectors.toList());
		} else if (ADAPTERS.containsKey(type)) {
			ConfigAdapter<?> ca = ADAPTERS.get(type);
			return ca.build(value);
		} else {
			return value;
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private Object handleDefaultValue(Object value, LinkedList<Class<?>> typeList) {
		typeList.addLast(value.getClass());
		if (value instanceof Collection) {
			return ((Collection<?>)value).stream().map(o -> handleDefaultValue(o, typeList)).collect(Collectors.toList());
		} else if (ADAPTERS.containsKey(value.getClass())) {
			ConfigAdapter ca = ADAPTERS.get(value.getClass());
			return ca.serialize(value);
		} else {
			return value;
		}
	}

	private String formatFieldName(String name) {
		String[] split = name.split("_");
		StringBuilder result = new StringBuilder();
		for (String part : split) {
			result.append(part.toUpperCase().charAt(0)).append(part.toLowerCase().substring(1));
		}
		return result.toString();
	}

	public void stop() {
		ProjectKorra.plugin.getServer().getPluginManager().removePermission(perm);
	}

	public String getAuthor() {
		return "FinnBueno";
	}

	public String getVersion() {
		return "1.1.1";
	}

	public Object createNewComboInstance(Player player) {
		return new EarthShove(player);
	}

	public ArrayList<ComboManager.AbilityInformation> getCombination() {
		return new ArrayList<>(
			Arrays.asList(
				new ComboManager.AbilityInformation("Shockwave", ClickType.SHIFT_DOWN),
				new ComboManager.AbilityInformation("EarthBlast", ClickType.LEFT_CLICK),
				new ComboManager.AbilityInformation("EarthBlast", ClickType.LEFT_CLICK)
			)
		);
	}
}

package me.finnbueno.earthshove.util;

/**
 * @author Finn Bon
 */
public class PotionConfigurationAdapter implements ConfigAdapter<PotionConfiguration> {
	@Override
	public String serialize(PotionConfiguration pc) {
		return String.format("%s:%d:%d", pc.getType().getName(), pc.getDuration(), pc.getAmplifier());
	}

	@Override
	public PotionConfiguration build(Object string) {
		String[] split = ((String) string).split(":");
		return new PotionConfiguration(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
	}
}

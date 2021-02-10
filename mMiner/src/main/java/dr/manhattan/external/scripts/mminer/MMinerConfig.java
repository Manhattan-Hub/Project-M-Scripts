package dr.manhattan.external.scripts.mminer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Title;

@ConfigGroup("m")
public interface MMinerConfig extends Config
{
	@ConfigTitleSection(
		keyName = "mainTitle",
		name = "MMiner",
		description = "Configure MMiner",
		position = 0
	)
	default Title mainTitle()
	{
		return new Title();
	}

	@ConfigItem(
			keyName = "maxRockDistance",
			name = "Maximum rock distance",
			description = "Maximum distance of a rock in tiles.",
			position = 1,
			titleSection = "mainTitle"
	)
	default int maxRockDistance()
	{
		return 10;
	}

}

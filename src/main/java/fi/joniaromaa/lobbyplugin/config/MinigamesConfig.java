package fi.joniaromaa.lobbyplugin.config;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;

public class MinigamesConfig
{
	@Getter private ChickenRaceConfig chickenRaceConfig;
	@Getter private NPCHunting npcHunting;
	
	public MinigamesConfig(ConfigurationSection configSection)
	{
		ConfigurationSection chickenRace = configSection.getConfigurationSection("chicken-race");
		if (chickenRace != null)
		{
			this.chickenRaceConfig = new ChickenRaceConfig(chickenRace);
		}

		ConfigurationSection npcHunting = configSection.getConfigurationSection("npc-hunting");
		if (npcHunting != null)
		{
			this.npcHunting = new NPCHunting(npcHunting);
		}
	}
}

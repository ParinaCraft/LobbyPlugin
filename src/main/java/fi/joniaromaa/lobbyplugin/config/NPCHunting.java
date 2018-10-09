package fi.joniaromaa.lobbyplugin.config;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;

public class NPCHunting
{
	@Getter private boolean enabled;
	
	@Getter private List<String> npcs;
	
	public NPCHunting(ConfigurationSection configSection)
	{
		this.enabled = configSection.getBoolean("enabled");
		
		this.npcs = configSection.getStringList("npcs");
	}
}

package fi.joniaromaa.lobbyplugin.npchunting;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;

public class NPCHuntingNPC
{
	@Getter private boolean enabled;

	@Getter private String foundMessage;
	@Getter private String alreadyFoundMessage;
	
	public NPCHuntingNPC(ConfigurationSection configSection)
	{
		this.enabled = configSection.getBoolean("enabled");
		
		this.foundMessage = configSection.getString("found-message");
		this.alreadyFoundMessage = configSection.getString("already-found-message");
	}
}

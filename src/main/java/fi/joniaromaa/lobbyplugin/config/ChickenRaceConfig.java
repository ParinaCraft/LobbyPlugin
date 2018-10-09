package fi.joniaromaa.lobbyplugin.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.lobbyplugin.minigames.chickenrace.ChickenRaceStartLocation;
import fi.joniaromaa.lobbyplugin.minigames.chickenrace.ChickenRaceTrack;
import lombok.Getter;

public class ChickenRaceConfig
{
	@Getter private boolean enabled;
	
	@Getter private int startCountdown;
	
	@Getter private List<ChickenRaceStartLocation> startLocations;
	@Getter private HashMap<String, ChickenRaceTrack> tracks;
	
	public ChickenRaceConfig(ConfigurationSection configSection)
	{
		this.startLocations = new ArrayList<>();
		this.tracks = new HashMap<>();
		
		this.enabled = configSection.getBoolean("enabled");
		
		this.startCountdown = configSection.getInt("start-countdown");
		
		ConfigurationSection startLocations = configSection.getConfigurationSection("start-locations");
		if (startLocations != null)
		{
			for(String startLocation : startLocations.getKeys(false))
			{
				ChickenRaceStartLocation startLocation_ = new ChickenRaceStartLocation(startLocations.getConfigurationSection(startLocation));
				if (startLocation_.isEnabled())
				{
					this.startLocations.add(startLocation_);
				}
			}
		}
		
		ConfigurationSection tracks = configSection.getConfigurationSection("tracks");
		if (tracks != null)
		{
			for(String track : tracks.getKeys(false))
			{
				ChickenRaceTrack track_ = new ChickenRaceTrack(tracks.getConfigurationSection(track));
				if (track_.isEnabled())
				{
					if (track_.getPath().size() > 0)
					{
						this.tracks.put(track_.getName(), track_);
					}
					else
					{
						LobbyPlugin.getPlugin().getLogger().warning(track + "'s path was empty! Skipping... ");
					}
				}
			}
		}
	}
}

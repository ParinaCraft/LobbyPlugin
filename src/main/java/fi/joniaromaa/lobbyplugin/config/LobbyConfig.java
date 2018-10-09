package fi.joniaromaa.lobbyplugin.config;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import lombok.Getter;

public class LobbyConfig
{
	@Getter private boolean enabled;
	
	@Getter private boolean voidTeleportOutEnabled;
	@Getter private int voidTeleportOutFallDistance;
	@Getter private HashMap<String, Location> voidTeleportOutLocations;
	
	@Getter private MinigamesConfig minigamesConfig;
	
	public LobbyConfig(FileConfiguration file)
	{
		this.voidTeleportOutLocations = new HashMap<>();
		
		this.enabled = file.getBoolean("enabled");
		
		this.voidTeleportOutEnabled = file.getBoolean("void.teleport-out.enabled");
		this.voidTeleportOutFallDistance = file.getInt("void.teleport-out.fall-distance");
		
		ConfigurationSection voidTeleportOutWorlds = file.getConfigurationSection("void.teleport-out.worlds");
		if (voidTeleportOutWorlds != null)
		{
			for(String world : voidTeleportOutWorlds.getKeys(false))
			{
				ConfigurationSection world_ = voidTeleportOutWorlds.getConfigurationSection(world);
				if (world_.getBoolean("enabled"))
				{
					String[] coords = world_.getString("coords").trim().split("\\s*,\\s*");
					
					this.voidTeleportOutLocations.put(world, new Location(LobbyPlugin.getPlugin().getServer().getWorld(world), Float.parseFloat(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[4])));
				}
			}
		}
		
		ConfigurationSection minigames = file.getConfigurationSection("minigames");
		if (minigames != null)
		{
			this.minigamesConfig = new MinigamesConfig(minigames);
		}
	}
}

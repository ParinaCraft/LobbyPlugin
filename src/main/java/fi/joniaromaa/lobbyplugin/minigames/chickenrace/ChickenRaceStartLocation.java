package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.comphenix.protocol.ProtocolLibrary;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.parinacorelibrary.bukkit.holograms.HologramManager;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class ChickenRaceStartLocation
{
	@Getter private boolean enabled;
	
	@Getter private String defaultTrack;

	@Getter private Location centerLocation;
	@Getter private Location exitLocation;
	@Getter private Location trackSelectorLocation;
	@Getter private Location leaderboardsLocation;
	
	@Getter private String shape;
	@Getter private Float[] shapeSize;
	@Getter private Effect shapeEffect;
	
	@Getter private List<String> tracks;
	
	@Getter private HologramManager hologramManager;
	
	public ChickenRaceStartLocation(ConfigurationSection configSection)
	{
		this.enabled = configSection.getBoolean("enabled");
		
		this.defaultTrack = configSection.getString("default-track");
		
		String[] centerLocation = configSection.getString("center-location").trim().split("\\s*;\\s*");
		String[] centerPos = centerLocation[1].split("\\s*,\\s*");
		this.centerLocation = new Location(LobbyPlugin.getPlugin().getServer().getWorld(centerLocation[0]), Double.parseDouble(centerPos[0]), Double.parseDouble(centerPos[1]), Double.parseDouble(centerPos[2]));

		String[] exitLocation = configSection.getString("exit-location").trim().split("\\s*;\\s*");
		String[] exitPos = exitLocation[1].split("\\s*,\\s*");
		this.exitLocation = new Location(LobbyPlugin.getPlugin().getServer().getWorld(exitLocation[0]), Double.parseDouble(exitPos[0]), Double.parseDouble(exitPos[1]), Double.parseDouble(exitPos[2]));
		
		if (configSection.contains("track-selector-location"))
		{
			String[] trackSelectorLocation = configSection.getString("track-selector-location").trim().split("\\s*;\\s*");
			String[] trackSelectorPos = trackSelectorLocation[1].split("\\s*,\\s*");
			this.trackSelectorLocation = new Location(LobbyPlugin.getPlugin().getServer().getWorld(trackSelectorLocation[0]), Double.parseDouble(trackSelectorPos[0]), Double.parseDouble(trackSelectorPos[1]), Double.parseDouble(trackSelectorPos[2]));
		}
		else
		{
			this.trackSelectorLocation = null;
		}
		
		String[] leaderboardsLocation = configSection.getString("leaderboards-location").trim().split("\\s*;\\s*");
		String[] leaderboardsPos = leaderboardsLocation[1].split("\\s*,\\s*");
		this.leaderboardsLocation = new Location(LobbyPlugin.getPlugin().getServer().getWorld(leaderboardsLocation[0]), Double.parseDouble(leaderboardsPos[0]), Double.parseDouble(leaderboardsPos[1]), Double.parseDouble(leaderboardsPos[2]));
		
		this.shape = configSection.getString("shape");
		this.shapeSize = configSection.getFloatList("shape-size").toArray(new Float[0]);
		this.shapeEffect = Effect.valueOf(configSection.getString("shape-effect"));
		
		this.tracks = configSection.getStringList("tracks");
		
		this.hologramManager = new HologramManager();
	}
	
	public void onEnable()
	{
		ChickenRaceLeaderboard leaderboard = new ChickenRaceLeaderboard(this.leaderboardsLocation, this.defaultTrack);
		
		LobbyPlugin.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(LobbyPlugin.getPlugin(), () -> leaderboard.tick(), 0, 100);
		
		this.hologramManager.addTickableHologram(leaderboard);
		this.hologramManager.addStaticHologram(ChatColor.BLUE + ChatColor.BOLD.toString() + "Bawk Bawk", this.getCenterLocation().clone().add(0, 2, 0));
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new ChickenRaceLeaderboardInjector(leaderboard));
	
		this.buildTrackSelector();
	}
	
	private void buildTrackSelector()
	{
		if (this.getTrackSelectorLocation() != null)
		{
			//TODO
		}
	}
	
	public void onDisable()
	{
		
	}
}

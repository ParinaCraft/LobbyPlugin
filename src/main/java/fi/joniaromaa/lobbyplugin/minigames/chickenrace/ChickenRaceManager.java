package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.lobbyplugin.config.ChickenRaceConfig;
import fi.joniaromaa.lobbyplugin.minigames.chickenrace.nms.ChickenRaceEntityChicken;
import fi.joniaromaa.lobbyplugin.nms.EntityNMS;
import lombok.Getter;

public class ChickenRaceManager
{
	@Getter private final ChickenRaceConfig config;
	
	@Getter private ChickenRaceRunnable updateRunnable;
	
	private HashMap<UUID, String> selectedTracks;
	
	private HashMap<UUID, ChickenRacePlayer> players;
	private HashMap<UUID, Long> countdownPlayers;
	
	public ChickenRaceManager(ChickenRaceConfig config)
	{
		this.config = config;
		
		this.updateRunnable = new ChickenRaceRunnable();
		
		this.selectedTracks = new HashMap<>();
		
		this.players = new HashMap<>();
		this.countdownPlayers = new HashMap<>();
	}
	
	public void onEnable()
	{
		for(ChickenRaceStartLocation startLocation : LobbyPlugin.getPlugin().getChickenRaceManager().getConfig().getStartLocations())
		{
			startLocation.onEnable();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void doCountdownLogic(Player player, ChickenRaceStartLocation startLocation)
	{
		if (!this.players.containsKey(player.getUniqueId()))
		{
			long now = System.nanoTime();
			
			String selectedTrack = this.selectedTracks.getOrDefault(player.getUniqueId(), startLocation.getDefaultTrack());
			if (!startLocation.getTracks().contains(selectedTrack))
			{
				selectedTrack = null;
			}

			Long previousTime = this.countdownPlayers.putIfAbsent(player.getUniqueId(), now);
			if (selectedTrack != null)
			{
				if (previousTime == null)
				{
					previousTime = now;
				}
				
				int seconds = this.config.getStartCountdown() - (int)Math.floor((now - previousTime) / 1000000000L);
				if (seconds > 0)
				{
					player.sendTitle(seconds + "", selectedTrack);
				}
				else
				{
					if (this.countdownPlayers.remove(player.getUniqueId()) != null)
					{
						try
						{
							this.startRace(player, selectedTrack, startLocation.getExitLocation());
						}
						catch (IllegalArgumentException | IllegalAccessException e)
						{
							e.printStackTrace();
							
							player.sendTitle("Error", "");
						}
					}
				}
			}
			else
			{
				if (previousTime == null || (this.config.getStartCountdown() - (int)Math.floor((now - previousTime) / 1000000000L) <= this.config.getStartCountdown() - 1))
				{
					player.sendTitle("No playable tracks", "");
					
					this.countdownPlayers.put(player.getUniqueId(), now);
				}
			}
		}
	}
	
	public void removeFromCountdownLogic(Player player)
	{
		this.countdownPlayers.remove(player.getUniqueId());
	}
	
	@SuppressWarnings("deprecation")
	public void startRace(Player player, String track, Location exitLocation) throws IllegalArgumentException, IllegalAccessException
	{
		if (player.isOnline())
		{
			player.sendTitle("GO!", track);
			
			ChickenRaceTrack track_ = this.config.getTracks().get(track);
			
			Chicken chicken = (Chicken)EntityNMS.spawnEntity(new ChickenRaceEntityChicken(((CraftWorld)player.getWorld()).getHandle()), player.getLocation());
			chicken.setVelocity(track_.getLaunchVelocity());
			
			ChickenRacePlayer player_ = new ChickenRacePlayer(player, chicken, track_, exitLocation);
			ChickenRacePlayer old = this.players.put(player.getUniqueId(), player_);
			if (old != null)
			{
				old.leave();
			}
			
			player_.start();
		}
	}
	
	public void leaveRace(Player player)
	{
		ChickenRacePlayer player_ = this.players.remove(player.getUniqueId());
		if (player_ != null)
		{
			player_.leave();
		}
	}
	
	public void onDisable()
	{
		for(ChickenRacePlayer player : this.players.values())
		{
			player.leave();
		}
		
		for(ChickenRaceStartLocation startLocation : LobbyPlugin.getPlugin().getChickenRaceManager().getConfig().getStartLocations())
		{
			startLocation.onDisable();
		}
		
		this.players.clear();
	}
	
	public ChickenRacePlayer getPlayer(Player player)
	{
		return this.players.get(player.getUniqueId());
	}
	
	public Collection<ChickenRacePlayer> getPlayers()
	{
		return this.players.values();
	}
}

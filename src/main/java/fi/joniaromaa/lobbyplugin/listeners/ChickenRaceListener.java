package fi.joniaromaa.lobbyplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;

public class ChickenRaceListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		LobbyPlugin.getPlugin().getChickenRaceManager().leaveRace(event.getPlayer());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEntityDismountEvent(EntityDismountEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			LobbyPlugin.getPlugin().getChickenRaceManager().leaveRace((Player)event.getEntity());
		}
	}
}

package fi.joniaromaa.lobbyplugin.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;

public class PlayerListener implements Listener
{
	/*@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		PlayerInventory playerInventory = player.getInventory();
		
		ItemStack profileItem = new ItemStack(Material.SKULL_ITEM);
		SkullMeta profileItemMeta = (SkullMeta)profileItem.getItemMeta();
		profileItemMeta.setDisplayName("Profiili");
		profileItemMeta.setOwner(player.getName());
		profileItem.setItemMeta(profileItemMeta);
		playerInventory.setItem(1, profileItem);
	}*/
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	private void onPlayerMoveEvent(PlayerMoveEvent event)
	{
		if (LobbyPlugin.getPlugin().getPluginConfig().isVoidTeleportOutEnabled())
		{
			if (event.getTo().getY() <= LobbyPlugin.getPlugin().getPluginConfig().getVoidTeleportOutFallDistance())
			{
				Location loc = LobbyPlugin.getPlugin().getPluginConfig().getVoidTeleportOutLocations().get(event.getTo().getWorld().getName());
				if (loc != null)
				{
					event.setTo(loc);
				}
			}
		}
	}

	/*@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			Material material = event.getMaterial();
			if (material == Material.SKULL_ITEM) //Profile?
			{
				//player.openInventory(arg0);
			}
		}
	}*/
}

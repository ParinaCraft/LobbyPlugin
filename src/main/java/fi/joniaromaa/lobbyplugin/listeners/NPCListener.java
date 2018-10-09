package fi.joniaromaa.lobbyplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import fi.joniaromaa.lobbyplugin.guis.SelectDuelsModePlayerInterfaceInventory;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class NPCListener implements Listener
{
	private static final SelectDuelsModePlayerInterfaceInventory INVENTORY = new SelectDuelsModePlayerInterfaceInventory();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onNPCRightClickEvent(NPCRightClickEvent event)
	{
		if (event.getNPC().getName().equals("Duels"))
		{
			event.getClicker().openInventory(NPCListener.INVENTORY.getInventory());
		}
	}
}

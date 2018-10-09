package fi.joniaromaa.lobbyplugin.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;

public class NPCHuntingListener implements Listener
{
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onNPCSpawnEvent(NPCSpawnEvent event)
	{
		NPC npc = event.getNPC();
		if (LobbyPlugin.getPlugin().getPluginConfig().getMinigamesConfig().getNpcHunting().getNpcs().contains(npc.getName()))
		{
			Entity npcEntity = npc.getEntity();
			if (npcEntity.getPassenger() == null)
			{
				ArmorStand armorStand = npcEntity.getWorld().spawn(npcEntity.getLocation(), ArmorStand.class);
				armorStand.setVisible(false);
				armorStand.setRemoveWhenFarAway(false);
				armorStand.setMarker(true);
				npcEntity.setPassenger(armorStand);
			}
		}
	}
}

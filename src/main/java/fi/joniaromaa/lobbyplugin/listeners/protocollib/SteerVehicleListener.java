package fi.joniaromaa.lobbyplugin.listeners.protocollib;

import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Lists;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.lobbyplugin.minigames.chickenrace.ChickenRacePlayer;

public class SteerVehicleListener extends PacketAdapter
{
	private final static List<PacketType> PACKETS = Lists.newArrayList(PacketType.Play.Client.STEER_VEHICLE);
	
	public SteerVehicleListener()
	{
		super(LobbyPlugin.getPlugin(), ListenerPriority.MONITOR, SteerVehicleListener.PACKETS);
	}
	
	@Override
	public void onPacketReceiving(PacketEvent event)
	{
		if (!event.isCancelled())
		{
			ChickenRacePlayer player = LobbyPlugin.getPlugin().getChickenRaceManager().getPlayer(event.getPlayer());
			if (player != null)
			{
				boolean jump = event.getPacket().getBooleans().read(0);
				
				player.setJump(jump);
				
				if (!jump && !player.isCanJump())
				{
					player.setCanJump(true);
				}
			}
		}
	}
}

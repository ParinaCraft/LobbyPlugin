package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Lists;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;

public class ChickenRaceLeaderboardInjector extends PacketAdapter
{
	private final static List<PacketType> PACKETS = Lists.newArrayList(PacketType.Play.Server.ENTITY_METADATA);
	
	private ChickenRaceLeaderboard leaderboard;
	
	public ChickenRaceLeaderboardInjector(ChickenRaceLeaderboard leaderboard)
	{
		super(LobbyPlugin.getPlugin(), ListenerPriority.HIGHEST, ChickenRaceLeaderboardInjector.PACKETS);
		
		this.leaderboard = leaderboard;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		this.leaderboard.injectOwn(event);
	}
}

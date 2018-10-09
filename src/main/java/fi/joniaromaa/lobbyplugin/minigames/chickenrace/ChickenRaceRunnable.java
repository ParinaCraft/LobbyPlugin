package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;

public class ChickenRaceRunnable implements Runnable
{
	private static final long START_POSITION_COUNTDOWN_UPDATE_INTERVAL = TimeUnit.MILLISECONDS.toNanos(1000);
	private static final long START_POSITION_PROMOTE_INTERVAL = TimeUnit.MILLISECONDS.toNanos(500);

	private long lastStartPositionCountdownUpdate = 0;
	private long lastStartPositionPromoteParticles = 0;
	
	@Override
	public void run()
	{
		List<Player> notPlaying = Lists.newArrayList(LobbyPlugin.getPlugin().getServer().getOnlinePlayers());
		
		for(ChickenRacePlayer player : LobbyPlugin.getPlugin().getChickenRaceManager().getPlayers().toArray(new ChickenRacePlayer[0])) //ChickenRacePlayer.tick might remove players from the game so toArray is needed
		{
			player.tick();
			
			notPlaying.remove(player.getPlayer());
		}
		
		long now = System.nanoTime();
		if (now - this.lastStartPositionCountdownUpdate >= ChickenRaceRunnable.START_POSITION_COUNTDOWN_UPDATE_INTERVAL)
		{
			this.lastStartPositionCountdownUpdate = now;
			
			for(ChickenRaceStartLocation startLocation : LobbyPlugin.getPlugin().getChickenRaceManager().getConfig().getStartLocations())
			{
				for(Player player : notPlaying)
				{
					if (player.getWorld().equals(startLocation.getCenterLocation().getWorld()) && player.getLocation().distanceSquared(startLocation.getCenterLocation()) <= 3)
					{
						LobbyPlugin.getPlugin().getChickenRaceManager().doCountdownLogic(player, startLocation);
					}
					else
					{
						LobbyPlugin.getPlugin().getChickenRaceManager().removeFromCountdownLogic(player);
					}
				}
			}
		}
		
		if (now - this.lastStartPositionPromoteParticles >= ChickenRaceRunnable.START_POSITION_PROMOTE_INTERVAL)
		{
			this.lastStartPositionPromoteParticles = now;
			
			for(ChickenRaceStartLocation startLocation : LobbyPlugin.getPlugin().getChickenRaceManager().getConfig().getStartLocations())
			{
				World world = startLocation.getCenterLocation().getWorld();
				Location location = startLocation.getCenterLocation().clone();
				
				if (startLocation.getShape().equals("plus"))
				{
					//Im idiot with this stuff so kill me plz
					
					float height = startLocation.getShapeSize()[0];
					float width = startLocation.getShapeSize()[1];
					
					float startWidth = width / 2;
					float endWidth = width - startWidth;
		
					for(float i = -startWidth; i <= endWidth; i += 0.5)
					{
						world.playEffect(location.clone().add(height, 0, i), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(-height, 0, -i), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(i, 0, height), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(-i, 0, -height), startLocation.getShapeEffect(), 1);
					}
		
					for(float i = -startWidth; i >= -startWidth - 0.5; i -= 0.5)
					{
						world.playEffect(location.clone().add(height - 1, 0, i), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(-(height - 1), 0, i), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(i, 0, height - 1), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(-i, 0, height - 1), startLocation.getShapeEffect(), 1);
					}
		
					for(float i = height - 0.5f; i < height; i += 0.5)
					{
						world.playEffect(location.clone().add(i, 0, -startWidth), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(-i, 0, -startWidth), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(startWidth, 0, i), startLocation.getShapeEffect(), 1);
						world.playEffect(location.clone().add(-startWidth, 0, i), startLocation.getShapeEffect(), 1);
					}
				}
			}
		}
	}
}

package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.lobbyplugin.nms.EntityNMS;
import fi.joniaromaa.lobbyplugin.nms.PlayerNMS;
import fi.joniaromaa.parinacorelibrary.api.ParinaCore;
import fi.joniaromaa.parinacorelibrary.common.storage.types.PostgreSqlStorage;
import fi.joniaromaa.parinacorelibrary.common.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class ChickenRacePlayer
{
	private static final long CHECK_DISTANCE_TO_NEXT_CIRCLE_INTERVAL = TimeUnit.MILLISECONDS.toNanos(1000);
	
	@Getter private final Player player;
	@Getter private final Entity vechile;
	@Getter private final ChickenRaceTrack track;
	@Getter private final Location exitLocation;
	
	@Getter private boolean playing;
	@Getter private int ticksSpent;
	@Getter private int currentCircle;
	
	@Getter @Setter private boolean jump;
	@Getter @Setter private boolean canJump;
	
	private Float distanceToNextCircle;
	private long lastCheckDistanceToNextCricle;
	
	public ChickenRacePlayer(Player player, Entity vechile, ChickenRaceTrack track, Location exitLocation)
	{
		this.player = player;
		this.vechile = vechile;
		this.track = track;
		this.exitLocation = exitLocation;
	}
	
	public void start()
	{
		if (this.vechile.isValid())
		{
			this.playing = true;
			this.currentCircle = 0;
			
			this.ticksSpent = 0;
			this.canJump = true;
			this.distanceToNextCircle = null;
			
			this.vechile.setPassenger(this.player);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void tick()
	{
		Vector velocity = this.player.getLocation().getDirection().normalize().multiply(this.track.getSpeed());
		velocity.setY(this.vechile.getVelocity().getY());
		
		if (this.jump && this.canJump)
		{
			this.canJump = false;
			
			velocity.setY(Math.min(this.track.getMaxY(), velocity.getY() + this.track.getJumpForce()));
		}
		else
		{
			velocity.setY(Math.max(this.track.getMinY(), velocity.getY() - this.track.getGravity()));
		}
		
		EntityNMS.lookAt((LivingEntity)this.vechile, this.player.getLocation().getYaw(), this.player.getLocation().getPitch());
		
		this.vechile.setVelocity(velocity);
		
		if (this.vechile.isOnGround() || (this.vechile.getLocation().getBlock() != null && this.vechile.getLocation().getBlock().getType() != Material.AIR))
		{
			LobbyPlugin.getPlugin().getChickenRaceManager().leaveRace(this.player);
			
			this.player.sendTitle("Hupsista!", "");
			return;
		}
		
		for(int i = this.currentCircle; i < this.track.getPath().size(); i++) //Show the next three circles
		{
			if (i >= this.currentCircle + this.track.getPathRenderDistance())
			{
				break;
			}
			
			ChickenRaceCircle nextCircleLoc = this.track.getPath().get(i);
			
			if (this.currentCircle == i)
			{
				if (Math.abs(this.vechile.getLocation().getY() - nextCircleLoc.getY()) <= 0.5 && Math.pow(this.vechile.getLocation().getX() - nextCircleLoc.getX(), 2) + Math.pow(this.vechile.getLocation().getZ() - nextCircleLoc.getZ(), 2) < Math.pow(nextCircleLoc.getRadius(), 2))
				{
					this.distanceToNextCircle = null;
					this.currentCircle++;
					continue;
				}
			}
			
			if (this.player.getWorld().equals(nextCircleLoc.getWorld()))
			{
				nextCircleLoc.spawnParticlesForPlayer(this.player);
			}
		}
		
		this.ticksSpent++;
		
		long currentTime = TimeUnit.MILLISECONDS.toNanos(this.ticksSpent * 50);
		
		PlayerNMS.sendActionBar(this.player, ChatColor.BLUE + "Bawk Bawk" + ChatColor.RESET + " - " + this.track.getName() + " - " + ChatColor.YELLOW + TimeUtils.getHumanReadableDataFromNanos(currentTime) + ChatColor.RESET + " - " + ChatColor.GREEN + this.currentCircle + " / " + ChatColor.RED + this.track.getPath().size());
		
		if (this.currentCircle >= this.track.getPath().size())
		{
			LobbyPlugin.getPlugin().getChickenRaceManager().leaveRace(this.player);
			
			this.player.sendTitle("Suoritit radan!", "");
			this.player.sendMessage("Suoritit radan ajalla: " + TimeUtils.getHumanReadableDataFromNanos(currentTime));
			
			LobbyPlugin.getPlugin().getServer().getScheduler().runTaskAsynchronously(LobbyPlugin.getPlugin(), () ->
			{
				try (Connection connection = ((PostgreSqlStorage)ParinaCore.getApi().getStorageManager().getStorage()).getConnection(); Statement statement = connection.createStatement())
				{
					if (statement.executeUpdate("INSERT INTO lobby.chicken_race_stats(user_id, track, time) SELECT id, '" + this.track.getName() + "', " + currentTime + " FROM base.users WHERE uuid = '" + this.player.getUniqueId() + "'") > 0)
					{
						return;
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				
				this.player.sendMessage("Failed to save your score, lol!");
			});
		}
		else
		{
			long now = System.nanoTime();
			if (now - this.lastCheckDistanceToNextCricle >= ChickenRacePlayer.CHECK_DISTANCE_TO_NEXT_CIRCLE_INTERVAL)
			{
				this.lastCheckDistanceToNextCricle = now;
				
				ChickenRaceCircle nextCircle = this.track.getPath().get(this.currentCircle);
				if (nextCircle.getWorld().equals(nextCircle.getWorld()))
				{
					float currentDistance = (float)this.vechile.getLocation().distance(new Location(nextCircle.getWorld(), nextCircle.getX(), nextCircle.getY(), nextCircle.getZ()));
					
					if (this.distanceToNextCircle != null)
					{
						if (this.distanceToNextCircle < currentDistance)
						{
							if (currentDistance - this.distanceToNextCircle >= this.track.getTooFarAwayThreshold())
							{
								LobbyPlugin.getPlugin().getChickenRaceManager().leaveRace(this.player);
								
								this.player.sendTitle("Harhauduit radalta", "");
							}
						}
						else
						{
							this.distanceToNextCircle = currentDistance;
						}
					}
					else
					{
						this.distanceToNextCircle = currentDistance;
					}
				}
			}
		}
	}
	
	public void leave()
	{
		if (this.vechile.isValid())
		{
			this.vechile.remove();
		}
		
		if (this.playing)
		{
			this.playing = false;
			
			LobbyPlugin.getPlugin().getServer().getScheduler().runTask(LobbyPlugin.getPlugin(), () -> this.player.teleport(this.exitLocation)); //Avoid crash
		}
	}
}

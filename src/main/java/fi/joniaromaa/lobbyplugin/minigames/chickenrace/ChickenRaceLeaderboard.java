package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Location;

import fi.joniaromaa.parinacorelibrary.api.ParinaCore;
import fi.joniaromaa.parinacorelibrary.bukkit.holograms.dynamic.LeaderboardHologram;
import fi.joniaromaa.parinacorelibrary.common.storage.types.PostgreSqlStorage;
import fi.joniaromaa.parinacorelibrary.common.utils.TimeUtils;
import net.md_5.bungee.api.ChatColor;

public class ChickenRaceLeaderboard extends LeaderboardHologram
{
	private String track;
	
	public ChickenRaceLeaderboard(Location location, String track)
	{
		super(ChatColor.BLUE + "Bawk Bawk", location);
		
		this.track = track;
	}

	@Override
	public void requestUpdate()
	{
		this.cached.clear();
		
		try (Connection connection = ((PostgreSqlStorage)ParinaCore.getApi().getStorageManager().getStorage()).getConnection(); Statement statement = connection.createStatement())
		{
			try (ResultSet result = statement.executeQuery("SELECT s.uuid, MIN(s.time) OVER(PARTITION BY s.user_id) AS time FROM(SELECT DISTINCT ON(s.user_id) s.user_id, u.uuid, MIN(s.time) OVER(PARTITION BY s.user_id) AS time, s.timestamp FROM lobby.chicken_race_stats s RIGHT JOIN base.users u ON u.id = s.user_id WHERE track = '" + this.track + "' ORDER BY s.user_id, MIN(s.time) OVER(PARTITION BY s.user_id), s.timestamp) AS s ORDER BY MIN(s.time) OVER(PARTITION BY s.user_id), s.timestamp"))
			{
				while (result.next())
				{
					this.cached.put(UUID.fromString(result.getString("uuid")), result.getLong("time"));
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		super.requestUpdate();
	}
	
	protected String formatScore(long score)
	{
		return TimeUtils.getHumanReadableDataFromNanos(score);
	}
}

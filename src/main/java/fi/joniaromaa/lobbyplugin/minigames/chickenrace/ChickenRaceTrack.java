package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import lombok.Getter;

public class ChickenRaceTrack
{
	@Getter private boolean enabled;
	@Getter private String name;
	
	@Getter private Vector launchVelocity;
	@Getter private float speed;
	
	@Getter private float maxY;
	@Getter private float minY;
	
	@Getter private float jumpForce;
	@Getter private float gravity;
	
	@Getter private int tooFarAwayThreshold;
	@Getter private int pathRenderDistance;
	
	@Getter private LinkedList<ChickenRaceCircle> path;
	
	public ChickenRaceTrack(ConfigurationSection configSection)
	{
		this.path = new LinkedList<>();
		
		this.enabled = configSection.getBoolean("enabled");
		this.name = configSection.getString("name");
		
		String[] launchVelocity = configSection.getString("launch-velocity").trim().split("\\s*,\\s*");
		this.launchVelocity = new Vector(Double.parseDouble(launchVelocity[0]), Double.parseDouble(launchVelocity[1]), Double.parseDouble(launchVelocity[2]));
		
		this.speed = (float)configSection.getDouble("speed");
		
		this.maxY = (float)configSection.getDouble("max-y");
		this.minY = (float)configSection.getDouble("min-y");
		
		this.jumpForce = (float)configSection.getDouble("jump-force");
		this.gravity = (float)configSection.getDouble("gravity");
		
		this.tooFarAwayThreshold = configSection.getInt("too-far-away-threshold");
		this.pathRenderDistance = configSection.getInt("path-render-distance");
		
		List<String> path = configSection.getStringList("path");
		if (path != null)
		{
			for(String path_ : path)
			{
				this.path.add(new ChickenRaceCircle(path_));
			}
		}
	}
}

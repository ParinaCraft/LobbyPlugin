package fi.joniaromaa.lobbyplugin.minigames.chickenrace;

import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.lobbyplugin.utils.CircleUtils;
import lombok.Getter;

public class ChickenRaceCircle
{
	@Getter private final World world;
	
	@Getter private final float x;
	@Getter private final float y;
	@Getter private final float z;
	
	@Getter private final float rotX;
	@Getter private final float rotY;
	@Getter private final float rotZ;
	
	@Getter private final float radius;
	
	@Getter private Effect effect;
	
	public ChickenRaceCircle(String data)
	{
		String[] data_ = data.trim().split("\\s*;\\s*");
		
		this.world = LobbyPlugin.getPlugin().getServer().getWorld(data_[0]);
		
		String[] pos = data_[1].split("\\s*,\\s*");
		this.x = Float.parseFloat(pos[0]);
		this.y = Float.parseFloat(pos[1]);
		this.z = Float.parseFloat(pos[2]);
		
		String[] rot = data_[2].split("\\s*,\\s*");
		this.rotX = (float)Math.toRadians(Float.parseFloat(rot[0]));
		this.rotY = (float)Math.toRadians(Float.parseFloat(rot[1]));
		this.rotZ = (float)Math.toRadians(Float.parseFloat(rot[2]));
		
		this.radius = Float.parseFloat(data_[3]);
		
		this.effect = Effect.valueOf(data_[4]);
	}
	
	public ChickenRaceCircle(World world, float x, float y, float z, float rotX, float rotY, float rotZ, float radius, Effect effect)
	{
		this.world = world;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		
		this.radius = radius;
		
		this.effect = effect;
	}
	
	public void spawnParticlesForPlayer(Player player)
	{
		CircleUtils.spawnCircleParticlesToPlayer(player, this.getWorld(), this.getX(), this.getY(), this.getZ(), this.getRadius(), 30, this.getRotX(), this.getRotY(), this.getRotZ(), this.getEffect(), 1);
	}
}

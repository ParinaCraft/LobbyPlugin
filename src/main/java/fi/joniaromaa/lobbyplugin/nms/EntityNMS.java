package fi.joniaromaa.lobbyplugin.nms;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class EntityNMS
{
	private static Field pathfinderTaskEntries;
	private static Field pathfinderExecutingTaskEntries;
	
	static
	{
		try
		{
			EntityNMS.pathfinderTaskEntries = PathfinderGoalSelector.class.getDeclaredField("b");
			EntityNMS.pathfinderTaskEntries.setAccessible(true);
			
			EntityNMS.pathfinderExecutingTaskEntries = PathfinderGoalSelector.class.getDeclaredField("c");
			EntityNMS.pathfinderExecutingTaskEntries.setAccessible(true);
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void removeAI(org.bukkit.entity.LivingEntity living) throws IllegalArgumentException, IllegalAccessException
	{
		EntityNMS.removeAI((EntityInsentient)((CraftLivingEntity)living).getHandle());
	}
	
	@SuppressWarnings("unchecked")
	public static void removeAI(EntityInsentient insentient) throws IllegalArgumentException, IllegalAccessException
	{
		((List<Object>)EntityNMS.pathfinderTaskEntries.get(insentient.goalSelector)).clear();
		((List<Object>)EntityNMS.pathfinderExecutingTaskEntries.get(insentient.targetSelector)).clear();
	}
	
	public static void lookAt(org.bukkit.entity.LivingEntity living, float yaw, float pitch)
	{
		EntityInsentient insentient = (EntityInsentient)((CraftLivingEntity)living).getHandle();
		
		insentient.yaw = yaw;
		insentient.pitch = pitch;
	}
	
	public static org.bukkit.entity.Entity spawnEntity(Entity entity, org.bukkit.Location location)
	{
		entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		org.bukkit.entity.Entity bukkitEntity = ((CraftWorld)location.getWorld()).addEntity(entity, SpawnReason.CUSTOM);
		
		return bukkitEntity;
	}
}

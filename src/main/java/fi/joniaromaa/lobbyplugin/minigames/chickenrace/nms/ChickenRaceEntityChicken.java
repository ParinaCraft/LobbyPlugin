package fi.joniaromaa.lobbyplugin.minigames.chickenrace.nms;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import fi.joniaromaa.lobbyplugin.nms.EntityNMS;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.World;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChickenRaceEntityChicken extends EntityChicken
{
	static
	{
		try
		{
			Field c = EntityTypes.class.getDeclaredField("c");
			Field d = EntityTypes.class.getDeclaredField("d");
			Field f = EntityTypes.class.getDeclaredField("f");
			
			c.setAccessible(true);
			d.setAccessible(true);
			f.setAccessible(true);
			
	        ((Map)c.get(null)).put("Chicken", ChickenRaceEntityChicken.class);
	        ((Map)d.get(null)).put(ChickenRaceEntityChicken.class, "Chicken");
	        ((Map)f.get(null)).put(ChickenRaceEntityChicken.class, Integer.valueOf(93));
		}
		catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e)
		{
			e.printStackTrace();
		}
	}
	
	public ChickenRaceEntityChicken(org.bukkit.World world) throws IllegalArgumentException, IllegalAccessException
	{
		super(((CraftWorld)world).getHandle());
		
		this.strip();
	}
	
	public ChickenRaceEntityChicken(World world) throws IllegalArgumentException, IllegalAccessException
	{
		super(world);
		
		this.strip();
	}
	
	private void strip() throws IllegalArgumentException, IllegalAccessException
	{
		EntityNMS.removeAI(this);
		
		this.bs = Integer.MAX_VALUE; //Egg timer
		this.persistent = true;
		this.ageLocked = true;
		this.setAge(0);
		this.setInvisible(true);
	}
	
	@Override
    protected String z() //Living sound
    {
    	return null;
    }
	
	@Override
	protected void a(BlockPosition blockposition, Block block) //Step sound
	{
		
	}
	
	@Override
    protected Item getLoot()
    {
        return null;
    }

	@Override
    protected void dropDeathLoot(boolean flag, int i)
    {
    	
    }

	@Override
	public boolean d(ItemStack itemstack) //Is breeding item
	{
		return false;
	}
	
	@Override
	protected int getExpValue(EntityHuman entityhuman)
	{
		return 0;
	}
}

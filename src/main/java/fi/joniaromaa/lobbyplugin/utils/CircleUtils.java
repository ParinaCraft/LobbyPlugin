package fi.joniaromaa.lobbyplugin.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CircleUtils
{
    @SuppressWarnings("deprecation")
	public static void spawnCircleParticlesToPlayer(Player player, World world, float x, float y, float z, float radius, int points, float rotX, float rotY, float rotZ, Effect effect, int amount)
    {
    	Location origin = new Location(world, x, y, z);
    	
        for (int i = 0; i < points; i ++)
        {
            double angle = i * 2 * Math.PI / points;
            Vector point = new Vector(radius * Math.cos(angle), 0, radius * Math.sin(angle));
            rotX(point, rotX);
            rotY(point, rotY);
            rotZ(point, rotZ);
            origin.add(point);

            player.playEffect(origin, effect, amount);
            
            origin.subtract(point);
        }
    }
 
    public static void rotX(Vector point, double angle)
    {
        double y = point.getY();
        
        point.setY(y * Math.cos(angle) - point.getZ() * Math.sin(angle));
        point.setZ(y * Math.sin(angle) + point.getZ() * Math.cos(angle));
    }
 
    public static void rotY(Vector point, double angle)
    {
        double z = point.getZ();
        
        point.setZ(z * Math.cos(angle) - point.getX() * Math.sin(angle));
        point.setX(z * Math.sin(angle) + point.getX() * Math.cos(angle));
    }
 
    public static void rotZ(Vector point, double angle)
    {
        double x = point.getX();
        
        point.setX(x * Math.cos(angle) - point.getY() * Math.sin(angle));
        point.setY(x * Math.sin(angle) + point.getY() * Math.cos(angle));
    }
}

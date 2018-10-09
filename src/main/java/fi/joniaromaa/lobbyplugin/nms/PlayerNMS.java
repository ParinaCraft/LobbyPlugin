package fi.joniaromaa.lobbyplugin.nms;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class PlayerNMS
{
	public static void sendActionBar(org.bukkit.entity.Player player, String message)
	{
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(new ChatComponentText(message), (byte)2));
	}
}

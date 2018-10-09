package fi.joniaromaa.lobbyplugin.guis;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.parinacorelibrary.bukkit.builder.ItemStackBuilder;
import fi.joniaromaa.parinacorelibrary.bukkit.inventory.PlayerInterfaceInventory;
import net.md_5.bungee.api.ChatColor;

public class SelectDuelsModePlayerInterfaceInventory extends PlayerInterfaceInventory
{
	@SuppressWarnings("deprecation")
	public SelectDuelsModePlayerInterfaceInventory()
	{
		super(LobbyPlugin.getPlugin(), 27, ChatColor.AQUA + "Valitse pelimuoto");
		
		this.setItem(11, ItemStackBuilder.builder()
				.type(Material.IRON_SWORD)
				.displayName(ChatColor.YELLOW + "Classic")
				.build(), this::joinClassic);
		
		this.setItem(13, ItemStackBuilder.builder()
				.type(Material.EYE_OF_ENDER)
				.displayName(ChatColor.YELLOW + "Skywars")
				.build(), this::joinSkywars);
		
		this.setItem(15, ItemStackBuilder.builder()
				.type(Material.GOLDEN_APPLE)
				.data((byte)1)
				.displayName(ChatColor.YELLOW + "Combo")
				.build(), this::joinCombo);
	}
	
	public void joinClassic(InventoryClickEvent event)
	{
		HumanEntity human = event.getWhoClicked();
		
		this.requestGameMode(human, "duels_1v1_classic");
	}
	
	public void joinSkywars(InventoryClickEvent event)
	{
		HumanEntity human = event.getWhoClicked();
		
		this.requestGameMode(human, "duels_1v1_skywars");
	}
	
	public void joinCombo(InventoryClickEvent event)
	{
		HumanEntity human = event.getWhoClicked();
		
		this.requestGameMode(human, "duels_1v1_combo");
	}
	
	private void requestGameMode(HumanEntity human, String name)
	{
		if (human instanceof Player)
		{
			this.requestGameMode((Player)human, name);
		}
	}
	
	private void requestGameMode(Player player, String name)
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(name);
		
		player.sendPluginMessage(LobbyPlugin.getPlugin(), "Queue", out.toByteArray());
		
		this.close(player);
	}
}

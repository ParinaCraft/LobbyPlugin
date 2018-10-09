package fi.joniaromaa.lobbyplugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

import fi.joniaromaa.lobbyplugin.commands.ChickenRaceCommands;
import fi.joniaromaa.lobbyplugin.config.LobbyConfig;
import fi.joniaromaa.lobbyplugin.listeners.ChickenRaceListener;
import fi.joniaromaa.lobbyplugin.listeners.NPCHuntingListener;
import fi.joniaromaa.lobbyplugin.listeners.NPCListener;
import fi.joniaromaa.lobbyplugin.listeners.PlayerListener;
import fi.joniaromaa.lobbyplugin.listeners.protocollib.SteerVehicleListener;
import fi.joniaromaa.lobbyplugin.minigames.chickenrace.ChickenRaceManager;
import lombok.Getter;

public class LobbyPlugin extends JavaPlugin
{
	@Getter private static LobbyPlugin plugin;
	
	@Getter private LobbyConfig pluginConfig;
	@Getter private ChickenRaceManager chickenRaceManager;
	
	public LobbyPlugin()
	{
		LobbyPlugin.plugin = this;
	}
	
	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		
		this.pluginConfig = new LobbyConfig(this.getConfig());
		if (this.pluginConfig == null || !this.pluginConfig.isEnabled())
		{
			return;
		}
		
		this.getCommand("chickenrace").setExecutor(new ChickenRaceCommands());
		
		if (this.pluginConfig.getMinigamesConfig() != null)
		{
			if (this.pluginConfig.getMinigamesConfig().getChickenRaceConfig() != null && this.pluginConfig.getMinigamesConfig().getChickenRaceConfig().isEnabled())
			{
				this.chickenRaceManager = new ChickenRaceManager(this.pluginConfig.getMinigamesConfig().getChickenRaceConfig());
				this.chickenRaceManager.onEnable();
				
				if (this.pluginConfig.getMinigamesConfig().getChickenRaceConfig().getTracks().size() > 0)
				{
					this.getServer().getPluginManager().registerEvents(new ChickenRaceListener(), this);
					
					this.getServer().getScheduler().runTaskTimer(this, this.chickenRaceManager.getUpdateRunnable(), 1L, 1L);
				}
				
				ProtocolLibrary.getProtocolManager().addPacketListener(new SteerVehicleListener());
			}
			
			if (this.getServer().getPluginManager().isPluginEnabled("Citizens"))
			{
				if (this.pluginConfig.getMinigamesConfig().getNpcHunting() != null && this.pluginConfig.getMinigamesConfig().getNpcHunting().isEnabled())
				{
					this.getServer().getPluginManager().registerEvents(new NPCHuntingListener(), this);
				}
			}
		}

		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new NPCListener(), this);
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "Queue");
	}
	
	@Override
	public void onDisable()
	{
		if (this.pluginConfig == null || !this.pluginConfig.isEnabled())
		{
			return;
		}
		
		if (this.chickenRaceManager != null)
		{
			this.chickenRaceManager.onDisable();
		}
	}
}

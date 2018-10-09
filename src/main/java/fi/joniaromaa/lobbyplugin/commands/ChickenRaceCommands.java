package fi.joniaromaa.lobbyplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fi.joniaromaa.lobbyplugin.LobbyPlugin;
import fi.joniaromaa.lobbyplugin.minigames.chickenrace.ChickenRaceCircle;
import fi.joniaromaa.lobbyplugin.utils.StringUtils;

public class ChickenRaceCommands implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			
			if (LobbyPlugin.getPlugin().getChickenRaceManager() != null)
			{
				if (args.length >= 1)
				{
					switch(args[0])
					{
						case "circlepreview":
							{
								new ChickenRaceCircle(StringUtils.join(" ", args, 1)).spawnParticlesForPlayer(player);;
							}
							break;
						default:
							{
								player.sendMessage("No sub command found!");
							}
							break;
					}
				}
				else
				{
					player.sendMessage("Please enter sub command!");
				}
			}
			else
			{
				player.sendMessage("Chicken race game is disabled at the moment!");
			}
		}
		else
		{
			sender.sendMessage("Only players may use this command!");
		}
		
		return true;
	}
}

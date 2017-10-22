package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.network.PacketDispatcher.XrayAction;
import com.mrnobody.morecommands.network.PacketDispatcher.XrayPresetAction;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
		name = "xray",
		description = "command.xray.description",
		example = "command.xray.example",
		syntax = "command.xray.syntax",
		videoURL = "command.xray.videoURL"
		)
public class CommandXray extends StandardCommand implements ServerCommandProperties {

	@Override
	public String getCommandName() {
		return "xray";
	}

	@Override
	public String getCommandUsage() {
		return "command.xray.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityPlayerMP player = getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class);
		
		if (params.length > 0) {
			if (params[0].equalsIgnoreCase("config")) 
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayAction(player, XrayAction.SHOW_CONF);
			else if (params[0].equalsIgnoreCase("radius") && params.length > 1) {
				int radius;
				try {radius = Math.max(0, Math.min(100, Integer.parseInt(params[1])));}
				catch (NumberFormatException nfe) {throw new CommandException("command.generic.invalidUsage", sender, this.getCommandName());}
				
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayChangeSettings(player, radius);
			}
			else if (params[0].equalsIgnoreCase("enable") || params[0].equalsIgnoreCase("on") || params[0].equalsIgnoreCase("1") || params[0].equalsIgnoreCase("true"))
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayChangeSettings(player, true);
			else if (params[0].equalsIgnoreCase("disable") || params[0].equalsIgnoreCase("off") || params[0].equalsIgnoreCase("0") || params[0].equalsIgnoreCase("false"))
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayChangeSettings(player, false);
			else if (params[0].equals("reset"))
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayAction(player, XrayAction.RESET);
			else if (params[0].equalsIgnoreCase("load") && params.length > 1)
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayChangePreset(player, XrayPresetAction.LOAD, params[1]);
			else if (params[0].equalsIgnoreCase("load_combined") && params.length > 1)
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayChangePreset(player, XrayPresetAction.LOAD_COMBINED, params[1]);
			else if (params[0].equalsIgnoreCase("save") && params.length > 1)
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayChangePreset(player, XrayPresetAction.SAVE, params[1]);
			else if (params[0].equalsIgnoreCase("delete") && params.length > 1)
				MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayChangePreset(player, XrayPresetAction.DELETE, params[1]);
			else throw new CommandException("command.generic.invalidUsage", sender, this.getCommandName());
		}
		else MoreCommands.INSTANCE.getPacketDispatcher().sendS05XrayAction(player, XrayAction.TOGGLE);
		
		return null;
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[] {CommandRequirement.MODDED_CLIENT};
	}
	
	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public int getDefaultPermissionLevel(String[] args) {
		return 2;
	}
	
	@Override
	public boolean canSenderUse(String commandName, ICommandSender sender, String[] params) {
		return isSenderOfEntityType(sender, EntityPlayerMP.class);
	}
}

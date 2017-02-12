package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.settings.ServerPlayerSettings;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
		name = "freecam",
		description = "command.freecam.description",
		example = "command.freecam.example",
		syntax = "command.freecam.syntax",
		videoURL = "command.freecam.videoURL"
		)
public class CommandFreecam extends StandardCommand implements ServerCommandProperties {
	@Override
	public String getCommandName() {
		return "freecam";
	}

	@Override
	public String getCommandUsage() {
		return "command.freecam.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		ServerPlayerSettings settings = getPlayerSettings(getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class));
		
		if (settings.freecam) {
			settings.freecam = false;
			sender.sendLangfileMessage("command.freecam.off");
		}
		else {
			settings.freecam = true;
            sender.sendLangfileMessage("command.freecam.on");
		}
		
		MoreCommands.INSTANCE.getPacketDispatcher().sendS03Freecam(getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class));
		return null;
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[] {CommandRequirement.MODDED_CLIENT, CommandRequirement.PATCH_ENTITYCLIENTPLAYERMP};
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

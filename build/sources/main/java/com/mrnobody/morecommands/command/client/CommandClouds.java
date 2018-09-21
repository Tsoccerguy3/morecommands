package com.mrnobody.morecommands.command.client;

import com.mrnobody.morecommands.command.ClientCommandProperties;
import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;

import net.minecraft.client.Minecraft;

@Command(
		name = "clouds",
		description = "command.clouds.description",
		example = "command.clouds.example",
		syntax = "command.clouds.syntax",
		videoURL = "command.clouds.videoURL"
		)
public class CommandClouds extends StandardCommand implements ClientCommandProperties {
	@Override
	public String getCommandName() {
		return "clouds";
	}

	@Override
	public String getCommandUsage() {
		return "command.clouds.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		try {Minecraft.getMinecraft().gameSettings.clouds = parseTrueFalse(params, 0, Minecraft.getMinecraft().gameSettings.clouds == 0) ? 2 : 0;}
		catch (IllegalArgumentException ex) {throw new CommandException("command.clouds.failure", sender);}
		
		sender.sendLangfileMessage(Minecraft.getMinecraft().gameSettings.clouds != 0 ? "command.clouds.on" : "command.clouds.off");
		return null;
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[0];
	}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public boolean registerIfServerModded() {
		return true;
	}
	
	@Override
	public int getDefaultPermissionLevel(String[] args) {
		return 0;
	}
}

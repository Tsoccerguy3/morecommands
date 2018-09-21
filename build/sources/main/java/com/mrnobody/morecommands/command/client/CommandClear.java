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
		name = "clear",
		description = "command.clear.description",
		example = "command.clear.example",
		syntax = "command.clear.syntax",
		videoURL = "command.clear.videoURL"
		)
public class CommandClear extends StandardCommand implements ClientCommandProperties {

	@Override
	public String getCommandName() {
		return "clear";
	}

	@Override
	public String getCommandUsage() {
		return "command.clear.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages(false);
		return null;
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[] {};
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

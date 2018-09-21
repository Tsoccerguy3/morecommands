package com.mrnobody.morecommands.command.client;

import java.util.Arrays;

import com.mrnobody.morecommands.command.ClientCommand;
import com.mrnobody.morecommands.command.ClientCommandProperties;
import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;

@Command(
		name = "help",
		description = "command.helpSideClient.description",
		example = "command.helpSideClient.example",
		syntax = "command.helpSideClient.syntax",
		videoURL = "command.helpSideClient.videoURL"
		)
public class CommandHelp extends StandardCommand implements ClientCommandProperties {
	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public String getCommandUsage() {
		return "command.helpSideClient.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		if (!ClientCommand.clientCommandsEnabled) {
			String args = "";
			for (String param : params) args += " " + param;
			Minecraft.getMinecraft().player.sendChatMessage("/help" + args);
			return null;
		}
		
		if (params.length > 0) {
			String args = "";
			if (params.length > 1) for (String param : Arrays.copyOfRange(params, 1, params.length)) args += " " + param;
			
			if (params[0].equalsIgnoreCase("client")) ClientCommandHandler.instance.executeCommand(sender.getMinecraftISender(), "chelp" + args);
			else if (params[0].equalsIgnoreCase("server")) ClientCommandHandler.instance.executeCommand(sender.getMinecraftISender(), "shelp" + args);
			else throw new CommandException("command.helpSideClient.info", sender);
		}
		else throw new CommandException("command.helpSideClient.info", sender);
		
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

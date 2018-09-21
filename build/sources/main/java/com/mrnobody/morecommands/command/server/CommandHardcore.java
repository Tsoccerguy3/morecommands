package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.util.WorldUtils;

import net.minecraft.command.ICommandSender;

@Command(
		name = "hardcore",
		description = "command.hardcore.description",
		example = "command.hardcore.example",
		syntax = "command.hardcore.syntax",
		videoURL = "command.hardcore.videoURL"
		)
public class CommandHardcore extends StandardCommand implements ServerCommandProperties {

	@Override
    public String getCommandName()
    {
        return "hardcore";
    }

	@Override
    public String getCommandUsage()
    {
        return "command.hardcore.syntax";
    }
    
	@Override
    public String execute(CommandSender sender, String[] params) throws CommandException {
		try {WorldUtils.setHardcore(sender.getWorld(), parseTrueFalse(params, 0, !WorldUtils.isHardcore(sender.getWorld())));}
		catch (IllegalArgumentException ex) {throw new CommandException("command.hardcore.failure", sender);}
		
		sender.sendLangfileMessage(WorldUtils.isHardcore(sender.getWorld()) ? "command.hardcore.on" : "command.hardcore.off");  
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
	public int getDefaultPermissionLevel(String[] args) {
		return 2;
	}
	
	@Override
	public boolean canSenderUse(String commandName, ICommandSender sender, String[] params) {
		return true;
	}
}

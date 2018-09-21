package com.mrnobody.morecommands.command.server;

import java.util.HashMap;
import java.util.Map;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Command(
		name = "command",
		description = "command.command.description",
		example = "command.command.example",
		syntax = "command.command.syntax",
		videoURL = "command.command.videoURL"
		)
public class CommandCommand extends StandardCommand implements ServerCommandProperties {
	private Map<String, ICommand> disabledCommands = new HashMap<String, ICommand>();
	
	@Override
    public String getCommandName()
    {
        return "command";
    }

	@Override
    public String getCommandUsage()
    {
        return "command.command.syntax";
    }

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		if (params.length > 1) {
			if (params[0].equalsIgnoreCase("enable")) {
				ICommand enable = this.disabledCommands.get(params[1]);
				
				if (enable != null) {
					((ServerCommandManager) sender.getServer().getCommandManager()).registerCommand(enable);
					this.disabledCommands.remove(params[1]);
					sender.sendLangfileMessage("command.command.enabled");
				}
				else throw new CommandException("command.command.alreadyEnabled", sender);
			}
			else if (params[0].equalsIgnoreCase("disable")) {
				if (params[1].equals(this.getCommandName())) throw new CommandException("command.command.wantedToDisable", sender);
				
				ICommand disable = (ICommand) ((ServerCommandManager) sender.getServer().getCommandManager()).getCommands().get(params[1]);
				
				if (disable != null) {
					this.disabledCommands.put(disable.getName(), disable);
					((ServerCommandManager) sender.getServer().getCommandManager()).getCommands().remove(params[1]);
					sender.sendLangfileMessage("command.command.disabled");
				}
				else throw new CommandException("command.command.alreadyDisabled", sender);
			}
			else throw new CommandException("command.generic.invalidUsage", sender, this.getCommandName());
		}
		else throw new CommandException("command.generic.invalidUsage", sender, this.getCommandName());
		
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

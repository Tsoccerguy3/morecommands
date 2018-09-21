package com.mrnobody.morecommands.command.client;

import java.util.Arrays;

import com.mrnobody.morecommands.command.ClientCommandProperties;
import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.MultipleCommands;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.event.EventHandler;
import com.mrnobody.morecommands.event.Listeners.EventListener;
import com.mrnobody.morecommands.settings.ClientPlayerSettings;
import com.mrnobody.morecommands.settings.MoreCommandsConfig;
import com.mrnobody.morecommands.util.DummyCommand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.event.CommandEvent;

@Command.MultipleCommand(
		name = {"alias", "unalias"},
		description = {"command.alias.description", "command.unalias.description"},
		example = {"command.alias.example", "command.unalias.example"},
		syntax = {"command.alias.syntax", "command.unalias.syntax"},
		videoURL = {"command.alias.videoURL", "command.unalias.videoURL"}
		)
public class CommandAlias extends MultipleCommands implements ClientCommandProperties, EventListener<CommandEvent> {
	public CommandAlias(int typeIndex) {
		super(typeIndex);
	}
	
	public CommandAlias() {
		super();
		EventHandler.COMMAND.register(this);
	}
	
	@Override
	public void onEvent(CommandEvent event) {
		if (event.getCommand() instanceof DummyCommand && ((DummyCommand) event.getCommand()).isClient()) {
			String command = null;
			
			if (MoreCommandsConfig.enablePlayerAliases && isSenderOfEntityType(event.getSender(), EntityPlayerSP.class))
				command = getPlayerSettings(getSenderAsEntity(event.getSender(), EntityPlayerSP.class)).aliases.get(event.getCommand().getName());
			
			if (command != null) executeCommand(command + " " + rejoinParams(event.getParameters()), true);
			else executeCommand(event.getCommand().getName() + " " + rejoinParams(event.getParameters()), false);
			
			event.setException(null);
			event.setCanceled(true);
		}
	}
	
    private void executeCommand(String command, boolean tryClient) {
    	if (!command.startsWith("/")) command = "/" + command;
        if (tryClient && ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().player, command.trim()) != 0) return;
        Minecraft.getMinecraft().player.sendChatMessage(command);
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"alias", "unalias"};
	}

	@Override
	public String[] getCommandUsages() {
		return new String[] {"command.alias.syntax", "command.unalias.syntax"};
	}

	@Override
	public String execute(String commandName, CommandSender sender, String[] params) throws CommandException {
		boolean remove = commandName.startsWith("unalias");
		ClientPlayerSettings settings = null;
		
		if (!MoreCommandsConfig.enablePlayerAliases)
			throw new CommandException("command.alias.aliasesDisabled", sender);
		
		if (!isSenderOfEntityType(sender.getMinecraftISender(), EntityPlayerSP.class)) 
			throw new CommandException("command.generic.notAPlayer", sender);
		else 
			settings = getPlayerSettings(getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerSP.class));
		
		if (remove) {
			if (params.length > 0) {
				String alias = params[0];
				ICommand command = (ICommand) ClientCommandHandler.instance.getCommands().get(alias);
				
				if (command != null && command instanceof DummyCommand && settings.aliases.containsKey(alias)) {
					settings.aliases.remove(alias);
					sender.sendLangfileMessage("command.unalias.success");
				}
				else throw new CommandException("command.unalias.notFound", sender);
			}
			else throw new CommandException("command.generic.invalidUsage", sender, this.getCommandName());
		}
		else {
			if (params.length > 1) {
				String alias = params[0];
				String command = params[1];
				String parameters = params.length > 2 ? " " + rejoinParams(Arrays.copyOfRange(params, 2, params.length)) : "";
				
				if (!command.equalsIgnoreCase(alias)) {
					if (ClientCommandHandler.instance.getCommands().get(alias) == null) {
						DummyCommand cmd = new DummyCommand(alias, true);
						ClientCommandHandler.instance.getCommands().put(alias, cmd);
					}
					else if (!(ClientCommandHandler.instance.getCommands().get(alias) instanceof DummyCommand))
						throw new CommandException("command.alias.overwrite", sender);
					
					settings.aliases.put(alias, command + parameters);
					sender.sendLangfileMessage("command.alias.success");
				}
				else throw new CommandException("command.alias.infiniteRecursion", sender);
			}
			else throw new CommandException("command.generic.invalidUsage", sender, this.getCommandName());
		}
		
		return null;
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[] {CommandRequirement.PATCH_CLIENTCOMMANDHANDLER};
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

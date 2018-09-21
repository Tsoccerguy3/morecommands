package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.settings.ServerPlayerSettings;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;

@Command(
	name = "inventory",
	description = "command.inventory.description",
	syntax = "command.inventory.syntax",
	example = "command.inventory.example",
	videoURL = "command.inventory.videoURL"
		)
public class CommandInventory extends StandardCommand implements ServerCommandProperties {
	@Override
	public String getCommandName() {
		return "inventory";
	}

	@Override
	public String getCommandUsage() {
		return "command.inventory.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		if (params.length > 1) {
			ServerPlayerSettings settings = getPlayerSettings(getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class));
			if (settings == null) {throw new CommandException("command.inventory.noSettingsFound", sender);}
			
			if ((params[0].equalsIgnoreCase("delete") || params[0].equalsIgnoreCase("del") || params[0].equalsIgnoreCase("remove") || params[0].equalsIgnoreCase("rem"))) {
				NBTTagList inventory = settings.inventories.get(params[1]);
				if (inventory == null) throw new CommandException("command.inventory.notFound", sender, params[1]);
				
				settings.inventories.remove(params[1]);
				sender.sendLangfileMessage("command.inventory.removeSuccess", params[1]);
			}
			else if (params[0].equalsIgnoreCase("load")) {
				NBTTagList inventory = settings.inventories.get(params[1]);
				if (inventory == null) {throw new CommandException("command.inventory.notFound", sender, params[1]);}
				
				getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class).inventory.readFromNBT(inventory);
				sender.sendLangfileMessage("command.inventory.loadSuccess", params[1]);
			}
			else if (params[0].equalsIgnoreCase("save")) {
				NBTTagList inventory = new NBTTagList();
				getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class).inventory.writeToNBT(inventory);
				
				settings.inventories.put(params[1], inventory);
				sender.sendLangfileMessage("command.inventory.saveSuccess", params[1]);
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
		return 0;
	}
	
	@Override
	public boolean canSenderUse(String commandName, ICommandSender sender, String[] params) {
		return isSenderOfEntityType(sender, EntityPlayerMP.class);
	}
}

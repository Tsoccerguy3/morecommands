package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
		name = "reach",
		description = "command.reach.description",
		example = "command.reach.example",
		syntax = "command.reach.syntax",
		videoURL = "command.reach.videoURL"
		)
public class CommandReach extends StandardCommand implements ServerCommandProperties {
	@Override
	public String getCommandName() {
		return "reach";
	}

	@Override
	public String getCommandUsage() {
		return "command.reach.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityPlayerMP playerEntity = getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class);
			
		if (params.length > 0) {
			try {
				float distance = Float.parseFloat(params[0]);
				MoreCommands.INSTANCE.getPacketDispatcher().sendS08Reach(playerEntity, distance);
				playerEntity.interactionManager.setBlockReachDistance(distance);
				sender.sendLangfileMessage("command.reach.set", params[0]);
			}
			catch (NumberFormatException e) {
				if (params[0].equalsIgnoreCase("reset")) {
					MoreCommands.INSTANCE.getPacketDispatcher().sendS08Reach(playerEntity, 5.0F);
					playerEntity.interactionManager.setBlockReachDistance(5.0F);
					sender.sendLangfileMessage("command.reach.reset");
				}
				else throw new CommandException("command.reach.invalidArg", sender);
			}
		}
		
		return null;
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[] {CommandRequirement.MODDED_CLIENT, CommandRequirement.PATCH_ENTITYPLAYERSP};
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

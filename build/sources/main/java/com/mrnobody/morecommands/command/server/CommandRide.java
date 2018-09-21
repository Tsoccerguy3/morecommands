package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.util.EntityUtils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
		name = "ride",
		description = "command.ride.description",
		example = "command.ride.example",
		syntax = "command.ride.syntax",
		videoURL = "command.ride.videoURL"
		)
public class CommandRide extends StandardCommand implements ServerCommandProperties {

	@Override
	public String getCommandName() {
		return "ride";
	}

	@Override
	public String getCommandUsage() {
		return "command.ride.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityPlayerMP player = getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class);
		Entity hit = EntityUtils.traceEntity(player, 128.0D);
		
		if (player.getRidingEntity() != null) {
			sender.sendLangfileMessage("command.ride.dismounted");
			player.dismountRidingEntity();
			return null;
		}
		
		if (hit != null) {
			if (hit instanceof EntityLiving) {
				player.startRiding(hit, true);
				sender.sendLangfileMessage("command.ride.mounted");
			}
			else sender.sendLangfileMessage("command.ride.notLiving");
		}
		else sender.sendLangfileMessage("command.ride.notFound");
		
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

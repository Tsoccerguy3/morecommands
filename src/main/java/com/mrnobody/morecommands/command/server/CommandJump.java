package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.util.Coordinate;
import com.mrnobody.morecommands.util.EntityUtils;
import com.mrnobody.morecommands.util.WorldUtils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

@Command(
		name = "jump",
		description = "command.jump.description",
		example = "command.jump.example",
		syntax = "command.jump.syntax",
		videoURL = "command.jump.videoURL"
		)
public class CommandJump extends StandardCommand implements ServerCommandProperties {

	@Override
	public String getCommandName() {
		return "jump";
	}

	@Override
	public String getCommandUsage() {
		return "command.jump.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityLivingBase entity = getSenderAsEntity(sender.getMinecraftISender(), EntityLivingBase.class);
		Coordinate hit = EntityUtils.traceBlock(entity, 128D);
		
		if (hit == null) throw new CommandException("command.jump.notInSight", sender);
		else {
			int y = hit.getBlockY() + 1;
			while (y < 260) {
				if (WorldUtils.isClear(entity.worldObj, new Coordinate(hit.getBlockX(), y++, hit.getBlockZ()))) {
					EntityUtils.setPosition(entity, new Coordinate(hit.getBlockX() + 0.5F, --y, hit.getBlockZ() + 0.5F));
					break;
				}
			}
		}
		
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
		return isSenderOfEntityType(sender, net.minecraft.entity.EntityLivingBase.class);
	}
}

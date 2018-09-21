package com.mrnobody.morecommands.command.server;

import java.util.Iterator;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Command(
		name = "defuse",
		description = "command.defuse.description",
		example = "command.defuse.example",
		syntax = "command.defuse.syntax",
		videoURL = "command.defuse.videoURL"
		)
public class CommandDefuse extends StandardCommand implements ServerCommandProperties {

	@Override
	public String getCommandName() {
		return "defuse";
	}

	@Override
	public String getCommandUsage() {
		return "command.defuse.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		double radius = 16;
		
		if (params.length > 0) {
			try {radius = Double.parseDouble(params[0]);}
			catch (NumberFormatException nfe) {throw new CommandException("command.defuse.invalidArg", sender);}
		}
		
		World world = sender.getWorld();
		BlockPos pos = sender.getPosition();
		
		Iterator<EntityTNTPrimed> tntPrimedIterator = world.getEntitiesWithinAABB(EntityTNTPrimed.class, new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius)).iterator();
	
		while (tntPrimedIterator.hasNext()) {
			Entity tntPrimed = tntPrimedIterator.next();
			tntPrimed.setDead();
			
			EntityItem tnt = new EntityItem(world, tntPrimed.posX, tntPrimed.posY, tntPrimed.posZ, new ItemStack(Item.getItemFromBlock(Blocks.TNT), 1));
			world.spawnEntity(tnt);
		}
		
		sender.sendLangfileMessage("command.defuse.defused");
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

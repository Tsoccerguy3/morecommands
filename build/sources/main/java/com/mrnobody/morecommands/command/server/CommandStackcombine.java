package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

@Command(
		name = "stackcombine",
		description = "command.stackcombine.description",
		example = "command.stackcombine.example",
		syntax = "command.stackcombine.syntax",
		videoURL = "command.stackcombine.videoURL"
		)
public class CommandStackcombine extends StandardCommand implements ServerCommandProperties {
	public String getCommandName() {
		return "stackcombine";
	}
    
	public String getCommandUsage() {
		return "command.stackcombine.syntax";
	}
    
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityPlayerMP player = getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class);
		
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack sloti = player.inventory.getStackInSlot(i);
			if (sloti == ItemStack.EMPTY) continue;
			
            for (int j = i + 1; j < player.inventory.getSizeInventory(); j++) {
            	ItemStack slotj = player.inventory.getStackInSlot(i);
            	if (slotj == ItemStack.EMPTY) continue;
            	
            	if (sloti.isItemEqual(slotj)) {
            		if (sloti.getCount() + slotj.getCount() > sloti.getMaxStackSize()) {
            			int noItems = sloti.getCount() + slotj.getCount();
            			sloti.setCount(sloti.getMaxStackSize()); noItems -= sloti.getMaxStackSize();
            			slotj.setCount(noItems > sloti.getMaxStackSize() ? sloti.getMaxStackSize() : noItems);
            			noItems -= sloti.getMaxStackSize();
            		}
            		else {
            			sloti.setCount(sloti.getCount() + slotj.getCount());
            			player.inventory.setInventorySlotContents(j, ItemStack.EMPTY);
            		}
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
		return 0;
	}
	
	@Override
	public boolean canSenderUse(String commandName, ICommandSender sender, String[] params) {
		return isSenderOfEntityType(sender, EntityPlayerMP.class);
	}
}

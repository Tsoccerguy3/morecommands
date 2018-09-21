package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.event.EventHandler;
import com.mrnobody.morecommands.event.Listeners.EventListener;
import com.mrnobody.morecommands.settings.ServerPlayerSettings;
import com.mrnobody.morecommands.util.EntityUtils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Command(
		name = "hunger",
		description = "command.hunger.description",
		example = "command.hunger.example",
		syntax = "command.hunger.syntax",
		videoURL = "command.hunger.videoURL"
		)
public class CommandHunger extends StandardCommand implements ServerCommandProperties, EventListener<TickEvent> {
	public CommandHunger() {
		EventHandler.TICK.register(this);
	}

	@Override
	public void onEvent(TickEvent ev) {
		if (ev instanceof TickEvent.PlayerTickEvent) {
			TickEvent.PlayerTickEvent event = (TickEvent.PlayerTickEvent) ev;
			
			if (event.player instanceof EntityPlayerMP && event.phase == TickEvent.Phase.END) {
				ServerPlayerSettings settings = getPlayerSettings((EntityPlayerMP) event.player);
				if (!settings.hunger) event.player.getFoodStats().setFoodLevel(20);
			}
		}
	}
	
	@Override
	public String getCommandName() {
		return "hunger";
	}

	@Override
	public String getCommandUsage() {
		return "command.hunger.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityPlayerMP player = getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class);
		ServerPlayerSettings settings = getPlayerSettings(player);
		int foodLevel;
		
		if (params.length > 0) {
			try {foodLevel = Integer.parseInt(params[0]);}
			catch (NumberFormatException e) {
				if (params[0].equalsIgnoreCase("min")) {foodLevel = 0;}
				else if (params[0].equalsIgnoreCase("max")) {foodLevel = 20;}
				else if (params[0].equalsIgnoreCase("get")) {sender.sendLangfileMessage("command.hunger.get", EntityUtils.getHunger(player)); return null;}
				else if (params[0].equalsIgnoreCase("enable") || params[0].equalsIgnoreCase("1")
						|| params[0].equalsIgnoreCase("on") || params[0].equalsIgnoreCase("true")) {
						settings.hunger = true;
						sender.sendLangfileMessage("command.hunger.on"); return null;
				}
				else if (params[0].equalsIgnoreCase("disable") || params[0].equalsIgnoreCase("0")
						|| params[0].equalsIgnoreCase("off") || params[0].equalsIgnoreCase("false")) {
						settings.hunger = false;
						sender.sendLangfileMessage("command.hunger.off"); return null;
				}
				else throw new CommandException("command.hunger.invalidParam", sender);
			}
			
			player.getFoodStats().setFoodLevel(foodLevel < 0 ? 0 : foodLevel > 20 ? 20 : foodLevel);
			sender.sendLangfileMessage("command.hunger.success");
		}
        else {
        	settings.hunger = !settings.hunger;
        	sender.sendLangfileMessage(settings.hunger ? "command.hunger.on" : "command.hunger.off");
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
		return isSenderOfEntityType(sender, EntityPlayerMP.class);
	}
}

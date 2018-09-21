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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

@Command(
		name = "effect",
		description = "command.effect.description",
		example = "command.effect.example",
		syntax = "command.effect.syntax",
		videoURL = "command.effect.videoURL"
		)
public class CommandEffect extends StandardCommand implements ServerCommandProperties {
	private static final int PAGE_MAX = 15;
	
	@Override
	public String getCommandName() {
		return "effect";
	}

	@Override
	public String getCommandUsage() {
		return "command.effect.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityLivingBase entity = getSenderAsEntity(sender.getMinecraftISender(), EntityLivingBase.class);
		
		if (params.length > 0) {
    		if(params[0].equals("list")) {
    			int page = 0;
    			ResourceLocation[] potions = Potion.REGISTRY.getKeys().toArray(new ResourceLocation[Potion.REGISTRY.getKeys().size()]);
    			
    			if (params.length > 1) {
    				try {
    					page = Integer.parseInt(params[1]) - 1; 
    					if (page < 0) page = 0;
    					else if (page * PAGE_MAX > potions.length) page = potions.length / PAGE_MAX;
    				}
    				catch (NumberFormatException e) {throw new CommandException("command.effect.NAN", sender);}
    			}
    			
    			final int stop = (page + 1) * PAGE_MAX;;
    			for (int i = page * PAGE_MAX; i < stop && i < potions.length; i++)
    				sender.sendStringMessage(" - '" +  potions[i] + "' (ID " + Potion.getIdFromPotion(Potion.REGISTRY.getObject(potions[i])) + ")");
    			
    			sender.sendLangfileMessage("command.effect.more", TextFormatting.RED);
    		}
    		else if (params[0].equals("remove") && params.length > 1) {
				if (params[1].equalsIgnoreCase("*")) {
					EntityUtils.removeAllPotionEffects(entity); 
					sender.sendLangfileMessage("command.effect.removeAllSuccess");
					return null;
				}
				
				Potion potion = getPotion(params[1]);
				if (potion != null) EntityUtils.removePotionEffect(entity, potion);
				else throw new CommandException("command.effect.notFound", sender);
				
				sender.sendLangfileMessage("command.effect.removeSuccess");
    		}
    		else if (params[0].equals("add") && params.length > 1) {
				int duration = 30;
				int strength = 0;
				boolean invisible = false;
				
				if (params.length > 2) {
					try {duration = Integer.parseInt(params[2]);}
					catch (NumberFormatException e) {throw new CommandException("command.effect.NAN", sender);}
				}
				
				if (params.length > 3) {
					try {strength = Integer.parseInt(params[3]);}
					catch (NumberFormatException e) {throw new CommandException("command.effect.NAN", sender);}
				}
				
				if (params.length > 4)
					invisible = Boolean.parseBoolean(params[4]);
				
				Potion potion = getPotion(params[1]);
				if (potion != null) EntityUtils.addPotionEffect(entity, potion, duration, strength, invisible);
				else throw new CommandException("command.effect.notFound", sender);
				
				sender.sendLangfileMessage("command.effect.addSuccess");
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
		return isSenderOfEntityType(sender, EntityLivingBase.class);
	}
}

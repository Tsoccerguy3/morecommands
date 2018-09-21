package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.settings.MoreCommandsConfig;
import com.mrnobody.morecommands.util.EntityUtils;

import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

@Command(
		name = "enchant",
		description = "command.enchant.description",
		example = "command.enchant.example",
		syntax = "command.enchant.syntax",
		videoURL = "command.enchant.videoURL"
		)
public class CommandEnchant extends StandardCommand implements ServerCommandProperties {
	private static final int PAGE_MAX = 15;
	
	@Override
	public String getCommandName() {
		return "enchant";
	}

	@Override
	public String getCommandUsage() {
		return "command.enchant.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		EntityLivingBase entity = getSenderAsEntity(sender.getMinecraftISender(), EntityLivingBase.class);
		
		if (params.length > 0) {
    		if(params[0].equalsIgnoreCase("list")) {
    			int page = 0;
    			ResourceLocation[] enchantments = Enchantment.REGISTRY.getKeys().toArray(new ResourceLocation[Enchantment.REGISTRY.getKeys().size()]);
    			
    			if (params.length > 1) {
    				try {
    					page = Integer.parseInt(params[1]) - 1; 
    					if (page < 0) page = 0;
    					else if (page * PAGE_MAX > enchantments.length) page = enchantments.length / PAGE_MAX;
    				}
    				catch (NumberFormatException e) {throw new CommandException("command.enchant.NAN", sender);}
    			}
    			
    			final int stop = (page + 1) * PAGE_MAX;;
    			for (int i = page * PAGE_MAX; i < stop && i < enchantments.length; i++)
    				sender.sendStringMessage(" - '" + enchantments[i] + "' (ID " + Enchantment.getEnchantmentID(Enchantment.REGISTRY.getObject(enchantments[i])) + ")");
    			
    			sender.sendLangfileMessage("command.enchant.more", TextFormatting.RED);
    		}
    		else if (params[0].equalsIgnoreCase("remove")) {
    			if (params.length <= 1 || (params.length > 1 && params[1].equalsIgnoreCase("*"))) {
    				EntityUtils.removeEnchantments(entity);
    				sender.sendLangfileMessage("command.enchant.removeAllSuccess");
    			}
    			else if (params.length > 1) {
    				Enchantment e = getEnchantment(params[1]);
    				
    				if (e != null) EntityUtils.removeEnchantment(entity, e);
    				else throw new CommandException("command.enchant.notFound", sender);
    				
    				sender.sendLangfileMessage("command.enchant.removeSuccess");
    			}
    		}
    		else if (params[0].equalsIgnoreCase("add") && params.length > 1) {
 				int level = 1;
				
				if (params.length > 2) {
					try {level = Integer.parseInt(params[2]);}
					catch (NumberFormatException e) {throw new CommandException("command.enchant.NAN", sender);}
				}
				
				Enchantment e = getEnchantment(params[1]);
				
				if (e != null) {
					if (!EntityUtils.addEnchantment(entity, e, level, MoreCommandsConfig.strictEnchanting))
						throw new CommandException("command.enchant.cantApply", sender);
				}
				else throw new CommandException("command.enchant.notFound", sender);
				
				sender.sendLangfileMessage("command.enchant.addSuccess");
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

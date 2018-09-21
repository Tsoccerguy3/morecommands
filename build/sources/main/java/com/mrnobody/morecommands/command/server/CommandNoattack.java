package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.event.EventHandler;
import com.mrnobody.morecommands.event.Listeners.TwoEventListener;
import com.mrnobody.morecommands.settings.ServerPlayerSettings;
import com.mrnobody.morecommands.util.EntityUtils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

@Command(
		name = "noattack",
		description = "command.noattack.description",
		example = "command.noattack.example",
		syntax = "command.noattack.syntax",
		videoURL = "command.noattack.videoURL"
		)
public class CommandNoattack extends StandardCommand implements ServerCommandProperties, TwoEventListener<LivingSetAttackTargetEvent, LivingAttackEvent> {
	public CommandNoattack() {
		EventHandler.SET_TARGET.registerFirst(this);
		EventHandler.ATTACK.registerSecond(this);
	}

	@Override
	public String getCommandName() {
		return "noattack";
	}

	@Override
	public String getCommandUsage() {
		return "command.noattack.syntax";
	}
	
	@Override
	public void onEvent1(LivingSetAttackTargetEvent event) {
		if (event.getTarget() instanceof EntityPlayerMP && event.getEntityLiving() instanceof EntityLiving) {
			if (getPlayerSettings((EntityPlayerMP) event.getTarget()).disableAttacks.contains(event.getTarget().getClass())) {
				((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
				event.getEntityLiving().setRevengeTarget(null);
			}
		}
	}
	
	@Override
	public void onEvent2(LivingAttackEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayerMP && event.getSource().getImmediateSource() instanceof EntityLiving) {
			if (getPlayerSettings((EntityPlayerMP) event.getEntityLiving()).disableAttacks.contains(event.getSource().getImmediateSource().getClass())) {
				((EntityLiving) event.getSource().getImmediateSource()).setAttackTarget(null);
				((EntityLiving) event.getSource().getImmediateSource()).setRevengeTarget(null);
				event.setCanceled(true);
			}
		}
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		ServerPlayerSettings settings = getPlayerSettings(getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class));
    	
		if (params.length > 0) {
			Class<? extends Entity> entityClass = (Class<? extends Entity>) EntityUtils.getEntityClass(new ResourceLocation(params[0]), true);
				
			if (entityClass == null) {
				try {entityClass = EntityUtils.getEntityClass(EntityUtils.getEntityName(Integer.parseInt(params[0])), false);}
				catch (NumberFormatException nfe) {throw new CommandException("command.noattack.unknownEntity", sender);}
			}
			
			if (!EntityLiving.class.isAssignableFrom(entityClass))
				throw new CommandException("command.noattack.notLiving", sender);
			
			if (settings.disableAttacks.contains(entityClass)) {
				settings.disableAttacks.remove(entityClass);
				sender.sendLangfileMessage("command.noattack.removed");
			}
			else {
				settings.disableAttacks.add((Class<? extends EntityLiving>) entityClass);
				sender.sendLangfileMessage("command.noattack.added");
			}
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
		return isSenderOfEntityType(sender, EntityPlayerMP.class);
	}
}

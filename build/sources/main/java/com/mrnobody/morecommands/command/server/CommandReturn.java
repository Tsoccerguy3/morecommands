package com.mrnobody.morecommands.command.server;

import java.text.DecimalFormat;

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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@Command(
		name = "return",
		description = "command.return.description",
		example = "command.return.example",
		syntax = "command.return.syntax",
		videoURL = "command.return.videoURL"
		)
public class CommandReturn extends StandardCommand implements ServerCommandProperties, EventListener<LivingDeathEvent> {
	public CommandReturn() {
		EventHandler.DEATH.register(this);
	}
	
	@Override
	public String getCommandName() {
		return "return";
	}

	@Override
	public String getCommandUsage() {
		return "command.return.syntax";
	}
	
	public void onEvent(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			ServerPlayerSettings settings = getPlayerSettings((EntityPlayerMP) event.getEntityLiving());
			settings.deathpoint = settings.lastPos = new BlockPos(event.getEntityLiving());
		}
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		ServerPlayerSettings settings = getPlayerSettings(getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class));
		BlockPos pos = settings.lastPos;
		
		if (params.length > 0 && params[0].equalsIgnoreCase("deathpoint")) pos = settings.deathpoint;
		else if (params.length > 0 && params[0].equalsIgnoreCase("teleport")) pos = settings.lastTeleport;
		
		if (pos == null) 
			throw new CommandException("command.return.noLastPos", sender);
		
		EntityPlayerMP player = getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class);
		BlockPos currentPos = player.getPosition();
		EntityUtils.setPosition(player, pos);
		settings.lastPos = currentPos;
		
		DecimalFormat f = new DecimalFormat("#.##");
				
		sender.sendStringMessage("Successfully returned to:"
				+ " X = " + f.format(settings.lastPos.getX())
				+ "; Y = " + f.format(settings.lastPos.getY())
				+ "; Z = " + f.format(settings.lastPos.getZ()));
		
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

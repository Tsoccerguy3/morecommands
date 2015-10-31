package com.mrnobody.morecommands.command.client;

import java.lang.reflect.Field;

import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.command.ClientCommand;
import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.handler.EventHandler;
import com.mrnobody.morecommands.handler.Listeners.EventListener;
import com.mrnobody.morecommands.util.ReflectionHelper;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

@Command(
		name = "music",
		description = "command.music.description",
		example = "command.music.example",
		syntax = "command.music.syntax",
		videoURL = "command.music.videoURL"
		)
public class CommandMusic extends ClientCommand implements EventListener<PlaySoundEvent17> {
	private Field musicTickerField = ReflectionHelper.getField(Minecraft.class, "mcMusicTicker");
	private Field playingField = ReflectionHelper.getField(MusicTicker.class, "field_147678_c");
	private Field playingTimerField = ReflectionHelper.getField(MusicTicker.class, "field_147676_d");
	
	private boolean stopSound = false;

	@Override
	public void onEvent(PlaySoundEvent17 event) {
		if (event.category == SoundCategory.MUSIC) {
			if (this.stopSound) 
				event.result = null;
		}
	}
	
	public CommandMusic() {
		EventHandler.SOUND.getHandler().register(this);
	}
	
	@Override
	public String getCommandName() {
		return "music";
	}

	@Override
	public String getUsage() {
		return "command.music.syntax";
	}

	@Override
	public void execute(CommandSender sender, String[] params) throws CommandException {
		if (params.length > 0) {
			if (params[0].equalsIgnoreCase("play")) {
				if (!this.stopSound)
					throw new CommandException("command.music.isplaying", sender);
				
				this.stopSound = false;
				
				try {
					MusicTicker musicTicker = (MusicTicker) this.musicTickerField.get(Minecraft.getMinecraft());
					this.playingTimerField.setInt(musicTicker, 0);
				}
				catch (Exception ex) {ex.printStackTrace();}
				
				sender.sendLangfileMessage("command.music.played");
			}
			else if (params[0].equalsIgnoreCase("next") || params[0].equalsIgnoreCase("skip")) {
				Minecraft.getMinecraft().getSoundHandler().stopSounds();
				this.stopSound = false;
				
				try {
					MusicTicker musicTicker = (MusicTicker) this.musicTickerField.get(Minecraft.getMinecraft());
					this.playingTimerField.setInt(musicTicker, 0);
				}
				catch (Exception ex) {ex.printStackTrace();}
				
				sender.sendLangfileMessage("command.music.skipped");
			}
			else if (params[0].equalsIgnoreCase("stop")) {
				Minecraft.getMinecraft().getSoundHandler().stopSounds();
				this.stopSound = true;
				sender.sendLangfileMessage("command.music.stopped");
			}
			else if (params[0].equalsIgnoreCase("volume") && params.length > 1) {
				try {
					int volume = Integer.parseInt(params[1]);
					
					if (volume < 0) volume = 0;
					if (volume > 100) volume = 100;
					
					Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MUSIC, volume / 100.0F);
					Minecraft.getMinecraft().getSoundHandler().setSoundLevel(SoundCategory.MUSIC, volume / 100.0F);
					
					sender.sendLangfileMessage("command.music.volumeset");
				}
				catch (NumberFormatException nfe) {throw new CommandException("command.music.invalidArg", sender);}
			}
			else throw new CommandException("command.music.invalidUsage", sender);
		}
	}
	
	@Override
	public Requirement[] getRequirements() {
		return new Requirement[0];
	}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public boolean registerIfServerModded() {
		return true;
	}
	
	@Override
	public int getPermissionLevel() {
		return 0;
	}
}

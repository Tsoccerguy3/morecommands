package com.mrnobody.morecommands.util;

import java.io.File;
import java.util.Date;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * A class containing information on the mod, such as mod id, version <br>
 * paths, etc.
 * 
 * @author MrNobody98
 *
 */
public class Reference {
	public static final String MODID = "mrnobody_morecommands";
	public static final String VERSION = "2.1";
	public static final String NAME = "MoreCommands Mod";
    public static final Date BUILD = new Date(System.currentTimeMillis()); //gets replaced during build process
	
	public static final File getModDir() {return Reference.INSTANCE.MOD_DIR;}
	public static final File getServerPlayerDir() {return Reference.INSTANCE.PLAYER_DIR_SERVER;}
	public static final File getClientPlayerDir() {return Reference.INSTANCE.PLAYER_DIR_CLIENT;}
	public static final File getMacroDir() {return Reference.INSTANCE.MACRO_DIR;}
	public static final File getSavesDir() {return Reference.INSTANCE.SAVES_DIR;}
	
	private static Reference INSTANCE;
	
	private File PLAYER_DIR_SERVER;
	private File PLAYER_DIR_CLIENT;
	
	private File MOD_DIR;
	private File MACRO_DIR;
	private File SAVES_DIR;
	
	private Reference(FMLPreInitializationEvent event) {
		this.MOD_DIR = new File(event.getModConfigurationDirectory(), "morecommands");
		if (!this.MOD_DIR.exists()) this.MOD_DIR.mkdirs();
		
		this.MACRO_DIR = new File(this.MOD_DIR, "macros");
		if (!this.MACRO_DIR.exists()) this.MACRO_DIR.mkdirs();
		
		this.PLAYER_DIR_SERVER = new File(this.MOD_DIR, "playerdata_server");
		if (!this.PLAYER_DIR_SERVER.exists()) this.PLAYER_DIR_SERVER.mkdirs();
		
		this.PLAYER_DIR_CLIENT = new File(this.MOD_DIR, "playerdata_client");
		if (!this.PLAYER_DIR_CLIENT.exists()) this.PLAYER_DIR_CLIENT.mkdirs();
	}
	
	public static final void init(FMLPreInitializationEvent event) {
		if (INSTANCE == null) Reference.INSTANCE = new Reference(event);
	}
	
	public static final void setSavesDir(File dir) {
		Reference.INSTANCE.SAVES_DIR = dir;
	}
}

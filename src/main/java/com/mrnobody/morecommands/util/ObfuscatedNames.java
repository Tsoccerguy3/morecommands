package com.mrnobody.morecommands.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import com.mrnobody.morecommands.core.MoreCommands;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockStem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.ClientCommandHandler;

/**
 * Contains wrapper classes for fields and methods that contains
 * information about them to access them via reflection.
 * 
 * @author MrNobody98
 *
 */
public final class ObfuscatedNames {
	private ObfuscatedNames() {}
	
	private static boolean deobfSet = false, isDeobf = false;
	
	/**
	 * Sets the environment names for the default {@link ObfuscatedField}s and {@link ObfuscatedMethod}s.
	 * The environment name is either de deobfuscated name or the obfuscated name depending on whether
	 * minecraft is running in a deobfuscated environment or not
	 * @param isDeobf whether minecraft is running in a deobfuscated environment
	 */
	public static void setEnvNames(boolean isDeobf) {
		if (deobfSet) return;
		
		try {
			for (Field f : ObfuscatedField.class.getFields()) {
				if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()) && 
					Modifier.isFinal(f.getModifiers()) && f.get(null) instanceof ObfuscatedField<?, ?>)
					((ObfuscatedField<?, ?>) f.get(null)).setEnvName(isDeobf);
			}
			
			for (Field f : ObfuscatedMethod.class.getFields()) {
				if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()) && 
					Modifier.isFinal(f.getModifiers()) && f.get(null) instanceof ObfuscatedMethod<?, ?>)
					((ObfuscatedMethod<?, ?>) f.get(null)).setEnvName(isDeobf);
			}
		}
		catch (Exception ex) {MoreCommands.INSTANCE.getLogger().warn("Failed to set environment names", ex);}
		
		deobfSet = true;
	}
	
	/**
	 * A class that contains information about fields.
	 * (Owner, type, deobfuscated and obfuscated name)
	 * 
	 * @author MrNobody98
	 *
	 * @param <O> the owner type
	 * @param <T> the field's type
	 */
	public static final class ObfuscatedField<O, T> {		
		public static final ObfuscatedField<Minecraft, MusicTicker> Minecraft_mcMusicTicker;
		public static final ObfuscatedField<MusicTicker, Integer> MusicTicker_field_147676_d;
		public static final ObfuscatedField<NetHandlerPlayClient, GuiScreen> NetHandlerPlayClient_guiScreenServer;
		public static final ObfuscatedField<ClientCommandHandler, ClientCommandHandler> ClientCommandHandler_instance;
		public static final ObfuscatedField<NetHandlerPlayClient, WorldClient> NetHandlerPlayClient_clientWorldController;
		public static final ObfuscatedField<GuiChat, String> GuiChat_defaultInputFieldText;
		
		static {if (FMLCommonHandler.instance().getSide().isClient()) {
			Minecraft_mcMusicTicker = new ObfuscatedField<Minecraft, MusicTicker>("mcMusicTicker", "field_147126_aw", Minecraft.class, MusicTicker.class);
			MusicTicker_field_147676_d = new ObfuscatedField<MusicTicker, Integer>("field_147676_d", "field_147676_d", MusicTicker.class, int.class);
			NetHandlerPlayClient_guiScreenServer = new ObfuscatedField<NetHandlerPlayClient, GuiScreen>("guiScreenServer", "field_147307_j", NetHandlerPlayClient.class, GuiScreen.class);
			ClientCommandHandler_instance = new ObfuscatedField<ClientCommandHandler, ClientCommandHandler>("instance", "instance", ClientCommandHandler.class, ClientCommandHandler.class);
			NetHandlerPlayClient_clientWorldController = new ObfuscatedField<NetHandlerPlayClient, WorldClient>("clientWorldController", "field_147300_g", NetHandlerPlayClient.class, WorldClient.class);
			GuiChat_defaultInputFieldText = new ObfuscatedField<GuiChat, String>("defaultInputFieldText", "field_146409_v", GuiChat.class, String.class);
		} else {
			Minecraft_mcMusicTicker = null;
			MusicTicker_field_147676_d = null;
			NetHandlerPlayClient_guiScreenServer = null;
			ClientCommandHandler_instance = null;
			NetHandlerPlayClient_clientWorldController = null;
			GuiChat_defaultInputFieldText = null;
		}}
		
		public static final ObfuscatedField<NetHandlerPlayServer, Double> NetHandlerPlayServer_lastPosX = new ObfuscatedField
				<NetHandlerPlayServer, Double>("lastPosX", "field_147373_o", NetHandlerPlayServer.class, double.class);
		public static final ObfuscatedField<NetHandlerPlayServer, Double> NetHandlerPlayServer_lastPosY = new ObfuscatedField
				<NetHandlerPlayServer, Double>("lastPosY", "field_147382_p", NetHandlerPlayServer.class, double.class);
		public static final ObfuscatedField<NetHandlerPlayServer, Double> NetHandlerPlayServer_lastPosZ = new ObfuscatedField
				<NetHandlerPlayServer, Double>("lastPosZ", "field_147381_q", NetHandlerPlayServer.class, double.class);
		public static final ObfuscatedField<NetHandlerPlayServer, Boolean> NetHandlerPlayServer_hasMoved = new ObfuscatedField
				<NetHandlerPlayServer, Boolean>("hasMoved", "field_147380_r", NetHandlerPlayServer.class, boolean.class);
		public static final ObfuscatedField<MinecraftServer, ICommandManager> MinecraftServer_commandManager = new ObfuscatedField
				<MinecraftServer, ICommandManager>("commandManager", "field_71321_q", MinecraftServer.class, ICommandManager.class);
		public static final ObfuscatedField<StatList, Map<String, StatBase>> StatList_oneShotStats = new ObfuscatedField
				<StatList, Map<String, StatBase>>("oneShotStats", "field_75942_a", StatList.class, (Class<Map<String, StatBase>>) (Class<?>) Map.class);
		public static final ObfuscatedField<BlockStem, Block> BlockStem_field_149877_a = new ObfuscatedField
				<BlockStem, Block>("field_149877_a", "field_149877_a", BlockStem.class, Block.class);
		public static final ObfuscatedField<FoodStats, Integer> FoodStats_foodLevel = new ObfuscatedField
				<FoodStats, Integer>("foodLevel", "field_75127_a", FoodStats.class, int.class);
		public static final ObfuscatedField<PlayerCapabilities, Float> PlayerCapabilities_walkSpeed = new ObfuscatedField
				<PlayerCapabilities, Float>("walkSpeed", "field_75097_g", PlayerCapabilities.class, float.class);
		public static final ObfuscatedField<PlayerCapabilities, Float> PlayerCapabilities_flySpeed = new ObfuscatedField
				<PlayerCapabilities, Float>("flySpeed", "field_75096_f", PlayerCapabilities.class, float.class);
		public static final ObfuscatedField<EntityMinecart, Float> EntityMinecart_currentSpeedRail = new ObfuscatedField
				<EntityMinecart, Float>("currentSpeedRail", "currentSpeedRail", EntityMinecart.class, float.class);
		public static final ObfuscatedField<EntityPlayerMP, String> EntityPlayerMP_translator = new ObfuscatedField
				<EntityPlayerMP, String>("translator", "field_71148_cg", EntityPlayerMP.class, String.class);
		public static final ObfuscatedField<NBTTagList, List<NBTBase>> NBTTagList_tagList = new ObfuscatedField
				<NBTTagList, List<NBTBase>>("tagList", "field_74747_a", NBTTagList.class, (Class<List<NBTBase>>) (Class<?>) List.class);
		public static final ObfuscatedField<EntityHorse, AnimalChest> EntityHorse_horseChest = new ObfuscatedField
				<EntityHorse, AnimalChest>("horseChest", "field_110296_bG", EntityHorse.class, AnimalChest.class);
		
		public static final ObfuscatedField<World, WorldInfo> World_worldInfo = new ObfuscatedField
				<World, WorldInfo>("worldInfo", "field_72986_A", World.class, WorldInfo.class);
		
		public static final ObfuscatedField<EntityList, Map<String, Class<? extends Entity>>> EntityList_stringToClassMapping = new ObfuscatedField
				<EntityList, Map<String, Class<? extends Entity>>>("stringToClassMapping", "field_75625_b", EntityList.class, (Class<Map<String, Class<? extends Entity>>>) (Class<?>) Map.class);
		public static final ObfuscatedField<EntityList, Map<Class<? extends Entity>, String>> EntityList_classToStringMapping = new ObfuscatedField
				<EntityList, Map<Class<? extends Entity>, String>>("classToStringMapping", "field_75626_c", EntityList.class, (Class<Map<Class<? extends Entity>, String>>) (Class<?>) Map.class);
		public static final ObfuscatedField<EntityList, Map<Integer, Class<? extends Entity>>> EntityList_IDtoClassMapping = new ObfuscatedField
				<EntityList, Map<Integer, Class<? extends Entity>>>("IDtoClassMapping", "field_75623_d", EntityList.class, (Class<Map<Integer, Class<? extends Entity>>>) (Class<?>) Map.class);
		public static final ObfuscatedField<EntityList, Map<Class<? extends Entity>, Integer>> EntityList_classToIDMapping = new ObfuscatedField
				<EntityList, Map<Class<? extends Entity>, Integer>>("classToIDMapping", "field_75624_e", EntityList.class, (Class<Map<Class<? extends Entity>, Integer>>) (Class<?>) Map.class);
		public static final ObfuscatedField<EntityList, Map<String, Integer>> EntityList_stringToIDMapping = new ObfuscatedField
				<EntityList, Map<String, Integer>>("stringToIDMapping", "field_75622_f", EntityList.class, (Class<Map<String, Integer>>) (Class<?>) Map.class);
		
		//This field is generated dynamically via com.mrnbobody.morecommands.asm.transform.TransformChatStyle
		public static final ObfuscatedField<ChatStyle, ChatStyle> ChatStyle_defaultChatStyle = new ObfuscatedField
				<ChatStyle, ChatStyle>("defaultChatStyle", "defaultChatStyle", ChatStyle.class, ChatStyle.class);
		public static final ObfuscatedField<Field, Integer> Field_modifiers = new ObfuscatedField
				<Field, Integer>("modifiers", "modifiers", Field.class, int.class);
		
		private final String obfName, deobfName; private String envName;
		private final Class<T> retClass; private final Class<O> owner;
		
		/**
		 * Constructs a new {@link ObfuscatedField}
		 * 
		 * @param deobfName the deobfuscated name of the field
		 * @param obfName the obfuscated name of the field
		 * @param owner the owner class of the field
		 * @param retClass the type class of the field
		 */
		public ObfuscatedField(String deobfName, String obfName, Class<O> owner, Class<T> retClass) {
			this.obfName = obfName;
			this.deobfName = deobfName;
			this.retClass = retClass;
			this.owner = owner;
			if (ObfuscatedNames.deobfSet) this.setEnvName(ObfuscatedNames.isDeobf);
		}
		
		/**
		 * Sets the environment name of this field depending on whether minecraft is running in a deobfusctated environment or not
		 * @param isDeobf whether minecraft is running in a deobfuscated environment
		 */
		public void setEnvName(boolean isDeobf) {
			this.envName = isDeobf ? this.deobfName : this.obfName;
		}
		
		/**
		 * @return the deobfuscated name of this field
		 */
		public String getDeobfName() {
			return this.deobfName;
		}
		
		/**
		 * @return the obfuscated name of this field
		 */
		public String getObfName() {
			return this.obfName;
		}
		
		/**
		 * @return the environment name of this field
		 */
		public String getEnvName() {
			return this.envName;
		}
		
		/**
		 * @return the owner class of this field
		 */
		public Class<O> getOwnerClass() {
			return this.owner;
		}
		
		/**
		 * @return the type class of this field
		 */
		public Class<T> getTypeClass() {
			return this.retClass;
		}
	}
	
	/**
	 * A class that contains information about methods.
	 * (Owner, return type, parameter types, deobfuscated and obfuscated name)
	 * 
	 * @author MrNobody98
	 *
	 * @param <O> the owner type
	 * @param <R> the return type
	 */
	public static final class ObfuscatedMethod<O, R> {
		public static final ObfuscatedMethod<MinecraftServer, Void> MinecraftServer_saveAllWorlds = new ObfuscatedMethod
				<MinecraftServer, Void>("saveAllWorlds", "func_71267_a", MinecraftServer.class, void.class, boolean.class);
		public static final ObfuscatedMethod<MinecraftServer, Void> MinecraftServer_loadAllWorlds = new ObfuscatedMethod
				<MinecraftServer, Void>("loadAllWorlds", "func_71247_a", MinecraftServer.class, void.class, String.class, String.class, long.class, WorldType.class, String.class);
		public static final ObfuscatedMethod<EntityHorse, Void> EntityHorse_func_110226_cD = new ObfuscatedMethod
				<EntityHorse, Void>("func_110226_cD", "func_110226_cD", EntityHorse.class, void.class);
		public static final ObfuscatedMethod<EntityHorse, Void> EntityHorse_func_110232_cE = new ObfuscatedMethod
				<EntityHorse, Void>("func_110232_cE", "func_110232_cE", EntityHorse.class, void.class);
		public static final ObfuscatedMethod<EntityDragon, Void> EntityDragon_createEnderPortal = new ObfuscatedMethod
				<EntityDragon, Void>("createEnderPortal", "func_70975_a", EntityDragon.class, void.class, int.class, int.class);
		//This method is generated dynamically via com.mrnbobody.morecommands.asm.transform.TransformBlockRailBase
		public static final ObfuscatedMethod<BlockRailBase, Void> BlockRailBase_setMaxRailSpeed = new ObfuscatedMethod
				<BlockRailBase, Void>("setMaxRailSpeed", "setMaxRailSpeed", BlockRailBase.class, void.class, float.class);
		
		private final String obfName, deobfName; private String envName;
		private final Class<R> retClass; private final Class<O> owner;
		private final Class<?>[] parameters;
		
		/**
		 * Constructs a new {@link ObfuscatedMethod}
		 * 
		 * @param deobfName the deobfuscated name of the method
		 * @param obfName the obfuscated name of the method
		 * @param owner the owner class of the method
		 * @param retClass the type class of the method
		 * @param parameters the parameter types of the method
		 */
		public ObfuscatedMethod(String deobfName, String obfName, Class<O> owner, Class<R> retClass, Class<?>... parameters) {
			this.obfName = obfName;
			this.deobfName = deobfName;
			this.retClass = retClass;
			this.owner = owner;
			this.parameters = parameters;
			if (ObfuscatedNames.deobfSet) this.setEnvName(ObfuscatedNames.isDeobf);
		}
		
		/**
		 * Sets the environment name of this field depending on whether minecraft is running in a deobfusctated environment or not
		 * @param isDeobf whether minecraft is running in a deobfuscated environment
		 */
		public void setEnvName(boolean isDeobf) {
			this.envName = isDeobf ? this.deobfName : this.obfName;
		}
		
		/**
		 * @return the deobfuscated name of this field
		 */
		public String getDeobfName() {
			return this.deobfName;
		}
		
		/**
		 * @return the obfuscated name of this field
		 */
		public String getObfName() {
			return this.obfName;
		}
		
		/**
		 * @return the environment name of this field
		 */
		public String getEnvName() {
			return this.envName;
		}
		
		/**
		 * @return the owner class of this field
		 */
		public Class<O> getOwnerClass() {
			return this.owner;
		}
		
		/**
		 * @return the class of the return type
		 */
		public Class<R> getReturnClass() {
			return this.retClass;
		}
		
		/**
		 * @return the parameter type classes
		 */
		public Class<?>[] getParameters() {
			return this.parameters;
		}
	}
}

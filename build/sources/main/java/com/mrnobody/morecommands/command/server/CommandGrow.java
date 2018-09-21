package com.mrnobody.morecommands.command.server;

import java.lang.reflect.Field;
import java.util.Random;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandException;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.CommandSender;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.util.ObfuscatedNames.ObfuscatedField;
import com.mrnobody.morecommands.util.ReflectionHelper;
import com.mrnobody.morecommands.util.WorldUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Command(
		name = "grow",
		description = "command.grow.description",
		example = "command.grow.example",
		syntax = "command.grow.syntax",
		videoURL = "command.grow.videoURL"
		)
public class CommandGrow extends StandardCommand implements ServerCommandProperties {
	private final Field field_149877_a = ReflectionHelper.getField(ObfuscatedField.BlockStem_crop);
	
	@Override
	public String getCommandName() {
		return "grow";
	}

	@Override
	public String getCommandUsage() {
		return "command.grow.syntax";
	}

	@Override
	public String execute(CommandSender sender, String[] params) throws CommandException {
		int radius = 16;
		
		if (params.length > 0) {
			try {radius = Integer.parseInt(params[0]);}
			catch (NumberFormatException nfe) {throw new CommandException("command.grow.invalidArg", sender);}
		}
		
		BlockPos pos = sender.getPosition();
		World world = sender.getWorld();
		Random rand = new Random();
		
		for (int i = 0; i < radius; i++) {
			for (int j = 0; j < radius; j++) {
				if (pos.getY() - j < 0 || pos.getY() + j > 256) continue;
				
				for (int k = 0; k < radius; k++) {
					this.growPlant(world, pos.getX() + i, pos.getY() + j, pos.getZ() + k, rand);
					this.growPlant(world, pos.getX() - i, pos.getY() + j, pos.getZ() + k, rand);
					this.growPlant(world, pos.getX() - i, pos.getY() + j, pos.getZ() - k, rand);
					this.growPlant(world, pos.getX() + i, pos.getY() + j, pos.getZ() - k, rand);
					this.growPlant(world, pos.getX() + i, pos.getY() - j, pos.getZ() + k, rand);
					this.growPlant(world, pos.getX() - i, pos.getY() - j, pos.getZ() + k, rand);
					this.growPlant(world, pos.getX() - i, pos.getY() - j, pos.getZ() - k, rand);
					this.growPlant(world, pos.getX() + i, pos.getY() - j, pos.getZ() - k, rand);
				}
			}
		}
		
		sender.sendLangfileMessage("command.grow.grown");
		return null;
	}
	
	private void growPlant(World world, int x, int y, int z, Random rand) {
		Block block = WorldUtils.getBlock(world, x, y, z);
		
		if (block instanceof BlockSapling) {
			((BlockSapling) block).grow(world, rand, new BlockPos(x, y, z), ((BlockSapling) block).getStateFromMeta(8));
		}
		else if (block instanceof BlockCrops) {
			((BlockCrops) block).grow(world, rand, new BlockPos(x, y, z), ((BlockCrops) block).getStateFromMeta(block instanceof BlockBeetroot ? 3 : 7));
		}
		else if (block instanceof BlockCactus || block instanceof BlockReed) {
			int length = 1;
			
			while (true) {
				int blen = length;
				
				if (WorldUtils.getBlock(world, x, y + length, z) == block) length++;
				if (WorldUtils.getBlock(world, x, y - length, z) == block) length++;
				
				if (blen == length) break;
			}
			
			if (length < 3) {
				for (int i = 0; i <= 3 - length; i++) {
					WorldUtils.setBlock(world, new BlockPos(x, y + i, z), block);
				}
			}
		}
		else if (block instanceof BlockStem) {
			WorldUtils.setBlockMeta(world, new BlockPos(x, y, z), 7);
			Block stemBlock = ReflectionHelper.get(ObfuscatedField.BlockStem_crop, field_149877_a, (BlockStem) block);
			
			if (stemBlock != null) {
				if (WorldUtils.getBlock(world, x - 1, y, z) == stemBlock) return;
                if (WorldUtils.getBlock(world, x + 1, y, z) == stemBlock) return;
                if (WorldUtils.getBlock(world, x, y, z - 1) == stemBlock) return;
                if (WorldUtils.getBlock(world, x, y, z + 1) == stemBlock) return;

                int i = rand.nextInt(4);
                int j = x;
                int k = z;

                if (i == 0) j = x - 1;
                if (i == 1) ++j;
                if (i == 2) k = z - 1;
                if (i == 3) ++k;

                Block b = WorldUtils.getBlock(world, j, y - 1, k);
                
                if (world.isAirBlock(new BlockPos(j, y, k)) && (b.canSustainPlant(
                	world.getBlockState(new BlockPos(j, y - 1, k)), world, 
                	new BlockPos(j, y - 1, k), EnumFacing.UP, (BlockStem) block) || b == Blocks.DIRT || b == Blocks.GRASS))
                {
                    WorldUtils.setBlock(world, new BlockPos(j, y, k), stemBlock);
                }
			}
		}
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

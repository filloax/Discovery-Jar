package com.filloax.discoveryjar.block;

import com.filloax.discoveryjar.item.ItemBlockMeta;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public final class DJBlocks {

	public DJBlocks() {
	
	}
	
	public static Block maze;
	public static Block golemGen;

	public static void init() {
		GameRegistry.registerBlock(maze = new BlockMaze(), ItemBlockMeta.class, "maze");
		GameRegistry.registerBlock(maze = new BlockGolemGen(), ItemBlockMeta.class, "golemGen");
	}
}

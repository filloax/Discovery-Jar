package com.filloax.discoveryjar.block;

import com.filloax.discoveryjar.item.ItemBlockMeta;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public final class DJBlocks {

	public DJBlocks() {
	
	}
	
	public static Block mazeBlock;

	public static void init() {
		GameRegistry.registerBlock(mazeBlock = new BlockMaze(), ItemBlockMeta.class, "mazeBlock");
	}
}

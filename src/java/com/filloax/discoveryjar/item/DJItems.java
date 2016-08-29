package com.filloax.discoveryjar.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public final class DJItems {

	public DJItems() {
		
	}
	
	private static ToolMaterial MAZEBREAKER = EnumHelper.addToolMaterial("MAZEBREAKER", 1, -1, 7F, 0F, 0);
	
	public static Item mazebreaker;
	
	public static void init() {
		GameRegistry.registerItem(mazebreaker = new ItemMazebreaker(MAZEBREAKER), "mazebreaker");
	}
}

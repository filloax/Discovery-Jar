package com.filloax.discoveryjar.item;

import java.util.Set;

import com.filloax.discoveryjar.Main;
import com.filloax.discoveryjar.block.DJBlocks;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemMazebreaker extends ItemTool {

	private static String unlocName = "mazebreaker";
	private static Set effectiveOn = Sets.newHashSet(new Block[] {DJBlocks.mazeBlock});
	
	public ItemMazebreaker(ToolMaterial material) {
		//This should only be effective with maze blocks
        super(0F,material,effectiveOn); 
        this.setUnlocalizedName(unlocName);
        this.setTextureName(Main.MODID + ":" + unlocName);
	}
	
	@Override
	public boolean func_150897_b(Block block) {
	    return effectiveOn.contains(block);
	}
	
	@Override
	public float func_150893_a(ItemStack stack, Block block) {
	    return effectiveOn.contains(block) ? this.efficiencyOnProperMaterial : super.func_150893_a(stack, block);
	}
}

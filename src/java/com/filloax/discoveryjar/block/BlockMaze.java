package com.filloax.discoveryjar.block;

import java.util.List;
import java.util.Random;

import com.filloax.discoveryjar.Main;
import com.filloax.discoveryjar.item.ItemMazebreaker;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMaze extends Block {

	/* META 0:Sandstone bricks
	 *      1:Sandstone bricks, mossy
	 *      2:Sandstone bricks, cracked
	 *      3:Yellow clay
	 */
	public static IIcon[] icons = new IIcon[4];
	
	public BlockMaze() {
        super(Material.rock);
        this.setHardness(2F);
        this.setBlockName("maze"); 
        this.setResistance(Float.MAX_VALUE);
        this.setStepSound(Block.soundTypeStone);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta > 3) {
			return this.getIcon(side,0);
		} else {
			return icons[meta];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		icons[0] = iconRegister.registerIcon("geostrata:sandstone_b");
		icons[1] = iconRegister.registerIcon(Main.MODID + ":sandstone_moss");
		icons[2] = iconRegister.registerIcon(Main.MODID + ":sandstone_crack");
		icons[3] = iconRegister.registerIcon("minecraft:hardened_clay_stained_yellow");
	}
	
	/**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 4; i ++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
   
    @Override
    public Item getItemDropped(int meta, Random rand, int fortune)
    {
    	if (meta < 3 & !Main.geostrataInstalled) {
    		return Item.getItemFromBlock(Blocks.cobblestone);
    	} else if (meta < 3) {
    		return GameRegistry.findItem("GeoStrata", "geostrata_rock_sandstone_cobble");
    	} else if (meta == 3) {
    		return Item.getItemFromBlock(Blocks.stained_hardened_clay);
    	} else {
    		return null;
    	}
    }
    
    @Override
    public int damageDropped(int meta) {
        switch (meta) {
        case 0:
        case 1:
        case 2:
        default:
        	return 1;
        case 3:
        	return 4;
        }
    }
    
    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return True to spawn the drops
     */
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
    	ItemStack item = player.getCurrentEquippedItem();
    	return item != null && item.getItem() instanceof ItemMazebreaker;
    }
	
	/**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @return True if the block is actually destroyed.
     */
	@Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
    	if (willHarvest || player.capabilities.isCreativeMode) {
        	return super.removedByPlayer(world, player, x, y, z, willHarvest);
        } else {
        	ItemStack item = player.getCurrentEquippedItem();
          	if (item != null && item.getItem() instanceof ItemTool) item.damageItem(16, player);
        	return false;
        }
    }
}
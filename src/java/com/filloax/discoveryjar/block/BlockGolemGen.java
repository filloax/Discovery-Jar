package com.filloax.discoveryjar.block;

import java.util.Random;

import com.filloax.discoveryjar.Main;
import com.filloax.discoveryjar.item.ItemMazebreaker;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/* If broken, turn metadata up by 1.
 * Unless it's 15, in which case proceed as normal, and drop 2-5 iron ingots.
 */
public class BlockGolemGen extends Block {

	private int leastDrop = 2;
	private int mostDrop = 5;
	
	protected BlockGolemGen() {
		super(Material.iron);
        this.setHardness(2F);
        this.setBlockName("golemGen"); 
        this.setResistance(Float.MAX_VALUE);
        this.setStepSound(Block.soundTypeMetal);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 1);
	}

	public static IIcon[] icons = new IIcon[6];
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		icons[0] = iconRegister.registerIcon(Main.MODID + ":golemGen1");
		icons[1] = iconRegister.registerIcon(Main.MODID + ":golemGen1_still");
		icons[2] = iconRegister.registerIcon(Main.MODID + ":golemGen2");
		icons[3] = iconRegister.registerIcon(Main.MODID + ":golemGen2_still");
		icons[4] = iconRegister.registerIcon(Main.MODID + ":golemGen3");
		icons[5] = iconRegister.registerIcon(Main.MODID + ":golemGen3_still");
	}

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
	@Override
	public IIcon getIcon(int side, int meta) {
		if (side < 2) {
			if (meta < 6) {
				return icons[1];
			} else if (meta < 12) {
				return icons[3];				
			} else {
				return icons[5];
			}
		} else {
			if (meta < 6) {
				return icons[0];
			} else if (meta < 12) {
				return icons[2];				
			} else {
				return icons[4];
			}
		}
	}
	
    @Override
    public Item getItemDropped(int meta, Random rand, int fortune)
    {
    	if (meta == 15) 
    		return Items.iron_ingot;
    	return null;
    }
    
    @Override
    //From 2 to 5 iron ingots
    public int quantityDropped(int meta, int fortune, Random rand) {
    	return this.leastDrop + rand.nextInt(this.mostDrop - this.leastDrop + fortune + 1);
    }
    
	@Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
		int meta = world.getBlockMetadata(x, y, z);
    	if (meta == 15 || player.capabilities.isCreativeMode) {
        	return super.removedByPlayer(world, player, x, y, z, willHarvest);
        } else {
          	world.setBlockMetadataWithNotify(x, y, z, meta+1, 3);
        	return false;
        }
    }
}

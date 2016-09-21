package com.filloax.discoveryjar.core;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.filloax.discoveryjar.Main;
import com.filloax.discoveryjar.block.BlockMaze;
import com.filloax.discoveryjar.block.DJBlocks;
import com.filloax.discoveryjar.item.DJItems;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent e) {
		
	}
		
    @EventHandler
    public void init(FMLInitializationEvent e) {
        DJBlocks.init();
        DJItems.init();
//        MinecraftForge.EVENT_BUS.register(Main.eventSubscriber);
//		FMLCommonHandler.instance().bus().register(Main.eventSubscriber);
    }
    
    @EventHandler   
    public void postInit(FMLPostInitializationEvent e) {
        Main.geostrataInstalled = Loader.isModLoaded("GeoStrata");
        
        if (Main.geostrataInstalled) {
        	System.out.println("Hi GeoStrata!");
        }
        
        // DEBUG CODE
        
//        System.out.println("Testing ctm algorithm: ");
//        Random rand = new Random();
//    	int x = rand.nextInt()%10000;
//    	int y = rand.nextInt()%10000;
//    	int z = rand.nextInt()%10000;
//    	for (int i=0;i< 16;i++) {
//            System.out.println(Math.floor(BlockMaze.ctmNoise(x, y, z)) +" from "+x+"; "+y+"; "+z);
//            x+=1;
//            z+=1;
//        }
//        System.out.println("Done.");
    }
    
    public File getSavesFolder(){
    	return null;
    };
        
    @EventHandler
	public void serverStarting(FMLServerStartingEvent e) throws IOException {
	
	}
}

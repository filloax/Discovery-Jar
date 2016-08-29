package com.filloax.discoveryjar.core;

import java.io.File;
import java.io.IOException;

import com.filloax.discoveryjar.Main;
import com.filloax.discoveryjar.block.DJBlocks;
import com.filloax.discoveryjar.item.DJItems;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

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
    }
    
    public File getSavesFolder(){
    	return null;
    };
        
    @EventHandler
	public void serverStarting(FMLServerStartingEvent e) throws IOException {
	
	}
}

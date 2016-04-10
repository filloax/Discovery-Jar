package com.filloax.improvedcommands;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main {

    public static final String MODID = "improvedcommands";
    public static final String MODNAME = "Improved Vanilla Commands";
    public static final String VERSION = "1.0";
        
    @Instance
    public static Main instance = new Main();
    
    public static SimpleNetworkWrapper network;
    
    @SidedProxy(clientSide="com.filloax.improvedcommands.ClientProxy", serverSide="com.filloax.improvedcommands.ServerProxy")
    public static CommonProxy proxy;
     
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	proxy.preInit(e);          
    }
        
    @EventHandler
    public void init(FMLInitializationEvent e) {
    	proxy.init(e);
    }
        
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	proxy.postInit(e);
    }
    @EventHandler
    public void serverLoad(FMLServerStartingEvent e) {
    	proxy.serverLoad(e);
    }
}
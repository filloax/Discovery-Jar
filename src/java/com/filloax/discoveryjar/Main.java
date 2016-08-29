package com.filloax.discoveryjar;

import com.filloax.discoveryjar.core.CommonProxy;
import com.filloax.discoveryjar.core.EventSubscriber;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main {

    public static final String MODID = "discoveryjar";
    public static final String MODNAME = "Discovery Jar";
    public static final String VERSION = "1.0";
        
    @Instance
    public static Main instance = new Main();
        
    @SidedProxy(clientSide="com.filloax.discoveryjar.core.ClientProxy", serverSide="com.filloax.discoveryjar.core.ServerProxy")
    public static CommonProxy proxy;
         
    public static boolean geostrataInstalled = false;
    
    public static final EventSubscriber eventSubscriber = new EventSubscriber();

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
    
}

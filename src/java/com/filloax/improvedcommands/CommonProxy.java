package com.filloax.improvedcommands;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommonProxy {    
	@EventHandler
    public void preInit(FMLPreInitializationEvent e) {
		Main.network = NetworkRegistry.INSTANCE.newSimpleChannel("channel");
		Main.network.registerMessage(SoundPacket.Handler.class, SoundPacket.class, 0, Side.CLIENT);
    }
    @EventHandler
    public void init(FMLInitializationEvent e) {

    }

    @EventHandler   
    public void postInit(FMLPostInitializationEvent e) {

    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent e) {
        e.registerServerCommand(new SetBlock());
        e.registerServerCommand(new Summon());
        e.registerServerCommand(new PlaySound());
    }
    
}

package com.filloax.improvedcommands.core;

import com.filloax.improvedcommands.Main;
import com.filloax.improvedcommands.command.PlaySound;
import com.filloax.improvedcommands.command.PlaySoundBackground;
import com.filloax.improvedcommands.command.SetBlock;
import com.filloax.improvedcommands.command.Summon;
import com.filloax.improvedcommands.util.SoundBackgroundPacket;
import com.filloax.improvedcommands.util.SoundPacket;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {    
	@EventHandler
    public void preInit(FMLPreInitializationEvent e) {
		int packetid = 0;
		Main.network = NetworkRegistry.INSTANCE.newSimpleChannel("channel");
		Main.network.registerMessage(SoundPacket.Handler.class, SoundPacket.class, packetid++, Side.CLIENT);
		Main.network.registerMessage(SoundBackgroundPacket.Handler.class, SoundBackgroundPacket.class, packetid++, Side.CLIENT);
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
        e.registerServerCommand(new PlaySoundBackground());
    }
    
}

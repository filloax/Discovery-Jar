package com.filloax.improvedcommands.util;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;

public class SoundBackgroundPacket implements IMessage {

	private String sound;
	private String mode;
	private float vol;
	private float pitch;
	private int delay;
	
	public SoundBackgroundPacket() {}
	
	public SoundBackgroundPacket(String sound, String mode, float vol, float pitch, int delay) {
		this.sound = sound;
		this.mode = mode;
		this.vol = vol;
		this.pitch = pitch;
		this.delay = delay;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sound = ByteBufUtils.readUTF8String(buf);
		mode = ByteBufUtils.readUTF8String(buf);
		vol = buf.readFloat();
		pitch = buf.readFloat();
		delay = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, sound);
		ByteBufUtils.writeUTF8String(buf, mode);
		buf.writeFloat(vol);
		buf.writeFloat(pitch);
		buf.writeInt(delay);
	}
	
	public static class Handler implements IMessageHandler<SoundBackgroundPacket, IMessage> {

	    private BackgroundSound playingsound;    
		
		@Override
		public IMessage onMessage(SoundBackgroundPacket m, MessageContext ctx) {
//			System.out.println(m.sound+";"+m.mode+";"+m.vol+";"+m.pitch);
	    	
	    	if (playingsound != null && !m.mode.equals("lowPr")) {
//				System.out.println("Old sound:"+oldsound.getName()+",repeat:"+oldsound.getRepeat()+",done:"+oldsound.isDonePlaying()+";stopping.");
	    		playingsound.stop();
	    	}
	    	if (!m.mode.equals("stop")) { 
//	    		System.out.println("Playing sound at " + m.x + "/" + m.y + "/" + m.z + ": " + m.sound);
	    		ResourceLocation resource = new ResourceLocation(m.sound);
	    		
		    	if (m.mode.equals("normal") || (m.mode.equals("lowPr") && (playingsound == null || Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(playingsound)))) {
		    		BackgroundSound sound = new BackgroundSound(resource, m.vol, m.pitch, false, m.delay);
		    		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		    		playingsound = sound;
		    	} else if (m.mode.equals("loop")) {
		    		BackgroundSound sound = new BackgroundSound(resource, m.vol, m.pitch, true, m.delay);  
		    		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		    		playingsound = sound;
		    	}
	    	}
			return null;
		}

	}
}

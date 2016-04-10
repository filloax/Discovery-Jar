package com.filloax.improvedcommands;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;

public class SoundPacket implements IMessage {
	
	private String sound;
	private String mode;
	private int x;
	private int y;
	private int z;
	private float vol;
	private float pitch;
	private int delay;
	
	public SoundPacket() {}
	
	public SoundPacket(String sound, String mode, int x, int y, int z, float vol, float pitch, int delay) {
		this.sound = sound;
		this.mode = mode;
		this.x = x;
		this.y = y;
		this.z = z;
		this.vol = vol;
		this.pitch = pitch;
		this.delay = delay;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sound = ByteBufUtils.readUTF8String(buf);
		mode = ByteBufUtils.readUTF8String(buf);
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		vol = buf.readFloat();
		pitch = buf.readFloat();
		delay = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, sound);
		ByteBufUtils.writeUTF8String(buf, mode);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeFloat(vol);
		buf.writeFloat(pitch);
		buf.writeInt(delay);
	}

	public static class Handler implements IMessageHandler<SoundPacket, IMessage> {

	    private final Map<ChunkCoordinates, LoopableSound> mapSoundPositions = new HashMap<ChunkCoordinates, LoopableSound>();    
		
		@Override
		public IMessage onMessage(SoundPacket m, MessageContext ctx) {
//			System.out.println(m.sound+";"+m.mode+";"+m.x+";"+m.y+";"+m.z+";"+m.vol+";"+m.pitch);
	    	
			ChunkCoordinates chunkcoordinates = new ChunkCoordinates(m.x, m.y, m.z);
			LoopableSound oldsound = mapSoundPositions.get(chunkcoordinates);
	    	if (oldsound != null && !m.mode.equals("lowPr")) {
//				System.out.println("Old sound:"+oldsound.getName()+",repeat:"+oldsound.getRepeat()+",done:"+oldsound.isDonePlaying()+";stopping.");
	    		oldsound.stop();
	    		mapSoundPositions.remove(chunkcoordinates);
	    	}
	    	if (!m.mode.equals("stop")) { 
	    		LoopableSound sound = null;
//	    		System.out.println("Playing sound at " + m.x + "/" + m.y + "/" + m.z + ": " + m.sound);
	    		ResourceLocation resource = new ResourceLocation(m.sound);
	    		
		    	if (m.mode.equals("normal") || (m.mode.equals("lowPr") && (oldsound == null || Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(oldsound)))) {
		    		sound = new LoopableSound(resource, m.vol, m.pitch, m.x, m.y, m.z, false, m.delay);
		    		mapSoundPositions.put(chunkcoordinates, sound);
		    		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		    	} else if (m.mode.equals("loop")) {
		    		sound = new LoopableSound(resource, m.vol, m.pitch, m.x, m.y, m.z, true, m.delay);  
		    		mapSoundPositions.put(chunkcoordinates, sound);
		    		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		    	}
	    	}
			return null;
		}

	}
}

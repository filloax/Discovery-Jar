package com.filloax.improvedcommands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class LoopableSound extends PositionedSoundRecord implements ITickableSound{

	private boolean donePlaying;

	public LoopableSound(ResourceLocation resource, float pitch, float vol, float x, float y, float z, boolean repeat, int delay) {
		super(resource, pitch, vol, x, y, z);
		this.repeat = repeat;
		this.field_147665_h = delay; //delay between each loop
	}

	public void stop() {
		this.donePlaying = true;
		this.repeat = false;
//		Minecraft.getMinecraft().getSoundHandler().stopSound(this);
	}

	@Override
	public void update() {}

	@Override
	public boolean isDonePlaying() {
		return this.donePlaying;
	}
	
	public String getName() {
		return this.field_147664_a.toString();
	}
}

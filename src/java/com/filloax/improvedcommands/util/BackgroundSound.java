package com.filloax.improvedcommands.util;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

public class BackgroundSound extends LoopableSound {

	public BackgroundSound(ResourceLocation resource, float pitch, float vol, boolean repeat, int delay) 
	{
		super(resource, pitch, vol, 0, 0, 0, repeat, delay);
		this.field_147666_i = ISound.AttenuationType.NONE;
	}
	
}

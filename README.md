# Improved Commands

This mod adds 4 commands:

/setblock2: like /setblock, but can use string item ids in the tileentity JSON, like in 1.8+

/summon2: like /summon,  but can use string item ids in the entity JSON, like in 1.8+

/playsound2: has 2 other args:<mode> and <delay>. mode can be either normal, stop, loop or lowPr (any other value will act as normal).
normal plays the sound and stop the one that was running in the same position
stop stops the one running in the same position and ingores the sound argument in the command
loop plays the sound and loops it when finished, also stops the old sound like above
lowPr (low priority) plays the sound only if the old one ahs been stopped/if no sound has been played in that position
delay is teh delay between each loop

/playsoundb : plays the sound in the background, regardless of position. has a mode and delay arguments that work like /playsound2
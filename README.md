# Improved-Vanilla-Commands
This mod adds 6 commands:

/setblock2: like /setblock, but can use string item ids in the tileentity JSON, like in 1.8+

/summon2: like /summon,  but can use string item ids in the entity JSON, like in 1.8+

/playsound2: has another arg:<mode>. This can be either normal, stop, loop or lowPr (any other value will act as normal).
normal plays the sound and stop the one that was running in the same position
stop stops the one running in the same position and ingores the sound argument in the command
loop plays the sound and loops it when finished, also stops the old sound like above
lowPr (low priority) plays the sound only if the old one ahs been stopped/if no sound has been played in that position

/playsoundf (NYI): other than the mode arg, it has another player arg. The first player arg, the source, is the player that will be 
followed by the sound, the second player arg, the target, who will be able to hear it. The coordinates are the offset, so no ~

/setseed (NYI): sets the world seed to a random number or the one specified

/restoreseed (NYI): sets the world seed to the original one

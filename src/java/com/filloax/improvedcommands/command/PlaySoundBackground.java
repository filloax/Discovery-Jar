package com.filloax.improvedcommands.command;

import com.filloax.improvedcommands.Main;
import com.filloax.improvedcommands.util.SoundBackgroundPacket;
import com.filloax.improvedcommands.util.SoundPacket;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PlaySoundBackground extends CommandBase {

	@Override
	public String getCommandName() {
		return "playsoundb";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.playsoundb.usage";
	}
	
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender player, String[] input)    
	{
        if (input.length < 1)
        {
            throw new WrongUsageException(this.getCommandUsage(player), new Object[0]);
        }
        else
        {
        	String sound = input[0];
        	String mode = "normal";
            EntityPlayerMP playerarg = getPlayer(player, "@p");
            float vol = 1;
            float pitch = 1;
            int delay = 0;
            
            World world = player.getEntityWorld();
            if (input.length > 1 && (input[1].equals("normal") || input[1].equals("stop") || input[1].equals("lowPr") || input[1].equals("loop"))) {
        		mode = input[1];
            }
            if (input.length > 2) {
            	playerarg = getPlayer(player, input[2]);
            }
            if (input.length > 3) {
            	vol = (float) parseDouble(player, input[3]);
            }
            if (input.length > 4) {
            	pitch = (float) parseDouble(player,input[4]);
            }
            if (input.length > 5) {
            	delay = parseInt(player, input[5]);
            }

            Main.network.sendTo(new SoundBackgroundPacket(sound, mode, vol, pitch, delay), playerarg);
            
            func_152373_a(player, this, "Playing sound to "+playerarg.getDisplayName(), new Object[0]);
//          System.out.println("Played \'"+sound+"\' to \'"+(playerarg != null ? playerarg.getDisplayName() : "everyone")+"\' at "+x+" "+y+" "+z);
        }
    }
	
    public boolean isUsernameIndex(String[] p_82358_1_, int index)
    {
        return index == 2 || index == 3;
    }
}

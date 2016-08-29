package com.filloax.improvedcommands.command;

import com.filloax.improvedcommands.Main;
import com.filloax.improvedcommands.util.SoundPacket;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PlaySound extends CommandBase{

    public String getCommandName()
    {
        return "playsound2";
    }
    
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands.playsound2.usage";
    }
    
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
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
            EntityPlayerMP playerarg = getPlayer(player, "@a");
            int x = player.getPlayerCoordinates().posX;
            int y = player.getPlayerCoordinates().posY;
            int z = player.getPlayerCoordinates().posZ;
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
            if (input.length > 5) {
            	x = MathHelper.floor_double(func_110666_a(player, (double)x, input[3]));
                y = MathHelper.floor_double(func_110666_a(player, (double)y, input[4]));
                z = MathHelper.floor_double(func_110666_a(player, (double)z, input[5]));
            }
            if (input.length > 6) {
            	vol = (float) parseDouble(player, input[6]);
            }
            if (input.length > 7) {
            	pitch = (float) parseDouble(player,input[7]);
            }
            if (input.length > 8) {
            	delay = parseInt(player, input[8]);
            }

            Main.network.sendTo(new SoundPacket(sound, mode, x, y, z, vol, pitch, delay), playerarg);
            
            func_152373_a(player, this, "Playing sound at "+x+" "+y+" "+z, new Object[0]);
//          System.out.println("Played \'"+sound+"\' to \'"+(playerarg != null ? playerarg.getDisplayName() : "everyone")+"\' at "+x+" "+y+" "+z);
        }
    }
	
    public boolean isUsernameIndex(String[] p_82358_1_, int index)
    {
        return index == 2;
    }
}

package com.filloax.improvedcommands;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SetBlock extends CommandSetBlock
{
  public SetBlock()
  {
  }

  public String getCommandName()
  {
    return "setblock2";
  }

  public void processCommand(ICommandSender player, String[] input)
  {
      if (input.length >= 4)
      {
          int i = player.getPlayerCoordinates().posX;
          int j = player.getPlayerCoordinates().posY;
          int k = player.getPlayerCoordinates().posZ;
          i = MathHelper.floor_double(func_110666_a(player, (double)i, input[0]));
          j = MathHelper.floor_double(func_110666_a(player, (double)j, input[1]));
          k = MathHelper.floor_double(func_110666_a(player, (double)k, input[2]));
          Block block = CommandBase.getBlockByText(player, input[3]);
          int l = 0;

          if (input.length >= 5)
          {
              l = parseIntBounded(player, input[4], 0, 15);
          }

          World world = player.getEntityWorld();

          if (!world.blockExists(i, j, k))
          {
              throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
          }
          else
          {
              NBTTagCompound nbttagcompound = new NBTTagCompound();
              boolean flag = false;

              if (input.length >= 7 && block.hasTileEntity())
              {
                  String s = func_147178_a(player, input, 6).getUnformattedText();

                  try
                  {
                      NBTBase nbtbase = JsonToNBT.getCompoundTagFromString(s);

                      if (!(nbtbase instanceof NBTTagCompound))
                      {
                          throw new CommandException("commands.setblock.tagError", new Object[] {"Not a valid tag"});
                      }

                      nbttagcompound = (NBTTagCompound)nbtbase;
                      flag = true;
                  }
                  catch (NBTException nbtexception)
                  {
                      throw new CommandException("commands.setblock.tagError", new Object[] {nbtexception.getMessage()});
                  }
              }

              if (input.length >= 6)
              {
                  if (input[5].equals("destroy"))
                  {
                      world.func_147480_a(i, j, k, true);
                  }
                  else if (input[5].equals("keep") && !world.isAirBlock(i, j, k))
                  {
                      throw new CommandException("commands.setblock.noChange", new Object[0]);
                  }
              }

              if (!world.setBlock(i, j, k, block, l, 3))
              {
                  throw new CommandException("commands.setblock.noChange", new Object[0]);
              }
              else
              {
                  if (flag)
                  {
                      TileEntity tileentity = world.getTileEntity(i, j, k);

                      if (tileentity != null)
                      {
                          nbttagcompound.setInteger("x", i);
                          nbttagcompound.setInteger("y", j);
                          nbttagcompound.setInteger("z", k);
                          tileentity.readFromNBT(nbttagcompound);
                      }
                  }

                  func_152373_a(player, this, "commands.setblock.success", new Object[0]);
              }
          }
      }
      else
      {
          throw new WrongUsageException("commands.setblock.usage", new Object[0]);
      }
  }
}

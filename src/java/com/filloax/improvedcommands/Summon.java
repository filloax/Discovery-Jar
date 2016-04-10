package com.filloax.improvedcommands;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Summon extends CommandSummon{
  public Summon()
  {
  }

  public String getCommandName()
  {
    return "summon2";
  }

  public void processCommand(ICommandSender player, String[] input)
  {
      if (input.length < 1)
      {
          throw new WrongUsageException("commands.summon.usage", new Object[0]);
      }
      else
      {
          String s = input[0];
          double d0 = (double)player.getPlayerCoordinates().posX + 0.5D;
          double d1 = (double)player.getPlayerCoordinates().posY;
          double d2 = (double)player.getPlayerCoordinates().posZ + 0.5D;

          if (input.length >= 4)
          {
              d0 = func_110666_a(player, d0, input[1]);
              d1 = func_110666_a(player, d1, input[2]);
              d2 = func_110666_a(player, d2, input[3]);
          }

          World world = player.getEntityWorld();

          if (!world.blockExists((int)d0, (int)d1, (int)d2))
          {
              func_152373_a(player, this, "commands.summon.outOfWorld", new Object[0]);
          }
          else
          {
              NBTTagCompound nbttagcompound = new NBTTagCompound();
              boolean flag = false;

              if (input.length >= 5)
              {
                  IChatComponent ichatcomponent = func_147178_a(player, input, 4);

                  try
                  {
                      NBTBase nbtbase = JsonToNBT.getCompoundTagFromString(ichatcomponent.getUnformattedText());

                      if (!(nbtbase instanceof NBTTagCompound))
                      {
                          func_152373_a(player, this, "commands.summon.tagError", new Object[] {"Not a valid tag"});
                          return;
                      }

                      nbttagcompound = (NBTTagCompound)nbtbase;
                      flag = true;
                  }
                  catch (NBTException nbtexception)
                  {
                      func_152373_a(player, this, "commands.summon.tagError", new Object[] {nbtexception.getMessage()});
                      return;
                  }
              }

              nbttagcompound.setString("id", s);
              Entity entity1 = EntityList.createEntityFromNBT(nbttagcompound, world);

              if (entity1 == null)
              {
                  func_152373_a(player, this, "commands.summon.failed", new Object[0]);
              }
              else
              {
                  entity1.setLocationAndAngles(d0, d1, d2, entity1.rotationYaw, entity1.rotationPitch);

                  if (!flag && entity1 instanceof EntityLiving)
                  {
                      ((EntityLiving)entity1).onSpawnWithEgg((IEntityLivingData)null);
                  }

                  world.spawnEntityInWorld(entity1);
                  Entity entity2 = entity1;

                  for (NBTTagCompound nbttagcompound1 = nbttagcompound; entity2 != null && nbttagcompound1.hasKey("Riding", 10); nbttagcompound1 = nbttagcompound1.getCompoundTag("Riding"))
                  {
                      Entity entity = EntityList.createEntityFromNBT(nbttagcompound1.getCompoundTag("Riding"), world);

                      if (entity != null)
                      {
                          entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
                          world.spawnEntityInWorld(entity);
                          entity2.mountEntity(entity);
                      }

                      entity2 = entity;
                  }

                  func_152373_a(player, this, "commands.summon.success", new Object[0]);
              }
          }
      }
  }
}

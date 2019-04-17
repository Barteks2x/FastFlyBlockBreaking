package com.github.barteks2x.fastflybreak;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Mod(modid = FFBB.MODID, version = FFBB.VERSION, name = FFBB.NAME)
public class FFBB {
  public static final String VERSION = "${version}";
  public static final String NAME = "FastFlyBlockBreaking";
  public static final String MODID = "fastflyblockbreaking";

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void blockBreakSpeed(PlayerEvent.BreakSpeed event){
    if(!event.entityPlayer.onGround && event.entityPlayer.capabilities.isFlying){
      event.newSpeed = event.originalSpeed * 5;
    }
  }
}

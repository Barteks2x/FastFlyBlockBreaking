package com.github.barteks2x.fastflybreak;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = FFBB.MODID, version = FFBB.VERSION, name = FFBB.NAME, acceptedMinecraftVersions = "[1.9.4,1.12.2]")
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
    if(!event.getEntityPlayer().onGround && event.getEntityPlayer().capabilities.isFlying){
      event.setNewSpeed(event.getOriginalSpeed() * 5);
    }
  }
}

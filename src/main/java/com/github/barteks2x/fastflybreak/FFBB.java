package com.github.barteks2x.fastflybreak;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("fastflyblockbreaking")
public class FFBB {
  public FFBB() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void blockBreakSpeed(PlayerEvent.BreakSpeed event){
    if(!event.getEntityPlayer().onGround && event.getEntityPlayer().abilities.isFlying){
      event.setNewSpeed(event.getOriginalSpeed() * 5);
    }
  }
}

package com.github.barteks2x.fastflybreak;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventListener {
  @SubscribeEvent
  public void blockBreakSpeed(PlayerEvent.BreakSpeed event){
    if(!event.entityPlayer.onGround && event.entityPlayer.capabilities.isFlying){
      event.newSpeed = event.originalSpeed * 5;
    }
  }
}

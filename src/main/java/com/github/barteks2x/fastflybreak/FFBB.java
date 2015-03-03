package com.github.barteks2x.fastflybreak;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = FFBB.MODID, version = FFBB.VERSION, name = FFBB.NAME)
public class FFBB {
  public static final String VERSION = "${version}";
  public static final String NAME = "FastFlyBlockBreaking";
  public static final String MODID = "fastflyblockbreaking";

  private EventListener eventListener;

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    this.eventListener = new EventListener();
    MinecraftForge.EVENT_BUS.register(this.eventListener);
  }
}

package com.github.barteks2x.fastflybreak;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod(modid = FFBB.MODID, version = FFBB.VERSION, name = FFBB.NAME, acceptedMinecraftVersions = "[1.9.4,1.12.2]")
public class FFBB {
  public static final String VERSION = "${version}";
  public static final String NAME = "FastFlyBlockBreaking";
  public static final String MODID = "fastflyblockbreaking";

  private Configuration config;
  private boolean alwaysFastBreaking;

  @Mod.EventHandler
  public void init(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    this.config = new Configuration(new File(event.getModConfigurationDirectory(), "fastflyblockbreaking.cfg"));
    this.alwaysFastBreaking = this.config.get("general", "alwaysFastBreaking", false).getBoolean();
    this.config.save();
  }

  @SubscribeEvent
  public void onConfigChange(ConfigChangedEvent.PostConfigChangedEvent evt) {
    this.alwaysFastBreaking = this.config.get("general", "alwaysFastBreaking", false).getBoolean();
  }

  @SubscribeEvent
  public void blockBreakSpeed(PlayerEvent.BreakSpeed event){
    if(!event.getEntityPlayer().onGround && (event.getEntityPlayer().capabilities.isFlying || alwaysFastBreaking)){
      event.setNewSpeed(event.getOriginalSpeed() * 5);
    }
  }
}

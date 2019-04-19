package com.github.barteks2x.fastflybreak;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.io.File;

@Mod(modid = FFBB.MODID, version = FFBB.VERSION, name = FFBB.NAME)
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
    if(!event.entityPlayer.onGround && (event.entityPlayer.capabilities.isFlying || alwaysFastBreaking)){
      event.newSpeed = event.originalSpeed * 5;
    }
  }
}

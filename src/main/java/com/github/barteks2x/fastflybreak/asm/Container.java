package com.github.barteks2x.fastflybreak.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;
import java.util.Arrays;

public class Container extends DummyModContainer {
  private final String VERSION = "[1.7.10]";
  private final VersionRange VERSION_RANGE;

  public Container() {
    super(new ModMetadata());
    try {
      this.VERSION_RANGE = VersionRange.createFromVersionSpec(VERSION);
    } catch (InvalidVersionSpecificationException ex)// WHY IS IT CHECKED EXCEPTION!?
    {
      throw new AssertionError("Forge says that version range " + VERSION + " is invalid", ex);
    }
    ModMetadata meta = getMetadata();
    meta.modId = "fastflyblockbreaking";
    meta.name = "FastFlyBlockBreaking";
    meta.version = "0.1";
    meta.authorList = Arrays.asList("Barteks2x");
    meta.description = "Allows to break blocks with normal speed when flying is enabled in survival mode.";
  }

  @Override
  public boolean registerBus(EventBus bus, LoadController controller) {
    bus.register(this);
    return true;
  }

  @Subscribe
  public void modConstruction(FMLConstructionEvent evt) {

  }

  @Subscribe
  public void preInit(FMLPreInitializationEvent evt) {

  }

  @Subscribe
  public void init(FMLInitializationEvent evt) {

  }

  @Subscribe
  public void postInit(FMLPostInitializationEvent evt) {

  }

  @Override
  public VersionRange acceptableMinecraftVersionRange() {
    return VERSION_RANGE;
  }
}

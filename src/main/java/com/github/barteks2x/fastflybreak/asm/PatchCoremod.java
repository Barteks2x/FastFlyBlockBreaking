package com.github.barteks2x.fastflybreak.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.util.Map;

public class PatchCoremod implements IFMLLoadingPlugin {
  @Override
  public String[] getASMTransformerClass() {
    return new String[] {Patcher.class.getName()};
  }

  @Override
  public String getModContainerClass() {
    return Container.class.getName();
  }

  @Override
  public String getSetupClass() {
    return null;
  }

  @Override
  public void injectData(Map<String, Object> data) {

  }

  @Override
  public String getAccessTransformerClass() {
    return null;
  }
}

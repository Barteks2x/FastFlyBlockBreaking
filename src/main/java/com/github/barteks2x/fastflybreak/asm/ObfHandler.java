package com.github.barteks2x.fastflybreak.asm;

import cpw.mods.fml.common.Loader;
import java.util.HashMap;
import java.util.Map;

public class ObfHandler {
  private static final String MC_VERSION = Loader.MC_VERSION;

  public static final String CL_ENTITY_PLAYER = "net.minecraft.entity.player.EntityPlayer";
  public static final String CL_PLAYER_CAPABILITIES =
      "net.minecraft.entity.player.PlayerCapabilities";
  public static final String MT_GET_BREAK_SPEED = "getBreakSpeed";

  public static final String FD_ON_GROUND = "onGround";
  public static final String FD_CAPABILITIES = "capabilities";
  public static final String FD_IS_FLYING = "isFlying";

  private final Mappings mappings;

  private Boolean isDeobf = null;

  public ObfHandler() {
    this.mappings = new Mappings();
  }

  public String getClassName(String deobf) {
    checkInit();
    if (isDeobf) {
      return deobf;
    }
    return getObfClassName(deobf);
  }

  private String getObfClassName(String deobf) {
    String obf = mappings.get(MC_VERSION + "|CL|" + deobf);
    if (obf != null) {
      return obf;
    }
    throw new IllegalStateException("No mapping for class: " + deobf);
  }

  public String getMethodName(String clDeobf, String deobf) {
    checkInit();
    
    if(isDeobf){
      return deobf;
    }
    String deobfWithVersion = MC_VERSION + "|MT|" + clDeobf + "|" + deobf;
    
    // try to find obfuscation mapping
    String obf = mappings.get(deobfWithVersion);
    if (obf != null) {
      return obf;
    }
    throw new IllegalStateException("No mapping for method: " + deobf);
  }

  public String getFieldName(String clDeobf, String deobf) {
    checkInit();
    
    if(isDeobf){
      return deobf;
    }
    String deobfWithVersion = MC_VERSION + "|FD|" + clDeobf + "|" + deobf;

    // try to find obfuscation mapping
    String obf = mappings.get(deobfWithVersion);
    if (obf != null) {
      return obf;
    }
    throw new IllegalStateException("No mapping for field: " + deobf);
  }

  public boolean checkClassesEqualAndInit(String deobf, String toTest) {
    // dev environment
    if (deobf.equals(toTest)) {
      isDeobf = true;
      return true;
    }
    // real minecraft
    if (getObfClassName(deobf).equals(toTest)) {
      isDeobf = false;
      return true;
    }
    // not equal
    return false;
  }

  public boolean methodsEqual(String clDeobf, String deobf, String toTest) {
    checkInit();
    if (isDeobf) {
      return deobf.equals(toTest);
    }
    return getMethodName(clDeobf, deobf).equals(toTest);
  }

  boolean fieldsEqual(String clDeobf, String deobf, String toTest) {
    checkInit();
    if (isDeobf) {
      return deobf.equals(toTest);
    }
    return getFieldName(clDeobf, deobf).equals(toTest);
  }

  private void checkInit() {
    if (isDeobf == null) {
      throw new IllegalStateException("ObfHandler not initialized!");
    }
  }

  private static class Mappings {
    private final Map<String, String> mappings = new HashMap<String, String>(5);

    Mappings() {
      // 1.7.10
      // classes
      mappings.put(String.format("1.7.10|CL|%s", CL_ENTITY_PLAYER), "yz");
      mappings.put(String.format("1.7.10|CL|%s", CL_PLAYER_CAPABILITIES), "yw");

      // methods
      mappings.put(String.format("1.7.10|MT|%s|%s", CL_ENTITY_PLAYER, MT_GET_BREAK_SPEED),
          MT_GET_BREAK_SPEED);//added by forge, not obfuscated

      // fields
      mappings.put(String.format("1.7.10|FD|%s|%s", CL_ENTITY_PLAYER, FD_ON_GROUND), "D");
      mappings.put(String.format("1.7.10|FD|%s|%s", CL_ENTITY_PLAYER, FD_CAPABILITIES), "bE");
      mappings.put(String.format("1.7.10|FD|%s|%s", CL_PLAYER_CAPABILITIES, FD_IS_FLYING), "b");
    }


    public String get(String x) {
      return mappings.get(x);
    }
  }
}

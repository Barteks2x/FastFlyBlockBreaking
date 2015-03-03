package com.github.barteks2x.fastflybreak.asm;

public class Util {
  private Util() {
    throw new AssertionError();
  }

  public static String toJvmName(String name) {
    return name.replace('.', '/');
  }
}

package com.github.barteks2x.fastflybreak.asm;

import java.util.Iterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Patcher implements IClassTransformer {
  @Override
  public byte[] transform(String name, String transformedName, byte[] basicClass) {
    if (name.equals("net.minecraft.entity.player.EntityPlayer") || name.equals("yz")) {
      return this.applyPlayerTransform(basicClass);
    }
    return basicClass;
  }

  private byte[] applyPlayerTransform(byte[] data) {
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(data);
    classReader.accept(classNode, 0);

    @SuppressWarnings("unchecked")
    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext()) {
      MethodNode meth = methods.next();
      if (!meth.name.equals("getBreakSpeed"))// method added by forge, deobfuscated
      {
        continue;
      }
      findAndModifyMethod(meth.instructions);
    }
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    classNode.accept(writer);
    return writer.toByteArray();
  }

  private void findAndModifyMethod(InsnList instructions) {
    Iterator<AbstractInsnNode> it = instructions.iterator();
    int i = -1;// so that after the first i++ it's 0
    boolean found = false;
    boolean isObf = true;
    while (it.hasNext()) {
      i++;
      AbstractInsnNode node = it.next();
      if (node.getOpcode() != Opcodes.GETFIELD) {
        continue;
      }
      FieldInsnNode fieldNode = (FieldInsnNode) node;
      if (fieldNode.name.equals("onGround") || fieldNode.name.equals("D")) {
        if (!fieldNode.desc.equals("Z"))
          throw new AssertionError("onGround field is not boolean!");
        if (fieldNode.name.equals("onGround"))
          isObf = false;
        found = true;
        break;
      }
    }
    if (!found) {
      return;
    }

    String entityPlayer = isObf ? "yz" : "net/minecraft/entity/player/EntityPlayer";
    String capabilities = isObf ? "bE" : "capabilities";
    String playerCapabilities = isObf ? "yw" : "net/minecraft/entity/player/PlayerCapabilities";
    String isFlying = isObf ? "b" : "isFlying";

    AbstractInsnNode insnStart = instructions.get(i + 1);

    AbstractInsnNode loadThis = new VarInsnNode(Opcodes.ALOAD, 0);
    AbstractInsnNode loadCapabilities =
        new FieldInsnNode(Opcodes.GETFIELD, entityPlayer, capabilities, "L" + playerCapabilities
            + ";");
    AbstractInsnNode loadIsFlying =
        new FieldInsnNode(Opcodes.GETFIELD, playerCapabilities, isFlying, "Z");
    AbstractInsnNode or = new InsnNode(Opcodes.IOR);

    instructions.insertBefore(insnStart, loadThis);
    instructions.insertBefore(insnStart, loadCapabilities);
    instructions.insertBefore(insnStart, loadIsFlying);
    instructions.insertBefore(insnStart, or);
  }
}

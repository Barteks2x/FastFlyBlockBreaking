package com.github.barteks2x.fastflybreak.asm;

import static com.github.barteks2x.fastflybreak.asm.ObfHandler.CL_ENTITY_PLAYER;
import static com.github.barteks2x.fastflybreak.asm.ObfHandler.CL_PLAYER_CAPABILITIES;
import static com.github.barteks2x.fastflybreak.asm.ObfHandler.FD_CAPABILITIES;
import static com.github.barteks2x.fastflybreak.asm.ObfHandler.FD_IS_FLYING;
import static com.github.barteks2x.fastflybreak.asm.ObfHandler.FD_ON_GROUND;
import static com.github.barteks2x.fastflybreak.asm.ObfHandler.MT_GET_BREAK_SPEED;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.IOR;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Patcher implements IClassTransformer {
  private final ObfHandler obfHandler = new ObfHandler();

  @Override
  public byte[] transform(String name, String transformedName, byte[] basicClass) {
    if (obfHandler.checkClassesEqualAndInit(CL_ENTITY_PLAYER, name)) {
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
      if (!obfHandler.methodsEqual(CL_ENTITY_PLAYER, MT_GET_BREAK_SPEED, meth.name)) {
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
    Queue<Integer> isOnGroundOccurrences = new ArrayDeque<Integer>();
    while (it.hasNext()) {
      i++;
      AbstractInsnNode node = it.next();
      if (node.getOpcode() != GETFIELD) {
        continue;
      }
      FieldInsnNode fieldNode = (FieldInsnNode) node;
      if (obfHandler.fieldsEqual(CL_ENTITY_PLAYER, FD_ON_GROUND, fieldNode.name)) {
        if (!fieldNode.desc.equals("Z")) {
          throw new RuntimeException("onGround field is not boolean!");
        }
        isOnGroundOccurrences.add(i);
      }
    }
    if (isOnGroundOccurrences.isEmpty()) {
      return;
    }

    String clEntityPlayer = Util.toJvmName(obfHandler.getClassName(CL_ENTITY_PLAYER));
    String fdCapabilities = obfHandler.getFieldName(CL_ENTITY_PLAYER, FD_CAPABILITIES);
    String clCapabilities = Util.toJvmName(obfHandler.getClassName(CL_PLAYER_CAPABILITIES));
    String fdIsFlying = obfHandler.getFieldName(CL_PLAYER_CAPABILITIES, FD_IS_FLYING);

    /**
     * Elements are inserted into queue from gthe first to the last, so we process it from the last
     * to the first This way after applying changes, no instructions modified later will move
     */
    while (!isOnGroundOccurrences.isEmpty()) {
      i = isOnGroundOccurrences.remove();

      AbstractInsnNode insnStart = instructions.get(i + 1);

      AbstractInsnNode loadThis = new VarInsnNode(ALOAD, 0);
      AbstractInsnNode loadCapabilities =
          new FieldInsnNode(GETFIELD, clEntityPlayer, fdCapabilities, "L" + clCapabilities + ";");
      AbstractInsnNode loadIsFlying = new FieldInsnNode(GETFIELD, clCapabilities, fdIsFlying, "Z");
      AbstractInsnNode or = new InsnNode(IOR);

      instructions.insertBefore(insnStart, loadThis);
      instructions.insertBefore(insnStart, loadCapabilities);
      instructions.insertBefore(insnStart, loadIsFlying);
      instructions.insertBefore(insnStart, or);
    }
  }
}

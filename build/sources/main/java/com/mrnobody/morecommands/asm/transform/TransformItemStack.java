package com.mrnobody.morecommands.asm.transform;

import java.util.ListIterator;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.ImmutableSet;
import com.mrnobody.morecommands.asm.ASMNames;
import com.mrnobody.morecommands.asm.NodeTransformer;

/**
 * Transforms several methods of {@link net.minecraft.item.ItemStack}:
 * 
 * <ul>
 * <li>
 * Transforms {@link net.minecraft.item.ItemStack#tryPlaceItemIntoWorld(net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int, int, float, float, float)}
 * so that an {@link com.mrnobody.morecommands.event.ItemStackChangeSizeEvent} is fired after the ItemStack's
 * stack size has changed. This allows to manipulate stack size.
 * </li>
 * <li>
 * Transforms {@link net.minecraft.item.ItemStack#damageItem(int, net.minecraft.entity.EntityLivingBase)} so that an
 * {@link com.mrnobody.morecommands.event.DamageItemEvent} is fired before the Item will be damaged. This allows to
 * prevent items from being damaged.
 * </li>
 * </ul>
 * 
 * @author MrNobody98
 */
public class TransformItemStack extends NodeTransformer {
	private static final ASMNames.Method GET_ITEM = ASMNames.Method.ItemStack_getItem;
	private static final ASMNames.Method ON_ITEM_USE = ASMNames.Method.Item_onItemUse;
	private static final ASMNames.Method CHANGE_SIZE_EVENT_INIT = ASMNames.Method.ItemStackChangeSizeEvent_init;
	private static final ASMNames.Method PLACE_ITEM = ASMNames.Method.ItemStack_onItemUse;
	private static final ASMNames.Method POST = ASMNames.Method.EventHandler_post;
	private static final ASMNames.Method DAMAGE_ITEM = ASMNames.Method.ItemStack_damageItem;
	private static final ASMNames.Method DAMAGE_ITEM_EVENT_INIT = ASMNames.Method.DamageItemEvent_init;
	
	private static final ASMNames.Field STACK_SIZE = ASMNames.Field.ItemStack_stackSize;
	private static final ASMNames.Field EVENTHANDLER_CHANGE_SIZE = ASMNames.Field.EventHandler_ITEMSTACK_CHANGE_SIZE;
	private static final ASMNames.Field EVENTHANDLER_DAMAGE_ITEM = ASMNames.Field.EventHandler_DAMAGE_ITEM;
	private static final ASMNames.Field NEW_SIZE = ASMNames.Field.ItemStackChangeSizeEvent_newSize;
	private static final ASMNames.Field DAMAGE = ASMNames.Field.DamageItemEvent_damage;
	
	private static final ASMNames.Type CHANGE_SIZE_EVENT = ASMNames.Type.ItemStackChangeSizeEvent;
	private static final ASMNames.Type DAMAGE_ITEM_EVENT = ASMNames.Type.DamageItemEvent;
	private static final ASMNames.Type ENUM_ACTION_RESULT = ASMNames.Type.EnumActionResult;
	
	private final ImmutableSet<String> transformClasses = ImmutableSet.of(ASMNames.Type.ItemStack.getName());
	
	@Override
	public Set<String> getTransformClassNames() {
		return transformClasses;
	}

	@Override
	public void beforeTransform() {}

	@Override
	public void afterTransform() {}
	
	@Override
	public void transform(String className, ClassNode node) {
		for (MethodNode method : node.methods) {
			boolean transform1 = false, transform2 = false;
			
			if (method.name.equals(PLACE_ITEM.getEnvName()) && method.desc.equals(PLACE_ITEM.getDesc())) {
				transformOnItemUse(method);
				transform1 = true;
			}
			else if (method.name.equals(DAMAGE_ITEM.getEnvName()) && method.desc.equals(DAMAGE_ITEM.getDesc())) {
				transformDamageItem(method);
				transform2 = true;
			}
			
			if (transform1 && transform2) break;
		}
	}
	
	private void transformDamageItem(MethodNode method) {
		InsnList postEvent = new InsnList();
		LabelNode label = new LabelNode(new Label());
		
		postEvent.add(new TypeInsnNode(Opcodes.NEW, DAMAGE_ITEM_EVENT.getInternalName()));
		postEvent.add(new InsnNode(Opcodes.DUP));
		postEvent.add(new VarInsnNode(Opcodes.ALOAD, 2));
		postEvent.add(new VarInsnNode(Opcodes.ILOAD, 1));
		postEvent.add(new VarInsnNode(Opcodes.ALOAD, 0));
		postEvent.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, DAMAGE_ITEM_EVENT_INIT.getOwnerInternalName(), DAMAGE_ITEM_EVENT_INIT.getEnvName(), DAMAGE_ITEM_EVENT_INIT.getDesc(), false));
		postEvent.add(new VarInsnNode(Opcodes.ASTORE, 3));
		
		postEvent.add(new FieldInsnNode(Opcodes.GETSTATIC, EVENTHANDLER_DAMAGE_ITEM.getOwnerInternalName(), EVENTHANDLER_DAMAGE_ITEM.getEnvName(), EVENTHANDLER_DAMAGE_ITEM.getDesc()));
		postEvent.add(new VarInsnNode(Opcodes.ALOAD, 3));
		postEvent.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, POST.getOwnerInternalName(), POST.getEnvName(), POST.getDesc(), false));
		postEvent.add(new JumpInsnNode(Opcodes.IFEQ, label));
		postEvent.add(new InsnNode(Opcodes.RETURN));
		
		postEvent.add(label);
		postEvent.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[] {DAMAGE_ITEM_EVENT.getInternalName()}, 0, null));
		postEvent.add(new VarInsnNode(Opcodes.ALOAD, 3));
		postEvent.add(new FieldInsnNode(Opcodes.GETFIELD, DAMAGE.getOwnerInternalName(), DAMAGE.getEnvName(), DAMAGE.getDesc()));
		postEvent.add(new VarInsnNode(Opcodes.ISTORE, 1));
		
		method.instructions.insert(postEvent);
	}
	
	private void transformOnItemUse(MethodNode method) {
		int oldSizeIndex = -1; boolean inserted = false;
		
		for (ListIterator<AbstractInsnNode> iterator = method.instructions.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();
			
			if (!inserted && insn.getNext() instanceof VarInsnNode && insn.getNext().getNext() instanceof MethodInsnNode) {
				VarInsnNode next1 = (VarInsnNode) insn.getNext();
				MethodInsnNode next2 = (MethodInsnNode) next1.getNext();
				
				if (next1.getOpcode() == Opcodes.ALOAD && next1.var == 0 &&
					next2.getOpcode() == Opcodes.INVOKEVIRTUAL && next2.owner.equals(GET_ITEM.getOwnerInternalName()) && 
					next2.name.equals(GET_ITEM.getEnvName()) && next2.desc.equals(GET_ITEM.getDesc())) {
					
					oldSizeIndex = method.maxLocals; method.maxLocals++;
					
					InsnList saveOldSize = new InsnList();
					
					saveOldSize.add(new VarInsnNode(Opcodes.ALOAD, 0));
					saveOldSize.add(new FieldInsnNode(Opcodes.GETFIELD, STACK_SIZE.getOwnerInternalName(), STACK_SIZE.getEnvName(), STACK_SIZE.getDesc()));
					saveOldSize.add(new VarInsnNode(Opcodes.ISTORE, oldSizeIndex));
					
					method.instructions.insert(insn, saveOldSize);
					inserted = true;
				}
			}
			
			if (inserted && insn.getNext() instanceof MethodInsnNode && insn.getNext().getNext() instanceof VarInsnNode) {
				MethodInsnNode next1 = (MethodInsnNode) insn.getNext();
				VarInsnNode next2 = (VarInsnNode) next1.getNext();
				
				if (next1.getOpcode() == Opcodes.INVOKEVIRTUAL && next1.owner.equals(ON_ITEM_USE.getOwnerInternalName()) && 
					next1.name.equals(ON_ITEM_USE.getEnvName()) && next1.desc.equals(ON_ITEM_USE.getDesc()) &&
					next2.getOpcode() == Opcodes.ASTORE && next2.var == 9) {
					
					int eventIndex = method.maxLocals; method.maxLocals++;
					InsnList postEvent = new InsnList();
					LabelNode label = new LabelNode(new Label());
					
					postEvent.add(new TypeInsnNode(Opcodes.NEW, CHANGE_SIZE_EVENT.getInternalName()));
					postEvent.add(new InsnNode(Opcodes.DUP));
					postEvent.add(new VarInsnNode(Opcodes.ALOAD, 1));
					postEvent.add(new VarInsnNode(Opcodes.ALOAD, 0));
					postEvent.add(new VarInsnNode(Opcodes.ILOAD, oldSizeIndex));
					postEvent.add(new VarInsnNode(Opcodes.ALOAD, 0));
					postEvent.add(new FieldInsnNode(Opcodes.GETFIELD, STACK_SIZE.getOwnerInternalName(), STACK_SIZE.getEnvName(), STACK_SIZE.getDesc()));
					postEvent.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, CHANGE_SIZE_EVENT_INIT.getOwnerInternalName(), CHANGE_SIZE_EVENT_INIT.getEnvName(), CHANGE_SIZE_EVENT_INIT.getDesc(), false));
					postEvent.add(new VarInsnNode(Opcodes.ASTORE, eventIndex));
					postEvent.add(new FieldInsnNode(Opcodes.GETSTATIC, EVENTHANDLER_CHANGE_SIZE.getOwnerInternalName(), EVENTHANDLER_CHANGE_SIZE.getEnvName(), EVENTHANDLER_CHANGE_SIZE.getDesc()));
					postEvent.add(new VarInsnNode(Opcodes.ALOAD, eventIndex));
					postEvent.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, POST.getOwnerInternalName(), POST.getEnvName(), POST.getDesc(), false));
					postEvent.add(new JumpInsnNode(Opcodes.IFEQ, label));
					postEvent.add(new VarInsnNode(Opcodes.ALOAD, 0));
					postEvent.add(new VarInsnNode(Opcodes.ALOAD, eventIndex));
					postEvent.add(new FieldInsnNode(Opcodes.GETFIELD, NEW_SIZE.getOwnerInternalName(), NEW_SIZE.getEnvName(), NEW_SIZE.getDesc()));
					postEvent.add(new FieldInsnNode(Opcodes.PUTFIELD, STACK_SIZE.getOwnerInternalName(), STACK_SIZE.getEnvName(), STACK_SIZE.getDesc()));
					postEvent.add(label);
					postEvent.add(new FrameNode(Opcodes.F_APPEND, 3, new Object[] {ENUM_ACTION_RESULT.getInternalName(), Opcodes.INTEGER, CHANGE_SIZE_EVENT.getInternalName()}, 0, null));
					
					method.instructions.insert(next2, postEvent);
				}
			}
			
			if (inserted && insn instanceof FrameNode && insn.getNext() instanceof VarInsnNode && 
				insn.getNext().getNext() instanceof InsnNode && insn.getNext().getOpcode() == Opcodes.ALOAD && 
				((VarInsnNode) insn.getNext()).var == 9 && insn.getNext().getNext().getOpcode() == Opcodes.ARETURN) {
				
				method.instructions.insert(insn, new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
				method.instructions.remove(insn);
				break;
			}
		}
	}
}
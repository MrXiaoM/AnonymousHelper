package anonym.minecraft.mod;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ModTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if ("net.minecraft.network.play.client.CPacketChatMessage".equals(transformedName)) {
			
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(1);
			classReader.accept(new ModTransformer.CV(classWriter), 8);
			System.out.println("聊天包注入完毕");
			return classWriter.toByteArray();
		}
		if ("net.minecraft.client.gui.GuiMerchant".equals(transformedName)) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(1);
			classReader.accept(new ModTransformer.CV1(classWriter), 8);
			System.out.println("交易Gui修改完毕");
			return classWriter.toByteArray();
		}
		if ("net.minecraft.client.gui.GuiMultiplayer".equals(transformedName)) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(1);
			classReader.accept(new ModTransformer.CV2(classWriter), 8);
			System.out.println("多人游戏Gui修改完毕");
			return classWriter.toByteArray();
		}
		return basicClass;
	}

	class CV1 extends ClassVisitor {
		public CV1(final ClassVisitor cv) {
			super(327680, cv);
		}

		@Override
		public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
				final String[] exceptions) {
			System.out.println(name + " " + desc);
			if (name.equals("initGui") || (name.equals("b") && desc.equals("()V")) ) { // func_73866_w_
				System.out.println("注入 "+name+" "+desc);
				final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new ModTransformer.CV1.MV(327680, mv, (name.equals("b") && desc.equals("()V")));
			}
			if (name.equals("actionPerformed") || (name.equals("a") && desc.equals("(Lbja;)V"))) { // func_146284
				System.out.println("注入 "+name+" "+desc);
				final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new ModTransformer.CV1.MV1(327680, mv, (name.equals("a") && desc.equals("(Lbja;)V")));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		class MV extends MethodVisitor {

			Boolean mapping=false;
			public MV(int api, MethodVisitor methodVisitor,boolean mapping) {
				super(api, methodVisitor);
				final String tips = "禁止套娃!";
				tips.toString();
				this.mapping = mapping;
			}

			@Override
			public void visitInsn(int opcode) {
				// ARETURN || RETURN
				if (opcode == 176 || opcode == 177) {
					mv.visitVarInsn(25, 0); // ALOAD 0
					// GETFIELD List GuiMerchant.buttonList
					mv.visitFieldInsn(180, "net/minecraft/client/gui/GuiMerchant",
							mapping?"field_146292_n":"buttonList", "Ljava/util/List;");
					// INVOKESTATIC GuiButton FastTrade.getTradeButton()
					mv.visitMethodInsn(184, 
							"anonym/minecraft/mod/AEventHandler",
							"addTradeButton",
							"(Ljava/util/List;)V", 
							false);
				}
				super.visitInsn(opcode);
			}
		}
		class MV1 extends MethodVisitor {
			boolean mapping = false;
			public MV1(int api, MethodVisitor methodVisitor, boolean mapping) {
				super(api, methodVisitor);
				final String tips = "禁止套娃!";
				tips.toString();
				this.mapping = mapping;
			}

			@Override
			public void visitCode() {
				mv.visitVarInsn(25, 1); // ALOAD 1
				// INVOKESTATIC GuiButton FastTrade.getTradeButton()
				mv.visitMethodInsn(184, "anonym/minecraft/mod/AEventHandler", "clickTradeButton",
						"(Lnet/minecraft/client/gui/GuiButton;)V", false);
				
				super.visitCode();
			}
		}
	}

	class CV2 extends ClassVisitor {
		String cl;

		public CV2(final ClassVisitor cv) {
			super(327680, cv);
			this.cl = FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/client/gui/GuiMultiplayer");
		}

		@Override
		public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
				final String[] exceptions) {
			System.out.println(name + " " + desc);
			if (name.equals("initGui") || (name.equals("b") && desc.equals("()V")) ) { // func_73866_w_
				System.out.println("注入 "+name+" "+desc);
				final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new ModTransformer.CV2.MV(327680, mv, (name.equals("b") && desc.equals("()V")));
			}
			if (name.equals("actionPerformed") || (name.equals("a") && desc.equals("(Lbja;)V"))) { // func_146284_a
				System.out.println("注入 "+name+" "+desc);
				final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new ModTransformer.CV2.MV1(327680, mv, (name.equals("a") && desc.equals("(Lbja;)V")));
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		class MV extends MethodVisitor {
			Boolean mapping = false;
			public MV(int api, MethodVisitor methodVisitor,boolean mapping) {
				super(api, methodVisitor);
				final String tips = "禁止套娃!";
				tips.toString();
				this.mapping = mapping;
			}

			@Override
			public void visitInsn(int opcode) {
				// ARETURN || RETURN
				if (opcode == 176 || opcode == 177) {
					mv.visitVarInsn(25, 0); // ALOAD 0
					// GETFIELD List GuiMerchant.buttonList
					mv.visitFieldInsn(180, "net/minecraft/client/gui/GuiMultiplayer",
							mapping?"field_146292_n":"buttonList", "Ljava/util/List;");
					// INVOKESTATIC GuiButton FastTrade.getTradeButton()
					mv.visitMethodInsn(184, 
							"anonym/minecraft/mod/AEventHandler",
							"addChangeNameButton",
							"(Ljava/util/List;)V", 
							false);
				}
				super.visitInsn(opcode);
			}
		}
		class MV1 extends MethodVisitor {
			boolean mapping = false;
			public MV1(int api, MethodVisitor methodVisitor,boolean mapping) {
				super(api, methodVisitor);
				final String tips = "禁止套娃!";
				tips.toString();
				this.mapping = mapping;
			}

			@Override
			public void visitInsn(int opcode) {
				// ARETURN || RETURN
				if (opcode == 176 || opcode == 177) {
					mv.visitVarInsn(25, 1); // ALOAD 1
					// INVOKESTATIC GuiButton FastTrade.getTradeButton()
					mv.visitMethodInsn(184, "anonym/minecraft/mod/AEventHandler", 
							"clickChangeNameButton",
							mapping?"(Lbja;)V":"(Lnet/minecraft/client/gui/GuiButton;)V", false);
				}
				super.visitInsn(opcode);
			}
		}
	}
	class CV extends ClassVisitor {
		public CV(final ClassVisitor cv) {
			super(327680, cv);
		}

		@Override
		public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
				final String[] exceptions) {
			System.out.println(name + " " + desc);
			if (name.equals("<init>") && desc.equals("(Ljava/lang/String;)V")) {
				System.out.println("注入 "+name+" "+desc);
				final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				
				return new anonym.minecraft.mod.ModTransformer.CV.MV(327680, mv);
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

		class MV extends MethodVisitor {

			public MV(int api, MethodVisitor methodVisitor) {
				super(api, methodVisitor);
				final String tips = "禁止套娃!";
				tips.toString();
			}

			@Override
			public void visitInsn(int opcode) {
				// ARETURN || RETURN
				if (opcode == 176 || opcode == 177) {
					mv.visitVarInsn(25, 0); // ALOAD 0
					// INVOKESTATIC FMLPlugin.RM(C01PacketChatMessage;)V
					mv.visitMethodInsn(184, "anonym/minecraft/mod/AEventHandler", "handleItems",
							"(Lnet/minecraft/network/play/client/CPacketChatMessage;)V", false);
				}
				super.visitInsn(opcode);
			}
		}
	}
}

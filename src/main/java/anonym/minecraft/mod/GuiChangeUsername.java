package anonym.minecraft.mod;

import java.io.IOException;
import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

public class GuiChangeUsername extends GuiScreen{
	
	GuiScreen parent;
	GuiTextField tfInput;
	public GuiChangeUsername(GuiScreen parentScreen) {
		parent = parentScreen;
	}
	
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		tfInput = new GuiTextField(0, fontRenderer, width/2-100, height/2-5, 200, 20);
		tfInput.setMaxStringLength(16);
		tfInput.setFocused(false);
		tfInput.setCanLoseFocus(true);
		this.buttonList.add(new GuiButton(0, 
				this.width / 2 - 102, this.height / 2 +35,
				100, 20, "确认修改"));
		this.buttonList.add(new GuiButton(1, 
				this.width / 2 + 2, this.height / 2 +35,
				100, 20, "取消"));
	}
	
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	drawDefaultBackground();
    	drawCenteredString(fontRenderer, "修改玩家ID", width/2, height / 2 - 35, -1);
    	drawCenteredString(fontRenderer, "§7当前用户: " + mc.getSession().getUsername(), width/2, height / 2 - 23, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
		tfInput.drawTextBox();
    }
    
    protected void actionPerformed(GuiButton button)
    {
    	clickButton(button.id);
    }
    private void clickButton(int id) {
    	// 确认修改用户名
    	if(id == 0) {
    		String ID = tfInput.getText();
            try {
                Class<Session> c = net.minecraft.util.Session.class;
                boolean a = false;
                for(Field f : c.getDeclaredFields()) {
                	if(f.getName() == "username") {
                		a = true;
                		break;
                	}
                }
                Field playerID;
                if(a) playerID = c.getDeclaredField("username");
                else playerID = c.getDeclaredField("field_74286_b");
                
                playerID.setAccessible(true);
                playerID.set(mc.getSession(), ID);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e){
                e.printStackTrace();
            }
    		mc.displayGuiScreen(parent);
    	}
    	// 返回
    	if(id == 1) {
    		mc.displayGuiScreen(parent);
    	}
    }
    
    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
    	if(par2 == Keyboard.KEY_RETURN) {
    		this.clickButton(0);
    	}
        if(tfInput.textboxKeyTyped(par1, par2)) //向文本框传入输入的内容
            return;
        super.keyTyped(par1, par2);
    }
     
    @Override
    protected void mouseClicked(int par1, int par2, int par3) throws IOException {
        tfInput.mouseClicked(par1, par2, par3); //调用文本框的鼠标点击检查
        super.mouseClicked(par1, par2, par3);
    }
     
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false); //关闭键盘连续输入
    }
}

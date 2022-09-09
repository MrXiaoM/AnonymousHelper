package anonym.minecraft.mod;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModInfo extends DummyModContainer {
	public ModInfo() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "AnonymousHelper";
		meta.name = "匿名者工具组";
		meta.version = "2.7.0";
		meta.authorList = Arrays.asList("AnonymTechnology");
		meta.credits = "Forge 和 FML 团队";
		meta.description = "让你可以在村民兑换页面中进行快速兑换，显示区块边界，修改玩家名称，还有无限夜视的功能";
		meta.url = "https://github.com/AnonymTechnology/AnonymousHelper";
		
		new CPacketChatMessage("awa");
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		Main.logger = event.getModLog();
	}

	@Subscribe
	public void init(FMLInitializationEvent event)  {
		Main.key_fullbright = new KeyBinding("开启或关闭无限夜视", 38, "匿名者");
		Main.key_chunkedge = new KeyBinding("切换区块边界显示", 67, "匿名者");
		ClientRegistry.registerKeyBinding(Main.key_fullbright);
		ClientRegistry.registerKeyBinding(Main.key_chunkedge);
		Main.eventHandler = new AEventHandler();
		MinecraftForge.EVENT_BUS.register(Main.eventHandler);
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}

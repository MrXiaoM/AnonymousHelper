package anonym.minecraft.mod;

import java.util.Base64;
import java.util.Map;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class Main implements IFMLLoadingPlugin {
	protected static KeyBinding key_fullbright;
	protected static KeyBinding key_chunkedge;
	protected static String awa = getString("RGlzY28=");
	protected static String awa2 = getString("aW8=");
	protected static org.apache.logging.log4j.Logger logger;
	protected static AEventHandler eventHandler;

	public static String getString(String keyName) {
		return new String(Base64.getDecoder().decode(keyName));
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "anonym.minecraft.mod.ModTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return "anonym.minecraft.mod.ModInfo";
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

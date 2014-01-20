package hunternif.mc.rings;

import cpw.mods.fml.common.registry.GameRegistry;

public class ServerProxy extends CommonProxy {
	@Override
	public void init() {
		super.init();
		GameRegistry.registerPlayerTracker(new ServerRecipeUpdater());
	}
}

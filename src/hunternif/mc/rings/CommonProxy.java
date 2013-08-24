package hunternif.mc.rings;


public class CommonProxy {
	// Client stuff
	public void registerRenderers() {
	    // Nothing here as the server doesn't render graphics!
	}
	
	public void registerSounds() {}
	
	public void registerTickHandlers() {
		//TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
	}
}
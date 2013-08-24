package hunternif.mc.rings;

import java.util.logging.Level;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.FMLLog;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {}
	
	@Override
	public void registerSounds() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void registerTickHandlers() {
		/*TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);*/
	}
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
        try {
        	/*for (Sound sound : Sound.values()) {
        		event.manager.soundPoolSounds.addSound(sound.getName()+".ogg");
        	}*/
        }
        catch (Exception e) {
        	FMLLog.log(RingsOfPower.ID, Level.WARNING, "Failed to register one or more sounds.");
        }
    }
}
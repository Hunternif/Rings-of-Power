package hunternif.mc.rings;

import hunternif.mc.rings.config.Config;
import hunternif.mc.rings.config.ConfigLoader;
import hunternif.mc.rings.network.CustomPacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=RingsOfPower.ID, name=RingsOfPower.NAME, version=RingsOfPower.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=true, packetHandler=CustomPacketHandler.class, channels={RingsOfPower.CHANNEL})
public class RingsOfPower {
	public static final String ID = "ringsofpower";
	public static final String NAME = "Rings of Power";
	public static final String VERSION = "@@MOD_VERSION@@";
	public static final String CHANNEL = ID;
	
	@Instance(ID)
	public static RingsOfPower instance;
	
	public static Logger logger;
	
	public static final List<Item> itemList = new ArrayList<Item>();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigLoader.preLoad(config, Config.class);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		ConfigLoader.load(Config.class);
		
		GameRegistry.addShapedRecipe(new ItemStack(Config.commonRing.instance),
				"iii", "iXi", "iii", 'i', Item.ingotIron, 'X', Item.bucketLava);
		
		TickRegistry.registerTickHandler(Config.fireRing.instance, Side.SERVER);
		TickRegistry.registerTickHandler(Config.flyRing.instance, Side.SERVER);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
}

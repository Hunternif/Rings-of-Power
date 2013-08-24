package hunternif.mc.rings;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid=RingsOfPower.ID, name=RingsOfPower.NAME, version=RingsOfPower.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=true)
public class RingsOfPower {
	public static final String ID = "RingsOfPower";
	public static final String NAME = "Rings of Power";
	public static final String VERSION = "0";
	public static final String CHANNEL = ID;
}

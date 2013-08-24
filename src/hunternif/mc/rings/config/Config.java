package hunternif.mc.rings.config;

import hunternif.mc.rings.item.FireRing;
import hunternif.mc.rings.item.ModItem;
import hunternif.mc.rings.item.TeleportRing;
import net.minecraft.item.Item;

public class Config {
	public static CfgInfo<ModItem> commonRing = new CfgInfo<ModItem>(26950, "Common Ring");
	public static CfgInfo<TeleportRing> tpRing = new CfgInfo<TeleportRing>(26951, "Teleport Ring").setCoreItem(Item.enderPearl);
	public static CfgInfo<FireRing> fireRing = new CfgInfo<FireRing>(26952, "Fire Ring").setCoreItem(Item.flintAndSteel);
}

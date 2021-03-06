package hunternif.mc.rings.config;

import hunternif.mc.rings.item.FireRing;
import hunternif.mc.rings.item.FlyingRing;
import hunternif.mc.rings.item.HarvestRing;
import hunternif.mc.rings.item.IceRing;
import hunternif.mc.rings.item.ModItem;
import hunternif.mc.rings.item.TeleportRing;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Config {
	public static CfgInfo<ModItem> commonRing = new CfgInfo<ModItem>(26950, "Common Ring");
	public static CfgInfo<TeleportRing> tpRing = new CfgInfo<TeleportRing>(26951, "Teleport Ring").addCoreIngredient(Item.enderPearl);
	public static CfgInfo<FireRing> fireRing = new CfgInfo<FireRing>(26952, "Fire Ring").addCoreIngredient(Item.flintAndSteel);
	public static CfgInfo<IceRing> iceRing = new CfgInfo<IceRing>(26953, "Ice Ring").addCoreIngredient(Block.blockSnow).addCoreIngredient(Block.ice);
	public static CfgInfo<HarvestRing> harvestRing = new CfgInfo<HarvestRing>(26954, "Harvest Ring")
			.addCoreIngredient(Item.seeds).addCoreIngredient(Item.melonSeeds).addCoreIngredient(Item.pumpkinSeeds).addCoreIngredient(Block.sapling);
	public static CfgInfo<FlyingRing> flyRing = new CfgInfo<FlyingRing>(26955, "Flying Ring").addCoreIngredient(Item.feather);
}

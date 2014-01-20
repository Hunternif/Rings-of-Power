package hunternif.mc.rings;

import hunternif.mc.rings.config.CfgInfo;
import hunternif.mc.rings.config.ConfigLoader;
import hunternif.mc.rings.item.PoweredRing;
import hunternif.mc.rings.network.SyncRecipePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/** Used to synchronize Rings of Power recipes on clients. */
public class ServerRecipeUpdater implements IPlayerTracker {

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		// Send recipe data to player:
		for (Item item : RingsOfPower.itemList) {
			if (item instanceof PoweredRing) {
				CfgInfo info = ConfigLoader.getItemInfoByID(item.itemID);
				if (info != null) {
					PacketDispatcher.sendPacketToPlayer(new SyncRecipePacket(
							info, ConfigLoader.ringOfPowerCommonIngredient).makePacket(), (Player) player);
				}
			}
		}
		//TODO: reset recipes on the client when the player disconnects!
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {}

}

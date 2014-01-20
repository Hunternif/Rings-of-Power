package hunternif.mc.rings.network;

import hunternif.mc.rings.RingsOfPower;
import hunternif.mc.rings.config.CfgInfo;
import hunternif.mc.rings.config.ConfigLoader;
import hunternif.mc.rings.item.PoweredRing;
import net.minecraft.item.Item;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * A connection handler that does the following things:
 * <ol>
 * <li>Sends recipe data from server to clients when a player connects</li>
 * <li>Resets recipe data on the client upon disconnection, reading it from the
 * mod config file</li>
 * </ol>
 */
public class RecipeUpdaterConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
		// Check if this is integrated server, running on the JVM as the client:
		if (manager instanceof MemoryConnection) {
			return;
		}
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
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {}

	@Override
	public void connectionClosed(INetworkManager manager) {
		// Check if this is integrated server, running on the JVM as the client:
		if (manager instanceof MemoryConnection) {
			return;
		}
		ConfigLoader.resetRecipes(RingsOfPower.config);
		RingsOfPower.logger.info("Recipes have been reset");
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler,
			INetworkManager manager, Packet1Login login) {}

}

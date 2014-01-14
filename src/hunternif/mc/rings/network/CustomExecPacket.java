package hunternif.mc.rings.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public abstract class CustomExecPacket extends CustomPacket {
	protected enum PacketDirection {
		CLIENT_TO_SERVER(false, true), SERVER_TO_CLIENT(true, false), BOTH(true, true);
		public final boolean toClient;
		public final boolean toServer;
		private PacketDirection(boolean toClient, boolean toServer) {
			this.toClient = toClient;
			this.toServer = toServer;
		}
	}
	
	@Override
	public final void process(ByteArrayDataInput in, EntityPlayer player, Side side)
			throws IOException, ProtocolException {
		if (side.isClient() && !getPacketDirection().toClient) {
			throw new ProtocolException("Can't send " + getClass().getSimpleName() + " to client");
		} else if (!side.isClient() && !getPacketDirection().toServer) {
			throw new ProtocolException("Can't send " + getClass().getSimpleName() + " to server");
		}
		read(in);
		execute(player, side);
	}
	
	public abstract PacketDirection getPacketDirection();
	
	public abstract void read(ByteArrayDataInput in) throws IOException;
	
	public abstract void execute(EntityPlayer player, Side side) throws ProtocolException;
}

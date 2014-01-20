package hunternif.mc.rings.network;

import hunternif.mc.rings.RingsOfPower;
import hunternif.mc.rings.util.ZipUtil;

import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * @author credits to diesieben07
 */
public abstract class CustomPacket {
	private static final BiMap<Integer, Class<? extends CustomPacket>> idMap;

	static {
		ImmutableBiMap.Builder<Integer, Class<? extends CustomPacket>> builder = ImmutableBiMap.builder();
		
		builder.put(Integer.valueOf(0), EffectPacket.class);
		builder.put(Integer.valueOf(1), SyncRecipePacket.class);
		
		idMap = builder.build();
	}

	public static CustomPacket constructPacket(int packetId)
			throws ProtocolException, InstantiationException, IllegalAccessException {
		Class<? extends CustomPacket> clazz = idMap.get(Integer.valueOf(packetId));
		if (clazz == null) {
			throw new ProtocolException("Unknown Packet Id!");
		} else {
			return clazz.newInstance();
		}
	}

	public static class ProtocolException extends Exception {
		public ProtocolException() {
		}
		public ProtocolException(String message, Throwable cause) {
			super(message, cause);
		}
		public ProtocolException(String message) {
			super(message);
		}
		public ProtocolException(Throwable cause) {
			super(cause);
		}
	}

	public final int getPacketId() {
		if (idMap.inverse().containsKey(getClass())) {
			return idMap.inverse().get(getClass()).intValue();
		} else {
			throw new RuntimeException("Packet " + getClass().getSimpleName() + " is missing a mapping!");
		}
	}
	
	public final Packet makePacket() {
		ByteArrayDataOutput dataOut = ByteStreams.newDataOutput();
		try {
			write(dataOut);
		} catch (IOException e) {
			RingsOfPower.logger.severe("Error writing packet: " + e.toString());
		}
		byte[] data = dataOut.toByteArray();
		if (isCompressed()) {
			data = ZipUtil.compressByteArray(data);
		}
		ByteArrayDataOutput packetOut = ByteStreams.newDataOutput();
		packetOut.writeByte(getPacketId());
		packetOut.write(data);
		return PacketDispatcher.getPacket(RingsOfPower.CHANNEL, packetOut.toByteArray());
	}
	
	protected boolean isCompressed() {
		return false;
	}

	public abstract void write(ByteArrayDataOutput out) throws IOException;
	
	public abstract void process(ByteArrayDataInput in, EntityPlayer player, Side side)
			throws IOException, ProtocolException;
	
	protected enum PacketDirection {
		CLIENT_TO_SERVER(false, true), SERVER_TO_CLIENT(true, false), BOTH(true, true);
		public final boolean toClient;
		public final boolean toServer;
		private PacketDirection(boolean toClient, boolean toServer) {
			this.toClient = toClient;
			this.toServer = toServer;
		}
	}
	
	public abstract PacketDirection getPacketDirection();
	
	protected void writeCompressedNBT(NBTTagCompound tag, ByteArrayDataOutput out) {
		try {
			byte[] bytes = CompressedStreamTools.compress(tag);
			out.writeShort((short)bytes.length);
			out.write(bytes);
		} catch (IOException e) {
			RingsOfPower.logger.log(Level.SEVERE, "Error compressing NBT tag in packet", e);
		}
	}
}
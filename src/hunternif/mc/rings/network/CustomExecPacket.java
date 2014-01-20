package hunternif.mc.rings.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public abstract class CustomExecPacket extends CustomPacket {
	@Override
	public final void process(ByteArrayDataInput in, EntityPlayer player, Side side)
			throws IOException, ProtocolException {
		read(in);
		execute(player, side);
	}
	
	public abstract void read(ByteArrayDataInput in) throws IOException;
	
	public abstract void execute(EntityPlayer player, Side side) throws ProtocolException;
}

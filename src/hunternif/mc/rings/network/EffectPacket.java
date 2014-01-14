package hunternif.mc.rings.network;

import hunternif.mc.rings.effect.Effect;
import hunternif.mc.rings.effect.EffectInstance;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class EffectPacket extends CustomExecPacket {
	private int effectID;
	private Object[] data;
	
	public EffectPacket() {}
	
	public EffectPacket(EffectInstance instance) {
		effectID = instance.effectID;
		data = instance.data;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeShort(effectID);
		Effect.effectList[effectID].writeInstanceData(data, out);
	}

	@Override
	public void read(ByteArrayDataInput in) throws IOException {
		effectID = in.readShort();
		data = Effect.effectList[effectID].readInstanceData(in);
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		EffectInstance effInst = new EffectInstance(effectID, data);
		effInst.perform();
	}

	@Override
	public PacketDirection getPacketDirection() {
		return PacketDirection.SERVER_TO_CLIENT;
	}

}

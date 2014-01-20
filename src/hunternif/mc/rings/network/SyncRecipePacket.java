package hunternif.mc.rings.network;

import hunternif.mc.rings.RingsOfPower;
import hunternif.mc.rings.config.CfgInfo;
import hunternif.mc.rings.config.ConfigLoader;
import hunternif.mc.rings.util.RecipeUtil;

import java.io.IOException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

/** Synchronizes recipe data for one Ring of Power on clients. */
public class SyncRecipePacket extends CustomExecPacket {

	private CfgInfo info;
	private Object commonIngredient;
	
	public SyncRecipePacket() {}
	
	public SyncRecipePacket(CfgInfo ringInfo, Object commonIngredient) {
		this.info = ringInfo;
		this.commonIngredient = commonIngredient;
	}
	
	@Override
	public void write(ByteArrayDataOutput out) throws IOException {
		writeIngredient(out, commonIngredient);
		out.writeShort(info.getID());
		// Write core ingredient variants:
		out.writeByte(info.getCoreIngredient().size());
		for (Object ingredient : info.getCoreIngredient()) {
			writeIngredient(out, ingredient);
		}
	}
	
	protected void writeIngredient(ByteArrayDataOutput out, Object ingredient) throws IOException {
		if (ingredient instanceof Block) {
			out.writeBoolean(true);
			out.writeShort(((Block) ingredient).blockID);
		} else if (ingredient instanceof Item) {
			out.writeBoolean(false);
			out.writeShort(((Item) ingredient).itemID);
		} else if (ingredient instanceof ItemStack) {
			out.writeBoolean(false);
			out.writeShort(((ItemStack) ingredient).itemID);
		} else {
			throw new IOException("Recipe ingredient is not a Block or an Item! " + ingredient.toString());
		}
	}
	protected Object readIngredient(ByteArrayDataInput in) throws IOException {
		boolean isBlock = in.readBoolean();
		if (isBlock) {
			return Block.blocksList[in.readShort()];
		} else {
			return Item.itemsList[in.readShort()];
		}
	}

	@Override
	public PacketDirection getPacketDirection() {
		return PacketDirection.SERVER_TO_CLIENT;
	}

	@Override
	public void read(ByteArrayDataInput in) throws IOException {
		commonIngredient = readIngredient(in);
		int id = in.readShort();
		info = ConfigLoader.getItemInfoByID(id);
		if (info == null) {
			RingsOfPower.logger.warning("SyncRecipe: CfgInfo not found for id " + id);
			return;
		}
		int coreIngredientsLength = in.readByte();
		Object[] coreIngredients = new Object[coreIngredientsLength];
		for (int j = 0; j < coreIngredientsLength; j++) {
			coreIngredients[j] = readIngredient(in);
		}
		info.setCoreIngredients(coreIngredients);
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (commonIngredient == null || info == null) return;
		ConfigLoader.ringOfPowerCommonIngredient = commonIngredient;
		
		// Delete all old recipes:
		List<IRecipe> recipes = RecipeUtil.findAllCraftingRecipes(info.getInstance());
		CraftingManager.getInstance().getRecipeList().removeAll(recipes);
		// Register new recipes:
		RecipeUtil.registerRingRecipe(info, commonIngredient);
		
		RingsOfPower.logger.info("Updated recipe for " + info.getName());
	}

}

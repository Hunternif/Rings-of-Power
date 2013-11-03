package hunternif.mc.rings.item;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class FlyingRing extends PoweredRing implements ITickHandler {
	private static final String TAG_FLYING_RING_SLOT = "RoPFlyingRingSlot";
	private static final String TAG_FUEL_UNIT_CONSUMPTION = "RoPFuelUnitCons";
	
	public FlyingRing(int id) {
		super(id);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isEquipped) {
		if (entity instanceof EntityPlayer && !entity.worldObj.isRemote) {
			entity.getEntityData().setInteger(TAG_FLYING_RING_SLOT, slot);
			EntityPlayer player = (EntityPlayer) entity;
			if (player.capabilities.isCreativeMode) {
				return;
			}
			if (hasFuel(stack, player)) {
				allowFlying(player);
			} else {
				forbidFlying(player);
			}
			if (player.capabilities.isFlying) {
				int fuelTicks = getFuelUnitTicks(stack);
				if (fuelTicks % 100 == 0) { // 20 ticks * 5 seconds
					consumeFuel(stack, player);
				}
				incrementFuelUnitTicks(stack);
			}
		}
	}
	
	@Override
	public boolean hasFuel(ItemStack stack, EntityPlayer player) {
		return super.hasFuel(stack, player) || getFuelUnitTicks(stack) % 100 != 0;
	}
	
	protected ItemStack findThisItem(IInventory inventory) {
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() == this) {
				return stack;
			}
		}
		return null;
	}
	
	protected void allowFlying(EntityPlayer player) {
		if (!player.capabilities.allowFlying) {
			player.capabilities.allowFlying = true;
			player.sendPlayerAbilities();
		}
	}
	protected void forbidFlying(EntityPlayer player) {
		if (player.capabilities.allowFlying) {
			player.capabilities.isFlying = false;
			player.capabilities.allowFlying = false;
			player.sendPlayerAbilities();
		}
	}
	
	/** Number of ticks consuming since last consumption of fuel. */
	public int getFuelUnitTicks(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			return 0;
		}
		return tag.getInteger(TAG_FUEL_UNIT_CONSUMPTION);
	}
	
	protected void incrementFuelUnitTicks(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		int curTicks = getFuelUnitTicks(stack);
		tag.setInteger(TAG_FUEL_UNIT_CONSUMPTION, curTicks + 1);
	}
	
	@Override
	public void consumeFuel(ItemStack stack, EntityPlayer player) {
		super.consumeFuel(stack, player);
		NBTTagCompound tag = stack.getTagCompound();
		tag.setInteger(TAG_FUEL_UNIT_CONSUMPTION, 0);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = (EntityPlayer) tickData[0];
		NBTTagCompound tag = player.getEntityData();
		if (tag.hasKey(TAG_FLYING_RING_SLOT)) {
			// Verify that the ring is in its designated slot:
			int slot = player.getEntityData().getInteger(TAG_FLYING_RING_SLOT);
			if (slot < 0 || slot >= 36 ||
					player.inventory.mainInventory[slot] == null ||
					player.inventory.mainInventory[slot].itemID != this.itemID) {
				tag.removeTag(TAG_FLYING_RING_SLOT);
				forbidFlying(player);
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return this.getUnlocalizedName();
	}
}

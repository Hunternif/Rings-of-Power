package hunternif.mc.rings.item;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class FlyingRing extends PoweredRing {
	private static final String TAG_FUEL_UNIT_CONSUMPTION = "RoPFuelUnitCons";
	
	public FlyingRing(int id) {
		super(id);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isEquipped) {
		if (entity instanceof EntityPlayer && !entity.worldObj.isRemote) {
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
	
	private Set<EntityPlayer> playersWithThisItem = Collections.synchronizedSet(new HashSet<EntityPlayer>());
	
	@ForgeSubscribe
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer && !event.entity.worldObj.isRemote) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.capabilities.isCreativeMode) {
				return;
			}
			ItemStack stack = findThisItem(player.inventory);
			if (stack != null) {
				if (!playersWithThisItem.contains(player)) {
					playersWithThisItem.add(player);
					if (hasFuel(stack, player)) {
						allowFlying(player);
					}
				}
			} else {
				if (playersWithThisItem.contains(player)) {
					forbidFlying(player);
					playersWithThisItem.remove(player);
				}
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
}

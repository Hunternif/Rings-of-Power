package hunternif.mc.rings.item;

import hunternif.mc.rings.RingsOfPower;
import hunternif.mc.rings.util.BlockUtil;

import java.util.EnumSet;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;

public class FireRing extends PoweredRing implements ITickHandler {
	private static final String TAG_FIRE_RING_SLOT = "RoPFireRingSlot";
	
	private static final String[] immuneToFireObfNames = {"field_70178_ae", "isImmuneToFire"};
	private static final int deltaYdown = 3;
	private static final int deltaYup = 4;
	
	public FireRing(int id) {
		super(id);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode && !hasFuel(itemStack, player)) {
			FMLLog.log(RingsOfPower.ID, Level.INFO, "No fuel in inventory!");
			return itemStack;
		}
		if (!world.isRemote) {
			int playerX = MathHelper.floor_double(player.posX);
			int playerY = MathHelper.floor_double(player.posY);
			int playerZ = MathHelper.floor_double(player.posZ);
			for (int x = playerX - 4; x <= playerX + 4; x++) {
				for (int z = playerZ - 4; z <= playerZ + 4; z++) {
					boolean foundSurface = false;
					int y = playerY;
					// Look down:
					while (Math.abs(playerY-y) <= deltaYdown) {
						if (BlockUtil.isSurfaceAt(world, x, y, z, true)) {
							foundSurface = true;
							break;
						} else {
							y--;
						}
					}
					if (!foundSurface) {
						y = playerY + 1;
						// Look up:
						while (Math.abs(playerY-y) <= deltaYup) {
							if (BlockUtil.isSurfaceAt(world, x, y, z, true)) {
								foundSurface = true;
								break;
							} else {
								y++;
							}
						}
					}
					if (foundSurface) {
						world.setBlock(x, y, z, Block.fire.blockID);
						if (world.getBlockId(x, y-1, z) == Block.obsidian.blockID) {
							world.setBlock(x, y-1, z, Block.lavaStill.blockID);
						}
					}
				}
			}
		}
		consumeFuel(itemStack, player);
		return itemStack;
	}
	
	private void setImmuneToFire(Entity entity, boolean value) {
		if (entity.isImmuneToFire() != value) {
			ObfuscationReflectionHelper.setPrivateValue(Entity.class, entity, value, immuneToFireObfNames);
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isEquipped) {
		if (entity instanceof EntityPlayer && !entity.worldObj.isRemote) {
			entity.getEntityData().setInteger(TAG_FIRE_RING_SLOT, slot);
			setImmuneToFire(entity, true);
		}
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = (EntityPlayer) tickData[0];
		NBTTagCompound tag = player.getEntityData();
		if (tag.hasKey(TAG_FIRE_RING_SLOT)) {
			// Verify that the ring is in its designated slot:
			int slot = player.getEntityData().getInteger(TAG_FIRE_RING_SLOT);
			if (slot < 0 || slot >= 36 ||
					player.inventory.mainInventory[slot] == null ||
					player.inventory.mainInventory[slot].itemID != this.itemID) {
				tag.removeTag(TAG_FIRE_RING_SLOT);
				setImmuneToFire(player, false);
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

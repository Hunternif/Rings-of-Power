package hunternif.mc.rings.item;

import hunternif.mc.rings.RingsOfPower;
import hunternif.mc.rings.util.BlockUtil;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public class IceRing extends PoweredRing {
	private static final int deltaYdown = 3;
	private static final int deltaYup = 4;

	public IceRing(int id) {
		super(id);
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
						int blockIdBelow = world.getBlockId(x, y-1, z);
						if (blockIdBelow == Block.waterMoving.blockID || blockIdBelow == Block.waterStill.blockID) {
							world.setBlock(x, y-1, z, Block.ice.blockID);
						} else {
							world.setBlock(x, y, z, Block.snow.blockID);
						}
					}
				}
			}
		}
		consumeFuel(itemStack, player);
		return itemStack;
	}
}

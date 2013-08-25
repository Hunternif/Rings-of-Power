package hunternif.mc.rings.item;

import hunternif.mc.rings.RingsOfPower;
import hunternif.mc.rings.util.BlockUtil;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public class HarvestRing extends PoweredRing {
	private static final int deltaYdown = 3;
	private static final int deltaYup = 4;

	public HarvestRing(int id) {
		super(id);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			if (!player.capabilities.isCreativeMode && !hasFuel(itemStack, player)) {
				FMLLog.log(RingsOfPower.ID, Level.INFO, "No fuel in inventory!");
				return itemStack;
			}
			int playerX = MathHelper.floor_double(player.posX);
			int playerY = MathHelper.floor_double(player.posY);
			int playerZ = MathHelper.floor_double(player.posZ);
			for (int x = playerX - 4; x <= playerX + 4; x++) {
				for (int z = playerZ - 4; z <= playerZ + 4; z++) {
					boolean foundCrop = false;
					int y = playerY;
					// Look down:
					while (Math.abs(playerY-y) <= deltaYdown) {
						if (BlockUtil.isCrop(world, x, y, z)) {
							foundCrop = true;
							break;
						} else {
							y--;
						}
					}
					if (!foundCrop) {
						y = playerY + 1;
						// Look up:
						while (Math.abs(playerY-y) <= deltaYup) {
							if (BlockUtil.isCrop(world, x, y, z)) {
								foundCrop = true;
								break;
							} else {
								y++;
							}
						}
					}
					if (foundCrop) {
						int blockID = world.getBlockId(x, y, z);
						if (blockID == Block.crops.blockID) {
							Block.crops.harvestBlock(world, player, x, y, z, world.getBlockMetadata(x, y, z));
						} else if (blockID == Block.carrot.blockID) {
							Block.carrot.harvestBlock(world, player, x, y, z, world.getBlockMetadata(x, y, z));
						} else if (blockID == Block.potato.blockID) {
							Block.potato.harvestBlock(world, player, x, y, z, world.getBlockMetadata(x, y, z));
						}
						world.setBlockToAir(x, y, z);
					}
				}
			}
		}
		return itemStack;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world,
			int blockID, int x, int y, int z, EntityLivingBase entity) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (!world.isRemote && blockID == Block.leaves.blockID &&
				(metadata & 3) == 0 // This means rainforest leaves, I think
				&& Math.random() < 0.4)
		{
			world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(Item.appleRed)));
		}
		return false;
	}
}

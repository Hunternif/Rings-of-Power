package hunternif.mc.rings.item;

import hunternif.mc.rings.effect.Effect;
import hunternif.mc.rings.effect.EffectInstance;
import hunternif.mc.rings.network.EffectPacket;
import hunternif.mc.rings.util.BlockUtil;
import hunternif.mc.rings.util.NetworkUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class IceRing extends PoweredRing {
	private static final int deltaYdown = 3;
	private static final int deltaYup = 4;

	public IceRing(int id) {
		super(id);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode && !hasFuel(itemStack, player)) {
			//RingsOfPower.logger.info("No fuel in inventory!");
			return itemStack;
		}
		if (!world.isRemote) {
			int playerX = MathHelper.floor_double(player.posX);
			int playerY = MathHelper.floor_double(player.posY);
			int playerZ = MathHelper.floor_double(player.posZ);
			
			// Show effect
			EffectInstance effect = new EffectInstance(Effect.snow, player);
			NetworkUtil.sendToAllAround(new EffectPacket(effect).makePacket(), player);
			
			for (int x = playerX - 4; x <= playerX + 4; x++) {
				for (int z = playerZ - 4; z <= playerZ + 4; z++) {
					boolean foundSurface = false;
					int y = playerY;
					int surfaceY = y;
					// Look down:
					while (Math.abs(playerY-y) <= deltaYdown) {
						// Extinguish all fire:
						if (world.getBlockId(x, y, z) == Block.fire.blockID) {
							world.playAuxSFXAtEntity(player, 1004, x, y, z, 0);
							world.setBlockToAir(x, y, z);
						}
						if (!foundSurface && BlockUtil.isSurfaceAt(world, x, y, z, true, false)) {
							foundSurface = true;
							surfaceY = y;
							//break; Don't break so all fire can be extinguished
						}
						y--;
					}
					// Look up:
					y = playerY + 1;
					while (Math.abs(playerY-y) <= deltaYup) {
						// Extinguish all fire:
						if (world.getBlockId(x, y, z) == Block.fire.blockID) {
							world.playAuxSFXAtEntity(player, 1004, x, y, z, 0);
							world.setBlockToAir(x, y, z);
						}
						if (!foundSurface && BlockUtil.isSurfaceAt(world, x, y, z, true, false)) {
							foundSurface = true;
							surfaceY = y;
							//break; Don't break so all fire can be extinguished
						}
						y++;
					}
					if (foundSurface) {
						// Set water to ice:
						int blockIdBelow = world.getBlockId(x, surfaceY-1, z);
						if (blockIdBelow == Block.waterMoving.blockID || blockIdBelow == Block.waterStill.blockID) {
							world.setBlock(x, surfaceY-1, z, Block.ice.blockID);
						}
						// Turn moving lava to cobblestone: 
						else if (blockIdBelow == Block.lavaMoving.blockID) {
							world.setBlock(x, surfaceY-1, z, Block.cobblestone.blockID);
						}
						// Turn still lava to obsidian:
						else if (blockIdBelow == Block.lavaStill.blockID) {
							world.setBlock(x, surfaceY-1, z, Block.obsidian.blockID);
						}
						// on other blocks place a layer snow:
						else {
							world.setBlock(x, surfaceY, z, Block.snow.blockID);
						}
					}
				}
			}
		}
		consumeFuel(itemStack, player);
		return itemStack;
	}
}

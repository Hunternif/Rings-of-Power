package hunternif.mc.rings.effect;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectFire extends EntityEffect {
	public static final int MAX_PARTICLES = 20;

	public EffectFire(int id) {
		super(id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void perform(Entity entity, Object ... data) {
		World world = Minecraft.getMinecraft().theWorld;
		Random rand = new Random();
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		for (int i = 0; i < MAX_PARTICLES; i++) {
			float yaw = (rand.nextFloat()*2.0F - 1.0F) * (float) Math.PI;
			float pitch = (rand.nextFloat() - 0.5F) * (float) Math.PI;
			double distance = rand.nextDouble() * 0.3 + 0.7;
			double cosYaw = (double) MathHelper.cos(yaw);
			double sinYaw = (double) MathHelper.sin(yaw);
			double cosPitch = (double) MathHelper.cos(pitch);
			double sinPitch = (double) MathHelper.sin(pitch);
			double rX = -sinYaw*cosPitch * distance;
			double rZ = cosYaw*cosPitch * distance;
			double rY = -sinPitch * distance;
			double velX = -sinYaw*cosPitch / (distance) * 0.2;
			double velZ = cosYaw*cosPitch / (distance) * 0.2;
			double velY = -sinPitch / (distance) * 0.2;
			world.spawnParticle("flame", x + rX, y + rY, z + rZ, velX, velY, velZ);
		}
	}
}

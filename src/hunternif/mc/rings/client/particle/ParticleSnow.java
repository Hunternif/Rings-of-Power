package hunternif.mc.rings.client.particle;

import java.util.Random;

import net.minecraft.world.World;

public class ParticleSnow extends ModParticle {
	public ParticleSnow(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		this.rand = new Random((long)((x + y + z)*100000));
		particleScale = 1;
		setRandomMaxAge(20, 30);
		randomizeVelocity(0.04);
		setTexturePositions(0, 0, 4, true);
		setFade(0, 0.8f);
	}
	
	public void onUpdate() {
		// Random swaying motion
		//randomizeVelocity(0.0125);
		
		// Gravity
		this.motionY -= 0.01;
		
		// Slowing down
		this.motionX *= 0.8;
		this.motionY *= 0.8;
		this.motionZ *= 0.8;

		super.onUpdate();
	}
}
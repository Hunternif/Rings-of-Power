package hunternif.mc.rings.client.particle;

import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Simple icon implementation that spans a complete image,
 * with full width and height. */
public class FullIcon implements Icon {
	private final int width, height;
	
	public FullIcon(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconWidth() {
		return width;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconHeight() {
		return height;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMinU() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMaxU() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getInterpolatedU(double d0) {
		return (float) d0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMinV() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMaxV() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getInterpolatedV(double d0) {
		return (float) d0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getIconName() {
		// TODO Auto-generated method stub
		return null;
	}

}

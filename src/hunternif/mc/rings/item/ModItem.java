package hunternif.mc.rings.item;

import hunternif.mc.rings.RingsOfPower;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModItem extends Item {

	public Item fuelItem;
	public Block fuelBlock;
	
	public ModItem(int id) {
		super(id);
		setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(RingsOfPower.ID + ":" + getUnlocalizedName().substring("item.".length()));
	}

}

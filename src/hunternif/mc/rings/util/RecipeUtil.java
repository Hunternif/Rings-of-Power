package hunternif.mc.rings.util;

import hunternif.mc.rings.config.CfgInfo;
import hunternif.mc.rings.config.Config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeUtil {
	/** Returns the list of crafting recipes that produces the specified Block /
	 * Item. */
	public static List<IRecipe> findAllCraftingRecipes(Object result) {
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		for (Object recipeObj : CraftingManager.getInstance().getRecipeList()) {
			IRecipe recipe = (IRecipe) recipeObj;
			ItemStack stack = recipe.getRecipeOutput();
			if (stack != null && (result instanceof Block && stack.itemID == ((Block)result).blockID
					|| result instanceof Item && stack.itemID == ((Item)result).itemID
					|| result instanceof ItemStack && stack.itemID == ((ItemStack)result).itemID)) {
				recipes.add(recipe);
			}
		}
		return recipes;
	}
	
	/** Returns ID for a Block, an Item or an ItemStack, -1 otherwise. */
	public static int getObjectID(Object obj) {
		if (obj instanceof Block) {
			return ((Block)obj).blockID;
		} else if (obj instanceof Item) {
			return ((Item)obj).itemID;
		} else if (obj instanceof ItemStack) {
			return ((ItemStack)obj).itemID;
		}
		return -1;
	}
	
	/** Returns a list of recipe variants for a Ring of Power. */
	public static void registerRingRecipe(CfgInfo ringCfg, Object commonIngred) {
		for (Object coreItem : ringCfg.getCoreIngredient()) {
			GameRegistry.addRecipe(new ItemStack((Item)ringCfg.getInstance()), "CDC", "DRD", "CDC",
					'C', coreItem, 'D', commonIngred, 'R', Config.commonRing.getInstance());
		}
	}
}

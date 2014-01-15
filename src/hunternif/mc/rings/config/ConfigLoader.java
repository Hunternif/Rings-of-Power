package hunternif.mc.rings.config;

import hunternif.mc.rings.RingsOfPower;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ConfigLoader {
	public static final String CATEGORY_RECIPE = "recipe";
	
	/** Block or Item that is used as common ingredient for all Rings of Power,
	 * besides the Common Ring and the core ingredients for specific ring.
	 * Default is item diamond. */
	private static Object ringOfPowerCommonIngredient = Item.diamond;
	
	/**
	 * Preload data from the config, such as Item and Block IDs and recipes. 
	 * @param idsConfig Forge configuration file which holds Block and Item IDs.
	 * @param config a class containing static fields with Blocks and Items. */
	public static void preLoad(Configuration configFile, Class config) {
		try {
			configFile.load();
			configFile.addCustomCategoryComment(CATEGORY_RECIPE, "Below for each Ring of Power " +
					"is assigned a list of variants of core ingredient for its recipe. The " +
					"ingredient variants list is formatted as follows: \"block123, item234\", " +
					"where 123 and 234 is Block or Item ID respectively.");
			
			String commonIngredientProp = configFile.get(CATEGORY_RECIPE,
					"commonIngredient","item"+Item.diamond.itemID,
					"Block or Item that is used as common ingredient for all Rings of Power, " +
					"besides the Common Ring and the core ingredients for specific ring. " +
					"Default is item diamond.").getString();
			ringOfPowerCommonIngredient = parseRecipeIngredients(commonIngredientProp)[0];
			
			
			Field[] fields = config.getFields();
			for (Field field : fields) {
				if (field.getType().equals(CfgInfo.class)) {
					CfgInfo<?> info = (CfgInfo)field.get(null);
					info.initialize(field);
					
					// Read item/block id:
					int id = info.id;
					if (info.isBlock()) {
						id = configFile.getBlock(field.getName(), id).getInt();
					} else {
						id = configFile.getItem(field.getName(), id).getInt();
					}
					info.id = id;
					
					if (!info.equals(Config.commonRing)) {
						// Read Ring of Power core item ingredient variants:
						// (The default values are already set in the CfgInfo)
						String[] coreItemVariants = configFile.get(CATEGORY_RECIPE, field.getName(),
								writeIngredients(info.coreItems.toArray())).getStringList();
						info.setCoreItem(parseRecipeIngredients(coreItemVariants));
					}
				}
			}
		} catch(Exception e) {
			RingsOfPower.logger.log(Level.SEVERE, "Failed to load config", e);
		} finally {
			configFile.save();
		}
	}
	
	/** Parses a string of recipe ingredients in the format "block123, item345"
	 * where 123 and 345 are Block and Item id respectively. Returns an array of
	 * Blocks or Items. */
	public static Object[] parseRecipeIngredients(String ... entries) throws NumberFormatException {
		Object[] objects = new Object[entries.length];
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
			if (entry.startsWith("block")) {
				int id = Integer.parseInt(entry.substring("block".length()));
				objects[i] = Block.blocksList[id];
			} else if (entry.startsWith("item")) {
				int id = Integer.parseInt(entry.substring("item".length()));
				objects[i] = Item.itemsList[id];
			} 
		}
		return objects;
	}
	
	/** Prints the list of recipe ingredients in the format "block123, item234",
	 * where
	 * @param ingredients
	 * @return
	 */
	public static String[] writeIngredients(Object[] ingredients) {
		String[] strings = new String[ingredients.length];
		for (int i = 0; i < ingredients.length; i++) {
			Object ingredient = ingredients[i];
			if (ingredient instanceof Block) {
				strings[i] = "block" + ((Block)ingredient).blockID;
			} else if (ingredient instanceof Item) {
				strings[i] = "item" + ((Item)ingredient).itemID;
			} else if (ingredient instanceof ItemStack) {
				strings[i] = "item" + ((ItemStack)ingredient).itemID;
			}
		}
		return strings;
	}
	
	/** Instantiate items and blocks and register their recipes. */
	public static void load(Class config) {
		try {
			List<CfgInfo> itemsWithRecipes = new ArrayList<CfgInfo>();
			Field[] fields = config.getFields();
			// Parse fields to instantiate the items:
			for (Field field : fields) {
				if (field.getType().equals(CfgInfo.class)) {
					CfgInfo info = (CfgInfo)field.get(null);
					Constructor constructor = info.type.getConstructor(int.class);
					info.instance = constructor.newInstance(info.id);
					if (info.isBlock()) {
						((Block)info.instance).setUnlocalizedName(field.getName());
						GameRegistry.registerBlock((Block)info.instance, ItemBlock.class, field.getName(), RingsOfPower.ID);
						LanguageRegistry.addName(info.instance, info.name);
					} else {
						((Item)info.instance).setUnlocalizedName(field.getName());
						LanguageRegistry.addName(info.instance, info.name);
						GameRegistry.registerItem((Item)info.instance, field.getName(), RingsOfPower.ID);
						RingsOfPower.itemList.add((Item)info.instance);
						
					}
					RingsOfPower.logger.info("Registered item " + info.name);
					// Add recipe for rings of power
					if (!info.coreItems.isEmpty()) {
						for (Object coreItem : info.coreItems) {
							GameRegistry.addRecipe(new ItemStack((Item)info.instance), "CDC", "DRD", "CDC",
									'C', coreItem, 'D', ringOfPowerCommonIngredient, 'R', Config.commonRing.instance);
							RingsOfPower.logger.info("Added recipe for item " + info.name);
						}
					}
				}
			}
		} catch(Exception e) {
			RingsOfPower.logger.log(Level.SEVERE, "Failed to instantiate items", e);
		}
	}
}

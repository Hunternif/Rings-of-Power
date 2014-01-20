package hunternif.mc.rings.config;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CfgInfo<T> {
	protected T instance;
	/** At first this is default ID, then it is set by Configuration. */
	protected int id;
	protected final String name;
	/** List of variants of core ingredient for the recipe (the one that's in the
	 * corners of the crafting grid). */
	private List coreIngredient = new ArrayList();
	
	protected Class type;
	
	protected CfgInfo(int defaultID, String englishName) {
		this.id = defaultID;
		this.name = englishName;
	}
	
	protected void initialize(Field field) {
		this.type = (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
	}
	
	public int getID() {
		if (instance == null) return id;
		return isBlock() ? ((Block)instance).blockID : ((Item)instance).itemID;
	}
	public boolean isBlock() {
		return Block.class.isAssignableFrom(type);
	}
	public Class getType() {
		return type;
	}
	/** Adds a core ingredient variant. Block, Item or ItemStack. */
	public CfgInfo<T> addCoreIngredient(Object item) {
		coreIngredient.add(item);
		return this;
	}
	/** Replaces core ingredients with the given list. Block, Item or ItemStack. */
	public CfgInfo<T> setCoreIngredients(Object ... ingredientVariants) {
		coreIngredient = Arrays.asList(ingredientVariants);
		return this;
	}

	public T getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	public List getCoreIngredient() {
		return coreIngredient;
	}

}

package hunternif.mc.rings.config;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CfgInfo<T> {
	public T instance;
	protected int id;
	public String name;
	public Object coreItem;
	
	protected Class type;
	
	protected CfgInfo(int defaultID, String englishName) {
		this.id = defaultID;
		this.name = englishName;
	}
	
	protected void initialize(Field field) {
		this.type = (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
	}
	
	public int getID() {
		return isBlock() ? ((Block)instance).blockID : ((Item)instance).itemID;
	}
	public boolean isBlock() {
		return Block.class.isAssignableFrom(type);
	}
	public CfgInfo<T> setCoreItem(Object item) {
		this.coreItem = item;
		return this;
	}
}

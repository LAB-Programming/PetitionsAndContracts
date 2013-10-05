package net.clonecomputer.lab.petitionscontracts;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.bukkit.plugin.java.JavaPlugin;

public class PetitionsAndContracts extends JavaPlugin {
	
	static PetitionsAndContracts plugin;
	
	private PetitionStorage storage;

	@Override
	public void onEnable() {
		plugin = this;
		storage = new PetitionStorage(loadData());
		getCommand("petition").setExecutor(new PetitionCommandExecutor());
	}
	
	@Override
	public void onDisable() {
		plugin = null;
	}
	
	public PetitionStorage getPetitionStorage() {
		return storage;
	}
	
	private CaseInsensitiveMap loadData() {
		return new CaseInsensitiveMap();
	}
	
	public static String implode(String separator, Object[] array, int startIndex) {
		if(startIndex >= array.length) throw new IllegalArgumentException("startIndex is out of bounds of array", new ArrayIndexOutOfBoundsException(startIndex));
		StringBuilder string = new StringBuilder(array[startIndex].toString());
		for(int i = startIndex + 1; i < array.length; ++i) {
			string.append(separator);
			string.append(array[i]);
		}
		return string.toString();
	}
	
}

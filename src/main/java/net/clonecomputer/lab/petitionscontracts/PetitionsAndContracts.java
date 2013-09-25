package net.clonecomputer.lab.petitionscontracts;

import java.util.HashSet;
import java.util.Set;

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
	
	private Set<PetitionData> loadData() {
		return new HashSet<PetitionData>();
	}
	
	
	
}

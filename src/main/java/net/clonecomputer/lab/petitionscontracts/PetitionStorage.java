package net.clonecomputer.lab.petitionscontracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class PetitionStorage {
	
	private final CaseInsensitiveMap petitions;

	public PetitionStorage(CaseInsensitiveMap data) {
		petitions = data;
	}
	
	public boolean addPetition(String title, BookMeta petition, Player creator) {
		if(petitions.containsKey(title)) return false;
		petitions.put(title, new PetitionData(title, petition, creator));
		return true;
	}
	
	public List<PetitionData> toSortedList() {
		@SuppressWarnings("unchecked")
		ArrayList<PetitionData> list = new ArrayList<PetitionData>(petitions.values());
		Collections.sort(list);
		return list;
	}
	
	public PetitionData getPetitionData(String title) {
		return (PetitionData) petitions.get(title);
	}
}

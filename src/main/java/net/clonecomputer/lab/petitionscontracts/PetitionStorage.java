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
	
	public PetitionData addPetition(String title, BookMeta petition, Player creator) {
		if(petitions.containsKey(title)) return null;
		PetitionData newPetition = new PetitionData(title, petition, creator);
		petitions.put(title, newPetition);
		return newPetition;
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
	
	/**
	 * Soon to just shuffle the petition into a different list with options on how long closed petitions should be kept
	 */
	void closePetition(PetitionData petition) {
		petitions.remove(petition.getTitle());
	}
}

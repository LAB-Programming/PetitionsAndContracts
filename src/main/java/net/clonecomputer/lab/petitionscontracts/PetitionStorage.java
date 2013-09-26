package net.clonecomputer.lab.petitionscontracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PetitionStorage {
	
	private final Set<PetitionData> petitions;

	public PetitionStorage(Set<PetitionData> data) {
		petitions = data;
	}
	
	public void addPetition(PetitionData petition) {
		petitions.add(petition);
	}
	
	public List<PetitionData> toSortedList() {
		ArrayList<PetitionData> list = new ArrayList<PetitionData>(petitions);
		Collections.sort(list);
		return list;
	}
}

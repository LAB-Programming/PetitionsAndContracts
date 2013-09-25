package net.clonecomputer.lab.petitionscontracts;

import java.util.Set;

public class PetitionStorage {
	
	private final Set<PetitionData> petitions;

	public PetitionStorage(Set<PetitionData> data) {
		petitions = data;
	}
	
	public void addPetition(PetitionData petition) {
		petitions.add(petition);
	}
	
}

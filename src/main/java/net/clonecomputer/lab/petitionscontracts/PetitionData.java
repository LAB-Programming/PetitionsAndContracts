package net.clonecomputer.lab.petitionscontracts;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class PetitionData implements Comparable<PetitionData> {

	private String title;
	private BookMeta bookData;
	private Set<Player> signers;
	private Date timeIssued;
	private Set<Player> closers;
	
	/**
	 * Used when loading PetitionData from a file
	 */
	public static PetitionData load(String title, BookMeta bookData, Set<Player> signers, Date timeIssued, Set<Player> closers) {
		return new PetitionData(title, bookData, signers, timeIssued, closers);
	}
	
	private PetitionData(String name, BookMeta data, Set<Player> signersSet, Date issued, Set<Player> closersSet) {
		title = name;
		bookData = data;
		signers = signersSet;
		timeIssued = issued;
		closers = closersSet;
	}
	
	public PetitionData(String name, BookMeta petition, Player creator) {
		title = name;
		bookData = petition;
		signers = new HashSet<Player>();
		signers.add(creator);
		timeIssued = new Date();
	}
	
	public String getTitle() {
		return title;
	}
	
	public BookMeta getBookMeta() {
		return bookData;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof PetitionData)) return false;
		return this.title.equalsIgnoreCase(((PetitionData) o).title);
	}

	@Override
	public int compareTo(PetitionData o) {
		if(this.equals(o)) return 0;
		//Sort by number of signers then by oldness
		return this.signers.size() != o.signers.size() ? this.signers.size() - o.signers.size() :
			(int) (this.timeIssued.getTime() - o.timeIssued.getTime());
	}
	
	public String toChatString() {
		return String.format("§r§6%1$s§r\n§r - §b%2$d§r signatures, submitted §a%3$tD§r by §3%4$s§r", title, signers.size(), timeIssued, bookData.getAuthor());
	}
	
}

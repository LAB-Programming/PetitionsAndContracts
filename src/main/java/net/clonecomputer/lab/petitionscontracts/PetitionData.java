package net.clonecomputer.lab.petitionscontracts;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class PetitionData implements Comparable<PetitionData> {

	private BookMeta bookData;
	private Set<Player> signers;
	private Date timeIssued;
	private Set<Player> closers;
	
	/**
	 * Used when loading PetitionData from a file
	 */
	public static PetitionData load(BookMeta bookData, Set<Player> signers, Date timeIssued, Set<Player> closers) {
		return new PetitionData(bookData, signers, timeIssued, closers);
	}
	
	private PetitionData(BookMeta data, Set<Player> signersSet, Date issued, Set<Player> closersSet) {
		bookData = data;
		signers = signersSet;
		timeIssued = issued;
		closers = closersSet;
	}
	
	public PetitionData(BookMeta petition, Player creator) {
		bookData = petition;
		signers = new HashSet<Player>();
		signers.add(creator);
		timeIssued = new Date();
	}

	@Override
	public int compareTo(PetitionData o) {
		//Sort by number of signers then by oldness
		return this.signers.size() != o.signers.size() ? this.signers.size() - o.signers.size() :
			(int) (this.timeIssued.getTime() - o.timeIssued.getTime());
	}
	
	public String toColorizedString() {
		//Might want to tweak the colors a bit still
		return String.format("§9%1$s§r, sub. by §3%2$s§r, §a%3$tD§r. %4$d signature(s)", bookData.getTitle(), bookData.getAuthor(), timeIssued, signers.size());
	}
	
}
package net.clonecomputer.lab.petitionscontracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
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
	
	public void setBookItemMeta(ItemStack book, PetitionData petition) {
		if(book.getType() != Material.WRITTEN_BOOK) throw new IllegalArgumentException("book must be a written book");
		BookMeta meta = petition.getBookMeta();
		meta.setDisplayName("§r§6" + petition.getTitle());
		List<String> lore = new ArrayList<String>(3);
		lore.add("§r§b" + petition.getSigners().size() + " §7signatures");
		lore.add(String.format("§r§7Submitted on §a%tD", petition.getTimeIssued()));
		lore.add("§r§7By §3" + petition.getSubmitter().getName());
		meta.setLore(lore);
		book.setItemMeta(meta);
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

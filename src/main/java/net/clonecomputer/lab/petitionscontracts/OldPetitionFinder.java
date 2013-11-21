package net.clonecomputer.lab.petitionscontracts;

import static net.clonecomputer.lab.petitionscontracts.PetitionsAndContracts.TITLE_REGEX;
import static net.clonecomputer.lab.petitionscontracts.PetitionsAndContracts.plugin;

import java.util.regex.Matcher;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class OldPetitionFinder implements Listener {
	
	void updateOnlinePlayersInventories() {
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			updateInventory(p.getInventory());
		}
	}
	
	
	
	@EventHandler
	public void onPlayerJoined(PlayerJoinEvent e) {
		updateInventory(e.getPlayer().getInventory());
	}
	
	@EventHandler
	public void onPlayerOpenInventory(InventoryOpenEvent e) {
		updateInventory(e.getInventory());
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		updateItem(e.getItem().getItemStack());
	}
	
	
	
	private void updateInventory(Inventory inv) {
		updateInventory(inv, 0);
	}
	
	private void updateInventory(Inventory inv, int startSlot) {
		for(int i = startSlot; i < inv.getSize(); ++i) {
			updateItem(inv.getItem(i));
		}
	}
	
	private void updateItem(ItemStack item) {
		if(item == null) return; // nothing in that slot of their inventory
		if(item.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			Matcher matcher = TITLE_REGEX.matcher(meta.getTitle());
			if(matcher.matches()) {
				PetitionData petition = plugin.getPetitionStorage().getPetitionData(matcher.group(1));
				//Checks first if the petition exists and then if it does not have the right number of signatures
				if(petition != null && Integer.parseInt(matcher.group(2)) != petition.getSigners().size()) {
					plugin.setPetitionTitleAndLore(meta, petition);
					item.setItemMeta(meta);
				}
			}
		}
	}
	
}

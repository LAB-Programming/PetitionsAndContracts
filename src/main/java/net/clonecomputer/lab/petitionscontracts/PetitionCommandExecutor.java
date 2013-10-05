package net.clonecomputer.lab.petitionscontracts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import static net.clonecomputer.lab.petitionscontracts.PetitionsAndContracts.*;

public class PetitionCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			return false;
		}
		if(args[0].equalsIgnoreCase("create")) {
			doCreateCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("list")) {
			doListCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("get")) {
			doGetCommand(sender, cmd, label, args);
		} else {
			return false;
		}
		return true;
	}

	private void doCreateCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(args.length > 1) {
				ItemStack item = ((Player) sender).getItemInHand();
				if(item.getType() == Material.WRITTEN_BOOK) {
					String title = implode(" ", args, 1);
					PetitionData petition = plugin.getPetitionStorage().addPetition(title, (BookMeta) item.getItemMeta(), (Player) sender);
					if(petition != null) {
						plugin.setBookItemMeta(item, petition);
						sender.sendMessage("§aSuccessfully submitted petition!");
					} else {
						sender.sendMessage("§4A petition by that name already exists!");
					}
				} else {
					sender.sendMessage("§4You must be holding a written (signed) book to submit as a petition!");
				}
			} else {
				sender.sendMessage("§4Usage: /" + label + " create <title>");
			}
		} else {
			sender.sendMessage("§4Sorry, you must be a player to create a petition!");
		}
	}

	private void doListCommand(CommandSender sender, Command cmd, String label, String[] args) {
		List<PetitionData> petitionsList = PetitionsAndContracts.plugin.getPetitionStorage().toSortedList();
		sender.sendMessage("§e--------- §rList of Outstanding Petitions §e---------");
		for(PetitionData p : petitionsList) {
			sender.sendMessage(p.toChatString());
		}
		sender.sendMessage("§e--------- §r" + petitionsList.size() + " outstanding permissions §e-----------");
	}

	private void doGetCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(args.length > 1) {
				PetitionData bookData = plugin.getPetitionStorage().getPetitionData(implode(" ", args, 1));
				if(bookData != null) {
					PlayerInventory inv = ((Player) sender).getInventory();
					ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
					plugin.setBookItemMeta(book, bookData);
					inv.addItem(book);
					sender.sendMessage("§aYou have received petition " + bookData.getTitle());
				} else {
					sender.sendMessage("§4That petition does not exist");;
				}
			} else {
				sender.sendMessage("§4Usage: /" + label + " get <title>");
			}
		} else {
			sender.sendMessage("§4Sorry you must be a player to get a petition book");
		}
	}

}

package net.clonecomputer.lab.petitionscontracts;

import static net.clonecomputer.lab.petitionscontracts.PetitionsAndContracts.TITLE_REGEX;
import static net.clonecomputer.lab.petitionscontracts.PetitionsAndContracts.implode;
import static net.clonecomputer.lab.petitionscontracts.PetitionsAndContracts.plugin;

import java.util.List;
import java.util.regex.Matcher;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;

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
		} else if(args[0].equalsIgnoreCase("sign")) {
			doSignCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("unsign")) {
			doUnsignCommand(sender, cmd, label, args);
		} else if(args[0].equalsIgnoreCase("close")) {
			doCloseCommand(sender, cmd, label, args);
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
	
	private void doSignCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			ItemStack item = ((Player) sender).getItemInHand();
			BookMeta meta = null;
			Matcher titleMatcher = null;
			if(item.getType() == Material.WRITTEN_BOOK) {
				meta = (BookMeta) item.getItemMeta();
				titleMatcher = TITLE_REGEX.matcher(meta.getTitle());
			}
			if(meta != null && titleMatcher != null && titleMatcher.matches()) {
				String title = titleMatcher.group(1);
				PetitionData petition = plugin.getPetitionStorage().getPetitionData(title);
				if(petition != null) {
					if(petition.addSignature((Player) sender)) {
						plugin.updateOnlinePlayersInventories();
						sender.sendMessage("§aSuccessfully added your signature to the petition!");
					} else {
						sender.sendMessage("§3You have already signed that petition!");
					}
				} else {
					sender.sendMessage("§4Couldn't find petition (probably has been deleted)");
				}
			} else {
				sender.sendMessage("§4You must have the petition you want to sign in your hand!");
			}
		} else {
			sender.sendMessage("§4Sorry you must be a player to sign petitions");
		}
	}
	
	private void doUnsignCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(args.length <= 1) {
				ItemStack item = ((Player) sender).getItemInHand();
				BookMeta meta = null;
				Matcher titleMatcher = null;
				if(item.getType() == Material.WRITTEN_BOOK) {
					meta = (BookMeta) item.getItemMeta();
					titleMatcher = TITLE_REGEX.matcher(meta.getTitle());
				}
				if(meta != null && titleMatcher != null && titleMatcher.matches()) {
					String title = titleMatcher.group(1);
					PetitionData petition = plugin.getPetitionStorage().getPetitionData(title);
					if(petition != null) {
						if(petition.removeSignature((Player) sender)) {
							plugin.updateOnlinePlayersInventories();
							sender.sendMessage("§aSuccessfully unsigned the petition!");
						} else {
							sender.sendMessage("§3You have not signed that petition!");
						}
					} else {
						sender.sendMessage("§4Couldn't find petition (might have been deleted)");
					}
				} else {
					sender.sendMessage("§3You must either have the petition you want to unsign in your hand or specify its name!");
				}
			} else {
				PetitionData petition = plugin.getPetitionStorage().getPetitionData(implode(" ", args, 1));
				if(petition != null) {
					if(petition.removeSignature((Player) sender)) {
						plugin.updateOnlinePlayersInventories();
						sender.sendMessage("§aSuccessfully unsigned the petition!");
					} else {
						sender.sendMessage("§3You have not signed that petition!");
					}
				} else {
					sender.sendMessage("§4That petition does not exist");;
				}
			}
		} else {
			sender.sendMessage("§4Sorry you must be a player to unsign petitions");
		}
	}
	
	
	/**
	 * There are a lot of redundancies between this and doSignCommand.<br />
	 * They probably should either be merged or mostly refactored into another method
	 */
	private void doCloseCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			ItemStack item = ((Player) sender).getItemInHand();
			BookMeta meta = null;
			Matcher titleMatcher = null;
			if(item.getType() == Material.WRITTEN_BOOK) {
				meta = (BookMeta) item.getItemMeta();
				titleMatcher = TITLE_REGEX.matcher(meta.getTitle());
			}
			if(meta != null && titleMatcher != null && titleMatcher.matches()) {
				String title = titleMatcher.group(1);
				PetitionData petition = plugin.getPetitionStorage().getPetitionData(title);
				if(petition != null) {
					if(petition.addCloser((Player) sender)) {
						sender.sendMessage("§aSuccessfully added added your mark to close the petition!");
					} else {
						sender.sendMessage("§3You have already marked that petition as closed!");
					}
				} else {
					sender.sendMessage("§4Couldn't find petition (probably has been deleted)");
				}
			} else {
				sender.sendMessage("§4You must have the petition you want to mark as closed in your hand!");
			}
		} else {
			sender.sendMessage("§4Please log onto the server as a player to mark a petition as closed");
		}
	}

}

package net.clonecomputer.lab.petitionscontracts;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class PetitionCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			return false;
		}
		if(args[0].equalsIgnoreCase("create")) {
			doCreateCommand(sender, cmd, label, args);
		}
		return true;
	}

	private void doCreateCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			ItemStack item = ((Player) sender).getItemInHand();
			if(item.getType() == Material.WRITTEN_BOOK) {
				PetitionsAndContracts.plugin.getPetitionStorage().addPetition(new PetitionData((BookMeta) item.getItemMeta(), (Player) sender));
				sender.sendMessage("§aSuccessfully submitted petition!");
			} else {
				sender.sendMessage("§4You must be holding a written (signed) book to submit as a petition!");
			}
		} else {
			sender.sendMessage("§4Sorry, you must be a player to create a petition!");
		}
	}

}

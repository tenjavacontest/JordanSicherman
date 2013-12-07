/**
 * 
 */
package ten.java.JordanSicherman.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ten.java.JordanSicherman.Utilities.Utilities;

/**
 * @author Jordan
 * 
 */
public class BookCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			// Send a list of books if we don't provide one in the arguments.
			if (args.length == 0) {
				for (String book : Utilities.getListOfBooks().keySet())
					Utilities.sendMessage(sender, "&e" + book);
				return true;
			}

			// Find the name.
			String name = "";
			for (String arg : args)
				name += arg + " ";
			name = name.trim();

			// Make sure the book exists.
			if (!Utilities.getListOfBooks().containsKey(name)) {
				Utilities.sendMessage(sender, "&4That book doesn't exist!");
				return true;
			}

			// Construct the book.
			Utilities.constructBook((Player) sender, name);
		}
		return true;
	}
}

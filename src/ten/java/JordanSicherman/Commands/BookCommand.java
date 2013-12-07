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
			if (args.length == 0) {
				for (String book : Utilities.getListOfBooks().keySet())
					Utilities.sendMessage(sender, "&e" + book);
				return true;
			}
			String name = "";
			for (String arg : args)
				name += arg + " ";
			name = name.trim();
			Utilities.constructBook((Player) sender, name);
		}
		return true;
	}
}

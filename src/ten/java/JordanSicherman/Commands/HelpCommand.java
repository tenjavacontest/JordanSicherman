/**
 * 
 */
package ten.java.JordanSicherman.Commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ten.java.JordanSicherman.Main;
import ten.java.JordanSicherman.Utilities.Utilities;

/**
 * @author Jordan
 * 
 */
public class HelpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Store all applicable commands.
		Map<String, Map<String, Object>> commands = Main.instance.getDescription().getCommands();
		String permission;

		// A header.
		Utilities.sendMessage(sender, "-=-=-=-=- COMMANDS -=-=-=-=-");
		sender.sendMessage("");

		// Let the sender know if they are able to use each individual command.
		for (String cmd : commands.keySet()) {
			// Get the permission node.
			permission = (String) commands.get(cmd).get("permission");

			// Send the sender a message if they have the permission node
			// required to use the command.
			if (permission == null || sender.hasPermission(permission))
				Utilities.sendMessage(sender, "&e" + cmd + ": &r" + commands.get(cmd).get("description")
						+ (permission != null ? " &6(" + permission + ")" : ""));
		}

		// Make OPs aware of the permission nodes.
		if (sender.isOp()) {
			// A header.
			sender.sendMessage("");
			Utilities.sendMessage(sender, "-=-=-=-=- PERMISSIONS -=-=-=-=-");
			sender.sendMessage("");

			// Let the sender know each individual permission.
			for (String cmd : commands.keySet()) {
				// Get the permission node.
				permission = (String) commands.get(cmd).get("permission");

				// Send the sender the permission node.
				if (permission != null)
					Utilities.sendMessage(sender, "&e" + permission);
			}
		}

		return true;
	}
}

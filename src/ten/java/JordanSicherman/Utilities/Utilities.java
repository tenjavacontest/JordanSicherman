/**
 * 
 */
package ten.java.JordanSicherman.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import ten.java.JordanSicherman.Main;

/**
 * @author Jordan
 * 
 */
public class Utilities {

	/**
	 * Send a message to a receiver. Replaces colors formatted with the
	 * ampersand.
	 * 
	 * @param to
	 *            The receiver.
	 * @param message
	 *            The message with ampersand color codes.
	 */
	public static void sendMessage(CommandSender to, String message) {
		// Replace the colors.
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (to instanceof ConsoleCommandSender)
			// Make sure the console gets a prefix.
			message = "[TenJava] " + message;
		to.sendMessage(message);
	}

	/**
	 * Schedule a task. Simply an ease-of-access method.
	 * 
	 * @param async
	 *            Whether or not to run asynchronously.
	 * @param repeating
	 *            Whether or not to schedule repeating.
	 * @param runnable
	 *            The runnable object.
	 * @param secondsDelay
	 *            The seconds of delay before running or between runs.
	 */
	public static void schedule(boolean async, boolean repeating, Runnable runnable, int secondsDelay) {
		if (async) {
			if (repeating)
				Main.instance.getServer().getScheduler().runTaskTimerAsynchronously(Main.instance, runnable, 0, secondsDelay * 20L);
			else
				Main.instance.getServer().getScheduler().runTaskLaterAsynchronously(Main.instance, runnable, secondsDelay * 20L);
		} else if (repeating)
			Main.instance.getServer().getScheduler().runTaskTimer(Main.instance, runnable, 0, secondsDelay * 20L);
		else
			Main.instance.getServer().getScheduler().runTaskLater(Main.instance, runnable, secondsDelay * 20L);
	}
}

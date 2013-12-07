/**
 * 
 */
package ten.java.JordanSicherman;

import org.bukkit.plugin.java.JavaPlugin;

import ten.java.JordanSicherman.Commands.BookCommand;
import ten.java.JordanSicherman.Commands.HelpCommand;
import ten.java.JordanSicherman.Utilities.Utilities;

/**
 * @author Jordan
 * 
 */
public class Main extends JavaPlugin {

	public static Main instance; // The instance of this class for other classes
									// to refer to.

	/**
	 * The onEnable.
	 */
	@Override
	public void onEnable() {
		// Store the instance.
		instance = this;
		// Make sure our config exists.
		saveDefaultConfig();

		// Enable all the commands.
		doCommands();

		// Cache our book list.
		Utilities.schedule(true, false, new Runnable() {
			@Override
			public void run() {
				Utilities.getListOfBooks();
			}
		}, 0);
	}

	/**
	 * The onDisable.
	 */
	@Override
	public void onDisable() {
		// Make sure all tasks are cancelled.
		getServer().getScheduler().cancelTasks(this);
	}

	/**
	 * Register all commands.
	 */
	private void doCommands() {
		getCommand("shakespeare").setExecutor(new HelpCommand());
		getCommand("read").setExecutor(new BookCommand());
	}
}

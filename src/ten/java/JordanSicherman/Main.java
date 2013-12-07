/**
 * 
 */
package ten.java.JordanSicherman;

import org.bukkit.plugin.java.JavaPlugin;

import ten.java.JordanSicherman.Commands.HelpCommand;

/**
 * @author Jordan
 * 
 */
public class Main extends JavaPlugin {

	public static Main instance; // The instance of this class for other classes to refer to.

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
		getCommand("tenjava").setExecutor(new HelpCommand());
	}
}

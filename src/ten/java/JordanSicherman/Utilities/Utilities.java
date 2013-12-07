/**
 * 
 */
package ten.java.JordanSicherman.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import ten.java.JordanSicherman.Main;

/**
 * @author Jordan
 * 
 */
public class Utilities {

	private static Map<String, String> bookshelf;
	private static final String website = "http://shakespeare.classicauthors.net";

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
	 * Parse a webpage and get a list of books with the URL they can be read at.
	 * 
	 * @return The list of readable books in name:url form.
	 */
	public static Map<String, String> getListOfBooks() {
		// Make sure we haven't already done this.
		if (bookshelf != null)
			return bookshelf;

		bookshelf = new HashMap<String, String>();

		try {
			// Connect to the webpage.
			URL url = new URL(website + "/index.html");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String inputLine;
			String raw = null;
			// Go through the ranks and see if we can find the line that has all
			// the book titles and URLs.
			while ((inputLine = in.readLine()) != null)
				if (inputLine.trim().startsWith("<p><a href=\"" + website)) {
					// We found it, let's continue.
					raw = inputLine;
					break;
				}

			if (raw != null) {
				List<String> names = new ArrayList<String>();
				List<String> urls = new ArrayList<String>();

				// Find all the titles (encapsuled between HTML b tags)
				Pattern pattern = Pattern.compile("<b>(.*?)</b>");
				Matcher matcher = pattern.matcher(raw);
				while (matcher.find())
					names.add(matcher.group(1));

				// Find all the URLs (encapsulated between quotations)
				pattern = Pattern.compile("\"(.*?)\"");
				matcher = pattern.matcher(raw);
				while (matcher.find())
					urls.add(matcher.group(1));

				// Match the title to the URL and add them to the map.
				for (int i = 0; i < names.size(); i++) {
					String page = urls.get(i);
					// Make sure we navigate to the first page.
					page = page.replace(website + "/", "");
					String slug = page.substring(0, page.length() - 1);
					String name = names.get(i);
					name = name.replaceAll("`", "'");
					System.out.println("Library contains: " + name);
					bookshelf.put(name.toLowerCase().trim(), website + "/" + page + slug + "1");
				}
			}
			in.close();
		} catch (Exception e) {

		}
		return bookshelf;
	}

	/**
	 * The entrance point to the book creation.
	 * 
	 * @param player
	 *            The player to provide the book with.
	 * @param bookName
	 *            The book name.
	 */
	public static void constructBook(Player player, String bookName) {
		sendMessage(player, "&eCreating book, please wait...");

		final ItemStack defaultBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) defaultBook.getItemMeta();
		meta.setTitle(bookName);
		meta.setAuthor("Shakespeare");
		defaultBook.setItemMeta(meta);

		Random random = new Random();

		for (int i = 1; i < 30; i++)
			schedule(true, false, new BookRunnable(player, bookName, i, defaultBook), random.nextInt(5) + 2);
	}

	/**
	 * A simple runnable class to construct a book and give it to a player.
	 * 
	 * @author Jordan
	 * 
	 */
	private static class BookRunnable implements Runnable {
		Player player;
		String bookName;
		int bookPage;
		ItemStack defaultBook;

		public BookRunnable(Player player, String bookName, int bookPage, ItemStack defaultBook) {
			this.player = player;
			this.bookName = bookName;
			this.bookPage = bookPage;
			this.defaultBook = defaultBook;
		}

		@Override
		public void run() {
			ItemStack book;
			try {
				// Construct the book, or fail to and return.
				book = constructBook(bookName, defaultBook, bookPage);
			} catch (Exception e) {
				return;
			}
			// Alert the player.
			player.getInventory().setItem(bookPage - 1, book);
		}
	}

	/**
	 * A somewhat recursive method of constructing a book.
	 * 
	 * @param bookName
	 *            The name of the book to construct.
	 * @param book
	 *            The existing book.
	 * @param number
	 *            The webpage number.
	 * @return The book with the current page appended.
	 * @throws Exception
	 *             If we reach the end of the book.
	 */
	public static ItemStack constructBook(String bookName, ItemStack defaultBook, int number) throws Exception {
		String bookURL = bookshelf.get(bookName.toLowerCase());
		bookURL = bookURL.replace("1", number + "");

		// Connect to the webpage.
		URL url = new URL(bookURL);

		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		String inputLine;
		String title = null;
		List<String> content = new ArrayList<String>();
		while ((inputLine = in.readLine()) != null) {
			// We've reached the end of this book.
			if (inputLine.contains("verify-v1"))
				throw new Exception();
			if (title == null) {
				// Find all the titles (encapsuled between HTML h3 tags)
				Pattern pattern = Pattern.compile("<h3>(.*?)</h3>");
				Matcher matcher = pattern.matcher(inputLine);
				if (matcher.find())
					title = matcher.group(1);
			} else // Find all the content on the page.
			if (!inputLine.trim().startsWith("<"))
				content.add(inputLine.trim());
			// Reach the end of the written part.
			if (inputLine.trim().startsWith("</p"))
				break;
		}

		in.close();

		ItemStack book = defaultBook.clone();

		BookMeta meta = (BookMeta) book.getItemMeta();
		// Make sure the title has our number.
		meta.setTitle(meta.getTitle() + " - " + number);
		String page = "";
		// Fill the page.
		for (String line : content) {
			page += line + " ";
			// Make sure we don't overflow.
			if (page.length() >= 18 * 10) {
				meta.addPage(page.trim());
				page = "";
			}
		}
		// If we hadn't previously added the page, add it now.
		if (!page.isEmpty())
			meta.addPage(page.trim());

		// Save the meta because we're done.
		book.setItemMeta(meta);
		return book;
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

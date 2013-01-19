package com.legit2.hqm.Infractions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.legit2.hqm.Virtues.Virtues;

public class Util {

	private static Infractions plugin; // obviously needed
	static Logger log = Logger.getLogger("Minecraft");
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static Calendar cal = Calendar.getInstance();
	static String date = dateFormat.format(cal.getTime());
	
	public static void consoleMSG(String level, String msg)
	{
		// Define variables
		Logger log = Logger.getLogger("Minecraft");
		
		if(level.equalsIgnoreCase("info")) log.info("[Demigods] " + msg);
		if(level.equalsIgnoreCase("warning")) log.warning("[Demigods] " + msg);
		if(level.equalsIgnoreCase("severe")) log.severe("[Demigods] " + msg);
	}

	public static int getMaxScore(Player p) {
		int maxScore = Settings.getSettingInt("ban_at_score");
		if (maxScore < 1)
			maxScore = 1;
		else if (maxScore > 20)
			maxScore = 20;
		if (p.hasPermission("infractions.*")) { // '*' Permission check.
			maxScore = Settings.getSettingInt("ban_at_score");
		} else if (p.hasPermission("infractions.maxscore.1")) {
			maxScore = 1;
		} else if (p.hasPermission("infractions.maxscore.2")) {
			maxScore = 2;
		} else if (p.hasPermission("infractions.maxscore.3")) {
			maxScore = 3;
		} else if (p.hasPermission("infractions.maxscore.4")) {
			maxScore = 4;
		} else if (p.hasPermission("infractions.maxscore.5")) {
			maxScore = 5;
		} else if (p.hasPermission("infractions.maxscore.6")) {
			maxScore = 6;
		} else if (p.hasPermission("infractions.maxscore.7")) {
			maxScore = 7;
		} else if (p.hasPermission("infractions.maxscore.8")) {
			maxScore = 8;
		} else if (p.hasPermission("infractions.maxscore.9")) {
			maxScore = 9;
		} else if (p.hasPermission("infractions.maxscore.10")) {
			maxScore = 10;
		} else if (p.hasPermission("infractions.maxscore.11")) {
			maxScore = 11;
		} else if (p.hasPermission("infractions.maxscore.12")) {
			maxScore = 12;
		} else if (p.hasPermission("infractions.maxscore.13")) {
			maxScore = 13;
		} else if (p.hasPermission("infractions.maxscore.14")) {
			maxScore = 14;
		} else if (p.hasPermission("infractions.maxscore.15")) {
			maxScore = 15;
		} else if (p.hasPermission("infractions.maxscore.16")) {
			maxScore = 16;
		} else if (p.hasPermission("infractions.maxscore.17")) {
			maxScore = 17;
		} else if (p.hasPermission("infractions.maxscore.18")) {
			maxScore = 18;
		} else if (p.hasPermission("infractions.maxscore.19")) {
			maxScore = 19;
		} else if (p.hasPermission("infractions.maxscore.20")) {
			maxScore = 20;
		}
		return maxScore;
	}

	public static ChatColor chatColor(Player p, ChatColor color) {
		if (p == null)
			return ChatColor.RESET;
		return (color);
	}

	@SuppressWarnings("unchecked")
	public static boolean addInfraction(String target, int level, int id,
			String infraction, String proof) {
		if (!Save.hasData(target, "INFRACTIONS")) {
			setInfractions(target, new HashMap<String, String>());
		}
		String readableID = Integer.toString(id);
		((HashMap<String, String>) Save.getData(target, "INFRACTIONS")).put(
				readableID, level + ":" + infraction + " - " + proof + " - "
						+ date);
		return true;
	}

	public static void checkScore(Player p) {
		if (getMaxScore(p) <= getScore(p) && (!p.hasPermission("infractions.banexempt"))) {
			p.kickPlayer("You have been banned.");
			try {
				p.setBanned(true);
			} catch (NullPointerException e) {
				log.info("[Infractions] Unable to set " + p.toString()
						+ " to banned.");
			}
		} else {
			try {
				p.setBanned(false);
			} catch (NullPointerException e) {
				log.info("[Infractions] Unable to set " + p.toString()
						+ " to unbanned.");
			}
		}
	}

	public static void kickNotify(String p, String reason) {
		if (!Virtues.getVirtues().contains(reason)) {
			try {
				getOnlinePlayer(p); // Check if player is online.
				if (getMaxScore(Util.getOnlinePlayer(p)) <= getScore(Util
						.getOnlinePlayer(p))) { // Players that should be banned are
												// banned.
					checkScore(Util.getOnlinePlayer(p));
				} else if (Settings.getSettingBoolean("kick_on_cite")) {
					Util.getOnlinePlayer(p).kickPlayer(
							"You've been cited for " + reason + "."); // Kick a
																		// player if
																		// option is
																		// set to
																		// true.
				} else {
					Util.getOnlinePlayer(p)
							.sendMessage(
									ChatColor.RED + "You've been cited for "
											+ reason + ".");
					Util.getOnlinePlayer(p).sendMessage(
							"Use " + ChatColor.YELLOW + "/history"
									+ ChatColor.WHITE + " for more information."); // Send
																					// player
																					// a
																					// message
																					// about
																					// their
																					// infraction.
				}
			} catch (NullPointerException e) {
				Save.saveData(p, "NEWINFRACTION", true);
			}
		} else {
			Virtues.rewardPlayer(p, reason);
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getInfractions(String target) {
		if (Save.hasData(target, "INFRACTIONS")) {
			HashMap<String, String> original = ((HashMap<String, String>) Save
					.getData(target, "INFRACTIONS"));
			HashMap<String, String> toreturn = new HashMap<String, String>();
			for (String s : original.keySet()) {
				toreturn.put(s, original.get(s));
			}
			setInfractions(target, toreturn); // clean original
			return toreturn;
		}
		return null;
	}

	public static ArrayList<String> getInfractionsList(String target) {
		if (Save.hasData(target, "INFRACTIONS")) {
			HashMap<String, String> original = getInfractions(target);
			ArrayList<String> toreturn = new ArrayList<String>();
			for (String s : original.keySet()) {
				toreturn.add(s);
			}
			return toreturn;
		}
		return null;
	}

	public static String getInfractionsPlayer(String name) {
		String found = null;
		String lowerName = name.toLowerCase();
		int delta = Integer.MAX_VALUE;
		for (String playername : Save.getCompleteData().keySet()) {
			if (playername.toLowerCase().startsWith(lowerName)) {
				int curDelta = playername.length() - lowerName.length();
				if (curDelta < delta) {
					found = playername;
					delta = curDelta;
				}
				if (curDelta == 0)
					break;
			}
		}
		return found;
	}

	public static Player getOnlinePlayer(String name) {
		return plugin.getServer().getPlayer(name);
	}

	public static OfflinePlayer getOfflinePlayer(String name) {
		OfflinePlayer target = plugin.getServer().getOfflinePlayer(name);
		return target;
	}

	public static Infractions getPlugin() {
		return plugin;
	}

	public static int getScore(Player p) {
		return getScore(p.getName());
	}

	public static int getScore(String p) {
		if (Save.hasData(p, "SCORE"))
			return (Integer) Save.getData(p, "SCORE");
		return 0;
	}

	public static boolean hasPermission(Player p, String pe) { // convenience
																// method for
																// permissions
		return p.hasPermission(pe);
	}

	public static boolean hasPermissionOrOP(Player p, String pe) { // convenience
																	// method
																	// for
																	// permissions
		if (p == null)
			return true;
		if (p.isOp())
			return true;
		return p.hasPermission(pe);
	}

	public static boolean isValidURL(String input) {
		if (!input.startsWith("http://") && !input.startsWith("https://")) {
			input = ("http://" + input);
		}
		try {
			URI uri = new URI(input);
			URL url = uri.toURL();
			java.net.URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (URISyntaxException e) {
			return false;
		}
	}

	public static void messageSend(Player p, String str) {
		if (p == null) {
			str = "[Infractions] " + str;
			log.info(str);
		} else {
			p.sendMessage(str);
		}
	}

	public static void reloadPlugin() {
		Bukkit.getServer().getPluginManager().disablePlugin(plugin);
		Bukkit.getServer().getPluginManager().enablePlugin(plugin);
	}

	public static boolean removeInfraction(String target, String givenID) {
		for (String readableID : Util.getInfractionsList(target)) {
			if (readableID.equals(givenID))
				return (Util.getInfractions(target).remove(readableID) == null);
		}
		return false;
	}

	public static void setInfractions(String p, HashMap<String, String> data) {
		Save.saveData(p, "INFRACTIONS", data);
	}

	public static void setScore(Player p, int amt) {
		setScore(p.getName(), amt);
	}

	public static void setScore(String p, int amt) {
		Save.saveData(p, "SCORE", new Integer(amt));
	}

	public Util(Infractions i) {
		plugin = i;
	}
}

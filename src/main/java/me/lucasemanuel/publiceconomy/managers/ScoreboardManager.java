/**
 *  Name:    ScoreboardManager.java
 *  Created: 20:18:37 - 16 maj 2013
 * 
 *  Author:  Lucas Arnström - LucasEmanuel @ Bukkit forums
 *  Contact: lucasarnstrom(at)gmail(dot)com
 *  
 *
 *  Copyright 2013 Lucas Arnström
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *  
 *
 *
 *  Filedescription:
 *
 * 
 */

package me.lucasemanuel.publiceconomy.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.lucasemanuel.publiceconomy.Main;
import me.lucasemanuel.publiceconomy.utils.ConsoleLogger;

public class ScoreboardManager {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	private HashMap<String, Scoreboard> playerboards;
	
	public ScoreboardManager(Main instance) {
		plugin = instance;
		logger = new ConsoleLogger(instance, "ScoreboardManager");
		
		playerboards = new HashMap<String, Scoreboard>();
		
		// Schedule it to load all players directly on the next tick to allow the server to get ready.
		// This is here in case the plugin was reloaded.
		plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					addPlayer(player);
				}
			}
		});
		
		logger.debug("Initiated");
	}

	public void addPlayer(final Player player) {
		final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Objective o = board.registerNewObjective("stats", "dummy");
		
		o.setDisplayName(ChatColor.GOLD + "PENGAR");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Score s = o.getScore(Bukkit.getOfflinePlayer("Saldo:"));
		s.setScore((int) plugin.getMoneyManager().getBalance(player.getName()));
		
		playerboards.put(player.getName(), board);
		
		plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
			public void run() {
				player.setScoreboard(board);
			}
		});
	}
	
	public void removePlayer(Player player) {
		if(playerboards.containsKey(player.getName())) {
			playerboards.remove(player.getName());
			
			// Just in case this method was called by toggleHideBoard()
			player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
		}
	}

	public void updateBalance(String playername) {
		if(playerboards.containsKey(playername)) {
			Scoreboard board = playerboards.get(playername);
			Objective o = board.getObjective(DisplaySlot.SIDEBAR);
			Score s = o.getScore(Bukkit.getOfflinePlayer("Saldo:"));
			
			s.setScore((int) plugin.getMoneyManager().getBalance(playername));
		}
	}

	public void toggleHideBoard(Player player) {
		if(playerboards.containsKey(player.getName())) {
			removePlayer(player);
		}
		else {
			addPlayer(player);
		}
	}
}

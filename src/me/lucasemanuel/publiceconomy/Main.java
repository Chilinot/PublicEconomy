/**
 *  Name:    Main.java
 *  Created: 22:42:52 - 15 maj 2013
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

package me.lucasemanuel.publiceconomy;

import me.lucasemanuel.publiceconomy.listeners.Players;
import me.lucasemanuel.publiceconomy.managers.ChestManager;
import me.lucasemanuel.publiceconomy.managers.MoneyManager;
import me.lucasemanuel.publiceconomy.managers.ScoreboardManager;
import me.lucasemanuel.publiceconomy.threading.ConcurrentSQLiteConnection;
import me.lucasemanuel.publiceconomy.utils.ConsoleLogger;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private ConsoleLogger logger;
	
	private ConcurrentSQLiteConnection data;
	private ChestManager chestmanager;
	private MoneyManager moneymanager;
	private ScoreboardManager sm;
	
	public void onEnable() {
		
		Config.load(this);
		
		logger = new ConsoleLogger(this, "Main");
		logger.debug("Initiating startup sequence!");
		
		
		logger.debug("Initiating managers...");
		
		data         = new ConcurrentSQLiteConnection(this);
		chestmanager = new ChestManager(this);
		moneymanager = new MoneyManager(this);
		sm           = new ScoreboardManager(this);
		
		
		logger.debug("Registering eventlisteners...");
		
		getServer().getPluginManager().registerEvents(new Players(this), this);
		
		
		logger.debug("Registering commands...");
		
		Commands commands = new Commands(this);
		
		getCommand("test").setExecutor(commands);
		getCommand("saldo").setExecutor(commands);
		
	}

	public ConcurrentSQLiteConnection getDataStorage() {
		return data;
	}

	public ChestManager getChestManager() {
		return chestmanager;
	}

	public MoneyManager getMoneyManager() {
		return moneymanager;
	}

	public ScoreboardManager getScoreboardManager() {
		return sm;
	}
}

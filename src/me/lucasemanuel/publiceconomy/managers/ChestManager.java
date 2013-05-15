/**
 *  Name:    ChestManager.java
 *  Created: 23:35:19 - 15 maj 2013
 * 
 *  Author:  Lucas Arnstr�m - LucasEmanuel @ Bukkit forums
 *  Contact: lucasarnstrom(at)gmail(dot)com
 *  
 *
 *  Copyright 2013 Lucas Arnstr�m
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
import java.util.HashSet;

import org.bukkit.Location;

import me.lucasemanuel.publiceconomy.Main;
import me.lucasemanuel.publiceconomy.utils.ConsoleLogger;
import me.lucasemanuel.publiceconomy.utils.SerializedLocation;

public class ChestManager {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	private HashMap<Location, Boolean> shopchests;
	
	public ChestManager(Main instance) {
		plugin = instance;
		logger = new ConsoleLogger(instance, "ChestManager");
		
		shopchests = new HashMap<Location, Boolean>();
		
		loadSavedChestlocations();
		
		logger.debug("Initiated");
	}

	private void loadSavedChestlocations() {
		
		HashSet<String> locations = plugin.getDataStorage().loadChests();
		
		if(locations != null) {
			for(String string : locations) {
				shopchests.put(SerializedLocation.deserializeString(string), false);
			}
		}
		else {
			logger.severe("Something went wrong while retrieving saved shopchests from sqlite-database!");
		}
	}

	public boolean isShopChest(Location l) {
		return shopchests.containsKey(l);
	}

	public boolean isBlocked(Location location) {
		return shopchests.get(location);
	}

	public void block(Location location) {
		shopchests.put(location, true);
	}

	public void unblock(Location location) {
		shopchests.put(location, false);
	}

	public void registerChest(Location location) {
		if(!shopchests.containsKey(location)) {
			
			shopchests.put(location, false);
			
			final SerializedLocation l = new SerializedLocation(location);
			
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				public void run() {
					plugin.getDataStorage().registerChest(l);
				}
			});
		}
	}
}

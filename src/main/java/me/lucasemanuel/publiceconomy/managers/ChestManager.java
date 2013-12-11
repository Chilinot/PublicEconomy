/**
 *  Name:    ChestManager.java
 *  Created: 23:35:19 - 15 maj 2013
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
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;

import me.lucasemanuel.publiceconomy.Main;
import se.lucasarnstrom.lucasutils.ConsoleLogger;
import se.lucasarnstrom.lucasutils.SerializedLocation;;

public class ChestManager {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	private HashMap<Location, Boolean> shopchests;
	
	public ChestManager(Main instance) {
		plugin = instance;
		logger = new ConsoleLogger("ChestManager");
		
		shopchests = new HashMap<Location, Boolean>();
		
		loadSavedChestlocations();
		
		logger.debug("Initiated");
	}

	private void loadSavedChestlocations() {
		
		HashSet<String> locations = plugin.getDataStorage().loadChests();
		
		if(locations != null) {
			for(String string : locations) {
				Location l = SerializedLocation.deserializeString(string);
				if(l.getBlock().getType().equals(Material.CHEST))
					shopchests.put(l, false);
				else
					logger.warning("Loaded a registered chest from storage that is no longer a chest! "
							+ "WORLD:\"" + l.getWorld().getName() + "\", X:" + l.getBlockX() + ", Y:" + l.getBlockY() + ", Z:" + l.getBlockZ());
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

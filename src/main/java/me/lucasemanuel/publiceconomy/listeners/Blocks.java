/**
 *  Name:    BLocks.java
 *  Created: 17:49:23 - 30 maj 2013
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

package me.lucasemanuel.publiceconomy.listeners;

import me.lucasemanuel.publiceconomy.Main;
import me.lucasemanuel.publiceconomy.utils.ConsoleLogger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class Blocks implements Listener {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	public Blocks(Main instance) {
		plugin = instance;
		logger = new ConsoleLogger(instance, "BlockListener");
		
		logger.debug("Initiated");
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		if(b.getType().equals(Material.CHEST) 
				&& plugin.getChestManager().isShopChest(b.getLocation())
				&& !event.getPlayer().hasPermission("publiceconomy.chest.break")) {
			
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Du får inte ha sönder denna kista!");
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onItemSpawn(ItemSpawnEvent event) {
		plugin.getMoneyManager().fixLore(event.getEntity().getItemStack());
	}
}

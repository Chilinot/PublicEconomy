/**
 *  Name:    Players.java
 *  Created: 22:45:43 - 15 maj 2013
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
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class Players implements Listener {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	public Players(Main instance) {
		plugin = instance;
		logger = new ConsoleLogger(instance, "PlayerListener");
		
		logger.debug("Initiated");
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerLogin(PlayerLoginEvent event) {
		plugin.getScoreboardManager().addPlayer(event.getPlayer());
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.getScoreboardManager().removePlayer(event.getPlayer());
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(player.isOp() && player.getItemInHand().getType().equals(Material.BLAZE_ROD)) {
			event.getPlayer().sendMessage(ChatColor.GREEN + "Kistan registreras!");
			plugin.getChestManager().registerChest(event.getClickedBlock().getLocation());
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		Inventory inv = event.getInventory();
		
		if(inv.getHolder() instanceof Chest) {
			Chest chest = (Chest) inv.getHolder();
			
			if(plugin.getChestManager().isShopChest(chest.getLocation())) {
				if(!plugin.getChestManager().isBlocked(chest.getLocation())) {
					plugin.getChestManager().block(chest.getLocation());
				}
				else {
					((Player) event.getPlayer()).sendMessage(ChatColor.RED + "Kistan används redan!");
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		
		if(inv.getHolder() instanceof Chest) {
			Chest chest = (Chest) inv.getHolder();
			
			if(plugin.getChestManager().isShopChest(chest.getLocation())) {
				plugin.getMoneyManager().giveMoneyForItems(((Player)event.getPlayer()).getName(), chest.getInventory().getContents());
				
				chest.getInventory().clear();
				
				plugin.getChestManager().unblock(chest.getLocation());
			}
		}
	}
}

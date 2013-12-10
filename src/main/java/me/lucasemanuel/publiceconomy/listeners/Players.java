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

import java.util.Set;

import me.lucasemanuel.publiceconomy.Main;

import se.lucasarnstrom.lucasutils.ConsoleLogger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Players implements Listener {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	public Players(Main instance) {
		plugin = instance;
		logger = new ConsoleLogger("PlayerListener");
		
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
		
		if(event.getClickedBlock().getType().equals(Material.CHEST)
				&& player.hasPermission("publiceconomy.chest.register") 
				&& player.getItemInHand().getType().equals(Material.BLAZE_ROD)
				&& !plugin.getChestManager().isShopChest(event.getClickedBlock().getLocation())) {
			
			event.getPlayer().sendMessage(ChatColor.GREEN + "Kistan registreras!");
			plugin.getChestManager().registerChest(event.getClickedBlock().getLocation());
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		
		InventoryHolder holder = event.getInventory().getHolder();
		if(holder instanceof Chest) {
			Chest chest = (Chest) holder;
			
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
	
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled=true)
	public void onInventoryClose(InventoryCloseEvent event) {
		
		InventoryHolder holder = event.getInventory().getHolder();
		
		if(holder instanceof Chest) {
			
			Chest chest = (Chest) holder;
			
			Location  loc = chest.getLocation();
			Inventory inv = chest.getInventory();
			
			if(plugin.getChestManager().isShopChest(loc)) {
				
				Set<ItemStack> worthless = plugin.getMoneyManager().giveMoneyForItems(((Player)event.getPlayer()).getName(), inv.getContents());
				inv.clear();
				
				if(worthless.size() > 0) {
					
					Player player = (Player) event.getPlayer();
					
					for(ItemStack i : worthless) {
						player.getInventory().addItem(i);
					}
					
					player.updateInventory();
					player.sendMessage("Du fick tillbaka de saker som saknade värde.");
				}
				
				plugin.getChestManager().unblock(loc);
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack is1 = event.getCurrentItem();
		ItemStack is2 = event.getCursor();
		
		if(is1 != null && is1.getType() != Material.AIR)
			plugin.getMoneyManager().fixLore(is1);
		if(is2 != null && is2.getType() != Material.AIR)
			plugin.getMoneyManager().fixLore(is2);
	}
}

/**
 *  Name:    Commands.java
 *  Created: 22:49:18 - 15 maj 2013
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import me.lucasemanuel.publiceconomy.utils.ConsoleLogger;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	public Commands(Main instance) {
		plugin = instance;
		logger = new ConsoleLogger(instance, "CommandExecutor");
		
		logger.debug("Initiated");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String command = cmd.getName();
		
		switch(command) {
			
			case "test": return test(sender, args);
			
			case "saldo": return saldo(sender, args);
			
		}
		
		//TODO lägg till saldo-kommando
		
		return false;
	}
	
	private boolean saldo(CommandSender sender, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("This command can only be used by players!");
			return true;
		}
		
		Player player = (Player) sender;
		
		plugin.getScoreboardManager().toggleHideBoard(player);
		
		return true;
	}

	private boolean test(CommandSender sender, String[] args) {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader("E:/priser.txt");
			
			BufferedReader inFil = new BufferedReader(fr);                   

			String rad = inFil.readLine();
			while(rad != null) {
				lines.add(rad);
				rad = inFil.readLine();
			}              
			
			// Stäng filen 
			inFil.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "item_values.yml"));
		
		
		for(String line : lines) {
			
			String[] parts = line.split(" ");
			
			int id = Integer.parseInt(parts[0].split(":")[0]);
			
			String[] prices = parts[parts.length - 1].split(":");
			
			int price = 0;
			
			if(prices.length == 1 && !prices[0].equals("N/A"))
				price = Integer.parseInt(prices[0]);
			else if(prices.length == 1 && prices[0].equals("N/A"))
				price = 0;
			else if(prices.length == 2)
				price = Integer.parseInt(prices[1].replace("#", ""));
			
			
			String name = Material.getMaterial(id).name();
			
			double p = (double) price;
			
			config.set("items." + name, p);
		}
		
		try {
			config.save(plugin.getDataFolder() + File.separator + "item_values.yml");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}

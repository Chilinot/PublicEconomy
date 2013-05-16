/**
 *  Name: Config.java
 *  Date: 14:27:53 - 8 sep 2012
 * 
 *  Author: LucasEmanuel @ bukkit forums
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
 *  Filedescription:
 *  
 *  
 *  
 * 
 * 
 */

package me.lucasemanuel.publiceconomy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

	public static void load(JavaPlugin plugin) {
		
		FileConfiguration config = plugin.getConfig();
		boolean save = false;
		
		// General
		
		if(!config.contains("debug")) {
			config.set("debug", false);
			save = true;
		}
		
		// MySQL
		
		if(!config.contains("database.auth.username")) {
			config.set("database.auth.username", "username");
			save = true;
		}
		
		if(!config.contains("database.auth.password")) {
			config.set("database.auth.password", "password");
			save = true;
		}
		
		if(!config.contains("database.settings.host")) {
			config.set("database.settings.host", "localhost");
			save = true;
		}
		
		if(!config.contains("database.settings.port")) {
			config.set("database.settings.port", 3306);
			save = true;
		}
				
		if(!config.contains("database.settings.database")) {
			config.set("database.settings.database", "publiceconomy");
			save = true;
		}
		
		if(!config.contains("database.settings.tablename")) {
			config.set("database.settings.tablename", "player_accounts");
			save = true;
		}
		
		
		
		if(save) {
			plugin.saveConfig();
		}
	}
}

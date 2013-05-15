/**
 *  Name:    MoneyManager.java
 *  Created: 00:04:12 - 16 maj 2013
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

import me.lucasemanuel.publiceconomy.Main;
import me.lucasemanuel.publiceconomy.utils.ConsoleLogger;

public class MoneyManager {
	
	private ConsoleLogger logger;
	
	public MoneyManager(Main instance) {
		logger = new ConsoleLogger(instance, "MoneyManager");
		
		logger.debug("Initiated");
	}
}

/**
 *  Name: ConcurrentMySQLConnection.java
 *  Created: 19:48:08 - 16 maj 2013
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

package me.lucasemanuel.publiceconomy.threading;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConcurrentMySQLConnection {
	
	private final String username;
	private final String password;
	private final String host;
	private final int    port;
	private final String database;
	private final String tablename;

	public ConcurrentMySQLConnection(String username, String password, String host, int port, String database, String tablename) {
		this.username   = username;
		this.password   = password;
		this.host       = host;
		this.port       = port;
		this.database   = database;
		this.tablename  = tablename;
	}
	
	public synchronized void updateBalance(String playername, double balance) {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
			
			Connection con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			
			stmt.executeUpdate("INSERT INTO " + tablename + " (playername, balance) VALUES('" + playername + "', " + balance + ") " +
					"ON DUPLICATE KEY UPDATE balance=" + balance);
			
			stmt.close();
			con.close();
		}
		catch(SQLException | ClassNotFoundException e) {
			System.out.println("Error while updating playerbalance! Message: " + e.getMessage());
		}
	}
	
	public synchronized void testConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
		Connection con = DriverManager.getConnection(url, username, password);
		con.close();
	}
}

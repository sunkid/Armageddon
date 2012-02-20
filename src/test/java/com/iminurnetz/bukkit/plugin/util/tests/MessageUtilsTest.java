/**
 * LICENSING
 * 
 * This software is copyright by sunkid <sunkid@iminurnetz.com> and is
 * distributed under a dual license:
 * 
 * Non-Commercial Use:
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Commercial Use:
 *    Please contact sunkid@iminurnetz.com
 */
package com.iminurnetz.bukkit.plugin.util.tests;

import junit.framework.TestCase;

import org.bukkit.ChatColor;

import com.iminurnetz.bukkit.plugin.util.MessageUtils;


public class MessageUtilsTest  extends TestCase {
	public void testConvertColor() {
		String original = getOldCode(ChatColor.AQUA) + "Hello World" + getOldCode(ChatColor.BLACK);
		String expected = ChatColor.AQUA.toString() + "Hello World" + ChatColor.BLACK.toString();
		assertEquals(expected, MessageUtils.convertColor(original));
	}
	
	private String getOldCode(ChatColor color) {
        return new String("&" + color.getChar()).toLowerCase();
	}
}

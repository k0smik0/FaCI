/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (InteractionsWeigths.java) is part of facri.
 * 
 *     InteractionsWeigths.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     InteractionsWeigths.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.generators.base;

public class InteractionsWeigths {
	public static class Interactions {
		public static int POST_LIKE = 2;
		public static float RESHARED_OWN_POST = 3;
		public static int POST_COMMENT = 4;		
		public static int TAG = 5;
		public static int POST_TO_WALL = 6;
	}
	public static class Friendships {
		public static int FRIEND = 1;
	}
}

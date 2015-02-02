/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (T.java) is part of facri.
 * 
 *     T.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     T.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package christmas_egg;

public class T { /* 231 characters */
  public static void main(String[] v) {
//	  int arg = v.length>0? Integer.parseInt(v[0]): 10;
    new T( new Integer( (v.length>0? Integer.parseInt(v[0]): 10) ) );
  }
  String o="";
  T(int n) {
    for(int r=1; r<=n; ++r) {
      x(' ',n-r);
      x('*',2*r-1);
      o+="%n";
    }
    x(' ',n-1);
    System.out.printf(o+"*%n");
  }
  void x(char c,int x) { 
    if(x>0) {
      o+=c;
      x(c,x-1);
    }
  }
}

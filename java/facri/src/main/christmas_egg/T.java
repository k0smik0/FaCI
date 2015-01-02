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
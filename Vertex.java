package asst3b;
import java.awt.Point;

//public static class Vertex {

public  class Vertex {
  	final static int RADIUS = 20;
  	int x, y;
  	public boolean isFinalState = false;
  	public boolean isInitialState = false;

  	
  	public Vertex() {
  	}
  	
  	public Vertex(int x, int y) {
  		this.x = x;
  		this.y = y;
  	}
  	
  	public Vertex(Point p) {
  		this(p.x, p.y);
  	}

  	public int getX() {
  		return x;
  	}
  	
  	public void setX(int x) {
  		this.x = x;
  	}
  	
  	public int getY() {
  		return y;
  	}
  	
  	public void setY(int y) {
  		this.y = y;
  	}
  	
  	public boolean equals(Object o) {
  		Vertex c = (Vertex)o;
  		return c.getX() == x && c.getY() == y; 
  	}
  	
  	public double getDistance(Vertex c) {
  		return getDistance(x, y, c.x, c.y);
  	}
  	
  	public static double getDistance(Vertex c1, Vertex c2) {
  		return getDistance(c1.x, c1.y, c2.x, c2.y);
  	}
  	
  	public static double getDistance(double x1, double y1, double x2, double y2) {
  		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  	}
  	
  	public boolean contains(Point p) {
  		return getDistance(x, y, p.x, p.y) <= RADIUS;
  	}
}
 
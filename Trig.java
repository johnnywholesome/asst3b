// Jonathan Schmedt - cs317, Spring 2014
package asst3b;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Trig extends DFABuilder{
	
	
	public static Edge trimLine(Graphics g, int x1, int y1, int x2, int y2) {
        double length = Vertex.getDistance(x1, y1, x2, y2);
        double ratio = Vertex.RADIUS/length;
        double x3 = x1 + ratio * (x2 - x1);
        double y3 = y1 + ratio * (y2 - y1);
        ratio = (length - Vertex.RADIUS)/length;
        double x4 = x1 + ratio * (x2 - x1);
        double y4 = y1 + ratio * (y2 - y1);
        Vertex u = new Vertex((int)x3,(int)y3);
        Vertex v = new Vertex((int)x4, (int)y4);
        Edge trimmed = new Edge(u,v);
        drawArrow(g, x3, y3, x4, y4);
        return trimmed;
	}
	
	public static void drawArrow(Graphics g1, double x1, double y1, double x2, double y2) {        
		int ARR_SIZE = 8;
          Graphics2D g = (Graphics2D) g1.create();

       
          double dx = x2 - x1, dy = y2 - y1;
          double angle = Math.atan2(dy, dx);
          int len = (int) Math.sqrt(dx*dx + dy*dy);
          AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
          at.concatenate(AffineTransform.getRotateInstance(angle));
          g.transform(at);

          // Draw horizontal arrow starting in (0, 0)
          g.drawLine(0, 0, len, 0);
          g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                        new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
          
   }
	
	public static void drawTriangle(Graphics g, int x1, int y1) {
      g.drawLine(x1 - Vertex.RADIUS, y1, x1 - (Vertex.RADIUS + 15), y1 + (Vertex.RADIUS - 5));
      g.drawLine(x1 - Vertex.RADIUS, y1, x1 - (Vertex.RADIUS + 15), y1 - (Vertex.RADIUS - 5));
      g.drawLine(x1 - (Vertex.RADIUS + 15), y1 + (Vertex.RADIUS - 5), x1 - (Vertex.RADIUS + 15), y1 - (Vertex.RADIUS - 5));
	}
	
	public static void drawSelfEdge(Graphics g, Vertex from, Vertex toDraw) {
		  // The following code draws a "self" arrow for an edge, assuming
		  // x and y are the points of the vertex and R has been set to Vertex.RADIUS
		  // The magic numbers 6, 8, and 10 should be proportional to R, which is 20 here.
		Edge self = new Edge(from, from);
		int R = Vertex.RADIUS;  
		int x = toDraw.getX();
		int y = toDraw.getY();
		
		g.drawArc(x - R/2, (int) (y - (R*Math.sqrt(3))), R, (int) (R*Math.sqrt(3)),0,180);
		        
		          g.fillPolygon(new int [] {x + R/2, x + R/2 - 8, x + R/2 + 8},
		                        new int [] { (int) (y - R*(Math.sqrt(3))/2.0),
		                                     (int) (y - R*(Math.sqrt(3))/2.0) - 8,
		                                     (int) (y - R*(Math.sqrt(3))/2.0) - 8},
		                       3);
		          ArrayList<String> labels = GraphPanel.getLabelArray(self);
		          if (labels.size()> 1) {
		        	  int incrementX = 10;
		        	  for (int j=0; j<labels.size(); j++) {
		        		  if (j < labels.size()-1)
		        			  g.drawString(labels.get(j)+", ", x + R/2 + (j*incrementX), (int) (y - R*(Math.sqrt(3))/2.0) - 15);
		        		  else 
		        			  g.drawString(labels.get(j), x + R/2 + (j*incrementX), (int) (y - R*(Math.sqrt(3))/2.0) - 15);
		        	  }
		          }
		          g.drawString(labels.get(0), x + R/2, (int)(y - R*(Math.sqrt(3))/2.0) - 15);
		        /*  g.drawArc(x - R/2, (int) ((y - (Math.sqrt(3))+R/8)), R, (int) (R*Math.sqrt(3)),180,180);
		          
		          g.fillPolygon(new int [] {x + R/2, x + R/2 - 8, x + R/2 + 8},
	                        new int [] { (int) (y - (Math.sqrt(3))+ R),
	                                     (int) (y - (Math.sqrt(3))+ R) + 8,
	                                     (int) (y - (Math.sqrt(3))+ R) + 8},
	                       3);
				*/
	}
}

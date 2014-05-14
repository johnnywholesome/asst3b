// Jonatahan Schmedt - cs317, Spring 2014
package asst3b;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

// =============================
// START OF GRAPH PANEL CLASS
// =============================
class GraphPanel extends JPanel {

	public static ArrayList<Vertex> list;
	public static ArrayList<Edge> edges;
	public static ArrayList<Character> alphabet;
	public static Vertex initialState = null, current = null;
	public static boolean isInitialOn = false, tracing = false;
	private Vertex startV = null;
	private boolean isLineOn = false;
	private int endOfLineX, endOfLineY;
	public static int COLOR_INDEX = 0;
	public Color[] colors = {Color.YELLOW, Color.CYAN, Color.GREEN, Color.RED};
	// Overrides:
	// protected void paintComponent(Graphics g)

	// auxiliary methods:
	// boolean isTooCloseToVertex(Point p)
	// Vertex getContainingVertex(Point p)
	// void removeAdjacentEdges(Vertex vertex)

	GraphPanel() {
		list = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		alphabet = new ArrayList<Character>();
		isInitialOn = false;
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		// Two statements in constructor: AddMouseListener: mouseClicked,
		// Pressed, Released.
		// AddMouseMotionListener: mouseDragged.

		addMouseListener(new MouseAdapter() {

			// Why no @Override's here? -- Interface, not abstract class

			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					// Add a vertex
					if (!isTooCloseToVertex(e.getPoint())) {
						Vertex toAdd = new Vertex(e.getPoint());
						list.add(toAdd);
						try {
							FileManager.saveVertexArray(list);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						repaint();
					}
				}

				if (e.getButton() == MouseEvent.BUTTON1 && e.isAltDown()) {
					Vertex c = getContainingVertex(e.getPoint());
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).equals(c)
								&& !list.get(i).isInitialState
								&& !isInitialOn) {
							list.get(i).isInitialState = true;
							initialState = list.get(i);
							isInitialOn = true;
							try {
								FileManager.saveVertexArray(list);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						else if (list.get(i).equals(c)
								&& list.get(i).isInitialState) {
							list.get(i).isInitialState = false;
							initialState = null;
							isInitialOn = false;
							try {
								FileManager.saveVertexArray(list);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()) {
					Vertex c = getContainingVertex(e.getPoint());
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).equals(c)
								&& !list.get(i).isFinalState) {
							list.get(i).isFinalState = true;
							try {
								FileManager.saveVertexArray(list);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else if (list.get(i).equals(c)
								&& list.get(i).isFinalState) {
							list.get(i).isFinalState = false;
							try {
								FileManager.saveVertexArray(list);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				else if (e.getButton() == MouseEvent.BUTTON3) {
					// remove a vertex
					Vertex c = getContainingVertex(e.getPoint());
					if (c != null) {
						list.remove(c);
						try {
							FileManager.saveVertexArray(list);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						removeAdjacentEdges(c);
						try {
							FileManager.saveEdgeArray(edges);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						repaint();
					}
				}
			}

			public void mousePressed(MouseEvent e) {
				Vertex c = getContainingVertex(e.getPoint());
				if (c != null) {
					startV = c;
					endOfLineX = e.getX();
					endOfLineY = e.getY();
					isLineOn = true;
					repaint();
				}
			}

			public void mouseReleased(MouseEvent e) {
				Vertex c = getContainingVertex(e.getPoint());
				if (isLineOn && c != null && !c.equals(startV)) {
					// Add a new edge
					Edge toAdd = new Edge(startV, c);
					promptForLabel(toAdd);
					try {
						FileManager.saveEdgeArray(edges);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				else if (isLineOn && c != null && c.equals(startV)
						&& e.getButton() == MouseEvent.BUTTON1
						&& !e.isShiftDown() && !e.isAltDown()) {
					// Add a new self edge
					Edge toAdd = new Edge(startV, startV);
					promptForLabel(toAdd);
					try {
						FileManager.saveEdgeArray(edges);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				isLineOn = false;
				repaint();
			}
		});
		// end addMouseListener(new MouseAdapter())

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (e.isControlDown()) {
					isLineOn = false;
					Vertex c = getContainingVertex(e.getPoint());
					if (c != null) {
						c.setX(e.getX());
						c.setY(e.getY());
						try {
							FileManager.saveVertexArray(list);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						repaint();
					}
				} else if (isLineOn) {
					endOfLineX = e.getX();
					endOfLineY = e.getY();
					repaint();
					try {
						FileManager.saveEdgeArray(edges);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}

	Vertex getInitialState() {
		return initialState;
	}

	void promptForLabel(Edge edge) {
		String input = JOptionPane.showInputDialog("Enter a character");
		try {
			char label = input.charAt(0);
			edge.label = label;
			// check if edge already exists
			boolean exists = false;
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).label == label && edges.get(i).u == edge.u
						&& edges.get(i).v == edge.v) {
					edges.remove(i);
					exists = true;
					repaint();
				}
			}
			// if it doesn't exist, add it
			if (!exists) {
				edges.add(edge);
				determineAlphabet();
			}

		} catch (NullPointerException e) {
			System.out.println("New Edge Operation was cancelled.");
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	// Adds unique label to the alphabet array
	static void determineAlphabet() {
		for (int i=0; i<edges.size(); i++) {
			if (!alphabet.contains(edges.get(i).label)) {
				alphabet.add(edges.get(i).label);
			}
		}
		System.out.println(edges.size()+","+alphabet.size());
	}

	boolean isTooCloseToVertex(Point p) {
		for (int i = 0; i < list.size(); i++)
			if (Vertex.getDistance(list.get(i).getX(), list.get(i).getY(),
					p.x, p.y) <= 2 * Vertex.RADIUS + 20)
				return true;

		return false;
	}

	static Vertex getContainingVertex(Point p) {
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).contains(p))
				return list.get(i);

		return null;
	}

	void removeAdjacentEdges(Vertex vertex) {
		for (int i = 0; i < edges.size(); i++)
			if (edges.get(i).u.equals(vertex)
					|| edges.get(i).v.equals(vertex)) {
				edges.remove(i--);
			}
		try {
			FileManager.saveEdgeArray(edges);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Edge getNext(char EdgeLabelToFind, Vertex current) {
		// Determines if there is an Edge out of the current node that
		// matches the label.
		for (int i = 0; i < edges.size(); i++) {
			Edge next = edges.get(i);
			if (current == next.u && next.label == EdgeLabelToFind)
				return next;
			}
		return null;
	}
	
	// Combine all of the Edge labels into one string
	public static String getLabels(Edge toCheck) {
		String edgeLabels = "";
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).u.equals(toCheck.u)
					&& edges.get(i).v.equals(toCheck.v)) {
				edgeLabels += edges.get(i).label;
			}
		}
		return edgeLabels;
	}

	// Makes it easier to print multiple labels on top of each other
	public static ArrayList<String> getLabelArray(Edge toCheck) {
		ArrayList<String> edgeLabels = new ArrayList<String>();
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).u.equals(toCheck.u)
					&& edges.get(i).v.equals(toCheck.v)) {
				edgeLabels.add("" + edges.get(i).label);
			}
		}
		return edgeLabels;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw the circle
		for (int i = 0; i < list.size(); i++) {
			if (tracing && list.get(i).equals(current))
				// change the color of the circle
				g.setColor(colors[COLOR_INDEX]);
			else
				g.setColor(colors[0]);
			g.fillOval(list.get(i).getX() - Vertex.RADIUS, list.get(i)
					.getY() - Vertex.RADIUS, 2 * Vertex.RADIUS,
					2 * Vertex.RADIUS);
			
			//draw black circle around vertex
			g.setColor(Color.BLACK);
			g.drawOval(list.get(i).getX() - Vertex.RADIUS, list.get(i)
					.getY() - Vertex.RADIUS, 2 * Vertex.RADIUS,
					2 * Vertex.RADIUS);
			
			if (list.get(i).isFinalState)
				// draw smaller circle inside vertex
				g.drawOval(list.get(i).getX() - Vertex.RADIUS + 5, list
						.get(i).getY() - Vertex.RADIUS + 5,
						2 * Vertex.RADIUS - 10, 2 * Vertex.RADIUS - 10);
			
			if (list.get(i).isInitialState)
				// draw triangle
				Trig.drawTriangle(g, list.get(i).getX(), list.get(i).getY());
		}

		if (isLineOn) {
			// draws line while dragging mouse
			g.drawLine(startV.getX(), startV.getY(), endOfLineX, endOfLineY);
			// drawArrow(g, startV.getX(), startV.getY(), endOfLineX,
			// endOfLineY);
		}

		// error starts here.
		for (int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			Vertex from = new Vertex(e.u.getX(),e.u.getY());
			if (edges.get(i).u == edges.get(i).v) {
				Vertex toDraw = new Vertex(edges.get(i).u.getX(),
						edges.get(i).v.getY());
				Trig.drawSelfEdge(g, from, toDraw);
			}

			else {
				Edge trimmed = Trig.trimLine(g, edges.get(i).u.getX(),
						edges.get(i).u.getY(), edges.get(i).v.getX(),
						edges.get(i).v.getY());

				int x = (trimmed.u.getX() + trimmed.v.getX()) / 2;
				int y = (trimmed.u.getY() + trimmed.v.getY()) / 2;
				x = (trimmed.u.getX() + x) / 2;
				y = (trimmed.u.getY() + y) / 2;
				ArrayList<String> labels = getLabelArray(e);
				if (labels.size() > 1) {
					int incrementY = 10;
					for (int j = 0; j < labels.size(); j++) {
						g.drawString(labels.get(j), x, y - (j * incrementY));
					}
				} else {
					g.drawString(""+edges.get(i).label, x, y);
				}
			}
		}
	}
} // end of GraphPanel class

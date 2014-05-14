package asst3b;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
	static String strToWrite;
	static FileWriter write; 
	static BufferedWriter out;
	static File toRead;
	static File vertexTmp = new File(System.getProperty("user.dir")+"/asst3b/data/vertex.tmp");
	static File edgeTmp = new File(System.getProperty("user.dir")+"/asst3b/data/edge.tmp");
	
	
	public static void saveVertex(String fileName) {
	    File fileToSave = new File(fileName);
		if (vertexTmp.renameTo(fileToSave)) {
	    	System.out.println("Vertex data was saved.");
	    }
		else {
			System.out.println("Error:  Vertex data not saved.");
		}
		
	}
	
	public static void saveEdges(String fileName) {
		File fileToSave = new File(fileName);
		if (edgeTmp.renameTo(fileToSave)) {
			System.out.println("Edge data was saved.");
		}
		else {
			System.out.println("Error:  Edge data not saved.");
		}
	}
	
	public static void saveVertexArray(ArrayList<Vertex> toSave) throws IOException{
		strToWrite = "";
		write = new FileWriter(System.getProperty("user.dir")+"/asst3b/data/vertex.tmp", false);
		out = new BufferedWriter(write);
		
		for (Vertex x : toSave) {
			strToWrite += x.getX()+","+x.getY()+","+x.isFinalState+","+x.isInitialState+"\r\n";
		}
		out.write(strToWrite);
		out.close();
	}
	
	public static void saveEdgeArray(ArrayList<Edge> toSave) throws IOException{
		strToWrite = "";
		write = new FileWriter(System.getProperty("user.dir")+"/asst3b/data/edge.tmp", false);
		out = new BufferedWriter(write);
		
		for (Edge x : toSave) {
			strToWrite += x.u.getX()+","+x.u.getY()+","+x.v.getX()+","+x.v.getY()+","+x.label+"\r\n";
		}
		out.write(strToWrite);
		out.close();
	}

	
	public static void saveVertex(Vertex toWrite) throws IOException {
		strToWrite = "";
		write = new FileWriter(System.getProperty("user.dir")+"/asst3b/data/vertex.tmp",true);
		out = new BufferedWriter(write);
		strToWrite += toWrite.getX()+","+toWrite.getY()+","+toWrite.isFinalState+","+toWrite.isInitialState+"\r\n";
		out.write(strToWrite);
		out.close();
	}
	
	public static void saveEdge(Edge toWrite) throws IOException {
		strToWrite = "";
		write = new FileWriter(System.getProperty("user.dir")+"/asst3b/data/edge.tmp",true);
		out = new BufferedWriter(write);
		strToWrite += toWrite.u.getX()+","+toWrite.u.getY()+","+toWrite.v.getX()+","+toWrite.v.getY()+","+toWrite.label+"\r\n";
		out.write(strToWrite);
		out.close();
	}
	
	public static void save(String fileName) throws IOException {
		strToWrite = "";
		write = new FileWriter(fileName, false);
		out = new BufferedWriter(write);
		
		if (vertexTmp.exists()) {
			toRead = new File(System.getProperty("user.dir")+"/asst3b/data/vertex.tmp");
			Scanner f = new Scanner(toRead);
			while (f.hasNextLine()) {
				strToWrite+=f.nextLine()+"\r\n";
			}
			f.close();
		}
		if (edgeTmp.exists()) {
			toRead = new File(System.getProperty("user.dir")+"/asst3b/data/edge.tmp");
			Scanner f = new Scanner(toRead);
			while (f.hasNextLine()) {
				strToWrite+=f.nextLine()+"\r\n";
			}
			f.close();
		}
		if (vertexTmp.exists() || edgeTmp.exists())
			out.write(strToWrite);
		
		out.close();
		
		if (vertexTmp.exists())
			vertexTmp.delete();
		
		if (edgeTmp.exists())
			edgeTmp.delete();
	}
	
	public static void open(String fileName) throws IOException {
		toRead = new File(fileName);
		Scanner f = new Scanner(toRead);
		int i=-1;
		while (f.hasNextLine()) {
			i++;
			String[] line = f.nextLine().split(",");
			if (line.length == 4) {
				Vertex toAdd = new Vertex(Integer.parseInt(line[0]),Integer.parseInt(line[1]));
				toAdd.isFinalState = Boolean.valueOf(line[2]);
				toAdd.isInitialState = Boolean.valueOf(line[3]);
				GraphPanel.list.add(toAdd);
				if (toAdd.isInitialState) {
					GraphPanel.initialState = toAdd;
					GraphPanel.isInitialOn = true;
				}
			}
			else if (line.length == 5) {
				Point uPoint = new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
				Vertex u = GraphPanel.getContainingVertex(uPoint);
				Point vPoint = new Point(Integer.parseInt(line[2]), Integer.parseInt(line[3]));
				Vertex v = GraphPanel.getContainingVertex(vPoint);
				Edge toAdd = new Edge(u,v);
				String label = line[4];
				toAdd.label = label.charAt(0);
				GraphPanel.edges.add(toAdd);
				GraphPanel.determineAlphabet();
			}
			saveVertexArray(GraphPanel.list);
			saveEdgeArray(GraphPanel.edges);
		}
		f.close();
	}
	
	
	public static void deleteVertex(Vertex toDelete) throws IOException {
		strToWrite = "";
		toRead = new File(System.getProperty("user.dir")+"/asst3b/data/vertex.tmp");
		Scanner f = new Scanner(toRead);
		String toMatch = toDelete.getX()+","+toDelete.getY()+","+toDelete.isFinalState+","+toDelete.isInitialState;
		while(f.hasNextLine()) {
			String current = f.nextLine();
			if (!current.equals(toMatch)) {
				strToWrite += current+"\r\n";
			}
		}
		f.close();
		write = new FileWriter(System.getProperty("user.dir")+"/asst3b/data/vertex.tmp");
		out = new BufferedWriter(write);
		out.write(strToWrite);
		out.close();
	}
	

}

// Jonathan Schmedt  CS-317, Spring 2014

package asst3b;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;



public class DFABuilder extends JFrame {
	// Define graphics components
	JMenuBar mb = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JButton resetBtn = new JButton("Reset"), runBtn = new JButton("Run"),
			traceBtn = new JButton("Trace"), stepBtn = new JButton("Step");
	JTextField inField = new JTextField(20);
	JLabel statusLbl = new JLabel(""), doneLbl = new JLabel(""),
			stillToDoLbl = new JLabel(""), saveStatusLbl = new JLabel("");
	TitledBorder panelBorder = new TitledBorder("Enter Input Here"),
			statusBorder = new TitledBorder("status"),
			doneBorder = new TitledBorder("done"),
			stillToDoBorder = new TitledBorder("still to do");

	Dimension btnSize = new Dimension(100, 25),
			lblSize = new Dimension(100, 40);
	JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
	static DFABuilder frame;
	JPanel bottom;
	public JMenuItem close, open, save, exit;
	ImageIcon openIcon = new ImageIcon("asst3b/open.gif");
	ImageIcon saveIcon = new ImageIcon("asst3b/save.gif");
	ImageIcon exitIcon = new ImageIcon("asst3b/cross.gif");
	ImageIcon closeIcon = new ImageIcon("asst3b/minus.gif");	
	// Data to store
	String toAnalyze = "", processed = "", remaining = "";
	int stringIndex = 0;

	// =============================
	// DEFAULT CONSTRUCTOR
	// =============================

	public DFABuilder() {
		bottom = new GraphPanel();
		mb.setBackground(Color.WHITE);
		
		open = new JMenuItem("Open File", openIcon);
		open.addActionListener(new MenuBarListener());
		save = new JMenuItem("Save File", saveIcon);
		save.addActionListener(new MenuBarListener());
		close = new JMenuItem("Close File", closeIcon);
		close.addActionListener(new MenuBarListener());
		
		exit = new JMenuItem("Quit", exitIcon);
		exit.addActionListener(new MenuBarListener());
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(close);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		mb.add(fileMenu);
		setJMenuBar(mb);
		
		resetBtn.addActionListener(new ResetBtnListener());
		runBtn.addActionListener(new RunBtnListener());
		traceBtn.addActionListener(new TraceBtnListener());
		stepBtn.addActionListener(new StepBtnListener());
		top.add(resetBtn);
		top.add(inField);
		top.add(runBtn);
		top.add(traceBtn);
		top.add(stepBtn);
		statusLbl.setPreferredSize(lblSize);
		statusLbl.setBorder(statusBorder);
		top.add(statusLbl);
		doneLbl.setPreferredSize(lblSize);
		doneLbl.setBorder(doneBorder);
		top.add(doneLbl);
		stillToDoLbl.setPreferredSize(lblSize);
		stillToDoLbl.setBorder(stillToDoBorder);
		top.add(stillToDoLbl);
		top.setBorder(panelBorder);
		add(top, BorderLayout.NORTH);
		add(bottom, BorderLayout.CENTER);

	} // END OF CONSTRUCTOR

	public void processInput(String toAnalyze) {
		if (GraphPanel.isInitialOn) {
			GraphPanel.current= GraphPanel.initialState;
			int i=0;
			for (i = 0; i < toAnalyze.length(); i++) {
				if (GraphPanel.alphabet.contains(toAnalyze.charAt(i))) {				
					Edge next = GraphPanel.getNext(toAnalyze.charAt(i), GraphPanel.current);
					if (next != null)
						GraphPanel.current = next.v;
				}
			}
			if (GraphPanel.current.isFinalState) {
				statusLbl.setText("ACCEPTED!");
				top.repaint();

			} else {
				statusLbl.setText("REJECTED!!");
				top.repaint();
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"There is no Initial State declared.");
		}
	}

	// =============================
	// START OF ACTION LISTENERS
	// =============================
	
	// START OF MENUBAR LISTENER
	class MenuBarListener implements ActionListener {
		MenuBarListener() {
			
		}
		
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == open) {
				String fileToOpen;
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("CSV Comma Separated", "csv");
				fc.setFileFilter(filter);
				String currentDir = System.getProperty("user.dir");
				currentDir += "/asst3b/data";
				fc.setCurrentDirectory(new File(currentDir));
				int response = fc.showOpenDialog(frame);
				if (response == JFileChooser.APPROVE_OPTION) {
					fileToOpen = fc.getSelectedFile().toString();
					try {
						FileManager.open(fileToOpen);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					fileToOpen = "Action Cancelled.";
				}
				bottom.repaint();
			}
			
			if (e.getSource() == save) {
				String fileToSave;
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("CSV Comma Separated", "csv");
				fc.setFileFilter(filter);
				String currentDir = System.getProperty("user.dir");
				currentDir+="/asst3b/data";
				fc.setCurrentDirectory(new File(currentDir));
				int response = fc.showSaveDialog(frame);
				if (response == JFileChooser.APPROVE_OPTION) {
					fileToSave = fc.getSelectedFile().toString();
					try {
						FileManager.save(fileToSave);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					fileToSave = "Action Cancelled";
				}
				JOptionPane.showMessageDialog(null, "File successfully saved to:\n"+fileToSave);
				bottom.repaint();
			}
			
			else if(e.getSource() == close) {
				dispose();
				Thread DFA= new Thread() {
					public void run() {
						String[] args = {};
						DFABuilder.main(args);
					}
				};
				DFA.start();
				bottom.repaint();
			}
			
			else if (e.getSource() == exit) {
				System.exit(0);
			}
		}
	}
	
	
	// START OF RESETBTNLISTENER CLASS
	class ResetBtnListener implements ActionListener {
		ResetBtnListener() {
		}

		public void actionPerformed(ActionEvent e) {
			processed = "";
			remaining = "";

			statusLbl.setText("");
			doneLbl.setText("");
			stillToDoLbl.setText("");
			GraphPanel.tracing = false;
			GraphPanel.COLOR_INDEX = 0;
			top.repaint();
			bottom.repaint();
		}
	} // end of ResetBtnListener

	// START OF RUNBTNLISTENER CLASS
	class RunBtnListener implements ActionListener {
		RunBtnListener() {

		}

		public void actionPerformed(ActionEvent e) {
			toAnalyze = inField.getText();
			processInput(toAnalyze);

		}
	} // end of RunBtnListener

	// START OF TRACEBTNLISTENER CLASS
	class TraceBtnListener implements ActionListener {
		TraceBtnListener() {

		}

		public void actionPerformed(ActionEvent e) {
			toAnalyze = inField.getText();
			stillToDoLbl.setText(toAnalyze);
			processed = "";
			remaining = "";
			statusLbl.setText("");
			doneLbl.setText("");
			GraphPanel.tracing = true;
			GraphPanel.COLOR_INDEX = 1;
			stringIndex = 0;
			GraphPanel.current = GraphPanel.initialState;
			top.repaint();
			bottom.repaint();
		}
	} // end of TraceBtnListener class

	// START OF STEPBTNLISTENER CLASS
	class StepBtnListener implements ActionListener {
		StepBtnListener() {

		}

		public void actionPerformed(ActionEvent e) {
			toAnalyze = inField.getText();
			if (stringIndex >= 0 && stringIndex < toAnalyze.length()) {
				char toFind = toAnalyze.charAt(stringIndex);
				if (GraphPanel.alphabet.contains(toFind)) {
					Edge next = GraphPanel.getNext(toFind, GraphPanel.current);
					if (next != null) {
						GraphPanel.current = next.v;
					}
					bottom.repaint();
					processed += toFind;
					stringIndex++;
					doneLbl.setText(processed);
					stillToDoLbl.setText(toAnalyze.substring(stringIndex,
							toAnalyze.length()));
				}
				else {
					JOptionPane.showMessageDialog(null, ""+toFind+" is not defined in the alphabet.");
				}
			}

			if (stringIndex >= toAnalyze.length()) {
				if (GraphPanel.current.isFinalState) {
					GraphPanel.COLOR_INDEX = 2;
					bottom.repaint();
					statusLbl.setText("ACCEPTED!");
					top.repaint();
				} else {
					GraphPanel.COLOR_INDEX = 3;
					bottom.repaint();
					statusLbl.setText("REJECTED!");
					top.repaint();
				}
			}
		}
	} // end of StepBtnListener class

	
	// START OF MAIN METHOD
	public static void main(String[] args) {
		frame = new DFABuilder();
		frame.setTitle("DFABuilder - Jonathan Schmedt, Spring 2014");
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		/*
		 * frame.addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent we) { String
		 * ObjButtons[] = {"Yes","No"}; int PromptResult =
		 * JOptionPane.showOptionDialog
		 * (null,"Would you like to save this graph for next time?"
		 * ,"DFA Builder"
		 * ,JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,
		 * null,ObjButtons,ObjButtons[1]);
		 * if(PromptResult==JOptionPane.YES_OPTION) { ManageData.saveData(); }
		 * System.exit(0); } });
		 */
	} // END OF MAIN METHOD


} // end of DFABuilder class
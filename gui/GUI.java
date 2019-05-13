package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import controller.Controller;

import neuralNet.HopfieldNet;

public class GUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2941318999657277463L;
	
	protected static JRadioButton drawCheck = new JRadioButton("Draw");
	protected static final Grid2D grid = new Grid2D();
	protected JList list;
	protected Vector<String> listData;
	private Controller controller;
	private JLabel netStatus;
	private JLabel pattern;
	private JProgressBar bar;
	
	public GUI(Controller cont) {
		listData = new Vector<String>();
		list = new JList(listData);
		controller = cont;
		drawCheck.setSelected(true);
		this.setSize(1000, 500);
		this.setLocation(150,100);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		initMainPane();
	}
	
	private void initMainPane() {
		makeMenuBar();
		list.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				String name = (String)list.getSelectedValue();
				grid.setStates(controller.getData(name));
			}
			
		});
		
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				grid.clearGrid();
				
			}
			
		});
		
		JButton addButton = new JButton("Add");
		
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// open Popup. Ask for Name. Save in Map and display in List
				String name = JOptionPane.showInputDialog("Please enter the name of the pattern");
				controller.addData(name, grid.getStates());
				listData.add(name);
				list.updateUI();
			}
			
		});
		
		JButton removeButton = new JButton("Remove");
		
		removeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// remove currently displayed Pattern from List
				
				String name;
				if(!((name = (String)list.getSelectedValue()).compareTo("") == 0)) {
					listData.remove(name);
					controller.removeData(name);
					grid.clearGrid();
					list.updateUI();
				}
				
			}
			
		});
		
		JButton trainHopButton = new JButton("Train");
		
		trainHopButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
	
				controller.trainNewNet();		
				netStatus.setText("trained");
				pattern.setText(Integer.toString(listData.size()));
			}
			
		});
		
		JButton evalHopButton = new JButton("Evaluate");
		
		evalHopButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// Evaluate the current input on the canvas with the trained Net

				controller.evaluateNet(grid.getStates());
			}
			
		});

		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(clearButton);
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		
		
		JPanel hopfieldButtons = new JPanel(new FlowLayout());
		hopfieldButtons.add(trainHopButton);
		hopfieldButtons.add(evalHopButton);
		
		
		JPanel numberPanel = new JPanel(new BorderLayout());
		
		JScrollPane scrolly = new JScrollPane(list);
		numberPanel.add(scrolly);
		
		
		JLabel hopfieldHeader = new JLabel("Hopfield Network - Hebbian Learning");
		JPanel hopfieldPanel = new JPanel(new BorderLayout());
		
		JPanel netPanel = new JPanel(new GridLayout(7,2));
		bar = new JProgressBar(0,100);
		bar.setVisible(true);
		bar.setStringPainted(true);
		JLabel netLabel = new JLabel("Net:");
		netStatus = new JLabel("untrained");
		JLabel netPattern = new JLabel("Training-Pattern:");
		pattern = new JLabel("0");
		
		netPanel.add(new JLabel());
		netPanel.add(new JLabel());
		netPanel.add(new JLabel());
		netPanel.add(new JLabel());
		netPanel.add(netLabel);
		netPanel.add(netStatus);
		netPanel.add(netPattern);
		netPanel.add(pattern);
		netPanel.add(bar);
		netPanel.add(new JLabel());
		netPanel.add(new JLabel());
		netPanel.add(new JLabel());
		netPanel.add(new JLabel());
		netPanel.add(new JLabel());
		
		hopfieldPanel.add(hopfieldHeader, BorderLayout.NORTH);
		hopfieldPanel.add(hopfieldButtons, BorderLayout.SOUTH);
		hopfieldPanel.add(netPanel);
		
		
		JPanel patternPanel = new JPanel(new GridLayout(3,1));
		patternPanel.add(drawCheck);
		patternPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		patternPanel.add(buttonPanel);
		
		JTabbedPane actionPane = new JTabbedPane();
		
		actionPane.add("Hopfield", hopfieldPanel);
		
		JPanel rightSide = new JPanel(new BorderLayout());
		rightSide.add(actionPane);
		rightSide.add(patternPanel, BorderLayout.SOUTH);
		
		JPanel leftSide = new JPanel(new BorderLayout());
		leftSide.add(new JLabel("Data:            "), BorderLayout.NORTH);
		leftSide.add(numberPanel);
		leftSide.add(new JLabel("  "), BorderLayout.EAST);
		
		this.add(grid);
		this.add(rightSide, BorderLayout.EAST);
		this.add(leftSide, BorderLayout.WEST);
		
		this.setVisible(true);
	}
	
	public Grid2D getGrid() {
		return grid;
	}
	
	public void updateGrid(int[] pattern) {
		grid.setStates(pattern);
	}
	
	public void updateSingleState(int nr, int state) {
		grid.setPixel(nr, state);
	}
	
	public void addListData(String name) {
		listData.add(name);
		list.updateUI();
	}

	public void clearListData() {
		listData.clear();
	}
	
	public void setProgress(int i){
        if(i == -1){
           this.bar.setValue(0);
           this.bar.setString("done");  
        }
        else if(i == -2){
           this.bar.setString("");
        }
        else {
           this.bar.setValue(i);
           this.bar.setString(i+" %");
        }
    } 
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Exit")) {
	            System.exit(0);
	        } else if(e.getActionCommand().equals("Save Data")) {
	        	JFileChooser save = new JFileChooser();

	            save.setDialogTitle("Save Dictionary to ...");
	            save.setApproveButtonText("Speichern"); 
	            save.setVisible(true);
	         
	            if(save.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            	controller.saveData(save.getSelectedFile().toString());
	            }
	        } else if(e.getActionCommand().equals("Load Data")) {
	        	JFileChooser load = new JFileChooser();

	            load.setDialogTitle("Load Dictionary from ..."); 
	            load.setVisible(true);
	         
	            if(load.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

	            	controller.loadData(load.getSelectedFile().toString()); 
	            	netStatus.setText("new Data loaded!");
					pattern.setText("train again!");
	            }
	        }		
	}
	
	 private void makeMenuBar() {
	     
	        JMenuBar menuBar = new JMenuBar();
	        this.setJMenuBar(menuBar);
	        
	        JMenu fileMenu = new JMenu("File");
	        menuBar.add(fileMenu);
	        
	        JMenuItem saveData = new JMenuItem("Save Data");
	        saveData.addActionListener(this);
	        JMenuItem loadData = new JMenuItem("Load Data");
	        loadData.addActionListener(this);
	        JMenuItem exitAction = new JMenuItem("Exit");
	        exitAction.addActionListener(this);
	   
	          
	        fileMenu.addSeparator();
	        fileMenu.add(saveData);
	        fileMenu.add(loadData);
	        fileMenu.addSeparator();
	        fileMenu.add(exitAction);
	        
	        
	    }
}

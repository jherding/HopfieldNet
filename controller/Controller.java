package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import neuralNet.HopfieldNet;
import gui.GUI;

public class Controller {

	private GUI gui;
	private HopfieldNet net;
	private Map<String, int[]> data;
	
	public Controller() {
		gui = new GUI(this);
		data = new HashMap<String, int[]>();
	}
	
	public void addData(String name, int[] states) {
		data.put(name, states);
	}
	
	public void removeData(String name) {
		data.remove(name);
	}
	
	public int[] getData(String name) {
		return data.get(name);
	}
	
	public void updateGUI(int[] states) {
		gui.updateGrid(states);
	}
	
	public void updatePixel(int i, int a) {
		gui.updateSingleState(i, a);
	}
	
	public void trainNewNet() {
		net = new HopfieldNet(data, this);
		net.train();
	}
	
	public void evaluateNet(int[] pattern) {
		net.evaluate(pattern);
	}
	
	public void updateProgress(int i) {
		gui.setProgress(i);
	}
	
	
	public void saveData(String filename) {
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(data);
			oos.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadData(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			data = (Map)ois.readObject();
			data.remove(null);
			
			Set<String> set = data.keySet();
			gui.clearListData();
			for(String s: set) {
				gui.addListData(s);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Controller();

	}


}

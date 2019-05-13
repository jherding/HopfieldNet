package neuralNet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import controller.Controller;

public class HopfieldNet {

	private Map<int[], Integer> stableStates;
	private int[][] weights;
	private int[] thresholds;
	private Controller controller;
	
	public HopfieldNet(Controller cont) {
		stableStates = new HashMap<int[], Integer>();
		weights = new int[256][256];
		thresholds = new int[256];
		controller = cont;
		for(int i = 0; i < 256; i++) {
			thresholds[i] = 0;
			for(int j = 0; j < 256; j++) {
				weights[i][j] = 0;
			}			
		}
	}
	
	public HopfieldNet(Map<String, int[]> map, Controller cont) {
		this(cont);
		
		Set<Entry<String, int[]>> entries = map.entrySet();
		
		for(Entry<String, int[]> e: entries) {
			System.out.println(e.getKey());
			stableStates.put(e.getValue(), mapper(e.getKey()));
		}
	}
	
	
	public void addStableState(int[] states, int number) {
		stableStates.put(states, number);
	}
	
	/**
	 * trains the net accorcing to the Hebbian Learning algorithm
	 * only first half of matrix filled -> first integer of weight has to be the smaller one!
	 *
	 */
	public void train() {
		new Train().start();
	}
	
	public void evaluate(int[] pattern) {
		new Evaluate(pattern).start();
	}
	
	
	/**
	 * Maps a given name of a number onto its integer value
	 * @param string
	 * @return
	 */
	private int mapper(String string) {

		if(string.contains("one")) return 1;
		else
			if(string.contains("two")) return 2;
			else
				if(string.contains("three")) return 3;
				else
					if(string.contains("four")) return 4;
					else
						if(string.contains("five")) return 5;
						else
							if(string.contains("six")) return 6;
							else
								if(string.contains("seven")) return 7;
								else
									if(string.contains("eight")) return 8;
									else
										if(string.contains("nine")) return 9;
										else
											if(string.contains("zero")) return 0;
		return -1;
	}
	
	/**
	 * trains the net accorcing to the Hebbian Learning algorithm
	 * only first half of matrix filled -> first integer of weight has to be the smaller one!
	 *
	 */
	class Train extends Thread {
		
		public void run() {
			Set<Entry<int[], Integer>> entries = stableStates.entrySet();
			int weight = 0;
			int[] pattern;
			double loops = 100.0 / (256 * 256 / 2.0 * entries.size());
			double progress = 0;
			
			for(int i = 0; i < 256; i++) {
				for(int j = i+1; j < 256; j++) {
					for(Entry<int[], Integer> e: entries) {
						pattern = e.getKey();
						weight += (pattern[i] * pattern[j]);
						progress += loops;
						controller.updateProgress((int)progress);
					}
					weights[i][j] = weight;
					weight = 0;
				}
//				progress = i / 256.0 * 100.0;
//				controller.updateProgress((int)progress);
			}
		controller.updateProgress(-1);	
		}
	}
	
	class Evaluate extends Thread {
		private int[] pattern;
		
		public Evaluate(int[] pattern) {
			this.pattern = pattern;
		}
		
		public void run() {
			int input = 0;
			int first,second = 0;
			boolean changes = true;

			while(changes) {
				changes = false;
				for(int i = 0; i < 256; i++) {
					for(int j = 0; j < 256; j++) {
						if(i <= j) {
							first = i;
							second = j;
						} else {
							first = j;
							second = i;
						}
						
						input += pattern[j] * weights[first][second]; 
					}
					
					if(input > 0) {
						if(pattern[i] == -1) changes = true;
						
						pattern[i] = +1;
					} else if(input < 0) {
						if(pattern[i] == +1) changes = true;
						
						pattern[i] = -1;
					} 
					controller.updateGUI(pattern);
					input = 0;
				}
			}
//			controller.updateGUI(pattern);
		}
		
	}
}

package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class Grid2D extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean pressed;
	private Pixel[] maze;

	public Grid2D() {
		pressed = false;
		maze = new Pixel[256];
		this.setLayout(new GridLayout(16,16));
		this.setSize(20, 20);
		this.setVisible(true);
		
		for(int i = 0; i < 256; i++) {
			maze[i] = new Pixel(i);
			this.add(maze[i]);
		}
		
	}
	
	public void clearGrid() {
		for(int i = 0; i < 256; i++) {
			maze[i].setState(-1);
			maze[i].setBackground(Color.WHITE);
		}
	}
	
	public Pixel[] getGrid() {
		return maze;
	}
	
	public int[] getStates() {
		int[] temp = new int[256];
		
		for(int i = 0; i< 256; i++) {
			temp[i] = maze[i].getState();
		}
		
		return temp;
	}
	
	public void setStates(int[] states) {
		for(int i = 0; i < 256; i++) {
			maze[i].setState(states[i]);
		}
	}
	
	public void setPixel(int number, int act) {
		maze[number].setState(act);
	}
	
	class Pixel extends Canvas {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int state;
		private int pos;
		
		public Pixel(int pos) {
			state = -1;
			this.pos = pos;
			this.setVisible(true);
			this.setBackground(Color.WHITE);
			this.setSize(5,5);
			this.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent arg0) {
					update();
				}

				public void mouseEntered(MouseEvent e) {
					if(pressed)
						update();
				}
				
				public void mousePressed(MouseEvent arg0) {
					pressed = true;
				}
				
				
				public void mouseReleased(MouseEvent e) {
					pressed = false;
				}
				
			});
		}
		
		public void update() {
//			if(this.getBackground() == Color.WHITE) {
//				this.setBackground(Color.BLACK);
//				setState(1);
//			}
//			else 
//				if(this.getBackground() == Color.BLACK) { 
//					this.setBackground(Color.WHITE);
//					setState(-1);
//			}
			if(GUI.drawCheck.isSelected()) {
				setState(1);
			} else {
				setState(-1);
			}
		}
		
		public void setState(int x) {
			state = x;
			if(x == 1) {
				this.setBackground(Color.BLACK);
			} else {
				if(x == -1) {
					this.setBackground(Color.WHITE);
				} else {
					throw new RuntimeException("Error in setState()");
				}
			}
		}
		
		public int getState() {
			return state;
		}
				
		public int getPos() {
			return pos;
		}
	}

}

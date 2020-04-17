package Tetris;

import java.io.IOException;
import javax.swing.JFrame;

public class Window { //Creates the window for the game
	
	private JFrame window;
	private Board board;
	public static final int width = 315, height = 638;
	
	public Window() throws IOException {
		window = new JFrame("Tetris POG");
		window.setSize(width, height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
		board = new Board();
		
		window.add(board);
		window.addKeyListener(board);
		
		window.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Window();
	}

}

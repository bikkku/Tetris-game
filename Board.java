package Tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener { //Main functionality class

	private BufferedImage blocks;
	private final int blockSize = 30;
	private final int boardWidth = 10, boardHeight = 20;
	private int[][] board = new int[boardHeight][boardWidth];
	private Shape[] shapes = new Shape[7];
	private Shape currentShape;
	private Timer timer;
	private final int FPS = 60;
	private final int delay = 1000/FPS;
	private boolean gameOver = false;

	private static final long serialVersionUID = 1L;

	public Board() throws IOException {
		blocks = ImageIO.read(Board.class.getResource("/tetrissprite.png"));
		timer = new Timer(delay, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				repaint();
			}
		});

		timer.start();

		//Crop the different block shapes

		shapes[0] = new Shape(blocks.getSubimage(0, 0, blockSize, blockSize), new int[][] {
			{1, 1, 0}, //Z Shape
			{0, 1, 1} 
		}, this, 1);

		shapes[1] = new Shape(blocks.getSubimage(blockSize, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1}, //L Shape
			{1, 0, 0} 
		}, this, 2);

		shapes[2] = new Shape(blocks.getSubimage(blockSize*2, 0, blockSize, blockSize), new int[][] {
			{1, 1}, //O Shape
			{1, 1} 
		}, this, 3);

		shapes[3] = new Shape(blocks.getSubimage(blockSize*3, 0, blockSize, blockSize), new int[][] {
			{0, 1, 1}, //S Shape
			{1, 1, 0} 
		}, this, 4);

		shapes[4] = new Shape(blocks.getSubimage(blockSize*4, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1, 1} //I Shape
		}, this, 5);

		shapes[5] = new Shape(blocks.getSubimage(blockSize*5, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1}, //T Shape
			{0, 1, 0} 
		}, this, 6);

		shapes[6] = new Shape(blocks.getSubimage(blockSize*6, 0, blockSize, blockSize), new int[][] {
			{1, 1, 1}, //J Shape
			{0, 0, 1} 
		}, this, 7);
		setNextShape();
	}

	public void update() { //Generates a new shape or ends the game
		currentShape.update();
		if (gameOver) {
			timer.stop();
		}
	}

//Some basic getters	
	
	public int getBlockSize() {
		return blockSize;
	}
	
	public int[][] getBoard() {
		return board;
	}

	public void paintComponent(Graphics g) { //Creates a grid, and endscreen text, renders blocks
		super.paintComponent(g);
		currentShape.render(g);
		for (int i = 0; i < boardHeight; i++) {
			g.drawLine(0, i * blockSize, boardWidth*blockSize, i*blockSize);
		}
		for (int j = 0; j < boardWidth; j++) {
			g.drawLine(j * blockSize, 0, j*blockSize, boardHeight*blockSize);
		}
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != 0) {
					g.drawImage(blocks.getSubimage((board[row][col] - 1)*blockSize, 0, blockSize, blockSize), col*blockSize, row*blockSize, null);
				}
			}
		}
		if (gameOver == true) {
			String msg = "gotem";
			Font small = new Font("", Font.ITALIC, 25);
			g.setColor(Color.BLACK);
			g.setFont(small);
			g.drawString(msg, boardWidth / 2, boardHeight);
		}
	}

	public void setNextShape() { //Randomly generates next tetris piece, if a piece extends the top of the board the game stops
		int index = (int)(Math.random()*shapes.length);
		Shape newShape = new Shape(shapes[index].getBlock(), shapes[index].getCoordinates(), this, shapes[index].getColour());
		currentShape = newShape;
		for (int row = 0; row < currentShape.getCoordinates().length; row++) {
			for (int col = 0; col < currentShape.getCoordinates().length; col++) {
				if (currentShape.getCoordinates()[row][col] != 0) {
					if (board[row][col + 3] != 0) {
						gameOver = true;
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) { //Player inputs
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentShape.setDeltaX(-1);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentShape.setDeltaX(1);
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.dropSpeed();
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			currentShape.rotate();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { //Piece falls at normal speed when down key is released
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.normalSpeed();
		}
	}

	// unused
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}

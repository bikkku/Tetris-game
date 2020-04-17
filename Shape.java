package Tetris;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shape {

	private BufferedImage block;
	private int[][] coordinates;
	private Board board;
	private int deltaX = 0;
	private int x, y;
	private int colour;

	private int normalSpeed = 600, dropSpeed = 60, currentSpeed;
	private long time, lastTime;

	private boolean collision = false, moveX = false;

	public Shape(BufferedImage block, int[][] coordinates, Board board, int colour) { //Class that manages the pieces
		this.block = block;
		this.coordinates = coordinates;
		this.board = board;
		this.colour = colour;

		currentSpeed = normalSpeed;

		x = 3;
		y = 0;

		time = 0;
		lastTime = System.currentTimeMillis();
	}

	public void update() { //This method allows for piece movement and sets limitations
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();

		if (collision) {
			for (int row = 0; row < coordinates.length; row++) {
				for (int col = 0; col < coordinates[row].length; col++) {
					if (coordinates[row][col] != 0) {
						board.getBoard()[y + row][x + col] = colour;
					}
				}
			}
			deleteLine();
			board.setNextShape();
		}

		if (!(x + deltaX + coordinates[0].length > 10) && !(x + deltaX < 0)) {
			for (int row = 0; row < coordinates.length; row++) {
				for (int col = 0; col < coordinates[row].length; col++) {
					if (coordinates[row][col] != 0) {
						if (board.getBoard()[y + row][x + deltaX + col] != 0) {
							moveX = false;
						}
					}
				}
			}
			if (moveX == true) {
				x += deltaX;
			}
		}

		if (!(y + 1 + coordinates.length > 20)) {
			for (int row = 0; row < coordinates.length; row++) {
				for (int col = 0; col < coordinates[row].length; col++) {
					if (coordinates[row][col] != 0) {
						if (board.getBoard()[y + row + 1][x + col] != 0) {
							collision = true;
						}
					}
				}
			}

			if (time > currentSpeed) {
				y++;
				time = 0;
			}
		} else {
			collision = true;
		}
		deltaX = 0;
		moveX = true;
	}

	public void rotate() {//Rotates pieces with limitations
		if (collision) {
			return;
		}
		int[][] rotated = null;
		rotated = getTranspose(coordinates);
		rotated = getReverse(rotated);

		if (x + rotated[0].length > 10 || y + rotated.length > 20) {
			return;
		}

		for (int row = 0; row < rotated.length; row++) {
			for (int col = 0; col < rotated[0].length; col++) {
				if (board.getBoard()[y + row][x + col] != 0) {
					return;
				}
			}
		}
		coordinates = rotated;
	}

	private int[][] getTranspose(int[][] matrix) {//Gets the transpose of a matrix, used for rotate method
		int[][] transposed = new int[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				transposed[j][i] = matrix[i][j];
			}
		}
		return transposed;
	}

	private int[][] getReverse(int[][] matrix) {//Reverses a matrix, used for rotate method
		int mid = matrix.length / 2;
		for (int i = 0; i < mid; i++) {
			int[] m = matrix[i];
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = m;
		}
		return matrix;
	}


	public void render(Graphics g) {//Renders the block used for each piece
		for (int row = 0; row < coordinates.length; row++) {
			for (int col = 0; col < coordinates[row].length; col++) {
				if (coordinates[row][col] != 0) {
					g.drawImage(block, col*board.getBlockSize() + x*board.getBlockSize(), row*board.getBlockSize() + y*board.getBlockSize(), null);
				}
			}
		}
	}

	private void deleteLine() {//Removes a line when it is completely filled with blocks
		int height = board.getBoard().length - 1;
		for (int i = height; i > 0; i--) {
			int count = 0;
			for (int j = 0; j < board.getBoard()[0].length; j++) {
				if (board.getBoard()[i][j] != 0) {
					count++;
				}
				board.getBoard()[height][j] = board.getBoard()[i][j];
			}
			if (count < board.getBoard()[0].length) {
				height--;
			}
		}
	}

//Getters and setters	
	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}

	public void normalSpeed() {
		currentSpeed = normalSpeed;
	}
	public void dropSpeed() {
		currentSpeed = dropSpeed;
	}

	public BufferedImage getBlock() {
		return block;
	}

	public int[][] getCoordinates() {
		return coordinates;
	}

	public int getColour() {
		return colour;
	}

}

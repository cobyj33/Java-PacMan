package pacman.entities.ghosts;

import java.awt.Color;

import pacman.PacMan;
import pacman.entities.Entity;

public class Inky extends Ghost {
	
	public Inky(PacMan game, int row, int col) {
		super(game, row, col);
		color = Color.cyan;
		originalColor = Color.cyan;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void scatter() {
		targetRow = game.getBoard().length - 1;
		targetCol = game.getBoard()[targetRow].length;
		super.move();
	}

	@Override
	public void chase() {
		int playerDirection = game.getPlayer().getDirection();
		int[] vector = directions[playerDirection];
		
		targetRow = game.getPlayer().getRow() + vector[1] * 2;
		targetCol = game.getPlayer().getCol() + vector[0] * 2;
		
		targetRow += game.getPlayer().getRow() - game.blinky.getRow();
		targetCol += game.getPlayer().getCol() - game.blinky.getCol();
		
//		int[] targetPos = rotate(targetRow, targetCol);
//		targetRow = targetPos[0];
//		targetCol = targetPos[1];
		
		super.move();
	}
	
//	private int[] rotate(int row, int col) {
//		int pivotRow;
//		int pivotCol;
//		if (game.blinky != null) {
//			pivotRow = game.blinky.getRow();
//			pivotCol = game.blinky.getCol();
//		} else {
//			pivotRow = game.middleRow;
//			pivotCol = game.middleCol;
//		}
//		
//		row = row - pivotRow;
//		col = col - pivotCol;
//		
//		int temp = row;
//		row = -col;
//		col = -temp;
//		
//		row += pivotRow;
//		col += pivotCol;
//		
//		return new int[] {row, col};
//	}
	
	
	
}

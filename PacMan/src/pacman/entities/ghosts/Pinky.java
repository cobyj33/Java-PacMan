package pacman.entities.ghosts;

import java.awt.Color;

import pacman.PacMan;
import pacman.entities.Entity;

public class Pinky extends Ghost {
	
	public Pinky(PacMan game, int row, int col) {
		super(game, row, col);
		color = Color.pink;
		originalColor = Color.pink;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void scatter() {
		targetRow = 2;
		targetCol = 1;
		super.move();
	}

	@Override
	public void chase() {
		int playerDirection = game.getPlayer().getDirection();
		int[] vector = directions[playerDirection];
		
		targetRow = this.getRow() + directions[playerDirection][1] * 4;
		targetCol = this.getCol() + directions[playerDirection][0] * 4;
		
		super.move();
	}
	
	
}

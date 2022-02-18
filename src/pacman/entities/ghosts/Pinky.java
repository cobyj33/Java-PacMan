package pacman.entities.ghosts;

import java.awt.Color;

import pacman.PacMan;
import pacman.entities.Direction;
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
		Direction playerDirection = game.getPlayer().getDirection();
		
		targetRow = this.getRow() + playerDirection.getYComponent() * 4;
		targetCol = this.getCol() + playerDirection.getXComponent() * 4;
		
		super.move();
	}
	
	
}

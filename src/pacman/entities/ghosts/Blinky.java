package pacman.entities.ghosts;

import java.awt.Color;

import pacman.PacMan;
import pacman.entities.Entity;

public class Blinky extends Ghost {
	
	public Blinky(PacMan game, int row, int col) {
		super(game, row, col);
		color = Color.red;
		originalColor = Color.red;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void scatter() {
		targetRow = 2;
		targetCol = game.getBoard()[targetRow].length - 2;
		super.move();
	}

	@Override
	public void chase() {
		targetRow = game.getPlayer().getRow();
		targetCol = game.getPlayer().getCol();
		super.move();
	}
}

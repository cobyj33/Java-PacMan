package pacman.entities.ghosts;

import java.awt.Color;

import pacman.PacMan;
import pacman.entities.Direction;
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
		Direction playerDirection = game.getPlayer().getDirection();
		
		targetRow = game.getPlayer().getRow() + playerDirection.getYComponent() * 2;
		targetCol = game.getPlayer().getCol() + playerDirection.getXComponent() * 2;
		
		targetRow += game.getPlayer().getRow() - game.blinky.getRow();
		targetCol += game.getPlayer().getCol() - game.blinky.getCol();
		
		super.move();
	}
	
	
}

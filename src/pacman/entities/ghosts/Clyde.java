package pacman.entities.ghosts;

import java.awt.Color;

import pacman.Main;
import pacman.PacMan;
import pacman.entities.Entity;

public class Clyde extends Ghost {
	
	public Clyde(PacMan game, int row, int col) {
		super(game, row, col);
		color = Color.orange;
		originalColor = Color.orange;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void scatter() {
		targetRow = game.getBoard().length;
		targetCol = 1;
		super.move();
		
	}

	@Override
	public void chase() {
		int distanceFromPlayer = (int) (Main.getDistance((double)this.getCol(), (double)this.getRow(), (double)game.getPlayer().getCol(), (double)game.getPlayer().getRow()));
		
		if (distanceFromPlayer >= 8) {
			targetRow = game.getPlayer().getRow();
			targetCol = game.getPlayer().getCol();
			super.move();
		} else {
			scatter();
		}
		
	}
	
}

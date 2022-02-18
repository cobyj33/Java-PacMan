package pacman.entities;

import java.util.Arrays;
import java.util.Optional;

public enum Direction {
	UP(0, -1, 1),
	LEFT(-1, 0, 2),
	DOWN(0, 1, 3),
	RIGHT(1, 0, 4),
	NONE(0, 0, 0);
	
	private int x, y, priority;
	//less priority is better
	
	Direction(int x, int y, int priority) {
		this.x = x;
		this.y = y;
		this.priority = priority;
	}
	
	public int getXComponent() {
		return x;
	}
	
	public int getYComponent() {
		return y;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public Direction opposite() {
		return Direction.getDirectionFromValue(-this.getXComponent(), -this.getYComponent());
	}
	
	public static Direction[] generateView(Direction direction) {
		Direction[] leftAndRight = new Direction[3];
		leftAndRight[0] = getDirectionFromValue(direction.getYComponent(), -direction.getXComponent());
		leftAndRight[1] = direction;
		leftAndRight[2] = getDirectionFromValue(-direction.getYComponent(), direction.getXComponent());
		return leftAndRight;
	}
	
	public static Direction getDirectionFromValue(int x, int y) {
		Optional<Direction> selected = Arrays.stream(values()).filter(d -> d.getXComponent() == x && d.getYComponent() == y).findFirst();
		if (selected.isEmpty()) {
			System.out.println("[Direction.getDirectionFromValue()] could not find valid direction");
			return NONE;
		} else {
			return selected.get();
		}
	}
	
	public static Direction getRandom() {
		return values()[ (int) (Math.random() * (values().length - 1))];
	}
	
}

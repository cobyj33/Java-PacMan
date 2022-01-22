package screens;

import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pacman.PacMan;

public class ScreenManager {
	JPanel displayPanel;
	public static final int TITLESCREEN = 0,
			GAMESCREEN = 1,
			DEATHSCREEN = 2,
			PAUSESCREEN = 3;
	
	HashMap<Integer, JPanel> screens;
	int currentScreen;
	
	public ScreenManager(JPanel displayPanel) {
		screens = new HashMap<>();
		this.displayPanel = displayPanel;
		
		screens.put(TITLESCREEN, new TitleScreen(this));
		screens.put(GAMESCREEN, new PacMan(this));
		screens.put(PAUSESCREEN, new PauseScreen(this));
		screens.put(DEATHSCREEN, new DeathScreen(this));
		
		//starting screen
		currentScreen = TITLESCREEN;
		switchScreen(currentScreen);
		
	}
	
	public void addScreen(int id, JPanel screen) {
		screen.setPreferredSize(displayPanel.getSize());
		screens.put(id, screen);
	}
	
	public void switchScreen(int screenID) {
		SwingUtilities.invokeLater( () -> {
			
		if (screens.containsKey(screenID)) {
			
			System.out.println("Switching screens");
			displayPanel.remove(screens.get(currentScreen));
			JPanel nextScreen = screens.get(screenID);
			nextScreen.setPreferredSize(displayPanel.getSize());
			
			displayPanel.add(screens.get(screenID));
			displayPanel.revalidate(); displayPanel.repaint();
			currentScreen = screenID;
			
			
		} else {
			System.out.println("[ScreenManager]: Invalid Screen");
		}
			
		});
	}	
}
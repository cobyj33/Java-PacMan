package screens;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pacman.Main;
import pacman.PacMan;
import utilities.Scheduler;

public class ScreenManager {
	JPanel displayPanel;
	public static final int TITLESCREEN = 0,
			GAMESCREEN = 1,
			DEATHSCREEN = 2,
			PAUSESCREEN = 3;
	
	boolean switching;
	
	HashMap<Integer, JPanel> screens;
	int currentScreen;
	
	public ScreenManager(JPanel displayPanel) {
		screens = new HashMap<>();
		this.displayPanel = displayPanel;
		
		PacMan game = new PacMan(this);
		screens.put(TITLESCREEN, new TitleScreen(this));
		screens.put(GAMESCREEN, game);
		screens.put(PAUSESCREEN, new PauseScreen(this));
		screens.put(DEATHSCREEN, new DeathScreen(this, game));
		
		//starting screen
		currentScreen = TITLESCREEN;
		switchScreen(currentScreen);
		
	}
	
	public void addScreen(int id, JPanel screen) {
		screen.setPreferredSize(displayPanel.getSize());
		screens.put(id, screen);
	}
	
	public void switchScreen(int screenID) {
		if (switching) return;
		switching = true;
		Runnable screenSwitch = () -> {
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
			switching = false;
		};
		
		screenSwitch.run();
		
//		Transition transition = new Transition();
//		transition.setSize(displayPanel.getSize());
//		transition.getTransitionTime();
//		Main.addToLayeredPane(transition, Integer.valueOf(200));
//		transition.start();
//		Scheduler.schedule( () -> Main.removeFromLayeredPane(transition), transition.getTransitionTime(), TimeUnit.MILLISECONDS);
//		Scheduler.schedule(screenSwitch, transition.getScreenSwitchTime(), TimeUnit.MILLISECONDS);
	}
}
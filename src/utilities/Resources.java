package utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Resources {
	private static HashMap<Integer, Clip> SoundMap = new HashMap<>();
	public static boolean initialized = false;
	private static boolean muted = false;
	
	public static final int CHOMPSOUND = 0,
			DEATHSOUND = 1,
			EATGHOST = 2,
			EATFRUIT = 3,
			ONE_UP = 4,
			INTERMISSION = 5,
			INTROMUSIC = 6,
			SIREN = 7,
			POWERPELLET = 8,
			RETREATING = 9;
			
	
	public static void init() {
		if (!initialized) {
			initialized = true;
			
			try {
				Clip introMusic = AudioSystem.getClip();
				introMusic.open(AudioSystem.getAudioInputStream(new File("res/Sounds/game_start.wav")));
				SoundMap.put(INTROMUSIC, introMusic);
				
				Clip chompSound = AudioSystem.getClip();
				chompSound.open(AudioSystem.getAudioInputStream(new File("res/Sounds/munch_1.wav")));
				SoundMap.put(CHOMPSOUND, chompSound);
				
				Clip deathSound = AudioSystem.getClip();
				deathSound.open(AudioSystem.getAudioInputStream(new File("res/Sounds/death_1.wav")));
				SoundMap.put(DEATHSOUND, deathSound);
				
				Clip eatFruit = AudioSystem.getClip();
				eatFruit.open(AudioSystem.getAudioInputStream(new File("res/Sounds/eat_fruit.wav")));
				SoundMap.put(EATFRUIT, eatFruit);
				
				Clip eatGhost = AudioSystem.getClip();
				eatGhost.open(AudioSystem.getAudioInputStream(new File("res/Sounds/eat_ghost.wav")));
				SoundMap.put(EATGHOST, eatGhost);
				
				Clip oneUp = AudioSystem.getClip();
				oneUp.open(AudioSystem.getAudioInputStream(new File("res/Sounds/pacman_extrapac.wav")));
				SoundMap.put(ONE_UP, oneUp);
				
				Clip intermission = AudioSystem.getClip();
				intermission.open(AudioSystem.getAudioInputStream(new File("res/Sounds/intermission.wav")));
				SoundMap.put(INTERMISSION, intermission);
				
				Clip siren = AudioSystem.getClip();
				siren.open(AudioSystem.getAudioInputStream(new File("res/Sounds/siren_1.wav")));
				SoundMap.put(SIREN, siren);
				
				Clip powerPellet = AudioSystem.getClip();
				powerPellet.open(AudioSystem.getAudioInputStream(new File("res/Sounds/power_pellet.wav")));
				SoundMap.put(POWERPELLET, powerPellet);
				
				Clip retreating = AudioSystem.getClip();
				retreating.open(AudioSystem.getAudioInputStream(new File("res/Sounds/retreating.wav")));
				SoundMap.put(RETREATING, retreating);
				
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				System.out.println("Could not load all sound files");
				e.printStackTrace();
			}
			
		}
	}
	
	
	public static void playSound(int choice) {
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			if (muted) { return; }
			current.setMicrosecondPosition(0);
			current.start();
		} else {
			System.out.println("[Resources.playSound] Invalid choice");
		}
	}
	
	public static int getSoundLength(int choice) {
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			return (int) (current.getMicrosecondLength() / 1000.);
		} else {
			System.out.println("[Resources.getSoundLength] Invalid choice");
			return -1;
		}
	}
	
//	public static void loopSoundUntil(int choice, Predicate<?> requirement) {
//		if (SoundMap.containsKey(choice)) {
//			Clip current = SoundMap.get(choice);
//			
//			
//		} else {
//			System.out.println("[Resources.loopSoundUntil] Invalid choice");
//		}
//	}
	
	public static void playIfFinished(int choice) {
		if (muted) return;
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			if (!current.isActive()) {
				playSound(choice);
			}
		} else {
			System.out.println("[Resources.playIfFinished] Invalid choice");
		}
	}
	
	public static void stopSound(int choice) {
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			current.stop();
		} else {
			System.out.println("[Resources.stopSound] Invalid choice");
		}
	}
	
	public static void mute() {
		if (!muted) {
			muted = true;
			SoundMap.values().forEach(clip -> {
				if (clip == null) { return; }
				clip.stop(); 
				});
		} else {
			muted = false;
			System.out.println("restarting");
		}
	}
	
	public static void loopSoundFor(int choice, int time) {
		if (muted) return;
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			current.loop( (int) (time / (current.getMicrosecondLength() / 1000.)) + 1 );
			utilities.Scheduler.schedule(() -> current.stop(), time, TimeUnit.MILLISECONDS);
		} else {
			System.out.println("[Resources.loopSoundFor] Invalid choice");
		}
	}
	
	public static boolean isMuted() { 
		return muted;
	}
}

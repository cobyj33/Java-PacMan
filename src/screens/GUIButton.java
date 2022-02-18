package screens;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class GUIButton extends JButton {
	Mouse mouse;
	Color background;
	
	GUIButton(String text) {
		setFont(getFont().deriveFont(12f));
		setText(text);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setFocusable(false);
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
		
		mouse = new Mouse();
		
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
	}
	
	class Mouse extends MouseAdapter {
		Mouse() {}
		public void mouseEntered(MouseEvent e) {
			setContentAreaFilled(true);
			setBackground(Color.WHITE);
			setForeground(Color.BLACK);
		}
		
		public void mouseExited(MouseEvent e) {
			setContentAreaFilled(false);
			setBackground(Color.BLACK);
			setForeground(Color.WHITE);
		}
	}
	
}

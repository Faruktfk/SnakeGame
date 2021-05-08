package snakeGame;


import javax.swing.JFrame;

public class SGameFrame {
	SGamePanel panel = new SGamePanel();
	
	public SGameFrame() {
		
		JFrame window = new JFrame();
		window.setTitle("Snake Game");
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(panel); 
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new SGameFrame();
	}
}

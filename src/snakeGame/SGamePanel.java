package snakeGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SGamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 1300;
	static final int SCREEN_HEIGHT = 700;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
	static int DELAY;
	int[] x;
	int[] y;
	int bodyParts;
	int applesEaten;
	int appleX;
	int appleY;
	char direction;
	boolean running = false;
	Timer timer;
	Random random;
	
	long lastPressedTime;

	public SGamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		x = new int[GAME_UNITS];
		y = new int[GAME_UNITS];
		bodyParts = 6;
		applesEaten = 0;
		direction = 'R';
		newApple();
		running = true;
		DELAY = 80;
		timer = new Timer(DELAY, this);
		timer.start();
		lastPressedTime = System.currentTimeMillis();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		if (running) {

			for (int i = 0; i < SCREEN_WIDTH; i += UNIT_SIZE) {
				g.drawLine(i, 0, i, SCREEN_HEIGHT);
			}
			for (int i = 0; i < SCREEN_HEIGHT; i += UNIT_SIZE) {
				g.drawLine(0, i, SCREEN_WIDTH, i);
			}

			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					
					if (applesEaten % 10 == 0 && applesEaten != 0) {
						g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));

					}
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}

			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

		for (int i = 0; i < bodyParts; i++) {
			if (x[i] == appleX && y[i] == appleY) {
				newApple();
			}
		}
		if (appleY == SCREEN_HEIGHT - UNIT_SIZE || appleY == 0 || appleX == SCREEN_WIDTH - UNIT_SIZE || appleX == 0) {
			newApple();
		}
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];

		}

		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}

	public void checkApple() {
		if (appleX == x[0] && appleY == y[0]) {
			bodyParts++;
			applesEaten++;
			newApple();
			if (applesEaten % 10 == 0 && applesEaten != 0 && DELAY - 5 >= 0) {
				DELAY -= 5;
				timer.setDelay(DELAY);
			}
		}
	}

	public void checkCollisions() {
		// if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && y[0] == y[i]) {
				running = false;
			}
		}
		// if head touches left border
		if (x[0] < 0) {
//			x[0] = SCREEN_WIDTH;
			running = false;
		}

		// if head touches right border
		if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
//			x[0] = 0;
			running = false;
		}
		// if head touches top border
		if (y[0] < 0) {
//			y[0] = SCREEN_HEIGHT;
			running = false;
		}
		// if head touches bottom border
		if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
//			y[0] = 0;
			running = false;
		}

		if (!running) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {
		running = true;

		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());

		// ####################################
		JButton plyAgain = new JButton("Play Again");
		plyAgain.setFont(new Font("Ink Free", Font.BOLD, 40));
		plyAgain.setBackground(Color.black);
		plyAgain.setForeground(Color.white);
		plyAgain.setFocusable(false);
		plyAgain.setBounds(SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 + 100, 300, 100);
		plyAgain.setVisible(true);
		this.add(plyAgain);

		plyAgain.addActionListener(e -> {
			g.dispose();

			plyAgain.setVisible(false);
			paint(g);
			startGame();
			repaint();
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);

			long current = System.currentTimeMillis();
			if (current - lastPressedTime > 70) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
					if (direction != 'U') {
						direction = 'D';
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
					if (direction != 'D') {
						direction = 'U';
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
					if (direction != 'L') {
						direction = 'R';
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					if (direction != 'R') {
						direction = 'L';
					}
				}

				lastPressedTime = current;
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}

		}
	}
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakePyTranslation {
    static final int SQUARE_SIZE = 25;
    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    static final int BOMB_RANGE = 75; // 3 blocks × 25 pixels

    static JFrame frame;
    static JPanel panel;
    static JButton skinsButton;
    static JButton backButton;
    static ArrayList<Rectangle> snakeBody = new ArrayList<>();
    static Rectangle snakeHead;
    static Color snakeColor = Color.GREEN;
    static Rectangle fruit;
    static int snakeMoveX = 0;
    static int snakeMoveY = 0;
    static String snakeDirection = "none";
    static int score = 0;
    static Timer gameTimer;
    static Random random = new Random();
    static long bombSpawnTime;
    static boolean bombsEnabled = true;

    public static void main(String[] args) {
        frame = new JFrame("Snakegame");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        startMenu();
    }

    static void startMenu() {
        panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Background
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                // Title
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 28));
                g.drawString("SNAKE GAME", 110, 100);
                // Prompt
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Select a button to continue", 90, 140);
            }
        };

        panel.setBackground(Color.BLACK);
        panel.setFocusable(true);

        JButton playButton = new JButton("Play");
        playButton.setBounds(125, 180, 150, 40);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel);
                startGame();
            }
        });

        JButton skinButton = new JButton("Skins");
        skinButton.setBounds(125, 235, 150, 40);
        skinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel);
                skinsMenu();
            }
        });

        JButton settingsButton = new JButton("Settings");
        settingsButton.setBounds(125, 290, 150, 40);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel);
                showSettingsMenu();
            }
        });

        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(125, 345, 150, 40);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(playButton);
        panel.add(skinButton);
        panel.add(settingsButton);
        panel.add(quitButton);

        frame.remove(frame);
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }

    static void skinsMenu(){
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.setColor(Color.WHITE);
                g.drawRect(100, 65, 190, 50);
                g.fillRect(100, 65, 190, 50);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("SKINS MENU", 120, 100);
            }
        };

        panel.setBackground(Color.BLACK);
        panel.setFocusable(true);
        panel.setLayout(null);

        backButton = new JButton("Back");
        backButton.setBounds(160, 300, 80, 30);
        panel.add(backButton);
        backButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                frame.remove(panel);
                startMenu();
            }
        });

        JButton blueButton = new JButton("Blue");
        blueButton.setBounds(70, 180, 80, 30);
        panel.add(blueButton);
        blueButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.BLUE;
                panel.setBackground(Color.BLUE);
            }
        });

        JButton redButton = new JButton("Red");
        redButton.setBounds(160, 180, 80, 30);
        panel.add(redButton);
        redButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.RED;
                panel.setBackground(Color.RED);
            }
        });

        JButton greenButton = new JButton("Green");
        greenButton.setBounds(250, 180, 80, 30);
        panel.add(greenButton);
        greenButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.GREEN;
                panel.setBackground(Color.GREEN);
            }
        });

        JButton orangeButton = new JButton("Orange");
        orangeButton.setBounds(115, 220, 80, 30);
        panel.add(orangeButton);
        orangeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.ORANGE;
                panel.setBackground(Color.ORANGE);
            }
        });

        JButton yellowButton = new JButton("Yellow");
        yellowButton.setBounds(205, 220, 80, 30);
        panel.add(yellowButton);
        yellowButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                snakeColor = Color.YELLOW;
                panel.setBackground(Color.YELLOW);
            }
        });     

        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }

    static void showSettingsMenu() {
        panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("SETTINGS", 120, 80);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Bombs: " + (bombsEnabled ? "ON" : "OFF"), 130, 140);
            }
        };

        panel.setBackground(Color.BLACK);
        panel.setFocusable(true);

        JButton toggleBombsButton = new JButton(bombsEnabled ? "Bombs OFF" : "Bombs ON");
        toggleBombsButton.setBounds(125, 170, 150, 40);
        toggleBombsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bombsEnabled = !bombsEnabled;
                frame.getContentPane().removeAll();
                showSettingsMenu();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setBounds(125, 240, 150, 40);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                startMenu();
            }
        });

        panel.add(toggleBombsButton);
        panel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }

    static void gameOverScreen(String message) {
        if (gameTimer != null) gameTimer.stop();

        panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Background
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                // Border
                g.setColor(Color.RED);
                g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
                // Game Over text
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("GAME OVER", 110, 120);
                // Reason
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString(message, 85, 160);
                // Score
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Final Score: " + score, 130, 190);
            }
        };

        panel.setBackground(Color.BLACK);
        panel.setFocusable(true);

        JButton retryButton = new JButton("Play Again");
        retryButton.setBounds(125, 230, 150, 40);
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                snakeBody.clear();
                score = 0;
                snakeMoveX = 0;
                snakeMoveY = 0;
                snakeDirection = "none";
                startGame();
            }
        });

        JButton menuButton = new JButton("Main Menu");
        menuButton.setBounds(125, 285, 150, 40);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                startMenu();
            }
        });

        panel.add(retryButton);
        panel.add(menuButton);

        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }

    static void startGame() {
        ArrayList<Rectangle> bodyRects = new ArrayList<>();
        int[] headPos = {0, 0};
        int[] fruitPos = spawnFruit(bodyRects, headPos);
        int[] bombPos = spawnBomb(bodyRects, headPos);
        bombSpawnTime = System.currentTimeMillis();
        score = 0;

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Checkerboard
                for (int row = 0; row < 16; row++) {
                    for (int col = 0; col < 16; col++) {
                        if ((row + col) % 2 == 0) g.setColor(Color.GRAY);
                        else g.setColor(Color.BLACK);
                        g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    }
                }
                // Snake body
                g.setColor(snakeColor);
                for (Rectangle r : bodyRects) {
                    g.fillRect(r.x, r.y, SQUARE_SIZE, SQUARE_SIZE);
                }
                // Snake head
                g.setColor(snakeColor);
                g.fillRect(headPos[0], headPos[1], SQUARE_SIZE, SQUARE_SIZE);
                // Fruit
                g.setColor(Color.RED);
                g.fillRect(fruitPos[0], fruitPos[1], SQUARE_SIZE, SQUARE_SIZE);
                // Bomb
                if (bombsEnabled) {
                    g.setColor(Color.ORANGE);
                    g.drawOval(bombPos[0], bombPos[1], SQUARE_SIZE, SQUARE_SIZE);
                }
                // Score
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Score: " + score, 160, 395);
            }
        };

        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W && !snakeDirection.equals("down")) {
                    snakeMoveX = 0; snakeMoveY = -SQUARE_SIZE; snakeDirection = "up";
                } else if (e.getKeyCode() == KeyEvent.VK_A && !snakeDirection.equals("right")) {
                    snakeMoveX = -SQUARE_SIZE; snakeMoveY = 0; snakeDirection = "left";
                } else if (e.getKeyCode() == KeyEvent.VK_D && !snakeDirection.equals("left")) {
                    snakeMoveX = SQUARE_SIZE; snakeMoveY = 0; snakeDirection = "right";
                } else if (e.getKeyCode() == KeyEvent.VK_S && !snakeDirection.equals("up")) {
                    snakeMoveX = 0; snakeMoveY = SQUARE_SIZE; snakeDirection = "down";
                }
            }
        });

        gameTimer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snakeDirection.equals("none")) return;

                // Save old head position as tail
                bodyRects.add(new Rectangle(headPos[0], headPos[1], SQUARE_SIZE, SQUARE_SIZE));

                // Move head
                headPos[0] += snakeMoveX;
                headPos[1] += snakeMoveY;

                // Wall detection
                if (headPos[0] < 0 || headPos[0] >= WIDTH || headPos[1] < 0 || headPos[1] >= HEIGHT) {
                    frame.remove(panel);
                    gameOverScreen("Game Over: Crash");
                    return;
                }

                // Self collision
                for (int i = 0; i < bodyRects.size() - 1; i++) {
                    Rectangle tail = bodyRects.get(i);
                    if (tail.x == headPos[0] && tail.y == headPos[1]) {
                        frame.remove(panel);
                        gameOverScreen("Game Over: Tail Bite");
                        return;
                    }
                }

                // Fruit eating
                if (headPos[0] == fruitPos[0] && headPos[1] == fruitPos[1]) {
                    score++;
                    int[] newFruit = spawnFruit(bodyRects, headPos);
                    fruitPos[0] = newFruit[0];
                    fruitPos[1] = newFruit[1];
                } else {
                    if (!bodyRects.isEmpty()) bodyRects.remove(0);
                }

                // Bomb collision (3 block range)
                if (bombsEnabled) {
                    int dx = headPos[0] - bombPos[0];
                    int dy = headPos[1] - bombPos[1];
                    if (Math.sqrt(dx*dx + dy*dy) <= BOMB_RANGE) {
                        frame.getContentPane().removeAll();
                        gameOverScreen("Game Over: Hit Bomb");
                        return;
                    }
                }

                // Bomb explodes after 5 seconds
                if (System.currentTimeMillis() - bombSpawnTime >= 5000) {
                    int[] newBomb = spawnBomb(bodyRects, headPos);
                    bombPos[0] = newBomb[0];
                    bombPos[1] = newBomb[1];
                    bombSpawnTime = System.currentTimeMillis();
                }

                panel.repaint();
            }
        });

        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        panel.requestFocusInWindow();
        gameTimer.start();
    }

    static int[] spawnFruit(ArrayList<Rectangle> bodyRects, int[] headPos) {
        int fx, fy;
        while (true) {
            fx = random.nextInt(16) * SQUARE_SIZE;
            fy = random.nextInt(16) * SQUARE_SIZE;
            boolean onSnake = false;
            if (fx == headPos[0] && fy == headPos[1]) onSnake = true;
            for (Rectangle r : bodyRects) {
                if (r.x == fx && r.y == fy) onSnake = true;
            }
            if (!onSnake) break;
        }
        return new int[]{fx, fy};
    }

    static int[] spawnBomb(ArrayList<Rectangle> bodyRects, int[] headPos) {
        int bx, by;
        while (true) {
            bx = random.nextInt(16) * SQUARE_SIZE;
            by = random.nextInt(16) * SQUARE_SIZE;
            boolean onSnake = false;
            if (bx == headPos[0] && by == headPos[1]) onSnake = true;
            for (Rectangle r : bodyRects) {
                if (r.x == bx && r.y == by) onSnake = true;
            }
            if (!onSnake) break;
        }
        return new int[]{bx, by};
    }
}
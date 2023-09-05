import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;


class MazeGame extends Frame implements KeyListener {
    private static final int MAZE_SIZE = 10;
    int result =0;
    private static final int CELL_SIZE = 50;

    int store_x;
    int store_y;
    private int ballX;
    private int ballY;

    private Position head;
    private Position tail;
    int score=0;
    private Label scoreLabel;
    private Label highscoreLabel;
    int level = 0;

    Stack<Integer> scorestack= new Stack<>();
    PriorityQueue<Integer> highscores= new PriorityQueue<>(Comparator.reverseOrder());;

    int[][] maze1 = {
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 1, 1, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 1, 1, 1, 0, 0, 0, 0, 0}
    };
    int[][] maze2 = {
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 1, 1, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 1, 1, 1, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 1, 1, 0, 0},
            {0, 1, 1, 1, 1, 0, 0, 1, 1, 0}
    };
    int[][] maze3 = {
            {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 0, 0, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 1, 1, 0, 0, 0, 0, 1, 0},
            {1, 0, 0, 1, 1, 1, 1, 0, 1, 0},
            {1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 0}
    };


    public MazeGame() {
        setSize(MAZE_SIZE * CELL_SIZE, MAZE_SIZE * CELL_SIZE);
        setTitle("Maze Game");
        setResizable(false);
        setVisible(true);
        addKeyListener(this);
        head = null;
        tail = null;
        String hexColor1 = "7a9b7f";

        int rgb1 = Color.decode("#" + hexColor1).getRGB();
        Color label = new Color(rgb1);
        // Convert RGB to HSB
        float[] hsb1 = new float[3];
        Color.RGBtoHSB((rgb1 >> 16) & 0xFF, (rgb1 >> 8) & 0xFF, rgb1 & 0xFF, hsb1);

        Panel scorePanel = new Panel();
        scorePanel.setBackground(label);
        scoreLabel = new Label("SCORE: " + score, Label.CENTER);
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        scorePanel.add(scoreLabel);

        highscoreLabel = new Label("HIGHSCORE: " + highscores.peek(), Label.RIGHT);
        highscoreLabel.setForeground(Color.YELLOW);
        highscoreLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        scorePanel.add(highscoreLabel);

        add(scorePanel, BorderLayout.NORTH);

        initializeGame();
    }


    private void initializeGame() {
        ballX = 0;
        ballY = 0;
        highscores.offer(score);
        updateHighScoreLabel();
        if(level==0) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);

            JLabel message = new JLabel("<html><body style='width: 200px; text-align: center;'>Game Start!<br><br>Rules:<br>- Try to finish the level in one try.<br>- If you choose the same path again, your score will be deducted.</body></html>");
            message.setFont(new Font("Arial", Font.PLAIN, 14));
            message.setForeground(Color.BLACK);

            panel.add(message);

            JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog(null, "Game Start");

            dialog.getContentPane().setBackground(Color.WHITE);

            dialog.setVisible(true);
        }
        level++;
        clearLinkedList();
        repaint();
    }
    private void updateHighScoreLabel() {

        if (!highscores.isEmpty()) {
            int highestScore = highscores.peek();
            highscoreLabel.setText("HIGH SCORE: " + highestScore);
        } else {
            highscores.peek();
            highscoreLabel.setText("HIGH SCORE: -");
        }
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (level == 3) {
            drawMaze(maze3, g, 'P');
            drawFinishPoint(g, 'F');
            drawBall(g, 'G');
        } else if (level == 2) {
            drawMaze(maze2, g, 'G');
            drawFinishPoint(g, 'F');
            drawBall(g, 'B');
        } else {
            drawMaze(maze1, g, 'B');
            drawFinishPoint(g, 'F');
            drawBall(g, 'P');
        }
    }


    private void drawMaze(int[][] maze, Graphics g, char colour) {
        String hexColor = "aac4a3";
        int rgb = Color.decode("#" + hexColor).getRGB();
        float[] hsb = new float[3];
        Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, hsb);
        Color pastelColor = new Color(rgb);

        g.setColor(pastelColor);
        for (int row = 0; row < MAZE_SIZE; row++) {
            for (int col = 0; col < MAZE_SIZE; col++) {
                if (maze[row][col] == 1) {
                    g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawBall(Graphics g, char c) {
        if (c == 'B')
            g.setColor(Color.BLUE);
        else if (c == 'G')
            g.setColor(Color.DARK_GRAY);
        else
            g.setColor(Color.BLACK);

        g.fillOval(ballX * CELL_SIZE, ballY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
    private boolean isValidMove(int x, int y) {
        int[][] maze=null;
        if (level==1)
            maze = maze1;
        else if (level==2)
            maze =maze2;
        else if (level==3)
            maze =maze3;

        return (x >= 0 && x < MAZE_SIZE && y >= 0 && y < MAZE_SIZE && maze[y][x] != 1);
    }


    private void moveBall(int dx, int dy) {
        boolean check;
        int newBallX = ballX + dx;
        int newBallY = ballY + dy;

        if (isValidMove(newBallX, newBallY)) {
            Position newBallPosition = new Position(newBallX, newBallY);

            Position prevNode = tail;
            boolean foundMatch = false;

            while (prevNode != null && prevNode.prev != null) {
                if (prevNode.prev.x == newBallX && prevNode.prev.y == newBallY) {
                    foundMatch = true;
                    break;
                }
                prevNode = prevNode.prev;
            }

            if (foundMatch && !(newBallX == 0 && newBallY == 0)) {
                score -= 10;
                Position prevPosition = prevNode.prev;
                prevNode.prev = prevPosition.prev;

                if (prevNode.prev != null) {
                    prevNode.prev.next = prevNode;
                } else {
                    head = prevNode;
                }
            } else {
                Position prevBallPosition = new Position(ballX, ballY);

                if (head == null) {
                    head = prevBallPosition;
                    tail = prevBallPosition;
                } else {
                    tail.next = prevBallPosition;
                    prevBallPosition.prev = tail;
                    tail = prevBallPosition;
                }

                score += 10;
            }

            ballX = newBallX;
            ballY = newBallY;
            scoreLabel.setText("SCORE: " + score);

            store_x = newBallX;
            store_y = newBallY;
            repaint();

            if (ballX == MAZE_SIZE - 1 && ballY == MAZE_SIZE - 1) {
                scorestack.push(score);
                showLevelCompletedMessage();

                if (level == 3) {
                    showGameCompletedMessage();
                    System.exit(0);
                } else {
                    initializeGame();
                }
            }
        } else {
            showgameover();
        }
    }
    private void clearLinkedList() {
        head = null;
        tail = null;
    }

    private void showLevelCompletedMessage() {
        score=0;
        scoreLabel. setText("SCORE: " + score);
        String message = "Level " + level + " completed!";
        JOptionPane.showMessageDialog(this, message, "Level Completed", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showgameover() {

        int result = highscores.peek();

        scorestack.clear(); // Clear the scorestack

        highscores.offer(result);
        updateHighScoreLabel();

        String message = "GAME OVER! Score: " + result;
        JOptionPane.showMessageDialog(this, message, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
        level = 0;
        score=0;
        scoreLabel. setText("SCORE: " + score);
        clearLinkedList();
        initializeGame();
    }


    private void showGameCompletedMessage() {
        while (!scorestack.isEmpty()) {
            result += scorestack.pop();
        }

        highscores.offer(result);
        updateHighScoreLabel();

        String message = "Congratulations! You have completed all levels! Score: " + result;
        JOptionPane.showMessageDialog(this, message, "Game Completed", JOptionPane.INFORMATION_MESSAGE);
    }
    private void drawFinishPoint(Graphics g, char colour) {
        int finishX = MAZE_SIZE - 1;
        int finishY = MAZE_SIZE - 1;

        g.setColor(Color.RED);
        g.fillRect(finishX * CELL_SIZE, finishY * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        if (colour == 'F')
            g.setColor(Color.WHITE);
        else
            g.setColor(Color.BLACK);

        g.drawRect(finishX * CELL_SIZE, finishY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                moveBall(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                moveBall(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                moveBall(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                moveBall(1, 0);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        new MazeGame();
    }
}
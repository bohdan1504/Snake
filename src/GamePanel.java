import com.sun.source.tree.IfTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = SCREEN_WIDTH*SCREEN_HEIGHT/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int body_parts = 6;
    int apples_eaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JLabel scoreLabel;

    GamePanel (){

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Ink Free", Font.BOLD, 50));
        scoreLabel.setBounds(250,0,100,50);

        random = new Random();

//        this.add(scoreLabel);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(25,0,0));
//        this.setOpaque(false);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }



    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){


        if(running) {
            // GRID
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.setColor(Color.gray);
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < body_parts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
//                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Score text on top of the page
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(
                    "Score: " + apples_eaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + apples_eaten))/2,
                    g.getFont().getSize()
            );

        }
        else {
            gameOver(g);
        }

    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    public void move(){
        for (int i=body_parts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U': y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D': y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L': x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R': x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple(){
        if (x[0]==appleX && y[0]==appleY){
            body_parts++;
            newApple();
            apples_eaten++;
            scoreLabel.setText("Score: " + apples_eaten);

        }
    }

    public void checkCollisions(){
        // checks if head collides with body
        for(int i=body_parts; i>0; i--){
            if ((x[0]==x[i]) && (y[0]==y[i])){
                running = false;
            }
        }
        // checks if head touches left border
        if (x[0] < 0){
            running = false;}
        // checks if head touches right border
        if (x[0] > SCREEN_WIDTH){
            running = false;}
        // checks if head touches top border
        if (y[0] < 0){
            running = false;}
        // checks if head touches top border
        if (y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if (!running){
            timer.stop();}
    }

    public void gameOver (Graphics g){
        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(
                "Game Over",
                (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2,
                SCREEN_HEIGHT/2
        );
        // Score text on top of the page
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(
                "Score: " + apples_eaten,
                (SCREEN_WIDTH - metrics2.stringWidth("Score: " + apples_eaten))/2,
                g.getFont().getSize()
        );

        Timer pause = new Timer(2000, e -> {
            new GameFrame();
        });
        pause.setRepeats(false);
        pause.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    Timer littlePause1 = new Timer(80, e1 -> {
                        if (direction != 'R'){
                            direction = 'L';
                        }
                    });
                    littlePause1.setRepeats(false);
                    littlePause1.start();
                    break;
                case KeyEvent.VK_RIGHT:
                    Timer littlePause2 = new Timer(80, e1 -> {
                        if (direction != 'L') {
                            direction = 'R';
                        }
                    });
                    littlePause2.setRepeats(false);
                    littlePause2.start();
                    break;
                case KeyEvent.VK_UP:
                    Timer littlePause3 = new Timer(80, e1 -> {
                        if (direction != 'D') {
                            direction = 'U';
                        }
                    });
                    littlePause3.setRepeats(false);
                    littlePause3.start();
                    break;
                case KeyEvent.VK_DOWN:
                    Timer littlePause4 = new Timer(80, e1 -> {
                        if (direction != 'U') {
                            direction = 'D';
                        }
                    });
                    littlePause4.setRepeats(false);
                    littlePause4.start();
                    break;
            }
        }
    }
}

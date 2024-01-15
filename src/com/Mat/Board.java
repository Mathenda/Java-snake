package com.Mat;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    private final int B_WIDTH = 300;    //width of the board
    private final int B_HEIGHT = 300;   //height of the board
    private final int DOT_SIZE = 10;    //size of apple and dot of the snake
    private final int ALL_DOTS = 900;   //maximum number of possible dots on the board

    private final int[] x = new int[ALL_DOTS];  //store x coordinates of all joints of a snake
    private final int[] y = new int[ALL_DOTS];  //store y coordinates of all joints of a snake

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;



    private Timer timer;
    private Image ball;
    private Image head;
    private Image apple;

    public Board(){
        initBoard();
    }

    //initialize game board
    public void initBoard(){
        addKeyListener(new TAdapter()); //adds key listener
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }


    //ImageIcon class used for displaying PNG images
    private void loadImages(){
        ImageIcon iid = new ImageIcon("src/Resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/Resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/Resources/head.png");
        head = iih.getImage();
    }
// randomly place apple on map
    private void locateApple(){
        //calculate random position for apple
        int RAND_POS = 29;
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r*DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r*DOT_SIZE));
    }

    //create snake, randomly locate an apple on the board, start the timer
    private void initGame(){
        dots = 3;
        for (int z = 0; z < dots; z++){
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        locateApple();
        //determine the speed of the game
        int DELAY = 140;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //if the apple collides with the head, increase the number of joints of the snake
    private void checkApple(){
        if ((x[0] == apple_x) && (y[0] == apple_y)){
            dots++;
            locateApple();
        }
    }

    //update position by copying the coordinates of the previous dot to the current dot
    private void move(){
        for(int z = dots; z > 0; z--){
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }

        if(leftDirection){
            x[0] -= DOT_SIZE;
        }
        if(rightDirection){
            x[0] += DOT_SIZE;
        }
        if(upDirection){
            y[0] -= DOT_SIZE;
        }
        if(downDirection){
            y[0] += DOT_SIZE;
        }
    }

    //check if the snake has hit itself or the walls
    private void checkCollision(){
        for(int z = dots; z > 0; z--){
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                break;
            }
        }
        if(y[0] >= B_HEIGHT){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
        if(x[0] >= B_WIDTH){
            inGame = false;
        }
        if(x[0] < 0){
            inGame = false;
        }
        if(!inGame) timer.stop();


    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);    //paints the background
        doDrawing(g);
    }

    private void doDrawing(Graphics g){
        if (inGame){          //draw in game components if ingame
            g.drawImage(apple, apple_x, apple_y, this); //draw apple on screen with given coordinates
            for(int z = 0; z < dots; z++){  //draw head and body on screen
                if(z==0){       //if first dot
                    g.drawImage(head, x[z], y[z], this);
                }else{
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync(); //synchronisez the graphics state of the applications with the display device to ensure the drawing is compleete
        }else{  //draw gameover if not inGame
            gameOver(g);
        }
    }

    private void gameOver(Graphics g){
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) /2, B_HEIGHT / 2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }

        repaint();  //redraw gameboard to screen
    }

    //handle key events in a java swing application
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();

            //if left was pressed and snake is not moving right
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            //if right was pressed and snake is not moving left
            if((key == KeyEvent.VK_RIGHT) && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            //if up was pressed and snake is not moving down
            if((key == KeyEvent.VK_UP) && (!downDirection)){
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            //if down was pressed and snake is not moving up
            if((key == KeyEvent.VK_DOWN) && (!upDirection)){
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}

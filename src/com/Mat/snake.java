
package com.Mat;

import javax.swing.*;
import java.awt.*;

public class snake extends JFrame {
    public snake(){
        initUI();
    }

    private void initUI(){
        add(new Board());       //create new board
        setResizable(false);        //make jframe fixed size
        pack();         //makes sure window is sized to preffered width and height
        setTitle("Snake");  //sets title of the frame
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        //execute run method in an event dispatch thread
        EventQueue.invokeLater(()->{
            JFrame ex = new snake();
            ex.setVisible(true);
        });
    }
}

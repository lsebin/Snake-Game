package org.cis120.myGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RunSnake implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Snake");
        frame.setLocation(300, 300);

        final JPanel status_panel = new JPanel(new GridLayout(2, 1));
        frame.add(status_panel, BorderLayout.SOUTH);

        final JPanel update_panel = new JPanel(new GridLayout(1, 2));
        final JLabel status = new JLabel("Running...", JLabel.CENTER);
        update_panel.add(status);

        final JLabel appleEaten = new JLabel("Apple : 0", JLabel.CENTER);
        update_panel.add(appleEaten);

        final JLabel explain = new JLabel("Press spacebar to speed up", (JLabel.CENTER));

        status_panel.add(update_panel);
        status_panel.add(explain);

        // Main playing area
        final GameBoard board = new GameBoard(status, appleEaten);
        frame.add(board, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // Pause button
        final JButton pause = new JButton("Pause");
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.pause();
            }
        });
        control_panel.add(pause);

        // Save button
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.save();
            }
        });
        control_panel.add(save);

        // Recall button
        final JButton recall = new JButton("Recall");
        recall.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.recall();
            }
        });
        control_panel.add(recall);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        board.reset();
    }
}

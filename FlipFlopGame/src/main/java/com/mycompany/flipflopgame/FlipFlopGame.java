/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flipflopgame;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlipFlopGame extends JFrame implements ActionListener {
    private static final int GRID_SIZE = 4;
    private static final int HINT_PENALTY = 2;

    private JButton[][] buttons;
    private List<Integer> indices;
    private JButton previousButton;
    private int previousRow;
    private int previousCol;
    private boolean quickViewMode;
    private int score;
    private int matchedPairs;
    private JLabel scoreLabel;
    private JButton questionButton;
    private JButton cancelButton;
    private Timer hintTimer;
    private List<ImageIcon> imageIcons;

    public FlipFlopGame() {
        super("Flip Flop Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        buttons = new JButton[GRID_SIZE][GRID_SIZE];
        previousButton = null;
        quickViewMode = true;
        score = 0;
        matchedPairs = 0;

        // Generate a list of indices
        indices = generateIndices();

        // Shuffle the indices
        Collections.shuffle(indices);

        imageIcons = loadImages();

        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new java.awt.Dimension(100, 100));
                button.setBackground(new Color(128, 0, 128)); // RGB values for purple
                button.addActionListener(this);
                buttons[row][col] = button;

                panel.add(button);
                index++;
            }
        }

        add(panel);
        add(createControlPanel(), BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);

        hintTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideImagesAfterHint();
                questionButton.setEnabled(true);
                hintTimer.stop();
            }
        });

        showImagesForDelay();
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE); // Change the background color to white

        questionButton = new JButton("?");
        questionButton.setForeground(Color.GRAY);
        questionButton.setBackground(Color.RED);
        questionButton.setFocusPainted(false);
        questionButton.setFont(new Font("Arial", Font.BOLD, 16));
        questionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showImagesForHint();
                questionButton.setEnabled(false);
                hintTimer.restart();
                score -= HINT_PENALTY; // Reduce score by hint penalty
                scoreLabel.setText("Score: " + score);
            }
        });

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.GRAY);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 117, 0, 0));

        cancelButton = new JButton("X");
        cancelButton.setForeground(Color.GRAY);
        cancelButton.setBackground(Color.RED);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(FlipFlopGame.this,
                        "Are you sure you want to exit the game?", "Exit Game",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(questionButton, BorderLayout.WEST);
        controlPanel.add(scoreLabel, BorderLayout.CENTER);
        controlPanel.add(cancelButton, BorderLayout.EAST);

        return controlPanel;
    }

    private List<Integer> generateIndices() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE * GRID_SIZE / 2; i++) {
            indices.add(i);
            indices.add(i);
        }
        return indices;
    }

    private List<ImageIcon> loadImages() {
        List<ImageIcon> imageIcons = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE * GRID_SIZE / 2; i++) {
            String filePath = "C:/Users/corei 3 7th/Documents/NetBeansProjects/FlipFlopGame/src/main/resources/images/image" + i + ".png";
            ImageIcon imageIcon = new ImageIcon(filePath);
            imageIcons.add(imageIcon);
        }
        return imageIcons;
    }

    private void showImagesForDelay() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JButton button = buttons[row][col];
                button.setIcon(null);
                button.setBackground(new Color(128, 0, 128)); // RGB values for purple
            }
        }

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideImages();
                quickViewMode = false;
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showImagesForHint() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JButton button = buttons[row][col];
                button.setIcon(imageIcons.get(indices.get(row * GRID_SIZE + col)));
            }
        }
    }

    private void hideImagesAfterHint() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JButton button = buttons[row][col];
                if (button.getBackground() != Color.GREEN) {
                    button.setIcon(null);
                    button.setBackground(new Color(128, 0, 128)); // RGB values for purple
                }
            }
        }
    }

    private void hideImages() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JButton button = buttons[row][col];
                button.setIcon(null);
                button.setBackground(new Color(128, 0, 128)); // RGB values for purple
            }
        }
    }

    private void showNumber(int row, int col) {
        JButton button = buttons[row][col];
        int index = indices.get(row * GRID_SIZE + col);
        ImageIcon imageIcon = imageIcons.get(index);
        Image image = imageIcon.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
    }

    private void flipBack(int row, int col) {
        JButton button = buttons[row][col];
        button.setIcon(null);
        button.setBackground(new Color(128, 0, 128)); // RGB values for purple
    }

    private void processClick(int row, int col) {
        if (quickViewMode) {
            return;
        }

        JButton button = buttons[row][col];
        if (button.getIcon() != null) {
            return;
        }

        showNumber(row, col);

        if (previousButton == null) {
            previousButton = button;
            previousRow = row;
            previousCol = col;
        } else {
            if (indices.get(row * GRID_SIZE + col).equals(indices.get(previousRow * GRID_SIZE + previousCol))) {
                button.setBackground(Color.GREEN);
                previousButton.setBackground(Color.GREEN);
                score += 10;
                scoreLabel.setText("Score: " + score);
                matchedPairs++;

                if (matchedPairs == GRID_SIZE * GRID_SIZE / 2) {
                    JOptionPane.showMessageDialog(this, "Congratulations! You won the game!");
                    int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (choice == JOptionPane.YES_OPTION) {
                        restartGame();
                    } else {
                        System.exit(0);
                    }
                }
            } else {
                score -= 2;
                scoreLabel.setText("Score: " + score);
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flipBack(row, col);
                        flipBack(previousRow, previousCol);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
            previousButton = null;
        }
    }

    private void restartGame() {
        quickViewMode = true;
        previousButton = null;
        score = 0;
        matchedPairs = 0;
        scoreLabel.setText("Score: 0");

        // Generate a new list of indices
        indices = generateIndices();

        // Shuffle the indices
        Collections.shuffle(indices);

        // Hide images after delay
        showImagesForDelay();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (button == buttons[row][col]) {
                    processClick(row, col);
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlipFlopGame().setVisible(true);
            }
        });
    }
}

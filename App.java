import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class App {
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
    }

    public void createAndShowGUI() {
        frame = new JFrame("SPLASH TRIVIA!");

        JPanel canvas = new JPanel() {
            BufferedImage backgroundImage;
            BufferedImage zombieImage;
            BufferedImage personImage;
            List<Point> pathPoints = new ArrayList<>();
            List<Integer> zombieIndices = new ArrayList<>();
            List<Point> people = new ArrayList<>();
            {
                try {
                    backgroundImage = ImageIO.read(new File("final map.png"));
                    zombieImage = ImageIO.read(new File("Zombie.png"));
                    personImage = ImageIO.read(new File("Default Char.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Define path
                pathPoints.add(new Point(125, 500));
                pathPoints.add(new Point(125, 350));
                pathPoints.add(new Point(125, 200));
                pathPoints.add(new Point(237, 200));
                pathPoints.add(new Point(350, 200));
                pathPoints.add(new Point(462, 200));
                pathPoints.add(new Point(575, 200));
                pathPoints.add(new Point(575, 312));
                pathPoints.add(new Point(575, 425));
                pathPoints.add(new Point(637, 425));
                pathPoints.add(new Point(700, 425));
                pathPoints.add(new Point(700, 400));
            }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                if (zombieImage != null) {
                    for (int index : zombieIndices) {
                        if (index < pathPoints.size()) {
                            Point p = pathPoints.get(index);
                            g.drawImage(zombieImage, p.x, p.y, 40, 40, this);
                        }
                    }
                }
                if (personImage != null) {
                    for (Point p : people) {
                        g.drawImage(personImage, p.x, p.y, 40, 40, this);
                    }
                }
            }
        };

        JLabel label = new JLabel("Starting...");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        JButton triviaButton = new JButton("Answer Questions");
        triviaButton.setFont(new Font("Arial", Font.BOLD, 16));
        triviaButton.addActionListener(e -> launchTriviaGame());

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.add(label);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(triviaButton);

        canvas.setLayout(new BorderLayout());
        canvas.add(contentPanel, BorderLayout.CENTER);

        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Timer for label
        Timer labelTimer = new Timer(1000, new ActionListener() {
            int counter = 0;
            public void actionPerformed(ActionEvent e) {
                counter++;
                label.setText("Seconds passed: " + counter);
            }
        });
        labelTimer.start();

        Timer moveZombies = new Timer(2000, e -> {
            try {
                var panel = (JPanel) frame.getContentPane().getComponent(0);
                var indicesField = panel.getClass().getDeclaredField("zombieIndices");
                var pathField = panel.getClass().getDeclaredField("pathPoints");

                indicesField.setAccessible(true);
                pathField.setAccessible(true);

                @SuppressWarnings("unchecked")
                List<Integer> indices = (List<Integer>) indicesField.get(panel);
                List<Point> path = (List<Point>) pathField.get(panel);

                for (int i = 0; i < indices.size(); i++) {
                    int idx = indices.get(i);
                    if (idx < path.size() - 1) {
                        indices.set(i, idx + 1);
                    }
                }

                indices.removeIf(i -> i >= path.size() - 1);

                panel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        moveZombies.start();

        Timer spawnZombies = new Timer(2000, e -> {
            try {
                var panel = (JPanel) frame.getContentPane().getComponent(0);
                var indicesField = panel.getClass().getDeclaredField("zombieIndices");

                indicesField.setAccessible(true);

                @SuppressWarnings("unchecked")
                List<Integer> indices = (List<Integer>) indicesField.get(panel);

                indices.add(0); 
                panel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        spawnZombies.start();

        try {
            JPanel panel = (JPanel) frame.getContentPane().getComponent(0);
            var peopleField = panel.getClass().getDeclaredField("people");

            peopleField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Point> people = (List<Point>) peopleField.get(panel);

            people.add(new Point(300, 300)); // Spawn a person here
            panel.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void launchTriviaGame() {
        TriviaDialog triviaDialog = new TriviaDialog(frame);
        triviaDialog.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class App {
    private JFrame frame;
    private int ammoCount = 10;
    private JLabel ammoLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
    }

    public void createAndShowGUI() {
        frame = new JFrame("SPLASH TRIVIA!");

        JPanel canvas = new JPanel() {
            BufferedImage backgroundImage;
            BufferedImage zombieImage;
            BufferedImage personImage;
            BufferedImage gunImage;
            List<Point> pathPoints = new ArrayList<>();
            List<Integer> zombieIndices = new ArrayList<>();
            Point playerPosition = new Point(300, 300);
            {
                try {
                    backgroundImage = ImageIO.read(new File("final map.png"));
                    zombieImage = ImageIO.read(new File("Zombie.png"));
                    personImage = ImageIO.read(new File("Bigger Default Char.png"));
                    gunImage = ImageIO.read(new File("NotARocketLauncher.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                
                pathPoints.add(new Point(125, 500));
                pathPoints.add(new Point(125, 450));
                pathPoints.add(new Point(125, 400));
                pathPoints.add(new Point(125, 350));
                pathPoints.add(new Point(125, 300));
                pathPoints.add(new Point(125, 250));
                pathPoints.add(new Point(125, 200));
                pathPoints.add(new Point(180, 200));
                pathPoints.add(new Point(237, 200));
                pathPoints.add(new Point(293, 200));
                pathPoints.add(new Point(350, 200));
                pathPoints.add(new Point(406, 200));
                pathPoints.add(new Point(462, 200));
                pathPoints.add(new Point(518, 200));
                pathPoints.add(new Point(575, 200));
                pathPoints.add(new Point(575, 256));
                pathPoints.add(new Point(575, 312));
                pathPoints.add(new Point(575, 368));
                pathPoints.add(new Point(575, 425));
                pathPoints.add(new Point(606, 425));
                pathPoints.add(new Point(637, 425));
                pathPoints.add(new Point(668, 425));
                pathPoints.add(new Point(700, 425));
                pathPoints.add(new Point(700, 412));
                pathPoints.add(new Point(700, 400));
            
            setFocusable(true);
            requestFocusInWindow();

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    int moveAmount = 10;

                    switch (key) {
                        case KeyEvent.VK_W: playerPosition.y -= moveAmount; 
                        break;
                        case KeyEvent.VK_A: playerPosition.x -= moveAmount; 
                        break;
                        case KeyEvent.VK_S: playerPosition.y += moveAmount; 
                        break;
                        case KeyEvent.VK_D: playerPosition.x += moveAmount; 
                        break;
                        case KeyEvent.VK_SPACE:
                            if (ammoCount > 0) {
                            ammoCount--;
                            ammoLabel.setText("Ammo: " + ammoCount);
                            }
                            break;
                    }
                    repaint();
                }
            });
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
                if (personImage != null && playerPosition != null) {
                    g.drawImage(personImage, playerPosition.x, playerPosition.y, 40, 40, this);
                    if (gunImage != null) {
                        int gunX = playerPosition.x + 20;
                        int gunY = playerPosition.y + 10;
                        g.drawImage(gunImage, gunX, gunY, 20, 10, this);
                    }
                }
                
            }
        };

        JLabel label = new JLabel("Starting...");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        ammoLabel = new JLabel("Ammo: " + ammoCount);
        ammoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ammoLabel.setForeground(Color.YELLOW);

        JButton triviaButton = new JButton("Answer Questions");
        triviaButton.setFont(new Font("Arial", Font.BOLD, 16));
        triviaButton.addActionListener(e -> launchTriviaGame());

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.add(label);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(ammoLabel);
        contentPanel.add(triviaButton);

        canvas.setLayout(new BorderLayout());
        canvas.add(contentPanel, BorderLayout.CENTER);

        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        
        Timer labelTimer = new Timer(1000, new ActionListener() {
            int counter = 0;
            public void actionPerformed(ActionEvent e) {
                counter++;
                label.setText("Seconds passed: " + counter);
            }
        });
        labelTimer.start();

        Timer moveZombies = new Timer(1000, e -> {
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

        Timer spawnZombies = new Timer(1500, e -> {
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
    }

    public void launchTriviaGame() {
        TriviaDialog triviaDialog = new TriviaDialog(frame, this);
        triviaDialog.setVisible(true);
    }

    public void increaseAmmo() {
        ammoCount++;
        ammoLabel.setText("Ammo: " + ammoCount);
    }
}

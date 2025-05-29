import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//ALL COMMENTS BELOW ARE BY ARNAV KUMAR
public class App {
    private JFrame frame;
    private int ammoCount = 10;
    private JLabel ammoLabel;
    private JPanel gamePanel;
    private int zombiesAtEnd = 0;
    private Timer moveZombies;
    private Timer spawnZombies;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
    }

    public void createAndShowGUI() {
        frame = new JFrame("SPLASH TRIVIA!");
        //i set up the canvas in this block, comments are below
        JPanel canvas = new JPanel() {
            BufferedImage backgroundImage;
            BufferedImage zombieImage;
            BufferedImage personImage;
            BufferedImage gunImage;
            List<Point> pathPoints = new ArrayList<>();
            List<Integer> zombieIndices = new ArrayList<>();
            Point playerPosition = new Point(300, 300);
            List<Bullet> bullets = new ArrayList<>();
            //this is how i represent all the sprites with their respective images
            {
                try {
                    backgroundImage = ImageIO.read(new File("final map.png"));
                    zombieImage = ImageIO.read(new File("Zombie.png"));
                    personImage = ImageIO.read(new File("Bigger Default Char.png"));
                    gunImage = ImageIO.read(new File("NotARocketLauncher.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //this is the path along which the zombies travel
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
                pathPoints.add(new Point(700, 400)); //the end of the path, where we have to stop the zombies from reaching

                setFocusable(true);
                requestFocusInWindow();

                //this is where i set up the controls (WASD movement, space to shoot)
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
                                    bullets.add(new Bullet(playerPosition.x + 20, playerPosition.y));
                                }
                                break;
                        }
                        repaint();
                    }
                });

                //this is what happens after a bullet is shot(with space button); the next bullet is loaded from its iterator
                Timer bulletTimer = new Timer(20, e -> {
                    Iterator<Bullet> bulletIt = bullets.iterator();
                    while (bulletIt.hasNext()) {
                        Bullet bullet = bulletIt.next();
                        bullet.move();
                        if (bullet.getY() < 0) {
                            bulletIt.remove();
                            continue;
                        }
                        //using the remove() method, i removed zombies from the map when they were hit by the bullets.
                        //to check if they collided, i checked for an intersection of their rectanglular hitboxes.
                        Iterator<Integer> zombieIt = zombieIndices.iterator();
                        while (zombieIt.hasNext()) {
                            int zIdx = zombieIt.next();
                            if (zIdx < pathPoints.size()) {
                                Point zp = pathPoints.get(zIdx);
                                Rectangle zombieRect = new Rectangle(zp.x, zp.y, 40, 40);
                                Rectangle bulletRect = new Rectangle(bullet.getX(), bullet.getY(), 5, 5);
                                if (zombieRect.intersects(bulletRect)) {
                                    bulletIt.remove();
                                    zombieIt.remove();
                                    break;
                                }
                            }
                        }
                    }
                    this.repaint();
                });
                bulletTimer.start();
            }

            //this is where i actually display the sprites defined in the try/catch block
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
                for (Bullet bullet : bullets) {
                    bullet.draw(g);
                }
            }
        };

        //this is the label i used to keep track of time
        JLabel label = new JLabel("Starting...");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        //this label keeps track of ammo. i update this label every time the "shoot"(-1 ammo) or "correct"(+1 ammo) events occur.
        ammoLabel = new JLabel("Ammo: " + ammoCount);
        ammoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ammoLabel.setForeground(Color.YELLOW);

        //this is the button which opens the triviadialog window
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

        //keeps track of how much time(seconds) has elapsed
        Timer secondCounter = new Timer(1000, new ActionListener() {
            int counter = 0;
            public void actionPerformed(ActionEvent e) {
                counter++;
                label.setText("Seconds passed: " + counter);
            }
        });
        secondCounter.start();

        //zombies move every 1000 milliseconds(1 second) to the next node of the path, designated in lines 53 to 77
        moveZombies = new Timer(1000, e -> {
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


                int before = indices.size();
                indices.removeIf(i -> i >= path.size() - 1);
                int after = indices.size();
                int zombiesRemoved = before - after;
                if (zombiesRemoved > 0) {
                    zombiesAtEnd += zombiesRemoved;
                    if (zombiesAtEnd >= 5) {
                        endGame();
                    }
                }

                panel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        moveZombies.start();

        /*i choose to spawn zombies at the start of the path every 1.5 seconds. I leave a 0.5 second delay so that 
        it's easier to see the zombies moving along the path*/
        spawnZombies = new Timer(1500, e -> {
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

        gamePanel = canvas;
    }

    //this function is called when "Answer Questions" is pressed. This opens the TriviaDialog
    public void launchTriviaGame() {
        TriviaDialog triviaDialog = new TriviaDialog(frame, this);
        triviaDialog.setVisible(true);
        gamePanel.requestFocusInWindow();
    }

    public void increaseAmmo() {
        ammoCount++;
        ammoLabel.setText("Ammo: " + ammoCount);
    }
    //called when 5 zombies reach the end
    private void endGame() {
        moveZombies.stop();
        spawnZombies.stop();
        JOptionPane.showMessageDialog(frame, "Game Over! 5 zombies reached the end.", "Game Over", JOptionPane.ERROR_MESSAGE);
        frame.dispose();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class App {
    private JFrame frame;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App().createAndShowGUI();
            }
        });
    }
    
    public void createAndShowGUI() {
        frame = new JFrame("SPLASH TRIVIA!");

        JPanel canvas = new JPanel() {
            BufferedImage backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("final map.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        JLabel label = new JLabel("Starting...");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE); 
        
        
        JButton triviaButton = new JButton("Start Trivia Game");
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

        Timer timer = new Timer(1000, new ActionListener() {
            int counter = 0;
            public void actionPerformed(ActionEvent e) {
                counter++;
                label.setText("Seconds passed: " + counter);
            }
        });

        timer.start();
    }
    
    public void launchTriviaGame() {
        TriviaDialog triviaDialog = new TriviaDialog(frame);
        triviaDialog.setVisible(true);
    }
}
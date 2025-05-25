import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class App {
    public static void main(String[] args) {
        //Create the frame.
        JFrame frame = new JFrame("SPLASH TRIVIA!");

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
        canvas.add(label);
        
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
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
}
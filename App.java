import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class App {
    public static void main(String[] args) {
        //Create the frame.
        JFrame frame = new JFrame("Test");

        JPanel canvas = new JPanel();
        canvas.setPreferredSize( new Dimension(920, 1080) );

        JLabel label = new JLabel("Starting...");
        label.setFont(new Font("Arial", Font.BOLD, 32));
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
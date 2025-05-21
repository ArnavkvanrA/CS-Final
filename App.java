import javax.swing.*;
import java.awt.*;
public class App {
    public static void main(String[] args) {
        //Create the frame.
        JFrame frame = new JFrame("Test");

        JPanel canvas = new JPanel();
        canvas.setPreferredSize( new Dimension(920, 1080) );

        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
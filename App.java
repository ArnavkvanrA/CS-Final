import javax.swing.*;
import java.awt.*;
public class App {
    public static void main(String[] args) {
        //Create the frame.
        JFrame frame = new JFrame("Test");

        JPanel canvas = new JPanel();
        canvas.setPreferredSize( new Dimension( 800, 600 ) );

        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
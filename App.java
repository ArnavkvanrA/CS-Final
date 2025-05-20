import javax.swing.*;
import java.awt.*;
public class App{
    public static void main(String[] args){
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        JLabel label = new JLabel("Hello, Swing!", SwingConstants.CENTER);
        frame.getContentPane().add(label);
        frame.setVisible(true); 
    }
}
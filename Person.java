import javax.swing.*;
import java.awt.*;
public class Person{
    private double x_coord;
    private double y_coord;
    private int ammo;
    
    public Person(double x, double y){
        x_coord = x;
        y_coord = y;
    }

    public void move(int x_move, int y_move){
        x_coord += x_move;
        y_coord += y_move;
        if (x_coord + x_move < 0){
            x_coord = 0;
        }
        else if(x_coord + x_move > 1080){
            x_coord = 1080;
        }
        if (y_coord + y_move < 0){
            y_coord = 0;
        }
        else if(y_coord + y_move > 920){
            y_coord = 920;
        }
    }


}
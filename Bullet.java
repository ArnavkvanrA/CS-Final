import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Bullet {
    private int x;
    private int y;
    private int speed = 5;

    public Bullet(int x, int y){
        this.x=x;
        this.y=y;
    }
    public void move(){
        this.x += speed;
    }
    public Rectangle getBounds()
    {
        return new Rectangle(getX(), getY(), 10, 30);
    }   
    public void draw(Graphics g){
        g.setColor(Color.BLUE);
        g.fillOval(x,y,5,5);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}

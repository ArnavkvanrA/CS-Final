import java.awt.*;

public class Bullet {
    private int x;
    private int y;
    private int speed = 5;

    public Bullet(int x, int y){
        this.x=x;
        this.y=y;
    }
    public void move(){
        this.y -= speed;
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

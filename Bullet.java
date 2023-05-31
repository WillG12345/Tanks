import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.math.Vector2; 
public class Bullet
{
    private Texture img;
    private Vector2 position; // x and y
    private Vector2 dimensions; // width and height
    private Vector2 velocity; // deltaX and deltaY
    private float angle;
    
    public Bullet(Texture img, float x, float y, float angle) {
        this.img = img;
        position = new Vector2(x, y);
        dimensions = new Vector2(Constants.BULLET_RADIUS * 2, Constants.BULLET_RADIUS * 2);
        velocity = new Vector2((float)Math.cos(Math.toRadians(angle)) * Constants.BULLET_SPEED, (float)Math.sin(Math.toRadians(angle)) * Constants.BULLET_SPEED);
        this.angle = angle;
    }
    
    public void update() {
        position.add(velocity);
    }
    
    public Texture getImg() {
        return img;
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public float getWidth() {
        return dimensions.x;
    }
    
    public float getHeight() {
        return dimensions.y;
    }
    
    public float getAngle() {
        return angle;
    }
}

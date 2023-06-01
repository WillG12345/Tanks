import com.badlogic.gdx.ApplicationAdapter; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle; 
import com.badlogic.gdx.math.Circle; 
import com.badlogic.gdx.Input.Keys; 
import com.badlogic.gdx.math.Vector2; 
import com.badlogic.gdx.math.MathUtils; 
import com.badlogic.gdx.math.Intersector; 
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.InputProcessor; 
import com.badlogic.gdx.*; 
import com.badlogic.gdx.utils.Array;  
import java.util.*; 
import com.badlogic.gdx.audio.*; 
import com.badlogic.gdx.math.Polygon;
/**
 * By Aiden Favish
 * To replace LibGDX's awful implimentation of intersections with polygons and lines
 */
public class BetterIntersector
{
    
    public static boolean intersectLinePolygon(Vector2 point1, Vector2 point2, float[] verticies) {
        Vector2 vertex = new Vector2(verticies[0], verticies[1]);
        int temp = Intersector.pointLineSide(point1, point2, vertex);
        for (int i = 2; i < verticies.length; i+=2) {
            vertex.set(verticies[i], verticies[i+1]);
            if (Intersector.pointLineSide(point1, point2, vertex) != temp) {
                return true;
            }
        }
        return false;
    }
    
    
    
    
    
}




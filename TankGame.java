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

public class TankGame extends ApplicationAdapter 
{
    SpriteBatch batch;
    
    private Texture exit;
    private Texture exitHigh;
    private Rectangle exitButtonRect;
    
    private Texture set;
    private Texture setHigh;
    private Rectangle setButtonRect;



    Texture tankTexture;
    Texture tankTexture2;
    Polygon tankPolygon;
    Polygon tankPolygon2;
    Vector2 position;
    Vector2 position2;
    Texture img;
    private float tankRotation;
    private float tankRotation2;
    
    

    private Texture redbullet;
    private Texture bluebullet;
    private Circle rb;
    private Circle bb;
    
    // private Vector2 tankPosition;

    private Rectangle startButtonRect;
    private Texture start;
    private Texture startHighlighted;
    private Texture menuimg;
    private Texture instructionsimg;

    private Rectangle settingsButtonRect;

    private float mouseX;
    private float mouseY;
    float rotationSpeed = 250f; // Degrees per second
    float speed = 125f; // Pixels per second
    private BitmapFont font; 
    private GlyphLayout layout; 
    private Gamestate gamestate; 
    private Viewport viewport; //maintains the ratios of your world
    private OrthographicCamera camera; //the camera to our world
    private Vector2[][] barriers;
    
    // For bullets
    private Vector2 bulletPosition2;
    private Vector2 bulletPosition;
    private Vector2 bulletDirection;
    private Vector2 bulletVelocity;
    

    @Override
    public void create () {
        camera = new OrthographicCamera(); 
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();
        layout = new GlyphLayout(); 
        font = new BitmapFont(); 
        start = new Texture("button.png");
        startHighlighted =  new Texture("highlightedbutton.png");
        
        exit = new Texture("exit.png");
        exitHigh = new Texture("exitHigh.png");
        
        set = new Texture("set.png");
        setHigh = new Texture("setHigh.png");



        redbullet=new Texture("redbullet.png");
        bluebullet=new Texture("bluebullet.png");
        bulletPosition=new Vector2(100,100);
        bulletPosition2=new Vector2(100,100);

        img = new Texture("background.png");
        menuimg = new Texture("menuimg.jfif");
        instructionsimg = new Texture("instructionsimg.png");
        tankTexture = new Texture("tank1.png");
        tankTexture2=new Texture("Tank2.png");
        
        // Starting positon for red tank
        position = new Vector2(1300, 100);
        
        // Starting position for blue tank
        position2 = new Vector2(75, 400);
        
        tankRotation = 0;
        tankRotation2 = 180;

        rb=new Circle(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, 10);
        bb=new Circle(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, 10);

        // Define the polygon that represents the tank
        float[] vertices = {
                0, 0,
                tankTexture.getWidth(), 0,
                tankTexture.getWidth(), tankTexture.getHeight(),
                0, tankTexture.getHeight()
            };
        tankPolygon = new Polygon(vertices);
        tankPolygon.setOrigin(tankTexture.getWidth()/2, tankTexture.getHeight()/2);
        tankPolygon.setPosition(position.x, position.y);

        //2
        float[] vertices2 = {
                0, 0,
                tankTexture2.getWidth(), 0,
                tankTexture2.getWidth(), tankTexture2.getHeight(),
                0, tankTexture2.getHeight()
            };
        tankPolygon2 = new Polygon(vertices2);
        tankPolygon2.setOrigin(tankTexture2.getWidth()/2, tankTexture2.getHeight()/2);
        tankPolygon2.setPosition(position2.x, position2.y);
        gamestate=Gamestate.MENU;
        setButtonRect = new Rectangle(Constants.WORLD_WIDTH / 2 -  set.getWidth() / 2, Constants.WORLD_HEIGHT / 2-100 ,set.getWidth(), set.getHeight());
        exitButtonRect = new Rectangle(Constants.WORLD_WIDTH / 2 -  exit.getWidth() / 2, Constants.WORLD_HEIGHT / 2-100 ,exit.getWidth(), exit.getHeight());

        startButtonRect = new Rectangle(Constants.WORLD_WIDTH / 2 -  start.getWidth() / 2, Constants.WORLD_HEIGHT / 2 ,start.getWidth(), start.getHeight());
        
        // makes the barriers formatted startingPoint1, startingPoint2, amountToMoveAfterCollision
        barriers = new Vector2[4][3];
        barriers[0] = new Vector2[] {new Vector2(0, 0), new Vector2(0, Constants.WORLD_HEIGHT), new Vector2(1, 0)}; // left wall
        barriers[1] = new Vector2[] {new Vector2(0, Constants.WORLD_HEIGHT), new Vector2(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT), new Vector2(0, -1)}; // top wall
        barriers[2] = new Vector2[] {new Vector2(0, 0), new Vector2(Constants.WORLD_WIDTH, 0), new Vector2(0, 1)}; // bottom wall
        barriers[3] = new Vector2[] {new Vector2(Constants.WORLD_WIDTH, 0), new Vector2(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT), new Vector2(-1, 0)}; // right wall
    }

    public void render() {
        // Handle input
        handleInput();
        
        // Checks if we are colliding with a barrier
        //checkCollisions(); off for now until i get it working

        // Update game logic
        update();

        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin the sprite batch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        

        // Draw the background
        batch.draw(img, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Draw tanks
        drawredTank(tankTexture, tankPolygon);
        drawblueTank(tankTexture2, tankPolygon2);

        // Draw UI elements based on the game state
        if (gamestate == Gamestate.MENU) {

            drawMenu();
        } else if (gamestate == Gamestate.INSTRUCTIONS) {
            drawInstructions();
        }
        
        
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // Calculate bullet position and velocity based on tank information
            
            /*bulletPosition.set(position2.x + tankTexture2.getWidth() / 2, position2.y + tankTexture2.getHeight() / 2);
            bulletDirection.set(MathUtils.cosDeg(tankRotation2), MathUtils.sinDeg(tankRotation2)).nor();
            bulletVelocity.set(bulletDirection).scl(5);
            bulletPosition.add(bulletVelocity.x, bulletVelocity.y);

            // Draw the bullet
            batch.draw(bluebullet, bulletPosition.x, bulletPosition.y);*/
        }


        // End the sprite batch
        batch.end();
    }
    
    private void checkCollisions() {
        for (Vector2[] b: barriers) {
            if (Intersector.intersectLinePolygon(b[0], b[1], tankPolygon)) {
                position.add(b[2]);
                System.out.println("intersect red");
            }
            if (Intersector.intersectLinePolygon(b[0], b[1], tankPolygon2)) {
                position2.add(b[2]);
                System.out.println("intersect blue");
            }
        }
    }

    @Override
    public void dispose() {
        // Dispose textures and font
        img.dispose();
        tankTexture.dispose();
        tankTexture2.dispose();
        start.dispose();
        startHighlighted.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    /*

    private Polygon createTankPolygon(Texture tankTexture) {
    float[] vertices = {
    0, 0,
    tankTexture.getWidth(), 0,
    tankTexture.getWidth(), tankTexture.getHeight(),
    0, tankTexture.getHeight()
    };

    Polygon tankPolygon = new Polygon(vertices);
    tankPolygon.setOrigin(tankTexture.getWidth() / 2, tankTexture.getHeight() / 2);
    tankPolygon.setRotation(tankPolygon.getRotation());
    return tankPolygon;
    }
     */
    private void handleInput() {
        if (gamestate == Gamestate.GAME) {
            // Handle input for tank 1
            handleTankInput(tankPolygon, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN);

            // Handle input for tank 2
            handleTankInput2(tankPolygon2, Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S);
            
        } else {
            // Handle menu and instructions screen input
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                gamestate = Gamestate.INSTRUCTIONS;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                gamestate = Gamestate.MENU;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                gamestate = Gamestate.GAME;
            }
        }
    }

    private void handleTankInput(Polygon tankPolygon, int rotateLeftKey, int rotateRightKey, int moveForwardKey, int moveBackwardKey) {
        float rotation = tankPolygon.getRotation();
        if (Gdx.input.isKeyPressed(rotateLeftKey)) {
            rotation += rotationSpeed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(rotateRightKey)) {
            rotation -= rotationSpeed * Gdx.graphics.getDeltaTime();
        }
        float deltaX = 0;
        float deltaY = 0;
        if (Gdx.input.isKeyPressed(moveForwardKey)) {
            deltaY = speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(moveBackwardKey)) {
            deltaY = -speed * Gdx.graphics.getDeltaTime();
        }

        position.x -= (float) Math.sin(Math.toRadians(rotation)) * deltaY;
        position.y += (float) Math.cos(Math.toRadians(rotation)) * deltaY;
        tankPolygon.setPosition(position.x, position.y);
        tankPolygon.setRotation(rotation);

    }

    private void handleTankInput2(Polygon tankPolygon2, int rotateLeftKey, int rotateRightKey, int moveForwardKey, int moveBackwardKey) {
        float rotation2 = tankPolygon2.getRotation();
        if (Gdx.input.isKeyPressed(rotateLeftKey)) {
            rotation2 += rotationSpeed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(rotateRightKey)) {
            rotation2 -= rotationSpeed * Gdx.graphics.getDeltaTime();
        }
        float deltaX2 = 0;
        float deltaY2 = 0;
        if (Gdx.input.isKeyPressed(moveForwardKey)) {
            deltaY2 = speed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(moveBackwardKey)) {
            deltaY2 = -speed * Gdx.graphics.getDeltaTime();
        }

        position2.x -= (float) Math.sin(Math.toRadians(rotation2)) * deltaY2;
        position2.y += (float) Math.cos(Math.toRadians(rotation2)) * deltaY2;
        tankPolygon2.setPosition(position2.x, position2.y);
        tankPolygon2.setRotation(rotation2);
        

    }

    private void update() {
        // Update the camera
        camera.update();

        // Set the batch projection matrix to the camera combined matrix
        batch.setProjectionMatrix(camera.combined);
        
        tankPolygon.setPosition(position.x, position.y);
        tankPolygon2.setPosition(position2.x, position2.y);
    }

    private void drawredTank(Texture tankTexture, Polygon tankPolygon) {
        float tankX = tankPolygon.getX();
        float tankY = tankPolygon.getY();
        float tankOriginX = tankPolygon.getOriginX();
        float tankOriginY = tankPolygon.getOriginY();
        float tankRotation = tankPolygon.getRotation();

        //batch.draw(tankTexture, tankX+1200, tankY, tankOriginX, tankOriginY, tankTexture.getWidth(), tankTexture.getHeight(),
            //1f, 1f, tankRotation, 0, 0, tankTexture.getWidth(), tankTexture.getHeight(), false, false);
        batch.draw(tankTexture, position.x, position.y, tankOriginX, tankOriginY, tankTexture.getWidth(), tankTexture.getHeight(),
            1f, 1f, tankRotation, 0, 0, tankTexture.getWidth(), tankTexture.getHeight(), false, false);

    }

    private void drawblueTank(Texture tankTexture, Polygon tankPolygon) {
        float tankX = tankPolygon.getX();
        float tankY = tankPolygon.getY();
        float tankOriginX = tankPolygon.getOriginX();
        float tankOriginY = tankPolygon.getOriginY();
        float tankRotation = tankPolygon.getRotation();

        batch.draw(tankTexture, position2.x, position2.y, tankOriginX, tankOriginY, tankTexture.getWidth(), tankTexture.getHeight(),
            1f, 1f, tankRotation, 0, 0, tankTexture.getWidth(), tankTexture.getHeight(), false, false);

    }

    private void drawMenu() 
    {
        // Draw the menu background
        batch.draw(menuimg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        mouseX = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).x;
        mouseY = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).y;
        Circle temp = new Circle (mouseX,mouseY, .1f);

        // Draw the start button
        if (startButtonRect.contains(mouseX, mouseY)) {
            batch.draw(startHighlighted, startButtonRect.x, startButtonRect.y);
        } else {
            batch.draw(start, startButtonRect.x, startButtonRect.y);
        }
        if(Intersector.overlaps(temp,startButtonRect) && Gdx.input.justTouched())
        {
            gamestate = Gamestate.GAME; 
        }
          if (setButtonRect.contains(mouseX, mouseY)) {
            batch.draw(setHigh, setButtonRect.x, setButtonRect.y);
        } else {
            batch.draw(set, setButtonRect.x, setButtonRect.y);
        }
        if(Intersector.overlaps(temp,setButtonRect) && Gdx.input.justTouched())
        {
            gamestate = Gamestate.INSTRUCTIONS; 
        }

        font.setColor(Color.RED);
        layout.setText(font, "TANK GAME");
        font.draw(batch, layout, Constants.WORLD_WIDTH / 2 - layout.width / 2,
            Constants.WORLD_HEIGHT / 2 + layout.height+200 / 2);

        // Draw the menu text
        font.setColor(Color.BLUE);
        layout.setText(font, "Welcome to the Tank Game\nI: Instructions, G: Start the Game!");
        font.draw(batch, layout, Constants.WORLD_WIDTH / 2 - layout.width / 2,
            Constants.WORLD_HEIGHT / 2 + layout.height-100 / 2);
    }

    private void drawInstructions() {
        Circle temp = new Circle (mouseX,mouseY, .1f);

        // Draw the instructions background
        batch.draw(instructionsimg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Draw the instructions text
        font.setColor(Color.RED);
        layout.setText(font, "WASD keys for Blue Tank, Arrow keys for Red Tank, .\nPress M to return to the menu\nPress G to start the game!");
        font.draw(batch, layout, Constants.WORLD_WIDTH / 2 - layout.width / 2,
            Constants.WORLD_HEIGHT / 2 + layout.height / 2);
            
        if (exitButtonRect.contains(mouseX, mouseY)) {
            batch.draw(exitHigh, exitButtonRect.x, exitButtonRect.y);
        } else {
            batch.draw(exit, exitButtonRect.x, exitButtonRect.y);
        }
        if(Intersector.overlaps(temp,exitButtonRect) && Gdx.input.justTouched())
        {
            gamestate = Gamestate.MENU; 
        }

    }

}
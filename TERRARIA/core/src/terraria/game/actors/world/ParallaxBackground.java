package terraria.game.actors.world;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.player.Player;
import terraria.game.screens.GameScreen;


public class ParallaxBackground extends Actor {

    boolean constantAnimation;

    private final Array<Texture> textures;
    private final int LAYER_SPEED_DIFFERENCE = 1;

    float x,y,width,heigth;
    float scaleX = 1,scaleY = 1;
    int originX, originY,rotation,srcX,srcY;
    boolean flipX,flipY = false;

    private int scroll = 0;
    private int speed = 0;

    private boolean gameScreenParallax = false;

    public ParallaxBackground(Array<Texture> textures, boolean constantAnimation){

        this.constantAnimation = constantAnimation;

        this.textures = textures;
        textures.get(0).setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        textures.get(1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);

    }

    public void setSpeed(int newSpeed){
        this.speed = newSpeed;
    }

    /**
     * mise à jour de la camera
     * @param camera
     * @param stage
     */
    public void update(OrthographicCamera camera, Stage stage){

        Vector3 vec = camera.position;
        x =  vec.x - stage.getViewport().getScreenWidth()/2 ;
        y = vec.y - stage.getViewport().getScreenHeight()/2 ;
        width = stage.getViewport().getScreenWidth() ;
        heigth = stage.getViewport().getScreenHeight() ;

        if (constantAnimation)
            scroll += speed;
        else if (Gdx.input.isKeyPressed(Input.Keys.Q) && Player.isMovingHorizontally() && TerrariaGame.getState() == GameScreen.GAME_RUNNING)
            scroll -= speed;
        else if (Gdx.input.isKeyPressed(Input.Keys.D) && Player.isMovingHorizontally() && TerrariaGame.getState() == GameScreen.GAME_RUNNING)
            scroll += speed;

    }


    /**
     * draw textures
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        for(int i = 0;i<textures.size ;i++) {
            srcX = scroll + i * this.LAYER_SPEED_DIFFERENCE * scroll;
            srcX =  srcX/4;
            batch.draw(textures.get(i), x, y, originX, originY, width, heigth,scaleX,scaleY,rotation,srcX,srcY,textures.get(i).getWidth(),textures.get(i).getHeight(),flipX,flipY);
        }

    }

}
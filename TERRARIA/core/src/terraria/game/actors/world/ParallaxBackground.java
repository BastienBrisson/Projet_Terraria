package terraria.game.actors.world;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;


public class ParallaxBackground extends Actor {

    OrthographicCamera camera;
    boolean constantAnimation;

    private final Array<Texture> textures;
    private final int LAYER_SPEED_DIFFERENCE = 1;

    float x,y,width,heigth = 0;
    float scaleX,scaleY = 1;
    int originX, originY,rotation,srcX,srcY = 0;
    boolean flipX,flipY = false;

    private int scroll = 0;
    private int speed = 0;
    ScreenAdapter screen;

    public ParallaxBackground(Array<Texture> textures, boolean constantAnimation){

        this.constantAnimation = constantAnimation;

        this.textures = textures;
        for(int i = 0; i <textures.size;i++){
            textures.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        scaleX = scaleY = 1;


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

        this.camera = camera;
        Vector3 vec = camera.position;
        x =  (int)vec.x -  stage.getViewport().getScreenWidth()/2 - 16;;
        y = (int)vec.y -  stage.getViewport().getScreenHeight()/2 - 16;
        width =   stage.getViewport().getScreenWidth() + 16;
        heigth = stage.getViewport().getScreenHeight() + 16;

    }


    /**
     * draw textures
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        if(constantAnimation){scroll += speed;}
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            scroll -= speed;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            scroll += speed;
        }
        for(int i = 0;i<textures.size ;i++) {
            srcX = scroll + i*this.LAYER_SPEED_DIFFERENCE *scroll;
            srcX =  srcX/4;
            batch.draw(textures.get(i), x, y, originX, originY, width, heigth,scaleX,scaleY,rotation,srcX,srcY,textures.get(i).getWidth(),textures.get(i).getHeight(),flipX,flipY);

        }
    }
}
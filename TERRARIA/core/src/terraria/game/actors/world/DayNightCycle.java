package terraria.game.actors.world;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class DayNightCycle extends Actor {

    OrthographicCamera camera;

    private TimeOfDay time;
    private final Texture textures;

    float x, y, width, heigth = 0;
    float scaleX, scaleY = 1;
    int originX, originY, rotation, srcX, srcY = 0;
    boolean flipX, flipY = false;

    private int scroll = 0;
    private int speed = 0;

    long temps_depart;
    long duree;

    boolean first;
    int i;


    public enum TimeOfDay {
        DAY,
        NIGHT,
        SUNSET,
        SUNRISE;
    }

    public DayNightCycle(Texture textures) {

        this.textures = textures;
        this.time = TimeOfDay.DAY;
        textures.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        scaleX = scaleY = 1;
        i = 1;

        temps_depart = System.currentTimeMillis();
        duree = 600000; // 10min en millisecondes
        setSpeed(0);

        first = true;

    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    public TimeOfDay getTime(){
        return time;
    }

    /**
     * mise à jour de la camera
     *
     * @param camera
     * @param stage
     */
    public void update(OrthographicCamera camera, Stage stage) {

        this.camera = camera;
        Vector3 vec = camera.position;
        x = vec.x - stage.getViewport().getScreenWidth() / 2;
        y = vec.y - stage.getViewport().getScreenHeight() / 2;
        width = stage.getViewport().getScreenWidth();
        heigth = stage.getViewport().getScreenHeight() * 6;

    }

    /**
     * draw textures
     *
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        scroll -= speed;
        srcY = scroll;
        srcY = srcY/4;

        if(temps_depart - System.currentTimeMillis() < -duree)
        {
            setSpeed(3);

            if(srcY == -1068 /*night coordinate*/ * i || srcY == - 2136 /*light coordinate*/ * i){

                temps_depart = System.currentTimeMillis();
                if(first){i++; first = false;}
                setSpeed(0);

            }
            else{

                if(-srcY%2136 < 1068 ){time = TimeOfDay.SUNSET;}
                if(-srcY%2136 >= 1068  ){time = TimeOfDay.SUNRISE;}

                first = true;
            }

        }
        else{
            if(-srcY%2136 < 1068 ){time = TimeOfDay.DAY;}
            if(-srcY%2136 >= 1068  ){time = TimeOfDay.NIGHT;}
        }



        batch.draw(textures, x, y, originX, originY, width, heigth, scaleX, scaleY, rotation, srcX, srcY, textures.getWidth(), textures.getHeight(), flipX, flipY);


    }

    public long getDuree() {
        return duree;
    }
}



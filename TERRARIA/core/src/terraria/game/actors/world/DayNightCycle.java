package terraria.game.actors.world;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class DayNightCycle extends Actor {

    OrthographicCamera camera;

    private TimeOfDay timeOfDay;
    private final Texture texture;
    private boolean nightFilter;

    float x, y, width, heigth;
    int srcY;

    private static float dayTimer;
    private final float dayLength = 600; //durée d'un jour / d'une nuit (en seconde)


    public enum TimeOfDay {
        DAY,
        NIGHT,
        SUNSET,
        SUNRISE;
    }

    public DayNightCycle(Texture texture, boolean nightFilter, int srcY, float dayTimer) {
        this.texture = texture;
        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        this.nightFilter = nightFilter;

        this.srcY = srcY;
        if (srcY == 0) timeOfDay = TimeOfDay.DAY;
        else if (srcY == 1068) timeOfDay = TimeOfDay.NIGHT;
        else if (srcY < 1068) timeOfDay = TimeOfDay.SUNSET;
        else if (srcY > 1068) timeOfDay = TimeOfDay.SUNRISE;

        this.dayTimer = dayTimer;
    }


    /**
     * mise à jour de la camera
     *
     * @param camera
     * @param stage
     */
    public void update(float delta, OrthographicCamera camera, Stage stage) {

        this.camera = camera;
        Vector3 vec = camera.position;
        x = vec.x - stage.getViewport().getScreenWidth() / 2;
        y = vec.y - stage.getViewport().getScreenHeight() / 2;
        width = stage.getViewport().getScreenWidth();
        heigth = stage.getViewport().getScreenHeight() * 6;

        dayTimer += delta;

        if(dayTimer >= dayLength) {

            srcY = (srcY + 1) % 2136;

            if (srcY == 0 /*day coordinate*/ || srcY == 1068 /*night coordinate*/) dayTimer = 0;
            else if (srcY < 1068) timeOfDay = TimeOfDay.SUNSET;
            else if (srcY > 1068) timeOfDay = TimeOfDay.SUNRISE;

        } else {

            if(srcY < 1068 ) timeOfDay = TimeOfDay.DAY;
            if(srcY >= 1068  ) timeOfDay = TimeOfDay.NIGHT;

        }


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

        if (nightFilter) {

            int srcFunc = batch.getBlendSrcFunc();
            int dstFunc = batch.getBlendDstFunc();
            batch.enableBlending();
            batch.setBlendFunction(Gdx.gl20.GL_ZERO, Gdx.gl20.GL_SRC_COLOR);
            batch.draw(texture, x, y, 0, 0, width, heigth, 1, 1, 0, 0, -srcY, texture.getWidth(), texture.getHeight(), false, false);
            batch.setBlendFunction(srcFunc , dstFunc);

        } else {

            batch.draw(texture, x, y, 0, 0, width, heigth, 1, 1, 0, 0, -srcY, texture.getWidth(), texture.getHeight(), false, false);

        }

    }

    public TimeOfDay getTimeOfDay(){
        return timeOfDay;
    }
    public float getDayLength() {
        return dayLength;
    }
}



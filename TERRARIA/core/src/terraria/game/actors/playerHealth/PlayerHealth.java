package terraria.game.actors.playerHealth;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.Player;

public class PlayerHealth extends Actor {

    private static int MAXHEALTH = 10;

    private Player player;
    double health;  //0 = death
    TextureRegion[][] heart;
    int ScreenX, ScreenY,ScreenWidth,ScreenHeigth;

    public int  width = 35, height = 35;

    public PlayerHealth(Stage stage, TerrariaGame game){
        this.health = MAXHEALTH;
        heart =  TextureRegion.split(game.getAssetManager().get("heart.png", Texture.class), width, height);

    }

    public void ApplyDamage(Damage damage) {
        health = damage.getNumberOfdamage();
    }

    public void update(Camera camera, Stage stage){
        Vector3 vec = camera.position;
        ScreenX =  (int)vec.x -  stage.getViewport().getScreenWidth()/2;;
        ScreenY = (int)vec.y -  stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeigth = stage.getViewport().getScreenHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {


        for (int i = 0; i < (int)health; i++){
            batch.draw(heart[0][0], (ScreenX) + width *  i + (width/2), ScreenY + ScreenHeigth - (height + height/2));
        }

        if ((1 % health) == 0.25) {
            batch.draw(heart[0][3], ScreenX + width * (int)health + (width/2), ScreenY - (height + height/2));
        } else if ((1 % health) == 0.5) {
            batch.draw(heart[0][2], ScreenX + width * (int)health + (width/2), ScreenY - (height + height/2));
        } else if ((1 % health) == 0.75) {
            batch.draw(heart[0][1], ScreenX + width * (int)health + (width/2), ScreenY - (height + height/2));
        }

        for (int i = (int)health + 1; i <= MAXHEALTH; i++){
            batch.draw(heart[0][4], ScreenX + width * ((int)health + i) + (width/2), ScreenY - (height + height/2));
        }

    }

}

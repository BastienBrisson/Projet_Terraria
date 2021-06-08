package terraria.game.actors.entities.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.EntityLoader;
import terraria.game.actors.world.GeneratorMap.MapLoader;
import terraria.game.screens.MainMenuScreen;

public class PlayerHealth {


    public double health;  //0 = death
    public TextureRegion[][] heart;
    float ScreenX, ScreenY,ScreenWidth,ScreenHeigth;

    public int  width = 35, height = 35;

    public PlayerHealth( TerrariaGame game,TextureRegion[][] heart, double health ){
        this.health = health;
        this.heart = heart;

    }

    public void ApplyDamage(double damage) {
        double newHealth = health - Math.round(damage*4f)/4f;
        health = (newHealth > 0) ? newHealth : 0;
        //System.out.println(damage+" damage -> health = "+health);
    }

    public void update(Camera camera, Stage stage){

        Vector3 vec = camera.position;

        ScreenX =  vec.x -  stage.getViewport().getScreenWidth()/2;;
        ScreenY = vec.y -  stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeigth = stage.getViewport().getScreenHeight();

    }


    public void draw(Batch batch, float parentAlpha) {

        for (int i = 0; i < (int)health; i++){
            batch.draw(heart[0][0], (ScreenX) + width *  i + (width/2), ScreenY + ScreenHeigth - (height + height/2));
        }

        if(health != Player.MAXHEALTH) {

            if ((health % 1) == 0.25) {
                batch.draw(heart[0][3], ScreenX + width * ((int) health) + (width / 2), ScreenY + ScreenHeigth - (height + height / 2));
            } else if ((health % 1) == 0.5) {
                batch.draw(heart[0][2], ScreenX + width * ((int) health) + (width / 2), ScreenY + ScreenHeigth - (height + height / 2));
            } else if ((health % 1) == 0.75) {
                batch.draw(heart[0][1], ScreenX + width * ((int) health) + (width / 2), ScreenY + ScreenHeigth - (height + height / 2));
            } else {
                batch.draw(heart[0][4], ScreenX + width * ((int) health) + (width / 2), ScreenY + ScreenHeigth - (height + height / 2));
            }

            for (int i = (int) health + 1; i < Player.MAXHEALTH; i++) {
                batch.draw(heart[0][4], ScreenX + width * i + (width / 2), ScreenY + ScreenHeigth - (height + height / 2));
            }
        }

    }

}

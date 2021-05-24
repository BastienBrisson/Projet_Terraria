package terraria.game.actors.Inventory;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import terraria.game.TerrariaGame;


public class ItemsGraphic extends Actor {

    TextureRegion[][] itemsTexture;
    Items item;
    float ScreenX, ScreenY,ScreenWidth,ScreenHeight;
    public int  width = 50, height = 50;
    public static final int SLOTINVENTORYBAR = 10;
    boolean menu;

    public ItemsGraphic(TerrariaGame game, boolean menu, Items item) {
        this.menu = menu;
        this.item = item;
        itemsTexture = TextureRegion.split(game.getAssetManager().get("inventory/itemsInventory.png", Texture.class), width, height);
    }

    public void update(Camera camera, Stage stage, boolean menu){
        Vector3 vec = camera.position;
        ScreenX =  vec.x + stage.getViewport().getScreenWidth()/2 - SLOTINVENTORYBAR * width;
        ScreenY = vec.y - stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeight = stage.getViewport().getScreenHeight();
        this.menu = menu;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (item.col < 1)
            batch.draw(itemsTexture[0][item.getIdTile()], ScreenX + width *  item.emplacement - (width/2), ScreenY + ScreenHeight - (item.col*height+height + height/2));
        if (menu) {
            batch.draw(itemsTexture[0][item.getIdTile()], ScreenX + width *  item.emplacement - (width/2), ScreenY + ScreenHeight - (item.col*height+height + height/2));
        }
    }
}

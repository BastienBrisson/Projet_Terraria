package terraria.game.actors.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import terraria.game.TerrariaGame;


public class ItemsGraphic extends Actor {

    public TextureRegion[][] itemsTexture;
    public Items item;
    float ScreenX, ScreenY,ScreenWidth,ScreenHeight;
    public int  width = 50, height = 50;
    public static final int SLOTINVENTORYBAR = 10;
    boolean menu;
    private Inventory inventory;
    private DragAndDrop dragAndDrop;
    private boolean mooving = false;

    public ItemsGraphic(TerrariaGame game, boolean menu, final Items item, Inventory inventory, DragAndDrop dragAndDrop) {
        this.menu = menu;
        this.item = item;
        this.inventory = inventory;
        itemsTexture = TextureRegion.split(game.getAssetManager().get("inventory/itemsInventory.png", Texture.class), width, height);

        this.dragAndDrop = dragAndDrop;
        dragAndDrop.addSource(new DragAndDrop.Source(this) {
            final DragAndDrop.Payload payload = new DragAndDrop.Payload();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                ItemsGraphic items = (ItemsGraphic)event.getListenerActor();
                payload.setDragActor(getActor());
                payload.setObject(getActor());
                items.setMoving(true);
                return payload;
            }
        });
        dragAndDrop.addTarget(new DragAndDrop.Target(this) {
            public boolean drag (DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                //getActor().setColor(Color.GREEN);
                System.out.println("deplacement");
                return true;
            }

            public void reset (DragAndDrop.Source source, DragAndDrop.Payload payload) {
                System.out.println("reset");
            }

            public void drop (DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                System.out.println("drop");
                ItemsGraphic items = (ItemsGraphic) payload.getObject();
                int IdTmp = item.getIdTile();
                int amountTmp = item.getAmount();
                setItem(items.item);
                items.item.setIdTile(IdTmp);
                items.item.setAmount(amountTmp);
                items.setMoving(false);
            }
        });
    }

    public void update(Camera camera, Stage stage, boolean menu){
        Vector3 vec = camera.position;
        ScreenX =  vec.x + stage.getViewport().getScreenWidth()/2 - SLOTINVENTORYBAR * width;
        ScreenY = vec.y - stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeight = stage.getViewport().getScreenHeight();
        this.menu = menu;
        setBounds(ScreenX + width *  item.emplacement - (width/2), ScreenY + ScreenHeight - (item.col*height+height + height/2), 50, 50);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (mooving) {
            batch.draw(itemsTexture[0][item.getIdTile()], Gdx.input.getX(),  - Gdx.input.getY());
        } else {
            if (item.col < 1)
                batch.draw(itemsTexture[0][item.getIdTile()], ScreenX + width *  item.emplacement - (width/2), ScreenY + ScreenHeight - (item.col*height+height + height/2));
            if (menu) {
                batch.draw(itemsTexture[0][item.getIdTile()], ScreenX + width *  item.emplacement - (width/2), ScreenY + ScreenHeight - (item.col*height+height + height/2));
            }
        }

    }

    public void setItem(Items items) {
        this.item.setIdTile(items.getIdTile());
        this.item.setAmount(items.getAmount());
    }

    public void setMoving(boolean mooving) {
        this.mooving = mooving;
    }

    public boolean isMooving() {
        return mooving;
    }

}

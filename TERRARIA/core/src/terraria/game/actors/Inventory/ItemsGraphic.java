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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import terraria.game.TerrariaGame;


public class ItemsGraphic extends Actor {

    public TextureRegion[][] itemsTexture;
    public TextureRegion[][] hoverTexture;
    public Items item;
    float ScreenX, ScreenY,ScreenWidth,ScreenHeight;
    public int  width = 50, height = 50;
    public static final int SLOTINVENTORYBAR = 10;
    boolean menu;
    private Inventory inventory;
    private DragAndDrop dragAndDrop;
    private boolean mooving = false;
    private boolean hover;
    Vector3 cam;

    public ItemsGraphic(TerrariaGame game, boolean menu, final Items item, Inventory inventory, final DragAndDrop dragAndDrop) {
        this.menu = menu;
        this.item = item;
        this.inventory = inventory;
        hoverTexture = TextureRegion.split(game.getAssetManager().get("inventory/hover.png", Texture.class), width, height);
        itemsTexture = TextureRegion.split(game.getAssetManager().get("inventory/itemsInventory.png", Texture.class), width, height);
        this.dragAndDrop = dragAndDrop;
        dragAndDrop.addSource(new DragAndDrop.Source(this) {
            DragAndDrop.Payload payload = new DragAndDrop.Payload();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                ItemsGraphic items = (ItemsGraphic)event.getListenerActor();
                payload.setDragActor(getActor());
                payload.setObject(getActor());
                items.setMoving(true);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                super.dragStop(event, x, y, pointer, payload, target);
                ItemsGraphic items = (ItemsGraphic)event.getListenerActor();
                items.setMoving(false);
            }
        });
        dragAndDrop.addTarget(new DragAndDrop.Target(this) {
            public boolean drag (DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                setHover(true);
                return true;
            }

            public void reset (DragAndDrop.Source source, DragAndDrop.Payload payload) {
                ItemsGraphic items = (ItemsGraphic) payload.getObject();
                setHover(false);
            }

            public void drop (DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                ItemsGraphic items = (ItemsGraphic) payload.getObject();
                int IdTmp = item.getIdTile();
                int amountTmp = item.getAmount();
                setItem(items.item);
                items.item.setIdTile(IdTmp);
                items.item.setAmount(amountTmp);
            }
        });
    }

    public void update(Camera camera, Stage stage, boolean menu){
        cam = camera.position;
        ScreenX =  cam.x + stage.getViewport().getScreenWidth()/2 - SLOTINVENTORYBAR * width;
        ScreenY = cam.y - stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeight = stage.getViewport().getScreenHeight();
        this.menu = menu;
        setBounds(ScreenX + width *  item.emplacement - (width/2), ScreenY + ScreenHeight - (item.col*height+height + height/2), 50, 50);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (hover) {
            batch.draw(hoverTexture[0][0], ScreenX + width *  item.emplacement - (width/2), ScreenY + ScreenHeight - (item.col*height+height + height/2));
        }
        if (mooving) {
            batch.draw(itemsTexture[0][item.getIdTile()], cam.x-ScreenWidth/2+Gdx.input.getX(),  cam.y+ScreenHeight/2-Gdx.input.getY());
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

    public void setHover(boolean hover) {
        this.hover = hover;
    }
}

package terraria.game.actors.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import terraria.game.TerrariaGame;


public class ItemsGraphic extends Actor {

    private TextureRegion[][] itemsTexture;                  //Texture des items de l'inventaire
    private Items item;                                      //L'item qui est associé a ce graphic
    private float OriginX, OriginY,ScreenWidth,ScreenHeight;
    private static final int SLOTINVENTORYBAR = 10;
    private Inventory inventory;
    private boolean moving;                                 //Si l'item est en train d'être déplacé
    private boolean hover;                                  //Si l'item est survolé en drag & drop
    private Vector3 cam;
    private BitmapFont font;                            //Police d'écriture
    private int x;                   //Le numéro de la ligne ou est placé l'item dans l'affichage l'inventaire
    private int y;                   //Le numéro de la colonne ou est placé l'item dans l'affichage de l'inventaire
    private boolean craftableItem;

    public ItemsGraphic(TerrariaGame game, final Items item, final Inventory inventory, DragAndDrop dragAndDrop) {
        this.item = item;
        this.inventory = inventory;
        this.moving = false;
        this.craftableItem = false;
        this.font = new BitmapFont();
        if (item.getNum() >= 50) {
            this.x = 1;
            this.y = item.getNum() - 50;
            craftableItem = true;
        } else {
            //A partir du numéro de l'item il calcul sa position X et Y dans l'affichage de l'inventaire
            this.y = this.item.getNum();
            this.x = 0;
            while (this.y > 9) {
                this.y = this.y - 10;
                this.x++;
            }
        }

        this.itemsTexture = TextureRegion.split(game.getAssetManager().get("inventory/itemsInventory.png", Texture.class), inventory.getWidthTile(), inventory.getHeightTile());
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
                ItemsGraphic itemOrigin = (ItemsGraphic) payload.getObject();
                if (itemOrigin.isCraftableItem() && item.getIdTile() != 0) {
                    return false;
                }
                return true;
            }

            public void reset (DragAndDrop.Source source, DragAndDrop.Payload payload) {
                setHover(false);
            }

            public void drop (DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                ItemsGraphic itemOrigin = (ItemsGraphic) payload.getObject();
                if (itemOrigin.craftableItem) {
                    inventory.costUpdate(itemOrigin);
                }
                if (itemOrigin.getItem().getIdTile() == item.getIdTile()) {
                    item.addAmount(itemOrigin.getItem().getAmount());
                    itemOrigin.getItem().setIdTile(0);
                    itemOrigin.getItem().setAmount(0);
                } else {
                    int IdTmp = item.getIdTile();
                    int amountTmp = item.getAmount();
                    setItem(itemOrigin.item);
                    itemOrigin.item.setIdTile(IdTmp);
                    itemOrigin.item.setAmount(amountTmp);
                }
                inventory.updateCraft();

            }
        });
    }

    public void update(Camera camera, Stage stage, boolean menu){
        cam = camera.position;
        OriginX =  cam.x + stage.getViewport().getScreenWidth()/2 - SLOTINVENTORYBAR * inventory.getWidthTile();
        OriginY = cam.y - stage.getViewport().getScreenHeight()/2;
        ScreenWidth = stage.getViewport().getScreenWidth();
        ScreenHeight = stage.getViewport().getScreenHeight();
        this.inventory.setInventoryShow(menu);
        if (!craftableItem) {
            setBounds(OriginX + inventory.getWidthTile() *  y - (inventory.getWidthTile()/2), OriginY + ScreenHeight - (x*inventory.getHeightTile()+inventory.getHeightTile() + inventory.getHeightTile()/2), 50, 50);
        } else {
            setBounds(inventory.getScreenX() - (inventory.getWidthTile() / 2), OriginY + ScreenHeight - (7 * inventory.getHeightTile() + inventory.getHeightTile() / 2 + this.y * inventory.getHeightTile()), 50, 50);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!craftableItem) {
            if (moving) {
                batch.draw(itemsTexture[0][item.getIdTile()], cam.x-ScreenWidth/2+Gdx.input.getX(),  cam.y+ScreenHeight/2-Gdx.input.getY());
                if (item.getAmount() != 0)
                    font.draw(batch, String.valueOf(item.getAmount()),cam.x-ScreenWidth/2+Gdx.input.getX()+inventory.getWidthTile()/4+inventory.getWidthTile()/2,  cam.y+ScreenHeight/2-Gdx.input.getY()+inventory.getHeightTile()/3);
            } else {
                if (x < 1) {
                    batch.draw(itemsTexture[0][item.getIdTile()], OriginX + inventory.getWidthTile() *  y - (inventory.getWidthTile()/2), OriginY + ScreenHeight - (x*inventory.getHeightTile()+inventory.getHeightTile() + inventory.getHeightTile()/2));
                    if (item.getAmount() != 0)
                        font.draw(batch, String.valueOf(item.getAmount()), OriginX + inventory.getWidthTile() *  y+inventory.getWidthTile()/4, OriginY + ScreenHeight - (x*inventory.getHeightTile() + inventory.getHeightTile()/3 + inventory.getHeightTile()));
                }
                if (inventory.isInventoryShow()) {
                    batch.draw(itemsTexture[0][item.getIdTile()], OriginX + inventory.getWidthTile() *  y - (inventory.getWidthTile()/2), OriginY + ScreenHeight - (x*inventory.getHeightTile()+inventory.getHeightTile() + inventory.getHeightTile()/2));
                    if (item.getAmount() != 0)
                        font.draw(batch, String.valueOf(item.getAmount()), OriginX + inventory.getWidthTile() *  y+inventory.getWidthTile()/4, OriginY + ScreenHeight - (x*inventory.getHeightTile() + inventory.getHeightTile()/3 + inventory.getHeightTile()));
                }
            }
        } else {
            if (inventory.isInventoryShow()) {
                if (moving) {
                    batch.draw(itemsTexture[0][item.getIdTile()], cam.x - ScreenWidth / 2 + Gdx.input.getX(), cam.y + ScreenHeight / 2 - Gdx.input.getY());
                    if (item.getAmount() != 0)
                        font.draw(batch, String.valueOf(item.getAmount()),cam.x-ScreenWidth/2+Gdx.input.getX()+inventory.getWidthTile()/4+inventory.getWidthTile()/2,  cam.y+ScreenHeight/2-Gdx.input.getY()+inventory.getHeightTile()/3);
                } else {
                    batch.draw(itemsTexture[0][item.getIdTile()], inventory.getScreenX() - (inventory.getWidthTile() / 2), OriginY + ScreenHeight - (7 * inventory.getHeightTile() + inventory.getHeightTile() / 2 + this.y * inventory.getHeightTile()));
                    if (item.getAmount() != 0)
                        font.draw(batch, String.valueOf(item.getAmount()), inventory.getScreenX()  + inventory.getWidthTile()/4, OriginY + ScreenHeight - (7 * inventory.getHeightTile() + inventory.getHeightTile() / 2 + this.y * inventory.getHeightTile()) + inventory.getHeightTile()/4);
                }
            }
        }
    }

    public void setItem(Items items) {
        this.item.setIdTile(items.getIdTile());
        this.item.setAmount(items.getAmount());
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }
    public boolean isHover() {return hover;}

    public int getXPosition() {
        return x;
    }

    public void setXPosition(int x) {
        this.x = x;
    }

    public int getYPosition() {
        return y;
    }

    public void setYPosition(int y) {
        this.y = y;
    }

    public Items getItem() {
        return item;
    }

    public boolean isCraftableItem() {
        return craftableItem;
    }

    public void setCraftableItem(boolean craftableItem) {
        this.craftableItem = craftableItem;
    }
}

package terraria.game.actors.Inventory;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import terraria.game.TerrariaGame;
import terraria.game.actors.world.TileType;


public class Inventory extends Actor {

    public static final int SLOTINVENTORYBAR = 10;
    public static final int SIZEINVENTORY = 50;
    private static int currentItems = 0;
    private ArrayList<Items> inventory; // inventaire complet
    private ArrayList<ItemsGraphic> itemsGraphic;
    public Boolean inventoryShow = false;
    public TerrariaGame game;
    TextureRegion[][] slot;
    public BitmapFont font;
    int nbItems;

    float ScreenX, ScreenY,ScreenWidth,ScreenHeight;
    public int  width = 50, height = 50;

    public Inventory(TerrariaGame game) {
        this.game = game;
        this.inventory = new ArrayList<Items>();
        this.itemsGraphic = new ArrayList<ItemsGraphic>();
        this.font = new BitmapFont();
        for (int i = 0; i < SIZEINVENTORY; i++) {
            inventory.add(new Items(game, i));
            itemsGraphic.add(new ItemsGraphic(game,inventoryShow, inventory.get(i)));
        }

        slot = TextureRegion.split(game.getAssetManager().get("inventory/slot.png", Texture.class), width, height);
    }

    public void update(Camera camera, Stage stage){
        Vector3 vec = camera.position;
        ScreenX =  vec.x + stage.getViewport().getScreenWidth()/2 - SLOTINVENTORYBAR * width;
        ScreenY = vec.y - stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeight = stage.getViewport().getScreenHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        nbItems = 0;
        //On dessine les slots de la barre d'inventaire
        for (int i = 0; i < SLOTINVENTORYBAR; i++){
            if (currentItems == i) {
                batch.draw(slot[0][1], ScreenX + width *  i - (width/2), ScreenY + ScreenHeight - (height + height/2));
            } else {
                batch.draw(slot[0][0], ScreenX + width *  i - (width/2), ScreenY + ScreenHeight - (height + height/2));
            }
            if (this.inventory.get(nbItems).getAmount() != 0)
                font.draw(batch, String.valueOf(this.inventory.get(nbItems).getAmount()),ScreenX + width *  i +width/4, ScreenY + ScreenHeight - (height/3 + height));
            nbItems++;
        }

        if (inventoryShow) {
            for (int i = 1; i < 5; i++) {
                for (int j = 0; j < SLOTINVENTORYBAR; j++) {
                    batch.draw(slot[0][0], ScreenX + width * j - (width / 2), ScreenY + ScreenHeight - (i*height + height + height / 2));
                    if (this.inventory.get(nbItems).getAmount() != 0)
                        font.draw(batch, String.valueOf(this.inventory.get(nbItems).getAmount()),ScreenX + width *  j+width/4, ScreenY + ScreenHeight - (i*height + height/3 + height));
                    nbItems++;
                }
            }
        }


    }

    /**
     * Ajoute un élément à un slot, s'il existe un slot contenant déjà un élément
     * du même type et qu'il y a moins de 64 éléments dans ce slot. Dans le cas contraire
     * on va chercher s'il existe un slot vide pouvant acceuilir cet élement. 
     * @param idTile
     * @return
     */
    public boolean addTileInInventory(int idTile) {
        for(Items t : inventory) {
            if(idTile == t.getIdTile() && t.getAmount() < 64) {
                t.incrAmount();
                return true;
            }
        }

        for(Items t2 : inventory) {
            if(t2.getIdTile() == 0) {
                t2.setIdTile(idTile);
                t2.incrAmount();
                return true;
            }
        }

        return false;
    }

    /**
     * Enlève un élément de l'inventaire et supprime complètement l'élément du slot
     * quand le nombre d'élement descend à 0.
     */
    public void delTileInInventory() {
        Items currentSlot = inventory.get(currentItems);
        if(currentSlot.getAmount() == 1) {
            currentSlot.lastElement();
        } else {
            currentSlot.decrAmount();
        }
        //updateInventoryBar();
    }

    /**
     * Permet de mettre à jour les éléments de la barre d'inventaire
     * à partir de l'inventaire complet 
     */
    /*public void updateInventoryBar() {
        TileSlot slot;
        for(int i = 0; i < 10; i++) {
            slot = inventory.get(i);
            if(!slot.compareTo(inventoryBar.get(i))) {
                inventoryBar.get(i).setCoordinate(slot.getX(), slot.getY());
                inventoryBar.get(i).setTileStack(slot.getTileStack());
            }
        }
    }*/

    public void fillInventory(ArrayList<Items> inv) {
        int indice = 0;
        if (inv != null) {
            for(Items i : inv) {
                this.inventory.set(indice, i);
                this.getGraphicItems().set(indice, new ItemsGraphic(game, inventoryShow, this.inventory.get(indice)));
                indice++;
            }
        }
    }

    public int getCurrentItems() {
        return currentItems;
    }

    public static void setCurrentItems(int tile) {
        currentItems = tile;
    }

    public ArrayList<Items> getInventory() {
        return inventory;
    }

    public ArrayList<ItemsGraphic> getGraphicItems() {
        return itemsGraphic;
    }

    public boolean isInventoryOpen() {
        return this.inventoryShow;
    }
}
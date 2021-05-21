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
    BitmapFont font;

    TextureRegion[][] slot;
    TextureRegion[][] items;

    float ScreenX, ScreenY,ScreenWidth,ScreenHeight;
    public int  width = 50, height = 50;

    public Inventory(Stage stage, TerrariaGame game) {
        font = new BitmapFont();
        this.inventory = new ArrayList<Items>();

        for (int i = 0; i < 50; i++) {
            inventory.add(new Items());
        }

        slot = TextureRegion.split(game.getAssetManager().get("inventory/slot.png", Texture.class), width, height);
        items = TextureRegion.split(game.getAssetManager().get("inventory/itemsInventory.png", Texture.class), width, height);
        inventory.get(1).changeContent(TileType.GRASS, 20);
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

        //On dessine les slots de la barre d'inventaire
        for (int i = 0; i < SLOTINVENTORYBAR; i++){
            if (currentItems == i) {
                batch.draw(slot[0][1], ScreenX + width *  i - (width/2), ScreenY + ScreenHeight - (height + height/2));
            } else {
                batch.draw(slot[0][0], ScreenX + width *  i - (width/2), ScreenY + ScreenHeight - (height + height/2));
            }
        }

        //On dessine les items dans la barre d'inventaire
        for (int i = 0; i < SLOTINVENTORYBAR; i++){
            batch.draw(items[0][inventory.get(i).getIdTile()], ScreenX + width *  i - (width/2), ScreenY + ScreenHeight - (height + height/2));
            if (inventory.get(i).getAmount() != 0)
                font.draw(batch, String.valueOf(inventory.get(i).getAmount()),ScreenX + width *  i, ScreenY + ScreenHeight - (height/7 + height));
        }
    }

    /**
     * Ajoute un élément à un slot, s'il existe un slot contenant déjà un élément
     * du même type et qu'il y a moins de 64 éléments dans ce slot. Dans le cas contraire
     * on va chercher s'il existe un slot vide pouvant acceuilir cet élement. 
     * @param tile
     * @return
     */
    public boolean addTileInInventory(TileType tile) {
        int index = -1, i = 0;
        for(Items t : inventory) {
            TileType currentInventoryTile = TileType.getTileTypeById(t.getIdTile());
            if((currentInventoryTile.getName().compareTo(tile.getName()) == 0) && t.getAmount() < 64) {
                t.incrAmount();
                return true;
            } else if(t.getIdTile() == 0) {
                index = i;
            }
            i++;
        }

        if(index >= 0) {
            Items item = inventory.get(index);
            item.changeContent(tile, 1);
            inventory.set(index, item);
            return true;
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

    public static int getCurrentItems() {
        return currentItems;
    }

    public static void setCurrentItems(int tile) {
        currentItems = tile;
    }

    public ArrayList<Items> getInventory() {
        return inventory;
    }

}
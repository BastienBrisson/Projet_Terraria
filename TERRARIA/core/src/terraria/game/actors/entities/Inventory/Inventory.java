package terraria.game.actors.entities.Inventory;

import java.util.ArrayList;
import terraria.game.actors.world.TileType;


public class Inventory {

    public static final int SLOTINVENTORYBAR = 10;
    private static int currentTile = 0;
    private ArrayList<TileSlot> inventory; // inventaire complet
    private ArrayList<TileSlot> inventoryBar; // barre d'inventaire

    public Inventory() {
        this.inventory = new ArrayList<TileSlot>();
        this.inventoryBar = new ArrayList<TileSlot>();
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
        for(TileSlot t : inventory) {
            TileType currentInventoryTile = TileType.getTileTypeById(t.getTileStack().getIdTile()); 
            if((currentInventoryTile.getName().compareTo(tile.getName()) == 0) && t.getTileStack().getAmount() < 64) {
                t.addTileOnSlot();
                return true;
            } else if(t.getTileStack() == null) {
                index = i;
            }
            i++;
        }

        if(index >= 0) {
            TileSlot slot = inventory.get(index);
            TilesStack stack = new TilesStack(tile);
            slot.setTileStack(stack);
            inventory.set(index, slot);
            return true;
        }

        return false;
    }

    /**
     * Enlève un élément de l'inventaire et supprime complètement l'élément du slot
     * quand le nombre d'élement descend à 0.
     */
    public void delTileInInventory() {
        TileSlot currentSlot = inventory.get(currentTile);
        if(currentSlot.getTileStack().getAmount() == 1) {
            currentSlot.removeTileStack();
        } else {
            currentSlot.getTileStack().decrAmount();
        }
        updateInventoryBar();
    }

    /**
     * Permet de mettre à jour les éléments de la barre d'inventaire
     * à partir de l'inventaire complet 
     */
    public void updateInventoryBar() {
        TileSlot slot;
        for(int i = 0; i < 10; i++) {
            slot = inventory.get(i);
            if(!slot.compareTo(inventoryBar.get(i))) {
                inventoryBar.get(i).setCoordinate(slot.getX(), slot.getY());
                inventoryBar.get(i).setTileStack(slot.getTileStack());
            }
        }
    }

    public static int getCurrentTile() {
        return currentTile;
    }

    public static void setCurrentTile(int tile) {
        currentTile = tile;
    }

    public ArrayList<TileSlot> getInventory() {
        return inventory;
    }

    public ArrayList<TileSlot> getInventoryBar() {
        return inventoryBar;
    }
}
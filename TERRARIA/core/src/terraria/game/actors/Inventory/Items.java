package terraria.game.actors.Inventory;

import terraria.game.actors.world.TileType;

public class Items {


    public static final int MAXELEMENT = 65;
    private int amount;
    private int idTile;

    public Items() {
        this.amount = 0;
        this.idTile = 0;
    }

    public Items(TileType tile) {
        this.amount = 1;
        this.idTile = tile.getId();
    }

    public Items(TileType tile, int amount) {
        this.amount = amount;
        this.idTile = tile.getId();
    }

    public int getAmount() {
        return this.amount;
    }

    public int getIdTile() {
        return this.idTile;
    }

    public void incrAmount() {
        this.amount++;
    }

    public void decrAmount() {
        this.amount--;
    }

    public void lastElement() {
        this.amount--;
        this.idTile = 0;
    }

    public void changeContent(TileType tile, int amount) {
        this.amount = amount;
        this.idTile = tile.getId();
    }

    public boolean compareTo(Items item) {
        if(this.amount == item.amount && this.idTile == item.idTile) {
            return true;
        }
        return false;
    }
}

package terraria.game.actors.entities.Inventory;

import terraria.game.actors.world.TileType;

public class TilesStack {

    private int amount;
    private int idTile;

    public TilesStack(TileType tile) {
        this.amount = 1;
        this.idTile = tile.getId();
    }

    public TilesStack(int amount, TileType tile) {
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

    public void changeContent(TileType tile, int amount) {
        this.amount = amount;
        this.idTile = tile.getId();
    }

    public boolean compareTo(TilesStack stack) {
        if(this.amount == stack.amount && this.idTile == stack.idTile) {
            return true;
        }
        return false;
    }
}
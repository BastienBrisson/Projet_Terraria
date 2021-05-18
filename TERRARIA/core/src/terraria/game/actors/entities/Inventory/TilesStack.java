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

    public void changeSlot(TileType tile, int amount) {
        this.amount = amount;
        this.idTile = tile.getId();
    }
}
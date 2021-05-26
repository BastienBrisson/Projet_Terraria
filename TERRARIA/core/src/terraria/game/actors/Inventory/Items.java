package terraria.game.actors.Inventory;

import terraria.game.TerrariaGame;
import terraria.game.actors.world.TileType;

public class Items {


    public static final int MAXELEMENT = 65;

    public static final int SIZEINVENTORY = 50;
    private int amount;
    private int idTile;
    private boolean inTransition= false;

    int num;
    int col;
    int emplacement;

    public Items(TerrariaGame game, int num) {
        this.amount = 0;
        this.idTile = 0;
        this.num = num;
        emplacement = num;
        col = 0;
        while (emplacement > 9) {
            emplacement = emplacement - 10;
            col++;
        }


    }

    public Items(TerrariaGame game, TileType tile, int num) {
        this.amount = 1;
        this.idTile = tile.getId();
        this.num = num;
        emplacement = num;
        col = 0;
        while (emplacement > 10) {
            emplacement = emplacement - 10;
            col++;
        }
    }

    public Items(TerrariaGame game, TileType tile, int amount, int num) {
        this.amount = amount;
        this.idTile = tile.getId();
        this.num = num;
        emplacement = num;
        col = 0;
        while (emplacement > 10) {
            emplacement = emplacement - 10;
            col++;
        }
    }

    public Items(){
        this.amount = 0;
        this.idTile = 0;
    }


    public int getAmount() {
        return this.amount;
    }

    public int getIdTile() {
        return this.idTile;
    }

    public void setIdTile(int id) {
         this.idTile = id;
    }

    public void incrAmount() {
        this.amount++;
    }

    public void decrAmount() {
        if (this.amount > 0) {
            this.amount--;
            if (amount==0)
                this.setIdTile(0);
        } else {
            this.setIdTile(0);
        }
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

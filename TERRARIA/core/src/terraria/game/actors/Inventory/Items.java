package terraria.game.actors.Inventory;

public class Items {

    private int idTile;             //l'id de ce item
    private int amount;             //le quantitié de ce item
    private int num;                //Le numéro de l'item dans l'inventaire


    public Items(int num) {
        this.idTile = 0;
        this.amount = 0;
        this.num = num;
    }

    public Items(){
        this.idTile = 0;
        this.amount = 0;
        this.num = 0;
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

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }
}

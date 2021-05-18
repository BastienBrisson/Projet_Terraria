package terraria.game.actors.entities.Inventory;


public class TileSlot {
    
    public static final int SLOTSIZE = 50;
    public static final int MAXAMOUNT = 65;

    private int x, y;
    private TilesStack tileStack;

    public TileSlot(int x, int y, TilesStack tileStack) {
        this.x = x;
        this.y = y;
        this.tileStack = tileStack;
    }

    /*public void render(float delta) {

    }*/

    public TilesStack getTileStack() {
        return this.tileStack;
    }

    public void setCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setTileStack(TilesStack tileStack) {
        this.tileStack = tileStack;
    }

    public void removeTileStack() {
        this.tileStack = null;
    }

    public void addTileOnSlot() {
        this.tileStack.incrAmount();
    }

    public void delTileOnSlot() {
        this.tileStack.decrAmount();
    }

    public boolean compareTo(TileSlot slot) {
        if(this.x == slot.x && this.y == slot.y && this.tileStack.compareTo(slot.tileStack)) {
            return true;
        }
        return false;
    }
}
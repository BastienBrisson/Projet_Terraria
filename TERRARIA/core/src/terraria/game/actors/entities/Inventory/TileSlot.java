package terraria.game.actors.entities.Inventory;


public class TileSlot {
    
    public static final int SLOTSIZE = 50;

    private int x, y;
    private TilesStack tileStack;

    public TileSlot(int x, int y, TilesStack tileStack) {
        this.x = x;
        this.y = y;
        this.tileStack = tileStack;
    }

    /*public void render(float delta) {

    }*/

    public TilesStack getTilesStack() {
        return this.tileStack;
    }

}
package terraria.game.actors.entities;
import terraria.game.actors.Inventory.Items;

import java.util.ArrayList;

public class EntitySnapshot {

    public String type;
    public float x, y;
    public double health;
    public ArrayList<Items> inventory;

    public EntitySnapshot() {}

    public EntitySnapshot( String type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}

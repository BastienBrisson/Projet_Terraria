package terraria.game.actors.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import terraria.game.TerrariaGame;
import terraria.game.actors.world.GameMap;

public class Entity extends Actor {

    public Vector2 pos;
    protected EntityType type;
    protected float velocityY = 0;
    protected TerrariaGame game;
    protected GameMap gameMap;
    protected boolean grounded = false;
    protected boolean flipX = false;
    public double health;

    protected Array<Texture> textures;
    protected Array<Animation> animations;


    public void create (EntitySnapshot snapshot, EntityType type, GameMap gameMap, TerrariaGame game) {
        this.pos = new Vector2(snapshot.getX(), snapshot.getY());
        this.type = type;
        this.gameMap = gameMap;
        this.game = game;
        setX(pos.x); setY(pos.y); setWidth(type.width); setHeight(type.height);
    }

    public void create (int posX, int posY, EntityType type, GameMap gameMap, TerrariaGame game) {
        this.pos = new Vector2(posX,posY);
        this.type = type;
        this.gameMap = gameMap;
        this.game = game;
        setX(pos.x); setY(pos.y); setWidth(type.width); setHeight(type.height);
    }

    public void update(float deltaTime, float gravity, Camera camera, Stage stage) {
        //Apply gravity
        float newY = pos.y;
        this.velocityY += gravity * deltaTime * getWeight();

        //if (!grounded){System.out.println(velocityY);}  //test

        newY += this.velocityY * deltaTime;
        if(gameMap.doesRectCollideWithMap(pos.x, newY, (int)getWidth(), (int)getHeight())){
            if(velocityY < 0){
                this.pos.y = (float)Math.floor(pos.y);
                setY(pos.y);
                grounded = true;
            }
            this.velocityY = 0;
        }
        else{
            this.pos.y = newY;
            setY(pos.y);
            grounded = false;
        }
    }

    public EntitySnapshot getSaveSnapshot () {
        return new EntitySnapshot(type.getId(), pos.x, pos.y);
    }

    /**
     * Calcule le mouvement horizontale
     * @param amount
     * @return true si l'entité se déplace, false sinon (face à un mur)
     */
    protected boolean moveX (float amount) {
        //Check if textures face right or left (+x or -x)
        if (amount < 0) flipX = true;
        else flipX = false;

        //Check if movement possible
        float newX = this.pos.x + amount;
        if(!gameMap.doesRectCollideWithMap(newX, this.pos.y, (int)getWidth(), (int)getHeight())){
            this.pos.x = newX;
            setX(pos.x);
            return true;
        } else {
            return false;
        }
    }

    public float getX () {
        return pos.x;
    }
    public float getY () {
        return pos.y;
    }
    public boolean isGrounded() {
        return grounded;
    }
    public float getWidth() {
        return type.width;
    }
    public float getHeight() {
        return type.height;
    }
    public float getWeight() {
        return type.weight;
    }
    public EntityType getType() {
        return type;
    }
    public double getHealth() { return health; };

}

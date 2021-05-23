package terraria.game.actors.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import terraria.game.TerrariaGame;
import terraria.game.actors.world.GameMap;
import terraria.game.actors.world.TileType;
import terraria.game.screens.GameScreen;
import terraria.game.screens.LoadingScreen;

public class Entity extends Actor {

    public Vector2 pos;
    protected EntityType type;
    protected float velocityY = 0;
    protected TerrariaGame game;
    protected GameMap gameMap;
    protected boolean grounded = false;
    protected boolean flipX = false;

    protected Array<Texture> textures;
    protected Array<Animation> animations;


    public void create (EntitySnapshot snapshot, EntityType type, GameMap gameMap, TerrariaGame game) {
        this.pos = new Vector2(snapshot.getX(), snapshot.getY());
        this.type = type;
        this.gameMap = gameMap;
        this.game = game;
    }

    public void create (int posX, int posY, EntityType type, GameMap gameMap, TerrariaGame game) {
        this.pos = new Vector2(posX,posY);
        this.type = type;
        this.gameMap = gameMap;
        this.game = game;
    }


    public void update(float deltaTime, float gravity, Camera camera, Stage stage) {
        //Apply gravity
        float newY = pos.y;
        this.velocityY += gravity * deltaTime * getWeight();

        //if (!grounded){System.out.println(velocityY);}  //test

        newY += this.velocityY * deltaTime;
        if(gameMap.DoesRectCollideWithMap(pos.x, newY, (int)getWidth(), (int)getHeight())){
            if(velocityY < 0){
                this.pos.y = (float)Math.floor(pos.y);
                grounded = true;
            }
            this.velocityY = 0;
        }
        else{
            this.pos.y = newY;
            grounded = false;
        }
    }

    public EntitySnapshot getSaveSnapshot () {
        return new EntitySnapshot(type.getId(), pos.x, pos.y);
    }

    /**
     * Calcule le mouvement horizontale
     * @param amount
     */
    protected void moveX (float amount) {
        //Check if textures face right or left (+x or -x)
        if (amount < 0) flipX = true;
        else flipX = false;

        //Check if movement possible
        float newX = this.pos.x + amount;
        if(!gameMap.DoesRectCollideWithMap(newX, this.pos.y, (int)getWidth(), (int)getHeight())){
            this.pos.x = newX;
        }
    }

    public Vector2 getPos() {
        return pos;
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

    public Array<Texture> getArrayTextures(){return textures;}
    public Array<Animation> getArrayAnimations(){return animations;}


}

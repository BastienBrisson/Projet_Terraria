package terraria.game.actors.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import terraria.game.TerrariaGame;
import terraria.game.actors.world.GameMap;
import terraria.game.screens.LoadingScreen;

public class Mushroom extends Entity {

    Player target;

    private static final int SPEED = 100, JUMP_VELOCITY = 5;

    private static int state = 0;
    private static final int IDLE = 0, RUNNING = 1, JUMPING = 2;

    @Override
    public void create(EntitySnapshot snapshot, EntityType type, GameMap gameMap, TerrariaGame game) {
        super.create(snapshot, type, gameMap,game);
        init();
    }
    public void create(int posX, int posY, EntityType type, GameMap gameMap, TerrariaGame game) {
        super.create(posX,posY, type, gameMap,game);
        init();
    }

    public void init() {
        animations = new Array<>();
        for(int i = 0; i < LoadingScreen.TEXTURE_NUMBER_PLAYER ; i++){
            switch (i){
                case IDLE: animations.add(new Animation(new TextureRegion(new Texture("ennemies/mushroom"+IDLE+".png")),7 , 0.5F));break;
                case RUNNING: animations.add(new Animation(new TextureRegion(new Texture("ennemies/mushroom"+RUNNING+".png")),8 , 0.5F));break;
                case JUMPING: animations.add(new Animation(new TextureRegion(new Texture("ennemies/mushroom"+JUMPING+".png")),3 , 0.5F));break;
            }
        }
    }

    @Override
    public void update(float deltaTime, float gravity, Camera camera) {
        if (target != null) {
            float distance = target.pos.x - pos.x;
            if (distance > getWidth()) {
                //if (gameMap.getTileTypeByLocation(1, pos.x+ TileType.TILE_SIZE, pos.y).isCollidable() )

                moveX(SPEED * deltaTime);
                state = RUNNING;


            } else if (distance < -target.getWidth()) {

                moveX(-SPEED * deltaTime);
                state = RUNNING;

            } else {

                target.takeAHit(1);
                state = IDLE;

            }

        } else {
            state = IDLE;
        }

        super.update(deltaTime, gravity, camera);   //Apply gravity
    }

    @Override
    public EntitySnapshot getSaveSnapshot() {
        EntitySnapshot snapshot = super.getSaveSnapshot();
        return snapshot;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion texture;

        switch (state) {
            case JUMPING:
                texture = animations.get(JUMPING).getFrame();
                break;
            case RUNNING:
                texture = animations.get(RUNNING).getFrame();
                animations.get(RUNNING).update( Gdx.graphics.getDeltaTime());
                break;
            default :   //state == IDLE
                texture = animations.get(IDLE).getFrame();
                animations.get(IDLE).update( Gdx.graphics.getDeltaTime());
                break;
        }

        batch.draw(texture, flipX ? pos.x + getWidth() : pos.x, pos.y, flipX ? -getWidth() : getWidth(), getHeight());
    }

    public void setTarget(Player target) {
        this.target = target;
    }

}

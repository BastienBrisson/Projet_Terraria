package terraria.game.actors.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.player.Player;
import terraria.game.actors.world.GameMap;
import terraria.game.actors.world.TileType;
import terraria.game.screens.LoadingScreen;

public class Slime extends Entity {

    Player target;

    private static final int AIR_SPEED = 200, RUN_SPEED = 25, JUMP_VELOCITY = 5, RANGE = 25 * TileType.TILE_SIZE;

    private double health = 5;
    private boolean invulnerable = false, flip = false, readyToJump = false, jumping = false;
    private final float INVULNERABILITY_TIME = 0.25f, JUMP_TIME = 1;  //1f = 1sec
    private float invulnerabilityTimer = 0f, jumpTimer = 0f;



    private int state = 0;
    private static final int IDLE = 0, RUNNING = 1 ,JUMPING = 2, HIT = 3;

    @Override
    public void create(EntitySnapshot snapshot, EntityType type, GameMap gameMap, TerrariaGame game) {
        super.create(snapshot, type, gameMap,game);
        health = snapshot.health;
        init();
    }
    public void create(int posX, int posY, EntityType type, GameMap gameMap, TerrariaGame game) {
        super.create(posX,posY, type, gameMap,game);
        init();
    }

    public void init() {
        this.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y)  {
                super.clicked(event, x, y);
                takeAhit(1);
            }

        });

        animations = new Array<>();
        for(int i = 0; i < LoadingScreen.TEXTURE_NUMBER_MOBS; i++){
            switch (i){
                case IDLE: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("mobs/slime"+i+".png", Texture.class)),5 , 1F));break;
                case RUNNING: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("mobs/slime"+i+".png", Texture.class)),15 , 2F));break;
                case JUMPING: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("mobs/slime"+i+".png", Texture.class)),1 , 0.5F));break;
                case HIT: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("mobs/slime"+i+".png", Texture.class)),2 , 0.1F));break;
            }
        }
    }

    @Override
    public void update(float deltaTime, float gravity, Camera camera, Stage stage) {

        if (target != null) {

            float xDistance = target.pos.x - pos.x;
            float yDistance = target.pos.y - pos.y;

            //if player in range
            if (!invulnerable && xDistance < RANGE && xDistance > -RANGE && yDistance < RANGE && yDistance > -RANGE) {

                //only move with jumps
                if (state == JUMPING) {

                    //run toward player
                    if (xDistance > getWidth()) moveX(AIR_SPEED * deltaTime);
                    else if (xDistance < -target.getWidth()) moveX(-AIR_SPEED * deltaTime);

                    if (grounded) state = IDLE;


                } else {

                    //Time the next jump
                    if (readyToJump) {
                        this.velocityY += JUMP_VELOCITY * getWeight();
                        state = JUMPING;
                        readyToJump = false;
                    } else {
                        jumpTimer += deltaTime;
                        if (jumpTimer > JUMP_TIME) {
                            jumpTimer = 0f;
                            readyToJump = true;
                        }
                    }

                    //slowly walk if Tile over its head
                    TileType upperTile =  gameMap.getTileTypeByLocation(1, pos.x, pos.y + 32 + TileType.TILE_SIZE/2);
                    TileType upperLeftTile =  gameMap.getTileTypeByLocation(1, pos.x - TileType.TILE_SIZE, pos.y + 32 + TileType.TILE_SIZE/2);
                    TileType upperRightTile =  gameMap.getTileTypeByLocation(1, pos.x + TileType.TILE_SIZE, pos.y + 32 + TileType.TILE_SIZE/2);
                    if (grounded && ( (upperTile != null && upperTile.isCollidable()) || (upperLeftTile != null && upperLeftTile.isCollidable()) || (upperRightTile != null && upperRightTile.isCollidable()) ) ) {
                        state = RUNNING;
                        if (xDistance > getWidth()) moveX(RUN_SPEED * deltaTime);
                        else if (xDistance < -target.getWidth()) moveX(-RUN_SPEED * deltaTime);
                    }

                    //always face the player
                    if (xDistance > getWidth()) flipX = false;
                    else if (xDistance < -target.getWidth()) flipX = true;

                }

                //attack player on contact
                if (yDistance < getHeight() && yDistance > -target.getHeight() && xDistance < getWidth() && xDistance > -target.getWidth())    //player touched
                    target.takeAhit(1);

            //if mob out of rendering range
            } else if (xDistance > despawnRange || xDistance < -despawnRange || yDistance > despawnRange || yDistance < -despawnRange) {
                health = 0; //despawn

            //Player not in range, mob IDLE
            } else {
                state = IDLE;
            }
        } else {
            //No target (never supposed to happen)
            state = IDLE;
        }

        super.update(deltaTime, gravity, camera, stage);   //Apply gravity

        //Check the invulnerability frame
        if (invulnerable) {
            invulnerabilityTimer += deltaTime;
            if (invulnerabilityTimer > INVULNERABILITY_TIME) {
                invulnerabilityTimer = 0f;
                invulnerable = false;
            }
            if (flip) moveX(4*getWeight() * deltaTime);
            else moveX(-4*getWeight() * deltaTime);
        }

        //Check mob state
        if (invulnerable) state = HIT;

    }

    public void takeAhit(double damage) {
        if (!invulnerable) {
            if (health-damage <= 0) lootable = true;
            health -= damage;
            flip = flipX;
            if (grounded) this.velocityY += 3*getWeight();  //Knockback
            invulnerable = true;
        }
    }

    @Override
    public EntitySnapshot getSaveSnapshot() {
        EntitySnapshot snapshot = super.getSaveSnapshot();
        snapshot.health = health;
        return snapshot;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion texture;

        switch (state) {
            case JUMPING:
                texture = animations.get(JUMPING).getFrame();
                break;
            case HIT:
                texture = animations.get(HIT).getFrame();
                animations.get(HIT).update( Gdx.graphics.getDeltaTime());
                break;
            default :   //state == IDLE
                texture = animations.get(IDLE).getFrame();
                animations.get(IDLE).update( Gdx.graphics.getDeltaTime());
                break;
        }

        batch.draw(texture, !flipX ? pos.x + 32 : pos.x, pos.y, !flipX ? -32 : 32, 32);
    }

    public void setTarget(Player target) { this.target = target; }
    public double getHealth() { return health; }

}

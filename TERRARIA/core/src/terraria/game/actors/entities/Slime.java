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

    private static final int SPEED = 75, JUMP_VELOCITY = 6, RANGE = 25 * TileType.TILE_SIZE;

    private double health = 5;
    private boolean invulnerable = false, flip = false;
    private final float INVULNERABILITY_TIME = 0.25f;  //1f = 1sec
    private float invulnerabilityTimer = 0f;

    private int state = 0;
    private static final int IDLE = 0, RUNNING = 1, JUMPING = 2, HIT = 3;

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
                case RUNNING: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("mobs/slime"+i+".png", Texture.class)),15 , 1F));break;
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

                //run toward player
                if (xDistance > getWidth()) {
                    moveX(SPEED * deltaTime);
                    state = RUNNING;
                } else if (xDistance < -target.getWidth()) {
                    moveX(-SPEED * deltaTime);
                    state = RUNNING;
                } else {
                    if (yDistance < getHeight() && yDistance > -target.getHeight())    //player touched
                        target.takeAhit(1);
                    state = IDLE;
                }

                TileType frontTile =  flipX ? gameMap.getTileTypeByLocation(1, pos.x - TileType.TILE_SIZE/2, pos.y) : gameMap.getTileTypeByLocation(1, pos.x + getWidth() + TileType.TILE_SIZE/2, pos.y);
                TileType frontGroundTile =  flipX ? gameMap.getTileTypeByLocation(1, pos.x - TileType.TILE_SIZE/2, pos.y - TileType.TILE_SIZE) : gameMap.getTileTypeByLocation(1, pos.x + getWidth() + TileType.TILE_SIZE/2, pos.y - TileType.TILE_SIZE);
                //jump if block in front or if hole in front and player upward
                if ( grounded && ( ( frontTile != null && frontTile.isCollidable() ) || ( (frontGroundTile == null || !frontGroundTile.isCollidable()) && yDistance >= 0 )  ) )
                    this.velocityY += JUMP_VELOCITY * getWeight();

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
        else if (!grounded) state = JUMPING;
        else if (grounded && state == JUMPING) state = IDLE;

    }

    public void takeAhit(double damage) {
        if (!invulnerable) {
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
            case RUNNING:
                texture = animations.get(RUNNING).getFrame();
                animations.get(RUNNING).update( Gdx.graphics.getDeltaTime());
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

        batch.draw(texture, !flipX ? pos.x + getWidth() : pos.x, pos.y, !flipX ? -getWidth() : getWidth(), getHeight());
    }

    public void setTarget(Player target) { this.target = target; }
    public double getHealth() { return health; }

}

package terraria.game.actors.entities.player;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.*;
import terraria.game.actors.world.GameMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import terraria.game.screens.LoadingScreen;

public class Player extends Entity {

    private static final int SPEED = 200, JUMP_VELOCITY = 5;

    private PlayerHealth playerHealth;
    private boolean invulnerable;
    private final long INVULNERABILITY_TIME = 1000000000;
    private long invulnerabilityTimer = 0;
    public static int MAXHEALTH = 10;
    Boolean tooHigh = false;


    private static int state = 0;
    private static final int IDLE = 0, JUMPING = 1, RUNNING = 2;

    @Override
    public void create(EntitySnapshot snapshot, EntityType type, GameMap gameMap, TerrariaGame game) {
        super.create(snapshot, type, gameMap,game);
        TextureRegion[][] heart =  TextureRegion.split(game.getAssetManager().get("heart.png", Texture.class), 35, 35);
        this.playerHealth = new PlayerHealth(game, heart, snapshot.health);


        init();
    }
    public void create(int posX, int posY, EntityType type, GameMap gameMap, TerrariaGame game) {
        super.create(posX,posY, type, gameMap,game);
        TextureRegion[][] heart =  TextureRegion.split(game.getAssetManager().get("heart.png", Texture.class), 35, 35);
        this.playerHealth = new PlayerHealth(game, heart, MAXHEALTH);
        init();
    }


    public void init(){

        invulnerable = false;
        animations = new Array<>();
        for(int i = 0; i < LoadingScreen.TEXTURE_NUMBER_PLAYER ; i++){
            switch (i){
                case 0: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("playerAnimation/player"+i+".png", Texture.class)),2 , 0.5F));break;
                case 1: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("playerAnimation/player"+i+".png", Texture.class)),1 , 0.5F));break;
                case 2: animations.add(new Animation(new TextureRegion(game.getAssetManager().get("playerAnimation/player"+i+".png", Texture.class)),6 , 0.5F));break;
            }
        }
    }


    /**
     * Controle du joueur
     * @param deltaTime
     * @param gravity
     */
    @Override
    public void update(float deltaTime, float gravity, Camera camera, Stage stage) {

        camera.position.set(pos.x, pos.y + 32*5, 0);

        if ((Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.Z)) && grounded) {
            this.velocityY += JUMP_VELOCITY * getWeight();
        }
        else if (Gdx.input.isKeyPressed(Keys.SPACE) && !grounded && this.velocityY > 0) {
            this.velocityY += JUMP_VELOCITY * getWeight() * deltaTime;
        }

        super.update(deltaTime, gravity, camera, stage);   //Apply gravity

        if (Gdx.input.isKeyPressed(Keys.Q)) {
            moveX(-SPEED * deltaTime);
        }
        else if (Gdx.input.isKeyPressed(Keys.D)) {
            moveX(SPEED * deltaTime);
        } else if(Gdx.input.isKeyPressed(Keys.E)) {
            
        }

        //Gérer le temps d'invulnérabilité apres un coups
        if (invulnerable) {
            System.out.println("invulnérable: "+invulnerabilityTimer);
            invulnerabilityTimer -= deltaTime;
            if (invulnerabilityTimer <= 0)
                System.out.println("invulnerability end");
            invulnerable = false;
        }

        //Check the state of the character
        if (!grounded) state = JUMPING;
        else if (Gdx.input.isKeyPressed(Keys.Q) || Gdx.input.isKeyPressed(Keys.D)) state = RUNNING;
        else state = IDLE;

        playerHealth.update(camera,stage);

        //if fall from too high
        fallFromTooHigh();

    }

    public void takeAHit (int damage) {
        //Lancer un temps d'invulnérabilité
        if (!invulnerable) {
            //healthBar.applyDamage(1)
            invulnerable = true;
            System.out.println("touche!");
            invulnerabilityTimer = INVULNERABILITY_TIME;
        }
    }

    public void fallFromTooHigh(){

        if( velocityY < -350){ tooHigh = true;}
        if(isGrounded() && tooHigh){ playerHealth.ApplyDamage(3); tooHigh = false;}
    }


    @Override
    public EntitySnapshot getSaveSnapshot() {
        EntitySnapshot snapshot = super.getSaveSnapshot();
        snapshot.health = playerHealth.health;
        return snapshot;

    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        TextureRegion texture;

        switch (state) {
            case JUMPING:
                texture = animations.get(1).getFrame();
                break;
            case RUNNING:
                texture = animations.get(2).getFrame();
                animations.get(2).update( Gdx.graphics.getDeltaTime());
                break;
            default :   //state == IDLE
                texture = animations.get(0).getFrame();
                animations.get(0).update( Gdx.graphics.getDeltaTime());
                break;
        }

        batch.draw(texture, flipX ? pos.x+getWidth() : pos.x, pos.y, flipX ? -getWidth() : getWidth(), getHeight());
        playerHealth.draw(batch,parentAlpha);
    }
}

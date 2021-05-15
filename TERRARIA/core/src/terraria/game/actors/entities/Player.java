package terraria.game.actors.entities;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import terraria.game.actors.world.GameMap;
import terraria.game.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity {

    private static final int SPEED = 120;
    private static final int JUMP_VELOCITY = 5;


    boolean runRight = true;

    @Override
    public void create(EntitySnapshot snapshot, EntityType type, GameMap gameMap, GameScreen gameScreen) {
        super.create(snapshot, type, gameMap,gameScreen);
        init();
    }
    public void create(int posX, int posY, EntityType type, GameMap gameMap, GameScreen gameScreen) {
        super.create(posX,posY, type, gameMap,gameScreen);
        init();
    }



    public void init(){

        textures = new Array<>();
        for(int i = 1; i < 7; i++){
            textures.add(new Texture(Gdx.files.internal("playerAnimation/player"+i+".png")));
        }
        animations = new Array<>();
        for(int i =0; i < 6; i++){
            switch (i){
                case 0: case 1: animations.add(new Animation(new TextureRegion(textures.get(i)),2 , 0.5F));break;
                case 2: case 3: animations.add(new Animation(new TextureRegion(textures.get(i)),1 , 0.5F));break;
                case 4: case 5: animations.add(new Animation(new TextureRegion(textures.get(i)),6 , 0.5F));break;
            }
        }
    }


    /**
     * Controle du joueur
     * @param deltaTime
     * @param gravity
     */
    @Override
    public void update(float deltaTime, float gravity, Camera camera) {

        camera.position.set(pos.x, pos.y + 32*5, 0);

        if ((Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.UP)) && grounded) {
            this.velocityY += JUMP_VELOCITY * getWeight();

        }
        else if (Gdx.input.isKeyPressed(Keys.SPACE) && !grounded && this.velocityY > 0) {
            this.velocityY += JUMP_VELOCITY * getWeight() * deltaTime;
        }

        super.update(deltaTime, gravity, camera);//Apply gravity

        if (Gdx.input.isKeyPressed(Keys.Q)) {
            moveX(-SPEED * deltaTime);
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            moveX(SPEED * deltaTime);
        }
    }
    @Override
    public EntitySnapshot getSaveSnapshot() {
        EntitySnapshot snapshot = super.getSaveSnapshot();
        //snapshot.putFloat("spawnRadius", spawnRadius);
        return snapshot;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

        if (!grounded){
            if(runRight){
                batch.draw(animations.get(3).getFrame(), (float)pos.x, (float)pos.y);
            }
            else{ batch.draw(animations.get(2).getFrame(), (float)pos.x, (float)pos.y);}
        }
        else if (Gdx.input.isKeyPressed(Keys.Q)){
            runRight = false;
            batch.draw( animations.get(4).getFrame(), (float)pos.x, (float)pos.y);
            animations.get(4).update( Gdx.graphics.getDeltaTime());

        }
        else if (Gdx.input.isKeyPressed(Keys.D)){
            runRight = true;
            batch.draw( animations.get(5).getFrame(), (float)pos.x, (float)pos.y);
            animations.get(5).update( Gdx.graphics.getDeltaTime());
        }
        else{
            if(runRight){
                batch.draw(animations.get(1).getFrame(), (float)pos.x, (float)pos.y);
                animations.get(1).update( Gdx.graphics.getDeltaTime());
            }
            else{
                batch.draw(animations.get(0).getFrame(), (float)pos.x, (float)pos.y);
                animations.get(0).update( Gdx.graphics.getDeltaTime());
            }
        }
    }
}

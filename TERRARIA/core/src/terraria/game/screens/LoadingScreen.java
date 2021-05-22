package terraria.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.Entity;
import terraria.game.actors.entities.EntityLoader;
import terraria.game.actors.entities.player.PlayerHealth;
import terraria.game.actors.world.GameMap;
import terraria.game.actors.world.ParallaxBackground;
import terraria.game.actors.world.TileType;

import java.awt.*;
import java.util.ArrayList;

public class LoadingScreen extends ScreenAdapter {

    private ParallaxBackground parallaxBackground;
    private TerrariaGame game;
    private  ArrayList<Entity>  entities;
    private GameMap gameMap;
    private PlayerHealth playerHealth;


    public static int TEXTURE_NUMBER_PLAYER = 4;
    public static int TEXTURE_NUMBER_MUSHROOM = 3;
    public static int TEXTURE_NUMBER_PARALLAX_GAME = 3;

    private static final float PROGRESS_BAR_WIDTH = 500;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    private ShapeRenderer shapeRenderer;
    private ScreenViewport viewport;
    private Camera camera;
    private float progress = 0;



    LoadingScreen(TerrariaGame game){

        viewport = new ScreenViewport();
        camera = viewport.getCamera();
        shapeRenderer = new ShapeRenderer();

        this.game = game;

        for(int i = 1; i < TEXTURE_NUMBER_PARALLAX_GAME + 1;i++){
            game.getAssetManager().load("parallax/img"+i+".png", Texture.class);
         
        }
        for(int i = 0; i < TEXTURE_NUMBER_PLAYER;i++){
            game.getAssetManager().load("playerAnimation/player"+i+".png", Texture.class);

        }
        for(int i = 0; i < TEXTURE_NUMBER_MUSHROOM;i++){
            game.getAssetManager().load("ennemies/mushroom"+i+".png", Texture.class);

        }

        for (TileType tile : TileType.values()) {
            FileHandle texture = Gdx.files.internal("tiles/"+tile.getName()+".png");
            if (texture.exists())
                game.getAssetManager().load("tiles/"+tile.getName()+".png", Texture.class);
        }

        game.getAssetManager().load("herbes.png", Texture.class);
        game.getAssetManager().load("arbres/arbreTest.png", Texture.class);
        game.getAssetManager().load("cailloux.png",Texture.class );
        game.getAssetManager().load("filtre.png", Texture.class);
        game.getAssetManager().load("heart.png",Texture.class);
        game.getAssetManager().load("inventory/slot.png",Texture.class);
        game.getAssetManager().load("inventory/itemsInventory.png",Texture.class);

    }
    /**
     * Called when the screen should render itself.
     * @param delta
     */
    @Override
    public void	render(float delta){
        update();
        clearScreen();
        draw();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose(){
        shapeRenderer.dispose();

    }

    @Override
    public void	resize(int width, int height){
        viewport.update(width,height,true );
    }
    @Override
    public void	resume(){}

    /**
     * Called when this screen becomes the current screen for a Game.
     */
    @Override
    public void	show(){

    }

    public void update() {
        if (game.getAssetManager().update()) {

            Array<Texture> texturesParallax = new Array<Texture>();
            for(int i = 1; i < TEXTURE_NUMBER_PARALLAX_GAME ;i++){
                texturesParallax.add(game.getAssetManager().get("parallax/img"+i+".png", Texture.class));
                texturesParallax.get(texturesParallax.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
            }

            this.parallaxBackground = new ParallaxBackground(texturesParallax, false);

            gameMap = new GameMap(game);
            entities = new ArrayList<Entity>();
            entities = EntityLoader.loadEntities("test", gameMap, game);
            game.setScreen(new GameScreen(game, parallaxBackground, entities, gameMap));

        } else {
            progress = game.getAssetManager().getProgress();

        }
    }

    private void clearScreen(){
        Gdx.gl.glClearColor(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), Color.BLACK.getAlpha());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw(){
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        shapeRenderer.rect(
                (viewport.getScreenWidth() - PROGRESS_BAR_WIDTH)/2,
                (viewport.getScreenHeight() - PROGRESS_BAR_HEIGHT)/2,
                progress * PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT
        );
        shapeRenderer.end();
    }

    @Override
    public void hide(){}
    @Override
    public void	pause(){}

}


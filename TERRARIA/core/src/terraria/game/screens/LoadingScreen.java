package terraria.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.Entity;
import terraria.game.actors.entities.EntityLoader;
import terraria.game.actors.world.GameMap;
import terraria.game.actors.world.ParallaxBackground;
import terraria.game.actors.world.TileType;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadingScreen extends ScreenAdapter {

    private final ParallaxBackground parallaxBackground;
    private Stage stage;
    private OrthographicCamera camera;
    private TerrariaGame game;
    private  ArrayList<Entity>  entities;
    private GameMap gameMap;
    private float progress = 0;

    public static int TEXTURE_NUMBER_PLAYER = 3;
    public static int TEXTURE_NUMBER_PARALLAX_GAME = 3;


    LoadingScreen(TerrariaGame game){

        this.game = game;
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera) stage.getViewport().getCamera();


        for(int i = 1; i < TEXTURE_NUMBER_PARALLAX_GAME + 1;i++){
            game.getAssetManager().load("parallax/img"+i+".png", Texture.class);
         
        }
        for(int i = 0; i < TEXTURE_NUMBER_PLAYER;i++){
            game.getAssetManager().load("playerAnimation/player"+i+".png", Texture.class);

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

        game.getAssetManager().finishLoading();


        Array<Texture> texturesParallax = new Array<Texture>();
        for(int i = 1; i < TEXTURE_NUMBER_PARALLAX_GAME ;i++){
            texturesParallax.add(game.getAssetManager().get("parallax/img"+i+".png", Texture.class));
            texturesParallax.get(texturesParallax.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        this.parallaxBackground = new ParallaxBackground(texturesParallax, false);

        gameMap = new GameMap(game);
        entities = new ArrayList<Entity>();
        entities = EntityLoader.loadEntities("test", gameMap, game);



    }






    /**
     * Called when the screen should render itself.
     * @param delta
     */
    @Override
    public void	render(float delta){

        stage.act(delta);
        stage.draw();

        update();

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose(){
        stage.dispose();

    }

    /**
     * Called when this screen is no longer the current screen for a Game.
     */
    @Override
    public void hide(){}


    @Override
    public void	pause(){}


    @Override
    public void	resize(int width, int height){
        stage.getViewport().update(width,height,true );
    }
    @Override
    public void	resume(){}

    /**
     * Called when this screen becomes the current screen for a Game.
     */
    @Override
    public void	show(){ Gdx.input.setInputProcessor(stage);}



    public void update() {
        if (game.getAssetManager().update()) {
            game.setScreen(new GameScreen(game, parallaxBackground, entities, gameMap));
        } else {
            progress = game.getAssetManager().getProgress();
        }
    }


}


package terraria.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import terraria.game.TerrariaGame;
import terraria.game.actors.world.GameMap;
import terraria.game.actors.world.TileType;

public class LoadingScreen extends ScreenAdapter {

    private Stage stage;
    private OrthographicCamera camera;
    private TerrariaGame game;

    public static int TEXTURE_NUMBER_PLAYER = 6;
    public static int TEXTURE_NUMBER_PARALLAX_GAME = 3;
    public static int TEXTURE_NUMBER_PARALLAX_MENU = 2;
    public static int TEXTURE_NUMBER_TILES = 11;

    LoadingScreen(TerrariaGame game){

        this.game = game;
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera) stage.getViewport().getCamera();


        for(int i = 1; i < TEXTURE_NUMBER_PARALLAX_GAME + 1;i++){
            game.getAssetManager().load("background/img"+i+".png", Texture.class);
         
        }
        for(int i = 1; i < TEXTURE_NUMBER_PLAYER + 1;i++){
            game.getAssetManager().load("playerAnimation/player"+i+".png", Texture.class);

        }
        for(int i = 1; i < TEXTURE_NUMBER_TILES + 1;i++){
            game.getAssetManager().load("Tiles/"+i+".png", Texture.class);

        }

        game.getAssetManager().load("herbes.png", Texture.class);
        game.getAssetManager().load("arbres/arbreTest.png", Texture.class);
        game.getAssetManager().load("cailloux.png",Texture.class );
        game.getAssetManager().load("filtre.png", Texture.class);
    }




    /**
     * Called when the screen should render itself.
     * @param delta
     */
    @Override
    public void	render(float delta){

        stage.act(delta);
        stage.draw();

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



}


package terraria.game.screens;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import terraria.game.TerrariaGame;
import terraria.game.actors.Inventory.Inventory;
import terraria.game.actors.entities.*;
import terraria.game.actors.entities.player.Player;
import terraria.game.actors.entities.player.PlayerHealth;
import terraria.game.actors.world.GameMap;
import terraria.game.actors.world.GeneratorMap.MapLoader;
import terraria.game.actors.world.ParallaxBackground;

import java.util.ArrayList;


public class GameScreen extends ScreenAdapter {

    public TerrariaGame game;
    private Stage stage;
    private OrthographicCamera camera;

    //Acteurs//
    ParallaxBackground parallaxBackground;
    Inventory inventory;

    GameMap gameMap;
    ImageButton exitButton;
    Boolean isMenuShow = false;

    protected ArrayList<Entity> entities;
    Player player;

    public GameScreen(final TerrariaGame game, ParallaxBackground parallaxBackground, final ArrayList<Entity> entities, final GameMap gameMap) {

        this.game = game;
        this.parallaxBackground = parallaxBackground;
        this.entities = entities;
        this.gameMap = gameMap;


        //Initialisation du stage et de la camera//
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera) stage.getViewport().getCamera();

        TextureRegion exit = new TextureRegion(new Texture(Gdx.files.internal("background/exit.png")));
        TextureRegion exitPressed = new TextureRegion(new Texture(Gdx.files.internal("background/exitPressed.png")));
        exitButton = new ImageButton( new TextureRegionDrawable(exit), new TextureRegionDrawable(exitPressed));
        exitButton.setPosition(stage.getViewport().getScreenWidth()/2,(stage.getViewport().getScreenHeight()/2)-exit.getRegionHeight(), Align.center);
        exitButton.addListener(new ActorGestureListener() {

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                dispose();
                game.getAssetManager().clear();
                EntityLoader.saveEntities("test", entities);
                MapLoader.saveMap(gameMap.getId(), gameMap.getName(), gameMap.getMap(), gameMap.getStartingPoint());
                game.setScreen(new MainMenuScreen(game));
            }
        });

        parallaxBackground.setSize(stage.getViewport().getScreenWidth(),stage.getViewport().getScreenHeight());
        parallaxBackground.setSpeed(1);

        player = (Player) entities.get(0);

        inventory = new Inventory(stage, game);


        //On ajoute nos acteurs//
        stage.addActor(parallaxBackground);
        stage.addActor(gameMap);
        stage.addActor(inventory);

        for(Entity entity : entities ){
            stage.addActor(entity);
        }
        stage.addActor(exitButton);



        //spawnMushroom((int)player.pos.x+4* TileType.TILE_SIZE, (int)player.pos.y);

    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new terraria.game.screens.Input(this));
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Entity entity : entities) {
            entity.update(delta, -9.8f, camera, stage);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            EntityLoader.saveEntities("test", entities);
            MapLoader.saveMap(gameMap.getId(), gameMap.getName(), gameMap.getMap(), gameMap.getStartingPoint());
        }

        if (Gdx.input.justTouched()) {
            if (!isMenuShow)
                blocAction();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            //Permet l'ouverture de l'inventaire
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (isMenuShow) {
                isMenuShow = false;
            } else {
                isMenuShow = true;
            }
        }
        
        if (isMenuShow) {
            exitButton.setPosition(camera.position.x,camera.position.y, Align.center);
        } else {
            exitButton.setPosition(0,0, Align.center);
        }

        this.camera.update();
        this.parallaxBackground.update(camera, stage);
        this.gameMap.update(camera, stage);
        this.inventory.update(camera, stage);

        stage.act(delta);
        stage.draw();

    }

    public void blocAction() {
        Vector3 pos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector3 coordinate = gameMap.getTileCoordinateByLocation(1,pos.x, pos.y);
        if (coordinate != null) {
            if (gameMap.presentTile(coordinate) ) {
                gameMap.destroyTile(coordinate);
            } else {
                gameMap.addTile(coordinate);
                if (gameMap.DoesRectCollideWithMap(entities.get(0).getX(), entities.get(0).getY(), (int) entities.get(0).getWidth(), (int) entities.get(0).getHeight())) {
                    gameMap.destroyTile(coordinate);
                }

            }
        }

        stage.act();
        stage.draw();

        this.parallaxBackground.update(camera, stage);
        this.gameMap.update(camera, stage);

    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void spawnMushroom (int posX, int posY) {
        Mushroom entity = new Mushroom();
        entity.create(posX, posY, EntityType.SHROOM, gameMap, game);
        entities.add(entity);
        stage.addActor(entity);
        entity.setTarget(player);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true );
        parallaxBackground.setSize(stage.getViewport().getWorldWidth(),stage.getViewport().getWorldHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose() {
        stage.dispose();
    }




}

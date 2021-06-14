package terraria.game.screens;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import terraria.game.TerrariaGame;
import terraria.game.actors.Inventory.Inventory;
import terraria.game.actors.Inventory.ItemsGraphic;
import terraria.game.actors.entities.*;
import terraria.game.actors.entities.player.Player;
import terraria.game.actors.world.*;
import terraria.game.actors.world.GeneratorMap.MapLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameScreen extends ScreenAdapter {

    public final static int GAME_RUNNING = 1;
    public final static int GAME_PAUSED = 2;
    public final static int GAME_OVER = 3;

    public TerrariaGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private static float gravity = -9.8f;

    //Acteurs//
    ParallaxBackground parallaxBackground;
    Inventory inventory;
    DayNightCycle dayNightCycle;
    NightFiltre nightFiltre;

    private Music gameMusicDay, gameMusicNight;
    private Music forestAmbianceDay, forestAmbianceNight;

    GameMap gameMap;
    ImageButton saveButton;
    ImageButton optionButton;
    ImageButton exitButton;
    ImageButton restartButton;
    Boolean isMenuShow = false;

    protected ArrayList<Entity> entities;
    private final int MAX_ENTITIES = 128;
    private final int rabbitSpawnRate = 20, mushroomSpawnRate = 20, slimeSpawnRate = 10;    //ex: 1 rabbit spawn every 20sec
    private float rabbitTimer = rabbitSpawnRate/2, mushroomTimer = 0, slimeTimer = 0;       //used to count sec

    Player player;
    private int res = 0;

    public GameScreen(final TerrariaGame game, ParallaxBackground parallaxBackground, final ArrayList<Entity> entities, final GameMap gameMap) {
        this.game = game;
        this.parallaxBackground = parallaxBackground;
        this.entities = entities;
        this.gameMap = gameMap;

        //Initialisation du stage et de la camera//
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera) stage.getViewport().getCamera();


        dayNightCycle = new DayNightCycle(game.getAssetManager().get("dayNightCycle.png",Texture.class));
        nightFiltre = new NightFiltre(new Texture("filtreNight.png"));


        TextureRegion save = new TextureRegion(new Texture(Gdx.files.internal("background/save.png")));
        TextureRegion savePressed = new TextureRegion(new Texture(Gdx.files.internal("background/savePressed.png")));
        saveButton = new ImageButton(new TextureRegionDrawable(save), new TextureRegionDrawable(savePressed));
        saveButton.setPosition(0, 0, Align.center);
        saveButton.addListener(new ActorGestureListener() {
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                EntityLoader.saveEntities("test", entities);
                MapLoader.saveMap(gameMap.getId(), gameMap.getName(), gameMap.getMap(), gameMap.getStartingPoint());
            }
        });

        TextureRegion option = new TextureRegion(new Texture(Gdx.files.internal("background/options.png")));
        TextureRegion optionPressed = new TextureRegion(new Texture(Gdx.files.internal("background/optionsPressed.png")));
        optionButton = new ImageButton(new TextureRegionDrawable(option), new TextureRegionDrawable(optionPressed));
        optionButton.setPosition(0, 0, Align.center);
        optionButton.addListener(new ActorGestureListener() {
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
            }
        });

        TextureRegion exit = new TextureRegion(new Texture(Gdx.files.internal("background/exit.png")));
        TextureRegion exitPressed = new TextureRegion(new Texture(Gdx.files.internal("background/exitPressed.png")));
        exitButton = new ImageButton( new TextureRegionDrawable(exit), new TextureRegionDrawable(exitPressed));
        exitButton.setPosition(0,0, Align.center);
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

        final TextureRegion restart = new TextureRegion(new Texture(Gdx.files.internal("background/restart.png")));
        TextureRegion restartPressed = new TextureRegion(new Texture(Gdx.files.internal("background/restartPressed.png")));
        restartButton = new ImageButton(new TextureRegionDrawable(restart), new TextureRegionDrawable(restartPressed));
        restartButton.setPosition(0, 0, Align.center);
        restartButton.addListener(new ActorGestureListener() {

            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                Player p = new Player();
                p.create(gameMap.getStartingPoint()[0], gameMap.getStartingPoint()[1], EntityType.PLAYER, gameMap, game);
                entities.add(0, p);
                player = (Player) entities.get(0);
                player.playerHealth.update(camera, stage);
                res = 1;
                stage.addActor(player);
                setEntitiesTarget();
            }
        });

        parallaxBackground.setSize(stage.getViewport().getScreenWidth(),stage.getViewport().getScreenHeight());
        parallaxBackground.setSpeed(1);

        player = (Player) entities.get(0);
        gameMap.getDataMap().startingPoint[0] = (int) player.pos.x;
        gameMap.getDataMap().startingPoint[1] = (int) player.pos.y;
        inventory = player.getInventory();

        //On ajoute nos acteurs//
        stage.addActor(dayNightCycle);
        stage.addActor(parallaxBackground);
        stage.addActor(gameMap);


        for(Entity entity : entities ){
            stage.addActor(entity);
        }

        stage.addActor(nightFiltre);

        stage.addActor(inventory);
        for(ItemsGraphic items : inventory.getGraphicItems() ){
            stage.addActor(items);
        }
        for(ItemsGraphic items : inventory.getCraftableItemGraphicList() ){
            stage.addActor(items);
        }

        stage.addActor(saveButton);
        stage.addActor(optionButton);
        stage.addActor(exitButton);
        stage.addActor(restartButton);

        stage.addActor(inventory.getLeftArrow());
        stage.addActor(inventory.getRightArrow());

        //Make mobs focus the player
        setEntitiesTarget();

        //Initialize music
        gameMusicDay = game.getAssetManager().get("audio/music/game_song_day.mp3", Music.class);
        gameMusicNight = game.getAssetManager().get("audio/music/game_song_night.mp3", Music.class);

        forestAmbianceDay = game.getAssetManager().get("audio/music/forest_ambiance_day.wav", Music.class);
        forestAmbianceNight = game.getAssetManager().get("audio/music/forest_ambiance_night.wav", Music.class);

        gameMusicDay.setLooping(true);
        gameMusicNight.setLooping(true);
        forestAmbianceDay.setLooping(true);
        forestAmbianceNight.setLooping(true);

        TerrariaGame.setState(GAME_RUNNING);
    }




    @Override
    public void render(float delta) {
        this.camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (TerrariaGame.getState()) {
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_RUNNING:
                updateRunning(delta);
                break;
            case GAME_OVER:
                updateOver();
                break;
        }

        stage.act(delta);
        stage.draw();

    }


    public void updateRunning(float delta) {
        if(Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            TerrariaGame.setState(GAME_PAUSED);
        } else if(player.getHealth() <= 0) {
            TerrariaGame.setState(GAME_OVER);
        }

        //Right or left click on blocs
        if (!isMenuShow && !inventory.isInventoryShow() && Gdx.input.isTouched())
            blocAction();

        //Handle inventory
        if(Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E)) {
            if (inventory.isInventoryShow()) {
                inventory.setInventoryShow(false);
            } else {
                inventory.setInventoryShow(true);
                inventory.updateCraft();
            }
        }

        //Handle music
        TileType backTile = gameMap.getTileTypeByLocation(0, player.getX(), player.getY());
        DayNightCycle.TimeOfDay timeOfDay = dayNightCycle.getTime();
        switch (timeOfDay) {
            case SUNRISE:
            case DAY:
            case SUNSET:
                if (gameMusicNight.isPlaying()) gameMusicNight.stop();
                if (forestAmbianceNight.isPlaying()) forestAmbianceNight.stop();

                gameMusicDay.play();
                if (backTile == null) forestAmbianceDay.play();
                else forestAmbianceDay.pause();

                break;

            case NIGHT:
                if (gameMusicDay.isPlaying()) gameMusicDay.stop();
                if (forestAmbianceDay.isPlaying()) forestAmbianceDay.stop();

                gameMusicNight.play();
                if (backTile != TileType.STONE_BACKGROUND) forestAmbianceNight.play();
                else forestAmbianceNight.pause();

                break;
        }

        //Spawn mob
        updateSpawnMob(delta, timeOfDay);

        //Update all actors
        gameMap.update(camera, stage);

        Iterator<Entity> it = entities.iterator();
        Entity entity;
        while (it.hasNext()) {
            entity = it.next();
            entity.update(delta, gravity, camera, stage);
            if (entity.getHealth() <= 0) {  //if entity dead

                if (entity.isLootable()) {  //if entity has loot
                    switch (entity.getType()) {
                        case MUSHROOM:
                            inventory.addTileInInventory(TileType.MUSHROOM.getId());
                            break;
                        case SLIME:
                            inventory.addTileInInventory(TileType.SLIME.getId());
                            break;
                        case RABBIT:
                            inventory.addTileInInventory(TileType.RABBIT_MEAT.getId());
                            break;
                    }
                }

                entity.remove();            //remove it from stage
                it.remove();                //stop updating it
            }
        }

        dayNightCycle.update(camera,stage);
        parallaxBackground.update(camera, stage);
        nightFiltre.update(camera,stage);
        inventory.update(camera, stage);
        for (ItemsGraphic items : inventory.getGraphicItems()) {
            items.update(camera, stage, inventory.isInventoryOpen());
        }

        for (ItemsGraphic itemsCraft : inventory.getCraftableItemGraphicList()) {
            itemsCraft.update(camera, stage, inventory.isInventoryOpen());
        }

    }


    public void updatePaused() {
        //Handle pause Menu
        saveButton.setPosition(camera.position.x, camera.position.y + 150, Align.center);
        optionButton.setPosition(camera.position.x, camera.position.y, Align.center);
        exitButton.setPosition(camera.position.x,camera.position.y - 150, Align.center);

        if(Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            saveButton.setPosition(0, 0, Align.center);
            optionButton.setPosition(0,0, Align.center);
            exitButton.setPosition(0, 0, Align.center);
            TerrariaGame.setState(GAME_RUNNING);
        }
    }


    public void updateOver() {
        if(res == 1) {
            restartButton.setPosition(0,0, Align.center);
            camera.update();
            res = 0;
            TerrariaGame.setState(GAME_RUNNING);
        } else {
            restartButton.setPosition(camera.position.x, camera.position.y, Align.center);
        }
    }


    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new terraria.game.screens.Input(this));
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        gameMusicDay.play();
        forestAmbianceDay.play();
    }



    public void blocAction() {
        Vector3 pos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector3 coordinate = gameMap.getTileCoordinateByLocation(1, pos.x, pos.y);

        if (coordinate != null) {
            if (Gdx.input.isButtonPressed(Buttons.LEFT)){
                //destroy block
                //PROVISOIRE
                if (inventory.getItemsList().get(inventory.getCurrentItems()).getIdTile() < 18)
                    gameMap.initDestroyTile(coordinate, pos, inventory);
            } else if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
                //put block
                //PROVISOIRE
                if (inventory.getItemsList().get(inventory.getCurrentItems()).getIdTile() < 18)
                    gameMap.addTile(coordinate, inventory);
                //Check if no entity in the way
                for (Entity entity : entities) {
                    if (gameMap.doesRectCollideWithMap(entity.getX(), entity.getY(), (int) entity.getWidth(), (int) entity.getHeight()))
                        gameMap.destroyTile(coordinate, inventory);
                }
            }
        }
    }


    public void updateSpawnMob (float delta,  DayNightCycle.TimeOfDay timeOfDay) {
        if (entities.size() <= MAX_ENTITIES) {

            switch (timeOfDay) {
                case SUNRISE:
                case DAY:
                case SUNSET:
                    rabbitTimer += delta;
                    if (rabbitTimer >= rabbitSpawnRate) {
                        rabbitTimer = 0;
                        spawnMob(EntityType.RABBIT);
                    }
                    break;

                case NIGHT:
                    mushroomTimer += delta;
                    slimeTimer += delta;

                    if (mushroomTimer >= mushroomSpawnRate) {
                        mushroomTimer = 0;
                        spawnMob(EntityType.MUSHROOM);
                    }

                    if (slimeTimer >= slimeSpawnRate) {
                        slimeTimer = 0;
                        spawnMob(EntityType.SLIME);
                    }
                    break;
            }

        }
    }


    /**
     * Spawn un mob dans une case vide random comprise dans un carré de taille spawnRadius autour du joueur
     * Rabbit spawn de jour sur l'herbe et disparaissent la nuit
     * Mushroom spawn la nuit sur l'herbe et ne disparaissent pas le jour
     * Slime spawn sur n'importe quel bloc la nuit et disparaissent le jour
     * @param type mob a faire spawn
     */
    public void spawnMob (EntityType type) {
        int playerX = (int) player.getX() / TileType.TILE_SIZE;
        int playerY = (int) player.getY() / TileType.TILE_SIZE;
        int spawnRadius = player.getSpawnRadius();

        //Check all valid spots
        List<Vector2> validPos = new ArrayList<Vector2>();
        for (int x = playerX - spawnRadius; x <= playerX + spawnRadius; x++ ) {
            for (int y = playerY - spawnRadius; y <= playerY + spawnRadius; y++ ) {

                //Check if the spot is empty
                TileType tileType = gameMap.getTileTypeByCoordinate(1, x, y);
                if (tileType == null || !tileType.isCollidable()) {
                    TileType underTile = gameMap.getTileTypeByCoordinate(1, x, y-1);

                    //Check the specific conditions of each mob
                    switch (type) {
                        case MUSHROOM:
                        case RABBIT:
                            //Check if grass ground
                            if (underTile == TileType.GRASS || underTile == TileType.MOSSY_STONE)
                                validPos.add(new Vector2(x, y));
                            break;

                        case SLIME:
                            //Check if solid ground
                            if (underTile != null && underTile.isCollidable())
                                validPos.add(new Vector2(x, y));
                            break;
                    }

                }

            }
        }

        if (!validPos.isEmpty()) {
            //Choose a random spawn point
            Random rand = new Random();
            Vector2 randomPos = validPos.get(rand.nextInt(validPos.size()));

            //Create new mob
            switch (type) {
                case MUSHROOM:
                    Mushroom mushroom = new Mushroom();
                    mushroom.create((int)randomPos.x * TileType.TILE_SIZE, (int)randomPos.y * TileType.TILE_SIZE, EntityType.MUSHROOM, gameMap, game);
                    entities.add(mushroom);
                    stage.addActor(mushroom);
                    mushroom.setTarget(player);
                    break;
                case RABBIT:
                    Rabbit rabbit = new Rabbit();
                    rabbit.create((int)randomPos.x * TileType.TILE_SIZE, (int)randomPos.y * TileType.TILE_SIZE, EntityType.RABBIT, gameMap, game);
                    entities.add(rabbit);
                    stage.addActor(rabbit);
                    rabbit.setTarget(player);
                    break;
                case SLIME:
                    Slime slime = new Slime();
                    slime.create((int)randomPos.x * TileType.TILE_SIZE, (int)randomPos.y * TileType.TILE_SIZE, EntityType.SLIME, gameMap, game);
                    entities.add(slime);
                    stage.addActor(slime);
                    slime.setTarget(player);
                    break;
            }

        }
    }

    public void setEntitiesTarget () {
        for (Entity entity : entities) {
            switch (entity.getType()) {
                case MUSHROOM:
                    Mushroom mushroom = (Mushroom) entity;
                    mushroom.setTarget(player);
                    break;
                case RABBIT:
                    Rabbit rabbit = (Rabbit) entity;
                    rabbit.setTarget(player);
                    break;
                case SLIME:
                    Slime slime = (Slime) entity;
                    slime.setTarget(player);
                    break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.zoom = 1;
        stage.getViewport().update(width,height,true );
        parallaxBackground.setSize(stage.getViewport().getWorldWidth(),stage.getViewport().getWorldHeight());
    }

    @Override
    public void pause() {
        if(TerrariaGame.getState() == GAME_RUNNING) TerrariaGame.setState(GAME_PAUSED);
    }

    @Override
    public void resume() {
        if(TerrariaGame.getState() == GAME_PAUSED) TerrariaGame.setState(GAME_RUNNING);
    }

    @Override
    public void hide() {

    }

    public void dispose() {
        stage.dispose();

        gameMusicDay.stop();
        gameMusicNight.stop();
        forestAmbianceDay.stop();
        forestAmbianceNight.stop();

        gameMusicDay.dispose();
        gameMusicNight.dispose();
        forestAmbianceDay.dispose();
        forestAmbianceNight.dispose();
    }

    public Stage getStage(){
        return stage;
    }
    public OrthographicCamera getCamera() {
        return camera;
    }
}

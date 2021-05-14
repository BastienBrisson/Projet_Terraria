package terraria.game.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import terraria.game.actors.world.GeneratorMap.MapLoader;
import terraria.game.screens.GameScreen;
import terraria.game.actors.world.GeneratorMap.DataMap;


public class GameMap extends Actor {

    private Array<Texture> texturesTiles;
    private TextureRegion[][] plant;
    private TextureRegion [][] trees;
    private TextureRegion [][] pebble;
    private TextureRegion [][] filtre;

    GameScreen gameScreen;
    public Camera camera;
    private DataMap dataMap;

    int ScreenX, ScreenY,ScreenWidth,ScreenHeigth;


    public GameMap(GameScreen gameScreen) {

        dataMap = MapLoader.loadMap("basic", "My Grass Lands!");
        this.gameScreen = gameScreen;

        texturesTiles = new Array<Texture>();
        for(int i = 1; i < 12;i++){
            texturesTiles.add(new Texture(Gdx.files.internal("Tiles/"+i+".png")));
        }

        //tiles = TextureRegion.split(new Texture("tiles_32x32.png"), TileType.TILE_SIZE, TileType.TILE_SIZE);
        plant = TextureRegion.split(new Texture("herbes.png"), TileType.TILE_SIZE, TileType.TILE_SIZE);
        trees =  TextureRegion.split(new Texture(Gdx.files.internal("arbres/arbreTest.png")), 202, 375 );
        pebble =  TextureRegion.split(new Texture("cailloux.png"), TileType.TILE_SIZE, TileType.TILE_SIZE);
        filtre = TextureRegion.split(new Texture("filtre.png"), TileType.TILE_SIZE, TileType.TILE_SIZE);
    }

    public int[] getStartingPoint() {
        return dataMap.startingPoint;
    }
    public String getId() {
        return dataMap.id;
    }
    public int[][][] getMap() {
        return dataMap.map;
    }
    public String getName() {return dataMap.name;}


    public void render(Camera camera, Stage stage){
        this.camera = camera;
        Vector3 vec = camera.position;
        ScreenX =  (int)vec.x -  stage.getViewport().getScreenWidth()/2;;
        ScreenY = (int)vec.y -  stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeigth = stage.getViewport().getScreenHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        for (int layer = 0; layer < getMap().length ; layer++) {
            for (int row = 0; row < getMap()[0].length; row++) {
                for (int col = 0; col < getMap()[0][0].length; col++) {

                    if( col* TileType.TILE_SIZE - 128 <= ScreenX + ScreenWidth && (col * TileType.TILE_SIZE + 128>= ScreenX) && (row * TileType.TILE_SIZE - 128 <= ScreenY + ScreenHeigth && row * TileType.TILE_SIZE + 128 >= ScreenY)) {


                        TileType type = gameScreen.getTileTypeByCoordinate(layer, col, row);
                        TileType Lighttype = gameScreen.getTileTypeByCoordinate(2, col, row);


                        if (type != null) {

                            switch (type) {

                                case STEM:
                                    if (gameScreen.getTileTypeByCoordinate(layer, col, row - 1) == TileType.GRASS) {
                                        batch.draw(trees[0][col % 13], col * TileType.TILE_SIZE - 85, row * TileType.TILE_SIZE);

                                    }
                                    break;
                                case PEBBLE:
                                    batch.draw(pebble[0][col % 8], col * TileType.TILE_SIZE, row * TileType.TILE_SIZE);
                                    break;

                                case PLANT:
                                    batch.draw(plant[0][col % 4], col * TileType.TILE_SIZE, row * TileType.TILE_SIZE);
                                    break;

                                case GRASS:
                                case GRASSONSTONE:
                                    batch.draw(texturesTiles.get(type.getId() - 1), col * TileType.TILE_SIZE, row * TileType.TILE_SIZE);

                                    int srcFunc = batch.getBlendSrcFunc();
                                    int dstFunc = batch.getBlendDstFunc();
                                    batch.enableBlending();
                                    batch.setBlendFunction(Gdx.gl20.GL_DST_COLOR, Gdx.gl20.GL_SRC_ALPHA);
                                    batch.draw(filtre[0][0], col * TileType.TILE_SIZE, row * TileType.TILE_SIZE);
                                    batch.draw(filtre[0][1], col * TileType.TILE_SIZE, (row - 1) * TileType.TILE_SIZE);
                                    batch.draw(filtre[0][2], col * TileType.TILE_SIZE, (row - 2) * TileType.TILE_SIZE);
                                    batch.setBlendFunction(srcFunc, dstFunc);

                                    break;

                                case FILTRE0:
                                case FILTRE1:
                                case FILTRE2:break;

                                default:
                                    batch.draw(texturesTiles.get(type.getId() - 1), col * TileType.TILE_SIZE, row * TileType.TILE_SIZE);
                                    break;
                            }


                        }
                    }
                }
            }
        }
    }
}


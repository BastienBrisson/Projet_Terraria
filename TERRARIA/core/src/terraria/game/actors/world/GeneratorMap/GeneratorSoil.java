package terraria.game.actors.world.GeneratorMap;
import terraria.game.actors.world.GeneratorMap.Noise.PerlinNoise;
import terraria.game.actors.world.TileType;
import java.util.Random;

public class GeneratorSoil {

    protected  static void GenerateSoil(int LimitOfGrasses, int LimitOfRocks, int col, DataMap mapData, int random){
        mapData.map[1][LimitOfGrasses][col] = TileType.GRASS.getId();
        mapData.map[1][LimitOfRocks][col] = TileType.MOSSY_STONE.getId();


        float treeValLast = PerlinNoise.PerlinNoise1D((float) (( random - 1 )*0.1), 0.50f, 9);
        float treeVal = PerlinNoise.PerlinNoise1D((float) (random *0.1), 0.50f, 9);
        float treeValNext = PerlinNoise.PerlinNoise1D((float) ((random + 1 )*0.1), 0.50f, 9);

        Random rand = new Random();
        if (treeVal >= treeValLast && treeVal >= treeValNext && treeVal > -0.6 && LimitOfGrasses < LimitOfRocks) {
            GenerateTrees(LimitOfGrasses-1,col,mapData);
        }

        else{

            int randomSur3 = rand.nextInt(3);
            if(randomSur3 == 0){
                if(LimitOfRocks >= LimitOfGrasses) {
                    mapData.map[1][LimitOfGrasses - 1][col] = TileType.WEED.getId();
                }
            }
            else{
                if(LimitOfGrasses >= LimitOfRocks) {
                    randomSur3 = rand.nextInt(4);
                    if(randomSur3 ==1) {
                        mapData.map[1][LimitOfRocks - 1][col] = TileType.PEBBLE.getId();
                    }
                }
                else{
                    randomSur3 = rand.nextInt(16);
                    if(randomSur3 ==1) {
                        mapData.map[1][LimitOfGrasses - 1][col] = TileType.PEBBLE.getId();
                    }
                }
            }
        }
    }

    public static void GenerateTrees(int row, int col, DataMap mapData){

        Random rand = new Random();
        int random = rand.nextInt(3);
        int TreeHeight = 0;

        switch(random){
            case 0:
                TreeHeight = 4;
                break;
            case 1:
                TreeHeight = 5;
                break;
            case 2:
                TreeHeight = 6;
                break;
        }
        for(int i = 0; i < TreeHeight; i++){
            mapData.map[1][row - i][col] = TileType.LOG.getId();
        }
    }
}

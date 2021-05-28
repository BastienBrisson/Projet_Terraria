package terraria.game.actors.world.GeneratorMap;
import terraria.game.actors.world.GeneratorMap.Noise.PerlinNoise;
import terraria.game.actors.world.TileType;
import java.util.Random;

public class GeneratorSoil {

    protected  static void generateSoil(int LimitOfGrasses, int LimitOfRocks, int col, DataMap mapData, int random){
        mapData.map[1][LimitOfGrasses][col] = TileType.GRASS.getId();
        mapData.map[1][LimitOfRocks][col] = TileType.MOSSY_STONE.getId();


        float treeValLast = PerlinNoise.PerlinNoise1D((float) (( random - 1 )*0.1), 0.50f, 9);
        float treeVal = PerlinNoise.PerlinNoise1D((float) (random *0.1), 0.50f, 9);
        float treeValNext = PerlinNoise.PerlinNoise1D((float) ((random + 1 )*0.1), 0.50f, 9);

        Random rand = new Random();
        if (treeVal >= treeValLast && treeVal >= treeValNext && treeVal > -0.6 && LimitOfGrasses < LimitOfRocks) {
            generateTrees(LimitOfGrasses-1,col,mapData);
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

    public static void generateTrees(int row, int col, DataMap mapData){
        int treeHeight = 0;

        switch(col % 13) {
            case 0:
            case 11:
                treeHeight = 8;
                break;
            case 1:
            case 5:
            case 6:
            case 10:
                treeHeight = 4;
                break;
            case 2:
            case 9:
                treeHeight = 5;
                break;
            case 3:
            case 4:
            case 7:
            case 8:
                treeHeight = 6;
                break;
            case 12:
                treeHeight = 3;
                break;
        }

        for(int i = 0; i < treeHeight; i++){
            mapData.map[1][row - i][col] = TileType.LOG.getId();
        }
    }
}

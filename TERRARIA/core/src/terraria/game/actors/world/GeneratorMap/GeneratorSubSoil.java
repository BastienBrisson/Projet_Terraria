package terraria.game.actors.world.GeneratorMap;
import terraria.game.actors.world.GeneratorMap.Noise.PerlinNoise2D;
import terraria.game.actors.world.TileType;


public class GeneratorSubSoil {

    public  static void GenerateSubSoil(int LimitOfGrasses, int LimitOfRocks, int col, DataMap mapData, int random){

        int row;
        if(LimitOfGrasses < LimitOfRocks){
            row = LimitOfGrasses + 1;
        }
        else{
            row = LimitOfRocks + 1;
        }

        while( row < MapLoader.SIZE_HEIGHT) {
            int fq = 2;
            double [] noiseTab = new double[5];
            for( int i = 0; i < noiseTab.length; i++){
               noiseTab[i] = PerlinNoise2D.noise2D(((double)(random + i*10)/(32)*fq)+0.1,((double)row/(32)*fq)+0.1 ) + 1;
               fq = 9;
            }

            boolean HeightLimitOfCaves = (row - (LimitOfRocks))%20 > 10;
            int i = 0;

            if(row >= LimitOfRocks){mapData.map[0][row][col] = TileType.CAVE.getId();}

            if((noiseTab[i] > 0.7 && noiseTab[i] < 1.3)   && HeightLimitOfCaves) {

                    if(row == MapLoader.SIZE_HEIGHT - 1){  mapData.map[1][row][col] = TileType.LAVA.getId();}
                    else{ mapData.map[1][row][col] = TileType.CAVE.getId();}
            }
            else{
                i++;
                if((noiseTab[i++]> 1.7) && (row > LimitOfRocks + (MapLoader.SIZE_HEIGHT/4))){
                    mapData.map[1][row][col] = TileType.DIAMOND.getId();
                }
                else if((noiseTab[i++] > 1.65) && (row > LimitOfRocks + (MapLoader.SIZE_HEIGHT/8))){
                    mapData.map[1][row][col] = TileType.GOLD.getId();
                }
                else if((noiseTab[i++] > 1.5) && (row > LimitOfRocks + (MapLoader.SIZE_HEIGHT/16))){
                    mapData.map[1][row][col] = TileType.IRON.getId();
                }
                else if(noiseTab[i] > 1.5 && (row > LimitOfRocks && row < LimitOfRocks + (MapLoader.SIZE_HEIGHT/8))){
                    mapData.map[1][row][col] = TileType.COAL.getId();
                }
                else if(row >= LimitOfRocks){
                    mapData.map[1][row][col] = TileType.DIRT.getId();
                }
                else{
                    mapData.map[1][row][col] = TileType.EARTH.getId();
                }
            }
            row++;
        }


    }
}

package terraria.game.actors.world;

import terraria.game.actors.world.GeneratorMap.MapLoader;

public class CalculatorLight {


    public static void calculatorLightMap(int[][][] map, int height, int width, int  originX, int originY) {

        for(int j = originX; j < width; j++) {
            for (int i = height - 1; i >= originY; --i) {
                map[2][i][j] = TileType.NOLIGHT.getId();
            }
        }

        for(int j = originX; j < width; j++) {
            for (int i = height - 1; i >= originY; --i) {


                if ((map[0][i][j] == 0 && (map[1][i][j] == 0 || map[1][i][j] == TileType.WEED.getId() || map[1][i][j] == TileType.LOG.getId() || map[1][i][j] == TileType.PEBBLE.getId() ))) {
                   calculatorLightSource(j, i +1, map);

                }
                else if(map[1][i][j] ==  TileType.LAVA.getId()){
                    calculatorLightSource(j, i, map);
                }
            }
        }
    }


    public static void calculatorLightSource( int col, int row, int[][][] map){

        int MAXDEGRADE = 7;
        for(int j = (MAXDEGRADE + 1); j >= 1; --j) {

            if (col + (MAXDEGRADE + 2) - j < MapLoader.WIDTH) {
                for (int i = 1; i < j; ++i) {

                    if (row + i < MapLoader.HEIGHT) {
                        if(map[2][row + i][col + (MAXDEGRADE + 2) - j] < (j - i) + (TileType.NOLIGHT.getId() -1)){
                            map[2][row + i][col + (MAXDEGRADE + 2) - j] = (j - i) + (TileType.NOLIGHT.getId() -1);
                        }
                    }

                    if (row - i >= 0) {
                        if(map[2][row - i][col + (MAXDEGRADE + 2) - j] < (j - i) + (TileType.NOLIGHT.getId() -1))
                            map[2][row - i][col + (MAXDEGRADE + 2) - j] = (j - i) + (TileType.NOLIGHT.getId() -1);
                    }
                }
                if(row< MapLoader.HEIGHT) {
                    if (map[2][row][col + (MAXDEGRADE + 2) - j] < j + (TileType.NOLIGHT.getId() - 1)) {
                        map[2][row][col + (MAXDEGRADE + 2) - j] = j + (TileType.NOLIGHT.getId() - 1);
                    }
                }
            }

        }
        for(int j = (MAXDEGRADE + 2); j >= 1; --j) {

            if (col + j - (MAXDEGRADE + 2) >= 0) {
                for (int i = 1; i < j; ++i) {

                    if (row + i < MapLoader.HEIGHT) {
                        if( map[2][row + i][col + j - (MAXDEGRADE + 2)] < (j - i) + (TileType.NOLIGHT.getId() -1) ) {
                            map[2][row + i][col + j - (MAXDEGRADE + 2)] = (j - i) + (TileType.NOLIGHT.getId() -1);
                        }
                    }

                    if (row - i >= 0) {
                        if(map[2][row - i][col + j - (MAXDEGRADE + 2)]  < (j - i) + (TileType.NOLIGHT.getId() -1)) {
                            map[2][row - i][col + j - (MAXDEGRADE + 2)] = (j - i) + (TileType.NOLIGHT.getId() -1);
                        }
                    }

                }
                if(row< MapLoader.HEIGHT) {
                    if (map[2][row][col + j - (MAXDEGRADE + 2)] < j + (TileType.NOLIGHT.getId() - 1)) {
                        map[2][row][col + j - (MAXDEGRADE + 2)] = j + (TileType.NOLIGHT.getId() - 1);
                    }
                }
            }

        }



    }

}

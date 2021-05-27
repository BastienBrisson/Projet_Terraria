package terraria.game.actors.world;

import terraria.game.actors.world.GeneratorMap.MapLoader;

public class CalculatorLight {


    public static void calculatorLightMap(int[][][] map, int height, int width, int  originX, int originY) {

        for(int j = originX; j < width; j++) {
            for (int i = height - 1; i >= originY; --i) {

                map[2][i][j] = TileType.NOLIGHT.getId();
                if (map[0][i][j] == 0 && (map[1][i][j] == 0 || map[1][i][j] == 11 || map[1][i][j] == 13 || map[1][i][j] == 12)) {
                    calculatorLightSource(j, i +1, map);

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
                        if(map[2][row + i][col + (MAXDEGRADE + 2) - j] < (j - i) + 13){
                            map[2][row + i][col + (MAXDEGRADE + 2) - j] = (j - i) + 13;
                        }
                    }

                    if (row - i >= 0) {
                        if(map[2][row - i][col + (MAXDEGRADE + 2) - j] < (j - i) + 13)
                            map[2][row - i][col + (MAXDEGRADE + 2) - j] = (j - i) + 13;
                    }
                }
                if( map[2][row][col +  (MAXDEGRADE + 2) - j]  < j + 13) {
                    map[2][row][col + (MAXDEGRADE + 2) - j] = j + 13;
                }
            }

        }
        for(int j = (MAXDEGRADE + 2); j >= 1; --j) {

            if (col + j - (MAXDEGRADE + 2) >= 0) {
                for (int i = 1; i < j; ++i) {

                    if (row + i < MapLoader.HEIGHT) {
                        if( map[2][row + i][col + j - (MAXDEGRADE + 2)] < (j - i) + 13 ) {
                            map[2][row + i][col + j - (MAXDEGRADE + 2)] = (j - i) + 13;
                        }
                    }

                    if (row - i >= 0) {
                        if(map[2][row - i][col + j - (MAXDEGRADE + 2)]  < (j - i) + 13) {
                            map[2][row - i][col + j - (MAXDEGRADE + 2)] = (j - i) + 13;
                        }
                    }

                }
                if(map[2][row][col + j - (MAXDEGRADE + 2)]  < j + 13) {
                    map[2][row][col + j - (MAXDEGRADE + 2)] = j  + 13;
                }
            }

        }



    }

}

package terraria.game.actors.world.GeneratorMap;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import terraria.game.actors.world.CalculatorLight;
import terraria.game.actors.world.GeneratorMap.Noise.*;
import terraria.game.actors.world.TileType;
import java.util.Random;


public class MapLoader {

    private static Json json = new Json();
    public static final int HEIGHT = 256;
    public static final int WIDTH = 1024;

    /**
     * On obtient une mapProcédurale :
     * lut dans un fichier si elle existre
     * En generant une map random qui sera enregistrer dans un nouveau fichier si elle n'existe pas
     *
     * @param id
     * @param name
     * @return
     */
    public static DataMap loadMap(String id, String name) {

        Gdx.files.local("saves/").file().mkdirs();

        FileHandle file = Gdx.files.local("saves/" + id + ".map");
        DataMap mapData;
        if (file.exists()) {
            mapData = json.fromJson(DataMap.class, file.readString());
        } else {
            mapData = generateRandomMap(id, name);
            saveMap(mapData.id, mapData.name, mapData.map, mapData.startingPoint, mapData.srcY, mapData.dayTimer);
            mapData = json.fromJson(DataMap.class, file.readString());
        }
        return mapData;
    }

    /**
     * On Sauvegarde notre map dans un fichier [id].map
     *
     * @param id
     * @param name
     * @param map
     */
    public static void saveMap(String id, String name, int[][][] map, int[] startingPoint, int srcY, float dayTimer) {
        DataMap mapData = new DataMap();
        mapData.id = id;
        mapData.name = name;
        mapData.map = map;
        mapData.startingPoint = startingPoint;
        mapData.height = HEIGHT;
        mapData.width = WIDTH;
        mapData.srcY = srcY;
        mapData.dayTimer = dayTimer;

        Gdx.files.local("saves/").file().mkdirs();
        FileHandle file = Gdx.files.local("saves/" + id + ".map");
        file.writeString(json.prettyPrint(mapData), false);
    }

    /**
     * On genere une map à trois dimension avec un id et un nom donné en argument
     *
     * @param id
     * @param name
     * @return
     */
    public static DataMap generateRandomMap(String id, String name) {
        DataMap mapData = new DataMap();
        mapData.id = id;
        mapData.name = id;
        mapData.map = new int[3][HEIGHT][WIDTH];

        Random rand = new Random();
        int random = rand.nextInt(1000);
        //System.out.println("random : " + random);

        for (int col = 0; col < WIDTH; col++) {


            float LimitOfGrasses = PerlinNoise.PerlinNoise1D((float) (random * 0.1), 0.25f, 1);
            LimitOfGrasses = (float) noiseScaling(LimitOfGrasses, 16, 16); //16
            float LimitOfRocks = PerlinNoise.PerlinNoise1D((float) (random * 0.1), 0.60f, 1);
            LimitOfRocks = (float) noiseScaling(LimitOfRocks, 40, 10);//14.2


            if (col == WIDTH / 2)
                starting((int) LimitOfGrasses, (int) LimitOfRocks, random, col, mapData);


            new GeneratorSoil().generateSoil((int) LimitOfGrasses, (int) LimitOfRocks, col, mapData, random);
            new GeneratorSubSoil().generateSubSoil((int) LimitOfGrasses, (int) LimitOfRocks, col, mapData, random);


            random++;
        }

        CalculatorLight.calculatorLightMap(mapData.map, MapLoader.HEIGHT, MapLoader.WIDTH, 0, 0);
        return mapData;

    }





    public static float noiseScaling(float noise, double amplitude, double AxeVertical) {
        noise = noise + 1;
        noise = (float) (noise * (amplitude) / 2);
        noise = (float) (noise + HEIGHT / 2 + (AxeVertical));
        return noise;
    }

    public static void starting(int LimitOfGrasses, int LimitOfRocks, int random, int col, DataMap mapData) {

        boolean starting = false;
        int i = 1;

        while (!starting) {


            int nextLimitOfGrasses = (int) noiseScaling(PerlinNoise.PerlinNoise1D((float) ((random + i) * 0.1), 0.25f, 1), 16, 16); //16
            int nextLimitOfRocks = (int) noiseScaling(PerlinNoise.PerlinNoise1D((float) ((random + i) * 0.1), 0.60f, 1), 40, 10);//14.2

            //System.out.println("next : " + i +" "+ (int)nextLimitOfGrasses + "  " + (int)nextLimitOfRocks + "\n");

            if (LimitOfGrasses < LimitOfRocks) {

                    if (nextLimitOfGrasses >= LimitOfGrasses) {
                        mapData.startingPoint = new int[]{(int) (HEIGHT - LimitOfGrasses), col + (i-1)};
                        starting = true;
                    }
                    else{
                        LimitOfGrasses = nextLimitOfGrasses;
                        LimitOfRocks = nextLimitOfRocks;
                        i++;
                    }

            } else {
                    if(nextLimitOfRocks >= LimitOfRocks && nextLimitOfGrasses >= LimitOfRocks) {
                        mapData.startingPoint = new int[]{ (HEIGHT - LimitOfRocks), col};
                        starting = true;
                    }
                    else{
                        LimitOfGrasses = nextLimitOfGrasses;
                        LimitOfRocks = nextLimitOfRocks;
                        i++;
                    }
            }


        }

    }
}
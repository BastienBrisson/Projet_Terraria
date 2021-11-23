package terraria.game.actors.world.GeneratorMap;

public class DataMap {

    public String id;
    public String name;

    /**
     * layer 0 = background
     * layer 1 = blocs
     * layer 2 = light
     */
    public int[][][] map;

    public int[] startingPoint;
    public int height;
    public int width;
    public int srcY;
    public float dayTimer;


}

package terraria.game.actors.world;
import java.util.HashMap;

/**
 * Enumeration des cases possibles dans le jeu
 */
public enum TileType {


    GRASS(1, true),
    EARTH(2, true),
    DIRT(3, true),
    CAVE(4, false),
    COAL(5, true),
    GOLD(6, true),
    DIAMOND(7, true),
    IRON(8, true),
    LAVA(9, true),
    GRASSONSTONE(10, true),
    lightOff(11, false),

    PLANT(12, false),
    STEM(13, false),
    PEBBLE(14, false),
    FILTRE0(15, false),
    FILTRE1(16, false),
    FILTRE2(17, false);



    public static final int TILE_SIZE = 32;

    private int id;
    private boolean collidable;


    private TileType(int id, boolean collidable) {
        this.id = id;
        this.collidable = collidable;

    }

    public int getId() {
        return id;
    }
    public boolean isCollidable() {
        return collidable;
    }




    public static HashMap<Integer, TileType> caseMap;
    static {
        caseMap = new HashMap<Integer, TileType>();

        for (TileType tileType : TileType.values()) {
            caseMap.put(tileType.getId(), tileType);
        }
    }

    /**
     * Retourne le TypeCase selon l'id grace à CaseMae
     * @param id
     * @return TypeCase
     */
    public static TileType getTileTypeById (int id) {
        return caseMap.get(id);
    }

}
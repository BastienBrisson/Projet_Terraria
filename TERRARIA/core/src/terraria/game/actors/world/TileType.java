package terraria.game.actors.world;
import java.util.HashMap;

/**
 * Enumeration des cases possibles dans le jeu
 */
public enum TileType {


    GRASS(1, true, "grass"),
    DIRT(2, true, "dirt"),
    STONE(3, true, "stone"),
    CAVE_BACKGROUND(4, false, "cave_bg"),
    COAL(5, true, "coal"),
    GOLD(6, true, "gold"),
    DIAMOND(7, true, "diamond"),
    IRON(8, true, "iron"),
    LAVA(9, true, "lava"),
    MOSSY_STONE(10, true, "mossy_stone"),
    DARK_BACKGROUND(11, false, "dark_bg"),

    WEED(12, false, "weed"),
    LOG(13, false, "log"),
    PEBBLE(14, false,"pebble"),
    FILTRE0(15, false, "light_filter"),
    FILTRE1(16, false,"light_filter"),
    FILTRE2(17, false, "light_filter");



    public static final int TILE_SIZE = 32;

    private int id;
    private boolean collidable;
    private String name;


    private TileType(int id, boolean collidable, String name) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public boolean isCollidable() {
        return collidable;
    }
    public String getName() {
        return name;
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
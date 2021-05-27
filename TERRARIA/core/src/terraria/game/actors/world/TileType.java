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

    WEED(11, false, "weed"),
    LOG(12, false, "log"),
    PEBBLE(13, false,"pebble"),

    LIGHTSOURCE0(22, false,"light"),
    LIGHTSOURCE1(21, false,"light"),
    LIGHTSOURCE2(20, false,"light"),
    LIGHTSOURCE3(19, false,"light"),
    LIGHTSOURCE4(18, false,"light"),
    LIGHTSOURCE5(17, false,"light"),
    LIGHTSOURCE6(16, false,"light"),
    LIGHTSOURCE7(15, false,"light"),
    NOLIGHT(14, false,"light");




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
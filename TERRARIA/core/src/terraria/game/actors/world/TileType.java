package terraria.game.actors.world;
import java.util.HashMap;

/**
 * Enumeration des cases possibles dans le jeu
 */
public enum TileType {


    GRASS(1, true, "grass", 1f),
    DIRT(2, true, "dirt", 1f),
    STONE(3, true, "stone", 2f),
    STONE_BACKGROUND(4, false, "stone_bg", 2f),
    COAL(5, true, "coal", 3f),
    GOLD(6, true, "gold", 5f),
    DIAMOND(7, true, "diamond", 6f),
    IRON(8, true, "iron", 4f),
    LAVA(9, true, "lava", 0f),
    MOSSY_STONE(10, true, "mossy_stone", 2f),
    DIRT_BACKGROUND(14,false,"dirt_bg", 0.5f),


    WEED(11, false, "weed", 0),
    LOG(12, false, "log", 2f),
    PEBBLE(13, false,"pebble", 0),


    PLANKS(15, true,"planks", 1.5f),
    SAPLING(16, false, "sapling", 0),
    TIMBER(17, true, "timber", 2f),

    STICK(18, false, "stick", 0),

    IRON_INGOT(19, false, "iron_ingot", 0),
    GOLD_INGOT(20, false, "gold_ingot", 0),
    DIAMOND_INGOT(21, false, "diamond_ingot", 0),
    COAL_INGOT(22, false, "coal_ingot", 0),

    SWORD_WOOD(23, false, "sword wood", 0),
    SWORD_STONE(24, false, "sword stone", 0),
    SWORD_IRON(25, false, "sword iron", 0),
    SWORD_DIAMOND(26, false, "sword diamond", 0),
    SWORD_GOLD(27, false, "dword gold", 0),

    PICKAXE_WOOD(28, false, "pickaxe wood", 0),
    PICKAXE_STONE(29, false, "pickaxe stone", 0),
    PICKAXE_IRON(30, false, "pickaxe iron", 0),
    PICKAXE_DIAMOND(31, false, "pickaxe diamond", 0),
    PICKAXE_GOLD(32, false, "pickaxe gold", 0),


    MUSHROOM(33, false, "mushroom", 0),
    SLIME(34, false, "slime", 0),
    RABBIT_MEAT(35, false, "rabbit_meat", 0),


    LIGHTSOURCE0(108, false,"light", 0),
    LIGHTSOURCE1(107, false,"light", 0),
    LIGHTSOURCE2(106, false,"light", 0),
    LIGHTSOURCE3(105, false,"light", 0),
    LIGHTSOURCE4(104, false,"light", 0),
    LIGHTSOURCE5(103, false,"light", 0),
    LIGHTSOURCE6(102, false,"light", 0),
    LIGHTSOURCE7(101, false,"light", 0),
    NOLIGHT(100, false,"light", 0);




    public static final int TILE_SIZE = 32;

    private int id;
    private boolean collidable;
    private String name;
    private float hardness; //time in sec that the player will take to destroy a block by hand

    private TileType(int id, boolean collidable, String name, float hardness) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        this.hardness = hardness;
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
    public float getHardness() {
        return hardness;
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
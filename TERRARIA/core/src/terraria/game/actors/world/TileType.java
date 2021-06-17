package terraria.game.actors.world;
import java.util.HashMap;

/**
 * Enumeration des cases possibles dans le jeu
 */
public enum TileType {

    EMPTY(0, true, "empty", 1f, 1, 1),
    GRASS(1, true, "grass", 1f, 1, 1),
    DIRT(2, true, "dirt", 1f, 1, 1),
    STONE(3, true, "stone", 2f, 1, 1),
    STONE_BACKGROUND(4, false, "stone_bg", 2f, 1, 1),
    COAL(5, true, "coal", 3f, 1, 1),
    GOLD(6, true, "gold", 5f, 1, 1),
    DIAMOND(7, true, "diamond", 6f, 1, 1),
    IRON(8, true, "iron", 4f, 1, 1),
    LAVA(9, true, "lava", 0f, 1, 1),
    MOSSY_STONE(10, true, "mossy_stone", 2f, 1, 1),
    DIRT_BACKGROUND(14,false,"dirt_bg", 0.5f, 1, 1),


    WEED(11, false, "weed", 0, 1, 1),
    LOG(12, false, "log", 2f, 1, 1),
    PEBBLE(13, false,"pebble", 0, 1, 1),


    PLANKS(15, true,"planks", 1.5f, 1, 1),
    SAPLING(16, false, "sapling", 0, 1, 1),
    TIMBER(17, true, "timber", 2f, 1, 1),

    STICK(18, false, "stick", 0, 1, 1),

    IRON_INGOT(19, false, "iron_ingot", 0, 1, 1),
    GOLD_INGOT(20, false, "gold_ingot", 0, 1, 1),
    DIAMOND_INGOT(21, false, "diamond_ingot", 0, 1, 1),
    COAL_INGOT(22, false, "coal_ingot", 0, 1, 1),

    SWORD_WOOD(23, false, "sword wood", 0, 1, 1.5f),
    SWORD_STONE(24, false, "sword stone", 0, 1, 2),
    SWORD_IRON(25, false, "sword iron", 0, 1, 2.5f),
    SWORD_DIAMOND(26, false, "sword diamond", 0, 1, 3.5f),
    SWORD_GOLD(27, false, "dword gold", 0, 1, 3),

    PICKAXE_WOOD(28, false, "pickaxe wood", 0, 1.5f, 1),
    PICKAXE_STONE(29, false, "pickaxe stone", 0, 2f, 1),
    PICKAXE_IRON(30, false, "pickaxe iron", 0, 2.5f, 1),
    PICKAXE_DIAMOND(31, false, "pickaxe diamond", 0, 3.5f, 1),
    PICKAXE_GOLD(32, false, "pickaxe gold", 0, 3f, 1),


    MUSHROOM(33, false, "mushroom", 0, 1, 1),
    SLIME(34, false, "slime", 0, 1, 1),
    RABBIT_MEAT(35, false, "rabbit_meat", 0, 1, 1),


    LIGHTSOURCE0(108, false,"light", 0, 1, 1),
    LIGHTSOURCE1(107, false,"light", 0, 1, 1),
    LIGHTSOURCE2(106, false,"light", 0, 1, 1),
    LIGHTSOURCE3(105, false,"light", 0, 1, 1),
    LIGHTSOURCE4(104, false,"light", 0, 1, 1),
    LIGHTSOURCE5(103, false,"light", 0, 1, 1),
    LIGHTSOURCE6(102, false,"light", 0, 1, 1),
    LIGHTSOURCE7(101, false,"light", 0, 1, 1),
    NOLIGHT(100, false,"light", 0, 1, 1);




    public static final int TILE_SIZE = 32;

    private int id;
    private boolean collidable;
    private String name;
    private float hardness; //time in sec that the player will take to destroy a block by hand
    private float efficiency;
    private float damage;

    private TileType(int id, boolean collidable, String name, float hardness, float efficiency, float damage) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        this.hardness = hardness;
        this.efficiency = efficiency;
        this.damage = damage;
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

    public float getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public float getDamage() {
        return damage;
    }
}
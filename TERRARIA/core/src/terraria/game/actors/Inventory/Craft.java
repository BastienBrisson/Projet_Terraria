package terraria.game.actors.Inventory;

import terraria.game.actors.world.TileType;

import java.util.HashMap;

/**
 * Enumeration des craft possible dans le jeu
 */
public enum Craft {

    PLANKS_CRAFT(TileType.PLANKS.getId(), 2, new int[]{TileType.TIMBER.getId()}, new int[]{1}),
    STICK_CRAFT(18, 2, new int[]{15}, new int[]{2}),

    IRON_INGOT_CRAFT(19, 1, new int[]{8}, new int[]{1}),
    GOLD_INGOT_CRAFT(20, 1, new int[]{6}, new int[]{1}),
    DIAMOND_INGOT_CRAFT(21, 1, new int[]{7}, new int[]{1}),
    COAL_INGOT_CRAFT(22, 1, new int[]{5}, new int[]{1}),

    SWORD_WOOD_CRAFT(23, 1, new int[]{18, 15}, new int[]{1, 2}),
    SWORD_STONE_CRAFT(24, 1, new int[]{18, 3}, new int[]{1, 2}),
    SWORD_IRON_CRAFT(25, 1, new int[]{18, 19}, new int[]{1, 2}),
    SWORD_DIAMOND_CRAFT(26, 1, new int[]{18, 21}, new int[]{1, 2}),
    SWORD_GOLD_CRAFT(27, 1, new int[]{18, 20}, new int[]{1, 2}),

    PICKAXE_WOOD_CRAFT(28, 1, new int[]{18, 15}, new int[]{1, 3}),
    PICKAXE_STONE_CRAFT(29, 1, new int[]{18, 3}, new int[]{1, 3}),
    PICKAXE_IRON_CRAFT(30, 1, new int[]{18, 19}, new int[]{1, 3}),
    PICKAXE_DIAMOND_CRAFT(31, 1, new int[]{18, 21}, new int[]{1, 3}),
    PICKAXE_GOLD_CRAFT(32, 1, new int[]{18, 20}, new int[]{1, 3}),

    COOK_RABBIT_FOOD(36, 1, new int[]{35, 22}, new int[]{1, 1});

    private int idItem;             //Item qui sera crée
    private int nbItem;             //Nombre de fois que l'items va être produit
    private int[] idsItemNeeded;    //Les items dont le craft a besoin
    private int[] numberItemNeeded; //Quantité de chaque item dont le craft a besoin (même ordre que idsItemNeeded)

    private Craft(int idItem, int nbItem, int[] idsItemNeeded, int[] numberItemNeeded) {
        this.idItem = idItem;
        this.nbItem = nbItem;
        this.idsItemNeeded = idsItemNeeded;
        this.numberItemNeeded = numberItemNeeded;
    }

    public static HashMap<Integer, Craft> caseMap;
    static {
        caseMap = new HashMap<Integer, Craft>();

        for (Craft craft : Craft.values()) {
            caseMap.put(craft.getIdItem(), craft);
        }
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int[] getIdsItemNeeded() {
        return idsItemNeeded;
    }

    public void setIdsItemNeeded(int[] idsItemNeeded) {
        this.idsItemNeeded = idsItemNeeded;
    }

    public int[] getNumberItemNeeded() {
        return numberItemNeeded;
    }

    public void setNumberItemNeeded(int[] numberItemNeeded) {
        this.numberItemNeeded = numberItemNeeded;
    }

    public int getNbItem() {
        return nbItem;
    }

    public void setNbItem(int nbItem) {
        this.nbItem = nbItem;
    }

    public static Craft getCraftById(int id) {
        return caseMap.get(id);
    }
}

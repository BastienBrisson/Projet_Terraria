package terraria.game.actors.Inventory;

import terraria.game.actors.world.TileType;

import java.util.HashMap;

/**
 * Enumeration des craft possible dans le jeu
 */
public enum Craft {

    PLANKS_CRAFT(15, 2, new int[]{12}, new int[]{1}),
    STICK_CRAFT(2, 2, new int[]{15}, new int[]{2});
    //RANDOM_CRAFT(7, 5, new int[]{2, 15}, new int[]{1, 2});

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

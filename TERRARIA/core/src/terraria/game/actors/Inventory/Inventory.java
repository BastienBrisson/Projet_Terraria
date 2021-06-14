package terraria.game.actors.Inventory;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import terraria.game.TerrariaGame;
import terraria.game.actors.entities.EntityLoader;
import terraria.game.actors.world.GeneratorMap.MapLoader;
import terraria.game.actors.world.TileType;

public class Inventory extends Actor {

    private TerrariaGame game;
    private static final int SLOTINVENTORYBAR = 10;     //Nombre d'objet dans la barre d'inventaire
    private static final int SIZEINVENTORY = 50;        //Nombre d'objet total de l'inventaire
    private static int currentItems = 0;                //Numéro de l'items actuellement sélectionné
    private ArrayList<Items> itemsList;                 //La liste des objets de l'inventaire
    private ArrayList<ItemsGraphic> itemsGraphic;       //La liste la classe qui gère les textures des objets de l'inventaire
    private ArrayList<Items> countItems;
    private ArrayList<ItemsGraphic> craftableItemGraphicList;
    private boolean inventoryShow;                      //Boolean qui détermine si l'inventaire est affiché ou non
    private TextureRegion[][] slot;                     //Texture de chaque slot d'inventaire
    private TextureRegion[][] hoverTexture;                  //Texture quand on passe la souris sur un slot en drag & drop

    private DragAndDrop dragAndDrop;                    //Drag & Drop de l'inventaire
    private float ScreenX, ScreenY,ScreenWidth,ScreenHeight;     //Taille de l'écran
    private int  width = 50, height = 50;                //Taille d'un slot
    private int nbCraftableItem;
    private BitmapFont font;                            //Police d'écriture
    private int currentPage;

    ImageButton leftArrow;
    ImageButton rightArrow;
    TextureRegion leftArrowImg;
    TextureRegion rightArrowImg;

    Vector3 cam;

    public Inventory(TerrariaGame game) {
        this.game = game;
        this.currentPage = 1;
        this.itemsList = new ArrayList<>();
        this.itemsGraphic = new ArrayList<>();
        this.countItems = new ArrayList<>();
        this.craftableItemGraphicList = new ArrayList<>();
        this.dragAndDrop = new DragAndDrop();
        this.inventoryShow = false;
        this.slot = TextureRegion.split(game.getAssetManager().get("inventory/slot.png", Texture.class), width, height);
        this.hoverTexture = TextureRegion.split(game.getAssetManager().get("inventory/hover.png", Texture.class), width, height);
        this.nbCraftableItem = 0;
        this.font = new BitmapFont();
        //On crée les items de l'inventaire
        for (int i = 0; i < SIZEINVENTORY; i++) {
            itemsList.add(new Items(i));
            itemsGraphic.add(new ItemsGraphic(game, itemsList.get(i), this, dragAndDrop));
        }
        for (int i = 0; i < 50; i++) {
            countItems.add(new Items(i, i));
            craftableItemGraphicList.add(new ItemsGraphic(game, new Items(i+50, 0), this, dragAndDrop));
        }
        countItems();
        leftArrowImg = new TextureRegion(new Texture(Gdx.files.internal("inventory/left_arrow.png")));
        rightArrowImg = new TextureRegion(new Texture(Gdx.files.internal("inventory/right_arrow.png")));

        leftArrow = new ImageButton(new TextureRegionDrawable(leftArrowImg));
        leftArrow.setPosition(ScreenX - (width/2), ScreenY + ScreenHeight -  (6*height+height+height/2) - height, Align.center);
        leftArrow.addListener(new ActorGestureListener() {
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (currentPage > 1) {
                    currentPage--;
                }
            }
        });

        rightArrow = new ImageButton(new TextureRegionDrawable(rightArrowImg));
        rightArrow.setPosition(ScreenX - (width/2), ScreenY + ScreenHeight -  (6*height+height+height/2) - height, Align.center);
        rightArrow.addListener(new ActorGestureListener() {
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (currentPage < 4) {
                    currentPage++;
                }
            }
        });
    }

    public void update(Camera camera, Stage stage){
        cam = camera.position;
        ScreenX =  cam.x + stage.getViewport().getScreenWidth()/2 - SLOTINVENTORYBAR * width;
        ScreenY = cam.y - stage.getViewport().getScreenHeight()/2;
        ScreenWidth =   stage.getViewport().getScreenWidth();
        ScreenHeight = stage.getViewport().getScreenHeight();

        if (isInventoryShow()) {
            leftArrow.setPosition(ScreenX + 25 , ScreenY + ScreenHeight - 12*height, Align.center);
            rightArrow.setPosition(ScreenX + 75 , ScreenY + ScreenHeight - 12*height, Align.center);
        } else {
            leftArrow.setPosition(0, 0, Align.center);
            rightArrow.setPosition(0, 0, Align.center);
        }


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int nbItems = 0;
        //On dessine les slots de la barre d'inventaire
        for (int i = 0; i < SLOTINVENTORYBAR; i++){
            if (currentItems == i) {
                batch.draw(slot[0][1], ScreenX + width *  i - (width/2), ScreenY + ScreenHeight - (height + height/2));
            } else {
                batch.draw(slot[0][0], ScreenX + width *  i - (width/2), ScreenY + ScreenHeight - (height + height/2));
            }
            if (itemsGraphic.get(i).isHover())
                batch.draw(hoverTexture[0][0], ScreenX + width *  itemsGraphic.get(i).getYPosition() - (width/2), ScreenY + ScreenHeight - (itemsGraphic.get(i).getXPosition()*height+height+height/2));
            nbItems++;
        }

        //Si l'inventaire est affiché
        if (inventoryShow) {
            font.draw(batch, String.valueOf("Inventory"),ScreenX,  ScreenY + ScreenHeight - 10);
            for (int i = 1; i < 5; i++) {
                for (int j = 0; j < SLOTINVENTORYBAR; j++) {
                    batch.draw(slot[0][0], ScreenX + width * j - (width / 2), ScreenY + ScreenHeight - (i*height + height + height / 2));
                    if (itemsGraphic.get(nbItems).isHover())
                        batch.draw(hoverTexture[0][0], ScreenX + width *  j - (width/2), ScreenY + ScreenHeight - (i*height+height+height/2));
                    nbItems++;
                }
            }

            if (nbCraftableItem != 0) {
                font.draw(batch, String.valueOf("Craftable items"),ScreenX,  ScreenY + ScreenHeight - 10 - 6*getHeightTile());
            }
            //On dessine les slots de craft disponible
            for (int craftableItem = 0; craftableItem < nbCraftableItem && craftableItem < 5; craftableItem++) {
                batch.draw(slot[0][0], ScreenX - (width/2), ScreenY + ScreenHeight -  (6*height+height+height/2) - craftableItem*height);
            }


        }




    }

    /**
     * Ajoute un élément à un slot, s'il existe un slot contenant déjà un élément
     * du même type et qu'il y a moins de 64 éléments dans ce slot. Dans le cas contraire
     * on va chercher s'il existe un slot vide pouvant acceuilir cet élement. 
     * @param idTile
     * @return
     */
    public boolean addTileInInventory(int idTile) {
        for(Items t : itemsList) {
            if(idTile == t.getIdTile() && t.getAmount() < 64) {
                t.incrAmount();
                return true;
            }
        }
        for(Items t2 : itemsList) {
            if(t2.getIdTile() == 0) {
                t2.setIdTile(idTile);
                t2.incrAmount();
                return true;
            }
        }

        return false;
    }


    /**
     * Enlève un élément de l'inventaire et supprime complètement l'élément du slot
     * quand le nombre d'élement descend à 0.
     */
    public void delTileInInventory() {
        Items currentSlot = itemsList.get(currentItems);
        if(currentSlot.getAmount() == 1) {
            currentSlot.lastElement();
        } else {
            currentSlot.decrAmount();
        }
    }

    public void fillInventory(ArrayList<Items> inv) {
        int indice = 0;
        if (inv != null) {
            for(Items i : inv) {
                this.itemsList.set(indice, i);
                this.getGraphicItems().set(indice, new ItemsGraphic(game, this.itemsList.get(indice),this, dragAndDrop));
                indice++;
            }
        }
    }

    public int getCurrentItems() {
        return currentItems;
    }

    public static void setCurrentItems(int tile) {
        currentItems = tile;
    }

    public ArrayList<Items> getItemsList() {
        return itemsList;
    }

    public ArrayList<ItemsGraphic> getGraphicItems() {
        return itemsGraphic;
    }

    public boolean isInventoryOpen() {
        return this.inventoryShow;
    }

    public boolean isInventoryShow() {
        return inventoryShow;
    }

    public void setInventoryShow(boolean inventoryShow) {
        this.inventoryShow = inventoryShow;
    }

    public int getWidthTile() {
        return width;
    }

    public void setWidthTile(int width) {
        this.width = width;
    }

    public int getHeightTile() {
        return height;
    }

    public void setHeightTile(int height) {
        this.height = height;
    }

    public void countItems() {
        for (Items item : this.countItems) {
            this.countItems.get(item.getNum()).setAmount(0);
        }
        for (Items item : itemsList) {
            countItems.get(item.getIdTile()).addAmount(item.getAmount());
        }
    }

    public void updateCraftableItem() {
        int numCraftItem = 0;
        boolean craftable = true;
        Craft checkIfCraftable;
        for (int itemsCraf = 0; itemsCraf < Craft.values().length; itemsCraf++) {
            checkIfCraftable = Craft.values()[itemsCraf];
            int i = 0;
            while (i < checkIfCraftable.getIdsItemNeeded().length && craftable) {
                if (countItems.get(checkIfCraftable.getIdsItemNeeded()[i]).getAmount() < checkIfCraftable.getNumberItemNeeded()[i] && craftable) {
                    craftable = false;
                }
                i++;
            }
            System.out.println(craftable);
            if (craftable) {
                craftableItemGraphicList.get(numCraftItem).getItem().setIdTile(checkIfCraftable.getIdItem());
                craftableItemGraphicList.get(numCraftItem).getItem().setAmount(checkIfCraftable.getNbItem());
                numCraftItem++;
            }
            craftable=true;
        }
        nbCraftableItem = numCraftItem;
        for (int i = numCraftItem; i < 50; i++) {
            craftableItemGraphicList.get(i).getItem().setIdTile(0);
            craftableItemGraphicList.get(i).getItem().setAmount(0);
        }
    }


    public float getScreenX() {
        return ScreenX;
    }

    public void setScreenX(float screenX) {
        ScreenX = screenX;
    }

    public float getScreenY() {
        return ScreenY;
    }

    public void setScreenY(float screenY) {
        ScreenY = screenY;
    }

    public ArrayList<ItemsGraphic> getCraftableItemGraphicList() {
        return craftableItemGraphicList;
    }

    public void setCraftableItemGraphicList(ArrayList<ItemsGraphic> craftableItemGraphicList) {
        this.craftableItemGraphicList = craftableItemGraphicList;
    }

    public void updateCraft() {
        countItems();
        updateCraftableItem();;
    }

    public void costUpdate(Craft craft) {
        for (int i = 0; i < craft.getIdsItemNeeded().length; i++) {
            int num = craft.getNumberItemNeeded()[i];
            int inv = 0;
            while (inv < craftableItemGraphicList.size() && num > 0) {
                if (itemsList.get(inv).getIdTile() == craft.getIdsItemNeeded()[i]) {
                    while (itemsList.get(inv).getAmount() > 0 && num > 0) {
                        itemsList.get(inv).decrAmount();
                        num--;
                    }
                }
                inv++;
            }
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ImageButton getLeftArrow() {
        return leftArrow;
    }

    public void setLeftArrow(ImageButton leftArrow) {
        this.leftArrow = leftArrow;
    }

    public ImageButton getRightArrow() {
        return rightArrow;
    }

    public void setRightArrow(ImageButton rightArrow) {
        this.rightArrow = rightArrow;
    }
}
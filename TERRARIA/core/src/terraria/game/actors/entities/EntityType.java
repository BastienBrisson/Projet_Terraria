package terraria.game.actors.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import terraria.game.TerrariaGame;
import terraria.game.actors.world.GameMap;
import terraria.game.screens.GameScreen;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public enum EntityType {

    PLAYER("player", Player.class, 40, 40, 52 ),
    SHROOM("mushroom", Mushroom.class, 20, 32, 32);

    private String id;
    private Class loaderClass;
    public float weight;
    public float  width, height;


    private EntityType(String id, Class loaderClass, float weight, float width, float height) {
        this.id = id;
        this.loaderClass = loaderClass;
        this.weight = weight;
        this.height = height;
        this.width = width;

    }

    public String getId() {
        return id;
    }


    public static Entity createEntityUsingSnapshot (EntitySnapshot entitySnapshot, GameMap gameMap, TerrariaGame game) {
        EntityType type = entityTypes.get(entitySnapshot.type);
        try {
            Entity entity = (Entity) ClassReflection.newInstance(type.loaderClass);
            entity.create(entitySnapshot, type, gameMap, game);
            return entity;
        } catch (ReflectionException e) {
            Gdx.app.error("Entity Loader", "Could not load entity of type " + type.id);
            return null;
        }
    }

    private static HashMap<String, EntityType> entityTypes;

    static {
        entityTypes = new HashMap<String, EntityType>();
        for (EntityType type : EntityType.values())
            entityTypes.put(type.id, type);
    }

}
package terraria.game.actors.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import terraria.game.actors.world.GameMap;
import terraria.game.actors.world.TileType;
import terraria.game.screens.GameScreen;

import java.util.ArrayList;

public class EntityLoader {

    private static Json json = new Json();

    public static ArrayList<Entity> loadEntities (String id, GameMap gameMap, GameScreen gameScreen) {
        Gdx.files.local("saves/").file().mkdirs();
        FileHandle file = Gdx.files.local("saves/" + id + ".entities");
        ArrayList<Entity> entities = new ArrayList<Entity>();

        if (file.exists()) {
            EntitySnapshot[] snapshots = json.fromJson(EntitySnapshot[].class, file.readString());
            for (EntitySnapshot snapshot : snapshots) {
                entities.add(EntityType.createEntityUsingSnapshot(snapshot, gameMap, gameScreen));
            }
            return entities;

        } else {
            Player player = new Player();
            player.create(gameMap.getStartingPoint()[1] * TileType.TILE_SIZE,  gameMap.getStartingPoint()[0] * TileType.TILE_SIZE,EntityType.PLAYER,gameMap,gameScreen);
            entities.add(player);
            saveEntities(id, entities);
            return entities;
        }
    }

    public static void saveEntities (String id, ArrayList<Entity> entities) {
        ArrayList<EntitySnapshot> snapshots = new ArrayList<EntitySnapshot>();
        for (Entity entity : entities)
            snapshots.add(entity.getSaveSnapshot());

        Gdx.files.local("saves/").file().mkdirs();
        FileHandle file = Gdx.files.local("saves/" + id + ".entities");
        file.writeString(json.prettyPrint(snapshots), false);
    }

}

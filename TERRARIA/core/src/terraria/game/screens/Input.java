package terraria.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import terraria.game.TerrariaGame;
import terraria.game.actors.Inventory.Inventory;
import terraria.game.actors.entities.EntityLoader;
import terraria.game.actors.entities.EntityType;
import terraria.game.actors.world.GeneratorMap.MapLoader;

public class Input implements InputProcessor {

    private GameScreen screen;
    private int zoomIndice = 0;

    public Input(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        /*switch (keycode) {
            //Save
            case Keys.ALT_RIGHT:
                EntityLoader.saveEntities("test", this.screen.entities);
                MapLoader.saveMap(this.screen.gameMap.getId(), this.screen.gameMap.getName(), this.screen.gameMap.getMap(), this.screen.gameMap.getStartingPoint());
                break;

            //Spawn mobs
            case Keys.M:
                screen.spawnMob(EntityType.MUSHROOM);
                break;
            case Keys.L:
                screen.spawnMob(EntityType.RABBIT);
                break;
            case Keys.K:
                screen.spawnMob(EntityType.SLIME);
                break;

            default:
                break;
        }*/
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(TerrariaGame.getState() == GameScreen.GAME_RUNNING) {
            switch (character) {
                case '&':
                    Inventory.setCurrentItems(0);
                    break;
                case 'é':
                    Inventory.setCurrentItems(1);
                    break;
                case '"':
                    Inventory.setCurrentItems(2);
                    break;
                case '\'':
                    Inventory.setCurrentItems(3);
                    break;
                case '(':
                    Inventory.setCurrentItems(4);
                    break;
                case '-':
                    Inventory.setCurrentItems(5);
                    break;
                case 'è':
                    Inventory.setCurrentItems(6);
                    break;
                case '_':
                    Inventory.setCurrentItems(7);
                    break;
                case 'ç':
                    Inventory.setCurrentItems(8);
                    break;
                case 'à':
                    Inventory.setCurrentItems(9);
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return true; }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if(TerrariaGame.getState() == GameScreen.GAME_RUNNING) {
            if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.CONTROL_LEFT)) {
                float ratio = screen.getStage().getViewport().getWorldWidth()/screen.getStage().getViewport().getWorldHeight();

                if(amountY == 1) {
                    //System.out.println(ratio);
                    if(this.zoomIndice < 2) {

                        screen.getCamera().zoom += 0.1f;

                        float width = screen.getStage().getViewport().getWorldWidth();

                        this.screen.getStage().getViewport().setScreenWidth(( (screen.getStage().getViewport().getScreenWidth() + (int)(width * 0.1))));
                        this.screen.getStage().getViewport().setScreenHeight( (screen.getStage().getViewport().getScreenHeight() + (int)((width * 0.1)/ratio)));

                        zoomIndice += 1;
                    }
                } else if(amountY == -1) {
                    if (this.zoomIndice > -3) {
                        screen.getCamera().zoom -= 0.1f;

                        float width = screen.getStage().getViewport().getWorldWidth();

                        this.screen.getStage().getViewport().setScreenWidth( (screen.getStage().getViewport().getScreenWidth() - (int)(width * 0.1)));
                        this.screen.getStage().getViewport().setScreenHeight( (screen.getStage().getViewport().getScreenHeight() - (int)((width * 0.1)/ratio)));

                        zoomIndice -= 1;
                    }
                }
            } else {
                if(amountY == 1) {
                    if(screen.inventory.getCurrentItems() == 9) {
                        Inventory.setCurrentItems(0);
                    } else {
                        Inventory.setCurrentItems(screen.inventory.getCurrentItems() + 1);
                    }
                } else if(amountY == -1) {
                    if(screen.inventory.getCurrentItems() == 0) {
                        Inventory.setCurrentItems(9);
                    } else {
                        Inventory.setCurrentItems(screen.inventory.getCurrentItems() - 1);
                    }
                }
            }
        }
        return false;
    }
}

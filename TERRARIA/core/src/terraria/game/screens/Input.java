package terraria.game.screens;

import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {

    private GameScreen screen;
    private int zoomIndice = 0;

    public Input(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

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
        return false;
    }
}

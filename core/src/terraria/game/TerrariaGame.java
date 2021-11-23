package terraria.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import terraria.game.screens.MainMenuScreen;

public class TerrariaGame extends Game {
	private static int state;
	private AssetManager assetManager;

	@Override
	public void create () {

		assetManager = new AssetManager();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		assetManager.clear();
		assetManager.dispose();
	}

	public static int getState() {
		return state;
	}

	public static void setState(int newState) {
		state = newState;
	}

	public AssetManager getAssetManager(){
		return assetManager;
	}
}

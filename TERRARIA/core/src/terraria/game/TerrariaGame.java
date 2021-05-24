package terraria.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import terraria.game.screens.MainMenuScreen;

public class TerrariaGame extends Game {

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

	public AssetManager getAssetManager(){
		return assetManager;
	}
}

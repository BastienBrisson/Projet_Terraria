package terraria.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import terraria.game.screens.GameScreen;
import terraria.game.screens.MainScreen;

public class TerrariaGame extends Game {

	private AssetManager assetManager;

	@Override
	public void create () {

		assetManager = new AssetManager();

		/*On demarre l'ecran de jeu*//*
		this.setScreen(new GameScreen());*/

		this.setScreen(new MainScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
	}

	public AssetManager getAssetManager(){
		return assetManager;
	}
}

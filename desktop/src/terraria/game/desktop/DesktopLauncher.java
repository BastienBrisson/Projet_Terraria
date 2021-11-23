package terraria.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import terraria.game.TerrariaGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Untitled Terraria Project";
		config.fullscreen = false;
		config.width = 1280;
		config.height = 720;

		config.addIcon("icons/icon_16.png", Files.FileType.Internal);
		config.addIcon("icons/icon_32.png", Files.FileType.Internal);
		config.addIcon("icons/icon_128.png", Files.FileType.Internal);

		new LwjglApplication(new TerrariaGame(), config);

	}
}

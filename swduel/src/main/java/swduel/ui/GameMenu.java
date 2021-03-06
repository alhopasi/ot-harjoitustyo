package swduel.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Luokka huolehtii pelin päävalikosta ja kentän valinnasta.
 */
public class GameMenu {

    private String version;
    private List<String> arenas;
    private int arenaNumber;
    private Label arenaLabel;
    private Stage stage;
    private Game game;
    private AudioHandler musicHandler;

    /**
     * Luokka huolehtii päävalikon sisällöstä ja käynnistää pelin
     * taustamusiikin.
     *
     * @param stage Näytettävä stage
     */
    public GameMenu(Stage stage) {
        this.stage = stage;
        this.version = "0.32";
        this.arenas = readArenaNames();
        this.musicHandler = new AudioHandler();
        musicHandler.setMusicPath("sounds/music.mp3");
        musicHandler.playMusic();
    }

    /**
     * Areenan nimi joka on valittuna. Käytetään peliä käynnistettäessä hakemaan
     * oikea areenan nimi.
     *
     * @return Palauttaa areenan joka on valittuna arenaNumberilla.
     */
    public String getChosenArena() {
        return arenas.get(arenaNumber);
    }

    /**
     * Rakentaa päävalikon näytettäväksi
     *
     * @return Näytettävä scene
     */
    public Scene getScene() {
        VBox centerBox = createCenterBox();
        VBox bottomBox = createBottomBox();

        BorderPane window = new BorderPane();
        window.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        window.setBottom(bottomBox);
        window.setCenter(centerBox);

        Scene menuScene = new Scene(window);

        game = new Game(stage, menuScene);

        addKeyHandler(menuScene);

        return menuScene;
    }

    private List<String> readArenaNames() {

        List<String> arenaList = new ArrayList<>();
        for (File file : new File("arenas/").listFiles()) {
            arenaList.add(file.getName());
        }
        Collections.sort(arenaList);
        return arenaList;
    }

    private VBox createBottomBox() {
        String infoString = "How to play:\n"
                + "eliminate your opponent until you win.\n"
                + "          Player 1 keys:    Player 2 keys:\n"
                + "Movement: A, D              Left Arrow, Right Arrow\n"
                + "Jetpack:  W                 Up Arrow\n"
                + "Attack:   TAB               Right CTRL";
        Label infoLabel = createLabel(infoString, 16);
        Label musicInfoLabel = createLabel("Press 'M' to toggle music", 16);
        musicInfoLabel.setPadding(new Insets(5, 5, 5, 5));

        Label versionLabel = createLabel("Version " + version, 16);
        HBox versionBox = new HBox();
        versionBox.getChildren().add(versionLabel);

        VBox bottomBox = new VBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(infoLabel, musicInfoLabel, versionBox);

        return bottomBox;
    }

    private VBox createCenterBox() {
        Label titleLabel = createLabel("SW Duel", 60);
        titleLabel.setPadding(new Insets(160, 20, 20, 20));
        Label playLabel = createLabel("<< Press Enter >>", 30);
        arenaLabel = createLabel(arenas.get(arenaNumber), 14);
        Label arenaInfoLabel = createLabel("Use arrows to select arena", 12);
        arenaInfoLabel.setPadding(new Insets(0, 20, 20, 20));
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(titleLabel, playLabel, arenaLabel, arenaInfoLabel);

        return centerBox;
    }

    private Label createLabel(String text, int size) {
        Label label = new Label(text);
        label.setPadding(new Insets(20, 20, 20, 20));
        label.setTextFill(Color.LIGHTBLUE);
        label.setFont(new Font("Consolas", size));

        return label;
    }

    private void addKeyHandler(Scene menuScene) {
        menuScene.setOnKeyPressed((event) -> {
            handleArenaChange(event);
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            } else if (event.getCode() == KeyCode.ENTER) {
                stage.setScene(game.getScene());
                game.initGame(getChosenArena());
            } else if (event.getCode() == KeyCode.M) {
                musicHandler.toggleMusic();
            }
        });
    }

    private void handleArenaChange(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            if (event.getCode() == KeyCode.LEFT) {
                arenaNumber--;
                if (arenaNumber < 0) {
                    arenaNumber = arenas.size() - 1;
                }
            } else {
                arenaNumber++;
                if (arenaNumber == arenas.size()) {
                    arenaNumber = 0;
                }
            }
            arenaLabel.setText(arenas.get(arenaNumber));
        }
    }
}

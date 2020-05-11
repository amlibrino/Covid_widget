package org.hmid.gui.widget;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Window;
import org.hmid.config.ConfigModel;
import org.hmid.config.ConfigurationService;
import org.hmid.datafetch.DataProviderService;
import org.hmid.datafetch.model.CountryData;
import org.hmid.datafetch.model.CovidDataModel;
import org.hmid.datafetch.model.GlobalData;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WidgetController implements Initializable {
    //planificateur
    private ScheduledExecutorService executorService;
    private ConfigModel configModel;


    @FXML
    public AnchorPane rootPane;
    @FXML
    public Text textGlobalReport;
    @FXML
    public Text textCountryCode;
    @FXML
    public Text textCountryReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            configModel = new ConfigurationService().getConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //on appel la methode initialize
        initializeSchedulerService();
        //initiializer l appli et afficher quitter ou rafrichir
        initializeContextMenu();
        textCountryCode.setText(configModel.getCountryCode());

    }

    // on cree la methode initilaz...
    private void initializeSchedulerService() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::loadData, 0, configModel.getRefreshIntervalInSeconds(), TimeUnit.SECONDS);
    }
        // attention au processus de javaFX (charger les donnees dans la partie javafx)
        private void loadData() {
            DataProviderService dataProviderService = new DataProviderService();
            CovidDataModel covidDataModel = dataProviderService.getData(configModel.getCountryName());
            // attention au processus de javaFX
            Platform.runLater(()->{
                // déploiement des données dans l'affichage
                inflateData(covidDataModel);
            });

        }  //va chercher lesdonnées coviddatamodel



    private void inflateData(CovidDataModel covidDataModel) {
        GlobalData globalData = covidDataModel.getGlobalData();
        CountryData countryData = covidDataModel.getCountryData();
        //affectons les donnees aux champs
        textGlobalReport.setText(getFormatedData(globalData.getCases(),globalData.getRecovered(),globalData.getDeaths()));
        textCountryReport.setText(getFormatedData(countryData.getCases(),countryData.getRecovered(),countryData.getDeaths()));
        // redimentionnement du stage
        readjustStage(textCountryCode.getScene().getWindow());

    }

    private void readjustStage(Window stage) {
        stage.sizeToScene();
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(visualBounds.getMaxX() - 25 - textCountryCode.getScene().getWidth());
        stage.setY(visualBounds.getMinY() + 25);
    }

    // formatage du texte pour recuperer juste les champs dont on a besoin
    private String getFormatedData(long totalCases, long recoveredCases, long totalDeath){
        return String.format("Cas: %-8d | Guéris: %-7d | Morts: %-6d",totalCases,recoveredCases,totalDeath);
        // %-8d : - pour alligné a droit, 8 max 8caracteres,
    }

    //ajouter un menu pour quitter et rafraichir manuellement
    private void initializeContextMenu() {
        MenuItem exitItem = new MenuItem("Quitter");
        exitItem.setOnAction(event -> {
            System.exit(0);
        });
        MenuItem refreshItem = new MenuItem("Rafraichir");
        refreshItem.setOnAction(event -> {
            executorService.schedule(this::loadData,0,TimeUnit.SECONDS);
        });
        final ContextMenu contextMenu = new ContextMenu(exitItem, refreshItem);
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(rootPane, event.getScreenX(), event.getScreenY());
            } else {
                if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }
            }
        });
    }
}

package main;

import javafx.application.Preloader.StateChangeNotification;
import javafx.application.Preloader.ProgressNotification;
import javafx.concurrent.Task;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.view.SearchController;
import javafx.scene.image.Image;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    BooleanProperty ready = new SimpleBooleanProperty(false);

    @Override
    public void start(Stage primaryStage) {
        longStart();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("THE PANAMA EXPRESS");
        this.primaryStage.getIcons().add(new Image("file:/home/jan/Development/panama_express/src/main/logo5_small2_no_schrift stock-illustration-23004462-old-fashioned-steam-train-in-schwarz-und-weiß.jpg"));

        ready.addListener(new ChangeListener<Boolean>(){
            public void changed(
                    ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (Boolean.TRUE.equals(t1)) {
                    Platform.runLater(new Runnable() {
                        public void run() {

                            initRootLayout();
                            showSearchWindow();

                        }
                    });
                }
            }
        });;
    }


    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {

        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showSearchWindow() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/search_window.fxml"));
            AnchorPane search_window = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(search_window);

            SearchController controller = loader.getController();
//            controller.setMainApp(this);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void showChartWindow( PieChart chart) throws Exception{
        Stage dialog = new Stage();
       FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/chart_window.fxml"));
//        Parent page = (Parent) FXMLLoader.load(MainApp.class.getResource("view/chart_window.fxml"));

        BorderPane chartlayout = (BorderPane) loader.load();
        Scene scene = new Scene(chartlayout);



        chartlayout.setCenter(chart);
//        ((BorderPane) scene.getRoot()).getChildren().add(chart);

        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {

        return primaryStage;
    }


    private void longStart() {
        //simulate long init in background
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int max = 10;
                for (int i = 1; i <= max; i++) {
                    Thread.sleep(300);
                    // Send progress to preloader
                    notifyPreloader(new ProgressNotification(((double) i)/max));
                }
                // After init is ready, the app is ready to be shown
                // Do this before hiding the preloader stage to prevent the
                // app from exiting prematurely
                ready.setValue(Boolean.TRUE);

                notifyPreloader(new StateChangeNotification(
                        StateChangeNotification.Type.BEFORE_START));

                return null;
            }
        };
        new Thread(task).start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
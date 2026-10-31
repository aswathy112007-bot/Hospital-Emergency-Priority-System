import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class HospitalEmergencyApp extends Application {
    private int idCounter = 1000;
    private TabPane tabPane;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hospital Emergency Priority System (JavaFX)");

        tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createDashboardTab(),
            createRegistrationTab(),
            createQueueTab(),
            createAlertsTab(),
            createRecordsTab(),
            createReportsTab()
        );

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(tabPane);

        Scene scene = new Scene(mainLayout, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createDashboardTab() {
        Tab tab = new Tab("1. Dashboard");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-faint-color: #f4f6f9;");

        Label title = new Label("Hospital Emergency & Priority Overview");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;");

        HBox statsCardBox = new HBox(15);
        statsCardBox.getChildren().addAll(
            createCard("Total Registered", String.valueOf(HospitalDAO.getAllPatients().size()), "#3B82F6"),
            createCard("Waiting Queue", String.valueOf(HospitalDAO.getActivePriorityQueue().size()), "#F59E0B"),
            createCard("Active Alerts", String.valueOf(HospitalDAO.getAllPatients().stream().filter(Patient::isEmergency).count()), "#EF4444")
        );

        content.getChildren().addAll(title, statsCardBox);
        tab.setContent(content);
        return tab;
    }

    private VBox createCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-text-fill: #6B7280; -fx-font-weight: bold;");
        
        Label lblVal = new Label(value);
        lblVal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        
        card.getChildren().addAll(lblTitle, lblVal);
        return card;
    }

    private Tab createRegistrationTab() {
        Tab tab = new Tab("2. Register Patient");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);

        TextField txtName = new TextField();
        TextField txtAge = new TextField();
        ComboBox<String> cbGender = new ComboBox<>(FXCollections.observableArrayList("Male", "Female", "Other"));
        TextField txtContact = new TextField();
        TextArea txtSymptoms = new TextArea();
        txtSymptoms.setPrefRowCount(3);

        grid.addRow(0, new Label("Patient Name:"), txtName);
        grid.addRow(1, new Label("Age:"), txtAge);
        grid.addRow(2, new Label("Gender:"), cbGender);
        grid.addRow(3, new Label("Contact Number:"), txtContact);
        grid.addRow(4, new Label("Symptoms / Reason:"), txtSymptoms);

        Button btnRegister = new Button("Register Patient");
        btnRegister.setStyle("-fx-background-color: #1E3A8A; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Label lblMsg = new Label();

        btnRegister.setOnAction(e -> {
            try {
                String id = "P-" + (++idCounter);
                String name = txtName.getText();
                int age = Integer.parseInt(txtAge.getText());
                String gender = cbGender.getValue();
                String contact = txtContact.getText();
                String symptoms = txtSymptoms.getText();

                Patient patient = new Patient(id, name, age, gender, contact, symptoms, LocalDateTime.now(), "Pending Assessment", 3, false, "Waiting");
                HospitalDAO.savePatient(patient);

                lblMsg.setText("Successfully registered! ID: " + id);
                lblMsg.setStyle("-fx-text-fill: green;");
                txtName.clear(); txtAge.clear(); txtContact.clear(); txtSymptoms.clear();
            } catch (Exception ex) {
                lblMsg.setText("Error: Please check input values.");
                lblMsg.setStyle("-fx-text-fill: red;");
            }
        });

        VBox layout = new VBox(15, grid, btnRegister, lblMsg);
        layout.setPadding(new Insets(20));
        tab.setContent(layout);
        return tab;
    }

    private Tab createQueueTab() {
        Tab tab = new Tab("3. Patient Queue");
        tab.setClosable(false);

        TableView<Patient> table = new TableView<>();
        TableColumn<Patient, String> colId = new TableColumn<>("Patient ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        
        TableColumn<Patient, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Patient, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Patient, Integer> colUrgency = new TableColumn<>("Urgency");
        colUrgency.setCellValueFactory(new PropertyValueFactory<>("urgencyLevel"));

        table.getColumns().addAll(colId, colName, colStatus, colUrgency);

        Button btnRefresh = new Button("Refresh Priority Queue");
        btnRefresh.setOnAction(e -> {
            PriorityQueue<Patient> pq = HospitalDAO.getActivePriorityQueue();
            table.setItems(FXCollections.observableArrayList(pq));
        });

        VBox layout = new VBox(10, btnRefresh, table);
        layout.setPadding(new Insets(15));
        tab.setContent(layout);
        return tab;
    }

    private Tab createAlertsTab() {
        Tab tab = new Tab("4. Emergency Alerts");
        tab.setClosable(false);
        Label lbl = new Label("Emergency Alert Monitor (Red Level Cases)");
        lbl.setStyle("-fx-font-size: 16px; -fx-text-fill: red; -fx-font-weight: bold;");
        VBox layout = new VBox(15, lbl);
        layout.setPadding(new Insets(20));
        tab.setContent(layout);
        return tab;
    }

    private Tab createRecordsTab() {
        Tab tab = new Tab("5. Patient Records");
        tab.setClosable(false);
        TableView<Patient> table = new TableView<>();
        // Populate master records view
        tab.setContent(new VBox(table));
        return tab;
    }

    private Tab createReportsTab() {
        Tab tab = new Tab("6. Reports");
        tab.setClosable(false);
        VBox layout = new VBox(15, new Label("Hospital Operational Reports & Statistics"));
        layout.setPadding(new Insets(20));
        tab.setContent(layout);
        return tab;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
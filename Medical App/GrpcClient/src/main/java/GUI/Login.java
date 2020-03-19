package GUI;
import Controller.Controller;
import Model.MedicationPlanDTO;
import grpc.User;
import grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableViewSkinBase;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;

import javax.xml.crypto.Data;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Login extends Application {

	private Stage window;
	private Scene loginScene,patientScene;
	private Controller controller;
	private Text clock;
	private Text simulatedClock;
	private TableView<MedicationPlanDTO> table = new TableView<>();
	private ObservableList<MedicationPlanDTO> tvObservableList = FXCollections.observableArrayList();
	private List<MedicationPlanDTO> medPlanList;
	private int[] simultedTime = new int[3];
	private static int renderFlag;
	private String usernamePatient;
	private boolean isFirstCall = false;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Login Form Window");
		controller = new Controller();
		medPlanList =  new LinkedList<>();
		clock =  new Text();
		simulatedClock = new Text();
		loginPage();
		simulationClock();
		realTimeClock();
	}

	private boolean checkIntakeIntervals(String[] hours)
	{
		if((Integer.parseInt(hours[0]) > simultedTime[0]) || (Integer.parseInt(hours[0]) == simultedTime[0] &&
				((Integer.parseInt(hours[1]) > simultedTime[1])||(Integer.parseInt(hours[2]) > simultedTime[2]))))
		{
			return false;
		}
		return true;
	}

	private List<String> checkNotTaken()
	{
		List<String> request  =  new ArrayList<>();
		for(MedicationPlanDTO m:this.medPlanList)
		{
			String [] limitTime = m.getIntakeMoments().split("-",2);
			String [] hours =  limitTime[1].split(":",3);
			if(m.getTaken().equalsIgnoreCase("false") && checkIntakeIntervals(hours) == false)
			{
				request.add(usernamePatient + " didn't take the " + m.getMedName() + " pill in time");
			}
		}
		return request;
	}

	public void simulationClock() {
		simultedTime[0] = 21;
		simultedTime[1] = 58;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				int size =0;
				while (true) {
					String clockSim = ((simultedTime[0]<10)? "0"+simultedTime[0]:simultedTime[0]) + ":" +
							((simultedTime[1]<10)? "0"+simultedTime[1]:simultedTime[1]) + ":" +
							((simultedTime[2]<10)? "0"+simultedTime[2]:simultedTime[2]);
					//System.out.println(clockSim);
					simulatedClock.setText(clockSim);
					try {
						Thread.sleep(300);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}

					if(simultedTime[2]>59)
					{
						simultedTime[1] ++;
						simultedTime[2] = 0;
					}

					if(simultedTime[1] >59)
					{
						if(renderFlag == 1) {
							List<String> result = checkNotTaken();
							controller.publishNotTaken(result, usernamePatient);
						}
						simultedTime[0]++;
						simultedTime[1] = 0;
					}

					if(simultedTime[0] == 24)
					{
						if(medPlanList.size()!=0)
						{
							for(MedicationPlanDTO m:medPlanList) {
								controller.pubishTaken(m.getIdMedicationRecipe(), "reset");
							}
						}

						if(renderFlag == 1) {
							tvObservableList.clear();
							fillTableObservableListWithSampleData();
						}
						simultedTime[0] = 0;
						simultedTime[1] = 0;
						simultedTime[2] = 0;
					}
					simultedTime[2] ++;
				}
			}
		};

		Thread t = new Thread(runnable);
		t.setDaemon(true);
		t.start();
	}

	public void realTimeClock() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (true) {
					java.util.Date date = new java.util.Date();
					String formatTimeStr = "hh:mm:ss";
					DateFormat formatTime = new SimpleDateFormat(formatTimeStr);
					String formattedTimeStr = formatTime.format(date);
					clock.setText(formattedTimeStr);
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread t = new Thread(runnable);
		t.setDaemon(true);
		t.start();
	}

	private void setTableappearance() {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.prefHeightProperty().bind(window.heightProperty());
		table.prefWidthProperty().bind(window.widthProperty());
	}

	private void fillTableObservableListWithSampleData() {
		medPlanList = controller.getMedicationPlan();
		tvObservableList.addAll(medPlanList);
		table.setItems(tvObservableList);
		table.refresh();
	}

	private void addButtonToTable() {
		TableColumn<MedicationPlanDTO, Void> colBtn = new TableColumn("");
		Callback<TableColumn<MedicationPlanDTO, Void>, TableCell<MedicationPlanDTO, Void>> cellFactory = new Callback<TableColumn<MedicationPlanDTO, Void>, TableCell<MedicationPlanDTO, Void>>() {
			@Override
			public TableCell<MedicationPlanDTO, Void> call(final TableColumn<MedicationPlanDTO, Void> param) {
				final TableCell<MedicationPlanDTO, Void> cell = new TableCell<MedicationPlanDTO, Void>() {

					private final Button btn = new Button("Take");
					{
						btn.setOnAction((ActionEvent event) -> {
							MedicationPlanDTO data = getTableView().getItems().get(getIndex());
							controller.pubishTaken(data.getIdMedicationRecipe(),"notReset");
							medPlanList = controller.getMedicationPlan();
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				cell.setStyle("-fx-alignment: CENTER;");
				return cell;
			}
		};

		colBtn.setCellFactory(cellFactory);

		table.getColumns().add(colBtn);
	}

	private void patientPage() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);

		GridPane gridHeader = new GridPane();
		gridHeader.setAlignment(Pos.CENTER);

		Text username = new Text();
		username.setFont(Font.font("Tahoma", FontWeight.LIGHT, 25));
		username.setText("Pill dispenser for " + usernamePatient);

		setTableappearance();
		fillTableObservableListWithSampleData();

		if(isFirstCall == false) {
			TableColumn<MedicationPlanDTO, String> colStartTime = new TableColumn<>("StartTime");
			colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));

			TableColumn<MedicationPlanDTO, String> colEndTime = new TableColumn<>("EndTime");
			colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));

			TableColumn<MedicationPlanDTO, String> colMedName = new TableColumn<>("MedicationName");
			colMedName.setCellValueFactory(new PropertyValueFactory<>("medName"));

			TableColumn<MedicationPlanDTO, String> intakeMom = new TableColumn<>("IntakeMoments");
			intakeMom.setCellValueFactory(new PropertyValueFactory<>("intakeMoments"));

			TableColumn<MedicationPlanDTO, String> dosage = new TableColumn<>("Dosage");
			dosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));

			TableColumn<MedicationPlanDTO, String> sideEffects = new TableColumn<>("SideEffects");
			sideEffects.setCellValueFactory(new PropertyValueFactory<>("sideEffects"));

			table.getColumns().addAll(colStartTime, colEndTime, colMedName, intakeMom, dosage, sideEffects);

			addButtonToTable();
		}

		Button logoutBtn = new Button();
		logoutBtn.setText("Logout");

		clock.setFont(Font.font("Tahoma", FontWeight.LIGHT, 30));
		gridHeader.add(this.clock, 0, 1);

		simulatedClock.setFont(Font.font("Tahoma", FontWeight.LIGHT, 30));
		gridHeader.add(this.simulatedClock, 1, 1);

		gridHeader.add(logoutBtn, 1, 0);
		logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.logout();
				username.setText("");
				usernamePatient = "";
				tvObservableList.clear();
				medPlanList.clear();
				window.setScene(loginScene);
				renderFlag = 0;
			}
		});

		gridHeader.add(username, 0, 0);
		grid.add(gridHeader, 0, 0);
		grid.add(table, 0, 1);

		GridPane.setHalignment(username, HPos.RIGHT);
		patientScene = new Scene(grid);
		renderFlag = 1;
		window.setScene(patientScene);
		window.show();
		isFirstCall=true;
	}

	private void loginPage()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(10));

		Text welcomeTxt =  new Text("Login");
		welcomeTxt.setFont(Font.font("Tahoma", FontWeight.LIGHT,25));
		grid.add(welcomeTxt,0,0);

		Label labelUser  = new Label("Username");
		grid.add(labelUser,0,1);

		TextField  txtUser =  new TextField();
		txtUser.setPromptText("username");
		grid.add(txtUser,1,1);

		Label labelPassword  = new Label("Password");
		grid.add(labelPassword,0,2);

		PasswordField txtPass =  new PasswordField();
		txtPass.setPromptText("password");
		grid.add(txtPass,1,2);

		Button loginBtn  =  new Button();
		loginBtn.setText("Sign In");
		grid.add(loginBtn,1,3);

		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String result = controller.login(txtUser.getText(),txtPass.getText());
				System.out.println(result);
				if(!result.equalsIgnoreCase("INVALID USERNAME OR PASSWORD"))
				{
					usernamePatient = result;
					patientPage();
					isFirstCall = true;
				}
				txtUser.setText("");
				txtPass.setText("");
			}
		});
		loginScene = new Scene(grid,500,500);
		window.setScene(loginScene);
		window.show();
	}
}
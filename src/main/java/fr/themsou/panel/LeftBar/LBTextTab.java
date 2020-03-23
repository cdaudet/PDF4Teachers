package fr.themsou.panel.LeftBar;

import fr.themsou.document.editions.Edition;
import fr.themsou.document.editions.elements.Element;
import fr.themsou.document.editions.elements.NoDisplayTextElement;
import fr.themsou.document.editions.elements.TextElement;
import fr.themsou.document.render.PageRenderer;
import fr.themsou.main.Main;
import fr.themsou.utils.Builders;
import fr.themsou.utils.SortEvent;
import fr.themsou.utils.SortManager;
import fr.themsou.utils.Sorter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class LBTextTab extends Tab {

	public Pane pane = new Pane();

	private ComboBox<String> fontCombo; String[] fontNames;
	private ComboBox<Integer> sizeCombo = new ComboBox<>(FXCollections.observableArrayList(6, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 22, 24, 26, 28, 30, 34, 38, 42, 46, 50));
	private ColorPicker colorPicker = new ColorPicker();
	private ToggleButton boldBtn = new ToggleButton("");
	private ToggleButton itBtn = new ToggleButton("");
	private TextArea txtArea = new TextArea();
	private Button deleteBtn = new Button("Supprimer");
	private Button newBtn = new Button("Nouveau");



	public TreeView treeView = new TreeView<>();
	public TreeItem<String> treeViewRoot = new TreeItem<>();

	public TreeItem<String> favoritesText = new TreeItem<>("favoritesText");
	public TreeItem<String> favoritesTextOptionsItem = new TreeItem("favoritesOptions");
	public ToggleButton favoritesTextToggleOption = new ToggleButton("");
	public GridPane favoritesTextOptions = new GridPane();
	public SortManager favoritesTextSortManager;

	public TreeItem<String> lastsText = new TreeItem<>("lastsText");
	public TreeItem<String> lastsTextOptionsItem = new TreeItem("lastsOptions");
	public ToggleButton lastsTextToggleOption = new ToggleButton("");
	public GridPane lastsTextOptions = new GridPane();
	public SortManager lastsTextSortManager;

	public TreeItem<String> onFileText = new TreeItem<>("onFileText");
	public TreeItem<String> onFileTextOptionsItem = new TreeItem("onFileOptions");
	public ToggleButton onFileTextToggleOption = new ToggleButton("");
	public GridPane onFileTextOptions = new GridPane();
	public SortManager onFileTextSortManager;

	private boolean txtAreaScrollBarListenerIsSetup = false;

	public LBTextTab(){

		setClosable(false);
		setContent(pane);

		setGraphic(Builders.buildImage(getClass().getResource("/img/Text.png")+"", 0, 25));
		Main.leftBar.getTabs().add(1, this);

		setup();
	}

	public void setup(){


		/*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fontNames = ge.getAvailableFontFamilyNames();
		fontCombo = new ComboBox<>(FXCollections.observableArrayList(fontNames));*/
		fontCombo = new ComboBox<>(FXCollections.observableArrayList("Arial", "Lato", "Lato Light", "Times New Roman"));

		fontCombo.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> stringListView) {

				return new ShapeCell();
			}
		});
		//fontCombo.setEditable(true);

		Builders.setPosition(fontCombo, 5, 5, 160, 30, true);
		fontCombo.setStyle("-fx-font-size: 13");
		fontCombo.setCursor(Cursor.HAND);
		fontCombo.getSelectionModel().select("Arial");
		fontCombo.setMaxHeight(25);
		pane.getChildren().add(fontCombo);
		fontCombo.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.selectedProperty().get() == null;}, Main.mainScreen.selectedProperty()));

		Builders.setPosition(sizeCombo, 170, 5, 95, 30, true);
		sizeCombo.setStyle("-fx-font-size: 13");
		sizeCombo.setCursor(Cursor.HAND);
		sizeCombo.getSelectionModel().select(7);
		pane.getChildren().add(sizeCombo);
		sizeCombo.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.selectedProperty().get() == null;}, Main.mainScreen.selectedProperty()));

		Builders.setPosition(colorPicker, 5, 40, 160, 30, false);
		colorPicker.setStyle("-fx-font-size: 13");
		colorPicker.setCursor(Cursor.HAND);
		colorPicker.setValue(Color.BLACK);
		pane.getChildren().add(colorPicker);
		colorPicker.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.selectedProperty().get() == null;}, Main.mainScreen.selectedProperty()));

		Builders.setPosition(boldBtn, 170, 40, 45, 29, true);
		boldBtn.setCursor(Cursor.HAND);
		boldBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		boldBtn.setGraphic(Builders.buildImage(getClass().getResource("/img/TextTab/Bold.png")+"", 0, 0));
		pane.getChildren().add(boldBtn);
		boldBtn.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.selectedProperty().get() == null;}, Main.mainScreen.selectedProperty()));

		Builders.setPosition(itBtn, 220, 40, 45, 29, true);
		itBtn.setFont(Font.font("Arial", FontPosture.ITALIC, 20));
		itBtn.setCursor(Cursor.HAND);
		itBtn.setGraphic(Builders.buildImage(getClass().getResource("/img/TextTab/Italic.png")+"", 0, 0));
		pane.getChildren().add(itBtn);
		itBtn.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.selectedProperty().get() == null;}, Main.mainScreen.selectedProperty()));

		Builders.setPosition(txtArea, 5, 75, 260, 30, true);
		txtArea.setStyle("-fx-font-size: 13");
		pane.getChildren().add(txtArea);
		txtArea.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.getSelected() == null || !(Main.mainScreen.getSelected() instanceof TextElement);}, Main.mainScreen.selectedProperty()));

		txtArea.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				updateHeightAndYLocations(getHorizontalSB(txtArea).isVisible());
				if(!txtAreaScrollBarListenerIsSetup){
					getHorizontalSB(txtArea).visibleProperty().addListener(new ChangeListener<Boolean>() {
						@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
							updateHeightAndYLocations(newValue);
						}
					});
					txtAreaScrollBarListenerIsSetup = true;
				}
			}
		});

		Builders.setPosition(deleteBtn, 5, 110, 127.5, 30, false);
		deleteBtn.setCursor(Cursor.HAND);
		pane.getChildren().add(deleteBtn);
		deleteBtn.disableProperty().bind(Bindings.createBooleanBinding(() -> {return Main.mainScreen.selectedProperty().get() == null;}, Main.mainScreen.selectedProperty()));

		Builders.setPosition(newBtn, 137.5, 110, 127.5, 30, false);
		newBtn.setCursor(Cursor.HAND);
		pane.getChildren().add(newBtn);
		newBtn.disableProperty().bind(Main.mainScreen.statusProperty().isNotEqualTo(-1));


		Main.mainScreen.selectedProperty().addListener(new ChangeListener<Element>() {
			@Override public void changed(ObservableValue<? extends Element> observable, Element oldElement, Element newElement) {



				if(oldElement != null){
					if(oldElement instanceof TextElement){
						TextElement current = (TextElement) oldElement;
						current.textProperty().unbind();
						current.realFontProperty().unbind();

						if(((TextElement) oldElement).getText().isBlank()){
							oldElement.delete();
						}

						lastsTextSortManager.simulateCall();
						onFileTextSortManager.simulateCall();
					}
				}

				if(newElement != null){
					if(newElement instanceof TextElement){
						TextElement current = (TextElement) newElement;

						txtArea.setText(current.getText());
						boldBtn.setSelected(TextElement.getFontWeight(current.getRealFont()) == FontWeight.BOLD);
						itBtn.setSelected(TextElement.getFontPosture(current.getRealFont()) == FontPosture.ITALIC);
						colorPicker.setValue((Color) current.getFill());
						fontCombo.getSelectionModel().select(current.getRealFont().getFamily());
						sizeCombo.getSelectionModel().select((Integer) ((int) current.getRealFont().getSize()));

						current.textProperty().bind(txtArea.textProperty());
						current.realFontProperty().bind(Bindings.createObjectBinding(() -> { Edition.setUnsave(); return getFont(); }, fontCombo.getSelectionModel().selectedItemProperty(), sizeCombo.getSelectionModel().selectedItemProperty(), itBtn.selectedProperty(), boldBtn.selectedProperty()));

					}
				}
			}
		});

		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(Main.mainScreen.getSelected() != null){
					if(Main.mainScreen.getSelected() instanceof TextElement){
						((TextElement) Main.mainScreen.getSelected()).setFill(colorPicker.getValue());
						Edition.setUnsave();
					}

				}
			}
		});

		newBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent mouseEvent) {

				PageRenderer page = Main.mainScreen.document.pages.get(0);
				if(Main.mainScreen.document.getCurrentPage() != -1)
					page = Main.mainScreen.document.pages.get(Main.mainScreen.document.getCurrentPage());


				TextElement current = new TextElement(30, (int) (page.mouseY * 800 / page.getHeight()), getFont(),
						txtArea.getText(), colorPicker.getValue(), page.getPage(), page);


				page.addElement(current, true);
				Main.mainScreen.selectedProperty().setValue(current);

				txtArea.setText("");
				addSavedElement(current.toNoDisplayTextElement(NoDisplayTextElement.LAST_TYPE, true));
				txtArea.requestFocus();
			}
		});

		deleteBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {

				Main.mainScreen.getSelected().delete();
				Main.mainScreen.setSelected(null);
			}
		});

		// TREE VIEW


		Builders.setPosition(favoritesTextToggleOption, 0, 0, 30, 30, true);
		favoritesTextToggleOption.setGraphic(Builders.buildImage(getClass().getResource("/img/Sort/sort.png") +"", 0, 0));
		favoritesTextToggleOption.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
				if(newValue){
					favoritesText.getChildren().add(0, favoritesTextOptionsItem);
					favoritesText.setExpanded(true);
				}else{
					favoritesText.getChildren().remove(0);
				}
			}
		});

		Builders.setPosition(lastsTextToggleOption, 0, 0, 30, 30, true);
		lastsTextToggleOption.setGraphic(Builders.buildImage(getClass().getResource("/img/Sort/sort.png") +"", 0, 0));
		lastsTextToggleOption.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue){
					lastsText.getChildren().add(0, lastsTextOptionsItem);
					lastsText.setExpanded(true);
				}else{
					lastsText.getChildren().remove(0);
				}
			}
		});

		Builders.setPosition(onFileTextToggleOption, 0, 0, 30, 30, true);
		onFileTextToggleOption.setGraphic(Builders.buildImage(getClass().getResource("/img/Sort/sort.png") +"", 0, 0));
		onFileTextToggleOption.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue){
					onFileText.getChildren().add(0, onFileTextOptionsItem);
					onFileText.setExpanded(true);
				}else{
					onFileText.getChildren().remove(0);
				}
			}
		});

		treeView.disableProperty().bind(Main.mainScreen.statusProperty().isNotEqualTo(-1));
		treeView.setEditable(true);
		treeView.setBackground(new Background(new BackgroundFill(Color.rgb(244, 244, 244), CornerRadii.EMPTY, Insets.EMPTY)));
		treeView.setLayoutY(150);
		treeView.setPrefWidth(270);
		treeView.prefHeightProperty().bind(pane.heightProperty().subtract(treeView.layoutYProperty()));
		treeView.setShowRoot(false);
		treeView.setRoot(treeViewRoot);

		new LBTextTreeView(treeView);

		favoritesTextSortManager = new SortManager(new SortEvent(){
			@Override public void call(String sortType, boolean order){

				List<NoDisplayTextElement> toSort = new ArrayList<>();
				for(int i = 0; i < favoritesText.getChildren().size(); i++){
					if(favoritesText.getChildren().get(i) instanceof NoDisplayTextElement){
						toSort.add((NoDisplayTextElement) favoritesText.getChildren().get(i));
					}
				}
				clearSavedFavoritesElements();
				for(NoDisplayTextElement item : autoSortList(toSort, sortType, order)) favoritesText.getChildren().add(item);
			}
		}, null, null);
		favoritesTextSortManager.setup(favoritesTextOptions, "Ajout", "Ajout", "Nom", "Utilisation", "\n", "Police", "Taille", "Couleur");

		lastsTextSortManager = new SortManager(new SortEvent(){
			@Override public void call(String sortType, boolean order){

				List<NoDisplayTextElement> toSort = new ArrayList<>();
				for(int i = 0; i < lastsText.getChildren().size(); i++){
					if(lastsText.getChildren().get(i) instanceof NoDisplayTextElement){
						toSort.add((NoDisplayTextElement) lastsText.getChildren().get(i));
					}
				}
				clearSavedLastsElements();
				for(NoDisplayTextElement item : autoSortList(toSort, sortType, order)) lastsText.getChildren().add(item);

			}
		}, null, null);
		lastsTextSortManager.setup(lastsTextOptions, "Ajout", "Ajout", "Nom", "Utilisation", "\n", "Police", "Taille", "Couleur");

		onFileTextSortManager = new SortManager(new SortEvent(){
			@Override public void call(String sortType, boolean order){

				List<NoDisplayTextElement> toSort = new ArrayList<>();
				for(int i = 0; i < onFileText.getChildren().size(); i++){
					if(onFileText.getChildren().get(i) instanceof NoDisplayTextElement){
						toSort.add((NoDisplayTextElement) onFileText.getChildren().get(i));
					}
				}
				clearSavedOnFileElements();
				for(NoDisplayTextElement item : autoSortList(toSort, sortType, order)) onFileText.getChildren().add(item);

			}
		}, null, null);
		onFileTextSortManager.setup(onFileTextOptions, "Position", "Position", "Nom", "\n", "Police", "Taille", "Couleur");

		treeViewRoot.getChildren().addAll(favoritesText, lastsText, onFileText);

		pane.getChildren().add(treeView);

	}

	public void updateHeightAndYLocations(boolean sbIsVisible){

		int lineNumber = txtArea.getParagraphs().size();
		int height = lineNumber >= 3 ? 70 : lineNumber*20+10;

		if(sbIsVisible) height += 16;

		if(txtArea.getHeight() != height){
			txtArea.minHeightProperty().bind(new SimpleDoubleProperty(height));
			deleteBtn.setLayoutY(80 + height);
			newBtn.setLayoutY(80 + height);
			treeView.setLayoutY(120 + height);
		}

	}

	public void selectItem(){
		new Thread(new Runnable() {
			@Override public void run() {
				try{
					Thread.sleep(50);
					Platform.runLater(new Runnable() {
						@Override public void run() {
							String text = txtArea.getText();
							txtArea.setText(text);
							txtArea.positionCaret(txtArea.getText().length());
						}
					});
				}catch(InterruptedException e){ e.printStackTrace();}
			}
		}, "selector").start();

	}

	private Font getFont(){

		return TextElement.getFont(fontCombo.getSelectionModel().getSelectedItem(), itBtn.isSelected(), boldBtn.isSelected(), sizeCombo.getSelectionModel().getSelectedItem());
	}

	public class ShapeCell extends ListCell<String>{
		@Override
		public void updateItem(String item, boolean empty){
			super.updateItem(item, empty);

			if(empty){
				setText(null);
				setGraphic(null);
			}else{
				setText(item);
				setStyle("-fx-font: 14 \"" + item + "\"");
			}
		}
	}
	public void addSavedElement(NoDisplayTextElement element){
		if(element.getType() == NoDisplayTextElement.FAVORITE_TYPE){
			if(!favoritesText.getChildren().contains(element)){
				favoritesText.getChildren().add(element);
				favoritesTextSortManager.simulateCall();
			}
		}else if(element.getType() == NoDisplayTextElement.LAST_TYPE){
			if(!lastsText.getChildren().contains(element)){

				if(lastsText.getChildren().size() > 49){
					List<NoDisplayTextElement> toSort = new ArrayList<>();
					for(int i = 0; i < lastsText.getChildren().size(); i++){
						if(lastsText.getChildren().get(i) instanceof NoDisplayTextElement){
							toSort.add((NoDisplayTextElement) lastsText.getChildren().get(i));
						}
					}
					List<NoDisplayTextElement> sorted = Sorter.sortElementsByUtils(toSort, true);
					removeSavedElement(sorted.get(sorted.size()-1));
				}
				lastsText.getChildren().add(element);
				lastsTextSortManager.simulateCall();
			}
		}
	}
	public void removeSavedElement(NoDisplayTextElement element){
		if(element.getType() == NoDisplayTextElement.FAVORITE_TYPE){
			favoritesText.getChildren().remove(element);
		}else{
			lastsText.getChildren().remove(element);
		}
	}
	public void clearSavedFavoritesElements(){
		List<TreeItem<String>> items = favoritesText.getChildren();
		for(int i = items.size()-1; i >= 0; i--){
			if(items.get(i) instanceof NoDisplayTextElement){
				items.remove(i);
			}
		}
	}
	public void clearSavedLastsElements(){
		List<TreeItem<String>> items = lastsText.getChildren();
		for(int i = items.size()-1; i >= 0; i--){
			if(items.get(i) instanceof NoDisplayTextElement){
				items.remove(i);
			}
		}
	}
	public void clearSavedOnFileElements(){
		List<TreeItem<String>> items = onFileText.getChildren();
		for(int i = items.size()-1; i >= 0; i--){
			if(items.get(i) instanceof NoDisplayTextElement){
				items.remove(i);
			}
		}
	}

	public void updateOnFileElementsList(){
		clearSavedOnFileElements();

		if(Main.mainScreen.getStatus() == -1){
			for(PageRenderer page : Main.mainScreen.document.pages){
				for(int i = 0; i < page.getElements().size(); i++){
					if(page.getElements().get(i) instanceof TextElement){
						TextElement element = (TextElement) page.getElements().get(i);
						onFileText.getChildren().add(element.toNoDisplayTextElement(NoDisplayTextElement.ONFILE_TYPE, true));
					}
				}
			}
		}
		onFileTextSortManager.simulateCall();
	}
	public void addOnFileElement(TextElement element){

		onFileText.getChildren().add(element.toNoDisplayTextElement(NoDisplayTextElement.ONFILE_TYPE, true));
		onFileTextSortManager.simulateCall();

	}
	public void removeOnFileElement(TextElement element){

		List<TreeItem<String>> items = onFileText.getChildren();
		for(TreeItem<String> item : items){
			if(item instanceof NoDisplayTextElement){
				if(((NoDisplayTextElement) item).getCore().equals(element)){
					items.remove(item);
					break;
				}
			}
		}
		items = lastsText.getChildren();
		for(TreeItem<String> item : items){
			if(item instanceof NoDisplayTextElement){
				if(((NoDisplayTextElement) item).getCore() != null){
					if(((NoDisplayTextElement) item).getCore().equals(element)){
						items.remove(item);
						break;
					}
				}
			}
		}
	}

	private ScrollBar getHorizontalSB(final TextArea scrollPane) {
		Set<Node> nodes = scrollPane.lookupAll(".scroll-bar");
		for (final Node node : nodes) {
			if (node instanceof ScrollBar) {
				ScrollBar sb = (ScrollBar) node;
				if(sb.getOrientation() == Orientation.HORIZONTAL){
					return sb;
				}
			}
		}
		return null;
	}
	private List<NoDisplayTextElement> autoSortList(List<NoDisplayTextElement> toSort, String sortType, boolean order){

		if(sortType.equals("Ajout")){
			return Sorter.sortElementsByDate(toSort, order);
		}else if(sortType.equals("Nom")){
			return Sorter.sortElementsByName(toSort, order);
		}else if(sortType.equals("Utilisation")){
			return Sorter.sortElementsByUtils(toSort, order);
		}else if(sortType.equals("Police")){
			return Sorter.sortElementsByPolice(toSort, order);
		}else if(sortType.equals("Taille")){
			return Sorter.sortElementsBySize(toSort, order);
		}else if(sortType.equals("Couleur")){
			return Sorter.sortElementsByColor(toSort, order);
		}else if(sortType.equals("Position")){
			return Sorter.sortElementsByCorePosition(toSort, order);
		}
		return toSort;
	}
}

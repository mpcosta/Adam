package adam.view;

import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class WorldMap extends StackPane {
	
	private final String URL_SEPARATOR = "&";
	private final String COUNTRY_SEPARATOR = "|";
	private final String PERCENTAGE_SEPARATOR = ",";
	
	// cht=MAP_ORIENTATION&MAP_SIZE&MAP_COUNTRIES&MAP_COLOR_VALUES_PERCENTAGE&MAP_COLORS_RANGE
	private final String urlSource = "https://chart.googleapis.com/chart?cht=";
	
	// Map orientation within the LAT and LONG
	private final String mapOrientation = "map:fixed=-55,-180,84,-180";
	
	// Maximum size of the map
	private final String mapSize = "chs=800x375";
	
	// Countries separated by |
	private String mapCountries = "chld=";
	
	// Percentage separated by ,
	private String mapColorValuesPercentage = "chd=t:";
	
	// Map color ranges (first one is map color, and then 0 -> 100 range)
	private String mapColorRanges = "chco=CCCCCC,38B0DE,000000";
	
	private String currentMapSource;
	
	private ImageView mapView;

	public WorldMap() {
		
		currentMapSource = "";
		mapView = new ImageView();
		ObservableList<String> cTest = FXCollections.observableArrayList();
		cTest.addAll("RO", "US");
		ObservableList<Double> vTest = FXCollections.observableArrayList(); 
		vTest.add(25445.0);
		vTest.add(34382374.0);
		getChildren().add(changeMap(cTest, vTest));
	}
	
	public ImageView changeMap(ObservableList<String> countriesList, ObservableList<Double> values) {
		currentMapSource = urlSource + mapOrientation + URL_SEPARATOR + mapSize + URL_SEPARATOR + mapCountries;
		
		for (String country : countriesList) {
			currentMapSource += (country.toUpperCase() + COUNTRY_SEPARATOR);
		}
		
		currentMapSource = currentMapSource.substring(0, currentMapSource.length() - 1);
		
		currentMapSource += URL_SEPARATOR + mapColorValuesPercentage;	
		Double maxValue = Collections.max(values);
		for (Double value : values) { 
			currentMapSource += ((int)((value * 100) / maxValue) + PERCENTAGE_SEPARATOR);
		}
		
		currentMapSource = currentMapSource.substring(0, currentMapSource.length() - 1);
		
		currentMapSource += (URL_SEPARATOR + mapColorRanges);
		
		mapView.setImage(new Image(currentMapSource));
		
		System.out.println(currentMapSource);
		
		mapView.setPreserveRatio(true);
		mapView.setFitWidth(800);
		
		return mapView;
	}
}

package adam.view;

import java.util.Collections;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class WorldMap extends StackPane {
	/**
	 * Variables for the url, country, and percentage separator
	 */
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
	
	// Countries separated by | for legend
	private String mapCountriesForLegend = "chdl=";
	
	// Percentage separated by ,
	private String mapColorValuesPercentage = "chd=t:";
	
	// Map color ranges (first one is map color, and then 0 -> 100 range)
	private String mapColorRanges = "chco=CCCCCC,38B0DE,000000";
	
	private String currentMapSource;
	
	private ImageView mapView;
/**
	 * A constructor that creates the world map view and adds it to the pane 
	 */
	public WorldMap() {
		
		currentMapSource = "";
		mapView = new ImageView();
		mapView.setPreserveRatio(true);
		
		getChildren().add(mapView);
		
		mapView.fitWidthProperty().bind(MainView.primaryStage.widthProperty());
//		mapView.fitHeightProperty().bind(MainView.primaryStage.widthProperty());
	}
	/**
	 * A method to change the map depending on the values of the country known from the url separator and url source 
	 * @param countriesList
	 * @param values
	 * @return the map view after the changes 
	 */
	public ImageView changeMap(ObservableList<String> countriesCodes, ObservableList<String> countriesNames, ObservableList<Double> values) {
		currentMapSource = urlSource + mapOrientation + URL_SEPARATOR + mapSize + URL_SEPARATOR + mapCountries;
		
		for (String country : countriesCodes) {
			currentMapSource += (country.toUpperCase() + COUNTRY_SEPARATOR);
		}
		
		currentMapSource = currentMapSource.substring(0, currentMapSource.length() - 1) + URL_SEPARATOR;
		
		currentMapSource += mapCountriesForLegend;
		for (String country : countriesNames) {
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
		
		currentMapSource = currentMapSource.replaceAll("\\s", "%20");
		
		mapView.setImage(new Image(currentMapSource));		
		
		return mapView;
	}
}

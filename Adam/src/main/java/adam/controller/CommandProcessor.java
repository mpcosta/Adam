package adam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adam.model.Area;
import adam.model.Region;
import adam.model.RequestException;
import adam.view.ChartPane;
import adam.view.MainView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class CommandProcessor
{
	//The following are keywords that the user can enter into the search bar:
	private static final String[] DATA = new String[]
	{
		"GDP", "CPI", "BOP", "unemployment", "inflation", "government spending", "government consumption" 
	},
	CHARTS = new String[]
	{
		"line", "bar", "map"
	},
	TIME = new String[]
	{
		"from", "to"
	},
	AREAS = new String[]
	{
		"all areas"
	},
	COMMANDS = new String[]
	{
		"explain"
	};
	//The following constants correlate to indexes in the above arrays of valid keywords:
	private static final int LINE = 0, BAR = 1, MAP = 2,
			FROM = 0, TO = 1,
			ALL_AREAS = 0,
			EXPLAIN = 0;
	private static final String REQUESTEX_DEFAULT = "An error occured when attempting to retrieve results.",
			REQUESTEX_NO_CONNECTION = "Cannot connect to server and no offline data exists for the specified request.",
			REQUESTEX_NO_CONNECTION_RANGE = REQUESTEX_NO_CONNECTION + " Check your internet connection, or try a smaller year range.",
			REQUESTEX_INVALID_RANGE = "Invalid range specified. Start year must occur before end year.",
			REQUESTEX_MAP_INVALID_RANGE = "Map can only display data for one year. Please specify one year, rather than a range.",
			REQUESTEX_MAP_NO_DATA = "No data to display.",
			
			DEFAULT_HELP = "Type commands into the box above. Commands can either describe a graph or a map to display, or can be used to explain certain concepts.\n\nExamples:\nGDP for the European Union vs the Russian Federation from 2000 to 2015 on a line graph.\nexplain CPI.";
	private static final Pattern PATTERN_RANGE = Pattern.compile(".*(\\d\\d\\d\\d) to (\\d\\d\\d\\d).*"),
			PATTERN_SINGLE = Pattern.compile(".*(\\d\\d\\d\\d).*");
	
	private MainView mainView;
	
	private String command;
	
	public CommandProcessor(MainView m)
	{
		mainView = m;
		command = null;
	}
	/**
	 * A static get method for the Array list to get all the indicators 
	 * @return indicators
	 */
	public static ArrayList<String> getAllIndicators()
	{
		ArrayList<String> indicators = new ArrayList<String>();
		for (String data : DATA)
		{
			indicators.add(data);
		}
		return indicators;
	}
	/**
	 * A static get method to retrieve all the graphs
	 * @return graphs
	 */
	public static ArrayList<String> getAllGraphs()
	{
		ArrayList<String> graphs = new ArrayList<String>();
		for (String graph : CHARTS)
		{
			graphs.add(graph);
		}
		return graphs;
	}
	/**
	 * A get method to show the possible the suggestions the User can search 
	 * @param existing
	 * @param segment
	 * @param full
	 * @param display
	 * @param override
	 */
	public void getSuggestions(String existing, String segment, String full, LinkedList<String> display, LinkedList<String> override)
	{
		ArrayList<String> suggestions = Area.estimateNamesFromFragment(segment);
		for (String s : DATA)
		{
			if (s.contains(segment))
				suggestions.add(s);
		}
		for (String s : CHARTS)
		{
			String suggestion = "on a " + s;
			if (s.equals(CHARTS[LINE]))
				suggestion += " graph";
			else if (s.equals(CHARTS[BAR]))
				suggestion += " chart";
			if (suggestion.contains(segment))
				suggestions.add(suggestion);
		}
		for (String s : TIME)
		{
			if (s.contains(segment))
			{
				if (segment.equals(TIME[FROM]))
					suggestions.add(TIME[FROM]);
				else if (existing.contains(TIME[FROM]))
					suggestions.add(TIME[TO]);
			}
		}
		for (String s : AREAS)
		{
			if (s.contains(segment))
				suggestions.add(s);
		}
		for (String s : COMMANDS)
		{
			if (s.contains(segment))
				suggestions.add(s);
		}
		
		for (String suggestion : suggestions)
		{
			if (full.contains(suggestion) && !full.endsWith(suggestion))
				return;
		}
		
		for (String item : suggestions)
		{
			if (display.contains(item))
				continue;
			display.add(item);
			override.add(existing + item + " ");
		}
	}
	/**
	 * A method to process what the user inputs and prints the correct graph according to the input 
	 * A boolean to match whether the key word is matched to the data input 
	 * A matcher for the year range inputed by the user to be implemented in the graph 
	 * A boolean to check if invalid data is typed in the search bar for an error message to appear 
	 * @param c the code for the country 
	 * @return the graph/chart for the user 
	 */
	public Pane process(String c)
	{
		command = c;
		
		if (command.equals(""))
			return constructMessagePane(DEFAULT_HELP);
		
		if (command.contains(COMMANDS[EXPLAIN]))
			return explainIndicatorData();
		
		String title = "";
		ArrayList<String> names = Area.getAllNames(),
				foundNames = findAllThatMatch(names.toArray(new String[0]));
		ArrayList<Area> areas = new ArrayList<Area>();
		
		if (command.contains(AREAS[ALL_AREAS]))
			foundNames.addAll(Area.getAllNames());
		
		final String AREA_SEPARATOR = " vs ";
		for (String name : foundNames)
		{
			String code = Area.getAreaCodeFromName(name);
			try
			{
				Area area = Area.getAreaFromCode(code);
				title += area.getName() + AREA_SEPARATOR;
				areas.add(area);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (areas.size() > 0)
			title = title.substring(0, title.length() - AREA_SEPARATOR.length());
			
		
		boolean keyWordIdentitified = false;
		int dataType = -1;
		for (String keyWord : findAllThatMatch(DATA))
		{
			for (int i = 0; i < DATA.length; i++)
			{
				if (keyWord.equals(DATA[i]))
				{
					title = keyWord + " for " + title;
					dataType = i;
					keyWordIdentitified = true;
					break;
				}
			}
			if (keyWordIdentitified)
				break;
		}
		
		keyWordIdentitified = false;
		int chartType = -1;
		for (String keyWord : findAllThatMatch(CHARTS))
		{
			for (int i = 0; i < CHARTS.length; i++)
			{
				if (keyWord.equals(CHARTS[i]))
				{
					chartType = i;
					keyWordIdentitified = true;
					break;
				}
			}
			if (keyWordIdentitified)
				break;
		}
		
		Matcher rangeMatcher = PATTERN_RANGE.matcher(command);
		int startingYear = -1, endingYear = -1;
		if (rangeMatcher.matches())
		{
			startingYear = Integer.parseInt(rangeMatcher.group(1));
			endingYear = Integer.parseInt(rangeMatcher.group(2));
		}
		if (startingYear == -1 || endingYear == -1)
		{
			Matcher singleMatcher = PATTERN_SINGLE.matcher(command);
			if (singleMatcher.matches())
			{
				startingYear = Integer.parseInt(singleMatcher.group(1));
				endingYear = startingYear;
			}
		}
		
		boolean noAreas = areas.size() == 0,
				invalidDataType = dataType == -1,
				invalidChartType = chartType == -1,
				invalidRange = startingYear == -1 || endingYear == -1;
		if (noAreas || invalidDataType || invalidChartType || invalidRange)
		{
			String errorMessage = "Information required:\n";
			if (noAreas)
				errorMessage += "- Country/Region\n";
			if (invalidDataType)
				errorMessage += "- Data type (e.g. GDP).\n";
			if (invalidChartType)
				errorMessage += "- Chart type (e.g. line).\n";
			if (invalidRange)
				errorMessage += "- Year range (e.g. 2010 to 2012).\n";
			return constructMessagePane(errorMessage);
		}
		
		try
		{
			return constructChart(title, areas, chartType, dataType, startingYear, endingYear);
		}
		catch (RequestException e)
		{
			switch (e.getType())
			{
				case RequestException.NO_CONNECTION:
					return constructMessagePane(REQUESTEX_NO_CONNECTION_RANGE + " " + e.getInfo());
				case RequestException.INVALID_RANGE:
					return constructMessagePane(REQUESTEX_INVALID_RANGE + " " + e.getInfo());
				case RequestException.MAP_INVALID_RANGE:
					return constructMessagePane(REQUESTEX_MAP_INVALID_RANGE + " " + e.getInfo());
				case RequestException.MAP_NO_COUNTRY_DATA_FOUND:
					return constructMessagePane(REQUESTEX_MAP_NO_DATA + " " + e.getInfo());
			}
			return constructMessagePane(REQUESTEX_DEFAULT + " " + e.getInfo());
		}
	}
	/**
	 * An update method that shows a loading label to show the user the graph is formalising and will be appearing soon 
	 * @param status
	 */
	private void updateProgress(String status)
	{
		final String newValue = status;
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				mainView.setLoadingLabel(newValue);
			}
		});
	}
	/**
	 * A method to formalise the chart including the type of chart and the range of years 
	 * Calling the observable list to call the data from the indicator 
	 * @param title
	 * @param areas
	 * @param chartType
	 * @param dataType
	 * @param startingYear
	 * @param endingYear
	 * @return the chart 
	 */
	private ChartPane constructChart(String title, ArrayList<Area> areas, int chartType, int dataType, int startingYear, int endingYear) throws RequestException
	{
		ChartPane chartPane = new ChartPane(chartType, title);
		if (chartType == MAP)
		{
			if (startingYear != endingYear)
				throw new RequestException(RequestException.MAP_INVALID_RANGE);
			ObservableList<String> areaCodes = FXCollections.observableArrayList();
			ObservableList<String> areaNames = FXCollections.observableArrayList();
			ObservableList<Double> areaValues = FXCollections.observableArrayList();
			for (Area area : areas)
			{
				if (area instanceof Region)
					continue;
				updateProgress("Collecting information for " + area.getName() + "...");
				HashMap<Integer, Double> indicatorData = area.getIndicatorData(dataType, startingYear, endingYear);
				Double d = indicatorData.get(startingYear);
				if (d == null)
					continue;
				areaCodes.add(area.getCode());
				areaNames.add(area.getName());
				areaValues.add(d);
			}
			if (areaValues.size() == 0)
				throw new RequestException(RequestException.MAP_NO_COUNTRY_DATA_FOUND);
			chartPane.setMapData(areaCodes, areaNames, areaValues);
		}
		else
		{
			ObservableList<Series<String, Double>> chartData = FXCollections.observableArrayList();
			for (Area area : areas)
			{
				updateProgress("Collecting information for " + area.getName() + "...");
				Series<String, Double> series = new Series<String, Double>();
				series.setName(area.getName());
				
				ObservableList<Data<String, Double>> data = series.getData();
				HashMap<Integer, Double> indicatorData = area.getIndicatorData(dataType, startingYear, endingYear);
				for (int year : indicatorData.keySet())
				{
					data.add(new Data<String, Double>(String.valueOf(year), indicatorData.get(year)));
				}
				if (data.size() > 0)
					chartData.add(series);
			}
			chartData.sort((s1, s2) -> s1.getData().get(0).getXValue().compareTo(s2.getData().get(0).getXValue()));
			
			chartPane.setChartData(chartData);
		}
		return chartPane;
	}
	
	/**
	 * Private helper method for constructing message panes from a string.
	 * @param message	The message to display.
	 * @return	A Pane containing the message.
	 */
	private Pane constructMessagePane(String message)
	{
		StackPane pane = new StackPane();
		Label label = new Label(message);
		label.setWrapText(true);
		label.setPadding(new Insets(5));
		pane.getChildren().add(label);
		return pane;
	}
	
	/**
	 * Returns a pane with information about the indicator data specified in the current command.
	 * @return	A Pane displaying the information.
	 */
	private Pane explainIndicatorData()
	{
		String explanation = null;
		for (int i = 0; i < DATA.length; i++)
		{
			if (command.contains(DATA[i]))
			{
				explanation = "Here is some information about " + DATA[i] + ":\n";
				try
				{
					explanation += Area.getIndicatorInfo(i, Area.INDICATOR_NAME) + "\n\n";
					explanation += "Source Note:\n" + Area.getIndicatorInfo(i, Area.INDICATOR_SOURCE_NOTE) + "\n\n";
					explanation += "Source Organisation:\n" + Area.getIndicatorInfo(i, Area.INDICATOR_SOURCE_ORGANISATION) + "\n\n";
					explanation += "Topics:\n" + Area.getIndicatorInfo(i, Area.INDICATOR_TOPICS) + "\n\n";
				}
				catch (RequestException e)
				{
					explanation = REQUESTEX_NO_CONNECTION;
				}
				break;
			}
		}
		if (explanation == null)
		{
			explanation = "Cannot find information for specified item. Valid items are:\n";
			for (String data : DATA)
			{
				explanation += data + "\n";
			}
		}
		return constructMessagePane(explanation);
	}
	
	/**
	 * A method for the User to see possible options related to what they are typing in the search bar 
	 * @param possibilities
	 * @return possible options for the User 
	 */
	private ArrayList<String> findAllThatMatch(String[] possibilities)
	{
		ArrayList<String> found = new ArrayList<String>();
		for (String possibility : possibilities)
		{
			if (command.contains(possibility))
			{
				found.add(possibility);
				command = command.replace(possibility, "");
			}
		}
		return found;
	}
}

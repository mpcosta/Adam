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
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class CommandProcessor
{
	private static final String[] DATA = new String[]
	{
		"GDP", "CPI", "BOP", "unemployment", "inflation", "government spending", "government consumption" 
	},
	CHARTS = new String[]
	{
		"line", "bar", "pie", "map"
	},
	TIME = new String[]
	{
		"from", "to"
	},
	AREAS = new String[]
	{
		"all areas"
	};
	private static final int LINE = 0, BAR = 1, PIE = 2, MAP = 3,
			FROM = 0, TO = 1,
			ALL_AREAS = 0;
	private static final String REQUESTEX_DEFAULT = "An error occured when attempting to retrieve results.",
			REQUESTEX_NO_CONNECTION = "Cannot connect to server and no offline data exists for the specified request. Check your internet connection, or try a smaller year range.",
			REQUESTEX_INVALID_RANGE = "Invalid range specified. Start year must occur before end year.";
	private static final Pattern PATTERN_RANGE = Pattern.compile(".*(\\d\\d\\d\\d) to (\\d\\d\\d\\d).*"),
			PATTERN_SINGLE = Pattern.compile(".*(\\d\\d\\d\\d).*");
	
	private MainView mainView;
	
	private String command;
	
	public CommandProcessor(MainView m)
	{
		mainView = m;
		command = null;
	}
	
	public static ArrayList<String> getAllIndicators()
	{
		ArrayList<String> indicators = new ArrayList<String>();
		for (String data : DATA)
		{
			indicators.add(data);
		}
		return indicators;
	}
	
	public static ArrayList<String> getAllGraphs()
	{
		ArrayList<String> graphs = new ArrayList<String>();
		for (String graph : CHARTS)
		{
			graphs.add(graph);
		}
		return graphs;
	}
	
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
			if (s.equals(CHARTS[LINE]) || s.equals(CHARTS[PIE]))
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
	
	public Pane process(String c)
	{
		command = c;
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
		//if (dataType == -1)
		//	return null;
		
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
		//if (chartType == -1)
		//	return null;
		
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
			
		//if (startingYear == -1 || endingYear == -1)
		//	return null;
		
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
			if (e.getType() == RequestException.NO_CONNECTION)
				return constructMessagePane(REQUESTEX_NO_CONNECTION + " " + e.getInfo());
			else if (e.getType() == RequestException.INVALID_RANGE)
				return constructMessagePane(REQUESTEX_INVALID_RANGE + " " + e.getInfo());
			else
				return constructMessagePane(REQUESTEX_DEFAULT + " " + e.getInfo());
		}
	}
	
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
	
	private ChartPane constructChart(String title, ArrayList<Area> areas, int chartType, int dataType, int startingYear, int endingYear) throws RequestException
	{
		ChartPane chartPane = new ChartPane(chartType, title);
		if (chartType == MAP)
		{
			if (startingYear != endingYear)
				return null;
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
				return null;
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
	
	private Pane constructMessagePane(String message)
	{
		StackPane pane = new StackPane();
		Label label = new Label(message);
		pane.getChildren().add(label);
		return pane;
	}
	
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

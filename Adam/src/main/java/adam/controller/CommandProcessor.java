package adam.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import adam.model.Area;
import adam.view.ChartPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class CommandProcessor
{
	private final String[] DATA = new String[]
	{
		"GDP", "CPI", "BOP"
	},
	CHARTS = new String[]
	{
		"line", "bar", "pie"
	},
	TIME = new String[]
	{
		"from", "to"
	};
	private final int GDP = 0, CPI = 1, BOP = 2,
			LINE = 0, BAR = 1, PIE = 2,
			FROM = 0, TO = 1;
	private final Pattern PATTERN_RANGE = Pattern.compile(".*(\\d\\d\\d\\d) to (\\d\\d\\d\\d).*");
	
	private String command;
	
	public CommandProcessor()
	{
		command = null;
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
			String suggestion = "on a " + s + " ";
			if (s.equals(CHARTS[LINE]) || s.equals(CHARTS[PIE]))
				suggestion += "graph";
			else if (s.equals(CHARTS[BAR]))
				suggestion += "chart";
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
			
		//if (startingYear == -1 || endingYear == -1)
		//	return null;
		
		boolean noAreas = areas.size() == 0,
				invalidDataType = dataType == -1,
				invalidChartType = chartType == -1,
				invalidRange = startingYear == -1 || endingYear == -1;
		if (noAreas || invalidDataType || invalidChartType || invalidRange)
		{
			String errorMessage = "Could not complete request: \n";
			if (noAreas)
				errorMessage += "Country/Region required.\n";
			if (invalidDataType)
				errorMessage += "Data type to display required (e.g. GDP).\n";
			if (invalidChartType)
				errorMessage += "Chart type to display data on required (e.g. line).\n";
			if (invalidRange)
				errorMessage += "Year range required (e.g. 2010 to 2012).\n";
			StackPane pane = new StackPane();
			Label label = new Label(errorMessage);
			pane.getChildren().add(label);
			return pane;
		}
		
		ObservableList<Series<String, Double>> chartData = FXCollections.observableArrayList();
		for (Area area : areas)
		{
			Series<String, Double> series = new Series<String, Double>();
			series.setName(area.getName());
			for (int year = startingYear; year <= endingYear; year++)
			{
				try
				{
					ObservableList<Data<String, Double>> data = series.getData();
					double dataPoint = 0;
					switch (dataType)
					{
						case GDP:
							dataPoint = area.getGDP(year);
							break;
						case CPI:
							dataPoint = area.getCPI(year);
							break;
						case BOP:
							dataPoint = area.getBOP(year);
							break;
					}
					data.add(new Data<String, Double>(Integer.toString(year), dataPoint));
				}
				catch (Exception e)
				{
					//Indicator data not available for specified year.
				}
			}
			chartData.add(series);
		}
		ChartPane chartPane = new ChartPane(chartType, title);
		chartPane.setChartData(chartData);
		
		return chartPane;
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

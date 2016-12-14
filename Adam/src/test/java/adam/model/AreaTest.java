package adam.model;

import static adam.model.Area.cacheAllNames;
import static adam.model.Area.codeIsValid;
import static adam.model.Area.estimateNamesFromFragment;
import static adam.model.Area.getAllNames;
import static adam.model.Area.getAreaFromCode;
import static adam.model.Area.getAreaCodeFromName;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Test;

import adam.model.Area.Code;
import adam.model.Country;
import adam.model.Region;

public class AreaTest {
	
	@Test
	public void testEstimateNamesFromFragment() {
		assertThat(estimateNamesFromFragment("U"), hasItem("United Kingdom"));
	}
	
	@Test
	public void testEstimateNamesFromFragmentCaching()
	{
		for (int i = 0; i < 2; i++)
		{
			assertThat(estimateNamesFromFragment("E"), hasItem("European Union"));
		}
	}
	
	@Test
	public void testGetAllNames() {
		assertThat(getAllNames().isEmpty(), is(false));
	}
	
	@Test
	public void testIsCodeValid() throws Exception {
		assertThat(codeIsValid(""), is(false));
		assertThat(codeIsValid("g"), is(false));
		assertThat(codeIsValid("gb"), is(true));
		assertThat(codeIsValid("gbb"), is(false));
		assertThat(codeIsValid(new Code("gb")), is(true));
	}
	
	@Test
	public void testAreaIsOfCorrectType() throws Exception
	{
		final Area area0 = getAreaFromCode("DE"), area1 = getAreaFromCode("EU");
		assertThat(area0 instanceof Country, is(true));
		assertThat(area1 instanceof Region, is(true));
	}
	
	@Test
	public void testGetAreaNameFromCode()
	{
		assertThat(getAreaCodeFromName("Belgium"), is("BE"));
		assertThat(getAreaCodeFromName("European Union"), is("EU"));
	}
	
	@Test
	public void testAreaIndicators() throws Exception
	{
		final Area area = getAreaFromCode("FR");
		final HashMap<Integer, Double> gdp = area.getIndicatorData(Area.GDP, 1999, 2000),
				inflation = area.getIndicatorData(Area.INFLATION, 1999, 2000);
		assertThat(gdp.get(1999) > gdp.get(2000), is(true));
		assertThat(inflation.get(1999) < inflation.get(2000), is(true));
	}
}

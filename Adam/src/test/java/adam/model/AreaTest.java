package adam.model;

import static adam.model.Area.cacheAllNames;
import static adam.model.Area.codeIsValid;
import static adam.model.Area.estimateNamesFromFragment;
import static adam.model.Area.getAllNames;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import adam.model.Area.Code;

public class AreaTest {
	
	@Test
	public void testEstimateNamesFromFragment() {
		assertThat(estimateNamesFromFragment("U"), hasItem("United Kingdom"));
	}
	
	@Test
	public void testGetAllNames() {
		assertThat(getAllNames().isEmpty(), is(false));
	}
	
	@Test
	public void testCacheAllNames() {
		cacheAllNames();
	}
	
	@Test
	public void testIsCodeValid() throws Exception {
		assertThat(codeIsValid(""), is(false));
		assertThat(codeIsValid("g"), is(false));
		assertThat(codeIsValid("gb"), is(true));
		assertThat(codeIsValid("gbb"), is(false));
		assertThat(codeIsValid(new Code("gb")), is(true));
	}
	
}

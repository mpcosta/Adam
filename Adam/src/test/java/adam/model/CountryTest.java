package adam.model;

import static adam.model.Country.getCountry;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CountryTest {
	
	@Test
	public void testInitialise() throws Exception {
		final String code = "US";
		
		final Country country = getCountry(code);
		
		assertThat(country.getCode().toUpperCase(), is(code));
		assertThat(country.getName(), is("United States"));
		assertThat(country.getCapitalCity(), is("Washington D.C."));
		assertThat(country.getRegion(), is("North America"));
	}

}

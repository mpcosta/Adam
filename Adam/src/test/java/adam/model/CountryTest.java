package adam.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CountryTest {
	
	@Test
	public void testInitialise() {
		final String code = "US";
		
		try {
			final Country country = Country.getCountry(code);
			
			assertThat(country.getCode().toUpperCase(), is(code));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

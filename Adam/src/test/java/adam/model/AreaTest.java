package adam.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AreaTest {
	
	@Test
	public void testIsCodeValid() throws Exception {
		assertThat(Area.codeIsValid(""), is(false));
		assertThat(Area.codeIsValid("g"), is(false));
		assertThat(Area.codeIsValid("gb"), is(true)); // XXX Fix failing test
		assertThat(Area.codeIsValid("gbb"), is(false));
	}
	
}

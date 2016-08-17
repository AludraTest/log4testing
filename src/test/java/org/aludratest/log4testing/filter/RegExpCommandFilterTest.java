package org.aludratest.log4testing.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.aludratest.log4testing.MockTestStepLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.junit.Test;

public class RegExpCommandFilterTest {

	@Test
	public void testDefault() throws InvalidConfigurationException {
		RegExpCommandFilter filter = new RegExpCommandFilter();

		// no setting should result in no filtering
		filter.init(new Properties());

		MockTestStepLog step = new MockTestStepLog(0, null);
		step.setCommand("filterMe");
		assertTrue(filter.filter(step));
	}

	@Test
	public void testNullCommand() throws InvalidConfigurationException {
		RegExpCommandFilter filter = new RegExpCommandFilter();

		Properties p = new Properties();
		p.setProperty("commandRegexp", "doPrefix.*");

		filter.init(p);

		MockTestStepLog step = new MockTestStepLog(0, null);

		// a NULL command should result in not being included (but no error)
		assertFalse(filter.filter(step));
	}

	@Test
	public void testNormal() throws InvalidConfigurationException {
		RegExpCommandFilter filter = new RegExpCommandFilter();

		Properties p = new Properties();
		p.setProperty("commandRegexp", "doPrefix.*");

		filter.init(p);

		MockTestStepLog step = new MockTestStepLog(0, null);
		step.setCommand("doPrefix123");
		assertTrue(filter.filter(step));

		step = new MockTestStepLog(0, null);
		step.setCommand("somedoPrefix123");
		assertFalse(filter.filter(step));
	}


}

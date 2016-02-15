package org.aludratest.log4testing.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.aludratest.log4testing.MockTestCaseLog;
import org.aludratest.log4testing.MockTestStepGroupLog;
import org.aludratest.log4testing.MockTestStepLog;
import org.aludratest.log4testing.MockTestSuiteLog;
import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestStepGroupLog;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.aludratest.log4testing.config.MockLog4TestingConfiguration;
import org.aludratest.log4testing.config.MockTestLogWriterConfiguration;
import org.aludratest.log4testing.config.MockTestStepFilterConfiguration;
import org.aludratest.log4testing.filter.MockTestStepFilter;
import org.aludratest.log4testing.output.LogWriterException;
import org.junit.Test;

/**
 * Tests the Log4Testing engine: It must register in the framework, must accept various configuration inputs, and forward all
 * framework events to all writers. Also, it must call the abbreviators and test step filters in a way which is transparent to the
 * writers.
 * 
 * @author falbrech
 * 
 */
public class Log4TestingEngineTest {

	@Test
	public void testDefaultConfiguration() {
		// should read the log4testing.xml in src/test/java
		// we detect this by checking MockTestLogWriter's counters
		MockTestLogWriter.startingTestProcessCalled.set(0);

		Log4TestingEngine engine = Log4TestingEngine.newEngine();
		MockTestFramework framework = new MockTestFramework();
		engine.applyTo(framework);

		MockTestSuiteLog suite = new MockTestSuiteLog(0, "TEST");
		framework.fireStartingTestProcess(suite);

		assertEquals(1, MockTestLogWriter.startingTestProcessCalled.get());
	}

	@Test
	public void testCustomConfigurationLocation() {
		MockTestLogWriter.startingTestProcessCalled.set(0);
		Log4TestingEngine engine = Log4TestingEngine.newEngine(getClass().getClassLoader().getResource("log4testing_empty.xml"));

		MockTestFramework framework = new MockTestFramework();
		engine.applyTo(framework);

		MockTestSuiteLog suite = new MockTestSuiteLog(0, "TEST");
		framework.fireStartingTestProcess(suite);

		assertEquals(0, MockTestLogWriter.startingTestProcessCalled.get());
	}

	@Test
	public void testCustomConfigurationObject() {
		MockLog4TestingConfiguration config = new MockLog4TestingConfiguration();

		config.getGlobalProperties().setProperty("testKey", "123");

		MockTestLogWriterConfiguration writerConfig = new MockTestLogWriterConfiguration(MyMockTestWriter.class.getName());
		// property resolution must also work for custom configuration objects (not to be contained in configuration objects
		// classes)
		writerConfig.getWriterProperties().setProperty("testVal", "${testKey}456");

		config.getTestLogWriterConfigurations().add(writerConfig);

		Log4TestingEngine engine = Log4TestingEngine.newEngine(config);

		MockTestFramework framework = new MockTestFramework();
		engine.applyTo(framework);

		MockTestSuiteLog suite = new MockTestSuiteLog(0, "TEST");
		framework.fireStartingTestProcess(suite);

		assertTrue(MyMockTestWriter.initCalled);
	}

	@Test
	public void testInvocationsAndTwoOfSameWriterClass() {
		MockLog4TestingConfiguration config = new MockLog4TestingConfiguration();

		MockTestLogWriterConfiguration writerConfig = new MockTestLogWriterConfiguration(
				TestInvocationsTestWriter.class.getName());
		config.getTestLogWriterConfigurations().add(writerConfig);
		// and the same again - must be supported.
		writerConfig = new MockTestLogWriterConfiguration(TestInvocationsTestWriter.class.getName());
		config.getTestLogWriterConfigurations().add(writerConfig);

		Log4TestingEngine engine = Log4TestingEngine.newEngine(config);

		MockTestFramework framework = new MockTestFramework();
		engine.applyTo(framework);

		MockTestSuiteLog suite = new MockTestSuiteLog(0, "TEST");
		MockTestCaseLog log = new MockTestCaseLog(2, "TESTCASE", suite);
		suite.addTestCase(log);

		framework.fireStartingTestProcess(suite);
		framework.fireStartingTestSuite(suite);
		framework.fireStartingTestCase(log);
		framework.fireFinishedTestCase(log);
		framework.fireFinishedTestSuite(suite);
		framework.fireFinishedTestProcess(suite);

		for (int i = 0; i < 6; i++) {
			assertEquals("Method #" + i + " not called twice", 2, TestInvocationsTestWriter.methodsCalled[i]);
		}
	}

	@Test
	public void testStepFilteringAndAbbreviations() {
		MockLog4TestingConfiguration config = new MockLog4TestingConfiguration();
		config.getAbbreviatorConfiguration().getAbbreviations().put("TESTSTEP", "TS");

		MockTestLogWriterConfiguration writerConfig = new MockTestLogWriterConfiguration(
				FilteringHelperTestWriter.class.getName());

		MockTestStepFilterConfiguration filterConfig = new MockTestStepFilterConfiguration(CountingTestStepFilter.class.getName());
		writerConfig.getTestStepFilters().add(filterConfig);
		config.getTestLogWriterConfigurations().add(writerConfig);
		Log4TestingEngine engine = Log4TestingEngine.newEngine(config);

		MockTestFramework framework = new MockTestFramework();
		engine.applyTo(framework);

		MockTestSuiteLog suite = new MockTestSuiteLog(0, "TEST");
		MockTestCaseLog log = new MockTestCaseLog(2, "TESTSTEPXX", suite);
		suite.addTestCase(log);

		MockTestStepGroupLog group = new MockTestStepGroupLog(3, "GROUP", log);
		log.addTestStepGroup(group);

		MockTestStepLog step = new MockTestStepLog(4, group);
		step.setCommand("TESTSTEP1");
		group.addTestStep(step);
		step = new MockTestStepLog(5, group);
		step.setCommand("TESTSTEP2");
		group.addTestStep(step);

		framework.fireStartingTestProcess(suite);
		framework.fireStartingTestSuite(suite);
		framework.fireStartingTestCase(log);
		framework.fireFinishedTestCase(log);
		framework.fireFinishedTestSuite(suite);
		framework.fireFinishedTestProcess(suite);

		assertEquals(2, CountingTestStepFilter.filterInvoked);
	}

	public static class MyMockTestWriter extends MockTestLogWriter {

		private static boolean initCalled;

		@Override
		public void init(Properties properties) throws InvalidConfigurationException {
			initCalled = true;

			String testVal = properties.getProperty("testVal");
			if (!"123456".equals(testVal)) {
				throw new InvalidConfigurationException("testVal: expected 123456, but found " + testVal);
			}
			super.init(properties);
		}

	}

	public static class TestInvocationsTestWriter extends MockTestLogWriter {

		private static int[] methodsCalled = new int[6];

		@Override
		public void startingTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
			methodsCalled[0]++;
		}

		@Override
		public void startingTestSuite(TestSuiteLog suite) throws LogWriterException {
			methodsCalled[1]++;
		}

		@Override
		public void startingTestCase(TestCaseLog testCase) throws LogWriterException {
			methodsCalled[2]++;
		}

		@Override
		public void finishedTestCase(TestCaseLog testCase) throws LogWriterException {
			methodsCalled[3]++;
			// assert steps are filtered
			for (TestStepGroupLog group : testCase.getTestStepGroups()) {
				if (group.getTestSteps().size() == 2) {
					throw new IllegalStateException("Expected filtered test steps, but received all");
				}
			}
		}

		@Override
		public void finishedTestSuite(TestSuiteLog suite) throws LogWriterException {
			methodsCalled[4]++;
		}

		@Override
		public void finishedTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
			methodsCalled[5]++;
		}

	}

	public static class FilteringHelperTestWriter extends MockTestLogWriter {
		@Override
		public void finishedTestCase(TestCaseLog testCase) throws LogWriterException {
			// assert steps are filtered
			for (TestStepGroupLog group : testCase.getTestStepGroups()) {
				if (group.getTestSteps().size() == 2) {
					throw new IllegalStateException("Expected filtered test steps, but received all");
				}
			}

			// assert test case abbreviation works
			if (!testCase.getName().equals("TSXX")) {
				throw new IllegalStateException("Abbreviation seems not to be active");
			}
		}

	}

	public static class CountingTestStepFilter extends MockTestStepFilter {

		static int filterInvoked;

		@Override
		public boolean filter(TestStepLog testStep) {
			filterInvoked++;
			if (!testStep.getCommand().startsWith("TESTSTEP")) {
				throw new IllegalStateException("Abbreviation seems to abbreviate test step commands");
			}

			return testStep.getCommand().equals("TESTSTEP2");
		}

	}
}

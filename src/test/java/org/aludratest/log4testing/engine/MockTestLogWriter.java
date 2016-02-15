package org.aludratest.log4testing.engine;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.aludratest.log4testing.output.LogWriterException;
import org.aludratest.log4testing.output.TestLogWriter;

public class MockTestLogWriter implements TestLogWriter {

	static AtomicInteger startingTestProcessCalled = new AtomicInteger();

	@Override
	public void init(Properties properties) throws InvalidConfigurationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startingTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
		startingTestProcessCalled.incrementAndGet();
	}

	@Override
	public void startingTestSuite(TestSuiteLog suite) throws LogWriterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startingTestCase(TestCaseLog testCase) throws LogWriterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishedTestCase(TestCaseLog testCase) throws LogWriterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishedTestSuite(TestSuiteLog suite) throws LogWriterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishedTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
		// TODO Auto-generated method stub

	}

}

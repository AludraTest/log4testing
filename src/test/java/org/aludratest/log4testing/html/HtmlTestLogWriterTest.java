package org.aludratest.log4testing.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import org.aludratest.log4testing.AbstractTestLogWriterTest;
import org.aludratest.log4testing.MockAttachmentLog;
import org.aludratest.log4testing.MockTestCaseLog;
import org.aludratest.log4testing.MockTestStepGroupLog;
import org.aludratest.log4testing.MockTestStepLog;
import org.aludratest.log4testing.MockTestSuiteLog;
import org.aludratest.log4testing.TestStatus;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.aludratest.log4testing.output.LogWriterException;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the HTML log writer. This includes validness of default Velocitymacro templates, writing of files at correct locations
 * etc.
 * 
 * @author falbrech
 * 
 */
public class HtmlTestLogWriterTest extends AbstractTestLogWriterTest {

	@Before
	public void clearRootFolder() throws IOException {
		File f = getRootFolder();
		if (f.isDirectory()) {
			FileUtils.deleteDirectory(f);
		}
	}

	private File getRootFolder() {
		return new File(new File(System.getProperty("user.dir")), "target/test/htmlwriter");
	}

	private Properties createProperties() {
		Properties p = new Properties();
		p.setProperty("outputFolder", "target/test/htmlwriter");

		return p;
	}

	@Test
	public void testBasic() throws InvalidConfigurationException, LogWriterException {
		// must create directory
		HtmlTestLogWriter writer = new HtmlTestLogWriter();
		Properties p = createProperties();
		writer.init(p);

		// writer must cope with changing objects (not same instance)
		writer.startingTestProcess(new MockTestSuiteLog(1, "root"));
		writer.finishedTestProcess(new MockTestSuiteLog(1, "root"));

		// now, the root directory and suite HTML must have been created
		File f = new File(getRootFolder(), "root.html");
		assertTrue(f.isFile());

		// also the additional resources
		f = new File(getRootFolder(), "log4testing.css");
		assertTrue(f.isFile());

		f = new File(getRootFolder(), "testcase.js");
		assertTrue(f.isFile());

		f = new File(getRootFolder(), "jquery.js");
		assertTrue(f.isFile());
	}

	@Test
	public void testComplex() throws InvalidConfigurationException, LogWriterException, IOException {
		HtmlTestLogWriter writer = new HtmlTestLogWriter();
		Properties p = createProperties();
		writer.init(p);

		MockTestSuiteLog root = createSuite("root");

		MockTestSuiteLog child1 = createSuite("child1", root);
		createSuite("child2", root);

		MockTestCaseLog case1 = createTestCase("case1", child1);
		case1.setStatus(TestStatus.FAILEDAUTOMATION);
		
		MockTestStepGroupLog group1 = createStepGroup("First Steps", case1);
		createStep("doSomething", TestStatus.PASSED, group1);
		MockTestStepLog step = createStep("doSomethingWrong", TestStatus.FAILEDAUTOMATION, group1);

		MockAttachmentLog attachment = new MockAttachmentLog("Some Log", "txt", "This is some log.");
		step.addAttachment(attachment);

		writer.startingTestProcess(root);
		writer.startingTestSuite(child1);
		writer.startingTestCase(case1);
		writer.finishedTestCase(case1);
		writer.finishedTestSuite(child1);
		writer.finishedTestProcess(root);

		// read output file into memory
		// macro must resolve all $myvar references
		String html = FileUtils.readFileToString(new File(getRootFolder(), "root.html"));
		assertFalse(Pattern.compile("\\$[a-zA-Z\\{]").matcher(html).find());

		html = FileUtils.readFileToString(new File(getRootFolder(), "case1.html"));
		assertFalse(Pattern.compile("\\$[a-zA-Z\\{]").matcher(html).find());

		// attachment must have been written
		assertTrue(new File(getRootFolder(), "__attachments/step7/attachment0.txt").isFile());
	}

	@Test
	public void testDateFormat() throws LogWriterException, IOException, InvalidConfigurationException {
		HtmlTestLogWriter writer = new HtmlTestLogWriter();
		Properties properties = createProperties();
		properties.setProperty("dateTimeFormat", "dd-MMM-yyyy HH:mm:ss");
		// This also tests, as a side effect, the suiteMacroFile parameter
		properties.setProperty("suiteMacroFile", HtmlTestLogWriterTest.class.getPackage().getName().replace(".", "/")
				+ "/dateFormat.vm");
		writer.init(properties);

		MockTestSuiteLog log = new MockTestSuiteLog(1, "testDateTime");
		log.setStartTime(DateTime.parse("2016-02-11T17:11"));

		writer.startingTestProcess(log);
		writer.startingTestSuite(log);
		log.setEndTime(DateTime.now());
		writer.finishedTestSuite(log);
		writer.finishedTestProcess(log);

		// check file for correct content
		String contents = FileUtils.readFileToString(new File(getRootFolder(), "testDateTime.html"));
		assertEquals("11-Feb-2016 17:11:00", contents);
	}

	@Test
	public void testCustomTestCaseMacro() throws LogWriterException, IOException, InvalidConfigurationException {
		HtmlTestLogWriter writer = new HtmlTestLogWriter();
		Properties properties = createProperties();
		properties.setProperty("testCaseMacroFile", HtmlTestLogWriterTest.class.getPackage().getName().replace(".", "/")
				+ "/custom.vm");
		writer.init(properties);

		MockTestSuiteLog suite = new MockTestSuiteLog(1, "testDateTime");

		MockTestCaseLog testCase = new MockTestCaseLog(2, "testCase", suite);
		suite.addTestCase(testCase);

		writer.startingTestProcess(suite);
		writer.startingTestSuite(suite);
		writer.startingTestCase(testCase);
		writer.finishedTestCase(testCase);
		writer.finishedTestSuite(suite);
		writer.finishedTestProcess(suite);

		// check file for correct content
		String contents = FileUtils.readFileToString(new File(getRootFolder(), "testCase.html"));
		assertEquals("Some custom text", contents);
	}

	@Test
	public void testAdditionalResources() throws InvalidConfigurationException {
		HtmlTestLogWriter writer = new HtmlTestLogWriter();
		Properties properties = createProperties();
		properties.setProperty("additionalResources", HtmlTestLogWriterTest.class.getPackage().getName().replace(".", "/")
				+ "/custom.vm");
		writer.init(properties);

		assertTrue(new File(getRootFolder(), "custom.vm").isFile());
	}

}

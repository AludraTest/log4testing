package org.aludratest.log4testing.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;

import org.aludratest.log4testing.AttachmentLog;
import org.aludratest.log4testing.NamedTestLogElement;
import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestStepGroupLog;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.TestStepLogContainer;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.aludratest.log4testing.output.LogWriterException;
import org.aludratest.log4testing.output.TestLogWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlTestLogWriter implements TestLogWriter {

	private static final Logger LOG = LoggerFactory.getLogger(HtmlTestLogWriter.class);

	private static final String DEFAULT_SUITE_MACRO = HtmlTestLogWriter.class.getPackage().getName().replace('.', '/')
			+ "/testSuite.vm";

	private static final String DEFAULT_TESTCASE_MACRO = HtmlTestLogWriter.class.getPackage().getName().replace('.', '/')
			+ "/testCase.vm";

	private static final String DEFAULT_DATE_TIME_FORMAT = "dd-MMM-yyyy HH:mm:ss";

	private static final String DEFAULT_ADDITIONAL_RESOURCES = "log4testing.css,testcase.js,jquery.js";

	private File rootFolder;
	
	private Template suiteTemplate;
	
	private Template testCaseTemplate;

	private VelocityEngine velocityEngine;

	private DateTimeFormatter dateTimeFormat;

	private PeriodFormatter periodFormat;

	@Override
	public void init(Properties properties) throws InvalidConfigurationException {
		rootFolder = new File(properties.getProperty("outputFolder", "target/log4testing"));
		rootFolder.mkdirs();
		if (!rootFolder.isDirectory()) {
			throw new InvalidConfigurationException("Could not create output folder " + rootFolder.getAbsolutePath());
		}
		
		try {
			dateTimeFormat = DateTimeFormat.forPattern(properties.getProperty("dateTimeFormat", DEFAULT_DATE_TIME_FORMAT));
		}
		catch (IllegalArgumentException e) {
			throw new InvalidConfigurationException("Invalid dateTimeFormat pattern", e);
		}

		// currently, not configurable
		periodFormat = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendLiteral(":").minimumPrintedDigits(2)
				.appendMinutes().appendLiteral(":").appendSeconds().appendLiteral(".").appendMillis3Digit().toFormatter();

		String suiteMacroFile = properties.getProperty("suiteMacroFile", DEFAULT_SUITE_MACRO);
		String testCaseMacroFile = properties.getProperty("testCaseMacroFile", DEFAULT_TESTCASE_MACRO);

		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, Slf4jLogChute.class.getName());
		velocityEngine.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.setProperty("eventhandler.include.class", "org.apache.velocity.app.event.implement.IncludeRelativePath");
		velocityEngine.init();

		try {
			suiteTemplate = velocityEngine.getTemplate(suiteMacroFile);
			testCaseTemplate = velocityEngine.getTemplate(testCaseMacroFile);
		}
		catch (ResourceNotFoundException e) {
			throw new InvalidConfigurationException("Could not find Velocitymacro Template for HTML rendering", e);
		}
		catch (ParseErrorException e) {
			throw new InvalidConfigurationException("Could not compile Velocitymacro Template for HTML rendering", e);
		}

		// copy additional resources to output directory
		String additionalResources = properties.getProperty("additionalResources", DEFAULT_ADDITIONAL_RESOURCES);
		for (String res : additionalResources.split(",")) {
			InputStream in = findResource(res);
			try {
				if (in == null) {
					throw new InvalidConfigurationException("Additional resource not found: " + res);
				}
				// copy to output directory
				writeToFile(in, new File(rootFolder, new File(res).getName()));
			}
			catch (IOException e) {
				throw new InvalidConfigurationException("Could not write additional resource to output folder", e);
			}
			finally {
				IOUtils.closeQuietly(in);
			}
		}
	}

	@Override
	public void startingTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
		// create HTML files for all suites and test cases
		updateSuiteHtml(rootSuite, true);
	}

	@Override
	public void startingTestSuite(TestSuiteLog suite) throws LogWriterException {
		updateSuiteAndParentsHtml(suite);
	}

	@Override
	public void startingTestCase(TestCaseLog testCase) throws LogWriterException {
		updateTestCaseHtml(testCase);
		updateSuiteAndParentsHtml(testCase.getParent());
	}

	@Override
	public void finishedTestCase(TestCaseLog testCase) throws LogWriterException {
		updateTestCaseHtml(testCase);
		updateSuiteAndParentsHtml(testCase.getParent());
	}

	@Override
	public void finishedTestSuite(TestSuiteLog suite) throws LogWriterException {
		updateSuiteAndParentsHtml(suite);
	}

	@Override
	public void finishedTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
		updateSuiteHtml(rootSuite, false);
	}

	private void updateSuiteAndParentsHtml(TestSuiteLog suite) throws LogWriterException {
		if (suite != null) {
			updateSuiteHtml(suite, false);
			updateSuiteAndParentsHtml(suite.getParent());
		}
	}

	private void updateSuiteHtml(TestSuiteLog suite, boolean recursive) throws LogWriterException {
		File outputFile = getPathToSuite(suite);
		updateHtml(suiteTemplate, suite, "testSuite", outputFile);

		if (recursive) {
			for (TestSuiteLog child : suite.getChildSuites()) {
				updateSuiteHtml(child, true);
			}
		}
	}

	private void updateTestCaseHtml(TestCaseLog testCase) throws LogWriterException {
		File outputFile = getPathToTestCase(testCase);
		updateHtml(testCaseTemplate, testCase, "testCase", outputFile);
	}

	private synchronized void updateHtml(Template template, NamedTestLogElement testObject, String variableName, File outputFile)
			throws LogWriterException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter writer = new BufferedWriter(osw);
			template.merge(createContext(testObject, variableName), writer);
			writer.flush();
		}
		catch (Exception e) {
			// velocitymacro could throw ANY exception, e.g. NoSuchElementException
			throw new LogWriterException("Could not write " + variableName + " HTML", e);
		}
		finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private File getPathToSuite(TestSuiteLog suite) {
		File path = new File(rootFolder, suite.getName().replace(".", "/") + ".html").getAbsoluteFile();
		path.getParentFile().mkdirs();
		return path;
	}

	private File getPathToTestCase(TestCaseLog testCase) {
		File path = new File(rootFolder, testCase.getName().replace(".", "/") + ".html").getAbsoluteFile();
		path.getParentFile().mkdirs();
		return path;
	}

	private TimeFormat timeFormat = new TimeFormat();

	private HtmlFormat htmlFormat = new HtmlFormat();

	private PathHelper pathHelper = new PathHelper();

	private VelocityContext createContext(NamedTestLogElement testObject, String variableName) {
		VelocityContext context = new VelocityContext();
		context.put(variableName, testObject);
		context.put("time", timeFormat);
		context.put("html", htmlFormat);
		context.put("pathHelper", pathHelper);
		return context;
	}

	private InputStream findResource(String resourceName) {
		// try current directory
		File workDir = new File(System.getProperty("user.dir")).getAbsoluteFile();
		File f = new File(workDir, resourceName);
		if (f.isFile()) {
			try {
				return new FileInputStream(f);
			}
			catch (FileNotFoundException e) {
				// safe to ignore (must have been deleted in parallel)
				// fall through to next tries
				LOG.debug("Could not find resource in current working directory", e);
			}
		}

		// use context classloader
		try {
			InputStream in = findResource(Thread.currentThread().getContextClassLoader(), resourceName);
			if (in != null) {
				return in;
			}
		}
		catch (IOException e) {
			// ignore
			LOG.debug("Could not open resource on classpath", e);
		}

		// try, seen from current class
		URL url = HtmlTestLogWriter.class.getResource(resourceName);
		if (url != null) {
			try {
				return url.openStream();
			}
			catch (IOException e) {
				LOG.debug("Could not open resource on writer's classpath", e);
			}
		}

		// now, try our classloder
		try {
			return findResource(HtmlTestLogWriter.class.getClassLoader(), resourceName);
		}
		catch (IOException e) {
			LOG.debug("Could not open resource on classpath", e);
			return null;
		}
	}

	private InputStream findResource(ClassLoader cl, String resourceName) throws IOException {
		URL url = cl.getResource(resourceName);
		if (url != null) {
			return url.openStream();
		}

		return null;
	}

	private static void writeToFile(InputStream in, File f) throws IOException {
		FileOutputStream fos = new FileOutputStream(f);
		try {
			IOUtils.copy(in, fos);
		}
		finally {
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Formats DateTimes, Durations and Periods.
	 */
	public class TimeFormat {

		private TimeFormat() {
		}

		public String format(Object object) {
			if (object instanceof DateTime) {
				return formatDateTime((DateTime) object);
			}
			if (object instanceof Period) {
				return formatPeriod((Period) object);
			}
			if (object instanceof Duration) {
				return formatDuration((Duration) object);
			}

			return object == null ? "" : object.toString();
		}

		private String formatDateTime(DateTime dateTime) {
			return dateTime.toString(dateTimeFormat);
		}

		private String formatPeriod(Period period) {
			return period.toString(periodFormat);
		}

		private String formatDuration(Duration duration) {
			return formatPeriod(duration.toPeriod());
		}
	}

	/**
	 * Formats texts in HTML.
	 * 
	 * @author Volker Bergmann
	 */
	public class HtmlFormat {

		/**
		 * @param text
		 *            the text to format in HTML
		 * @return an HTML representation of the text
		 */
		public String format(String text) {
			if (text == null) {
				return "";
			}
			return StringEscapeUtils.escapeHtml4(text).replace("\n", "<br />");
		}
	}

	public class PathHelper {

		public String getPathToBaseDir(TestSuiteLog suiteLog) {
			return getPathToBaseDir(getPathToSuite(suiteLog));
		}

		public String getPathFromBaseDir(TestSuiteLog suiteLog) {
			return getPathFromBaseDir(getPathToSuite(suiteLog));
		}

		public String getPathToBaseDir(TestCaseLog testCaseLog) {
			return getPathToBaseDir(getPathToTestCase(testCaseLog));
		}

		public String getPathFromBaseDir(TestCaseLog testCaseLog) {
			return getPathFromBaseDir(getPathToTestCase(testCaseLog));
		}

		public String getPathToAttachment(TestStepLog step, AttachmentLog attachment) {
			int index = step.getAttachments().indexOf(attachment);
			if (index == -1) {
				return null;
			}
			File attachmentFile = new File(rootFolder, "__attachments/step" + step.getId() + "/attachment" + index + "."
					+ attachment.getFileExtension());
			attachmentFile.getParentFile().mkdirs();
			InputStream in = attachment.getFileContents();
			try {
				writeToFile(in, attachmentFile);
			}
			catch (IOException e) {
				LOG.warn("Could not write attachment file " + attachmentFile.getAbsolutePath(), e);
			}
			finally {
				IOUtils.closeQuietly(in);
			}

			TestStepLogContainer container = step;
			while (container instanceof TestStepLog) {
				container = ((TestStepLog) container).getParent();
			}

			if (!(container instanceof TestStepGroupLog)) {
				return null;
			}

			TestCaseLog testCase = ((TestStepGroupLog) container).getParent();
			return PathUtils.getRelativePath(attachmentFile.getAbsoluteFile(), getPathToTestCase(testCase).getParentFile(), "/");
		}

		private String getPathFromBaseDir(File f) {
			return PathUtils.getRelativePath(f, rootFolder, "/");
		}

		private String getPathToBaseDir(File f) {
			return PathUtils.getRelativePath(rootFolder, f, "/");
		}
	}

}

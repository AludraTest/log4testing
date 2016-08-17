package org.aludratest.log4testing.xml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.aludratest.log4testing.AttachmentLog;
import org.aludratest.log4testing.NamedTestLogElement;
import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestLogElement;
import org.aludratest.log4testing.TestStatus;
import org.aludratest.log4testing.TestStepGroupLog;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.aludratest.log4testing.output.LogWriterException;
import org.aludratest.log4testing.output.TestLogWriter;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Writes log results to an XML file. The whole file is only written when {@link #finishedTestProcess(TestSuiteLog)} is called, so
 * no XML is available during the tests run.
 * 
 * @author falbrech
 * 
 */
public class XmlTestLogWriter implements TestLogWriter {

	private XMLStreamWriter xmlWriter;

	private File xmlFile;

	private boolean compress;

	private boolean includeAttachments;

	@Override
	public void init(Properties properties) throws InvalidConfigurationException {
		String fileName = properties.getProperty("outputFile", "target/testResults.xml");
		compress = Boolean.valueOf(properties.getProperty("compress", "false"));
		includeAttachments = Boolean.valueOf(properties.getProperty("includeAttachments", "true"));

		xmlFile = new File(fileName);
		xmlFile.getParentFile().mkdirs();
		try {
			// check that file is writable; reset file
			new FileOutputStream(xmlFile).close();
		}
		catch (IOException e) {
			throw new InvalidConfigurationException("Could not create XML output file " + xmlFile.getAbsolutePath(), e);
		}
	}

	@Override
	public void startingTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
	}

	@Override
	public void startingTestSuite(TestSuiteLog suite) throws LogWriterException {
	}

	@Override
	public void startingTestCase(TestCaseLog testCase) throws LogWriterException {
	}

	@Override
	public void finishedTestCase(TestCaseLog testCase) throws LogWriterException {
	}

	@Override
	public void finishedTestSuite(TestSuiteLog suite) throws LogWriterException {
	}

	@Override
	public void finishedTestProcess(TestSuiteLog rootSuite) throws LogWriterException {
		OutputStream os = null;		
		try {
			os = new BufferedOutputStream(new FileOutputStream(xmlFile));
			if (compress) {
				os = new GZIPOutputStream(os);
			}

			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			xmlWriter = factory.createXMLStreamWriter(os, "UTF-8");

			xmlWriter.writeStartDocument("UTF-8", "1.0");
			writeSuite(rootSuite);
			xmlWriter.writeEndDocument();
			xmlWriter.close();
		}
		catch (XMLStreamException e) {
			throw new LogWriterException("Exception when writing XML document", e);
		}
		catch (IOException e) {
			throw new LogWriterException("Exception when creating XML file", e);
		}
		finally {
			IOUtils.closeQuietly(os);
		}
	}

	private void writeSuite(TestSuiteLog suite) throws XMLStreamException {
		xmlWriter.writeStartElement("testSuite");
		writeCommonNamedAttributes(suite);

		List<? extends TestCaseLog> testCases = suite.getTestCases();
		if (!testCases.isEmpty()) {
			xmlWriter.writeStartElement("testCases");
			for (TestCaseLog testCase : testCases) {
				writeTestCase(testCase);
			}
			xmlWriter.writeEndElement();
		}

		List<? extends TestSuiteLog> childSuites = suite.getChildSuites();
		if (!childSuites.isEmpty()) {
			xmlWriter.writeStartElement("testSuites");
			for (TestSuiteLog log : childSuites) {
				writeSuite(log);
			}
			xmlWriter.writeEndElement();
		}

		xmlWriter.writeEndElement();
	}

	private void writeTestCase(TestCaseLog testCase) throws XMLStreamException {
		xmlWriter.writeStartElement("testCase");
		if (testCase.isIgnored()) {
			xmlWriter.writeAttribute("ignored", "true");
		}

		writeCommonNamedAttributes(testCase);
		writeAttributeElement("ignoredReason", testCase.getIgnoredReason());

		List<? extends TestStepGroupLog> groups = testCase.getTestStepGroups();
		if (!groups.isEmpty()) {
			xmlWriter.writeStartElement("testStepGroups");

			for (TestStepGroupLog group : groups) {
				writeTestStepGroup(group);
			}

			xmlWriter.writeEndElement();
		}

		xmlWriter.writeEndElement();
	}

	private void writeTestStepGroup(TestStepGroupLog group) throws XMLStreamException {
		xmlWriter.writeStartElement("testStepGroup");
		writeCommonNamedAttributes(group);

		List<? extends TestStepLog> steps = group.getTestSteps();
		if (!steps.isEmpty()) {
			xmlWriter.writeStartElement("testSteps");

			for (TestStepLog step : steps) {
				writeTestStep(step);
			}

			xmlWriter.writeEndElement();
		}

		xmlWriter.writeEndElement();
	}

	private void writeTestStep(TestStepLog step) throws XMLStreamException {
		xmlWriter.writeStartElement("testStep");

		writeCommonAttributes(step);
		writeAttributeElement("offsetSeconds", String.valueOf(step.getStartTimeOffsetSeconds()));
		writeAttributeElement("command", step.getCommand());
		writeAttributeElement("elementType", step.getElementType());
		writeAttributeElement("elementName", step.getElementName());
		writeAttributeElement("technicalLocator", step.getTechnicalLocator());
		writeAttributeElement("usedArguments", step.getUsedArguments());
		writeAttributeElement("technicalArguments", step.getTechnicalArguments());
		writeAttributeElement("service", step.getService());
		writeAttributeElement("result", step.getResult());
		writeAttributeElement("errorMessage", step.getErrorMessage());
		writeExceptionElement("error", step.getError());
		writeAttributeElement("comment", step.getComment());

		List<? extends TestStepLog> childSteps = step.getTestSteps();
		if (!childSteps.isEmpty()) {
			xmlWriter.writeStartElement("testSteps");

			for (TestStepLog log : childSteps) {
				writeTestStep(log);
			}

			xmlWriter.writeEndElement();
		}

		if (includeAttachments) {
			List<? extends AttachmentLog> attachments = step.getAttachments();
			if (!attachments.isEmpty()) {
				xmlWriter.writeStartElement("attachments");

				for (AttachmentLog attachment : attachments) {
					writeAttachment(attachment);
				}

				xmlWriter.writeEndElement();
			}
		}

		xmlWriter.writeEndElement();
	}

	private void writeAttachment(AttachmentLog attachment) throws XMLStreamException {
		xmlWriter.writeStartElement("attachment");

		writeAttributeElement("label", attachment.getLabel());
		writeAttributeElement("fileExtension", attachment.getFileExtension());

		xmlWriter.writeStartElement("fileContents");
		InputStream in = attachment.getFileContents();
		XMLStreamWriterStream os = new XMLStreamWriterStream(xmlWriter);
		Base64OutputStream bos = new Base64OutputStream(os);
		try {
			IOUtils.copy(in, bos);
		}
		catch (IOException e) {
			// could be wrapped XMLSE by our writer, do not wrap twice
			if (e.getCause() instanceof XMLStreamException) {
				throw (XMLStreamException) e.getCause();
			}
			throw new XMLStreamException(e);
		}
		finally {
			IOUtils.closeQuietly(in);
			// no-ops, but good for static code checkers
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(os);
		}

		xmlWriter.writeEndElement();

		xmlWriter.writeEndElement();
	}

	private void writeCommonNamedAttributes(NamedTestLogElement element) throws XMLStreamException {
		writeAttributeElement("name", element.getName());
		writeCommonAttributes(element);
	}

	private void writeCommonAttributes(TestLogElement element) throws XMLStreamException {
		writeAttributeElement("id", String.valueOf(element.getId()));
		writeAttributeElement("startTime", element.getStartTime());
		writeAttributeElement("endTime", element.getEndTime());
		writeAttributeElement("duration", element.getDuration());
		writeAttributeElement("work", element.getWork());

		// for ignored test cases, check if there is a test step group with a non-ignored status, and use that
		TestStatus status = element.getStatus();
		if (element instanceof TestCaseLog) {
			TestCaseLog testCase = (TestCaseLog) element;
			if (testCase.isIgnored()) {
				if (!testCase.isFailed()) {
					status = TestStatus.PASSED;
				}
				else {
					for (TestStepGroupLog group : testCase.getTestStepGroups()) {
						if (group.getStatus() != TestStatus.IGNORED && group.getStatus() != TestStatus.PASSED) {
							status = group.getStatus();
							// no break here; use last non-ignored, failed status
						}
					}
				}
			}
		}
		writeAttributeElement("status", status);
	}

	private void writeAttributeElement(String attributeName, Object value) throws XMLStreamException {
		if (value == null) {
			return;
		}
		xmlWriter.writeStartElement(attributeName);
		String data = value.toString();
		// newline conversion required?
		if (data.contains("\n")) {
			// remove windows style line break
			data = data.replace("\r", "");
			// write line by line, followed by an &#13;
			for (String line : StringUtils.split(data, "\n")) {
				xmlWriter.writeCharacters(line);
				xmlWriter.writeEntityRef("#13");
			}
		}
		else {
			xmlWriter.writeCharacters(data);
		}
		xmlWriter.writeEndElement();
	}

	private void writeExceptionElement(String attributeName, Throwable exception) throws XMLStreamException {
		if (exception == null) {
			return;
		}

		xmlWriter.writeStartElement(attributeName);

		writeAttributeElement("class", exception.getClass().getName());
		writeAttributeElement("message", exception.getMessage());
		writeExceptionElement("cause", exception.getCause());

		xmlWriter.writeEndElement();
	}

	/**
	 * An adapter which writes ISO-8859-1 (which includes Base64 characters) stream data to the XML writer. This is only intended
	 * for usage together with a wrapping Base64OutputStream and should not be used / copied to outside this class!
	 * 
	 * @author falbrech
	 * 
	 */
	private static class XMLStreamWriterStream extends OutputStream {

		private XMLStreamWriter writer;

		public XMLStreamWriterStream(XMLStreamWriter writer) {
			this.writer = writer;
		}

		@Override
		public void write(int b) throws IOException {
			char ch = (char) b;
			try {
				writer.writeCharacters(new char[] { ch }, 0, 1);
			}
			catch (XMLStreamException e) {
				throw new IOException(e);
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			char[] characters = new char[len];
			for (int i = off; i < off + len; i++) {
				characters[i - off] = (char) b[i];
			}

			try {
				writer.writeCharacters(characters, 0, characters.length);
			}
			catch (XMLStreamException e) {
				throw new IOException(e);
			}
		}
	}

}

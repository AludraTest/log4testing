package org.aludratest.log4testing.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.aludratest.log4testing.AbstractTestLogWriterTest;
import org.aludratest.log4testing.MockAttachmentLog;
import org.aludratest.log4testing.MockTestCaseLog;
import org.aludratest.log4testing.MockTestStepGroupLog;
import org.aludratest.log4testing.MockTestStepLog;
import org.aludratest.log4testing.MockTestSuiteLog;
import org.aludratest.log4testing.TestStatus;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.aludratest.log4testing.output.LogWriterException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlTestLogWriterTest extends AbstractTestLogWriterTest {

	@Before
	public void removeFile() throws IOException {
		File f = getOutputFile();
		FileUtils.deleteQuietly(f);
	}

	private File getOutputFile() {
		return new File(new File(System.getProperty("user.dir")), "target/test/xmlwriter/result.xml");
	}

	private Properties createProperties() {
		Properties p = new Properties();
		p.setProperty("outputFile", getOutputFile().getAbsolutePath());

		return p;
	}

	@Test
	public void testBasic()
			throws InvalidConfigurationException, LogWriterException, SAXException, IOException, ParserConfigurationException {
		// must create directory
		XmlTestLogWriter writer = new XmlTestLogWriter();
		Properties p = createProperties();
		writer.init(p);

		// writer must cope with changing objects (not same instance)
		writer.startingTestProcess(new MockTestSuiteLog(1, "root"));
		writer.finishedTestProcess(new MockTestSuiteLog(1, "root"));

		// check that XML file has been created
		File f = getOutputFile();
		assertTrue(f.isFile());

		// check that no namespace if present in file
		Document doc = parseXml(f);
		Element root = doc.getDocumentElement();
		assertEquals("testSuite", root.getTagName());
		assertNull(root.getNamespaceURI());

		assertEquals("root", getElementText(root, "name"));
		assertEquals("PASSED", getElementText(root, "status"));
		assertEquals("1", getElementText(root, "id"));
	}

	@Test
	public void testComplex() throws Exception {
		XmlTestLogWriter writer = new XmlTestLogWriter();
		Properties p = createProperties();
		writer.init(p);

		doComplexTest(writer, true);
	}

	@Test
	public void testNoAttachmentsFlag() throws Exception {
		XmlTestLogWriter writer = new XmlTestLogWriter();
		Properties p = createProperties();
		p.setProperty("includeAttachments", "false");
		writer.init(p);

		doComplexTest(writer, false);
	}

	@Test
	public void testCompressFlag() throws Exception {
		XmlTestLogWriter writer = new XmlTestLogWriter();
		Properties p = createProperties();
		p.setProperty("compress", "true");
		writer.init(p);

		// writer must cope with changing objects (not same instance)
		writer.startingTestProcess(new MockTestSuiteLog(1, "root"));
		writer.finishedTestProcess(new MockTestSuiteLog(1, "root"));

		// check that file has been created
		File f = getOutputFile();
		assertTrue(f.isFile());

		// file must be compressed (gzipped)
		FileInputStream fis = new FileInputStream(f);
		try {
			GZIPInputStream gis = new GZIPInputStream(fis);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(gis, baos);

			// just to be sure, read that data into a document
			DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(baos.toByteArray()));
		}
		catch (ZipException e) {
			fail("Output file is not compressed though configured compress = true");
		}
		finally {
			IOUtils.closeQuietly(fis);
		}
	}

	private void doComplexTest(XmlTestLogWriter writer, boolean expectAttachment) throws Exception {
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

		File f = getOutputFile();
		assertTrue(f.isFile());

		Document doc = parseXml(f);
		
		// assert that everything is there - directly go to attachment step
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();
		Element stepElem = (Element) path.evaluate("/testSuite[./name/text()='root']/testSuites/testSuite[./name/text()='child1']/testCases/testCase[./name/text()='case1']/testStepGroups/testStepGroup[./name/text() = 'First Steps']/testSteps/testStep[2]", doc, XPathConstants.NODE);
		
		assertEquals("doSomethingWrong", getElementText(stepElem, "command"));
		assertEquals("FAILEDAUTOMATION", getElementText(stepElem, "status"));
		
		// get and check the attachment, if expected
		if (expectAttachment) {
			Element attElem = getChildElement(getChildElement(stepElem, "attachments", true), "attachment", true);
			assertEquals("Some Log", getElementText(attElem, "label"));
			assertEquals("txt", getElementText(attElem, "fileExtension"));
			assertEquals(Base64.encodeBase64String("This is some log.".getBytes("UTF-8")),
					getElementText(attElem, "fileContents"));
		}
		else {
			Element attElem = getChildElement(stepElem, "attachments", false);
			assertNull("Attachment included in XML though configured includeAttachments = false", attElem);
		}
	}

	private Document parseXml(File f) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		return dbf.newDocumentBuilder().parse(f);
	}

	private Element getChildElement(Element element, String childTagName, boolean required) {
		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				if (childTagName.equals(e.getTagName())) {
					return e;
				}
			}
		}

		if (required) {
			fail("Element " + childTagName + " not found within element " + element.getTagName());
		}
		return null;
	}

	private String getElementText(Element element, String childTagName) {
		// normalizes the text
		return getChildElement(element, childTagName, true).getTextContent().trim().replaceAll("\\s+$", "").replaceAll("^\\s+",
				"");
	}

}

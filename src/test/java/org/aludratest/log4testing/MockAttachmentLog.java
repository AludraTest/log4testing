package org.aludratest.log4testing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.aludratest.log4testing.AttachmentLog;

public class MockAttachmentLog implements AttachmentLog {

	private String label;
	private String extension;
	private byte[] contents;

	public MockAttachmentLog(String label, String extension, String contents) {
		this(label, extension, contents.getBytes());
	}

	public MockAttachmentLog(String label, String extension, byte[] contents) {
		this.label = label;
		this.extension = extension;
		this.contents = contents;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getFileExtension() {
		return extension;
	}

	@Override
	public InputStream getFileContents() {
		return new ByteArrayInputStream(contents);
	}

}

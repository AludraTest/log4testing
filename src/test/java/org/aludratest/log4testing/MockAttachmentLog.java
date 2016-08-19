package org.aludratest.log4testing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MockAttachmentLog extends AbstractAttachmentLog {

	private byte[] contents;

	public MockAttachmentLog(String label, String extension, String contents) throws UnsupportedEncodingException {
		super(label, extension);
		this.contents = contents.getBytes("UTF-8");
	}

	public MockAttachmentLog(String label, String extension, byte[] contents) {
		super(label, extension);
		this.contents = contents;
	}

	@Override
	public InputStream getFileContents() {
		return new ByteArrayInputStream(contents);
	}

}

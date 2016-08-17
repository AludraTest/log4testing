package org.aludratest.log4testing;

import java.io.InputStream;

public interface AttachmentLog {

	String getLabel();

	String getFileExtension();

	InputStream getFileContents();

}

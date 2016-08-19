package org.aludratest.log4testing;

import java.io.InputStream;

/**
 * Interface for attachment log elements. Such elements hold attachments made to a test step log by the Test Framework.
 * Attachments could e.g. be screenshots, error logs, etc. <br>
 * A Test Framework may not generate attachments at all, this is totally fine.
 * 
 * @author falbrech
 *
 */
public interface AttachmentLog {

	/**
	 * Returns a human-readable label for this attachment, e.g. "Screenshot of active window".
	 * 
	 * @return A human-readable label for this attachment, never <code>null</code>.
	 */
	String getLabel();

	/**
	 * Returns a suitable file extension for this attachment, so it can be stored with better automatic MIME type detection. For
	 * screenshots, this would e.g. return "png" or "jpeg". For text logs, "txt" seems to be a good value.
	 * 
	 * @return A suitable file extension for this attachment, never <code>null</code>.
	 */
	String getFileExtension();

	/**
	 * Returns the binary contents of this file as an input stream. Each call of this method should create a new stream, which
	 * must be closed by the caller.
	 * 
	 * @return The binary contents of this file as an input stream which must be closed by the caller, never <code>null</code>.
	 */
	InputStream getFileContents();

}

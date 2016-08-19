package org.aludratest.log4testing;


/**
 * Convenience abstract base class which implements <code>label</code> and <code>extension</code> properties for attachment log
 * elements. Subclasses only need to determine how to get the input stream to the data.
 * 
 * @author falbrech
 *
 */
public abstract class AbstractAttachmentLog implements AttachmentLog {

	private String label;

	private String fileExtension;

	/**
	 * Creates a new attachment log element with given label and extension.
	 * 
	 * @param label
	 *            Label to use for the attachment log element.
	 * @param fileExtension
	 *            File extension to use for the attachment log element.
	 * 
	 * @see #getLabel()
	 * @see #getFileExtension()
	 */
	protected AbstractAttachmentLog(String label, String fileExtension) {
		this.label = label;
		this.fileExtension = fileExtension;
	}

	@Override
	public final String getLabel() {
		return label;
	}

	@Override
	public final String getFileExtension() {
		return fileExtension;
	}

}

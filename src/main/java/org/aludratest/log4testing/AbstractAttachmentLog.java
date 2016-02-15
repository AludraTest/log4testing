package org.aludratest.log4testing;


public abstract class AbstractAttachmentLog implements AttachmentLog {

	private String label;

	private String fileExtension;

	protected AbstractAttachmentLog(String label, String fileExtension) {
		this.label = label;
		this.fileExtension = fileExtension;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getFileExtension() {
		return fileExtension;
	}

}

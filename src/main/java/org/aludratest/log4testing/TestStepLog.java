package org.aludratest.log4testing;

import java.util.List;

public interface TestStepLog extends TestStepLogContainer {

	int getStartTimeOffsetSeconds();

	TestStepLogContainer getParent();

	String getCommand();

	String getService();

	String getElementType();

	String getElementName();

	String getTechnicalLocator();

	String getTechnicalArguments();

	String getUsedArguments();

	String getResult();

	String getErrorMessage();

	Throwable getError();

	String getComment();

	List<? extends AttachmentLog> getAttachments();

}

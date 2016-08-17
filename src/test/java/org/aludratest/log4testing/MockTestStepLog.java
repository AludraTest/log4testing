package org.aludratest.log4testing;

import java.util.ArrayList;
import java.util.List;

import org.aludratest.log4testing.AttachmentLog;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.TestStepLogContainer;

public class MockTestStepLog extends MockNamedTestLogElement implements TestStepLog {

	private int startTimeOffsetSeconds;

	private TestStepLogContainer parent;

	private String command;

	private String service;

	private String elementType;

	private String elementName;

	private String technicalLocator;

	private String technicalArguments;

	private String usedArguments;

	private String result;

	private String errorMessage;

	private Throwable error;

	private String comment;

	private List<TestStepLog> childSteps = new ArrayList<TestStepLog>();

	private List<AttachmentLog> attachments = new ArrayList<AttachmentLog>();

	public MockTestStepLog(int id, TestStepLogContainer parent) {
		super(id, "step" + id);
		this.parent = parent;
	}

	@Override
	public List<? extends TestStepLog> getTestSteps() {
		return childSteps;
	}

	public void addChildTestStep(TestStepLog step) {
		childSteps.add(step);
	}

	@Override
	public int getStartTimeOffsetSeconds() {
		return startTimeOffsetSeconds;
	}

	public void setStartTimeOffsetSeconds(int startTimeOffsetSeconds) {
		this.startTimeOffsetSeconds = startTimeOffsetSeconds;
	}

	@Override
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@Override
	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	@Override
	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	@Override
	public String getTechnicalLocator() {
		return technicalLocator;
	}

	public void setTechnicalLocator(String technicalLocator) {
		this.technicalLocator = technicalLocator;
	}

	@Override
	public String getTechnicalArguments() {
		return technicalArguments;
	}

	public void setTechnicalArguments(String technicalArguments) {
		this.technicalArguments = technicalArguments;
	}

	@Override
	public String getUsedArguments() {
		return usedArguments;
	}

	public void setUsedArguments(String usedArguments) {
		this.usedArguments = usedArguments;
	}

	@Override
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	@Override
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public TestStepLogContainer getParent() {
		return parent;
	}

	@Override
	public List<AttachmentLog> getAttachments() {
		return attachments;
	}

	public void addAttachment(AttachmentLog attachment) {
		attachments.add(attachment);
	}

}

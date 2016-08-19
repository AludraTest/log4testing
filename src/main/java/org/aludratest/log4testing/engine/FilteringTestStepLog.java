package org.aludratest.log4testing.engine;

import java.util.List;

import org.aludratest.log4testing.AttachmentLog;
import org.aludratest.log4testing.TestStepGroupLog;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.TestStepLogContainer;

/**
 * This wrapper only exists to add filtering wrappers when asking the test step for its parent.
 * 
 * @author falbrech
 * 
 */
class FilteringTestStepLog extends FilteringTestLogElement<TestStepLog> implements TestStepLog {

	public FilteringTestStepLog(TestStepLog delegate, LogContext context) {
		super(delegate, context);
	}

	@Override
	public TestStepLogContainer getParent() {
		TestStepLogContainer parent = getDelegate().getParent();
		
		if (parent instanceof TestStepLog) {
			return new FilteringTestStepLog((TestStepLog) parent, getLogContext());
		}
		if (parent instanceof TestStepGroupLog) {
			return new FilteringTestStepGroupLog((TestStepGroupLog) parent, getLogContext());
		}
		
		// well, better than returning null...
		return parent;
	}

	@Override
	public int getStartTimeOffsetSeconds() {
		return getDelegate().getStartTimeOffsetSeconds();
	}

	@Override
	public List<? extends TestStepLog> getTestSteps() {
		return getDelegate().getTestSteps();
	}

	@Override
	public String getCommand() {
		return getDelegate().getCommand();
	}

	@Override
	public String getService() {
		return getDelegate().getService();
	}

	@Override
	public String getElementType() {
		return getDelegate().getElementType();
	}

	@Override
	public String getElementName() {
		return getDelegate().getElementName();
	}

	@Override
	public String getTechnicalLocator() {
		return getDelegate().getTechnicalLocator();
	}

	@Override
	public String getTechnicalArguments() {
		return getDelegate().getTechnicalArguments();
	}

	@Override
	public String getUsedArguments() {
		return getDelegate().getUsedArguments();
	}

	@Override
	public String getResult() {
		return getDelegate().getResult();
	}

	@Override
	public String getErrorMessage() {
		return getDelegate().getErrorMessage();
	}

	@Override
	public Throwable getError() {
		return getDelegate().getError();
	}

	@Override
	public String getComment() {
		return getDelegate().getComment();
	}

	@Override
	public List<? extends AttachmentLog> getAttachments() {
		return getDelegate().getAttachments();
	}
}

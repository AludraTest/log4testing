package org.aludratest.log4testing;


public interface TestStepGroupLog extends NamedTestLogElement, TestStepLogContainer {

	TestCaseLog getParent();

}

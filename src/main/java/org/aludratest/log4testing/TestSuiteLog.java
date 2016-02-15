package org.aludratest.log4testing;

import java.util.List;

public interface TestSuiteLog extends NamedTestLogElement {

	TestSuiteLog getParent();

	List<? extends TestSuiteLog> getChildSuites();

	List<? extends TestCaseLog> getTestCases();

	TestSuiteStatistics gatherStatistics();


}

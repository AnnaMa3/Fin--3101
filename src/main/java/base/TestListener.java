package base;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {


    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log(iTestResult.getName() + " -- Test method success\n");
    }
    private void log(String string) {
        System.out.print(string);
    }

}
package top.jjust.sockettransport;

import android.app.Application;
import android.test.ApplicationTestCase;

import top.jjust.common.StaticValue;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        System.out.println(StaticValue.fileRootPath);

    }
}
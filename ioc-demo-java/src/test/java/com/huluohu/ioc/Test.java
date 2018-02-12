package com.huluohu.ioc;

import com.huluohu.ioc.core.JsonApplicationContext;
import com.huluohu.ioc.entity.Robot;

public class Test {
    public static void main(String[] args) throws Exception {
        JsonApplicationContext context = new JsonApplicationContext("application.json");
        Robot robot = (Robot) context.getBean("robot");
        robot.show();
    }
}

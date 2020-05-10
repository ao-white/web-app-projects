/**
 *
 * @author Alex White
 */

package com.aow.flooringprogram;

import com.aow.flooringprogram.controller.FlooringProgramController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /*
        UserIO myIo = new UserIOConsoleImpl();
        FlooringProgramView myView = new FlooringProgramView(myIo);
        FlooringProgramDao myDao = new FlooringProgramDaoFileImpl();
        FlooringProgramAuditDao myAuditDao = new FlooringProgramAuditDaoFileImpl();
        FlooringProgramServiceLayer myService = new FlooringProgramServiceLayerImpl(myDao, myAuditDao);
        FlooringProgramController controller = new FlooringProgramController(myService, myView);
        controller.run();
        */
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringProgramController controller = ctx.getBean("controller", FlooringProgramController.class);
        controller.run();
    }
}

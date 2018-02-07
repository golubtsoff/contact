package edu.javacourse.contact.test;

import edu.javacourse.contact.config.GlobalConfig;
import edu.javacourse.contact.gui.ContactFrame;
import edu.javacourse.contact.gui.GuiResource;

/**
 * Класс для запуска тестовых вызовов
 */
public class ContactTest
{
    public static void main(String[] args) {
        try {
            if (args.length == 0){
                GlobalConfig.initGlobalConfig();
            } else if (args.length == 1){
                GlobalConfig.initGlobalConfig(args[0]);
            } else {
                return;
            }
            GuiResource.initComponentResources();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return;
        }
        ContactFrame cf = new ContactFrame();
        cf.setVisible(true);
    }
}

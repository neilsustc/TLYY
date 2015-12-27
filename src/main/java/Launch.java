package main.java;

import java.awt.Font;

import javax.swing.UIManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.ui.views.CustomerMgmtFrame;
import main.java.ui.views.MainFrame;

public class Launch
{
    public static void main(String[] args)
    {
        final Logger logger = LogManager.getLogger();
        logger.info("Application lanuched.");
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font defFont = new Font("cambria", Font.PLAIN, 14);
            UIManager.put("TextField.font", defFont);
            UIManager.put("TextArea.font", defFont);
            UIManager.put("PasswordField.font", defFont);
            UIManager.put("Label.font", defFont);
            UIManager.put("ComboBox.font", defFont);
            UIManager.put("Button.font", defFont);
            UIManager.put("List.font", defFont);
            UIManager.put("TitledBorder.font", defFont);
            // UIManager.put("Panel.border", new LineBorder(Color.black));
            // new LoginFrame();
//            new MainFrame("2333");
            // new ChangePassFrame();
            new CustomerMgmtFrame();
            logger.info("GUI initialized.");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

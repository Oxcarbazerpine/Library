package library;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login {
    public Login(){
    //  create dialog
    JTextField userName = new JTextField();
    JPasswordField password = new JPasswordField();
    Object[] message = {"用户名", userName, "密码", password};
    String title = "图书管理系统登录";
    Icon icon = null;
    Object[] options = {"登录", "退出"};
    Object initialSelectionValue = null;

    // JOptionPane oPane = new JOptionPane(message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon, options, initialSelectionValue);
    // JDialog dialog = oPane.createDialog(null, title);
    // dialog.setVisible(true);
    int value = JOptionPane.showOptionDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE, icon, options, initialSelectionValue);
    
    System.out.println(value);
    
    if(value == JOptionPane.OK_OPTION){
        String uN = userName.getText();
        String pwd = new String(password.getPassword());
        if(uN.equals("admin") && pwd.equals("123456")){
            new DbOp();
            new BookQuery();
        }
    }else if(value == JOptionPane.NO_OPTION){
        System.exit(0);
    }
}
    public static void main(String[] args){
        new Login();
    }
}
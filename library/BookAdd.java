package library;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
import javax.swing.*;

public class BookAdd extends JFrame implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    // draw background
    // String bgp = "bookbk.png";
    // Image img = tool.getImage(bgp);

    // public void paint(Graphics g){
    // g.drawImage(img, 0, 0, this);
    // }
    Container contentPane = getContentPane();
    // JPanel panel1 = new JPanel();
    // JPanel panel2 = new JPanel();
    Toolkit tool = getToolkit();
    JTextField[] booktxt = new JTextField[10];
    JTextField month = new JTextField(2);
    JTextField year = new JTextField(4);
    String[] lbName = { "ID", "书名", "页数", "作者", "翻译", "出版社", "出版时间", "价格", "库存",
            "分类" };
    JLabel[] booklb = new JLabel[10];
    JComboBox<String> bookType = new JComboBox<>();

    JButton saveBtn = new JButton("保存");
    JButton closeBtn = new JButton("关闭");



    public BookAdd() {

        setTitle("添加新书");
        setLayout(null); //new FlowLayout() or new GridLayout(row, col)
        int frameWidth = 500, frameHeight = 350; 
        setSize(frameWidth, frameHeight);
        setResizable(true);
        setForeground(Color.BLACK);
        

        // add elements to contentPane
        // define location
        int lx0 = 50;
        int lx = lx0, ly = 30;

        // define size
        int lbWidth = 50, lbHeight = 20;
        int fldWidth = 100, fldHeight = 20;
        int spaceH = 190, spaceV = 40; // horizental, vertical
        for (int i = 0; i < booklb.length; i++) {
            if (lx > lx0 + spaceH) {
                lx = lx0;
                ly = ly + spaceV;
            }
            booklb[i] = new JLabel(lbName[i]); //set label
            booklb[i].setBounds(lx, ly, lbWidth, lbHeight);  // add label
            booktxt[i] = new JTextField();  // set text field
            booktxt[i].setBounds(lx + lbWidth, ly, fldWidth, fldHeight); // add text field
            lx = lx + spaceH;
            add(booklb[i]); // swing or awt
            add(booktxt[i]);
        }

        booktxt[0].setEditable(false);
        booktxt[0].setText(getGivenId());
        booktxt[6].setVisible(false);
        booktxt[6].setText("1");
        year.setBounds(booklb[6].getLocation().x + lbWidth, booklb[6].getLocation().y, 50, booklb[6].getSize().height); 
        add(year);
        month.setBounds(year.getLocation().x + 60, year.getLocation().y, 30, year.getSize().height); 
        // month.setLocation(year.getLocation().x + 50, year.getLocation().y); 
        add(month);
        JLabel dateSep = new JLabel("-");
        dateSep.setBounds(month.getX()-10, month.getY(), 10, fldHeight);
        add(dateSep);

        booktxt[9].setText("1"); // to pass empty check when saving 
        booktxt[9].setVisible(false); // hide the text field for combo box

        String[] typeList = {"小说", "名著", "论述", "通识", "技术", "科学", "文学", "历史", "工具书", "其他"};
        for(int i = 0; i < typeList.length; i++){
            bookType.addItem(typeList[i]);
        }


        bookType.setBounds(booktxt[9].getBounds());  // alternate text field with comboBox
        add(bookType);

        saveBtn.setBounds(frameWidth/2 - 120, frameHeight - 100, 80, 25);
        closeBtn.setBounds(frameWidth/2 + 20, frameHeight - 100, 80, 25);
        saveBtn.addActionListener(this);
        closeBtn.addActionListener(this);
        add(saveBtn);
        add(closeBtn);

        // add(contentPane);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // database operation
                // DbOp.close();
                dispose();
            }
        });


        // show the window 
        // pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private static String getGivenId(){
        String returnString = "";
        String sql = "select * from book";

        try {

            // database operation
            ResultSet rs = DbOp.query(sql);

            int count = 0;
            while (rs.next()) {
                count++;
            }
            // get the number of id
            returnString = String.valueOf(count + 1);

            // fill the rest of digits with 0
            int len = returnString.length();
            int fixedLen = 5;
            for (int i = 0; i < fixedLen - len; i++) {
                returnString = "0" + returnString;

            }

            // attach the prefix "A"
            returnString = "A" + returnString;

        } catch (SQLException e) {
            System.out.println(e);
        }

        return returnString;

    }

    private void clearAndSetBookId() {
        for (int j = 0; j < booktxt.length; j++) {
            booktxt[j].setText("");
        }
        year.setText("");
        month.setText("");
        booktxt[0].setEditable(false);
        booktxt[0].setText(getGivenId());
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == saveBtn) {
            saveBtnActionPerformed();
        }
        if (obj == closeBtn) {
            // database operation
            // DbOp.close();
            dispose();
        }
    }

    public void saveBtnActionPerformed() {
        // input empty check
        // for(int i = 0; i < booktxt.length; i++){
        //     if(booktxt[i].getText().equals("")){
        //         JOptionPane.showMessageDialog(null, "请填写完整");
        //         return;
        //     }
        // }

        // extract input text
        String id = booktxt[0].getText();
        String title = booktxt[1].getText();
        int page = Integer.parseInt(booktxt[2].getText());
        String author = booktxt[3].getText();
        String translator = booktxt[4].getText();
        String publisher = booktxt[5].getText();
        String pubTime = year.getText() + "-" + month.getText() + "-01";
        int price = Integer.parseInt(booktxt[7].getText());
        int stock = Integer.parseInt(booktxt[8].getText());
        String type = bookType.getSelectedItem().toString();
 
        if (id.equals("")) {
            JOptionPane.showMessageDialog(null, "编号不能为空");
            return;
        }

        if (bookIdExist(id)) {
            JOptionPane.showMessageDialog(null, "图书编号已存在");
            booktxt[0].setEditable(true);
            return;
        }
        
        if (title.equals("")) {
            JOptionPane.showMessageDialog(null, "书名不能为空");
            return;
        }

        try {
            // SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM");
            // Date dateFmt = dateFmt.parse(pubTime);

            // int idInt = Integer.parseInt(idString);

            String sql = "insert into book(id, bookname, booktype, author, translator, publisher, publish_time, price, stock, page)";
            sql = sql + "values('" + id + "','" + title + "','" + type + "','" + author + "','" + translator + "','";
            sql = sql + publisher + "','" + pubTime + "'," + price + "," + stock + "," + page + ")";

            // database operation
            if (DbOp.update(sql) == 1) {
                JOptionPane.showMessageDialog(null, "添加图书成功！");
                clearAndSetBookId();
            }

        }
        // catch(ParseException e1){
        // JOptionPane.showMessageDialog(null, "时间格式错误（年-月）");
        // }
        catch (NullPointerException e2) {
            JOptionPane.showMessageDialog(null, "以下部分有格式错误：库存数量，价格，页数。请更改为数字");
        }
    }

    public boolean bookIdExist(String id) {
        boolean isExist = false;
        String sql = "select * from book where id = '" + id + "'";
        // database operation
        ResultSet rs = DbOp.query(sql);
        try {
            while (rs.next()) {
                isExist = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "读取数据错误");
        }

        return isExist;
    }

    public static void main(String[] args) {
        new DbOp();
        new BookAdd();
    }

}

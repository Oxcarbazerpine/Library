package library;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

// implements ActionListener 
public class BookQuery extends JFrame implements ActionListener{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JLabel[] label = new JLabel[3];
    JTextField[] kwdBox = new JTextField[3];
    JButton submitBtn, closeBtn, addBtn;
    JTable table;
    DefaultTableModel tModel = new DefaultTableModel();
    JScrollPane scroll;

    JPanel[] panel = new JPanel[3];
    JPanel btnPanel = new JPanel();
    JPanel panelUp = new JPanel();
    JPanel panelDown = new JPanel();
    JPanel panelMain = new JPanel();
    JMenuBar menuBar;
    int sqlCount;

    String[] dataColums =  { "id", "bookname", "booktype", "author", "translator", "publisher", "publish_time", "price", "stock", "page"};  
    String[]  columnNames = { "ID", "书名", "类型", "作者", "翻译", "出版社", "出版时间", "价格", "库存", "页数"};


    public BookQuery(){
        setTitle("图书查询");
        setSize(1000,500);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        JMenuItem choice = new JMenu("选项"); 
        JMenuItem addBook = new JMenuItem("新增书籍"); 
        addBook.setActionCommand("addBook");
        MenuActionListener menuActionListener = new MenuActionListener();
        addBook.addActionListener(menuActionListener);
        choice.add(addBook);
        menuBar.add(choice);

        setJMenuBar(menuBar);

        // Top part
        label[0] = new JLabel("书名：");
        label[1] = new JLabel("作者：");
        label[2] = new JLabel("出版时间：从");

        for(int i = 0; i < panel.length; i++){
            panel[i] = new JPanel();
            kwdBox[i] = new JTextField();
            kwdBox[i].setPreferredSize(new Dimension(60, 20));

            panel[i].add(label[i]);
            panel[i].add(kwdBox[i]);
        }


        panel[2].add(new JLabel("(格式示例：2001-01)"));
        submitBtn = new JButton("搜索");
        submitBtn.setActionCommand("submit");
        submitBtn.addActionListener(this);
        btnPanel.add(submitBtn);
        
        for(int i = 0; i < panel.length; i++){
            panelUp.add(panel[i]);
        }

        panelUp.add(btnPanel);

        add(panelUp,BorderLayout.PAGE_START);

        // set default table data, an empty table head
        tModel.setDataVector(new Object[0][0], columnNames);
        table = new JTable(tModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);  
        // add table to a scroll
        scroll = new JScrollPane(table);  
        scroll.setPreferredSize(new Dimension(800, 350));  
        panelMain.add(scroll);

        add(panelMain,BorderLayout.CENTER);
        
        // bottom part
        closeBtn = new JButton("关闭");
        addBtn = new JButton("新增书籍");

        closeBtn.setActionCommand("close");
        addBtn.setActionCommand("addBook");
        closeBtn.addActionListener(this);
        addBtn.addActionListener(this);

        panelDown.add(addBtn);
        panelDown.add(closeBtn);

        add(panelDown, BorderLayout.PAGE_END);

        // show window
        setVisible(true);
        
        // for window event like closing
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                DbOp.close();
                dispose();
            }
        });
    }

    private ResultSet search(String sql){
        ResultSet rs = DbOp.query(sql);

        return rs;
    }

    private int getSqlCount(ResultSet rs){
        int count = 0;

        try{
            // to count return entries

            rs.last();  // jump to the last entry
            count = rs.getRow();  // read the last entry row number
            rs.beforeFirst();
        }catch(SQLException e){
            System.out.println(e);
        }
        return count;
    }

    private Object[][] getTableData(ResultSet rs, int sqlCount){
        Object[][] obj = new Object[sqlCount][columnNames.length]; 
        int j = 0;
        try{
            while(rs.next()){
                for(int i = 0; i < columnNames.length; i++){
                    obj[j][i] = rs.getString(dataColums[i]);
                }
                j++;
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return obj;  
    }

    private String makeSql(){
        // input check
        Boolean isAllEmpty = kwdBox[0].getText().isBlank() && kwdBox[1].getText().isBlank() && kwdBox[2].getText().isBlank(); 
        if(isAllEmpty){
            JOptionPane.showMessageDialog(null,"请选择任意一项输入");
            return "";
        }
        String bookName = kwdBox[0].getText();
        String author = kwdBox[1].getText();
        String pubTime = kwdBox[2].getText();
        String sqlDate = "";
        if(! pubTime.isBlank()){
            pubTime += "-01";
            sqlDate = "AND ( DATEDIFF(publish_time,'" + pubTime + "') > 0 )";
        }

        String sql = "SELECT * FROM book ";

        sql += "WHERE bookname LIKE " + "'%" + bookName + "%'";
        sql += "AND author LIKE " + "'%" + author + "%'";
        sql += sqlDate;


        return sql;
    }

    private void addNewBook(){
        new BookAdd();
    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();

        if(command.equals("submit")){
            String sql = makeSql();
            if(sql.isEmpty()){
                return;
            }else{
                ResultSet rs = search(sql);
                tModel.setDataVector(getTableData(rs, getSqlCount(rs)), columnNames);
            }
        }

        if(command.equals("close")){
            DbOp.close();
            dispose();
        }

        if(command.equals("addBook")){        
            addNewBook();
        }
    }

    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            new DbOp();
            new BookQuery();
    }); 
    }

    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { 
            String command = e.getActionCommand();           
            if(command.equals("addBook")){        
                addNewBook();
                System.out.println("addBook");
            }
        }    
     }
  
  

}



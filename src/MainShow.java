import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainShow extends JPanel implements ActionListener {

    public String available = "3 3 2 0",available2;
    private int rCount = (available.length()+1)/2;//系统资源数量
    private static Vector<Vector<Object>> data = new Vector<Vector<Object>>(), data2;
    private JTable table;  //表格
    private MyTableModel model = new MyTableModel();
    private JButton safeButton, availableButton, addButton, delButton, requestButton, withdrawButton;
    public static List<PCB> pcbs = new ArrayList<>();
    private int selectRow = -1;//选中的进程

    public MainShow() {
        initData();
        initShow();
    }

    private void initData() {
        Vector<Object> p0 = new Vector<Object>();
        p0.add(new String("P0"));
        p0.add(new String("7 5 3 1"));
        p0.add(new String("0 1 0 1"));
        p0.add(new String("7 4 3 0"));

        Vector<Object> p1 = new Vector<Object>();
        p1.add(new String("P1"));
        p1.add(new String("3 2 2 1"));
        p1.add(new String("2 0 0 1"));
        p1.add(new String("1 2 2 0"));

        Vector<Object> p2 = new Vector<Object>();
        p2.add(new String("P2"));
        p2.add(new String("9 0 2 1"));
        p2.add(new String("3 0 2 1"));
        p2.add(new String("6 0 0 0"));

        Vector<Object> p3 = new Vector<Object>();
        p3.add(new String("P3"));
        p3.add(new String("2 2 2 2"));
        p3.add(new String("2 1 1 0"));
        p3.add(new String("0 1 1 0"));

        Vector<Object> p4 = new Vector<Object>();
        p4.add(new String("P4"));
        p4.add(new String("4 3 3 1"));
        p4.add(new String("0 0 2 1"));
        p4.add(new String("4 3 1 0"));

        Vector<Object> p5 = new Vector<Object>();
        p5.add(new String("P5"));
        p5.add(new String("5 5 2 2"));
        p5.add(new String("0 0 1 1"));
        p5.add(new String("5 5 1 1"));
        if (data.size() == 0) {
            data.add(p0);
            data.add(p1);
            data.add(p2);
            data.add(p3);
            data.add(p4);
            data.add(p5);
        }
    }

    private void initShow() {
        this.setSize(800,1000);
        Box baseBox = Box.createVerticalBox();//根盒子
        JPanel showPanel = new JPanel();
        showPanel.setSize(800,400);
        Box mainBox = Box.createVerticalBox();

        //-------------------容器内容------------------------------
        Box textTop = Box.createHorizontalBox();//标题盒子
        Box textButtons1 = Box.createHorizontalBox();//按钮盒子1
        Box textTable = Box.createHorizontalBox();//表格盒子
        Box textButtons2 = Box.createHorizontalBox();//按钮盒子2

        JLabel bannerLabel = new JLabel("资源分配表:");
        textTop.add(bannerLabel);


        JLabel jl = new JLabel("系统可用资源为  " + available);
        textButtons1.add(jl);
        textButtons1.add(Box.createHorizontalStrut(20));//创建间隔
        availableButton = new JButton("修改剩余资源");
        availableButton.addActionListener(this);
        availableButton.setContentAreaFilled(false);
        textButtons1.add(availableButton);
        textButtons1.add(Box.createHorizontalStrut(10));//创建间隔
        addButton = new JButton("添加进程");
        addButton.addActionListener(this);
        addButton.setContentAreaFilled(false);
        textButtons1.add(addButton);
        textButtons1.add(Box.createHorizontalStrut(10));//创建间隔
        delButton = new JButton("删除进程");
        delButton.addActionListener(this);
        delButton.setContentAreaFilled(false);
        textButtons1.add(delButton);

        //表格
        table = new JTable(model);                     //把模型对象作为参数构造表格对象，这样就可以用表格显示出数据
        SecurityCheckShow.setColumnColor(table);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);

        ListSelectionModel selectionModel = table.getSelectionModel();// 创建表格选择器
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 一次只能选择一个列表索引
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (selectionModel.getValueIsAdjusting()) {
                    if (table.getSelectedRow() < 0) {
                        selectRow = 0;
                        return;
                    }
                    selectRow = table.getSelectedRow();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);   //把表格控件放到滚动容器里，页面不够长显示可以滚动
        scroll.setPreferredSize(new Dimension(400, 200));
        textTable.add(scroll);

        requestButton = new JButton("分配资源");
        requestButton.addActionListener(this);
        requestButton.setContentAreaFilled(false);
        textButtons2.add(requestButton);
        textButtons2.add(Box.createHorizontalStrut(30));//创建间隔
        withdrawButton = new JButton("作业撤销");
        withdrawButton.addActionListener(this);
        withdrawButton.setContentAreaFilled(false);
        textButtons2.add(withdrawButton);
        textButtons2.add(Box.createHorizontalStrut(30));//创建间隔
        safeButton = new JButton("安全性检查");
        safeButton.addActionListener(this);
        safeButton.setContentAreaFilled(false);
        textButtons2.add(safeButton);
        //-------------------容器内容------------------------------

        mainBox.add(textTop);
        mainBox.add(Box.createVerticalStrut(15));                  //创建上下空间距离
        mainBox.add(textButtons1);
        mainBox.add(Box.createVerticalStrut(15));                  //创建上下空间距离
        mainBox.add(textTable);
        mainBox.add(Box.createVerticalStrut(15));                  //创建上下空间距离
        mainBox.add(textButtons2);

        showPanel.add(mainBox);
        baseBox.add(showPanel);

        this.add(baseBox, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //修改剩余资源
        if (e.getSource() == availableButton) {
            setAvailable();
            this.validate();
            this.removeAll();
            this.initShow();
            this.validate();
            this.repaint();
        }
        //添加进程
        else if (e.getSource() == addButton) {
            addPcb();
            this.validate();
            this.removeAll();
            this.initShow();
            this.validate();
            this.repaint();
        }
        //删除进程
        else if(e.getSource() == delButton){
            if (selectRow==-1) {
                JOptionPane.showMessageDialog(null,"未选择进程！","错误",JOptionPane.ERROR_MESSAGE);
            }
            else {
                data.remove(selectRow);
                this.validate();
                this.removeAll();
                this.initShow();
                this.validate();
                this.repaint();
            }
            selectRow = -1;
        }
        //安全性检查
        else if (e.getSource() == safeButton) {
            this.validate();
            this.removeAll();
            this.initShow();
            pcbs.clear();
            for (int i = 0; i < data.size(); i++) {
                pcbs.add(new PCB(data.get(i).get(0).toString(), data.get(i).get(1).toString(), data.get(i).get(3).toString(), data.get(i).get(2).toString(),false));
            }
            if(pcbs.get(0).getMax().length() != 2*rCount-1 || pcbs.get(0).getNeed().length() != 2*rCount-1 || pcbs.get(0).getAllocation().length() != 2*rCount-1) {
                JOptionPane.showMessageDialog(null, "进程资源与系统资源数量不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SecurityCheckShow securityCheckShow = new SecurityCheckShow(pcbs, available);
            this.add(securityCheckShow,BorderLayout.CENTER);
            this.validate();
            this.repaint();
        }
        //分配资源
        else if (e.getSource() == requestButton) {
            if (selectRow==-1) {
                JOptionPane.showMessageDialog(null,"未选择进程！","错误",JOptionPane.ERROR_MESSAGE);
            }
            else {
                String inputMsg = JOptionPane.showInputDialog(null, "对进程"+data.get(selectRow).get(0).toString()+"分配资源数：");
                if(inputMsg==null) {
                    return;
                }
                if (inputMsg.length() <= (2*rCount-2)) {
                    JOptionPane.showMessageDialog(null, "输入错误");
                    return;
                }
                if(data.get(0).get(1).toString().length() != 2*rCount-1 || data.get(0).get(2).toString().length()  != 2*rCount-1 || data.get(0).get(3).toString().length()  != 2*rCount-1) {
                    JOptionPane.showMessageDialog(null, "进程资源与系统资源数量不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int[] request = stringChange(inputMsg);

                int[] ints1 = stringChange(available);//当前系统剩余可用资源
                for(int i=0;i<request.length;i++) {
                    if (request[i] > ints1[i]) {
                        JOptionPane.showMessageDialog(null,"请求的资源大于该系统剩余可用资源！","分配失败",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                available2 = available;
                available = "";//当前系统剩余可用资源更新
                for(int i=0;i<request.length;i++) {
                    ints1[i] = ints1[i] - request[i];
                    if(i==request.length-1)
                        available += ints1[i];
                    else
                        available += ints1[i]+" ";
                }
                String need = data.get(selectRow).get(3).toString();//选中进程所需资源
                int[] ints2 = stringChange(need);
                for(int i=0;i<request.length;i++) {
                    if (request[i] > ints2[i]) {
                        JOptionPane.showMessageDialog(null,"请求的资源大于该进程所需资源！","分配失败",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                request(selectRow,request,ints2);

                this.validate();
                this.removeAll();
                this.initShow();
                pcbs.clear();
                for (int i = 0; i < data.size(); i++) {
                    pcbs.add(new PCB(data.get(i).get(0).toString(), data.get(i).get(1).toString(), data.get(i).get(3).toString(), data.get(i).get(2).toString(),false));
                }
                SecurityCheckShow securityCheckShow = new SecurityCheckShow(pcbs, available);
                this.add(securityCheckShow,BorderLayout.CENTER);
                this.validate();
                this.repaint();

                if(securityCheckShow.safe == false){
                    JOptionPane.showMessageDialog(null,"分配后系统处于不安全状态，将取消分配！","分配失败",JOptionPane.ERROR_MESSAGE);
                    available = available2;
                    data = data2;
                    this.validate();
                    this.removeAll();
                    this.initShow();
                    this.validate();
                    this.repaint();
                }
                else {
                    JOptionPane.showMessageDialog(null,"分配成功！","提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            selectRow = -1;
        }
        //作业撤销
        else if (e.getSource() == withdrawButton) {
            if (selectRow==-1) {
                JOptionPane.showMessageDialog(null,"未选择进程！","错误",JOptionPane.ERROR_MESSAGE);
            }
            else {
                release(selectRow);
                this.validate();
                this.removeAll();
                this.initShow();
                this.validate();
                this.repaint();
            }
            selectRow = -1;
        }
    }

    /**
     * 修改剩余资源
     */
    public void setAvailable() {
        String inputMsg = JOptionPane.showInputDialog(null, "输入可分配资源数：", available);
        if(inputMsg==null) {
            return;
        }
        rCount = (inputMsg.length()+1)/2;
        available = inputMsg;
    }

    /**
     * 添加进程
     */
    public void addPcb() {
        JDialog jDialog =new JDialog();
        jDialog.setTitle("添加进程");
        jDialog.setModal(true);// 是否阻止在显示的时候将内容输入其他窗口,只能操作此对话框
        jDialog.setSize(350, 280);// 对话框的大小
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);// 关闭后销毁对话框
        jDialog.setLocationRelativeTo(null);
        JPanel jp = new JPanel();
        jp.setLayout(null);

        JLabel[] jl={new JLabel("进程名字："),new JLabel("最大需求资源："),new JLabel("已分配资源："),new JLabel("需求资源：")};
        JTextField[] jt={new JTextField("P?"),new JTextField("以空格为分割"),new JTextField("以空格为分割"),new JTextField("以空格为分割")};
        for (int i = 0; i < jl.length; i++) {
            jl[i].setBounds(20, 20 + i * 40, 100, 30);
            jp.add(jl[i]);
        }
        for (int i = 0; i < jt.length; i++) {
            jt[i].setBounds(150, 20 + i * 40, 100, 30);
            jp.add(jt[i]);
        }

        JButton jbt1 = new JButton("确认");
        jbt1.setBounds(125, 190, 100, 40);
        jbt1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int c = JOptionPane.showConfirmDialog(null, "是否确定新增此进程", "验证操作", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) {
                    String pName = jt[0].getText();
                    String pMax = jt[1].getText();
                    String pAv = jt[2].getText();
                    String pNeed = jt[3].getText();
                    Vector<Object> p = new Vector<Object>();
                    p.add(new String(pName));
                    p.add(new String(pMax));
                    p.add(new String(pAv));
                    p.add(new String(pNeed));
                    data.add(p);
                    jDialog.dispose();
                }
            }
        });
        jp.add(jbt1);
        jDialog.add(jp);
        jDialog.setVisible(true);
    }

    /**
     * 分配资源
     */
    public void request(int selectRow, int[] request, int[] ints2) {
        String need2 = "";
        for (int i = 0; i < request.length; i++) {//选中进程所需资源更新
            ints2[i] = ints2[i] - request[i];
            if(i==request.length-1)
                need2 += ints2[i];
            else
                need2 += ints2[i]+" ";
        }

        String allocation = data.get(selectRow).get(2).toString();//选中进程已分配资源更新
        int[] ints3 = stringChange(allocation);
        String allocation2 = "";
        for(int i=0;i<request.length;i++) {
            ints3[i] = ints3[i] + request[i];
            if(i==request.length-1)
                allocation2 += ints3[i];
            else
                allocation2 += ints3[i]+" ";
        }

        Vector<Object> p = new Vector<Object>();
        p.add(data.get(selectRow).get(0).toString());
        p.add(data.get(selectRow).get(1).toString());
        p.add(allocation2);
        p.add(need2);

        data2 = new Vector<>();
        data2.addAll(data);
        data.removeAllElements();
        for (int i = 0; i< data2.size(); i++) {
            if(i==selectRow) {
                data.add(p);
                continue;
            }
            data.add(i, data2.get(i));
        }

    }

    /**
     * 撤销进程
     */
    private void release(int selectRow) {
        int c = JOptionPane.showConfirmDialog(null, "是否确定撤销此进程", "验证操作", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            int[] ints1 = stringChange(available);//当前系统剩余可用资源
            String allocation = data.get(selectRow).get(2).toString();//选中进程已分配资源
            int[] ints = stringChange(allocation);
            available="";
            for(int i=0;i<ints1.length;i++) {
                ints1[i] = ints1[i] + ints[i];
                if(i==ints1.length-1)
                    available += ints1[i];
                else
                    available += ints1[i]+" ";
            }

            data.remove(selectRow);
        }
    }

    /**
     * 字符串转整形数组
     */
    public int[] stringChange(String s){
        String[] s1 = s.split(" ");
        int[] ints = new int[s1.length];
        for(int i=0;i<s1.length;i++)
            ints[i]= Integer.parseInt(s1[i]);
        return ints;
    }

    static class MyTableModel extends AbstractTableModel  //模型内部类
    {
        Vector<String> title = new Vector<String>();// 列名

        /**
         * 构造方法，初始化二维动态数组data对应的数据
         */
        public MyTableModel(){
            title.add("进程名");
            title.add("最大需求");
            title.add("分配");
            title.add("需求");
        }

        /**
         * 得到列名
         */
        @Override
        public String getColumnName(int column) {
            return title.elementAt(column);
        }
        /**
         * 重写方法，得到表格列数
         */
        @Override
        public int getColumnCount() {
            return title.size();
        }
        /**
         * 得到表格行数
         */
        @Override
        public int getRowCount() {
            return data.size();
        }
        /**
         * 得到数据所对应对象
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //return data[rowIndex][columnIndex];
            return data.elementAt(rowIndex).elementAt(columnIndex);
        }
        /**
         * 得到指定列的数据类型
         */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return data.elementAt(0).elementAt(columnIndex).getClass();
        }
        /**
         * 指定设置数据单元是否可编辑.
         */
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}

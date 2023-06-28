import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SecurityCheckShow extends JPanel{
    private Vector<Vector<Object>> data = new Vector<Vector<Object>>();
    private JTable table;  //表格
    private SecurityCheckShow.MyTableModel model = new SecurityCheckShow.MyTableModel();  //声明模型对象
    private List<PCB> process = new ArrayList<>();
    private String available;
    private int rCount;//系统资源数量
    private int count;//进程总数
    public boolean safe;//记录是否为安全状态

    public SecurityCheckShow(List<PCB> pcbs, String available) {
        this.process = pcbs;
        this.available = available;
        this.count = pcbs.size();
        rCount = (available.length()+1)/2;
        Test(pcbs, available);
        initView();
    }

    /**
     * 测试数据的输入
     */
    private void Test(List<PCB> process, String available) {
        for (int i = 0; i < process.size(); i++) {
            if (process.get(i).isFinish() == false) {
                if (compare(available, process.get(i).getNeed())) {//当前系统剩余资源能否满足当前进程的需求
                    Vector<Object> v = new Vector<Object>();
                    v.add(process.get(i).getName());
                    v.add(available);//工作向量
                    v.add(process.get(i).getNeed());
                    v.add(process.get(i).getAllocation());
                    available = add(available, process.get(i).getAllocation());//工作加分配，进程完成后释放资源
                    v.add(available);
                    process.get(i).setFinish(true);
                    v.add(process.get(i).isFinish());

                    data.add(v);
                    process.remove(i);
                    Test(process, available);
                }
            }
        }
    }

    /**
     * 初始化窗口
     */
    private void initView() {
        Box baseBox = Box.createVerticalBox();     //根盒子
        JPanel showPanel = new JPanel();
        showPanel.setSize(800,400);
        Box mainBox = Box.createVerticalBox();

        //-------------------容器内容------------------------------
        Box textTop = Box.createHorizontalBox();//标题盒子
        Box textTop2 = Box.createHorizontalBox();//标题盒子2
        Box textTop3 = Box.createHorizontalBox();//标题盒子3
        Box textTop4 = Box.createHorizontalBox();//标题盒子4
        Box textTable = Box.createHorizontalBox();//表格盒子

        JLabel bannerLabel = new JLabel("安全序列表:");
        textTop.add(bannerLabel);

        JLabel bannerLabel2 = new JLabel("系统可用资源为  " + available);
        textTop2.add(bannerLabel2);

        String s = " ";
        int i;
        for (i = 0; i < data.size(); i++) {
            s += data.get(i).get(0).toString();
            if (i < 10) {
                s += " ";
            }
        }
        s += " ";
        String textMessage,textMessage2;
        if(i==0) {
            textMessage = "系统此时不能满足任一进程的需求";
            textMessage2 = "系统处于不安全状态！";
            safe = false;
        }
        else if (i < count) {
            textMessage = "在满足进程：" + s + "后，系统剩余资源：" + data.get(i-1).get(4) + " 不能满足其余任一进程的需求";
            textMessage2 = "系统处于不安全状态！";
            safe = false;
        }
        else{
            textMessage = "系统此时存在一个安全序列:" + s;
            textMessage2 = "系统处于安全状态";
            safe = true;
        }
        JLabel bannerLabel3 = new JLabel(textMessage);
        textTop3.add(bannerLabel3);
        JLabel bannerLabel4 = new JLabel(textMessage2);
        textTop4.add(bannerLabel4);

        //表格
        table = new JTable(model);                     //把模型对象作为参数构造表格对象，这样就可以用表格显示出数据
        setColumnColor(table);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);
        JScrollPane scroll = new JScrollPane(table);   //把表格控件放到滚动容器里，页面不够长显示可以滚动
        scroll.setPreferredSize(new Dimension(720, 200));
        textTable.add(scroll);
        //-------------------容器内容------------------------------

        mainBox.add(textTop);
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(textTop2);
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(textTop3);
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(textTop4);
        mainBox.add(Box.createVerticalStrut(8));
        mainBox.add(textTable);
        showPanel.add(mainBox);
        baseBox.add(showPanel);

        this.add(baseBox, BorderLayout.CENTER);
    }

    private boolean compare(String a, String b) {
        String a_temp[] = a.split(" ");
        String b_temp[] = b.split(" ");
        for (int i = 0; i < rCount; i++) {
            if (Integer.parseInt(a_temp[i]) < Integer.parseInt(b_temp[i])) {
                return false;
            }
        }
        return true;
    }

    private String add(String a, String b) {
        String a_temp[] = a.split(" ");
        String b_temp[] = b.split(" ");
        String c = new String();
        for (int i = 0; i < rCount; i++) {
            int temp = Integer.parseInt(a_temp[i]) + Integer.parseInt(b_temp[i]);
            c += Integer.toString(temp);
            if (i < rCount-1) {
                c += " ";
            }
        }
        return c;
    }

    class MyTableModel extends AbstractTableModel  //模型内部类
    {
        Vector<String> title = new Vector<String>();// 列名

        /**
         * 构造方法，初始化二维动态数组data对应的数据
         */
        public MyTableModel() {
            title.add("进程名");
            title.add("工作向量");
            title.add("需求");
            title.add("分配");
            title.add("工作向量+已分配");
            title.add("完成");
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

    public static void setColumnColor(JTable table) {
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if (row % 2 == 0)
                        setBackground(Color.WHITE);//设置奇数行底色
                    else if (row % 2 == 1)
                        setBackground(new Color(220, 230, 241));//设置偶数行底色
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            };
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
            tcr.setHorizontalAlignment(JLabel.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


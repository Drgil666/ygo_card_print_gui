package view;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Pictures;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import util.DigimonSeleniumUtil;
import util.PdfUtil;
import util.YuGiOhSeleniumUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Gilbert
 * @date 2022/5/17 20:57
 */
public class Main {
    public static final String SUFFIX_PNG = ".png";
    public static final String FILE_PATH = System.getProperty("user.dir");
    public static final String CARD_PATH = FILE_PATH + "/card";
    public static final Integer YGO_WIDTH = 223;
    public static final Integer YGO_HEIGHT = 324;
    public static final Integer DIGIMON_WIDTH = 238;
    public static final Integer DIGIMON_HEIGHT = 335;
    public static final String TEMPLATE_PATH = FILE_PATH + "/template.docx";
    public static final String EXPORT_DOC_PATH = "export.docx";
    public static final String EXPORT_PDF_PATH = "export.pdf";
    private static final long YGO_LEFT_MARGIN = 568L;
    private static final long YGO_RIGHT_MARGIN = 568L;
    private static final long YGO_TOP_MARGIN = 228L;
    private static final long YGO_BOTTOM_MARGIN = 228L;
    private static final long DIGIMON_LEFT_MARGIN = 300L;
    private static final long DIGIMON_RIGHT_MARGIN = 300L;
    private static final long DIGIMON_TOP_MARGIN = 50L;
    private static final long DIGIMON_BOTTOM_MARGIN = 50L;
    public static JFrame frame = new JFrame("红龙印卡机 By-DrGilbert");
    public static JTextArea textArea1 = new JTextArea("这是日志打印窗口", 27, 15);
    public static JTextField textArea2 = new JTextField("请在这里输入dtcg的json文本");

    public static void main(String[] args) {
        //设置界面可见：
        frame.setSize(800, 500);
        frame.setBackground(Color.white);
        frame.setLayout(null);
        frame.setLocation(750, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 创建一个默认的文件选取器
        JButton openBtn1 = new JButton("上传ydk文件");
        openBtn1.setBounds(650, 150, 120, 60);
        openBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileOpen(frame);   // 打开文件对话框，创建自己的类，第一个参数是父窗口，第二个是显示文本框
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        JButton openBtn2 = new JButton("下载dtcg");
        openBtn2.setBounds(600, 350, 180, 60);
        openBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(textArea2.getText());
                String card = textArea2.getText();
                card = card.replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .replace(" ", "");
                List<String> cardList = Arrays.asList(card.split(","));
                cardList = cardList.subList(1, cardList.size());
                try {
                    DigimonSeleniumUtil.pre();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                List<String> imageList = new ArrayList<>();
                for (String cardCode : cardList) {
                    String fileName = cardCode + ".jpg";
                    File file = new File(DigimonSeleniumUtil.downloadPath, fileName);
                    if (!file.exists()) {
                        try {
                            String cardUrl = DigimonSeleniumUtil.getImageByCardName(cardCode);
                            DigimonSeleniumUtil.downloadImage(cardUrl, fileName);
                        } catch (InterruptedException | IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    imageList.add(fileName);
                }
                System.out.println("生成DOCX中...");
                try {
                    Main.createDigimonTemplate(imageList.size());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                XWPFTemplate template = Main.createDigimonExportByLocal(imageList);
                OutputStream stream = null;
                try {
                    stream = new FileOutputStream(new File(FILE_PATH, EXPORT_DOC_PATH));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                try {
                    template.writeAndClose(stream);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("转换成PDF中...");
                try {
                    PdfUtil.convertToPdf();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("---------------------");
            }
        });
        frame.add(openBtn1);
        frame.add(openBtn2);
        textArea1.setEditable(false);
        textArea1.setBounds(60, 30, 300, 400);
        textArea1.setVisible(true);
        textArea2.setEditable(true);
        textArea2.setBounds(400, 30, 200, 300);
        textArea2.setVisible(true);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setViewportView(textArea1);
        frame.add(scrollPane);
        frame.add(textArea1);
        frame.add(textArea2);
        frame.setVisible(true);
//        addLine ("获取目录：" + System.getProperty ("user.dir"));
    }

    private static void fileOpen(Component parent) throws IOException, InterruptedException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("ydk files(*.ydk)", "ydk"));
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);    // 正真打开选择对话框 parent就是父窗口
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            /*返回值:
             *     JFileChooser.CANCEL_OPTION: 点击了取消或关闭
             *     JFileChooser.APPROVE_OPTION: 点击了确认或保存
             *     JFileChooser.ERROR_OPTION: 出现错误*/
            // 获得被选择文件
            File file = fileChooser.getSelectedFile();
            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            System.out.println(file.getAbsolutePath());
            generate(file.getAbsolutePath());
        }
    }

    public static void addLine(String text) {
        textArea1.append("\n" + text);
    }

    public static void addDialog(String title, String text) {
        final JDialog dialog = new JDialog(frame, title, true);
        //弹出的对话框
        dialog.setBounds(300, 300, 500, 150);
        //设置弹出对话框的位置和大小
        dialog.setLayout(null);
        JButton button = new JButton("确定");
        button.setBounds(220, 60, 60, 30);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(button);
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBounds(10, 10, 500, 20);
        dialog.add(label);
        dialog.setVisible(true);
    }

    public static void createYgoTemplate(Integer size) throws IOException {
        XWPFDocument document = new XWPFDocument();
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(YGO_LEFT_MARGIN);
        pageMar.setRight(YGO_RIGHT_MARGIN);
        pageMar.setTop(YGO_TOP_MARGIN);
        pageMar.setBottom(YGO_BOTTOM_MARGIN);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        // 设置边距
        for (int i = 1; i <= Math.ceil(size / 3.0); i++) {
            // 设置左对齐
            XWPFRun run = paragraph.createRun();
            //创建段落文本
            String text = "{{@image" + (3 * (i - 1) + 1) + "}}  {{@image" + (3 * (i - 1) + 2) + "}}  {{@image" + (3 * (i - 1) + 3) + "}}";
            run.setText(text);
            if (i != Math.ceil(size / 3.0)) {
                run.addBreak(BreakType.TEXT_WRAPPING);
            }
            if (i % 3 != 0) {
                run.addBreak(BreakType.TEXT_WRAPPING);
            }
        }
        FileOutputStream out = new FileOutputStream(new File(TEMPLATE_PATH));
        //生成文件
        document.write(out);
        out.close();
    }

    public static void createDigimonTemplate(Integer size) throws IOException {
        XWPFDocument document = new XWPFDocument();
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(DIGIMON_LEFT_MARGIN);
        pageMar.setRight(DIGIMON_RIGHT_MARGIN);
        pageMar.setTop(DIGIMON_TOP_MARGIN);
        pageMar.setBottom(DIGIMON_BOTTOM_MARGIN);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        // 设置边距
        for (int i = 1; i <= Math.ceil(size / 3.0); i++) {
            // 设置左对齐
            XWPFRun run = paragraph.createRun();
            //创建段落文本
            String text = "{{@image" + (3 * (i - 1) + 1) + "}} {{@image" + (3 * (i - 1) + 2) + "}} {{@image" + (3 * (i - 1) + 3) + "}}";
            run.setText(text);
            if (i != Math.ceil(size / 3.0)) {
                run.addBreak(BreakType.TEXT_WRAPPING);
            }
            if (i % 3 != 0) {
                run.addBreak(BreakType.TEXT_WRAPPING);
            }
        }
        FileOutputStream out = new FileOutputStream(new File(TEMPLATE_PATH));
        //生成文件
        document.write(out);
        out.close();
    }

    private static XWPFTemplate createYgoExportByLocal(final List<String> imageList) {
        XWPFTemplate template = XWPFTemplate.compile(TEMPLATE_PATH).render(new HashMap<String, Object>(10) {{
            for (int i = 0; i < imageList.size(); i++) {
                put("image" + (i + 1), Pictures.ofLocal(CARD_PATH + "/" + imageList.get(i)).size(YGO_WIDTH, YGO_HEIGHT).create());
            }
        }});
        return template;
    }

    public static XWPFTemplate createDigimonExportByLocal(final List<String> imageList) {
        XWPFTemplate template = XWPFTemplate.compile(TEMPLATE_PATH).render(new HashMap<String, Object>(10) {{
            for (int i = 0; i < imageList.size(); i++) {
                put("image" + (i + 1), Pictures.ofLocal(CARD_PATH + "/" + imageList.get(i)).size(DIGIMON_WIDTH, DIGIMON_HEIGHT).create());
            }
        }});
        return template;
    }

    private static void generate(String filepath) throws InterruptedException, IOException {
        addLine("等待初始化...");
        File file = new File(FILE_PATH, "card");
        if (!file.exists()) {
            file.mkdir();
        }
        YuGiOhSeleniumUtil.pre();
        addLine("初始化完成，开始执行...");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
        String lineTxt;
        List<String> imageList = new ArrayList<String>();
        while ((lineTxt = bufferedReader.readLine()) != null) {
//            addLine (lineTxt);
            if (org.apache.commons.lang3.StringUtils.isNumeric(lineTxt)) {
                Integer cardCode = Integer.parseInt(lineTxt);
                String fileName = cardCode + SUFFIX_PNG;
                if (!new File(CARD_PATH, fileName).exists()) {
                    //如果文件未被下载则下载
                    String cardName = YuGiOhSeleniumUtil.getImageByCardCode(cardCode.toString());
                    file = new File(CARD_PATH, cardName + SUFFIX_PNG);
                    file.renameTo(new File(CARD_PATH, fileName));
                }
                imageList.add(fileName);
            }
        }
        addLine("生成DOCX中...");
        createYgoTemplate(imageList.size());
        XWPFTemplate template = createYgoExportByLocal(imageList);
        OutputStream stream = new FileOutputStream(new File(FILE_PATH, EXPORT_DOC_PATH));
        template.writeAndClose(stream);
        stream.close();
        addLine("转换成PDF中...");
        PdfUtil.convertToPdf();
        addLine("---------------------");
    }
}

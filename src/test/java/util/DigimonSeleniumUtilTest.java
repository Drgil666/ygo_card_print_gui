package util;

import com.deepoove.poi.XWPFTemplate;
import view.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static view.Main.EXPORT_DOC_PATH;
import static view.Main.FILE_PATH;

/**
 * @author DrGilbert
 * @date 2022/6/8 13:29
 */
public class DigimonSeleniumUtilTest {

    @org.junit.Test
    public void pre() throws InterruptedException {

    }

    @org.junit.Test
    public void getImageByCardName() throws InterruptedException, IOException {
        DigimonSeleniumUtil.pre();
        List<String> cardList = new ArrayList<>();
        cardList.add("BT10-060");
        cardList.add("BT10-057");
        cardList.add("BT10-063");
        List<String> imageList = new ArrayList<>();
        for (String cardCode : cardList) {
            String cardUrl=DigimonSeleniumUtil.getImageByCardName(cardCode);
            imageList.add(cardUrl);
            imageList.add(cardUrl);
            imageList.add(cardUrl);
        }
        System.out.println("生成DOCX中...");
        Main.createYgoTemplate(imageList.size());
        XWPFTemplate template = Main.createDigimonExportByLocal(imageList);
        OutputStream stream = new FileOutputStream(new File(FILE_PATH, EXPORT_DOC_PATH));
        template.writeAndClose(stream);
        stream.close();
        System.out.println("转换成PDF中...");
        PdfUtil.convertToPdf();
        System.out.println("---------------------");
    }

    @org.junit.Test
    public void after() {
    }
}
package util;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import view.Main;

import java.io.*;
/**
 * @author Gilbert
 * @date 2022/4/25 12:53
 */
public class PdfUtil {
    private static final String FILE_PATH = Main.FILE_PATH;
    private static final String EXPORT_NAME = "export.docx";
    private static final String EXPORT_PDF_NAME = "export.pdf";

    public static void convertToPdf() throws IOException {
        File file1 = new File(FILE_PATH, EXPORT_NAME);
        File file2 = new File(FILE_PATH, EXPORT_PDF_NAME);
        InputStream docxInputStream = new FileInputStream(file1);
        OutputStream outputStream = new FileOutputStream(file2);
        IConverter converter = LocalConverter.builder().build();
        converter.convert(docxInputStream)
                .as(com.documents4j.api.DocumentType.DOCX)
                .to(outputStream)
                .as(com.documents4j.api.DocumentType.PDF)
                .execute();
        outputStream.close();
        Main.addLine ("转换完成!");
        Main.addDialog ("通知","转换已成功!");
    }
}

package org.example;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
public class Main {
        // 저장될 파일 위치
        private final String DIR = "c:\\test\\";
        // 파일 확장자
        private final String ext = ".png";
        // QR CODE에 덮혀질 이미지
        private final String LOGO = "C:\\Users\\JangBeomJun\\Downloads\\Group 1 (2).jpg";
        // QR CODE 내용 or URL
        private final String CONTENT = "2313 jang";
        // 가로
        private final int WIDTH = 300;
        // 세로
        private final int HEIGHT = 300;

        public void generate() {

            Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            try {
                initDirectory(DIR);

                bitMatrix = writer.encode(CONTENT, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
                BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());
                BufferedImage overly = getOverly(LOGO);

                int deltaHeight = qrImage.getHeight() - overly.getHeight();
                int deltaWidth = qrImage.getWidth() - overly.getWidth();

                BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) combined.getGraphics();

                g.drawImage(qrImage, 0, 0, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                // QR코드 이미지의 정중앙 위치에 덮음
                g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);


                ImageIO.write(combined, "png", os);

                Files.copy( new ByteArrayInputStream(os.toByteArray()), Paths.get(DIR + "stone" +ext), StandardCopyOption.REPLACE_EXISTING);

            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private BufferedImage getOverly(String LOGO) throws IOException {
            File url = new File(LOGO);
            return ImageIO.read(url);
        }

        private void initDirectory(String DIR) throws IOException {
            Files.createDirectories(Paths.get(DIR));
        }

        private MatrixToImageConfig getMatrixConfig() {
            return new MatrixToImageConfig(Main.Colors.BLACK.getArgb(), Main.Colors.WHITE.getArgb());
        }

        public enum Colors {
            BLUE(0xFF40BAD0),
            RED(0xFFE91C43),
            PURPLE(0xFF8A4F9E),
            ORANGE(0xFFF4B13D),
            WHITE(0xFFFFFFFF),
            BLACK(0xFF000000);
            private final int argb;
            Colors(final int argb){
                this.argb = argb;
            }
            public int getArgb(){
                return argb;
            }
        }

        public static void main(String[] args) throws WriterException, IOException {
            Main main = new Main();
            main.generate();
        }

}



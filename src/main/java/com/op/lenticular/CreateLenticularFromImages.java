package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

public class CreateLenticularFromImages extends Base {

    private String dir = host + "slit/sphere2/";
    private String ipImageName = "";
    private String opImageName = "sphere2";
    private String ext = ".png";
    private BufferedImage opImage;
    private Graphics2D opG;
    private int w; //4724
    private int h; //4724

    private double dpi = 600;
    private double ppf = 1;
    private int num = 15;
    private double printWmm = 210;
    private double printHmm = 297;
    private double in2mm = 25.4;

    private String fName = "SedgwickAve-Regular.ttf";
    private String[] text = {"REYKJAVIK", "9th to 14th July, 2017"};
    private float fontScale = 200;
    private Color fontCol = Color.WHITE;
    private Font font;
    private boolean addText = false;

    public static void main(String[] args) {
        CreateLenticularFromImages test = new CreateLenticularFromImages();
        try {
            test.createImage();
            // test.createColors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createImage() throws Exception {
        init();
        for (int f = num; f >= 1; f--) {
            drawImage(f);
        }
        addAlignment();
        //addPrintBorder();
        saveImage();
        System.out.println("Finished");
    }

    private void init() throws IOException, FontFormatException {
        File inputFile = new File(dir + "0001" + ext);
        BufferedImage ipImage = ImageIO.read(inputFile);
        w = ipImage.getWidth();
        h = ipImage.getHeight();

        opImage = createAlphaBufferedImage(w, h);
        opG = (Graphics2D) opImage.getGraphics();
        opG.setColor(Color.WHITE);
        opG.fillRect(0, 0, w, h);

        if (addText) {
            InputStream is = new BufferedInputStream(new FileInputStream(dir + fName));
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            float fontH = fontScale;
            font = font.deriveFont(Font.BOLD, fontH);
        }
    }

    private void drawImage(double frame) throws IOException, FontFormatException {
        String padded = Integer.toString((int)frame+10000).substring(1);

        File inputFile = new File(dir + ipImageName + padded + ext);
        System.out.println(inputFile.getName());
        BufferedImage ipImage = ImageIO.read(inputFile);
        addText(ipImage, (int) frame);

        double n = (double) num;
        for (double y = ((frame - 1) * ppf); y < h - ppf; y = y + (n * ppf)) {
            BufferedImage subImage = ipImage.getSubimage(0, (int) y, w, (int) ppf);
            opG.drawImage(subImage, 0, (int) y, null);
        }
    }

    private void addText(BufferedImage ipImage, int frame) throws IOException, FontFormatException {
        if (!addText) {
            return;
        }
        Graphics2D ipG = (Graphics2D) ipImage.getGraphics();
        ipG.setFont(font);
        ipG.setColor(fontCol);
        ipG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        FontRenderContext frc = ipG.getFontRenderContext();
        int textInd = frame / ((num + 1) / 2);
        String s = text[textInd];
        GlyphVector gv = font.createGlyphVector(frc, s);
        Rectangle2D rect = font.getStringBounds(s, ipG.getFontRenderContext());
        Shape glyph = gv.getOutline();
        AffineTransform tr = AffineTransform.getTranslateInstance((w - rect.getWidth()) / 2.0, h - rect.getHeight() * 0.75);
        AffineTransform at = new AffineTransform();
        at.concatenate(tr);
        Shape transformedGlyph = at.createTransformedShape(glyph);
        ipG.fill(transformedGlyph);
    }

    private void addAlignment() {
        opG.setColor(Color.BLACK);
        int yOff = 4;
        int xW = w / 25;
        int th = (int) (dpi / 40.0);
        for (int y = yOff; y < h; y = y + th) {
            opG.drawLine(0, y, xW, y);
            opG.drawLine(w - xW, y, w, y);
        }

    }

    private void addPrintBorder() {
        int pw = (int)(printWmm* (dpi/ in2mm));
        int ph = (int)(printHmm* (dpi/ in2mm));
        int xOff = (pw - w)/2;
        int yOff = (ph - h)/2;
        BufferedImage printBI = createAlphaBufferedImage(pw, ph);
        printBI.getGraphics().setColor(Color.WHITE);
        printBI.getGraphics().fillRect(0, 0, pw, ph);
        printBI.getGraphics().drawImage(opImage, xOff, yOff, null);

        opImage = printBI;
    }

    private void saveImage() throws Exception {
        File fFile1 = new File(dir + opImageName + "_OUT.png");
        savePNGFile(opImage, fFile1, dpi);
        opG.dispose();
        System.out.println("Image saved : " + fFile1.getPath());
    }

}

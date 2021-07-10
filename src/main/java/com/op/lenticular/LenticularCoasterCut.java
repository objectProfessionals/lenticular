package com.op.lenticular;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LenticularCoasterCut extends Base {

    String dir = host + "vert/";
    String fileName = "coaster";
    double dpi = 600;

    int w = 0;
    int h = 0;
    String inputType = ".jpg";
    String outputType = ".png";
    String finishedFileSuffix = outputType;
    String src = "";

    BufferedImage opImage;
    BufferedImage ipImage;
    double i2mm = 25.4;

    /**
     * @param args
     */
    public static void main(String[] args) {
        createLenticular();
    }

    public static void createLenticular() {
        try {
            createImageFile();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void createImageFile() throws IOException, Exception {
        LenticularCoasterCut mr = new LenticularCoasterCut();
        mr.createImageFiles();
    }

    public void createImageFiles() throws Exception {
        createOutputImage();
    }

    private void createOutputImage() throws Exception {
        src = dir;
        w = (int) ((210.0 / i2mm) * dpi);
        h = (int) ((297.0 / i2mm) * dpi);

        opImage = createBufferedImage(w, h);
        Graphics2D opG = (Graphics2D) opImage.getGraphics();
        opG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        opG.setColor(Color.WHITE);
        opG.fillRect(0, 0, w, h);
        opG.setColor(Color.GREEN);

        double sqq = 95.0;
        double sepp = 2.0;
        double radd = 5.0;

        int sq = (int) ((sqq / i2mm) * dpi);
        int rad = (int) ((radd / i2mm) * dpi);
        int sep = (int) ((sepp / i2mm) * dpi);
        int yOff = (int) ((h - (3.0 * sq)) / 2.0);
        int xOff = (int) ((w - (2.0 * sq)) / 2.0);
        int ySep = 0;
        for (int j = 0; j < 3; j++) {
            int xSep = 0;
            for (int i = 0; i < 2; i++) {
                int x = xOff + i * sq + (xSep);
                int y = yOff + j * sq + (ySep);
                addBox(opG, x, y, sq, rad);

                xSep = xSep + sep;
            }
            ySep = ySep + sep;
        }

        File fFile1 = new File(src + fileName + finishedFileSuffix);
        savePNGFile(opImage, fFile1, dpi);
        opG.dispose();
        System.out.println("PNG image created : " + src);
    }

    private void addBox(Graphics2D opG, int x, int y, int sq, int rad) {
        opG.drawRoundRect(x, y, sq, sq, rad * 2, rad * 2);
    }

}

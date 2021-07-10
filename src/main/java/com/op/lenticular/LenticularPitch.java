package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LenticularPitch extends Base {

    String dir = host + "horiz/";
    String fileName = "scanHoriz600";
    double dpi = 600;
    double lpi = 40;

    int w = 0;
    int h = 0;
    String inputType = ".png";
    String outputType = ".png";
    String finishedFileSuffix = "PITCH" + outputType;
    String src = "";

    BufferedImage opImage;
    BufferedImage ipImage;

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
        LenticularPitch mr = new LenticularPitch();
        mr.createImageFiles();
    }

    public void createImageFiles() throws Exception {
        createOutputImage();
    }

    private void createOutputImage() throws Exception {
        src = dir;
        String flName = src + fileName + inputType;
        File inputFile = new File(flName);
        ipImage = ImageIO.read(inputFile);
        w = ipImage.getWidth();
        h = ipImage.getHeight();

        opImage = createAlphaBufferedImage(w, h);
        Graphics2D opg = (Graphics2D) opImage.getGraphics();
        opg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        // opg.drawImage(ipImage, null, null);

        addAlignment(opg);

        File fFile1 = new File(src + fileName + finishedFileSuffix);
        savePNGFile(opImage, fFile1, dpi);
        opg.dispose();
        System.out.println("PNG image created : " + src);
    }

    private void addAlignment(Graphics2D opG) {
        opG.setColor(Color.BLACK);
        int xOff = 4;
        int th = (int) (dpi / 40.0);
        for (int x = xOff; x < w; x = x + th) {
            opG.drawRect(x, 0, 1, h);
        }

    }

}

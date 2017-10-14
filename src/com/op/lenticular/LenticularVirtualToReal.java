package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

public class LenticularVirtualToReal extends Base {

    String type = "real";
    String file = "minionsA43";
    String dir = host + "vert/" + type + "/";
    double dpi = 600;
    double frSc = 1;

    int w = 0;
    int h = 0;
    String inputType = ".png";
    String outputType = ".png";
    String finishedFileSuffix = "OUT" + outputType;
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
        LenticularVirtualToReal mr = new LenticularVirtualToReal();
        mr.createImageFiles();
    }

    public void createImageFiles() throws Exception {
        createOutputImage();
    }

    private void createOutputImage() throws Exception {
        src = dir;
        String flName = src + file + inputType;
        File inputFile = new File(flName);
        System.out.println("Loading image..." + inputFile);
        ipImage = ImageIO.read(inputFile);
        System.out.println("Loaded.");
        w = ipImage.getWidth();
        h = ipImage.getHeight();

        opImage = createAlphaBufferedImage(w, h);
        Graphics2D opg = (Graphics2D) opImage.getGraphics();
        opg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        drawStrips(opg);

        File fFile1 = new File(src + file + finishedFileSuffix);
        savePNGFile(opImage, fFile1, dpi * frSc);
        opg.dispose();
        System.out.println("PNG image created : " + src);
    }

    private void drawStrips(Graphics2D opG) {
        try {
            opG.setColor(Color.BLACK);
            int th = (int) (dpi * frSc / 40.0);
            int xx = 0;
            int tt = th / 2;
            for (int x = 0; x + th < w; x = x + th) {
                AffineTransform at = new AffineTransform();
                BufferedImage sub = ipImage.getSubimage(x, 0, th, h);

                // rotate(th, x, at);
                mirror(th, x, at);

                opG.drawImage(sub, at, null);

                int fr = 38;
                opG.fillRect(tt + x, 0, (int) frSc, h / fr);
                opG.fillRect(tt + x, (fr - 1) * h / fr, (int) frSc, h / fr);
                xx = x;
            }

            boolean brackets = false;
            if (brackets) {
                int lensesOff = 10;
                opG.drawRect(th * lensesOff + tt, 0, 1, h);
                opG.drawRect(-th * lensesOff + tt + xx, 0, 1, h);
            }
        } catch (RasterFormatException rfe) {
            System.out.println(rfe.getMessage());
        }
    }

    private void rotate(int th, int x, AffineTransform at) {
        AffineTransform rot = AffineTransform.getRotateInstance(Math.PI, 0, 0);
        AffineTransform tr = AffineTransform.getTranslateInstance(x + th, h);
        at.concatenate(tr);
        at.concatenate(rot);
    }

    private void mirror(int th, int x, AffineTransform at) {
        double th2 = ((double) th) / 2.0;
        AffineTransform tr = AffineTransform.getTranslateInstance(-th2, 0);
        AffineTransform sc = AffineTransform.getScaleInstance(-1, 1);
        AffineTransform tr2 = AffineTransform.getTranslateInstance(x + th2, 0);

        at.concatenate(tr2);
        at.concatenate(sc);
        at.concatenate(tr);
    }

}

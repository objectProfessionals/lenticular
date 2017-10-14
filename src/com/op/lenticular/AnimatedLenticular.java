package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AnimatedLenticular extends Base {
    private static final String HORIZ = "horiz";
    private static final String VERT = "vert";
    private static String DROPS = "drops";
    private static String SPIN = "spin";
    private static String LINEAR = "linear";
    private static String DOTS = "dots";
    private static String DOTS_A4 = "dotsA4";
    private static String SPIRAL = "spiral";
    private static String SINE = "sine";
    private static String LAND = "land";
    private static String TEXT = "text";
    private static String FLOWERS = "flowers";
    private static String VIRGA = "virga";
    private static String PORT = "portrait";
    private static String BLANK = "blank";
    private String type = SPIN;
    private String direction = HORIZ;
    private int numPics = 15;
    private int stripThick = 1;
    private String dir = host + direction + "/" + type + "/";
    private String fileName = type + "H";
    private double dpi = 600;
    private int w = 0;
    private int h = 0;
    private String inputType = ".png";
    private String outputType = ".png";
    private String finishedFileSuffix = "FIN" + outputType;
    private String src = "";
    private BufferedImage opImage;
    private BufferedImage ipImage;
    private double i2mm = 25.4;

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
        AnimatedLenticular mr = new AnimatedLenticular();
        mr.createImageFiles();
    }

    public void createImageFiles() throws Exception {
        validateImages();
        createOutputImage();
    }

    private void validateImages() throws Exception {
        if (numPics <= 0 || "".equals(dir.trim()) || "".equals(fileName.trim())
                || dpi <= 10) {
            throw new Exception("Image data not fully supplied");
        }
    }

    private void createOutputImage() throws Exception {
        src = dir;
        String flName = src + fileName + "01" + inputType;
        File inputFile = new File(flName);
        ipImage = ImageIO.read(inputFile);
        w = ipImage.getWidth();
        h = ipImage.getHeight();
        opImage = createAlphaBufferedImage(w, h);
        Graphics2D opg = (Graphics2D) opImage.getGraphics();
        opg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        src = dir;
        if (direction.equals(HORIZ)) {
            createStripsH(opg);
            addAlignmentH(opg);
        } else {
            createStripsV(opg);
            addAlignmentV(opg);
        }
        File fFile1 = new File(src + fileName + finishedFileSuffix);
        savePNGFile(opImage, fFile1, dpi);
        opg.dispose();
        System.out.println("PNG image created : " + src);
    }

    private void createStripsH(Graphics2D opg) throws IOException {
        String flName;
        File inputFile;
        for (int n = 1; n <= numPics; n++) {
            String nStr = (n < 10 ? "0" + n : "" + n);
            flName = src + fileName + nStr + inputType;
            inputFile = new File(flName);
            ipImage = ImageIO.read(inputFile);
            int yOff = n - 1;
            for (int y = yOff; y < h - stripThick; y = y
                    + (stripThick * numPics)) {
                BufferedImage clip = ipImage.getSubimage(0, y, w, stripThick);
                opg.drawImage(clip, 0, y, null);
            }
            System.out.println("Completed image: " + inputFile.getPath());
        }
    }

    private void addAlignmentH(Graphics2D opG) {
        double mm = 4.0;
        int edge = (int) (dpi * (mm / i2mm));
        int d = (stripThick * numPics);
        opG.setColor(Color.BLACK);
        for (int y = 0; y < h; y = y + d) {
            int www = 0;
            if (y == 0 || y + d > h) {
                www = w;
            } else {
                www = edge;
            }
            opG.fillRect(0, y, www, stripThick);
            opG.fillRect(w - www, y, www, stripThick);
        }
    }

    /**
     * VERTICAL
     *
     * @param opG
     */
    private void addAlignmentV(Graphics2D opG) {
        int hh = h / 40;
        int d = (stripThick * numPics);
        opG.setColor(Color.BLACK);
        for (int x = 0; x < w; x = x + d) {
            int hhh = 0;
            if (x == 0 || x + d > w) {
                hhh = h;
            } else {
                hhh = hh;
            }
            opG.fillRect(x, 0, stripThick, hhh);
            opG.fillRect(x, h - hhh, stripThick, hhh);
        }
    }

    private void createStripsV(Graphics2D opg) throws IOException {
        String flName;
        File inputFile;
        for (int n = 1; n <= numPics; n++) {
            String nStr = (n < 10 ? "0" + n : "" + n);
            flName = src + fileName + nStr + inputType;
            inputFile = new File(flName);
            ipImage = ImageIO.read(inputFile);
            int xOff = n - 1;
            for (int x = xOff; x < w - stripThick; x = x
                    + (stripThick * numPics)) {
                BufferedImage clip = ipImage.getSubimage(x, 0, stripThick, h);
                opg.drawImage(clip, x, 0, null);
            }
            System.out.println("Completed image: " + inputFile.getPath());
        }
    }
}

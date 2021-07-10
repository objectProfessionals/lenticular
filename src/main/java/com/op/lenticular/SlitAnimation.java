package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;


public class SlitAnimation extends Base {

    String dir = host + "slit/sphere/";
    String ext = ".png";
    String fileName = "";
    String opFileName = "_OUT";
    String opSlitsName = "_SLIT";
    double dpi = 300;

    int w = 0; //1000;
    int h = 0; //1000;
    int numFrames = 8;
    int frameF = 4;
    int frameH = numFrames * frameF;
    int slitDepth = frameH - frameF;
    BufferedImage opImageSlits;
    BufferedImage opImageFile;
    Graphics2D opGFile;
    Graphics2D opGSlits;
    ArrayList<BufferedImage> ipImages = new ArrayList<>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        createSlit();
    }

    public static void createSlit() {
        try {
            createImageFile();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void createImageFile() throws IOException, Exception {
        SlitAnimation mr = new SlitAnimation();
        mr.createImageFiles();
    }

    public void createImageFiles() throws Exception {
        initImages();

        //drawSlits();

        drawImages();

        saveImages();
    }

    private void drawSlits() {
        opGSlits.setColor(Color.BLACK);
        for (int y = 0; y < h; y = y + frameH) {
            opGSlits.fillRect(0, y, w, slitDepth);
        }
    }

    private void drawImages() {
        int i = 0;
        for (int y = 0; y < h; y = y + frameF) {
            BufferedImage image = ipImages.get(i);
            BufferedImage sub = image.getSubimage(0, y, w, frameF);
            opGFile.drawImage(sub, null, 0, y);
            i = (i + 1) % numFrames;
        }
    }

    private void initImages() throws Exception {
        for (int i = 0; i < numFrames; i++) {
            String flName = dir + "000" + fileName + (i+1) + ext;
            File inputFile = new File(flName);
            ipImages.add(ImageIO.read(inputFile));
        }
        w = ipImages.get(0).getWidth();
        h = ipImages.get(0).getHeight();

        opImageFile = createBufferedImage(w, h);
        opGFile = (Graphics2D) opImageFile.getGraphics();
        opGFile.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        opGFile.setColor(Color.WHITE);
        opGFile.fillRect(0, 0, w, h);

        opImageSlits = createBufferedImage(w, h);
        opGSlits = (Graphics2D) opImageSlits.getGraphics();
        opGSlits.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        opGSlits.setColor(Color.WHITE);
        opGSlits.fillRect(0, 0, w, h);
    }

    private void saveImages() throws Exception {
        File fFile1 = new File(dir + opFileName + ext);
        savePNGFile(opImageFile, fFile1, dpi);

        File fFile2 = new File(dir + opSlitsName + ext);
        savePNGFile(opImageSlits, fFile2, dpi);
    }

}

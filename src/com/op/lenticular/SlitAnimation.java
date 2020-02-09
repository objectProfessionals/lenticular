package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SlitAnimation extends Base {

    String dir = host + "slit/";
    String src = dir + "src/";
    String inputType = ".png";
    String outputType = ".png";
    String fileName = "anim"+outputType;
    String slitsName = "slits"+outputType;
    double dpi = 300;

    int w = 0;
    int h = 0;
    int slitDepth = 10;
    int numFrames = 10;
    int slitWidth = slitDepth-1;
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
        drawSlits();
        drawImages();
        saveImage();
    }

    private void drawSlits() {

//        for (int x = 0; x< 1)
//        opGSlits.fillRect();
    }

    private void drawImages() {
    }

    private void initImages() throws Exception {
        for (int i=0; i< numFrames; i++) {
            String flName = src + fileName + inputType;
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
        opGSlits = (Graphics2D) opImageFile.getGraphics();
        opGSlits.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        opGSlits.setColor(Color.WHITE);
        opGSlits.fillRect(0, 0, w, h);


    }

    private void saveImage() throws Exception {
        File fFile1 = new File(src + fileName);
        savePNGFile(opImageFile, fFile1, dpi);
    }

}

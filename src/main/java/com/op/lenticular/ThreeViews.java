package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ThreeViews extends Base {

    String dir = host + "threeViews/";
    String ext = ".png";
    String fileName = "Stairs";
    String opFileName = "_3V";
    String opSlitsName = opFileName + "_SLIT";
    double dpi = 300;
    private double in2mm = 25.4;

    boolean horiz = false;
    int w = 0; //1000;
    int h = 0; //1000;
    int opW = 0;
    int opH = 0;
    int numFrames = 3;
    int sliceWidthmm = 10;
    int creaseWidthmm = 1;
    int sliceWidth = 0;
    int creaseWidth = 0;
    int frameH = numFrames * sliceWidth;
    BufferedImage opImageFile;
    Graphics2D opG;
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
        ThreeViews mr = new ThreeViews();
        mr.createImageFiles();
    }

    public void createImageFiles() throws Exception {
        initImages();

        if (horiz) {
            //drawImagesHoriz();
        } else {
            drawImagesVert();
        }


        saveImages();
    }

    private void drawImagesHoriz() {
        int i = 0;
        for (int y = 0; y < h; y = y + sliceWidth) {
            BufferedImage image = ipImages.get(i);
            int sh = y + sliceWidth >= h ? h - y : sliceWidth;
            BufferedImage sub = image.getSubimage(0, y, w, sliceWidth);
            opG.drawImage(sub, null, 0, y);
            i = (i + 1) % numFrames;
        }
    }

    private void drawImagesVert() {
        int xx = 0;
        for (int x = 0; x < w; x = x + sliceWidth) {
            for (int i = 0; i < numFrames; i++) {
                BufferedImage image = ipImages.get(i);
                int sw = x + sliceWidth >= w ? w - x : sliceWidth;
                BufferedImage sub = image.getSubimage(x, 0, sw, h);
                opG.drawImage(sub, null, xx, 0);
                xx = xx + sliceWidth + creaseWidth;
            }
        }
    }

    private void initImages() throws Exception {
        for (int i = 0; i < numFrames; i++) {
            String flName = dir + fileName + (i + 1) + ext;
            File inputFile = new File(flName);
            ipImages.add(ImageIO.read(inputFile));
        }
        w = ipImages.get(0).getWidth();
        h = ipImages.get(0).getHeight();
        sliceWidth = (int) (sliceWidthmm * (dpi / in2mm));
        creaseWidth = (int) (creaseWidthmm * (dpi / in2mm));

        if (horiz) {
            opW = w;
            opH = h * numFrames;
            opImageFile = createBufferedImage(opW, opH);
        } else {
            int tot = 0;
            for (int x = 0; x < w; x = x + sliceWidth) {
                for (int i = 0; i < numFrames; i++) {
                    tot = tot + sliceWidth + creaseWidth;
                }
            }

            opW = tot;
            opH = h;
            opImageFile = createBufferedImage(opW, opH);
        }
        opG = (Graphics2D) opImageFile.getGraphics();
        opG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        opG.setColor(Color.WHITE);
        opG.fillRect(0, 0, opW, opH);

    }

    private void saveImages() throws Exception {
        File fFile1 = new File(dir + opFileName + ext);
        savePNGFile(opImageFile, fFile1, dpi);
    }

}

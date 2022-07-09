package com.op.lenticular.holoplay;

import com.op.lenticular.Base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class QuiltPng extends Base {

    String fileName = "test3Room1.";
    String dir = host + "holoplay/test2/";
    String ext = ".png";
    String opFileName = fileName + "_Quilt_8x6";

    //LG dim end = 1.23941x2=2.47882,1.65255x2=3.3051, z=-1
    int ipw = 0; //1000;
    int iph = 0; //1000;
    int opW = 3360;
    int opH = 3360;
    int cols = 8;
    int rows = 6;
    double w = 420;
    double h = 560;
    double scx = 0;
    double scy = 0;

    BufferedImage opImageFile;
    Graphics2D opG;
    ArrayList<BufferedImage> ipImages = new ArrayList<>();
    private int numImages = rows * cols;

    /**
     * @param args
     */
    public static void main(String[] args) {
        createQuilt();
    }

    public static void createQuilt() {
        try {
            createImageFile();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void createImageFile() throws IOException, Exception {
        QuiltPng mr = new QuiltPng();
        mr.createImageFiles();
    }

    public void createImageFiles() throws Exception {
        initImages();

        drawQuilt();
        saveImages();
    }

    private void drawQuilt() {
        int x = 0;
        int y = (int) (opH - h);
        int i = 0;
        AffineTransform sc = AffineTransform.getScaleInstance(scx, scy);
        BufferedImageOp bio = new AffineTransformOp(sc, AffineTransformOp.TYPE_BILINEAR);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                opG.drawImage(ipImages.get(i), bio, x, y);
                x = (int) (x + w);
                i++;
            }
            y = (int) (y - h);
            x = 0;
        }
    }

    private void initImages() throws Exception {
        for (int i = 0; i < numImages; i++) {
            String padded = String.format("%02d", i);
            String flName = dir + fileName + padded + ext;
            File inputFile = new File(flName);
            ipImages.add(ImageIO.read(inputFile));
        }
        opImageFile = createBufferedImage(opW, opH);
        opG = (Graphics2D) opImageFile.getGraphics();
        opG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        opG.setColor(Color.WHITE);
        opG.fillRect(0, 0, opW, opH);

        ipw = ipImages.get(0).getWidth();
        iph = ipImages.get(0).getHeight();

        scx = w / ((double) ipw);
        scy = h / ((double) iph);
    }

    private void saveImages() throws Exception {
        saveJPGFile(opImageFile, dir + opFileName + ext, 300, 100);
        File outfile = new File(dir + opFileName + ".png");

        savePNGFile(opImageFile, outfile, 300);
    }

}

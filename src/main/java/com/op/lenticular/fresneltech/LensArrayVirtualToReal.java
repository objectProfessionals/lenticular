package com.op.lenticular.fresneltech;

import com.op.lenticular.Base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;

public class LensArrayVirtualToReal extends Base {
    // xNum = 124.01574803149606299212598425197
// yNum = 300.67491563554555680539932508436
//yRad = 23.36;
//yNum = 300.32106164383561643835616438356

    double ss = 1;
    //private String dir = "popims/10x10/";
    // private String dir = "popims/CreditCard/";
    // private String dir = "popims/camera/";
    // private String dir = "fresnelTech/scans/";
    // private String dir = "fresnelTech/D300/";
    // private String dir = "popims/scans/";
    // private String dir = "fresnelTech/D300/";
    // private String dir = "popims/Lytro/";
    private String dir = host + "virtual2real/popims/";
    private String ext = ".jpg";
    private String extPng = ".png";
    private String extJpg = ".jpg";
    //private String name = "Virga";
    private String name = "a4scan";
    //private String name = "spheres";
    //private String name = "VirgaSculpt";
    private double yRad = -1;
    private double xRad = -1;
    private double xStart = -1;
    private double yStart = -1;
    // private double xDF = -1;
    private String opName = "rotated" + name + extPng;
    private Color bg = Color.WHITE;
    private Color fg = Color.BLACK;
    private BufferedImage ipImage;
    private BufferedImage opImage;
    private Graphics2D opG;
    private double diaFactor = 2.0;
    private double scaleX = 1;
    private double scaleY = 1;
    private int w;
    private int h;
    private boolean popims = true;
    private int dpi = -1;
    private boolean onlyOutline = false;
    private final static int SAMPLE = 0;
    private final static int A4 = 1;
    private final static int A2 = 2;
    private final static int x10 = 3;
    private final static int CC = 4;
    private final static int LYTRO = 5;
    private final static int D300 = 6;
    private int size = A4;
    double i2mm = 25.4;
    private static final double ROT = Math.PI;
    private static final double SCALE = 1;
    double cos60 = Math.cos(Math.PI / 3);
    double borderFactor = 0.0;
    // double scaleFactor = 9204.0 / 3280.0;
    double scaleFactor = 0;

    public static void main(String[] args) {
        LensArrayVirtualToReal test = new LensArrayVirtualToReal();
        try {
            test.createImage();
            //test.printLensPointsOrig(2, 100, 10, 1.35, 0.66);//fresnel
            //test.printLensPointsOrig(2, 60.734108, 6.08488, 1.15, 0.061416/2);//popims
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createImage() throws Exception {
        System.out.println("Loading images...");
        String ipName = name + ext;
        String flName = dir + ipName;
        File inputFile = new File(flName);
        System.out.println("Loaded image : " + inputFile.getPath());
        ipImage = ImageIO.read(inputFile);
        w = ipImage.getWidth();
        h = ipImage.getHeight();
        boolean toggleOff = false;
        double sin60 = Math.sin(Math.PI / 3);
        if (popims) {
            // popims
            yStart = 0;
            xStart = 0;

            // A4
            // yNum = 300.66429000948985727842653254895
            // xNum = 124.01250003100312500775078125194
            // yStart = 63.0;
            // xStart = 36.0;
            // a4scan2
            // yStart = 100.0;
            // xStart = 38.0;

            // 10x10
            // yNum = 101.22857144303265306329037900904
            // xNum = 59.050000014762500003690625000923
            // yStart = 57.0;
            // xStart = 40.0;
            if (size == A2) {
                fg = Color.BLACK;
                dpi = 720;
                double facY = 1.0012;
                yRad = 14.0 * facY;
                double facX = 0.98925;
                xRad = facX * 14.0 / sin60;

                double wMM = 210 * 2;
                double hMM = 297 * 2;
                w = (int) (((double) dpi) * wMM / i2mm);
                h = (int) (((double) dpi) * hMM / i2mm);
                yStart = -1; // 30.0;
                xStart = 1; // 16.5;
                System.out.println("ydiv=" + (((double) h) / (yRad * 2.0)));

            } else if (size == A4) {
                // A4
                dpi = 720;
                double fac = 14;
                double facY = 1.0012;
                yRad = fac * facY;
                double facX = 0.98925;
                xRad = facX * fac / sin60;
                double wMM = 210;
                double hMM = 297;
                w = (int) (((double) dpi) * wMM / i2mm);
                h = (int) (((double) dpi) * hMM / i2mm);
                yStart = -1;
                xStart = 1;
            } else if (size == x10) {
                // A4
                dpi = 720;
                double facY = 0.997; // 0.99815; // 1.0012;
                yRad = 14.0 * facY;
                double facX = 0.986; // 0.9895;

                // double facY = 0.99815; // 1.0012;
                // // double facX = 0.991;//0.989
                // double facX = 0.9895;
                xRad = facX * 14.0 / sin60;// (dpi * (i2mm * oX / oDpi) / i2mm)
                // / 2.0;
                // yStart = 28.0;
                // xStart = 22.0;
                // 4437, 7333 , 4427, 7350
                double wMM = 100;
                double hMM = 100;
                w = (int) (((double) dpi) * wMM / i2mm);
                h = (int) (((double) dpi) * hMM / i2mm);
                yStart = 0;// -7; // 30.0;
                xStart = 1; // 16 .5;
            } else if (size == CC) {
                // A4
                dpi = 600;
                double facY = 0.997; // 0.99815; // 1.0012;
                yRad = 20 * facY;
                double facX = 0.986; // 0.9895;

                xRad = facX * 14.0 / sin60;// (dpi * (i2mm * oX / oDpi) / i2mm)
                // / 2.0;
                double wMM = 85.1;
                double hMM = 53;
                w = (int) (((double) dpi) * wMM / i2mm);
                h = (int) (((double) dpi) * hMM / i2mm);
                yStart = 0;// -7; // 30.0;
                xStart = 0; // 16.5;
            } else if (size == LYTRO) {
                // scale = 9204px @ 720dpi
                double facY = 0.9935; // 0.99815; // 1.0012;
                double facX = 1.0003;
                yRad = 5.0 * facY;
                xRad = facX * yRad / sin60;
                w = 3280;
                h = 3280;
                yStart = 3;// -7; // 30.0;
                xStart = 3; // 16.5;
            } else if (size == SAMPLE) {
                // 10x10
                // yNum = 101.22857144303265306329037900904
                // xNum = 59.050000014762500003690625000923
                // xDF = 1.011;
                yStart = 57.0;
                xStart = 40.0;
            }

        } else {
            if (size == D300) {
                // lensArray
                yRad = 39.0 / 2.0;
                xRad = yRad / sin60;
                yRad = yRad * 1.9 * 0.5;
                xRad = yRad / sin60;
                int xx = 0;
                int yy = 16;

                yStart = yy + yRad;
                xStart = xx + xRad;

                dpi = 600;
            } else {
                // lensArray
                yRad = 55.075 / 2.0;
                xRad = yRad / sin60;
                int xx = 16;
                int yy = 0;

                yStart = yy + yRad;
                xStart = xx + xRad;

                //yRad = yRad * 1.9 * 0.5;
                //xRad = yRad / sin60;
                // xStart = 68;
                // yStart = 90;

                // from scans
                // int xx = 0;
                // int yy = 0;
                // yRad = 55.075 / 2.0;// ft
                // yRad = 23.35 / 2.0;// popims
                // xRad = yRad / sin60;
                // xRad = xRad * 0.996;// ft
                // xRad = xRad * 0.995;// popims
                // yStart = 0;
                // xStart = 0;

                // w = 5976 h = 4795
                dpi = 600;
            }
        }
        opImage = createAlphaBufferedImage(w, h);

        opG = (Graphics2D) opImage.getGraphics();
        opG.setColor(bg);

        // opG.fillRect(0, 0, w, h);

        opG.drawImage(ipImage, 0, 0, null);

        opG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (onlyOutline) {
            opG.drawImage(ipImage, null, null);
        }
        // calculateRads();
        double x = xStart;
        double y = yStart;
        int numX = 0;
        int numY = 0;
        boolean calcNumY = true;
        while (x < w) {
            y = yStart;
            while (y < h) {
                double yy = y;
                if (toggleOff) {
                    yy = y + yRad;
                }
                if (onlyOutline) {
                    drawOutline(x, yy);
                } else {
                    drawRotatedScaled(x, yy, ROT, SCALE);
                }
                y = y + diaFactor * yRad;
                if (calcNumY) {
                    numY++;
                }
            }
            x = x + xRad * 1.5;
            toggleOff = !toggleOff;
            calcNumY = false;
            numX++;
        }
        System.out.println("numX, numY=" + numX + "," + numY);
        System.out.println("Creating output image...");
        opImage = addBorder(opImage);
        opImage = doScale(opImage);
        File fFile1 = new File(dir + opName);
        savePNGFile(opImage, fFile1, dpi);

        opG.dispose();
        System.out.println("Image saved : " + fFile1.getPath());
    }

    private BufferedImage addBorder(BufferedImage opImage2) {
        if (borderFactor == 0.0) {
            return opImage2;
        }
        int wb = (int) (((double) w) * borderFactor);
        int hb = (int) (((double) w) * borderFactor);
        int ww = w + 2 * wb;
        int hh = h + 2 * hb;
        BufferedImage opImage1 = createAlphaBufferedImage(ww, hh);
        Graphics2D g2D = (Graphics2D) opImage1.getGraphics();
        g2D.drawImage(opImage2, wb, hb, null);
        return opImage1;
    }

    private BufferedImage doScale(BufferedImage opImage2) {
        if (scaleFactor == 0.0) {
            return opImage2;
        }
        int ww = (int) (((double) w * scaleFactor));
        int hh = (int) (((double) h * scaleFactor));
        BufferedImage opImage1 = createAlphaBufferedImage(ww, hh);
        Graphics2D g2D = (Graphics2D) opImage1.getGraphics();
        g2D.drawImage(opImage2, 0, 0, ww, hh, null);
        return opImage1;
    }

    private void printLensPoints(double n, double r, double d, double nLL,
                                 double rLL) {
        // 1/f = (n-1) (1/r1 -1/r2 + (((n-1)*d)/(n*R1*R2))
        double f = calculateF(r, -r, n, d);
        double fLL = calculateF(rLL, 1000000, nLL, 0.0);
        fLL = 0.06495; // 0.067784;// 0.22883;// 0.0686 + 0.03069;
        fLL = 0.05;
        System.out.println("f1=" + f);
        // System.out.println("f=" + f);
        // System.out.println("fLL=" + fLL);
        double lens1 = (fLL + f);
        double lens2 = (fLL + (f * 3.0));
        double cen = (fLL + (f * 4.0));
        System.out.println("lens1=" + lens1);
        System.out.println("lens2=" + lens2);
        System.out.println("cen=" + cen);
    }

    private void printLensPointsOrig(double n, double r, double d, double nLL,
                                     double rLL) {
        // 1/f = (n-1) (1/r1 -1/r2 + (((n-1)*d)/(n*R1*R2))
        double f = calculateF(r, -r, n, d);
        double fLL = calculateF(rLL, 100000, nLL, 0.0);
        System.out.println("f=" + f);
        System.out.println("fLL=" + fLL);
        double lens1 = (fLL + f);
        double lens2 = (fLL + (f * 3.0));
        double cen = (fLL + (f * 4.0));
        System.out.println("lens1=" + lens1);
        System.out.println("lens2=" + lens2);
        System.out.println("cen=" + cen);
    }

    private double calculateF(double r1, double r2, double n, double d) {
        double iF = (n - 1)
                * (((1 / r1) - (1 / r2) + (((n - 1) * d) / (n * r1 * r2))));
        double f = 1 / iF;
        return f;
    }

    private void drawRotatedScaled(double x, double yy, double rot, double scale) {
        int sx = (int) (x - xRad);
        int sy = (int) (yy - yRad);
        int sw = (int) (xRad * diaFactor);
        int sh = (int) (yRad * diaFactor);
        if (sx < 0 || sy < 0 || sx + sw > w || sy + sh > h) {
            return;
        }
        BufferedImage sub = ipImage.getSubimage(sx, sy, sw, sh);
        Shape hex = new HexShape(x, yy);
        opG.setClip(hex);
        AffineTransform at = new AffineTransform();
        AffineTransform sc = null;
        if (scale != 1.0) {
            sc = AffineTransform.getScaleInstance(scale, scale);
        }
        AffineTransform tr = AffineTransform.getTranslateInstance(-xRad, -yRad);
        AffineTransform tr2 = AffineTransform.getTranslateInstance(x, yy);
        AffineTransform ro = AffineTransform.getRotateInstance(rot);
        at.concatenate(tr2);
        if (sc != null) {
            at.concatenate(sc);
        }
        at.concatenate(ro);
        at.concatenate(tr);
        opG.drawImage(sub, at, null);

        if (x < 2 * xRad || x > w - (3 * xRad)) {
            opG.setColor(Color.RED);
            int r = 2;
            opG.fillOval((int) x - r, (int) yy - r, r * 2, r * 2);
        }
        // opG.draw(hex);
    }

    private void drawOutline(double x, double yy) {
        try {
            opG.setColor(fg);
            opG.setStroke(new BasicStroke(1));
            // BufferedImage sub = ipImage.getSubimage((int) (x - xRad),
            // (int) (yy - yRad), (int) (xRad * diaFactor),
            // (int) (yRad * diaFactor));
            Shape hex = new HexShape(x, yy);
            // opG.setClip(hex);
            // AffineTransform tr = AffineTransform.getTranslateInstance(x -
            // xRad,
            // yy - yRad);
            // opG.drawImage(sub, tr, null);
            opG.draw(hex);
            opG.drawLine((int) x, (int) yy, (int) x, (int) yy);
        } catch (RasterFormatException e) {
        }
    }

    private class HexShape extends Polygon {
        private static final long serialVersionUID = 8174789653342556173L;

        HexShape(double cx, double cy) {
            super();
            double radXOut = scaleX * xRad;
            double radYOut = scaleY * yRad;
            int x1 = (int) (cx + radXOut * cos60);
            int y1 = (int) (cy - radYOut);
            int x2 = (int) (cx + radXOut);
            int y2 = (int) (cy);
            int x3 = (int) (cx + radXOut * cos60);
            int y3 = (int) (cy + radYOut);
            int x4 = (int) (cx - radXOut * cos60);
            int y4 = (int) (cy + radYOut);
            int x5 = (int) (cx - radXOut);
            int y5 = (int) (cy);
            int x6 = (int) (cx - radXOut * cos60);
            int y6 = (int) (cy - radYOut);
            this.addPoint(x1, y1);
            this.addPoint(x2, y2);
            this.addPoint(x3, y3);
            this.addPoint(x4, y4);
            this.addPoint(x5, y5);
            this.addPoint(x6, y6);
        }
    }
}

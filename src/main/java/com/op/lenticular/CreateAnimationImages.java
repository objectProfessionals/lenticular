package com.op.lenticular;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class CreateAnimationImages extends Base {

    protected static final String HORIZ = "horiz";
    protected static final String VERT = "vert";
    private String direction = HORIZ;
    protected static String FLOWERS = "flowers";
    protected static String DROPS = "drops";
    protected static String SPIN = "spin";
    private static String WIND = "wind";

    protected static String SINE = "sine";
    protected static String LINEAR = "linear";
    protected static String DOTS = "dots";
    protected static String DOTS_A4 = "dotsA4";
    protected static String SPIRAL = "spiral";
    protected static String LAND = "land";
    protected static String TEXT = "text";
    protected static String PORT = "portrait";
    protected static String TORN = "tornado";
    protected static String VIRGA = "virga";
    protected static String CUFF = "cuff";
    protected static String BLANK = "blank";

    protected static String type = WIND;

    private String dir = host + direction + "/";
    private String extPng = ".png";
    private String name = "H";
    private Color bg = Color.WHITE;
    private Color fg = Color.GRAY;
    private Color fontCol = Color.DARK_GRAY;
    private BufferedImage opImage;
    private BufferedImage bgImage;
    private Graphics2D opG;
    private Graphics2D bgG;
    private boolean readImage = false;
    private BufferedImage rdImage;
    private String readFile = "virga.png";
    private boolean addText = false;
    private double frSc = 1;
    private double wmm = 105; //95;
    private double hmm = 138; //95;
    private double radmm = 5;
    private double sqmm = 5.0;
    private int w;
    private int h;

    private int dpi = 600;
    private ArrayList<Center> centers = new ArrayList<Center>();
    private int num = 16;
    private int frames = 15;
    // private String colStr =
    // "97F7EE,CCDDAA,BFADD8,88AA99,AA79F2,00A898,667755,558811,118877,593F7F,3D4C4B,00665C,552299,881122,2B2B2B,00332E,111011,110022";
    private String colStr = "78C4BD,345BA1,41A332,A5333B,A67D33,070B5D,222222,88AA99,AA79F2,00A898,667755,558811,118877,552299,881122";
    private ArrayList<Color> colors = new ArrayList<Color>();
    private Random rand;
    private int rnd = 1;
    private double bgSat = 0.5;
    private double bgLight = 0.25;
    private boolean drawBorder = true;
    private boolean borderRound = false;
    private double i2mm = 25.4;
    // private String s1 = "THANK YOU KABIR";
    private String s1 = "SANJAY & VIRGINIJA";
    private String s2 = "MARCH 30th 2014";
    private boolean drawBackground = true;
    private String fontFile = host + "fonts/NEWTOWN.TTF";
//    private final int CAP = BasicStroke.CAP_ROUND;
//    private final int JOIN = BasicStroke.JOIN_ROUND;
    private final int CAP = BasicStroke.CAP_SQUARE;
    private final int JOIN = BasicStroke.JOIN_MITER;

    public static void main(String[] args) {
        CreateAnimationImages test = new CreateAnimationImages();
        try {
            test.createImage();
            // test.createColors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createColors() throws Exception {
        initColors();
        w = (int) (dpi * frSc*wmm / i2mm);// 2362;
        h = (int) (dpi * frSc*hmm / i2mm);
        bgImage = createAlphaBufferedImage(w, h);
        bgG = (Graphics2D) bgImage.getGraphics();
        int sq = w / 4;
        int i = 0;
        for (int y = 0; y < h - 2; y = y + sq) {
            for (int x = 0; x < w - 2; x = x + sq) {
                if (i < colors.size()) {
                    Color col = colors.get(i);
                    bgG.setColor(col);
                    bgG.fillRect(x, y, sq, sq);
                    bgG.setColor(Color.WHITE);
                    String cStr = getHex(col);
                    bgG.drawString(cStr, x + 10, y + 20);
                    i++;
                }
            }
        }
        System.out.println("Creating colors image...");
        String src = dir + type
                + "/";


        String opName = type + name + "COLS" + extPng;
        File fFile1 = new File(src + opName);
        savePNGFile(bgImage, fFile1, dpi);
        bgG.dispose();
        System.out.println("Image saved : " + fFile1.getPath());
    }

    private String getHex(Color col) {
        String hexColour = Integer.toHexString(col.getRGB() & 0xffffff);
        if (hexColour.length() < 6) {
            hexColour = "000000".substring(0, 6 - hexColour.length())
                    + hexColour;
        }
        return "#" + hexColour;
    }

    public void createImage() throws Exception {
        if (type.equals(CUFF)) {
            wmm = 14;
            hmm = 14;
        } else if (type.equals(DOTS_A4)) {
            wmm = 210;
            hmm = 297;
        }
        w = (int) ((frSc*wmm * dpi / i2mm));
        h = (int) ((frSc*hmm * dpi / i2mm));
        initDir();
        initStatics();
        initColors();
        initCenters();
        initBackground();
        for (int f = 0; f < frames; f++) {
            drawImage(f);
        }
        System.out.println("Finished");
    }

    private void initDir() {
        File d = new File(dir+type);
        if (!d.exists()) {
            d.mkdir();
        }
    }

    private void initBackground() {
        bgImage = createAlphaBufferedImage(w, h);
        bgG = (Graphics2D) bgImage.getGraphics();
        if (drawBackground) {
            int sqpx = (int) ((sqmm * dpi / i2mm));
            int sq = (int)(sqpx * frSc);
            int xs = (int) ((double) (sq) * ((double) (1) / (double) frames));
            for (int y = 0; y < h; y = y + sq) {
                for (int x = -xs; x < w; x = x + sq) {
                    int rr = Math.abs(getNextRandomInt());
                    int i = (rr % colors.size());
                    Color col = getLighter(colors.get(i));
                    bgG.setColor(col);
                    bgG.fillRect(x, y, sq, sq);
                    // bgG.fillOval(x, y, sq, sq);
                }
            }
        }

        if (readImage) {
            try {
                System.out.println("Creating rd image...");
                String src = dir
                        + type + "/";
                String bgName = src + readFile;
                File bgFile = new File(bgName);
                rdImage = ImageIO.read(bgFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCentersForVirga();
        }
    }

    private void initCentersForVirga() {
        double nn = 10000;
        double bl = Math.sqrt(nn);
        double x = 0;
        double y = 0;
        double r = ((double) w) / (2.0 * bl);
        for (Center cen : centers) {
            cen.x = x + r;
            cen.y = y + r;

            int rgb = rdImage.getRGB((int) cen.x, (int) cen.y);
            Color col = new Color(rgb);
            cen.col = col;

            x = x + 2 * r;
            if (x > w) {
                y = y + 2 * r;
                x = r;
            }

        }

    }

    private Color getLighter(Color color) {
        float sc = 1 / 255f;
        float r = color.getRed() * sc;
        float g = color.getGreen() * sc;
        float b = color.getBlue() * sc;
        float gr = (float) bgSat;
        float a = (float) bgLight;
        return new Color(r * gr, g * gr, b * gr, a);
    }

    private void drawImage(int frame) throws Exception {
        opImage = createAlphaBufferedImage(w, h);
        opG = (Graphics2D) opImage.getGraphics();
        opG.setColor(bg);
        opG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        opG.fillRect(0, 0, w, h);
        opG.drawImage(bgImage, null, null);
        System.out.println("Drawing image...");
        drawBorder();
        for (int n = 0; n < num; n++) {
            if (isType(DROPS)) {
                drawRings(frame, n);
            } else if (isType(LINEAR)) {
                drawLinear(frame, n);
            } else if (isType(DOTS)) {
                drawDots(frame, n);
            } else if (isType(DOTS_A4)) {
                drawDots(frame, n);
            } else if (isType(SPIRAL)) {
                drawSpiral(frame, n);
            } else if (isType(LAND)) {
                drawLand(frame, n);
            } else if (isType(SINE)) {
                drawSine(frame, n);
            } else if (isType(SPIN)) {
                drawSpin(frame, n);
            } else if (isType(TEXT)) {
                drawText(frame, n);
            } else if (isType(FLOWERS)) {
                drawFlowers(frame, n);
            } else if (isType(PORT)) {
                drawPortrait(frame, n);
            } else if (isType(TORN)) {
                drawTornado(frame, n);
            } else if (isType(WIND)) {
                drawWind(frame, n);
            } else if (isType(VIRGA)) {
                drawVirga(frame, n);
            } else if (isType(CUFF)) {
                drawCuff(frame, n);
            } else if (isType(BLANK)) {
                drawBlank(frame, n);
            }
        }

        if (addText) {
            addAlignmentText(frame);
        }
        System.out.println("Creating output image=" + frame);
        String src = dir + type
                + "/";
        int f = frame + 1;
        String nStr = (num < 10 ? "" + f : (f < 10 ? "0" + f : "" + f));
        String opName = type + name + nStr + extPng;
        File fFile1 = new File(src + opName);
        savePNGFile(opImage, fFile1, dpi);
        opG.dispose();
        System.out.println("Image saved : " + fFile1.getPath());
    }

    private void drawBlank(int frame, int n) {
        // TODO Auto-generated method stub

    }

    private void drawCuff(int frame, int i) {
        Center c = centers.get(i);
        double f = (((double) frame) / frames);
        double ii = ((double) i) / (double) num;

        double tMax = 2;
        for (double t = 0; t < tMax; t++) {
            double fr = t / tMax + f / tMax;
            double frr = ((fr + ii) % 1.0);
            double ffr = frSc * w * frr;
            double rr = ffr;
            double x = (w / 2) - rr;
            double y = (h / 2) - rr;
            int str = (int) (frSc * ((double) w) / ((double) (num)));
            setStroke(str, CAP, JOIN);
            Color col = getFadedColor(c, 1);
            opG.setColor(col);
            opG.drawArc((int) x, (int) y, (int) rr * 2, (int) rr * 2, 0, 360);
        }
    }

    private void setStroke(int str, int cap, int join) {
        opG.setStroke(new BasicStroke(str, cap, join));
    }

    private void drawVirga(int frame, int n) {
        double nn = 10000;
        double bl = Math.sqrt(nn);
        double r = ((double) w / bl) / 2.0;
        double f = ((double) (frame)) / ((double) frames);
        double ff = Math.sin(f * Math.PI * 0.5);
        Center c = centers.get(n);
        double facY = (c.off > 0.5 ? 1 : -1);
        double facX = (c.off2 > 0.5 ? 1 : -1);
        double yOff = facY * ff * (double) h;
        double xOff = facX * ff * (double) w;

        double rad = ((double) w) * c.rad * 0.25;
        xOff = rad - rad * Math.cos(f * Math.PI * 2.0);
        yOff = rad * Math.sin(f * Math.PI * 2.0);

        double x = Math.abs((c.x + xOff)) % (double) w;
        double y = Math.abs((c.y + yOff)) % (double) h;
        x = (x < 0 ? w + x : x);
        y = (y < 0 ? h + y : y);

        opG.setColor(c.col);
        opG.fillRect((int) (x - r), (int) (y - r), (int) (r * 2), (int) (r * 2));
    }

    private void drawBorder() {
        opG.setColor(fg);
        double bordermm = 1.0;
        double border = (dpi * (frSc*bordermm / i2mm));
        double rad = (dpi * (frSc*radmm / i2mm));
        Area areaI = null;
        if (drawBorder) {
            double r1 = 0;
            double r2 = 0;
            if (borderRound) {
                r1 = rad * 2;
                r2 = (rad - border) * 2;
            } else {
                r1 = 0;
                r2 = 0;
            }

            Shape shape = new RoundRectangle2D.Double(0, 0, w, h, r1,
                    r1);
            Area area = new Area(shape);
            Shape shapeI = new RoundRectangle2D.Double(border, border, w - 2
                    * border, h - 2 * border,r2, r2);
            areaI = new Area(shapeI);
            area.subtract(areaI);
            opG.setClip(area);
            opG.fillRect(0, 0, w, h);
            opG.setClip(null);
        }
    }

    private void drawTornado(int frame, int n) {
        if (n > 0) {
            return;
        }
        TreeMap<Double, ArrayList<Object>> ordered = new TreeMap<Double, ArrayList<Object>>();
        double hStretchF = 2.0;
        for (int nn = 0; nn < num; nn++) {
            Center c = centers.get(nn);
            double lx = c.x;
            double ly = 0;
            double cx = w * 0.5;
            double r = c.rad * w * 0.65;
            double aOff = c.off * 180;
            double yOff = -c.off * h / 2;
            double aInc = 0.25 * c.rad / frSc;
            double repeatAng = 180 * c.off2;
            double aEn = repeatAng * (1 - ((double) frame / (double) frames));
            double aSt = repeatAng
                    * (1 - (((double) frame + 1) / (double) frames));
            for (double a = 0; a < h * hStretchF; a = a + 1) {
                double ang = aOff + (a * aInc);
                double rads2 = Math.toRadians(ang);
                double ye = a + yOff;
                double hf = (1 - (ye / (double) h));
                double xe = cx + hf * (r * Math.sin(rads2));
                double aMod = (a * aInc) % repeatAng;
                if (aMod >= aSt && aMod < aEn) {
                    double zF = (1 + Math.cos(rads2));
                    ordered.put(zF, new ArrayList<Object>());
                    Point2D lp = new Point2D.Double(lx, ly);
                    Point2D p = new Point2D.Double(xe, ye);
                    ordered.get(zF).add(lp);
                    ordered.get(zF).add(p);
                    ordered.get(zF).add(c);
                }
                lx = xe;
                ly = ye;
            }
        }
        for (Double key : ordered.keySet()) {
            Point2D lp = (Point2D) ordered.get(key).get(0);
            Point2D p = (Point2D) ordered.get(key).get(1);
            Center c = (Center) ordered.get(key).get(2);
            double hf = (1 - (lp.getY() / ((double) h * 1.1)));
            double zF = key * hf;
            opG.setColor(c.col);
            int str = (int)(frSc * 75) + (int) (zF * (frSc * 75 * c.off));
            setStroke(str, CAP, JOIN);
            opG.drawLine((int) lp.getX(), (int) lp.getY(), (int) p.getX(),
                    (int) p.getY());
        }
    }

    private void drawWind(int frame, int n) {
        if (n > 0) {
            return;
        }
        TreeMap<Double, ArrayList<Object>> ordered = new TreeMap<Double, ArrayList<Object>>();
        for (int nn = 0; nn < num; nn++) {
            Center c = centers.get(nn);
            double lx = 0;
            double ly = c.y;
            double cy = c.y;
            double r = c.rad * h * 0.15;
            double aOff = c.off * 180;
            double xOff = -c.off * w / 2;
            double aInc = 0.25 * c.rad / frSc;
            double repeatAng = 270 * c.off2;
            double aEn = repeatAng * (1 - ((double) frame / (double) frames));
            double aSt = repeatAng
                    * (1 - (((double) frame + 1) / (double) frames));
            for (double a = 0; a < w * 2; a = a + 1) {
                double ang = aOff + (a * aInc);
                double rads2 = Math.toRadians(ang);
                double xe = a + xOff;
                double ye = cy + (r * Math.sin(rads2));
                double aMod = (a * aInc) % repeatAng;
                if (aMod >= aSt && aMod < aEn) {
                    double zF = (1 + Math.cos(rads2));
                    ordered.put(zF, new ArrayList<Object>());
                    Point2D lp = new Point2D.Double(lx, ly);
                    Point2D p = new Point2D.Double(xe, ye);
                    ordered.get(zF).add(lp);
                    ordered.get(zF).add(p);
                    ordered.get(zF).add(c);
                }
                lx = xe;
                ly = ye;
            }
        }
        for (Double key : ordered.keySet()) {
            Point2D lp = (Point2D) ordered.get(key).get(0);
            Point2D p = (Point2D) ordered.get(key).get(1);
            Center c = (Center) ordered.get(key).get(2);
            double zF = c.rad;
            // float a = 0.5f + (0.5f * ((float) zF) / 2f);
            // Color col = getFadedColor(c, a);
            // opG.setColor(col);
            opG.setColor(c.col);
            int str = (int)(frSc * 50) + (int) (zF * (frSc * 200));
            setStroke(str, CAP, JOIN);
            opG.drawLine((int) lp.getX(), (int) lp.getY(), (int) p.getX(),
                    (int) p.getY());
        }
    }

    private void drawSpin(int frame, int n) {
        int xc = w / 2;
        int yc = h / 2;
        Center c = centers.get(n);
        Color col = c.col;
        opG.setColor(col);
        int str = (int) (frSc * 16 * num);
        setStroke(str, CAP, JOIN);
        double f = ((double) (frame + 1)) / ((double) frames);
        double nn = ((double) (n + 1)) / ((double) num);
        int nT = 6;
        double rad = 0.707 * w * nn;
        double x = xc - rad;
        double y = yc - rad;
        if (n < nT) {
            double numRepeats = 2 + n;
            double repeatAng = (360 / numRepeats);
            double aSt = 0;
            double aEn = 360;
            boolean rev = (n % 2) == 0;
            if (rev) {
                aSt = 360;
                aEn = 0;
                repeatAng = -repeatAng;
            }
            double extAng = repeatAng * c.off2;
            double off = (f * repeatAng + c.off * 360);
            for (double a = aSt; (!rev && a < aEn) || (rev && a > aEn); a = a
                    + repeatAng) {
                int aInc = 2;
                double aaSt = (a + off);
                double aaEn = (a + off + extAng);
                if (rev) {
                    aInc = -aInc;
                }
                for (double aa = aaSt; (!rev && aa <= aaEn)
                        || (rev && aa > aaEn); aa = aa + aInc) {
                    float fade = (float) ((aa - aaSt) / extAng);
                    Color colf = getFadedColor(c, fade);
                    opG.setColor(colf);
                    opG.drawArc((int) x, (int) y, (int) rad * 2, (int) rad * 2,
                            (int) aa, aInc);
                }
            }
        } else {
            double off = str * 0.6;
            double nR = (double) (str) + w * 0.707 * (nT - 1) / ((double) num);
            Shape circle = new Ellipse2D.Double(xc - nR - off, yc - nR - off,
                    (nR + off) * 2, (nR + off) * 2);
            Area circleA = new Area(circle);
            Shape whole = new Rectangle2D.Double(0, 0, w, h);
            Area wholeA = new Area(whole);
            wholeA.subtract(circleA);
            opG.setClip(wholeA);
            for (double ang = 0; ang < Math.PI * 2; ang = ang + Math.PI
                    * 0.0625) {
                double irStep = 0.11;
                for (double iir = 0.25; iir < 1.25; iir = iir + irStep) {
                    double st = (f * irStep);
                    double ir = w * (iir + st);
                    double or = w * (iir + st + 0.025);
                    double x1 = xc + ir * Math.cos(ang);
                    double y1 = yc + ir * Math.sin(ang);
                    double x2 = xc + or * Math.cos(ang);
                    double y2 = yc + or * Math.sin(ang);
                    opG.setColor(centers.get(nT + 1).col);
                    opG.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                }
            }
            opG.setClip(null);
        }
    }

    private void drawSine(int frame, int n) {
        if (n > 0) {
            return;
        }
        TreeMap<Double, ArrayList<Object>> ordered = new TreeMap<Double, ArrayList<Object>>();
        for (int nn = 0; nn < num; nn++) {
            Center c = centers.get(nn);
            double lx = 0;
            double ly = c.y;
            double cy = h * 0.5;
            double r = c.rad * h * 0.45;
            double aOff = c.off * 180;
            double xOff = -c.off * w / 2;
            double aInc = 0.25 * c.rad / frSc;
            double repeatAng = 180 * c.off2;
            double aEn = repeatAng * (1 - ((double) frame / (double) frames));
            double aSt = repeatAng
                    * (1 - (((double) frame + 1) / (double) frames));
            for (double a = 0; a < w * 2; a = a + 1) {
                double ang = aOff + (a * aInc);
                double rads2 = Math.toRadians(ang);
                double xe = a + xOff;
                double ye = cy + (r * Math.sin(rads2));
                double aMod = (a * aInc) % repeatAng;
                if (aMod >= aSt && aMod < aEn) {
                    double zF = (1 + Math.cos(rads2));
                    ordered.put(zF, new ArrayList<Object>());
                    Point2D lp = new Point2D.Double(lx, ly);
                    Point2D p = new Point2D.Double(xe, ye);
                    ordered.get(zF).add(lp);
                    ordered.get(zF).add(p);
                    ordered.get(zF).add(c);
                }
                lx = xe;
                ly = ye;
            }
        }
        for (Double key : ordered.keySet()) {
            Point2D lp = (Point2D) ordered.get(key).get(0);
            Point2D p = (Point2D) ordered.get(key).get(1);
            Center c = (Center) ordered.get(key).get(2);
            double zF = key;
            // float a = 0.5f + (0.5f * ((float) zF) / 2f);
            // Color col = getFadedColor(c, a);
            // opG.setColor(col);
            opG.setColor(c.col);
            int str = (int)(frSc * 75) + (int) (zF * (frSc * 150 * c.off));
            setStroke(str, CAP, JOIN);
            opG.drawLine((int) lp.getX(), (int) lp.getY(), (int) p.getX(),
                    (int) p.getY());
        }
    }

    private Color getFadedColor(Center c, float a) {
        return new Color((float) (c.col.getRed()) / 255f,
                ((float) (c.col.getGreen())) / 255f,
                ((float) (c.col.getBlue())) / 255f, a);
    }

    private void drawRings(int frame, int i) {
        Center c = centers.get(i);
        double f = (((double) frame) / frames);
        double fr = 1 - f;
        double ii = ((double) i) / (double) num;
        double frr = ((fr + ii) % 1.0);
        double ffr = frSc * 500 * frr;
        double rr = c.rad * ffr;
        double x = c.x - rr;
        double y = c.y - rr;
        // opG.setColor(c.col);
        double strF = (1 - frr);
        int str = (int) (frSc * 50 + (strF * frSc * 200 * c.rad));
        setStroke(str, CAP, JOIN);
        float fade = 0.5f + (0.5f * (float) strF);
        Color col = getFadedColor(c, 1);
        opG.setColor(col);
        opG.drawArc((int) x, (int) y, (int) rr * 2, (int) rr * 2, 0, 360);
    }

    private void drawFlowers(int frame, int n) {
        Center c = centers.get(n);
        opG.setColor(c.col);
        double f = (((double) frame) / (frames));
        double rad = frSc * 400 * c.rad;
        double petals = 2 + (int) ((c.rad * 8) - 2);
        double aInc = Math.PI * 2 / petals;
        double ang = 2 * Math.PI * c.off;
        double rOff = 0;
        double nn = ((double) n) / (double) num;
        double fnn = ((f + nn) % 1.0);
        double rf = fnn * 0.85;
        double ir = rOff + rad * rf;
        double f2 = (((double) frame + 1) / (frames));
        double fnn2 = ((f2 + nn) % 1.0);
        if (fnn2 < fnn) {
            fnn2 = fnn;
        }
        double rf2 = fnn2;
        double or = rOff + rad * rf2;
        double strf = 0.5 + 0.5 * (fnn);
        for (double a = 0; a < 2 * Math.PI; a = a + aInc) {
            double aa = a + ang;
            int str = (int) (frSc * 250 * c.rad * strf);
            setStroke(str, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            int x1 = (int) (c.x + (ir * Math.cos(aa)));
            int y1 = (int) (c.y + (ir * Math.sin(aa)));
            int x2 = (int) (c.x + (or * Math.cos(aa)));
            int y2 = (int) (c.y + (or * Math.sin(aa)));
            opG.drawLine(x1, y1, x2, y2);
        }
    }

    private void initCenters() {
        System.out.println("Started initCenters...");
        ArrayList<Color> cols = (ArrayList<Color>) colors.clone();
        boolean sort = true;
        boolean reverse = false;
        for (int i = 0; i < num; i++) {
            double x = (getNextRandomDouble() * w);
            double y = (getNextRandomDouble() * h);
            double r = h > w ? w : h;
            double rad = -1;
            double off = getNextRandomDouble();
            double off2 = getNextRandomDouble();
            Color cc = null;
            if (isType(FLOWERS)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                while (isInside(x, y, rad, frSc * 300)) {
                    x = (getNextRandomDouble() * w);
                    y = (getNextRandomDouble() * h);
                    rad = ((0.5 + 0.5 * getNextRandomDouble()));
                }
                off = (getNextRandomDouble());
            } else if (isType(SPIN)) {
                sort = false;
                rad = getNextRandomDouble();
                off = getNextRandomDouble();
                off2 = (0.4 + 0.4 * getNextRandomDouble());
                cc = colors.get(i);
            } else if (isType(SINE)) {
                rad = ((getNextRandomDouble()));
                off = ((0.5 + 0.5 * getNextRandomDouble()));
                off2 = (0.5 + 0.5 * getNextRandomDouble());
            } else if (isType(TORN)) {
                rad = ((getNextRandomDouble()));
                off = ((0.5 + 0.5 * getNextRandomDouble()));
                off2 = (0.5 + 0.5 * getNextRandomDouble());
            } else if (isType(WIND)) {
                rad = ((getNextRandomDouble()));
                off = ((0.5 + 0.5 * getNextRandomDouble()));
                off2 = (0.5 + 0.5 * getNextRandomDouble());
            } else if (isType(DROPS)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                off = (getNextRandomDouble());
                while (isInside(x, y, rad, frSc * 300)) {
                    x = (getNextRandomDouble() * w);
                    y = (getNextRandomDouble() * h);
                    rad = ((0.5 + 0.5 * getNextRandomDouble()));
                }
            } else if (isType(LINEAR)) {
                rad = ((0.25 + 0.75 * getNextRandomDouble()) * r * 0.5);
            } else if (isType(DOTS)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                off = ((0.25 + 0.75 * getNextRandomDouble()));
            } else if (isType(DOTS_A4)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                off = ((0.25 + 0.75 * getNextRandomDouble()));
            } else if (isType(SPIRAL)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                off = ((0.5 + 0.5 * getNextRandomDouble()));
            } else if (isType(LAND)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                off = (getNextRandomDouble());
                reverse = true;
            } else if (isType(TEXT)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                off = ((0.5 + 0.5 * getNextRandomDouble()));
            } else if (isType(PORT)) {
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
                off = (getNextRandomDouble());
                double bord = 400.0;
                x = bord + (getNextRandomDouble() * (w - (2 * bord)));
                y = bord + (getNextRandomDouble() * (h - (2 * bord)));
                while (isInside(x, y, rad, frSc * off)) {
                    x = bord + (getNextRandomDouble() * (w - (2 * bord)));
                    y = bord + (getNextRandomDouble() * (h - (2 * bord)));
                    rad = ((0.5 + 0.5 * getNextRandomDouble()));
                }
            } else if (isType(VIRGA)) {
                sort = false;
                rad = ((0.5 + 0.5 * getNextRandomDouble()));
            } else if (isType(CUFF)) {
                rad = ((getNextRandomDouble()));
                off = ((getNextRandomDouble()));
                off2 = (getNextRandomDouble());
            }
            if (cc == null) {
                cc = getRandomColor();
            }
            Center cen = new Center(x, y, rad, cc, off, off2);
            centers.add(cen);
            // System.out.println(cen);
        }
        this.colors = cols;
        if (sort) {
            Collections.sort(centers);
        }
        if (reverse) {
            Collections.reverse(centers);
        }
        System.out.println("Finished initCenters");
    }

    private void initStatics() {
        System.out.println("Started initStatics...");
        if (isType(DROPS)) {
            num = 20;
            rnd = 9;
        } else if (isType(FLOWERS)) {
            num = 20;
            rnd = 8;
        } else if (isType(LINEAR)) {
            num = 100;
        } else if (isType(DOTS)) {
            num = 25;
            rnd = 12;
        } else if (isType(DOTS_A4)) {
            num = 50;
            rnd = 12;
        } else if (isType(LAND)) {
            num = 30;
            rnd = 2;
        } else if (isType(SPIRAL)) {
            num = 10;
        } else if (isType(SINE)) {
            num = 10;
        } else if (isType(TORN)) {
            num = 30;
        } else if (isType(WIND)) {
            num = 20;
        } else if (isType(SPIN)) {
            colStr = "070B5D,345BA1,118877,558811,867D33,A67D33,881122,A5333B,222222,A56718,A26132,00A898,667755,558811,118877,552299";
            num = 10;
            rnd = 7;
        } else if (isType(TEXT)) {
            num = 10;
        } else if (isType(PORT)) {
            num = 20;
            rnd = 9;
        } else if (isType(VIRGA)) {
            num = 10000;
            rnd = 9;
        } else if (isType(CUFF)) {
            num = 10;
            rnd = 9;
            colStr = "78C4BD,345BA1,41A332,A5333B,ecc82d,cbaf8a,AA79F2,9ca4a4,881122,222222";
        } else if (isType(BLANK)) {
            num = 15;
            rnd = 9;
        }
        rand = new Random(rnd);
        System.out.println("Finished initStatics.");
    }

    private boolean isInside(double x, double y, double r, double rm) {
        Shape el = new Ellipse2D.Double(x - r * rm, y - r * rm, 2 * r * rm, 2
                * r * rm);
        for (Center cen : centers) {
            Shape elc = new Ellipse2D.Double(cen.x - cen.rad * rm, cen.y
                    - cen.rad * rm, 2 * cen.rad * rm, 2 * cen.rad * rm);
            if (elc.intersects(el.getBounds2D())) {
                return true;
            }
        }
        return false;
    }

    private boolean isType(String t) {
        return t.equals(type);
    }

    private Color getRandomColor() {
        int i = (int) (getNextRandomDouble() * colors.size());
        Color col = colors.get(i);
        colors.remove(i);
        return col;
    }

    private void initColors() {
        StringTokenizer st = new StringTokenizer(colStr, ",");
        int i = 0;

        while (i < num) {
            while (st.hasMoreElements()) {
                String hex = st.nextElement().toString();
                colors.add(Color.decode("#" + hex));
                i++;
            }
            st = new StringTokenizer(colStr, ",");
        }
    }

    private int getNextRandomInt() {
        return rand.nextInt();
    }

    private double getNextRandomDouble() {
        return rand.nextDouble();
    }

    private class Center implements Comparable<Center> {
        double x;
        double y;
        double rad;
        Color col;
        double off;
        double off2;

        public Center(double x, double y, double rad, Color col, double off,
                      double off2) {
            this.x = x;
            this.y = y;
            this.rad = rad;
            this.col = col;
            this.off = off;
            this.off2 = off2;
        }

        @Override
        public int compareTo(Center o) {
            Double r1 = this.rad;
            Double r2 = o.rad;
            return r1.compareTo(r2);
        }

        @Override
        public String toString() {
            String ret = "";
            ret = ((int) this.x) + "," + ((int) this.y) + " " + this.rad;
            // + " "
            // + this.col.getRed() + "," + this.col.getGreen() + "," //
            // + this.col.getBlue();
            return ret;
        }
    }

    /**
     * *
     *
     * @throws IOException *************************************************************
     *                     ***********
     */
    private void drawPortrait(int frame, int i) throws IOException {
        if (i > 0) {
            return;
        }
        String src = dir + type
                + "/";
        String flName = src + "virga.jpg";
        File inputFile = new File(flName);
        BufferedImage ipImage = ImageIO.read(inputFile);
        opG.drawImage(ipImage, 0, 0, null);
        double bord = 400;
        for (int n = 0; n < num; n++) {
            Center c = centers.get(n);
            double f = (((double) frame) / frames);
            double fr = 1 - f;
            double ii = ((double) n) / (double) num;
            double frr = ((fr + ii) % 1.0);
            double ffr = (bord / 2.0) + frSc * (bord / 2.0) * frr;
            double rr = c.rad * ffr;
            double x = c.x - rr;
            double y = c.y - rr;
            double rs = c.rad * (bord / 2.0);
            double s = ffr / rs;
            BufferedImage sub = ipImage.getSubimage((int) x, (int) y,
                    (int) rr * 2, (int) rr * 2);
            AffineTransform at = new AffineTransform();
            AffineTransform tr = AffineTransform.getTranslateInstance(-rr, -rr);
            AffineTransform sc = AffineTransform.getScaleInstance(s, s);
            // AffineTransform ro = AffineTransform.getRotateInstance(Math.PI *
            // 2
            // * frr);
            AffineTransform tr2 = AffineTransform.getTranslateInstance(rr + x,
                    rr + y);
            at.concatenate(tr2);
            at.concatenate(sc);
            at.concatenate(tr);
            double rrs = rr;
            double xx = c.x - rrs;
            double yy = c.y - rrs;
            Shape clip = new Ellipse2D.Double(xx, yy, rrs * 2, rrs * 2);
            opG.setClip(clip);
            opG.drawImage(sub, at, null);
            opG.setClip(null);
            // opG.drawArc((int) x, (int) y, (int) rr * 2, (int) rr * 2, 0, //
            // 360);
        }
    }

    private void drawLinear(int frame, int i) {
        double fr = ((double) frame) / frames;
        double ang = Math.PI / 3;
        double cos = Math.cos(ang);
        double sin = Math.sin(ang);
        Center c = centers.get(i);
        double rF = 5;
        double rx = rF*c.rad * cos;
        double ry = rF*c.rad * sin;
        double x = c.x;
        double y = c.y;
        double radF = 200.0 * c.rad / h;
        opG.setStroke(new BasicStroke((int) (((double) 1) * radF),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        opG.setColor(c.col);
        int nRad = (int) (w / rx);
        for (int m = -nRad; m < nRad; m++) {
            int xx = (int) (x + (rx * m));
            int yy = (int) (y + (ry * m));
            int x1 = (int) (xx - (fr * rx));
            int y1 = (int) (yy - (fr * ry));
            int x2 = (int) (xx - (fr * rx + (rx / frames)));
            int y2 = (int) (yy - (fr * ry + (ry / frames)));
            opG.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawDots(int frame, int n) {
        double fr = ((double) frame) / frames;
        double ang = 60 * Math.PI * 2 / 360;
        double cos = Math.cos(ang);
        double sin = Math.sin(ang);
        Center c = centers.get(n);
        double rx = 1.0 * w * c.rad * cos;
        double ry = 1.0 * h * c.rad * sin;
        int th = (int) (frSc * 500.0 * c.off);
        opG.setColor(c.col);
        double x = -w / 2 + 2 * c.x;
        double y = c.y;
        int nRad = (int) (w / rx);
        for (int m = -nRad * 3; m < nRad * 3; m++) {
            int xx = (int) (x + (rx * m));
            int yy = (int) (y + (ry * m));
            int x1 = (int) (xx - (fr * rx));
            int y1 = (int) (yy - (fr * ry));
            opG.fillOval(x1, y1, th, th);
        }
    }

    private void drawLand(int frame, int n) {
        // n = num - 1;
        double f = 1 - ((double) frame + 1) / frames;
        Center c = centers.get(n);
        double x = c.x;
        double y = c.y;
        double rad = c.rad;
        double off = c.off;
        Color col = c.col;
        opG.setColor(col);
        double cy = 3 * h / 4;
        double ySp = h * 0.01;
        double hT = cy;
        double hB = h - cy;
        for (double i = 1; i < 2; i++) {
            double ff = ((f + off) * i) % 1.0;
            double f2 = ff * ff;
            double yC = cy + (y < cy ? -f2 * hT - ySp : f2 * hB + ySp);
            double radMin = 0.05 * h;
            double radY = radMin + (0.2 * rad * f2 * h);
            double radX = radMin + (1 + f2 * 0.5) * 0.1 * rad * w;
            double x1 = x - (radX / 2.0);
            double y1 = yC - (radY / 2.0);
            // opG.fillOval((int) x1, (int) y1, (int) radX, (int) radY);
            if (yC < cy) {
                opG.fillArc((int) x1, (int) y1, (int) radX, (int) radY, 0, 180);
                double radYB = radY * ff;
                opG.fillArc((int) x1, (int) (yC - (radYB / 2.0)), (int) radX,
                        (int) (radYB), 180, 180);
            } else {
                opG.fillArc((int) x1, (int) y1, (int) radX, (int) radY, 180,
                        180);
                double radYB = radY * ff;
                opG.fillArc((int) x1, (int) (yC - (radYB / 2.0)), (int) radX,
                        (int) (radYB), 0, 180);
            }
        }
    }

    private void drawSpiral(int frame, int n) {
        int xc = w / 2;
        int yc = h / 2;
        // n = 1;
        Center c = centers.get(n);
        Color col = c.col;
        double rInc = frSc * 2 * c.off;
        double lx = xc;
        double ly = yc;
        double r = 1;
        double aInc = 1;
        double bAng = 360 * 2;
        double ff1 = ((double) frame) / ((double) frames);
        double aOff = 1 * ff1 * 360 + 90 * c.off;
        opG.setColor(col);
        for (double a = 0; a < bAng * 10; a = a + aInc) {
            double rads2 = Math.toRadians(a + aOff + aInc);
            double xe = r * Math.cos(rads2);
            double ye = r * Math.sin(rads2);
            xe = xe + xc;
            ye = ye + yc;
            int str = (int) (0.1 * r * c.rad);
            opG.setStroke(new BasicStroke(str, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_BEVEL));
            opG.drawLine((int) lx, (int) ly, (int) xe, (int) ye);
            lx = xe;
            ly = ye;
            // r = r + rInc;
            r = Math.pow(r, 1.5);
        }
    }

    private void addAlignmentText(int frame) throws FontFormatException,
            IOException {
        if (type.equals(CUFF)) {
            return;
        }
        String fName = fontFile;
        InputStream is = new BufferedInputStream(new FileInputStream(fName));
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        float fontH = (float) frSc * 100f;
        font = font.deriveFont(fontH);
        opG.setFont(font);
        opG.setColor(fontCol);
        FontRenderContext frc = opG.getFontRenderContext();
        String s = "";
        if (frame < frames / 2) {
            s = s1;
        } else {
            s = s2;
        }
        GlyphVector gv = font.createGlyphVector(frc, s);
        Rectangle2D rect = font.getStringBounds(s, opG.getFontRenderContext());
        Shape glyph = gv.getOutline();
        AffineTransform tr = AffineTransform.getTranslateInstance(
                (w - rect.getWidth()) / 2.0, h - rect.getHeight() * 1);
        // 1.5
        AffineTransform at = new AffineTransform();
        at.concatenate(tr);
        Shape transformedGlyph = at.createTransformedShape(glyph);
        opG.fill(transformedGlyph);
    }

    private void drawText(int frame, int n) throws FontFormatException,
            IOException {
        if (n > 0) {
            return;
        }
        int cx = w / 2;
        int cy = h / 2;
        opG.setColor(colors.get(1));
        double f = ((double) frame) / ((double) frames);
        double sc = Math.cos(f * Math.PI * 2);
        String fName = "NEWTOWN.TTF";
        InputStream is = new BufferedInputStream(new FileInputStream(fName));
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        float fontH = 500f;
        font = font.deriveFont(fontH);
        opG.setFont(font);
        // opG.drawString("SANJAY", cx, cy);
        AffineTransform at = new AffineTransform();
        FontRenderContext frc = opG.getFontRenderContext();
        String s = "VIRGA";
        GlyphVector gv = font.createGlyphVector(frc, s);
        Rectangle2D rect = font.getStringBounds(s, opG.getFontRenderContext());
        Shape glyph = gv.getOutline();
        double hr = 0.762;
        AffineTransform tr2 = AffineTransform.getTranslateInstance(
                -(rect.getWidth() / 2.0), (rect.getHeight() / (2.0 * hr)));
        AffineTransform sca = AffineTransform.getScaleInstance(1, sc * 4);
        AffineTransform tr = AffineTransform.getTranslateInstance(cx, cy);
        at.concatenate(tr);
        at.concatenate(sca);
        at.concatenate(tr2);
        Shape transformedGlyph = at.createTransformedShape(glyph);
        opG.fill(transformedGlyph);
        opG.draw(transformedGlyph.getBounds2D());
        opG.drawLine(0, cy, w, cy);
        // getPaths(font, frc, gv);
    }

    private void getPaths(Font font, FontRenderContext frc, GlyphVector gv) {
        ArrayList<Point2D.Float> pts = new ArrayList<Point2D.Float>();
        Shape g = gv.getGlyphOutline(0);
        PathIterator path = g.getPathIterator(null);
        while (!path.isDone()) {
            float[] arg = new float[6];
            path.currentSegment(arg);
            // System.out.println(gg + ": " + arg[0] + "," + arg[1]);
            System.out.println("'" + (int) arg[0] + "," + (int) arg[1]);
            Point2D.Float p = new Point2D.Float(arg[0], arg[1]);
            pts.add(p);
            path.next();
        }
        String s2 = "SANJA";
        GlyphVector gv2 = font.createGlyphVector(frc, s2);
        g = gv2.getGlyphOutline(0);
        path = g.getPathIterator(null);
        while (!path.isDone()) {
            float[] arg = new float[6];
            path.currentSegment(arg);
            // System.out.println(gg + ": " + arg[0] + "," + arg[1]);
            System.out.println("'" + (int) arg[0] + "," + (int) arg[1]);
            Point2D.Float p = new Point2D.Float(arg[0], arg[1]);
            pts.add(p);
            path.next();
        }
    }
}

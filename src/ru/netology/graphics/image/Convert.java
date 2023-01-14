package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Convert implements TextGraphicsConverter {

    private int maxWidth = 0;
    private int maxHeight = 0;
    private double maxRatio = 0;

    private TextColorSchema schema = new ColorSchema();

    public Convert() {

    }

    public Convert(int maxWidth, int maxHeight, int maxRatio) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxRatio = maxRatio;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int newWidth = 0;
        int newHeight = 0;
        int arrColor[] = new int[3];
        double imgRatio = (double) img.getWidth() / img.getHeight();

        if (maxRatio != 0 && maxRatio < imgRatio) {
            throw new BadImageSizeException(imgRatio, maxRatio);
        }

        if (maxWidth == 0 && maxHeight == 0) {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        } else if (maxWidth == 0) {
            double newRation = (double) img.getHeight() / maxHeight;
            newWidth = (int) (img.getWidth() / newRation);
            newHeight = (int) (img.getHeight() / newRation);
        } else if (maxHeight == 0) {
            double newRation = (double) img.getWidth() / maxWidth;
            newWidth = (int) (img.getWidth() / newRation);
            newHeight = (int) (img.getHeight() / newRation);
        } else if (maxWidth < img.getWidth() || maxHeight < img.getHeight()) {

            boolean isWidth = maxWidth >= img.getWidth();
            boolean isHeight = maxHeight >= img.getHeight();

            if (!isWidth && !isHeight) {
                double ratioWidth = (double) img.getWidth() / maxWidth;
                double ratioHeight = (double) img.getHeight() / maxHeight;
                double newRation = 0d;
                boolean boolMinRationWidth = ratioWidth < ratioHeight;
                if (boolMinRationWidth) {
                    if (img.getHeight() / ratioWidth <= maxHeight) {
                        newRation = ratioWidth;
                    } else {
                        if (img.getWidth() / ratioHeight <= maxWidth) {
                            newRation = ratioHeight;
                        } else {
                            newRation = ratioWidth;
                        }
                    }
                } else {
                    if (img.getWidth() / ratioHeight <= maxWidth) {
                        newRation = ratioHeight;
                    } else {
                        newRation = ratioWidth;
                    }
                }

                newWidth = (int) (img.getWidth() / newRation);
                newHeight = (int) (img.getHeight() / newRation);


            } else if (!isWidth) {
                double newRation = (double) img.getWidth() / maxWidth;
                newWidth = (int) (img.getWidth() / newRation);
                newHeight = (int) (img.getHeight() / newRation);
            } else {
                double newRation = (double) img.getHeight() / maxHeight;
                newWidth = (int) (img.getWidth() / newRation);
                newHeight = (int) (img.getHeight() / newRation);
            }

        } else {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();

        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder stringBuilder = new StringBuilder();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, arrColor)[0];
                stringBuilder.append(schema.convert(color))
                        .append(schema.convert(color));

            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

}

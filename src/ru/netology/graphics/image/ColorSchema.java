package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema {

    private char[] symbol = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        return color <= 32 ? symbol[0] :
                color <= 64 ? symbol[1] :
                color <= 96 ? symbol[2] :
                color <= 128 ? symbol[3] :
                color <= 160 ? symbol[4] :
                color <= 192 ? symbol[5] :
                color <= 224 ? symbol[6] : symbol[7];
    }
}

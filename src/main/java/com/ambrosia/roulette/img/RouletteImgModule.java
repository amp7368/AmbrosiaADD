package com.ambrosia.roulette.img;

import apple.lib.modules.AppleModule;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class RouletteImgModule extends AppleModule {

    private static RouletteImgModule instance;

    public RouletteImgModule() {
        instance = this;
    }

    public static RouletteImgModule get() {
        return instance;
    }

    @Override
    public void onLoad() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            File[] folder = getFile("Font").listFiles();
            if (folder == null) {
                throw new RuntimeException(getFile("Font") + " is not found");
            }
            for (File file : folder) {
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, file));
            }
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        RouletteImgBackgroundTable.load();
        System.exit(0);
    }

    @Override
    public String getName() {
        return "Image";
    }
}

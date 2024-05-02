package com.ambrosia.roulette.img;

import com.ambrosia.roulette.Roulette;
import com.ambrosia.roulette.table.RouletteSpace;
import com.ambrosia.roulette.table.RouletteSpaceColor;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.PngWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import javax.swing.border.StrokeBorder;

public class RouletteImgBackgroundTable {

    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;
    private static final Color BACKGROUND_COLOR = new Color(0x228C22);
    private static final int SCALE = 1;

    public static void load() {
        ImmutableImage img = ImmutableImage.create(WIDTH, HEIGHT);
        img = img.fill(BACKGROUND_COLOR);

        img = writeSpaces(img);
        PngWriter writer = new PngWriter().withMaxCompression();
        try {
            File file = RouletteImgModule.get().getFile("BettingTable.png");
            img.forWriter(writer).write(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ImmutableImage writeSpaces(ImmutableImage img) {
        int fontSize = 40;
        Font spaceFont = font(fontSize);

        BufferedImage awt = img.awt();
        Graphics2D graphics = (Graphics2D) awt.getGraphics();

        FontMetrics fontMetrics = graphics.getFontMetrics(spaceFont);
        int spaceWidth = fontSize * 3;
        int spaceHeight = fontSize * 2;

        for (RouletteSpace space : Roulette.TABLE.spaces(false)) {
            int x = space.col() * spaceWidth;
            int y = space.row() * spaceHeight;

            graphics.setStroke(new BasicStroke(3));
            graphics.setColor(Color.WHITE);
            graphics.drawRect(x, y, spaceWidth, spaceHeight);

            graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            new StrokeBorder(new BasicStroke(0), null);
            graphics.setColor(spaceColor(space.color()));

            float padding = .125f;
            int ovalX = (int) (x + padding * spaceWidth);
            int ovalY = (int) (y + padding * spaceHeight);
            int ovalWidth = (int) (spaceWidth - padding * spaceWidth * 2);
            int ovalHeight = (int) (spaceHeight - padding * spaceHeight * 2);
            graphics.fillOval(ovalX, ovalY, ovalWidth, ovalHeight);

            String text = String.valueOf(space);
            AttributedString textStyle = new AttributedString(text);
            textStyle.addAttribute(TextAttribute.FONT, spaceFont);
            textStyle.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
            textStyle.addAttribute(TextAttribute.JUSTIFICATION, 0f);

            float xi = x + .5f * spaceWidth - fontMetrics.stringWidth(text) / 2f;
            float yi = y + .5f * spaceHeight + fontMetrics.getMaxAscent() / 2f;
            graphics.drawString(textStyle.getIterator(), xi, yi);
        }
        return ImmutableImage.fromAwt(awt);
    }

    private static Color spaceColor(RouletteSpaceColor color) {
        return new Color(switch (color) {
            case BLACK -> 0x00;
            case RED -> 0xff0000;
            case GREEN -> 0xffffff;
        });
    }

    private static Font font(int fontSize) {
        return new Font("Roboto", Font.PLAIN, fontSize * SCALE);
    }
}

package view;

import rasterize.*;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Panel extends JPanel {
    private RasterBufferedImage raster;
    private static final int FPS = 30;

    Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new RasterBufferedImage(width, height);

        raster.setClearColor(Color.BLACK);
        setLoop();
    }

    public RasterBufferedImage getRaster() {
        return raster;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }

    public void resize() {
        if (getWidth() < 1 || getHeight() < 1)
            return;
        if (getWidth() <= raster.getWidth() && getHeight() <= raster.getHeight())
            return;
        RasterBufferedImage newRaster = new RasterBufferedImage(getWidth(), getHeight());

        newRaster.draw(raster);
        raster = newRaster;
    }

    private void setLoop() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, FPS);
    }

    public void clear() {
        raster.clear();
    }
}

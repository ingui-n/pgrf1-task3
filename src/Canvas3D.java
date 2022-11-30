import rasterize.*;
import render.WireRenderer;
import solids.Cube;
import solids.Solid;
import transforms.*;
import view.Panel;
import view.Window;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas3D {
    private final Panel panel;
    private LineRasterizer lineRasterizer;
    private RasterBufferedImage raster;
    private WireRenderer wireRenderer;
    private Cube cube;
    private Mat4 proj;
    private Camera camera;
    private double cameraSpeed = 0.3d;
    private int ox, oy;

    public Canvas3D(int width, int height) {
        Window window = new Window(width, height);
        panel = window.getPanel();
        raster = panel.getRaster();
        lineRasterizer = new FilledLineRasterizer(raster);

        //todo add more shapes
        printCube();

        window.setVisible(true);
        cameraListeners();
        resizeListener();
    }

    private void printCube() {
        cube = new Cube();

        camera = new Camera(new Vec3D(0, -3, 0), Math.toRadians(90), Math.toRadians(0), 1, true);//1 person
//        camera = new Camera(new Vec3D(0, 0, 0), Math.toRadians(90), Math.toRadians(0), 3, false);//3 person
        proj = new Mat4PerspRH(Math.toRadians(60), panel.getHeight() / (double) panel.getWidth(), 0.1, 300);
        wireRenderer = new WireRenderer(panel, lineRasterizer, camera.getViewMatrix(), proj);
    }

    private void cameraListeners() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.forward(cameraSpeed);
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(cameraSpeed);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(cameraSpeed);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(cameraSpeed);
                }

                display();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - ox;
                double azimuth = Math.PI * deltaX / panel.getWidth();

                int deltaY = e.getY() - oy;
                double zenith = Math.PI * deltaY / panel.getHeight();

                camera = camera.addAzimuth(azimuth);
                camera = camera.addZenith(zenith);

                ox = e.getX();
                oy = e.getY();

                display();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ox = e.getX();
                oy = e.getY();
            }
        });

        panel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == -1) {
                    camera = camera.forward(cameraSpeed);
                    display();
                } else if (e.getWheelRotation() == 1) {
                    camera = camera.backward(cameraSpeed);
                    display();
                }
            }
        });
    }

    private void resizeListener() {
        panel.addComponentListener(new ComponentAdapter() {
            /**
             * Resets raster and all rasterizerS
             * @param e the event to be processed
             */
            @Override
            public void componentResized(ComponentEvent e) {
                raster = panel.getRaster();
                lineRasterizer = new FilledLineRasterizer(raster);
                raster.clear();
                printCube();
            }
        });
    }

    private void display() {
        raster.clear();

        List<Solid> scene = new ArrayList<>();
        scene.add(cube);

        wireRenderer.setView(camera.getViewMatrix());
        wireRenderer.renderScene(scene);

        panel.repaint();
    }

    public void start() {
        display();
    }
}

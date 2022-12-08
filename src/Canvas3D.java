import rasterize.*;
import render.WireRenderer;
import solids.*;
import transforms.*;
import view.Panel;
import view.Window;

import java.awt.event.*;
import java.util.ArrayList;

public class Canvas3D {
    private final Panel panel;
    private LineRasterizer lineRasterizer;
    private RasterBufferedImage raster;
    private WireRenderer wireRenderer;
    private final Axis axis = new Axis();
    private final Cube cube = new Cube();
    private final Circle circle = new Circle();
    private final Rectangle rectangle = new Rectangle();
    private final ArrayList<Solid> solids = new ArrayList<>();
    private Camera camera;
    private final double cameraSpeed = 0.3;
    private int ox, oy;
    private double xRot = 0, yRot = 0, zRot = 0;
    private double xTran = 0, yTran = 0, zTran = 0;
    private double scale = 1;
    private boolean firstPerson = false;

    public Canvas3D(int width, int height) {
        Window window = new Window(width, height);
        panel = window.getPanel();
        raster = panel.getRaster();
        lineRasterizer = new FilledLineRasterizer(raster);
        initCamera();
        render();

        window.setVisible(true);
        panelListeners();
        resizeListener();
    }

    private void render() {
        Mat4 proj = new Mat4PerspRH(Math.toRadians(60), panel.getHeight() / (double) panel.getWidth(), 0.1, 300);
        wireRenderer = new WireRenderer(panel, lineRasterizer, camera.getViewMatrix(), proj);
    }

    private void panelListeners() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> camera = camera.forward(cameraSpeed);
                    case KeyEvent.VK_A -> camera = camera.left(cameraSpeed);
                    case KeyEvent.VK_S -> camera = camera.backward(cameraSpeed);
                    case KeyEvent.VK_D -> camera = camera.right(cameraSpeed);
                    case KeyEvent.VK_F1 -> xRot -= .1;
                    case KeyEvent.VK_F2 -> xRot += .1;
                    case KeyEvent.VK_F3 -> yRot += .1;
                    case KeyEvent.VK_F4 -> yRot -= .1;
                    case KeyEvent.VK_F5 -> zRot -= .1;
                    case KeyEvent.VK_F6 -> zRot += .1;
                    case KeyEvent.VK_F7 -> xTran -= .1;
                    case KeyEvent.VK_F8 -> xTran += .1;
                    case KeyEvent.VK_F9 -> yTran -= .1;
                    case KeyEvent.VK_F10 -> yTran += .1;
                    case KeyEvent.VK_F11 -> zTran -= .1;
                    case KeyEvent.VK_F12 -> zTran += .1;
                    case KeyEvent.VK_PLUS, KeyEvent.VK_ADD -> scale += .1;
                    case KeyEvent.VK_MINUS, KeyEvent.VK_SUBTRACT -> scale -= .1;
                    case KeyEvent.VK_L -> circle.setSelected(!circle.isSelected());
                    case KeyEvent.VK_K -> cube.setSelected(!cube.isSelected());
                    case KeyEvent.VK_J -> rectangle.setSelected(!rectangle.isSelected());
                    case KeyEvent.VK_P -> switchFirstPerson();
                }

                display();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double azimuth = Math.PI * (e.getX() - ox) / panel.getWidth();
                double zenith = Math.PI * (e.getY() - oy) / panel.getHeight();

                camera = camera.addAzimuth(azimuth).addZenith(zenith);

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
                if (e.getWheelRotation() == 1) {
                    camera = camera.backward(cameraSpeed);
                } else {
                    camera = camera.forward(cameraSpeed);
                }

                display();
            }
        });
    }

    private void switchFirstPerson() {
        firstPerson = !firstPerson;
        initCamera();
    }

    private void initCamera() {
        camera = new Camera(new Vec3D(0, 0, 0), Math.PI * 1.25, Math.PI * -0.125, 5, firstPerson);

        if (firstPerson)
            camera = camera.backward(5);
    }

    private void resizeListener() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                raster = panel.getRaster();
                lineRasterizer = new FilledLineRasterizer(raster);
                raster.clear();
                render();
                display();
            }
        });
    }

    private void display() {
        raster.clear();

        solids.add(axis);
        solids.add(circle);
        solids.add(cube);
        solids.add(rectangle);

        for (Solid solid : solids) {
            if (solid.isSelected()) {
                solid.setModel(
                        new Mat4RotXYZ(xRot, yRot, zRot)
                                .mul(new Mat4Transl(xTran, yTran, zTran))
                                .mul(new Mat4Scale(scale, scale, scale))
                );
            }
        }

        wireRenderer.setView(camera.getViewMatrix());
        wireRenderer.renderScene(solids);

        panel.repaint();
    }

    public void start() {
        display();
    }
}

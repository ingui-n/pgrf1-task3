package render;

import model.Line;
import rasterize.LineRasterizer;
import solids.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import view.Panel;

import java.util.List;

public class WireRenderer {
    private final Panel panel;
    private final LineRasterizer lineRasterizer;
    private final Mat4 proj;
    private Mat4 view;

    public WireRenderer(Panel panel, LineRasterizer lineRasterizer, Mat4 view, Mat4 proj) {
        this.panel = panel;
        this.lineRasterizer = lineRasterizer;
        this.proj = proj;
        this.view = view;
    }

    public void renderSolid(Solid solid) {
        Mat4 mvp = new Mat4(solid.getModel()).mul(view).mul(proj);//MVP pořadí!

        for (int i = 0; i < solid.getIndexBuffer().size(); i += 2) {
            int index1 = solid.getIndexBuffer().get(i);
            int index2 = solid.getIndexBuffer().get(i + 1);

            Point3D a = solid.getVertexBuffer().get(index1);
            Point3D b = solid.getVertexBuffer().get(index2);

            a = a.mul(mvp);
            b = b.mul(mvp);

            //fast clip
            if (isInView(a, b)) {
                //dehomogenization
                Point3D aDehomog = a.mul(1 / a.getW());
                Point3D bDehomog = b.mul(1 / b.getW());

                Vec3D v1 = transformToWindow(new Vec3D(aDehomog));
                Vec3D v2 = transformToWindow(new Vec3D(bDehomog));

                Line line = new Line((int) Math.round(v1.getX()), (int) Math.round(v1.getY()), (int) Math.round(v2.getX()), (int) Math.round(v2.getY()));
                line.setColor(solid.getColor(i));

                lineRasterizer.rasterize(line);
            }
        }

    }

    private Vec3D transformToWindow(Vec3D v) {
        return v
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((panel.getWidth() - 1) / 2., (panel.getHeight() - 1) / 2., 1));
    }

    public void renderScene(List<Solid> solids) {
        for (Solid solid : solids) {
            renderSolid(solid);
        }
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    private boolean isInView(Point3D a, Point3D b) {
        boolean pointA = -a.getW() <= a.getX() && a.getX() <= a.getW() && -a.getW() <= a.getY() && a.getY() <= a.getW() && 0 <= a.getZ() && a.getZ() <= a.getW();
        boolean pointB = -b.getW() <= b.getX() && b.getX() <= b.getW() && -b.getW() <= b.getY() && b.getY() <= b.getW() && 0 <= b.getZ() && b.getZ() <= b.getW();
        return pointA && pointB;
    }
}

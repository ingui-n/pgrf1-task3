package solids;

import transforms.Point3D;

public class Rectangle extends Solid {
    public Rectangle() {
        vertexBuffer.add(new Point3D(0, 0, 0));
        vertexBuffer.add(new Point3D(1, 0, 0));
        vertexBuffer.add(new Point3D(1, 2, 0));
        vertexBuffer.add(new Point3D(0, 2, 0));

        vertexBuffer.add(new Point3D(0, 0, 1));
        vertexBuffer.add(new Point3D(1, 0, 1));
        vertexBuffer.add(new Point3D(1, 2, 1));
        vertexBuffer.add(new Point3D(0, 2, 1));

        addIndices(0, 1, 1, 2, 2, 3, 3, 0);
        addIndices(4, 5, 5, 6, 6, 7, 7, 4);
        addIndices(7, 3, 4, 0, 5, 1, 2, 6);
    }
}

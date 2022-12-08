package solids;

import transforms.Point3D;

import java.awt.*;

public class Axis extends Solid {
    public Axis() {
        vertexBuffer.add(new Point3D(0, 0, 0));
        vertexBuffer.add(new Point3D(.5, 0, 0));
        vertexBuffer.add(new Point3D(0, .5, 0));
        vertexBuffer.add(new Point3D(0, 0, .5));

        addIndices(0, 1, 0, 2, 0, 3);

        setColor(Color.RED, Color.GREEN, Color.BLUE);
    }
}

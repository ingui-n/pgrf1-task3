package solids;

import transforms.Point3D;

public class Circle extends Solid {
    public Circle() {
        for (double x = 0; x <= 2 * Math.PI + .05; x += .05) {
            vertexBuffer.add(new Point3D(Math.sin(x), Math.cos(x), 0));
        }

        for (double x = 0; x <= 2 * Math.PI + .05; x += .05) {
            vertexBuffer.add(new Point3D(Math.cos(x), 0, Math.sin(x)));
        }

        int halfSize = vertexBuffer.size() / 2;
        
        for (int i = 0; i < vertexBuffer.size() - 1; i++) {
            if (halfSize - 1 != i)
                addIndices(i, i + 1);
        }
    }
}

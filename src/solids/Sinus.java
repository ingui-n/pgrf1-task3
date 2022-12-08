package solids;

import transforms.Point3D;

public class Sinus extends Solid {//todo remove

    public Sinus() {
        for (double x = 0; x < 50; x += .1) {
            vertexBuffer.add(new Point3D(x, 0, Math.sin(x)));
        }

        for (int i = 0; i < vertexBuffer.size() - 1; i++) {
            addIndices(i, i + 1);
        }
    }
}

package solids;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vertexBuffer = new ArrayList<>();
    protected List<Integer> indexBuffer = new ArrayList<>();
    private Mat4 model = new Mat4Identity();
    private boolean isSelected = false;
    private final ArrayList<Color> colors = new ArrayList<>();

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public void resetColors() {
        colors.clear();
    }

    public void setColor(Color... colors) {
        this.colors.addAll(Arrays.asList(colors));
    }

    public Color getColor(Integer i) {
        if (i > -1 && colors.size() > 0) {
            if (i == 0)
                return colors.get(0);

            int index = i / 2;

            if (index < colors.size())
                return colors.get(index);
        }

        return null;
    }

    protected void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        if (!isSelected) {
            for (int i = 0; i < indexBuffer.size() / 2; i++) {
                setColor(Color.CYAN);
            }
        } else {
            resetColors();
        }

        isSelected = selected;
    }
}

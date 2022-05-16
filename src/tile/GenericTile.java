package tile;
import javax.swing.*;

public abstract class GenericTile extends JLabel {
    public boolean isSolid = true;
    public boolean isBreakable = false;

    public GenericTile(ImageIcon sprite) {
        super(sprite);
    }

    public void destroy() {
        if (isBreakable) {
            isSolid = false;
            setVisible(false);
        }
    }

    public void reset() {
        if (isBreakable) {
            isSolid = true;
            setVisible(true);
        }
    }
}
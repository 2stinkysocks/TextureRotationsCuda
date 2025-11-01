
import java.util.ArrayList;

public class Main {

    public static final int xMin = -500000, xMax = 500000;
    public static final int zMin = -500000, zMax = 500000;
    public static final int yMin = 63   , yMax = 65;

    public static final TextureVersion version = TextureVersion.v1_21_2;

    public static final ArrayList<RotationInfo> formation = new ArrayList<>();


    // Add formation here
    static {
        formation.add(new RotationInfo(0, 0, 0, 0, false));
        formation.add(new RotationInfo(0, 0, -1, 2, false));
        formation.add(new RotationInfo(-1, 0, 0, 2, false));
        formation.add(new RotationInfo(1, 0, 0, 3, false));
        formation.add(new RotationInfo(1, 0, -1, 2, false));
        formation.add(new RotationInfo(0, 0, 1, 3, false));
        formation.add(new RotationInfo(-1, 0, 1, 1, false));
        formation.add(new RotationInfo(1, 0, 1, 2, false));
        formation.add(new RotationInfo(1, 0, 2, 1, false));
        formation.add(new RotationInfo(2, 0, 2, 1, false));
        formation.add(new RotationInfo(1, 0, 3, 2, false));
        formation.add(new RotationInfo(2, 0, 3, 1, false));
        formation.add(new RotationInfo(3, 0, 3, 3, false));
        formation.add(new RotationInfo(1, 0, 4, 2, false));
        formation.add(new RotationInfo(2, 0, 4, 2, false));
        formation.add(new RotationInfo(3, 0, 4, 2, false));
        formation.add(new RotationInfo(2, 0, 5, 1, false));
        formation.add(new RotationInfo(3, 0, 5, 1, false));
        formation.add(new RotationInfo(4, 0, 5, 0, false));
        formation.add(new RotationInfo(3, 0, 6, 0, false));
        formation.add(new RotationInfo(4, 0, 6, 0, false));
        formation.add(new RotationInfo(5, 0, 6, 2, false));
        formation.add(new RotationInfo(6, 0, 6, 0, false));
        formation.add(new RotationInfo(3, 0, 7, 3, false));
        formation.add(new RotationInfo(5, 0, 7, 2, false));
        formation.add(new RotationInfo(6, 0, 7, 0, false));
        formation.add(new RotationInfo(4, 0, 8, 0, false));
        formation.add(new RotationInfo(5, 0, 12, 1, false));
        formation.add(new RotationInfo(6, 0, 12, 2, false));
        formation.add(new RotationInfo(8, 0, 12, 0, false));
        formation.add(new RotationInfo(5, 0, 13, 0, false));
        formation.add(new RotationInfo(6, 0, 13, 2, false));


    }

    public static void main(String[] args) {
        TextureFinder a = new TextureFinder();
        a.start();
    }
}

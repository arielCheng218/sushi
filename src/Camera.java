import javax.swing.JFrame;
import java.awt.*;
import java.awt.Dimension;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

// HELP. ME.
// WHY DO YOU NOT WORK WHY DO YOU NOT WORK WHY WHY WHY WHY WHY

class Camera extends JFrame {

  JFrame frame = new JFrame("sushi 3D");
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  double width = screenSize.getWidth();
  double height = screenSize.getHeight();

  public Vector3D position = new Vector3D(width / 2.0, height / 2.0, 0);
  public double focalDistance = 1.0;

  public static void main(String[] args) {
    Camera camera = new Camera();
    World world = new World(camera.width, camera.height, camera.focalDistance, camera.position);
    camera.frame.add(world);
    world.addObject(new Sphere(0.99999, new Vector3D(camera.width / 2.0, camera.height / 2.0, -1)));
    camera.frame.setVisible(true);
    camera.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println("Done.");
  }
}
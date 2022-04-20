import javax.swing.JFrame;
import java.awt.*;
import java.awt.Dimension;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

// TO-DO: Rewrite camera class
// HELP. ME.

class Camera extends JFrame {

  public Vector3D position = new Vector3D(0, 0, -2.00001);
  public double focalDistance = 3.0;

  JFrame frame = new JFrame("sushi 3D");
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  double width = screenSize.getWidth();
  double height = screenSize.getHeight();

  public static void main(String[] args) {
    Camera camera = new Camera();
    World world = new World(camera.width, camera.height, camera.focalDistance, camera.position);
    camera.frame.add(world);
    world.addObject(new Sphere(1, new Vector3D(0, 0, -1)));
    camera.frame.setVisible(true);
    camera.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println("Done.");
  }
}
import javax.swing.JFrame;
import java.awt.*;
import java.awt.Dimension;
import java.net.http.WebSocket.Listener;
import java.util.EventListener;
import java.util.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


class Camera extends JFrame implements EventListener {

  JFrame frame = new JFrame("sushi 3D");
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  double width = screenSize.getWidth();
  double height = screenSize.getHeight();

  public Vector3D position = new Vector3D(width / 2.0, height / 2.0, 0);
  public double focalDistance = 50.0;

  public static void main(String[] args) {
    // setup
    Camera camera = new Camera();
    World world = new World(camera.width, camera.height, camera.focalDistance, camera.position);
    camera.frame.add(world);
    world.addObject(new Sphere(1.9, new Vector3D(camera.width / 2.0, camera.height / 2.0, -2)));
    camera.frame.setVisible(true);
    camera.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    camera.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println("Done.");
  }

}
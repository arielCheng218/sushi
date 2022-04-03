import java.util.ArrayList;
import javax.swing.JFrame;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

class Camera {

  public Vector3D position = new Vector3D(0, 0, 0);
  JFrame frame = new JFrame("sushi 3D");

  public void setup() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    frame.pack();
    frame.setVisible(true);
  }

  public void render(ArrayList<Object> objects) {
    setup();
    for (int i = 0; i < objects.size(); i++) {
      objects.get(i).render();
    }
  }

  public static void main(String[] args) {
    Camera camera = new Camera();
    World world = new World();
    world.addObject(new Sphere(new Vector3D(0, 0, 0)));
    camera.render(world.objects);
    System.out.println("Done.");
  }
}
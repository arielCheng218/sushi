import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.*;
import java.awt.Dimension;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


class Camera extends JPanel implements MouseMotionListener {

  JFrame frame = new JFrame("sushi 3D");
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

  public double width = screenSize.getWidth();
  public double height = screenSize.getHeight();
  
  private double speed = 20.0;

  public Vector3D position = new Vector3D(width / 2.0, height / 2.0, -1200);
  public double focalDistance = 1.0;

  public void setup() {
    this.frame.setVisible(true);
    this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println("Done.");
  }

  public static void main(String[] args) {
    Camera camera = new Camera();
    World world = new World(camera);
    camera.frame.add(world);

    // listen for mouse events and key presses
    camera.frame.addMouseMotionListener(camera);
    camera.setupKeyBindings(world, camera);

    // sphere
    double sphereRadius = 200.0;
    Vector3D sphereCenter = new Vector3D(80+(camera.width / 2.0), 80+(camera.height / 2.0), -1);
    Sphere sphere = new Sphere(sphereRadius, sphereCenter);
    world.addObject(sphere);

    camera.setup();
  }

  public void mouseDragged(MouseEvent event) {}

  public void mouseMoved(MouseEvent event) {
    // TODO rotate camera
    // System.out.println("Mouse movement: " + event.getX()+ "," + event.getY() + ".");
    // Rotate camera somehow
    // frame.repaint();
  }

  public void setupKeyBindings(World world, Camera camera) {
    Forward forward = new Forward(camera);
    Back back = new Back(camera);
    Left left = new Left(camera);
    Right right = new Right(camera);
    world.getInputMap().put(KeyStroke.getKeyStroke('w'), "forward");
    world.getActionMap().put("forward", forward);
    world.getInputMap().put(KeyStroke.getKeyStroke('s'), "back");
    world.getActionMap().put("back", back);
    world.getInputMap().put(KeyStroke.getKeyStroke('a'), "left");
    world.getActionMap().put("left", left);
    world.getInputMap().put(KeyStroke.getKeyStroke('d'), "right");
    world.getActionMap().put("right", right);
    // TODO add jump feature
  }

  public class Forward extends AbstractAction {
    Camera camera;
    Forward(Camera camera) {
      this.camera = camera;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println("forward action");
      camera.position = incVectorValue(2, speed, position);
      camera.frame.repaint();
    }
  }

  public class Back extends AbstractAction {
    Camera camera;
    Back(Camera camera) {
      this.camera = camera;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      camera.position = incVectorValue(2, -speed, position);
      camera.frame.repaint();
    }
  }

  public class Left extends AbstractAction {
    Camera camera;
    Left(Camera camera) {
      this.camera = camera;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      camera.position = incVectorValue(0, speed, position);
      camera.frame.repaint();
    }
  }

  public class Right extends AbstractAction {
    Camera camera;
    Right(Camera camera) {
      this.camera = camera;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      camera.position = incVectorValue(0, ㄦspeed, position);
      camera.frame.repaint();
    }
  }

  private Vector3D incVectorValue(int index, double increment, Vector3D vector) {
    if (index == 0) {
      return new Vector3D(vector.getX() + increment, vector.getY(), vector.getZ());
    } else if (index == 1) {
      return new Vector3D(vector.getX(), vector.getY() + increment, vector.getZ());
    } else if (index == 2) {
      return new Vector3D(vector.getX(), vector.getY(), vector.getZ() + increment);
    }
    return null;
  }
}
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.awt.event.MouseMotionListener;
import java.awt.*;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


class Camera extends JPanel implements MouseMotionListener {

  JFrame frame = new JFrame("sushi 3D");
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

  private World world;

  public int width = (int)screenSize.getWidth();
  public int height = (int)screenSize.getHeight();
  
  private double speed = 20.0;
  private PointerInfo lastMousePosition;
  Duration deltaTime;

  public Vector3D position = new Vector3D(width / 2.0, height / 2.0, -600);
  public double thetaZ = 0;
  public double thetaY = 0;
  public double focalDistance = 900.0;

  public void setup() {
    this.frame.setCursor(this.frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
    System.setProperty("apple.awt.fullscreenhidecursor","true");
    this.frame.setVisible(true);
    this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println("Done.");
  }

  public void loop() {
    while (WHEN_IN_FOCUSED_WINDOW != 0) {
      this.deltaTime = Duration.ZERO;
      Instant beginTime = Instant.now();
      this.frame.repaint();
      deltaTime = Duration.between(beginTime, Instant.now());
    }
  }

  public static void main(String[] args) {
    Camera camera = new Camera();
    camera.world = new World(camera);
    camera.frame.add(camera.world);

    // listen for mouse events and key presses
    camera.frame.addMouseMotionListener(camera);
    camera.setupKeyBindings(camera.world, camera);

    // set mouse position
    camera.lastMousePosition = MouseInfo.getPointerInfo();

    // sphere
    double sphereRadius = 80.0;
    Vector3D sphereCenter = new Vector3D(camera.width / 2.0, camera.height / 2.0, -1);
    Sphere sphere = new Sphere(sphereRadius, sphereCenter);
    camera.world.addObject(sphere);

    camera.setup();

    // game loop
    camera.loop();
  }

  public void mouseDragged(MouseEvent event) {}

  public void mouseMoved(MouseEvent event) {
    System.out.println("Mouse movement: " + event.getX()+ "," + event.getY() + ".");
    double deltaX = this.lastMousePosition.getLocation().getX() - event.getX();
    double deltaY = this.lastMousePosition.getLocation().getY() - event.getY();
    thetaZ += deltaX / this.focalDistance;
    thetaY += deltaY / this.focalDistance;
    System.out.println(thetaZ);
    System.out.println(thetaY);
    lastMousePosition = MouseInfo.getPointerInfo();
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
      camera.position = incVectorValue(2, speed*(1+camera.deltaTime.toMillis()), camera.position);
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
      camera.position = incVectorValue(2, -speed*(1+camera.deltaTime.toMillis()), camera.position);
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
      camera.position = incVectorValue(0, -speed*(1+camera.deltaTime.toMillis()), camera.position);
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
      camera.position = incVectorValue(0, speed*(1+camera.deltaTime.toMillis()), camera.position);
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
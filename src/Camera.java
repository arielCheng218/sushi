import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.awt.event.MouseMotionListener;
import java.awt.*;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


class Camera extends JPanel implements MouseMotionListener {

  // PROPERTIES OF CAMERA
  JFrame frame = new JFrame("sushi 3D");
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

  private World world;

  public int width = (int)screenSize.getWidth();
  public int height = (int)screenSize.getHeight();

  Duration deltaTime;

  private double speed = 20.0;
  public Vector3D position = new Vector3D(width / 2.0, height / 2.0, -600);
  public double thetaZ = 0;
  public double thetaY = 0;
  public double focalDistance = 900.0;

  // OTHER
  public boolean mouseInvisible = true;
  Robot robot;

  public void setup() {
    // Hide cursor
    this.frame.setCursor(this.frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
    System.setProperty("apple.awt.fullscreenhidecursor","true");
    // Set properties
    this.frame.setVisible(true);
    this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void loop() {
    while (WHEN_IN_FOCUSED_WINDOW != 0) {
      // get deltaTime value
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

    // colors
    Integer[] redRGB = {255, 0, 0};
    Integer[] greenRGB = {0, 255, 0};

    // sphere 3
    double sphereRadius_3 = 4000.0;
    Vector3D sphereCenter_3 = new Vector3D(camera.width / 2.0, (camera.height / 2.0)+4000, -90);
    Sphere sphere3 = new Sphere(sphereRadius_3, greenRGB, sphereCenter_3);
    camera.world.addObject(sphere3);

    // sphere 1
    double sphereRadius_1 = 20.0;
    Vector3D sphereCenter_1 = new Vector3D(camera.width / 2.0, camera.height / 2.0, -90);
    Sphere sphere1 = new Sphere(sphereRadius_1, redRGB, sphereCenter_1);
    camera.world.addObject(sphere1);

    // sphere 2
    double sphereRadius_2 = 40.0;
    Vector3D sphereCenter_2 = new Vector3D(camera.width / 2.0 + 80.0, camera.height / 2.0, -90);
    Sphere sphere2 = new Sphere(sphereRadius_2, redRGB, sphereCenter_2);
    camera.world.addObject(sphere2);

    camera.setup();

    // game loop
    camera.loop();
  }

  // Mouse handlers
  public void mouseDragged(MouseEvent event) {}

  public void mouseMoved(MouseEvent event) {
    if (this.mouseInvisible) {
      double deltaX = this.width / 2 - event.getXOnScreen();
      double deltaY = this.height / 2 - event.getYOnScreen();
      thetaZ += deltaX / (2 * Math.PI * this.focalDistance);
      thetaY += deltaY / (2 * Math.PI * this.focalDistance);
      this.setMousePosition();
    }
  }

  public void setMousePosition() {
    // User has not escaped, return mouse to center of screen
    if (this.mouseInvisible) {
      try {
        robot = new Robot();
        robot.mouseMove(this.width / 2, this.height / 2);
      } catch (AWTException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("User has escaped.");
    }
  }

  public void setupKeyBindings(World world, Camera camera) {
    // Initialize actions
    Forward forward = new Forward(camera);
    Back back = new Back(camera);
    Left left = new Left(camera);
    Right right = new Right(camera);
    Escape escape = new Escape(camera);
    // WASD movement
    world.getInputMap().put(KeyStroke.getKeyStroke('w'), "forward");
    world.getActionMap().put("forward", forward);
    world.getInputMap().put(KeyStroke.getKeyStroke('s'), "back");
    world.getActionMap().put("back", back);
    world.getInputMap().put(KeyStroke.getKeyStroke('a'), "left");
    world.getActionMap().put("left", left);
    world.getInputMap().put(KeyStroke.getKeyStroke('d'), "right");
    world.getActionMap().put("right", right);
    // Esc
    world.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
    world.getActionMap().put("escape", escape);
    // TODO add jump feature
  }

  // Key binding actions

  public class Forward extends AbstractAction {
    Camera camera;
    Forward(Camera camera) {
      this.camera = camera;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      camera.position = incVectorValue(2, speed*(1+camera.deltaTime.toMillis()), camera.position);
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
    }
  }

  public class Escape extends AbstractAction {
    Camera camera;
    Escape(Camera camera) {
      this.camera = camera;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      // Close window
      System.exit(0);
    }
  }

  // Utils
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
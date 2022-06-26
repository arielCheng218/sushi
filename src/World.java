import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


public class World extends JPanel {

  public ArrayList<Object> objects = new ArrayList<Object>();
  public int time;
  private double focalDistance;
  private Camera camera;

  World(Camera camera) {
    this.camera = camera;
  }

  public void addObject(Object obj) {
    objects.add(obj);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.BLACK);
    System.out.println("Drawing...");
    Vector3D lightPosition = new Vector3D(1, 1, -1);
    DirectionLight light = new DirectionLight(lightPosition);
    for (Object obj : objects) {
      System.out.println("Rendering " + obj + "...");
      for (int i = 0; i < camera.width; i++) {
        for (int j = 0; j < camera.height; j++) {
          Vector3D rayToPanel = new Vector3D(i, j, focalDistance);
          Vector3D rayDirection = camera.position.subtract(rayToPanel);
          Ray ray = new Ray(camera.position, rayDirection);
          double t = obj.objectIsHit(ray);
          if (t >= 0) {
            // if ray from camera intersects object, color pixel
            // Vector3D normal = (ray.at(t).subtract(obj.position)).normalize();
            // double coeff = light.direction.normalize().dotProduct(normal);
            // coeff = (255*((coeff / 2.0) + 0.5));
            // g.setColor(new Color(255, 0, 0, (int)coeff));
            g.setColor(Color.RED);
            g.drawLine(i, j, i, j);
          } else {
            // else treat as background
            g.setColor(new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 255 - (int)(255 * (j / camera.height))));
            g.drawLine(i, j, i, j);
          }
        }
      }
    }
    System.out.println("Finished drawing.");
  }
}
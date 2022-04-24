import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


public class World extends JPanel {

  public ArrayList<Object> objects = new ArrayList<Object>();
  public int time;
  private double width;
  private double height;
  private double focalDistance;
  private Vector3D cameraPosition;

  World(double width, double height, double focalDistance, Vector3D cameraPosition) {
    this.width = width;
    this.height = height;
    this.focalDistance = focalDistance;
    this.cameraPosition = cameraPosition;
  }

  public void addObject(Object obj) {
    objects.add(obj);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.BLACK);
    System.out.println("Drawing...");
    DirectionLight light = new DirectionLight(new Vector3D(0, 3, 0));
    for (Object obj : objects) {
      System.out.println("Rendering " + obj + "...");
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          Vector3D rayDirection = this.cameraPosition.subtract(new Vector3D(i, j, focalDistance));
          Ray ray = new Ray(this.cameraPosition, rayDirection);
          double t = obj.objectIsHit(ray);
          if (t > 0) {
            // if ray from camera intersects object, color pixel
            Vector3D normal = (ray.at(t).subtract(obj.position)).normalize();
            double coeff = light.direction.dotProduct(normal);
            coeff = (255*((coeff / 2.0) + 0.5));
            g.setColor(new Color(255, 0, 0, (int)coeff));
            g.drawLine(i, j, i, j);
          } else {
            // else treat as background
            g.setColor(new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 255 - (int)(255 * (j / this.height))));
            g.drawLine(i, j, i, j);
          }
        }
      }
    }
    System.out.println("Finished drawing.");
  }
}
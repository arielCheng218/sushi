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
    int count1 = 0;
    int count2 = 0;
    super.paintComponent(g);
    this.setBackground(Color.BLACK);
    System.out.println("Drawing...");
    g.setColor(Color.CYAN);
    for (Object obj : objects) {
      System.out.println("Rendering " + obj + "...");
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          count2++;
          Vector3D rayDirection = this.cameraPosition.subtract(new Vector3D(i, j, focalDistance));
          Ray ray = new Ray(this.cameraPosition, rayDirection); // Ray from camera to plane
          if (obj.objectIsHit(ray)) {
            count1++;
            g.drawLine(i, j, i, j);
          }
        }
      }
    }
    System.out.println(count1 + "/" + count2);
    System.out.println("Finished drawing.");
  }
}

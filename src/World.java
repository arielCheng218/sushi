import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorChangingVisitor;


public class World extends JPanel {

  public ArrayList<Object> objects = new ArrayList<Object>();
  public int time;
  private Camera camera;

  World(Camera camera) {
    this.camera = camera;
  }

  public void addObject(Object obj) {
    objects.add(obj);
  }

  private RealMatrix getRotationMatrix(char type, double angle) {
    if (type == 'x') {
      return MatrixUtils.createRealMatrix(new double[][] {
        {1, 0, 0},
        {0, Math.cos(angle), -Math.sin(angle)},
        {0, Math.sin(angle), Math.cos(angle)},
      });
    } else if (type == 'y') {
      return MatrixUtils.createRealMatrix(new double[][] {
        {Math.cos(angle), 0, Math.sin(angle)},
        {0, 1, 0},
        {-Math.sin(angle), 0, Math.cos(angle)},
      });
    } else if (type == 'z') {
      return MatrixUtils.createRealMatrix(new double[][] {
        {Math.cos(angle), -Math.sin(angle), 0},
        {Math.sin(angle), Math.cos(angle), 0},
        {0, 0, 1},
      });
    }
    return null;
  }

  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);
    this.setBackground(Color.BLACK);
    System.out.println("Drawing...");
    // Vector3D lightPosition = new Vector3D(1, 1, -1);
    // DirectionLight light = new DirectionLight(lightPosition);

    for (Object obj : objects) {
      System.out.println("Rendering " + obj + "...");

      for (int i = 0; i < camera.width; i++) {
        for (int j = 0; j < camera.height; j++) {

          RealVector rayToPlane = MatrixUtils.createRealVector(new double[] {i, j, camera.focalDistance});
          // System.out.println(rayToPlane);

          // rotate by camera.thetaZ and camera.thetaY
          RealMatrix rotateZ = getRotationMatrix('z', camera.thetaY);
          RealMatrix rotateY = getRotationMatrix('y', camera.thetaZ);
          rayToPlane = rotateZ.operate(rayToPlane);
          rayToPlane = rotateY.operate(rayToPlane);
          // System.out.println(rayToPlane);

          Vector3D vectorToPlane = new Vector3D(rayToPlane.getEntry(0), rayToPlane.getEntry(1), rayToPlane.getEntry(2));
          Vector3D rayDirection = camera.position.subtract(vectorToPlane);
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
            g.setColor(new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 255 - (int)(255 * j/camera.height)));
            g.drawLine(i, j, i, j);
          }
        }
      }
    }
    System.out.println("Finished drawing.");
  }
}
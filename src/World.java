import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorChangingVisitor;


public class World extends JPanel {

  public ArrayList<Object> objects = new ArrayList<Object>();
  public int time;
  private Camera camera;

  private BufferedImage canvas;

  RealMatrix rotateZ;
  RealMatrix rotateY;

  World(Camera camera) {
    this.camera = camera;
    this.canvas = new BufferedImage(this.camera.width, this.camera.height, BufferedImage.TYPE_INT_ARGB);
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

  private void fillCanvas(Color c) {
    for (int x = 0; x < this.camera.width; x++) {
      for (int y = 0; y < this.camera.height; y++) {
        this.canvas.setRGB(x, y, c.getRGB());
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);

    // lights
    Vector3D lightDirection = new Vector3D(camera.width / 2.0, camera.height / 2.0 + 40.0, -1);
    DirectionLight light = new DirectionLight(lightDirection);

    // get rotation matrices 
    rotateZ = getRotationMatrix('z', camera.thetaY);
    rotateY = getRotationMatrix('y', camera.thetaZ);
    
    for (Object obj : objects) {
      for (int i = 0; i < camera.width; i++) {
        for (int j = 0; j < camera.height; j++) {

          RealVector rayToPlane = MatrixUtils.createRealVector(new double[] {i, j, camera.focalDistance});

          // rotate ray to plane by camera.thetaZ and camera.thetaY
          rayToPlane = rotateZ.operate(rayToPlane);
          rayToPlane = rotateY.operate(rayToPlane);

          Vector3D cameraToPlane = new Vector3D(rayToPlane.getEntry(0), rayToPlane.getEntry(1), rayToPlane.getEntry(2));
          cameraToPlane = camera.position.subtract(cameraToPlane);

          // Ray object intersection
          Ray ray = new Ray(camera.position, cameraToPlane);
          double t = obj.objectIsHit(ray);

          if (t >= 0) {
            // if ray from camera intersects object, color pixel
            Vector3D normal = (ray.at(t).subtract(obj.position)).normalize();
            double coeff = light.direction.normalize().dotProduct(normal);
            coeff = (255*((coeff / 2.0) + 0.5));
            // TODO read color from object
            // TODO java HSV transform
            Color color = new Color(255, 0, 0, (int)coeff);
            this.canvas.setRGB(i, j, color.getRGB());
          } else {
            // else treat as background
           Color color = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 255 - (int)(255 * j/camera.height));
            this.canvas.setRGB(i, j, color.getRGB());
          }
        }
      }
    }
    g.drawImage(this.canvas, 0, 0, null);
  }
}
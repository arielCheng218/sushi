import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

// TODO implement spatial partitioning
// TODO implement bounding volume hierachy
// TODO refractive and reflective materials

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

  private void paintBackground() {
    for (int i = 0; i < camera.width; i++) {
      for (int j = 0; j < camera.height; j++) {
        Color color = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), 255 - (int)(255 * j / camera.height));
        this.canvas.setRGB(i, j, color.getRGB());
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);

    // lights
    Vector3D lightDirection = new Vector3D(0, -1, -0.78).normalize();
    DirectionLight light = new DirectionLight(lightDirection);

    // get rotation matrices 
    rotateZ = getRotationMatrix('z', camera.thetaY);
    rotateY = getRotationMatrix('y', camera.thetaZ);

    paintBackground();

    // TODO add z buffer
    
    for (Object obj : objects) {
      for (int i = 0; i < camera.width; i++) {
        for (int j = 0; j < camera.height; j++) {

          // System.out.println(obj);

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
            Vector3D normal = (obj.position.subtract(ray.at(t))).normalize();
            double coeff = -light.direction.dotProduct(normal);
            coeff = (coeff + 1) / 2; // make sure coefficient is between 0 and 1
            System.out.println((int)(200*coeff));
            System.out.println((int)(10*coeff));
            System.out.println((int)(10*coeff));
            // TODO read color from object
            // TODO java HSV transform
						// System.out.println((float)coeff);
            Color objectColor = new Color((int)(200*coeff), (int)(10*coeff), (int)(10*coeff));
            // Debug surface normals
            // Color objectColor = new Color((int)(255*((normal.getX() + 1)/2)), (int)(255*((normal.getY() + 1)/2)), (int)(255*((normal.getZ() + 1)/2)));
            this.canvas.setRGB(i, j, objectColor.getRGB());
          } 
        }
      }
    }
    g.drawImage(this.canvas, 0, 0, null);
  }
}
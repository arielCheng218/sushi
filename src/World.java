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

    // direction light
    Vector3D lightDirection = new Vector3D(0.5, 0.4, 0).normalize();
    DirectionLight directionLight = new DirectionLight(lightDirection);
    // point light
    Vector3D lightOrigin = new Vector3D((camera.width / 2.0)-40, (camera.height / 2.0)+80, -70);
    PointLight pointLight = new PointLight(lightOrigin);

    // get rotation matrices 
    rotateZ = getRotationMatrix('z', camera.thetaY);
    rotateY = getRotationMatrix('y', camera.thetaZ);

    paintBackground();

    // TODO add z buffer
    
    for (int objectIndex = 0; objectIndex < objects.size(); objectIndex++) {
      for (int i = 0; i < camera.width; i++) {
        for (int j = 0; j < camera.height; j++) {

          Color objectColor;
          Object obj = objects.get(objectIndex);
          
          RealVector rayToPlane = MatrixUtils.createRealVector(new double[] {i, j, camera.focalDistance});

          // rotate ray to plane by camera.thetaZ and camera.thetaY
          rayToPlane = rotateZ.operate(rayToPlane);
          rayToPlane = rotateY.operate(rayToPlane);

          Vector3D cameraToPlane = new Vector3D(rayToPlane.getEntry(0), rayToPlane.getEntry(1), rayToPlane.getEntry(2));
          cameraToPlane = camera.position.subtract(cameraToPlane);

          // Color pixel if it's part of an object

          // Ray object intersection
          Ray ray = new Ray(camera.position, cameraToPlane);
          double t = obj.objectIsHit(ray);

          if (t >= 0) {
            // Color objects
            Vector3D normal = (ray.at(t).subtract(obj.position)).normalize();
            double coeff = -directionLight.direction.dotProduct(normal);
            coeff = (coeff + 1) / 2; // make sure coefficient is between 0 and 1
            objectColor = new Color((float)obj.getColor()[0], (float)obj.getColor()[1], ((float)obj.getColor()[2]), (float)(coeff));
            this.canvas.setRGB(i, j, objectColor.getRGB());

            // FIXME is the hitpoint from the camera or from the origin

            // Color pixel if it's a shadow
            for (int shadowObjectIndex = 0; shadowObjectIndex < objects.size(); shadowObjectIndex++) {
              if (shadowObjectIndex != objectIndex) {
                Vector3D pHit = ray.at(t);
                Vector3D pHitToPointLight = pointLight.origin.subtract(pHit);
                double shadowRayIntersectObject = objects.get(shadowObjectIndex).objectIsHit(new Ray(pHit, pHitToPointLight)); 
                // if a ray from the hit point to the light source intersects an object, the light is blocked so there's a shadow
                if (shadowRayIntersectObject >= 0) {
                  objectColor = new Color(0, 0, 0);
                  this.canvas.setRGB(i, j, objectColor.getRGB());
                  break;
                };
              }
            }
          }
          
        }
      }
    }
    g.drawImage(this.canvas, 0, 0, null);
  }
}
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Ray {

  public Vector3D origin = new Vector3D(0, 0, 0);
  public Vector3D direction = new Vector3D(0, 0, 0);
  
  Ray(Vector3D origin, Vector3D direction) {
    this.origin = origin;
    this.direction = direction;
  }
}

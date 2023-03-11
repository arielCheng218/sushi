import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PointLight {
  public Vector3D origin;
  PointLight(Vector3D origin) {
    this.origin = origin;
  }
}
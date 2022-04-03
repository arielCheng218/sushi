import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Sphere extends Object {

  public double radius;

  Sphere(Vector3D point) {
    super(point);
  }

  @Override
  public boolean hitSurface() {
    return true;
  }

  @Override
  public void render() {
    System.out.println("Rendering sphere...");
  }
}
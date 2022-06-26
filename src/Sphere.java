import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Sphere extends Object {

  public double radius;

  Sphere(double radius, Vector3D position) {
    super(position);
    this.radius = radius;
  }

  @Override
  public double objectIsHit(Ray ray) {
    Vector3D oc = position.subtract(ray.origin);
    double a = Vector3D.dotProduct(ray.direction, ray.direction);
    double b = 2.0 * Vector3D.dotProduct(oc, ray.direction);
    double c = Vector3D.dotProduct(oc, oc) - this.radius*this.radius;
    double discriminant = b*b - 4*a*c;
    if (discriminant < 0) { return -1.0; }
    return (-b - Math.sqrt(discriminant)) / (2.0*a);
  }

}
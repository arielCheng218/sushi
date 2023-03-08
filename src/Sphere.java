import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Sphere extends Object {

  public double radius;
  public Integer[] RGBColor;

  Sphere(double radius, Integer[] RGBColor, Vector3D position) {
    super(position);
    this.radius = radius;
    this.RGBColor = RGBColor;
  }

  // Returns t in Ray(t) - whether the ray ever hits the sphere.
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

  @Override
  public Integer[] getColor() {
    return RGBColor;
  }

}
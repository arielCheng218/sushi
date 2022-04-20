import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Object {

  public Vector3D position = new Vector3D(0, 0, 0);
  // TO-DO: rotation
  // TO-DO: material
  // TO-DO: location/chunk in world (octree)

  Object(Vector3D point) {
    position = point;
  }

  public boolean objectIsHit(Ray ray) { return false; }
}
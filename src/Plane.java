import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Plane extends Object {
	
	// A plane is defined by a point (Vector3D Poistion) and two vectors.
	public Vector3D v1;
	public Vector3D v2;

	Plane(Vector3D v1, Vector3D v2, Vector3D position) {
		super(position);
		this.v1 = v1;
		this.v2 = v2;
	}

	private Vector3D getNegativeVector(Vector3D v) {
		return v.crossProduct(new Vector3D(-1, -1, -1));
	}

	// Any point inside the plane can be represented as a linear combination of vectors v1 and v2, added onto the position vector (p).
	// Plane(u, v) = u * v1 + u * v2 + p

	// objectIsHit returns t in Ray(t) - whether the ray hits the plane.
	// This method implements the Moller-Trumbore intersection algorithm.
	@Override
  public double objectIsHit(Ray ray) {
		RealMatrix m = MatrixUtils.createRealMatrix(new double[][] {
			{ray.direction.getX(),ray.direction.getY(), ray.direction.getZ()},
			{getNegativeVector(v1).getX(), getNegativeVector(v1).getY(), getNegativeVector(v1).getZ()},
			{getNegativeVector(v2).getX(), getNegativeVector(v2).getY(), getNegativeVector(v2).getZ()}
		});
		double det = (new LUDecomposition(m)).getDeterminant();
		if (det <= 0) { return -1.0; }
		return 1.0; // FIXME this is not done yet
  } 

}

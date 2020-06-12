package cz.zdepav.school.texiscript.utils;

/** Immutable representation of a 2D vector. */
public class Vec2 {

    /** maximal difference between values for them to be considered equal */
    private static final double equalityTolerance = 1000.0 * Double.MIN_VALUE;

    /** zero vector */
    public static final Vec2 zero = new Vec2(0, 0);

    /** x coordinate */
    public final double x;

    /** y coordinate */
    public final double y;

    /** Constructs a vector from two coordinates. */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** Returns the length of this vector. */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /** Returns the second power of the length of this vector. */
    public double sqrLength() {
        return this.x * this.x + this.y * this.y;
    }

    /** Adds another vector to this one and returns the result. */
    public Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    /** Adds another vector to this one and returns the result. */
    public Vec2 add(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }

    /**
     * Adds another vector to this one and returns the result.
     * @param distance other vector's length
     * @param direction other vector's direction in radians
     * @return Resulting vector.
     */
    public Vec2 addld(double distance, double direction) {
        return Vec2.ld(distance, direction, x, y);
    }

    /** Subtracts another vector from this one and returns the result. */
    public Vec2 sub(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    /** Subtracts another vector from this one and returns the result. */
    public Vec2 sub(double x, double y) {
        return new Vec2(this.x - x, this.y - y);
    }

    /** Computes the dot product with another vector. */
    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    /** Computes the dot product with another vector. */
    public double dot(double x, double y) {
        return this.x * x + this.y * y;
    }

    /** Multiplies this vector by a scalar value and returns the result. */
    public Vec2 mul(double f) {
        return new Vec2(x * f, y * f);
    }

    /** Computes the cross product with another vector. */
    public double cross(Vec2 v) {
        return x * v.y - v.x * y;
    }

    /**
     * Interpolates between this and other vector using linear interpolation.
     * @param v second vector
     * @param ammount interpolation ammount between the two colors (0 ~ this vector, 1 ~ v)
     * @return Resulting vector between this and v.
     */
    public Vec2 lerp(Vec2 v, double ammount) {
        if (ammount <= 0) {
            return this;
        } else if (ammount >= 1) {
            return v;
        } else {
            return new Vec2(x + (v.x - x) * ammount, y + (v.y - y) * ammount);
        }
    }

    /** Computes angle from this point to another one. */
    public double directionTo(Vec2 v) {
        return equals(v) ? 0 : Math.atan2(v.y - y, v.x - x);
    }

    /** Computes angle from this point to another one. */
    public double directionTo(double x, double y) {
        return equals(x, y) ? 0 : Math.atan2(y - this.y, x - this.x);
    }

    /**
     * Rotates this vector around 0;0 by a given angle and returns the result.
     * @param angle angle in radians
     * @return Resulting vector.
     */
    public Vec2 rotate(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        return new Vec2(x * c - y * s, x * s + y * c);
    }

    /**
     * Rotates this vector around origin by a given angle and returns the result.
     * @param origin point to rotate around
     * @param angle angle in radians
     * @return Resulting vector.
     */
    public Vec2 rotateAround(Vec2 origin, double angle) {
        double x = this.x - origin.x, y = this.y - origin.y;
        double c = Math.cos(angle), s = Math.sin(angle);
        return new Vec2(x * c - y * s, x * s + y * c).add(origin);
    }

    /** Computes distance from this point to another one. */
    public double distanceTo(Vec2 v) {
        double dx = v.x - x, dy = v.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /** Computes distance from this point to another one. */
    public double distanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /** Computes second power of the distance from this point to another one. */
    public double sqrDistanceTo(Vec2 v) {
        double dx = v.x - x, dy = v.y - y;
        return dx * dx + dy * dy;
    }

    /** Computes second power of the distance from this point to another one. */
    public double sqrDistanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return dx * dx + dy * dy;
    }

    /** Computes distance from this point to another one in a given metric. */
    public double distanceTo(Vec2 v, Metric metric) {
        double dx = v.x - x, dy = v.y - y;
        return metric.distance(dx, dy);
    }

    /** Computes distance from this point to another one in a given metric. */
    public double distanceTo(double x, double y, Metric metric) {
        double dx = x - this.x, dy = y - this.y;
        return metric.distance(dx, dy);
    }

    /** Normalizes this vector to a length of 1 and returns the result. */
    public Vec2 normalize() {
        var m = 1 / length();
        return new Vec2(x * m, y * m);
    }

    /** Negates both coordinates of this vector and returns the result. */
    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    /** Stretches this vector to a given length and returns the result. */
    public Vec2 toLength(double length) {
        var m = length / length();
        return new Vec2(x * m, y * m);
    }

    /**
     * Checks whether this point is inside a given triangle.
     * @param p1 triangle's first vertex
     * @param p2 triangle's second vertex
     * @param p3 triangle's third vertex
     * @return True if this point is inside the triangle, false otherwise.
     */
    public boolean insideTriangle(Vec2 p1, Vec2 p2, Vec2 p3) {
        return coordsInTriangle(p1, p2, p3).allNonNegative();
    }

    /**
     * Computes barycentric coordinates of this point within a given triangle.
     * @param p1 triangle's first vertex
     * @param p2 triangle's second vertex
     * @param p3 triangle's third vertex
     * @return Barycentric coordinates of this point within the given triangle.
     */
    public Vec3 coordsInTriangle(Vec2 p1, Vec2 p2, Vec2 p3) {
        var v0 = new Vec2(p2.x - p1.x, p2.y - p1.y);
        var v1 = new Vec2(p3.x - p1.x, p3.y - p1.y);
        var v2 = new Vec2(x - p1.x, y - p1.y);
        var dot00 = v0.dot(v0);
        var dot01 = v0.dot(v1);
        var dot02 = v0.dot(v2);
        var dot11 = v1.dot(v1);
        var dot12 = v1.dot(v2);
        var invD = 1 / (dot00 * dot11 - dot01 * dot01);
        var u = (dot11 * dot02 - dot01 * dot12) * invD;
        var v = (dot00 * dot12 - dot01 * dot02) * invD;
        return new Vec3(1 - u - v, u, v);
    }

    /** Returns a vector perpendicular to this one. */
    public Vec2 normal() {
        return new Vec2(y, -x);
    }

    /** Checks whether this vector is equal to a zero vector. */
    public boolean isZero() {
        return Math.abs(x) < equalityTolerance && Math.abs(y) < equalityTolerance;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec2) {
            var v = (Vec2) o;
            return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
        } else return false;
    }

    /** Compares this vector with another one. */
    public boolean equals(Vec2 v) {
        return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
    }

    /** Compares this vector with another one. */
    public boolean equals(double x, double y) {
        return Math.abs(this.x - x) < equalityTolerance && Math.abs(this.y - y) < equalityTolerance;
    }

    /** Converts this vector to a string in the format x;y. */
    public String toString() {
        return x + ";" + y;
    }

    /** Computes x coordinate of a vector with a given length and direction. */
    public static double ldx(double length, double direction) {
        return length * Math.cos(direction);
    }

    /** Computes x coordinate of a point with a given distance and direction from another point. */
    public static double ldx(double distance, double direction, double originX) {
        return originX + distance * Math.cos(direction);
    }

    /** Computes y coordinate of a vector with a given length and direction. */
    public static double ldy(double length, double direction) {
        return length * Math.sin(direction);
    }

    /** Computes y coordinate of a point with a given distance and direction from another point. */
    public static double ldy(double distance, double direction, double originY) {
        return originY + distance * Math.sin(direction);
    }

    /** Creates a vector with a given length and direction. */
    public static Vec2 ld(double length, double direction) {
        return new Vec2(length * Math.cos(direction), length * Math.sin(direction));
    }

    /** Creates a point with a given distance and direction from another point. */
    public static Vec2 ld(double distance, double direction, double originX, double originY) {
        return new Vec2(
            originX + distance * Math.cos(direction),
            originY + distance * Math.sin(direction)
        );
    }

    /** Creates a random unit vector. */
    public static Vec2 randUnit() {
        var a = Angle.rand();
        return new Vec2(Vec2.ldx(1, a), Vec2.ldy(1, a));
    }

    /** Creates a random unit vector using a specific random generator. */
    public static Vec2 randUnit(RandomGenerator rand) {
        var a = Angle.rand(rand);
        return new Vec2(Vec2.ldx(1, a), Vec2.ldy(1, a));
    }

    /** Creates a point on an ellipse with center at 0;0. */
    public static Vec2 onEllipse(double r1, double r2, double angle) {
        return new Vec2(Vec2.ldx(r1, angle), Vec2.ldy(r2, angle));
    }

    /** Creates a point on an ellipse with a given center. */
    public static Vec2 onEllipse(double r1, double r2, double angle, Vec2 center) {
        return new Vec2(Vec2.ldx(r1, angle, center.x), Vec2.ldy(r2, angle, center.y));
    }
}

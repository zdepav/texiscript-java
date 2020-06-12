package cz.zdepav.school.texiscript.utils;

/** Immutable representation of a 3D vector. */
public class Vec3 {

    /** maximal difference between values for them to be considered equal */
    private static final double equalityTolerance = 1000.0 * Double.MIN_VALUE;

    /** zero vector */
    public static final Vec3 zero = new Vec3(0, 0, 0);

    /** x coordinate */
    public final double x;

    /** y coordinate */
    public final double y;

    /** z coordinate */
    public final double z;

    /** Constructs a vector from three coordinates. */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Returns the length of this vector. */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /** Returns the second power of the length of this vector. */
    public double sqrLength() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /** Adds another vector to this one and returns the result. */
    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }

    /** Adds another vector to this one and returns the result. */
    public Vec3 add(double x, double y, double z) {
        return new Vec3(this.x + x, this.y + y, this.z + z);
    }

    /** Subtracts another vector from this one and returns the result. */
    public Vec3 sub(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }

    /** Subtracts another vector from this one and returns the result. */
    public Vec3 sub(double x, double y) {
        return new Vec3(this.x - x, this.y - y, this.z - z);
    }

    /** Computes the dot product with another vector. */
    public double dot(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /** Computes the dot product with another vector. */
    public double dot(double x, double y) {
        return this.x * x + this.y * y + this.z * z;
    }

    /** Multiplies this vector by a scalar value and returns the result. */
    public Vec3 mul(double f) {
        return new Vec3(x * f, y * f, z * f);
    }

    /**
     * Interpolates between this and other vector using linear interpolation.
     * @param v second vector
     * @param ammount interpolation ammount between the two colors (0 ~ this vector, 1 ~ v)
     * @return Resulting vector between this and v.
     */
    public Vec3 lerp(Vec3 v, double ammount) {
        if (ammount <= 0) {
            return this;
        } else if (ammount >= 1) {
            return v;
        } else {
            return new Vec3(x + (v.x - x) * ammount, y + (v.y - y) * ammount, z + (v.z - z) * ammount);
        }
    }

    /** Computes distance from this point to another one. */
    public double distanceTo(Vec3 v) {
        double dx = v.x - x, dy = v.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /** Computes distance from this point to another one. */
    public double distanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /** Computes second power of the distance from this point to another one. */
    public double sqrDistanceTo(Vec3 v) {
        double dx = v.x - x, dy = v.y - y;
        return dx * dx + dy * dy;
    }

    /** Computes second power of the distance from this point to another one. */
    public double sqrDistanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return dx * dx + dy * dy;
    }

    /** Computes distance from this point to another one in a given metric. */
    public double distanceTo(Vec3 v, Metric metric) {
        double dx = v.x - x, dy = v.y - y;
        return metric.distance(dx, dy);
    }

    /** Computes distance from this point to another one in a given metric. */
    public double distanceTo(double x, double y, Metric metric) {
        double dx = x - this.x, dy = y - this.y;
        return metric.distance(dx, dy);
    }

    /** Normalizes this vector to a length of 1 and returns the result. */
    public Vec3 normalize() {
        var m = 1 / length();
        return new Vec3(x * m, y * m, z * m);
    }

    /** Negates all coordinates of this vector and returns the result. */
    public Vec3 negate() {
        return new Vec3(-x, -y, -z);
    }

    /** Stretches this vector to a given length and returns the result. */
    public Vec3 toLength(double length) {
        var m = length / length();
        return new Vec3(x * m, y * m, z * m);
    }

    /** Checks whether this vector is equal to a zero vector. */
    public boolean isZero() {
        return Math.abs(x) < equalityTolerance && Math.abs(y) < equalityTolerance;
    }

    /** Checks whether all coordinates of this vector are non-negative. */
    public boolean allNonNegative() {
        return x >= 0 && y >= 0 && z >= 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec3) {
            var v = (Vec3)o;
            return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
        } else return false;
    }

    /** Compares this vector with another one. */
    public boolean equals(Vec3 v) {
        return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
    }

    /** Compares this vector with another one. */
    public boolean equals(double x, double y) {
        return Math.abs(this.x - x) < equalityTolerance && Math.abs(this.y - y) < equalityTolerance;
    }

    /** Creates a 2D vector from the x and y coordinates. */
    public Vec2 xy() {
        return new Vec2(x, y);
    }

    /** Creates a 2D vector from the x and z coordinates. */
    public Vec2 xz() {
        return new Vec2(x, z);
    }

    /** Creates a 2D vector from the y and z coordinates. */
    public Vec2 yz() {
        return new Vec2(y, z);
    }

    /** Creates a 2D vector from the y and x coordinates. */
    public Vec2 yx() {
        return new Vec2(y, x);
    }

    /** Creates a 2D vector from the z and x coordinates. */
    public Vec2 zx() {
        return new Vec2(z, x);
    }

    /** Creates a 2D vector from the z and y coordinates. */
    public Vec2 zy() {
        return new Vec2(z, y);
    }

    /** Converts this vector to a string in the format x;y;z. */
    public String toString() {
        return x + ";" + y + ";" + z;
    }

    /** Creates a random unit vector. */
    public static Vec3 randUnit() {
        var a1 = Angle.rand();
        var a2 = Angle.rand();
        var l = Vec2.ldx(1, a2);
        return new Vec3(l * Vec2.ldx(1, a1), l * Vec2.ldy(1, a1), Vec2.ldy(1, a2));
    }

    /** Creates a random unit vector using a specific random generator. */
    public static Vec3 randUnit(RandomGenerator rand) {
        var a1 = Angle.rand(rand);
        var a2 = Angle.rand(rand);
        var l = Vec2.ldx(1, a2);
        return new Vec3(l * Vec2.ldx(1, a1), l * Vec2.ldy(1, a1), Vec2.ldy(1, a2));
    }
}

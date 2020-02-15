package cz.zdepav.school.texiscript.utils;

/** @author Zdenek Pavlatka */
public class Vec3 {

    private static double equalityTolerance = 1000.0 * Double.MIN_VALUE;

    public static Vec3 zero = new Vec3(0, 0, 0);

    public final double x, y, z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double sqrLength() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }

    public Vec3 add(double x, double y, double z) {
        return new Vec3(this.x + x, this.y + y, this.z + z);
    }

    public Vec3 sub(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }

    public Vec3 sub(double x, double y) {
        return new Vec3(this.x - x, this.y - y, this.z - z);
    }

    public double dot(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double dot(double x, double y) {
        return this.x * x + this.y * y + this.z * z;
    }

    public Vec3 mul(double f) {
        return new Vec3(x * f, y * f, z * f);
    }

    public Vec3 lerp(Vec3 v, double ammount) {
        if (ammount <= 0) {
            return this;
        } else if (ammount >= 1) {
            return v;
        } else {
            return new Vec3(x + (v.x - x) * ammount, y + (v.y - y) * ammount, z + (v.z - z) * ammount);
        }
    }

    public double distanceTo(Vec3 v) {
        double dx = v.x - x, dy = v.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double sqrDistanceTo(Vec3 v) {
        double dx = v.x - x, dy = v.y - y;
        return dx * dx + dy * dy;
    }

    public double sqrDistanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return dx * dx + dy * dy;
    }

    public double distanceTo(Vec3 v, Metric metric) {
        double dx = v.x - x, dy = v.y - y;
        return metric.distance(dx, dy);
    }

    public double distanceTo(double x, double y, Metric metric) {
        double dx = x - this.x, dy = y - this.y;
        return metric.distance(dx, dy);
    }

    public Vec3 normalize() {
        var m = 1 / length();
        return new Vec3(x * m, y * m, z * m);
    }

    public Vec3 negate() {
        return new Vec3(-x, -y, -z);
    }

    public Vec3 toLength(double length) {
        var m = length / length();
        return new Vec3(x * m, y * m, z * m);
    }

    public boolean isZero() {
        return Math.abs(x) < equalityTolerance && Math.abs(y) < equalityTolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec3) {
            var v = (Vec3)o;
            return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
        } else return false;
    }

    public boolean equals(Vec3 v) {
        return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
    }

    public boolean equals(double x, double y) {
        return Math.abs(this.x - x) < equalityTolerance && Math.abs(this.y - y) < equalityTolerance;
    }

    public Vec2 xy() {
        return new Vec2(x, y);
    }

    public Vec2 xz() {
        return new Vec2(x, z);
    }

    public Vec2 yz() {
        return new Vec2(y, z);
    }

    public Vec2 yx() {
        return new Vec2(y, x);
    }

    public Vec2 zx() {
        return new Vec2(z, x);
    }

    public Vec2 zy() {
        return new Vec2(z, y);
    }

    public String toString() {
        return x + ";" + y;
    }

    public static Vec3 randUnit() {
        var a1 = Angle.rand();
        var a2 = Angle.rand();
        var l = Vec2.ldx(1, a2);
        return new Vec3(l * Vec2.ldx(1, a1), l * Vec2.ldy(1, a1), Vec2.ldy(1, a2));
    }

    public static Vec3 randUnit(RandomGenerator rand) {
        var a1 = Angle.rand(rand);
        var a2 = Angle.rand(rand);
        var l = Vec2.ldx(1, a2);
        return new Vec3(l * Vec2.ldx(1, a1), l * Vec2.ldy(1, a1), Vec2.ldy(1, a2));
    }
}

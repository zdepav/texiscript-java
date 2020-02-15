package cz.zdepav.school.texiscript.utils;

/** @author Zdenek Pavlatka */
public class Vec2 {

    private static double equalityTolerance = 1000.0 * Double.MIN_VALUE;

    public static Vec2 zero = new Vec2(0, 0);

    public final double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double sqrLength() {
        return this.x * this.x + this.y * this.y;
    }

    public Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    public Vec2 add(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }

    public Vec2 addld(double distance, double direction) {
        return Vec2.ld(distance, direction, x, y);
    }

    public Vec2 sub(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    public Vec2 sub(double x, double y) {
        return new Vec2(this.x - x, this.y - y);
    }

    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    public double dot(double x, double y) {
        return this.x * x + this.y * y;
    }

    public Vec2 mul(double f) {
        return new Vec2(x * f, y * f);
    }

    public double cross(Vec2 v) {
        return x * v.y - v.x * y;
    }

    public Vec2 lerp(Vec2 v, double ammount) {
        if (ammount <= 0) {
            return this;
        } else if (ammount >= 1) {
            return v;
        } else {
            return new Vec2(x + (v.x - x) * ammount, y + (v.y - y) * ammount);
        }
    }

    public double directionTo(Vec2 v) {
        return equals(v) ? 0 : Math.atan2(v.y - y, v.x - x);
    }

    public double directionTo(double x, double y) {
        return equals(x, y) ? 0 : Math.atan2(y - this.y, x - this.x);
    }

    public Vec2 rotate(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        return new Vec2(x * c - y * s, x * s + y * c);
    }

    public Vec2 rotateAround(Vec2 origin, double angle) {
        double x = this.x - origin.x, y = this.y - origin.y;
        double c = Math.cos(angle), s = Math.sin(angle);
        return new Vec2(x * c - y * s, x * s + y * c).add(origin);
    }

    public double distanceTo(Vec2 v) {
        double dx = v.x - x, dy = v.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double sqrDistanceTo(Vec2 v) {
        double dx = v.x - x, dy = v.y - y;
        return dx * dx + dy * dy;
    }

    public double sqrDistanceTo(double x, double y) {
        double dx = x - this.x, dy = y - this.y;
        return dx * dx + dy * dy;
    }

    public double distanceTo(Vec2 v, Metric metric) {
        double dx = v.x - x, dy = v.y - y;
        return metric.distance(dx, dy);
    }

    public double distanceTo(double x, double y, Metric metric) {
        double dx = x - this.x, dy = y - this.y;
        return metric.distance(dx, dy);
    }

    public Vec2 normalize() {
        var m = 1 / length();
        return new Vec2(x * m, y * m);
    }

    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    public Vec2 toLength(double length) {
        var m = length / length();
        return new Vec2(x * m, y * m);
    }

    public Vec2 normal() {
        return new Vec2(y, -x);
    }

    public boolean isZero() {
        return Math.abs(x) < equalityTolerance && Math.abs(y) < equalityTolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec2) {
            var v = (Vec2)o;
            return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
        } else return false;
    }

    public boolean equals(Vec2 v) {
        return Math.abs(x - v.x) < equalityTolerance && Math.abs(y - v.y) < equalityTolerance;
    }

    public boolean equals(double x, double y) {
        return Math.abs(this.x - x) < equalityTolerance && Math.abs(this.y - y) < equalityTolerance;
    }

    public String toString() {
        return x + ";" + y;
    }

    public static double ldx(double distance, double direction) {
        return distance * Math.cos(direction);
    }

    public static double ldx(double distance, double direction, double startX) {
        return startX + distance * Math.cos(direction);
    }

    public static double ldy(double distance, double direction) {
        return distance * Math.sin(direction);
    }

    public static double ldy(double distance, double direction, double startY) {
        return startY + distance * Math.sin(direction);
    }

    public static Vec2 ld(double distance, double direction) {
        return new Vec2(distance * Math.cos(direction), distance * Math.sin(direction));
    }

    public static Vec2 ld(double distance, double direction, double startX, double startY) {
        return new Vec2(
            startX + distance * Math.cos(direction),
            startY + distance * Math.sin(direction)
        );
    }

    public static Vec2 randUnit() {
        var a = Angle.rand();
        return new Vec2(Vec2.ldx(1, a), Vec2.ldy(1, a));
    }

    public static Vec2 randUnit(RandomGenerator rand) {
        var a = Angle.rand(rand);
        return new Vec2(Vec2.ldx(1, a), Vec2.ldy(1, a));
    }

    public static Vec2 randUnit3d() {
        double a = Angle.rand(), a2 = Angle.rand(), len = Vec2.ldx(1, a2);
        return new Vec2(Vec2.ldx(len, a), Vec2.ldy(len, a));
    }

    public static Vec2 randUnit3d(RandomGenerator rand) {
        double a = Angle.rand(rand), a2 = Angle.rand(rand), len = Vec2.ldx(1, a2);
        return new Vec2(Vec2.ldx(len, a), Vec2.ldy(len, a));
    }

    public static Vec2 onEllipse(double r1, double r2, double angle) {
        return new Vec2(Vec2.ldx(r1, angle), Vec2.ldy(r2, angle));
    }

    public static Vec2 onEllipse(double r1, double r2, double angle, Vec2 center) {
        return new Vec2(Vec2.ldx(r1, angle, center.x), Vec2.ldy(r2, angle, center.y));
    }
}

public class Enemy {
    private int state;
    private double X;
    private double Y;
    private double V;
    private double angle;
    private double RV;
    private double radius;
    private double explosion_start;
    private double explosion_end;
    private long nextShot;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRV() {
        return RV;
    }

    public void setRV(double RV) {
        this.RV = RV;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getExplosion_start() {
        return explosion_start;
    }

    public void setExplosion_start(double explosion_start) {
        this.explosion_start = explosion_start;
    }

    public double getExplosion_end() {
        return explosion_end;
    }

    public void setExplosion_end(double explosion_end) {
        this.explosion_end = explosion_end;
    }

    public long getNextShot() {
        return nextShot;
    }

    public void setNextShot(long nextShot) {
        this.nextShot = nextShot;
    }

    public Enemy(int state, double x, double y, double v, double angle, double RV, double radius, double explosion_start, double explosion_end, long nextShot) {
        this.state = state;
        X = x;
        Y = y;
        V = v;
        this.angle = angle;
        this.RV = RV;
        this.radius = radius;
        this.explosion_start = explosion_start;
        this.explosion_end = explosion_end;
        this.nextShot = nextShot;
    }
}

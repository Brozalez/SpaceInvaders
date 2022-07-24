package Principal;
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
    private double enemy2Count;
    private double enemy2SpawnX;
    private long nextShot;
    private long nextEnemy;
    private int count;
    
    public Enemy(int state, double x, double y, double v, double angle, double RV, long nextShot, double radius, long nextEnemy) {
        this.state = state;
        this.explosion_start = 0;
        this.explosion_end = 0;
        X = x;
        Y = y;
        V = v;
        this.angle = angle;
        this.RV = RV;
        this.radius = radius;
        this.nextEnemy = nextEnemy;
    }
    public Enemy(int state, double x, double y, double v, double angle, double RV, long nextShot, double radius, long nextEnemy, double enemy2SpawnX, int count) {
        this.state = state;
        this.explosion_start = 0;
        this.explosion_end = 0;
        this.enemy2SpawnX = GameLib.WIDTH * 0.20;
        this.X = x;
        this.Y = y;
        this.V = v;
        this.angle = angle;
        this.RV = RV;
        this.radius = radius;
        this.nextEnemy = nextEnemy;
        this.count = count;
    }
    public double getenemy2SpawnX() {
        return enemy2Count;
    }
    public void setenemy2SpawnX(double enemy2SpawnX) {
        this.enemy2SpawnX = enemy2SpawnX;
    }


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
        this.X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        this.Y = y;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        this.V = v;
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
        return this.radius;
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
    
    public long getNextEnemy() {
        return nextEnemy;
    }

    public void setNextEnemy(long nextEnemy) {
        this.nextEnemy = nextEnemy;
    }
    
    public void setCount(int count) {
    	this.count = count;
    }
    
    public int getCount() {
    	return this.count;
    }
    

    
}

public class Projectile{
	private int state;
	private double X;
	private double Y;
	private double VX;
	private double VY;
	private double radius;
	
	public Projectile(int state, double X, double Y, double VX, double VY, double radius) {
		this.setState(state);
		this.setX(X);
		this.setY(Y);
		this.setVX(VX);
		this.setVY(VY);
		this.setRadius(radius);
		
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
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public double getVX() {
		return VX;
	}

	public void setVX(double vX) {
		VX = vX;
	}

	public double getVY() {
		return VY;
	}

	public void setVY(double vY) {
		VY = vY;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

}
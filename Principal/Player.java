package Principal;
import java.awt.Color;


public class Player {
	private int state;
	private double X; 
	private double Y; 
	private double VX;
	private double VY;
	private double radius;
	private double explosion_start;
	private double explosion_end;
	private long nextShot;
	
	private static final int EXPLODING = 2;

	
	
	public Player(int state, double X, double Y, double VX, double VY, 
	double radius, double explosion_start, double explosion_end, long nextShot) { //construtor
		this.setState(state);
		this.setX(X);
		this.setY(Y);
		this.setVX(VX);
		this.setVY(VY);
		this.setRadius(radius);
		this.setExplosionStart(explosion_start);
		this.setExplosionEnd(explosion_end);
		this.setNextShot(nextShot);
	}
	
	public void verificaPosicao() {
		if(this.getX() < 0.0) this.setX(0.0);
		if(this.getX() >= GameLib.WIDTH) this.setX(GameLib.WIDTH - 1);
		if(this.getY() < 25.0) this.setY(25.0);
		if(this.getY() >= GameLib.HEIGHT) this.setY(GameLib.HEIGHT -1);
	}
	
	public void desenhaPlayer(long currentTime) {
		if(this.getState() == EXPLODING){
			
			double alpha = (currentTime - this.getExpStart()) / (this.getExpEnd() - this.getExpStart());
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		else{
			
			GameLib.setColor(Color.BLUE);
			GameLib.drawPlayer(this.getX(), this.getY(), this.getRadius());
		}
	}
	
	public void setState(int p) {
		this.state = p;
	}
	
	public void setX(double p) {
		this.X = p;
	}
	
	public void setY(double p) {
		this.Y = p;
	}
	
	public void setVX(double p) {
		this.VX = p;
	}
	
	public void setVY(double p) {
		this.VY = p;
	}
	
	public void setRadius(double p) {
		this.radius = p;
	}
	
	public void setExplosionStart(double p) {
		this.explosion_start = p;
	}
	
	public void setExplosionEnd(double p) {
		this.explosion_end = p;
	}
	
	public void setNextShot(long p) {
		this.nextShot = p;
	}
	
	public int getState() {
		return this.state;
	}
	
	public double getX() {
		return this.X;
	}
	
	public double getY() {
		return this.Y;
	}
	
	public double getVX() {
		return this.VX;
	}
	
	public double getVY() {
		return this.VY;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public double getExpStart() {
		return this.explosion_start;
	}
	
	public double getExpEnd() {
		return this.explosion_end;
	}
	
	public long getNextShot() {
		return this.nextShot;
	}
	
	

}


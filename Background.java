/*
 *	Classe para definir o plano de fundo do jogo (as estrelas).
 *	Esta classe será utilizada para construir o plano primário e secundário do jogo.
 *	Utiliza as implementações do tipo ArrayList para guardar as coordenadas X e Y.
 */

import java.awt.Color;
import java.util.*;

public class Background {

	private double speed;
	private double count;
	private int length; // Para limitar o tamanho da coleção
	private List<Double> X = new ArrayList<>();
	private List<Double> Y = new ArrayList<>();

	// Construtor da classe.
	public Background(double speed, double count, int length) {

		this.speed = speed;
		this.count = count;
		this.length = length;
	}
	
	/*
	 *	Método para inicicializar as coleções X e Y do plano de fundo. 
	 */
	public void startBackground() {
		
		while(this.X.size() < this.length) {
			
			this.X.add(Double.valueOf((Math.random() * GameLib.WIDTH)));
			this.Y.add(Double.valueOf(Math.random() * GameLib.HEIGHT));
		}
	}
	
	/*
	 *	Método para desenhar o plano de fundo primário (próximo). 
	 *	Como as coleções guardam objetos do tipo Double, é preciso convertê-los para o tipo primitivo double.
	 *	Estrelas de tamanho 3.
	 */
	public void drawBackgroundPrimary(long delta) {
		
		GameLib.setColor(Color.GRAY);
		this.count += this.speed * delta;
		Iterator<Double> i = this.X.iterator();	// Iterador da coleção X
		Iterator<Double> j = this.Y.iterator(); // Iterador da coleção Y

		while(i.hasNext() && j.hasNext()) {

			GameLib.fillRect(i.next().doubleValue(), (j.next().doubleValue() + this.count) % GameLib.HEIGHT, 3, 3);
		}
	}
	
	/*
	 *	Método para desenhar o plano de fundo secundário (distante).
	 *	Estrelas de tamanho 2. 
	 */
	public void drawBackgroundSecondary(long delta) {
		
		GameLib.setColor(Color.DARK_GRAY);
		this.count += this.speed * delta;
		Iterator<Double> i = this.X.iterator();	// Iterador da coleção X
		Iterator<Double> j = this.Y.iterator(); // Iterador da coleção Y

		while(i.hasNext() && j.hasNext()) {

			GameLib.fillRect(i.next().doubleValue(), (j.next().doubleValue() + this.count) % GameLib.HEIGHT, 2, 2);
		}
	}
}

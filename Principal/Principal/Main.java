package Principal;
import java.awt.Color;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/***********************************************************************/
/*                                                                     */
/* Para jogar:                                                         */
/*                                                                     */
/*    - cima, baixo, esquerda, direita: movimentaÃ§Ã£o do player.        */
/*    - control: disparo de projÃ©teis.                                 */
/*    - ESC: para sair do jogo.                                        */
/*                                                                     */
/***********************************************************************/

public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	public static final int enemyNumber = 10;
	public static long conta = 0;
	public static int id = 0;
	
	
	
	
	/* Espera, sem fazer nada, atÃ© que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parÃ¢metro "time.    */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
	/* Encontra e devolve o primeiro Ã­ndice do  */
	/* array referente a uma posiÃ§Ã£o "inativa". */
	
	public static int findFreeIndex(int [] stateArray){
		
		int i;
		
		for(i = 0; i < stateArray.length; i++){
			
			if(stateArray[i] == INACTIVE) break;
		}
		
		return i;
	}
	
	/* Encontra e devolve o conjunto de Ã­ndices (a quantidade */
	/* de Ã­ndices Ã© defnida atravÃ©s do parÃ¢metro "amount") do */
	/* array referente a posiÃ§Ãµes "inativas".                 */ 

	public static int [] findFreeIndex(int [] stateArray, int amount){

		int i, k;
		int [] freeArray = new int[amount];

		for(i = 0; i < freeArray.length; i++) freeArray[i] = stateArray.length; 
		
		for(i = 0, k = 0; i < stateArray.length && k < amount; i++){
				
			if(stateArray[i] == INACTIVE) { 
				
				freeArray[k] = i; 
				k++;
			}
		}
		
		return freeArray;
	}
	
	/* MÃ©todo principal */
	
	public static void main(String [] args){

		/* Indica que o jogo estÃ¡ em execuÃ§Ã£o */

		boolean running = true;

		/* variÃ¡veis usadas no controle de tempo efetuado no main loop */

		long delta;
		long currentTime = System.currentTimeMillis();

		//INSTANCIA O JOGADOR (ANTES ERA aqui embaixo QUE TA COMENTADAA AGORA)

		Player player = new Player(ACTIVE, GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, 0, 0, currentTime);


		/* variÃ¡veis dos projÃ©teis disparados pelo player */

		int [] projectile_states = new int[10];					// estados
		double [] projectile_X = new double[10];				// coordenadas x
		double [] projectile_Y = new double[10];				// coordenadas y
		double [] projectile_VX = new double[10];				// velocidades no eixo x
		double [] projectile_VY = new double[10];				// velocidades no eixo y

		/* variÃ¡veis dos inimigos tipo 1 */



		Enemy[] enemy1 = new Enemy[10];//lista de 10 inimigos1




		double enemy1_radius = 9.0;						// raio (tamanho do inimigo 1)

		/* variÃ¡veis dos inimigos tipo 2 */

		Enemy[] enemy2 = new Enemy[10];//lista de 10 inimigos2



		int [] enemy2_states = new int[10];					// estados
		double [] enemy2_X = new double[10];					// coordenadas x
		double [] enemy2_Y = new double[10];					// coordenadas y
		double [] enemy2_V = new double[10];					// velocidades
		double [] enemy2_angle = new double[10];				// Ã¢ngulos (indicam direÃ§Ã£o do movimento)
		double [] enemy2_RV = new double[10];					// velocidades de rotaÃ§Ã£o
		double [] enemy2_explosion_start = new double[10];			// instantes dos inÃ­cios das explosÃµes
		double [] enemy2_explosion_end = new double[10];			// instantes dos finais das explosÃµes
		double enemy2_spawnX = GameLib.WIDTH * 0.20;				// coordenada x do prÃ³ximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;							// contagem de inimigos tipo 2 (usada na "formaÃ§Ã£o de voo")
		double enemy2_radius = 12.0;						// raio (tamanho aproximado do inimigo 2)
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer

		/* variÃ¡veis dos projÃ©teis lanÃ§ados pelos inimigos (tanto tipo 1, quanto tipo 2) */

		int [] e_projectile_states = new int[200];				// estados
		double [] e_projectile_X = new double[200];				// coordenadas x
		double [] e_projectile_Y = new double[200];				// coordenadas y
		double [] e_projectile_VX = new double[200];				// velocidade no eixo x
		double [] e_projectile_VY = new double[200];				// velocidade no eixo y
		double e_projectile_radius = 2.0;					// raio (tamanho dos projÃ©teis inimigos)

		/* estrelas que formam o fundo do primeiro plano */
		Background primaryBackground = new Background(0.070, 0.0, 20);

		/* estrelas que formam o fundo do segundo plano */
		Background secondaryBackground = new Background(0.045, 0.0, 50);


		/* inicializaÃ§Ãµes */

		for(int i = 0; i < projectile_states.length; i++) projectile_states[i] = INACTIVE;
		for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
		for (int i = 0; i < enemyNumber; i++) {
			enemy1[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 9, currentTime + 2000*i);
		}
		for (int i = 0; i < enemyNumber; i++) {
			enemy2[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2, 0, currentTime + 500, 9, currentTime + 5000);
		}
		for(int i = 0; i < enemyNumber; i++) enemy2[i].setState(INACTIVE);//ok

		// Iniciando primeiro plano
		primaryBackground.startBackground();

		// Iniciando segundo plano
		secondaryBackground.startBackground();

		/* iniciado interface grÃ¡fica */

		GameLib.initGraphics();
		//GameLib.initGraphics_SAFE_MODE();  // chame esta versÃ£o do mÃ©todo caso nada seja desenhado na janela do jogo.

		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/* -----------------                                                                             */
		/*                                                                                               */
		/* O main loop do jogo executa as seguintes operaÃ§Ãµes:                                           */
		/*                                                                                               */
		/* 1) Verifica se hÃ¡ colisÃµes e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu entre a Ãºltima atualizaÃ§Ã£o     */
		/*    e o timestamp atual: posiÃ§Ã£o e orientaÃ§Ã£o, execuÃ§Ã£o de disparos de projÃ©teis, etc.         */
		/*                                                                                               */
		/* 3) Processa entrada do usuÃ¡rio (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um perÃ­odo de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/

		while(running){


			/* Usada para atualizar o estado dos elementos do jogo    */
			/* (player, projÃ©teis e inimigos) "delta" indica quantos  */
			/* ms se passaram desde a Ãºltima atualizaÃ§Ã£o.             */

			delta = System.currentTimeMillis() - currentTime;

			/* JÃ¡ a variÃ¡vel "currentTime" nos dÃ¡ o timestamp atual.  */

			currentTime = System.currentTimeMillis();

			/***************************/
			/* VerificaÃ§Ã£o de colisÃµes */
			/***************************/

			if(player.getState() == ACTIVE){


				/* colisÃµes player - projeteis (inimigo) */

				for(int i = 0; i < e_projectile_states.length; i++){

					double dx = e_projectile_X[i] - player.getX();
					double dy = e_projectile_Y[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);


					if(dist < (player.getRadius() + e_projectile_radius) * 0.8){


						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}

				/* colisÃµes player - inimigos */

				for(int i = 0; i < enemyNumber; i++){

					double dx = enemy1[i].getX() - player.getX();//ok
					double dy = enemy1[i].getY() - player.getY();//ok
					double dist = Math.sqrt(dx * dx + dy * dy);

					if(dist < (player.getRadius() + enemy1_radius) * 0.8){

						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}

				for(int i = 0; i < enemy2_states.length; i++){

					double dx = enemy2_X[i] - player.getX();
					double dy = enemy2_Y[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);

					if(dist < (player.getRadius() + enemy2_radius) * 0.8){

						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}
			}

			/* colisÃµes projeteis (player) - inimigos */

			for(int k = 0; k < projectile_states.length; k++){

				for(int i = 0; i < enemyNumber; i++){//ok

					if(enemy1[i].getState() == ACTIVE){//ok

						double dx = enemy1[i].getX() - projectile_X[k];
						double dy = enemy1[i].getY() - projectile_Y[k];
						double dist = Math.sqrt(dx * dx + dy * dy);

						if(dist < enemy1_radius){

							enemy1[i].setState(EXPLODING);//ok
							enemy1[i].setExplosion_start(currentTime);
							enemy1[i].setExplosion_end(currentTime + 500);
						}
					}
				}

				for(int i = 0; i < enemyNumber; i++){

					if(enemy2_states[i] == ACTIVE){

						double dx = enemy2_X[i] - projectile_X[k];
						double dy = enemy2_Y[i] - projectile_Y[k];
						double dist = Math.sqrt(dx * dx + dy * dy);

						if(dist < enemy2_radius){

							enemy2_states[i] = EXPLODING;
							enemy2_explosion_start[i] = currentTime;
							enemy2_explosion_end[i] = currentTime + 500;
						}
					}
				}
			}

			/***************************/
			/* AtualizaÃ§Ãµes de estados */
			/***************************/

			/* projeteis (player) */

			for(int i = 0; i < projectile_states.length; i++){

				if(projectile_states[i] == ACTIVE){

					/* verificando se projÃ©til saiu da tela */
					if(projectile_Y[i] < 0) {

						projectile_states[i] = INACTIVE;
					}
					else {

						projectile_X[i] += projectile_VX[i] * delta;
						projectile_Y[i] += projectile_VY[i] * delta;
					}
				}
			}

			/* projeteis (inimigos) */

			for(int i = 0; i < e_projectile_states.length; i++){

				if(e_projectile_states[i] == ACTIVE){

					/* verificando se projÃ©til saiu da tela */
					if(e_projectile_Y[i] > GameLib.HEIGHT) {

						e_projectile_states[i] = INACTIVE;
					}
					else {

						e_projectile_X[i] += e_projectile_VX[i] * delta;
						e_projectile_Y[i] += e_projectile_VY[i] * delta;
					}
				}
			}

			/* inimigos tipo 1 */

			for(int i = 0; i < enemyNumber; i++){//ok

				if(enemy1[i].getState() == EXPLODING){//ok

					if(currentTime > enemy1[i].getExplosion_end()){//ok

						enemy1[i].setState(INACTIVE);//ok
						enemy1[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						enemy1[i].setY(-10);
					}
				}


				if(enemy1[i].getState() == ACTIVE){//OK

					/* verificando se inimigo saiu da tela */
					if(enemy1[i].getY() > GameLib.HEIGHT + 10) {//OK


						enemy1[i].setState(INACTIVE);//OK
						enemy1[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						enemy1[i].setY(-10);

					}
					else {

						enemy1[i].setX(enemy1[i].getX() + enemy1[i].getV() * Math.cos(enemy1[i].getAngle()) * delta);//ok
						enemy1[i].setY(enemy1[i].getY() + enemy1[i].getV() * Math.sin(enemy1[i].getAngle()) * delta * (-1.0));//ok
						enemy1[i].setAngle(enemy1[i].getAngle()+ enemy1[i].getRV() * delta); //ok

						if(currentTime > enemy1[i].getNextShot() && enemy1[i].getY() < player.getY()){//ok

							int free = findFreeIndex(e_projectile_states);

							if(free < e_projectile_states.length){

								e_projectile_X[free] = enemy1[i].getX();
								e_projectile_Y[free] = enemy1[i].getY();
								e_projectile_VX[free] = Math.cos(enemy1[i].getAngle()) * 0.45;
								e_projectile_VY[free] = Math.sin(enemy1[i].getAngle()) * 0.45 * (-1.0);
								e_projectile_states[free] = ACTIVE;

								enemy1[i].setNextShot((long) (currentTime + 200 + Math.random() * 500));//ok
							}
						}
					}
				}
			}

			/* inimigos tipo 2 */

			for(int i = 0; i < enemy2_states.length; i++){

				if(enemy2_states[i] == EXPLODING){

					if(currentTime > enemy2_explosion_end[i]){

						enemy2_states[i] = INACTIVE;
					}
				}

				if(enemy2_states[i] == ACTIVE){

					/* verificando se inimigo saiu da tela */
					if(	enemy2_X[i] < -10 || enemy2_X[i] > GameLib.WIDTH + 10 ) {

						enemy2_states[i] = INACTIVE;
					}
					else {

						boolean shootNow = false;
						double previousY = enemy2_Y[i];

						enemy2_X[i] += enemy2_V[i] * Math.cos(enemy2_angle[i]) * delta;
						enemy2_Y[i] += enemy2_V[i] * Math.sin(enemy2_angle[i]) * delta * (-1.0);
						enemy2_angle[i] += enemy2_RV[i] * delta;

						double threshold = GameLib.HEIGHT * 0.30;

						if(previousY < threshold && enemy2_Y[i] >= threshold) {

							if(enemy2_X[i] < GameLib.WIDTH / 2) enemy2_RV[i] = 0.003;
							else enemy2_RV[i] = -0.003;
						}

						if(enemy2_RV[i] > 0 && Math.abs(enemy2_angle[i] - 3 * Math.PI) < 0.05){

							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 3 * Math.PI;
							shootNow = true;
						}

						if(enemy2_RV[i] < 0 && Math.abs(enemy2_angle[i]) < 0.05){

							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 0.0;
							shootNow = true;
						}

						if(shootNow){

							double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
							int [] freeArray = findFreeIndex(e_projectile_states, angles.length);

							for(int k = 0; k < freeArray.length; k++){

								int free = freeArray[k];

								if(free < e_projectile_states.length){

									double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;
									double vx = Math.cos(a);
									double vy = Math.sin(a);

									e_projectile_X[free] = enemy2_X[i];
									e_projectile_Y[free] = enemy2_Y[i];
									e_projectile_VX[free] = vx * 0.30;
									e_projectile_VY[free] = vy * 0.30;
									e_projectile_states[free] = ACTIVE;
								}
							}
						}
					}
				}
			}

			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */

	 		conta += delta;

						if(conta >= 2000) {
							conta = 0;
							id ++;
							if(id>9) id = 0;
							if (enemy1[id].getState() != EXPLODING) {
								enemy1[id].setState(ACTIVE);
							}
						}
			
			/* verificando se novos inimigos (tipo 2) devem ser "lanÃ§ados" */
			
			if(currentTime > nextEnemy2){
				
				int free = findFreeIndex(enemy2_states);
								
				if(free < enemy2_states.length){
					
					enemy2_X[free] = enemy2_spawnX;
					enemy2_Y[free] = -10.0;
					enemy2_V[free] = 0.42;
					enemy2_angle[free] = (3 * Math.PI) / 2;
					enemy2_RV[free] = 0.0;
					enemy2_states[free] = ACTIVE;

					enemy2_count++;
					
					if(enemy2_count < 10){
						
						nextEnemy2 = currentTime + 120;
					}
					else {
						
						enemy2_count = 0;
						enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
						nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
					}
				}
			}
			
			/* Verificando se a explosÃ£o do player jÃ¡ acabou.         */
			/* Ao final da explosÃ£o, o player volta a ser controlÃ¡vel */
			if(player.getState() == EXPLODING){
				
				if(currentTime > player.getExpEnd()){
					
					player.setState(ACTIVE);
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuÃ¡rio (teclado) */
			/********************************************/
			
			if(player.getState() == ACTIVE){
				
				if(GameLib.iskeyPressed(GameLib.KEY_UP)) player.moverCima(delta);
				if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player.moverBaixo(delta);
				if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player.moverEsquerda(delta);
				if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player.moverDireita(delta);
				
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(currentTime > player.getNextShot()){
						
						int free = findFreeIndex(projectile_states);
												
						if(free < projectile_states.length){
							
							projectile_X[free] = player.getX();
							projectile_Y[free] = player.getY() - 2 * player.getRadius();
							projectile_VX[free] = 0.0;
							projectile_VY[free] = -1.0;
							projectile_states[free] = ACTIVE;
							player.setNextShot(currentTime + 100);
						}
					}	
				}
			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estÃ£o dentro */
			/* da tela de jogo apÃ³s processar entrada do usuÃ¡rio.      */
			
			player.verificaPosicao(); //fun��es que checam a posi��o para o player n�oo sair da tela foram para a classe player

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano de fundo próximo/primário */
			primaryBackground.drawBackgroundPrimary(delta);
			
			/* desenhando plano de fundo distante/secundário */
			secondaryBackground.drawBackgroundSecondary(delta);
						
			/* desenhando player */
			
			player.desenhaPlayer(currentTime);
				
			/* deenhando projeteis (player) */
			
			for(int i = 0; i < projectile_states.length; i++){
				
				if(projectile_states[i] == ACTIVE){
					
					GameLib.setColor(Color.GREEN);
					GameLib.drawLine(projectile_X[i], projectile_Y[i] - 5, projectile_X[i], projectile_Y[i] + 5);
					GameLib.drawLine(projectile_X[i] - 1, projectile_Y[i] - 3, projectile_X[i] - 1, projectile_Y[i] + 3);
					GameLib.drawLine(projectile_X[i] + 1, projectile_Y[i] - 3, projectile_X[i] + 1, projectile_Y[i] + 3);
				}
			}
			
			/* desenhando projeteis (inimigos) */
		
			for(int i = 0; i < e_projectile_states.length; i++){
				
				if(e_projectile_states[i] == ACTIVE){
	
					GameLib.setColor(Color.RED);
					GameLib.drawCircle(e_projectile_X[i], e_projectile_Y[i], e_projectile_radius);
				}
			}
			
			/* desenhando inimigos (tipo 1) */
			
			for(int i = 0; i < enemyNumber; i++){//ok
				
				if(enemy1[i].getState() == EXPLODING){//ok
					
					double alpha = (currentTime - enemy1[i].getExplosion_start()) / (enemy1[i].getExplosion_end() - enemy1[i].getExplosion_start());//ok
					GameLib.drawExplosion(enemy1[i].getX(), enemy1[i].getY(), alpha);//ok
				}
				
				if(enemy1[i].getState() == ACTIVE){//ok
			
					GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemy1[i].getX(), enemy1[i].getY(), enemy1_radius);//ok
				}
			}
			
			/* desenhando inimigos (tipo 2) */
			
			for(int i = 0; i < enemy2_states.length; i++){
				
				if(enemy2_states[i] == EXPLODING){
					
					double alpha = (currentTime - enemy2_explosion_start[i]) / (enemy2_explosion_end[i] - enemy2_explosion_start[i]);
					GameLib.drawExplosion(enemy2_X[i], enemy2_Y[i], alpha);
				}
				
				if(enemy2_states[i] == ACTIVE){
			
					GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemy2_X[i], enemy2_Y[i], enemy2_radius);
				}
			}
			
			/* chamada a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execuÃ§Ã£o do laÃ§o do main loop demore aproximadamente 3 ms. */
			
			busyWait(currentTime + 3);
		}
		
		System.exit(0);
	}
}

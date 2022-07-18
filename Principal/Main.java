package Principal;
import java.awt.Color;

/***********************************************************************/
/*                                                                     */
/* Para jogar:                                                         */
/*                                                                     */
/*    - cima, baixo, esquerda, direita: movimentaÃ§Ã£o do player.        */
/*    - control: disparo de projeteis.                                 */
/*    - ESC: para sair do jogo.                                        */
/*                                                                     */
/***********************************************************************/

public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	
	
	
	
	
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

		/* variaveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();
		
		//INSTANCIA O JOGADOR (ANTES ERA aqui embaixo QUE TA COMENTADAA AGORA)
		
		Player player = new Player(ACTIVE, GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, 0, 0, currentTime);


		/* variaveis dos projeteis disparados pelo player */
		
		int [] projectile_states = new int[10];					// estados
		double [] projectile_X = new double[10];				// coordenadas x
		double [] projectile_Y = new double[10];				// coordenadas y
		double [] projectile_VX = new double[10];				// velocidades no eixo x
		double [] projectile_VY = new double[10];				// velocidades no eixo y

		/* variaveis dos inimigos tipo 1 */
		
		int [] enemy1_states = new int[10];					// estados
		double [] enemy1_X = new double[10];					// coordenadas x
		double [] enemy1_Y = new double[10];					// coordenadas y
		double [] enemy1_V = new double[10];					// velocidades
		double [] enemy1_angle = new double[10];				// Ã¢ngulos (indicam direÃ§Ã£o do movimento)
		double [] enemy1_RV = new double[10];					// velocidades de rotaÃ§Ã£o
		double [] enemy1_explosion_start = new double[10];			// instantes dos inÃ­cios das explosÃµes
		double [] enemy1_explosion_end = new double[10];			// instantes dos finais da explosÃµes
		long [] enemy1_nextShoot = new long[10];				// instantes do prÃ³ximo tiro
		double enemy1_radius = 9.0;								// raio (tamanho do inimigo 1)
		long nextEnemy1 = currentTime + 2000;					// instante em que um novo inimigo 1 deve aparecer
		
		/* variaveis dos inimigos tipo 2 */
		
		int [] enemy2_states = new int[10];						// estados
		double [] enemy2_X = new double[10];					// coordenadas x
		double [] enemy2_Y = new double[10];					// coordenadas y
		double [] enemy2_V = new double[10];					// velocidades
		double [] enemy2_angle = new double[10];				// Ã¢ngulos (indicam direÃ§Ã£o do movimento)
		double [] enemy2_RV = new double[10];					// velocidades de rotaÃ§Ã£o
		double [] enemy2_explosion_start = new double[10];		// instantes dos inÃ­cios das explosÃµes
		double [] enemy2_explosion_end = new double[10];		// instantes dos finais das explosÃµes
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do prÃ³ximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;									// contagem de inimigos tipo 2 (usada na "formaÃ§Ã£o de voo")
		double enemy2_radius = 12.0;							// raio (tamanho aproximado do inimigo 2)
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer

		/* variaveis dos inimigos tipo 3 */
		
		int [] enemy3_states = new int[10];						// estados
		double [] enemy3_X = new double[10];					// coordenadas x
		double [] enemy3_Y = new double[10];					// coordenadas y
		double [] enemy3_V = new double[10];					// velocidades
		double [] enemy3_angle = new double[10];				// Ã¢ngulos (indicam direÃ§Ã£o do movimento)
		double [] enemy3_RV = new double[10];					// velocidades de rotaÃ§Ã£o
		double [] enemy3_explosion_start = new double[10];		// instantes dos inÃ­cios das explosÃµes
		double [] enemy3_explosion_end = new double[10];		// instantes dos finais das explosÃµes
		long [] enemy3_nextShoot = new long[10];				// instantes do prÃ³ximo tiro
		double enemy3_radius = 10.0;							// raio (tamanho aproximado do inimigo 3)
		long nextEnemy3 = currentTime + 1000;					// instante em que um novo inimigo 3 deve aparecer


		/* variaveis do powerup */
		
		int [] powerup_states = new int[10];					// estados
		double [] powerup_X = new double[10];					// coordenadas x
		double [] powerup_Y = new double[10];					// coordenadas y
		double [] powerup_V = new double[10];					// velocidades
		double [] powerup_angle = new double[10];				// Ã¢ngulos (indicam direÃ§Ã£o do movimento)
		double [] powerup_RV = new double[10];					// velocidades de rotaÃ§Ã£o
		double powerup_radius = 5.0;						// raio (tamanho do inimigo 1)
		long nextpowerup = currentTime + 20000;					// instante em que um novo inimigo 1 deve aparecer


		/* variaveis dos projeteis lancados pelos inimigos (tanto tipo 1, 2 e 3) */
		
		int [] e_projectile_states = new int[200];				// estados
		double [] e_projectile_X = new double[200];				// coordenadas x
		double [] e_projectile_Y = new double[200];				// coordenadas y
		double [] e_projectile_VX = new double[200];				// velocidade no eixo x
		double [] e_projectile_VY = new double[200];				// velocidade no eixo y
		double e_projectile_radius = 2.0;					// raio (tamanho dos projeteis inimigos)
		
		/* estrelas que formam o fundo do primeiro plano */
		Background primaryBackground = new Background(0.070, 0.0, 20);
		
		/* estrelas que formam o fundo do segundo plano */
		Background secondaryBackground = new Background(0.045, 0.0, 50);
		
		
		/* inicializaÃ§Ãµes */
		
		for(int i = 0; i < projectile_states.length; i++) projectile_states[i] = INACTIVE;
		for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
		for(int i = 0; i < enemy1_states.length; i++) enemy1_states[i] = INACTIVE;
		for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE;
		for(int i = 0; i < enemy3_states.length; i++) enemy3_states[i] = INACTIVE;
		for(int i = 0; i < powerup_states.length; i++) powerup_states[i] = INACTIVE;
		
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
		/*    e o timestamp atual: posiÃ§Ã£o e orientaÃ§Ã£o, execuÃ§Ã£o de disparos de projeteis, etc.         */
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
			/* (player, projeteis e inimigos) "delta" indica quantos  */
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
							
				for(int i = 0; i < enemy1_states.length; i++){
					
					double dx = enemy1_X[i] - player.getX();
					double dy = enemy1_Y[i] - player.getY();
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

				for(int i = 0; i < enemy3_states.length; i++){
					
					double dx = enemy3_X[i] - player.getX();
					double dy = enemy3_Y[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy3_radius) * 0.8){
						
						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}


				/* colisoes player - powerup */

				for(int i = 0; i < powerup_states.length; i++){
					
					double dx = powerup_X[i] - player.getX();
					double dy = powerup_Y[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + powerup_radius) * 0.8){
						player.setState(ACTIVE);
						powerup_states[i] = INACTIVE;
						// player.setState(ACTIVE);
						for (int q = 0; q < 10; q = q + 1){
							enemy1_states[q] = EXPLODING;
							enemy1_explosion_start[q] = currentTime;
							enemy1_explosion_end[q] = currentTime + 500;
							enemy2_states[q] = EXPLODING;
							enemy2_explosion_start[q] = currentTime;
							enemy2_explosion_end[q] = currentTime + 500;
	
						}
					}
				}


			}
			
			/* colisÃµes projeteis (player) - inimigos */
			
			for(int k = 0; k < projectile_states.length; k++){
				
				for(int i = 0; i < enemy1_states.length; i++){
										
					if(enemy1_states[i] == ACTIVE){
					
						double dx = enemy1_X[i] - projectile_X[k];
						double dy = enemy1_Y[i] - projectile_Y[k];
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy1_radius){
							
							enemy1_states[i] = EXPLODING;
							enemy1_explosion_start[i] = currentTime;
							enemy1_explosion_end[i] = currentTime + 500;
						}
					}
				}
				
				for(int i = 0; i < enemy2_states.length; i++){
					
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

				for(int i = 0; i < enemy3_states.length; i++){
					
					if(enemy3_states[i] == ACTIVE){
						
						double dx = enemy3_X[i] - projectile_X[k];
						double dy = enemy3_Y[i] - projectile_Y[k];
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy3_radius){
							
							enemy3_states[i] = EXPLODING;
							enemy3_explosion_start[i] = currentTime;
							enemy3_explosion_end[i] = currentTime + 500;
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
			
			for(int i = 0; i < enemy1_states.length; i++){
				
				if(enemy1_states[i] == EXPLODING){
					
					if(currentTime > enemy1_explosion_end[i]){
						
						enemy1_states[i] = INACTIVE;
					}
				}
				
				if(enemy1_states[i] == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemy1_Y[i] > GameLib.HEIGHT + 10) {
						
						enemy1_states[i] = INACTIVE;
					}
					else {
					
						enemy1_X[i] += enemy1_V[i] * Math.cos(enemy1_angle[i]) * delta;
						enemy1_Y[i] += enemy1_V[i] * Math.sin(enemy1_angle[i]) * delta * (-1.0);
						enemy1_angle[i] += enemy1_RV[i] * delta;
						
						if(currentTime > enemy1_nextShoot[i] && enemy1_Y[i] < player.getY()){
																							
							int free = findFreeIndex(e_projectile_states);
							
							if(free < e_projectile_states.length){
								
								e_projectile_X[free] = enemy1_X[i];
								e_projectile_Y[free] = enemy1_Y[i];
								e_projectile_VX[free] = Math.cos(enemy1_angle[i]) * 0.45;
								e_projectile_VY[free] = Math.sin(enemy1_angle[i]) * 0.45 * (-1.0);
								e_projectile_states[free] = ACTIVE;
								
								enemy1_nextShoot[i] = (long) (currentTime + 200 + Math.random() * 500);
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

			/* inimigos tipo 3 */
			
			for(int i = 0; i < enemy3_states.length; i++){
				
				if(enemy3_states[i] == EXPLODING){
					
					if(currentTime > enemy3_explosion_end[i]){
						
						enemy3_states[i] = INACTIVE;
					}
				}
				
				if(enemy3_states[i] == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemy3_Y[i] > GameLib.HEIGHT + 10) {
						
						enemy3_states[i] = INACTIVE;
					}
					else {
					
						enemy3_X[i] += enemy3_V[i] -1.0;
						enemy3_Y[i] += enemy3_V[i] * Math.sin(enemy3_angle[i]) * delta * (-0.7);
						enemy3_angle[i] += enemy3_RV[i] * delta;
						
						if(currentTime > enemy3_nextShoot[i] && enemy3_Y[i] < player.getY()){
																							
							int free = findFreeIndex(e_projectile_states);
							
							if(free < e_projectile_states.length){
								
								e_projectile_X[free] = enemy3_X[i];
								e_projectile_Y[free] = enemy3_Y[i];
								e_projectile_VX[free] = Math.cos(enemy3_angle[i]) * 0.45;
								e_projectile_VY[free] = Math.sin(enemy3_angle[i]) * 0.45 * (-1.0);
								e_projectile_states[free] = ACTIVE;
								
								enemy3_nextShoot[i] = (long) (currentTime + 200);
							}
						}
					}
				}
			}



			//Powerup

			for(int i = 0; i < powerup_states.length; i++){
				
				if(powerup_states[i] == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(powerup_Y[i] > GameLib.HEIGHT + 10) {
						
						powerup_states[i] = INACTIVE;
					}
					else {
					
						powerup_X[i] += powerup_V[i] * Math.cos(powerup_angle[i]) * delta;
						powerup_Y[i] += powerup_V[i] * Math.sin(powerup_angle[i]) * delta * (-1.0);
						powerup_angle[i] += powerup_RV[i] * delta;
						
					}
				}
			}

			
			/* verificando se novos inimigos (tipo 1) devem ser "lancados" */
			
			if(currentTime > nextEnemy1){
				
				int free = findFreeIndex(enemy1_states);
								
				if(free < enemy1_states.length){
					
					enemy1_X[free] = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
					enemy1_Y[free] = -10.0;
					enemy1_V[free] = 0.20 + Math.random() * 0.15;
					enemy1_angle[free] = (3 * Math.PI) / 2;
					enemy1_RV[free] = 0.0;
					enemy1_states[free] = ACTIVE;
					enemy1_nextShoot[free] = currentTime + 500;
					nextEnemy1 = currentTime + 500;
				}
			}
			
			/* verificando se novos inimigos (tipo 2) devem ser "lancados" */
			
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

			/* verificando se novos inimigos (tipo 3) devem ser "lancados" */
			
			if(currentTime > nextEnemy3){
				
				int free = findFreeIndex(enemy3_states);
								
				if(free < enemy3_states.length){
					
					enemy3_X[free] = (GameLib.WIDTH - 20.0) + 10.0;
					enemy3_Y[free] = -10.0;
					enemy3_V[free] = 0.20 + Math.random() * 0.15;
					enemy3_angle[free] = (3 * Math.PI) / 2;
					enemy3_RV[free] = 0.0;
					enemy3_states[free] = ACTIVE;
					enemy3_nextShoot[free] = currentTime + 500;
					nextEnemy3 = currentTime + 1500;
				}
			}


			/* verificando se novos Powerups devem ser "lancados" */
			
			if(currentTime > nextpowerup){
				
				int free = findFreeIndex(powerup_states);
											
				if(free < powerup_states.length){
								
					powerup_X[free] = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
					powerup_Y[free] = -10.0;
					powerup_V[free] = 0.20 + Math.random() * 0.15;
					powerup_angle[free] = (3 * Math.PI) / 2;
					powerup_RV[free] = 0.0;
					powerup_states[free] = ACTIVE;
					nextpowerup = currentTime + 50000;
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
			
			for(int i = 0; i < enemy1_states.length; i++){
				
				if(enemy1_states[i] == EXPLODING){
					
					double alpha = (currentTime - enemy1_explosion_start[i]) / (enemy1_explosion_end[i] - enemy1_explosion_start[i]);
					GameLib.drawExplosion(enemy1_X[i], enemy1_Y[i], alpha);
				}
				
				if(enemy1_states[i] == ACTIVE){
			
					GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemy1_X[i], enemy1_Y[i], enemy1_radius);
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

			/* desenhando inimigos (tipo 3) */
			
			for(int i = 0; i < enemy3_states.length; i++){
				
				if(enemy3_states[i] == EXPLODING){
					
					double alpha = (currentTime - enemy3_explosion_start[i]) / (enemy3_explosion_end[i] - enemy3_explosion_start[i]);
					GameLib.drawExplosion(enemy3_X[i], enemy3_Y[i], alpha);
				}
				
				if(enemy3_states[i] == ACTIVE){
			
					GameLib.setColor(Color.orange);
					GameLib.drawTriangle(enemy3_X[i], enemy3_Y[i], enemy3_radius);
				}
			}


			/* desenhando powerup */
			
			for(int i = 0; i < powerup_states.length; i++){
								
				if(powerup_states[i] == ACTIVE){
			
					GameLib.setColor(Color.PINK);
					GameLib.drawCircle(powerup_X[i], powerup_Y[i], powerup_radius);
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

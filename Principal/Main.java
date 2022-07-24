package Principal;
import java.awt.Color;
import java.util.ArrayList;

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
	public static final int PROJECTILE_NUMBER = 10;
	public static final int E_PROJECTILE_NUMBER = 200; 
	public static final int ENEMY_NUMBER = 10;
	public static long conta = 0;
	public static int id = 0;
	
	
	
	
	
	
	/* Espera, sem fazer nada, atÃ© que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parÃ¢metro "time.    */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
	/* Encontra e devolve o primeiro Ã­ndice do  */
	/* array referente a uma posiÃ§Ã£o "inativa". */
	
	public static int findFreeIndexEProjectile(ArrayList<Projectile> stateArray){
		
		int i;		
		for(i = 0; i < E_PROJECTILE_NUMBER; i++){
			
			if(stateArray.get(i).getState() == INACTIVE) break;
		}
		
		return i;
	}
	
public static int findFreeIndexProjectile(ArrayList<Projectile> stateArray){
		
		int i;		
		for(i = 0; i < PROJECTILE_NUMBER; i++){
			
			if(stateArray.get(i).getState() == INACTIVE) break;
		}
		
		return i;
	}
	
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

	public static int [] findFreeIndexEProjectile(ArrayList<Projectile> stateArray, int amount){

		int i, k;
		int [] freeArray = new int[amount];

		for(i = 0; i < freeArray.length; i++) freeArray[i] = E_PROJECTILE_NUMBER; 
		
		for(i = 0, k = 0; i < E_PROJECTILE_NUMBER && k < amount; i++){
				
			if(stateArray.get(i).getState() == INACTIVE) { 
				
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
		ArrayList<Projectile> e_projectile = new ArrayList<Projectile>(E_PROJECTILE_NUMBER);
        ArrayList<Projectile> projectile = new ArrayList<Projectile>(PROJECTILE_NUMBER);
        for(int i = 0; i< E_PROJECTILE_NUMBER;i++) {
        	e_projectile.add(new Projectile(0,0.0,0.0,0.0,0.0));
        	e_projectile.get(i).setRadius(2);
        }
        for(int i = 0; i< PROJECTILE_NUMBER;i++) projectile.add(new Projectile(0,0.0,0.0,0.0,0.0));
        
        Enemy[] enemy1 = new Enemy[ENEMY_NUMBER];
		double enemy1_radius = 9.0;						// raio (tamanho do inimigo 1)
        Enemy[] enemy3 = new Enemy[ENEMY_NUMBER];
		double enemy3_radius = 10.0;						// raio (tamanho do inimigo 3)
        Enemy[] powerup = new Enemy[ENEMY_NUMBER];
		double powerup_radius = 5.0;						// raio (tamanho do Powerup)


		//Enemy[] enemy2 = new Enemy[ENEMY_NUMBER];
		
		/* variaveis dos inimigos tipo 2 */
		
		int [] enemy2_states = new int[10];						// estados
		double [] enemy2_X = new double[10];					// coordenadas x
		double [] enemy2_Y = new double[10];					// coordenadas y
		double [] enemy2_V = new double[10];					// velocidades
		double [] enemy2_angle = new double[10];				// Angulos (indicam direcao do movimento)
		double [] enemy2_RV = new double[10];					// velocidades de rotacao
		double [] enemy2_explosion_start = new double[10];		// instantes dos inicios das explosÃµes
		double [] enemy2_explosion_end = new double[10];		// instantes dos finais das explosÃµes
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do prÃ³ximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;									// contagem de inimigos tipo 2 (usada na "formaÃ§Ã£o de voo")
		double enemy2_radius = 12.0;							// raio (tamanho aproximado do inimigo 2)
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer

		/* variaveis dos inimigos tipo 3 */
		

		
		// int [] enemy3_states = new int[10];						// estados
		// double [] enemy3_X = new double[10];					// coordenadas x
		// double [] enemy3_Y = new double[10];					// coordenadas y
		// double [] enemy3_V = new double[10];					// velocidades
		// double [] enemy3_angle = new double[10];				// Angulos (indicam direcao do movimento)
		// double [] enemy3_RV = new double[10];					// velocidades de rotacao
		// double [] enemy3_explosion_start = new double[10];		// instantes dos inicios das explosÃµes
		// double [] enemy3_explosion_end = new double[10];		// instantes dos finais das explosÃµes
		// long [] enemy3_nextShoot = new long[10];				// instantes do prÃ³ximo tiro
		// long nextEnemy3 = currentTime + 1000;					// instante em que um novo inimigo 3 deve aparecer


		/* variaveis do powerup */
		
		int [] powerup_states = new int[10];					// estados
		double [] powerup_X = new double[10];					// coordenadas x
		double [] powerup_Y = new double[10];					// coordenadas y
		double [] powerup_V = new double[10];					// velocidades
		double [] powerup_angle = new double[10];				// Angulos (indicam direcao do movimento)
		double [] powerup_RV = new double[10];					// velocidades de rotacao
		long nextpowerup = currentTime + 20000;					// instante em que um novo inimigo 1 deve aparecer

		/* variaveis dos projeteis lancados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		
		int [] e_projectile_states = new int[200];				// estados
		double [] e_projectile_X = new double[200];				// coordenadas x
		double [] e_projectile_Y = new double[200];				// coordenadas y
		double [] e_projectile_VX = new double[200];			// velocidade no eixo x
		double [] e_projectile_VY = new double[200];			// velocidade no eixo y
		double e_projectile_radius = 2.0;						// raio (tamanho dos projeteis inimigos)
		
		/* estrelas que formam o fundo de primeiro plano */
		
		double [] background1_X = new double[20];
		double [] background1_Y = new double[20];
		double background1_speed = 0.070;
		double background1_count = 0.0;
		
		/* estrelas que formam o fundo de segundo plano */
		
		double [] background2_X = new double[50];
		double [] background2_Y = new double[50];
		double background2_speed = 0.045;
		double background2_count = 0.0;
		
		/* inicializacoes */
		for(int i = 0; i < PROJECTILE_NUMBER; i++) projectile.get(i).setState(INACTIVE);
		for(int i = 0; i < E_PROJECTILE_NUMBER; i++) e_projectile.get(i).setState(INACTIVE);
		for (int i = 0; i < ENEMY_NUMBER; i++) {
			enemy1[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 9, currentTime + 2000*i);
		}
		for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE;
		for (int i = 0; i < ENEMY_NUMBER; i++) {
			enemy3[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 10, currentTime + 1000*i);
		}
		for (int i = 0; i < ENEMY_NUMBER; i++) {
			powerup[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 5, currentTime + 2000*i);
		}


		
		for(int i = 0; i < background1_X.length; i++){
			
			background1_X[i] = Math.random() * GameLib.WIDTH;
			background1_Y[i] = Math.random() * GameLib.HEIGHT;
		}
		
		for(int i = 0; i < background2_X.length; i++){
			
			background2_X[i] = Math.random() * GameLib.WIDTH;
			background2_Y[i] = Math.random() * GameLib.HEIGHT;
		}
						
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
		/* 1) Verifica se hÃ¡ colisoes e atualiza estados dos elementos conforme a necessidade.           */
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
			/* verificacao de colisoes */
			/***************************/

			if(player.getState() == ACTIVE){
				
				
				/* colisoes player - projeteis (inimigo) */
				
				for(int i = 0; i < E_PROJECTILE_NUMBER; i++){
					
					double dx = e_projectile.get(i).getX() - player.getX();
					double dy = e_projectile.get(i).getY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					
					if(dist < (player.getRadius() + e_projectile.get(i).getRadius()) * 0.8){
						//System.out.println(dist);
						
						
						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}
			
				/* colisoes player - inimigos */
							
				for(int i = 0; i < ENEMY_NUMBER; i++){
					
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

				for(int i = 0; i < ENEMY_NUMBER; i++){
					
					double dx = enemy3[i].getX() - player.getX();
					double dy = enemy3[i].getY() - player.getY();
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
							enemy1[i].setState(EXPLODING);
							enemy1[i].setExplosion_start(currentTime);
							enemy1[i].setExplosion_end(currentTime + 500);
							// enemy2[i].setState(EXPLODING);
							// enemy2[i].setExplosion_start(currentTime);
							// enemy2[i].setExplosion_end(currentTime + 500);
							enemy2_states[i] = EXPLODING;
							enemy2_explosion_start[i] = currentTime;
							enemy2_explosion_end[i] = currentTime + 500;
							enemy3[i].setState(EXPLODING);
							enemy3[i].setExplosion_start(currentTime);
							enemy3[i].setExplosion_end(currentTime + 500);
						}
					}
				}
				
			}
			
			/* colisoes projeteis (player) - inimigos */
			
			for(int k = 0; k < PROJECTILE_NUMBER; k++){
				
				for(int i = 0; i < ENEMY_NUMBER; i++){
										
					if(enemy1[i].getState() == ACTIVE){
					
						double dx = enemy1[i].getX() - projectile.get(k).getX();
						double dy = enemy1[i].getY() - projectile.get(k).getY();
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy1_radius){
							
							
							enemy1[i].setState(EXPLODING);//ok
							enemy1[i].setExplosion_start(currentTime);
							enemy1[i].setExplosion_end(currentTime + 500);
						}
					}
				}
				
				for(int i = 0; i < enemy2_states.length; i++){
					
					if(enemy2_states[i] == ACTIVE){
						double dx = enemy2_X[i] - projectile.get(k).getX();
						double dy = enemy2_Y[i] - projectile.get(k).getY();
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy2_radius){
							
							enemy2_states[i] = EXPLODING;
							enemy2_explosion_start[i] = currentTime;
							enemy2_explosion_end[i] = currentTime + 500;
						}
					}
				}

				for(int i = 0; i < ENEMY_NUMBER; i++){
					
					if(enemy3[i].getState() == ACTIVE){

						double dx = enemy3[i].getX() - projectile.get(k).getX();
						double dy = enemy3[i].getY() - projectile.get(k).getY();
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy3_radius){
							
							
							enemy3[i].setState(EXPLODING);//ok
							enemy3[i].setExplosion_start(currentTime);
							enemy3[i].setExplosion_end(currentTime + 500);
						}
					}
				}


			}
				
			/***************************/
			/* Atualizacoes de estados */
			/***************************/
			
			/* projeteis (player) */
			
			for(int i = 0; i < PROJECTILE_NUMBER; i++){
				
				if(projectile.get(i).getState() == ACTIVE){
					
					/* verificando se projÃ©til saiu da tela */
					if(projectile.get(i).getY() < 0) {
						
						projectile.get(i).setState(INACTIVE);
						}
					else {
					
						projectile.get(i).setX(projectile.get(i).getX() + projectile.get(i).getVX()* delta);
						projectile.get(i).setY(projectile.get(i).getY() + projectile.get(i).getVY()* delta);

					}
				}
			}
			
			/* projeteis (inimigos) */
			
			for(int i = 0; i < E_PROJECTILE_NUMBER; i++){
				
				if(e_projectile.get(i).getState() == ACTIVE){
					
					/* verificando se projÃ©til saiu da tela */
					if(e_projectile.get(i).getY() > GameLib.HEIGHT) {
						
						e_projectile.get(i).setState(INACTIVE);
					}
					else {
					
						e_projectile.get(i).setX(e_projectile.get(i).getX() + e_projectile.get(i).getVX()* delta);
						e_projectile.get(i).setY(e_projectile.get(i).getY() + e_projectile.get(i).getVY()* delta);

					}
				}
			}
			
			/* inimigos tipo 1 */
			
			for(int i = 0; i < ENEMY_NUMBER; i++){
				
				if(enemy1[i].getState() == EXPLODING){
					
					if(currentTime > enemy1[i].getExplosion_end()){
						
						//inimigo continua inativo, mas retorna pra posi��o inicial, que � aleatoria em x
						enemy1[i].setState(INACTIVE);//ok
						enemy1[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						enemy1[i].setY(-10);
					}
				}
				
				if(enemy1[i].getState() == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemy1[i].getY() > GameLib.HEIGHT + 10) {
						
						//inimigo continua inativo, mas retorna pra posi��o inicial, que � aleatoria em x
						enemy1[i].setState(INACTIVE);//OK
						enemy1[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						enemy1[i].setY(-10);
					}
					else {
					
						enemy1[i].setX(enemy1[i].getX() + enemy1[i].getV() * Math.cos(enemy1[i].getAngle()) * delta);//ok
						enemy1[i].setY(enemy1[i].getY() + enemy1[i].getV() * Math.sin(enemy1[i].getAngle()) * delta * (-1.0));//ok
						enemy1[i].setAngle(enemy1[i].getAngle()+ enemy1[i].getRV() * delta); //ok
						
						if(currentTime > enemy1[i].getNextShot() && enemy1[i].getY() < player.getY()){
																							
							int free = findFreeIndexEProjectile(e_projectile);
							
							if(free < E_PROJECTILE_NUMBER){
								
								e_projectile.get(free).setX(enemy1[i].getX());
								e_projectile.get(free).setY(enemy1[i].getY());
								e_projectile.get(free).setVX(Math.cos(enemy1[i].getAngle()) * 0.45);
								e_projectile.get(free).setVY(Math.sin(enemy1[i].getAngle()) * 0.45 * (-1.0));
								e_projectile.get(free).setState(ACTIVE);
								
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
							int [] freeArray = findFreeIndexEProjectile(e_projectile, angles.length);

							for(int k = 0; k < freeArray.length; k++){
								
								int free = freeArray[k];
								
								if(free < E_PROJECTILE_NUMBER){
									
									double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;
									double vx = Math.cos(a);
									double vy = Math.sin(a);
										
									e_projectile.get(free).setX(enemy2_X[i]);
									e_projectile.get(free).setY(enemy2_Y[i]);
									e_projectile.get(free).setVX(vx * 0.30);
									e_projectile.get(free).setVY(vy * 0.30);
									e_projectile.get(free).setState(ACTIVE);
								}
							}
						}
					}
				}
			}

			/* inimigos tipo 3 */
			
			for(int i = 0; i < ENEMY_NUMBER; i++){
				
				if(enemy3[i].getState() == EXPLODING){
					
					if(currentTime > enemy3[i].getExplosion_end()){
						
						//inimigo continua inativo, mas retorna pra posi��o inicial, que � aleatoria em x
						enemy3[i].setState(INACTIVE);//ok
						enemy3[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						enemy3[i].setY(-10);
					}
				}
				
				if(enemy3[i].getState() == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemy3[i].getY() > GameLib.HEIGHT + 10) {
						
						//inimigo continua inativo, mas retorna pra posi��o inicial, que � aleatoria em x
						enemy3[i].setState(INACTIVE);//OK
						enemy3[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						enemy3[i].setY(-10);
					}
					else {
					
						enemy3[i].setX((enemy3[i].getX() + enemy3[i].getV() -1.0) * delta);//ok
						enemy3[i].setY(enemy3[i].getY() + enemy3[i].getV() * Math.sin(enemy3[i].getAngle()) * delta * (-0.7));//ok
						enemy3[i].setAngle(enemy3[i].getAngle()+ enemy3[i].getRV() * delta); //ok
						
						if(currentTime > enemy3[i].getNextShot() && enemy3[i].getY() < player.getY()){
																							
							int free = findFreeIndexEProjectile(e_projectile);
							
							if(free < E_PROJECTILE_NUMBER){
								
								e_projectile.get(free).setX(enemy3[i].getX());
								e_projectile.get(free).setY(enemy3[i].getY());
								e_projectile.get(free).setVX(Math.cos(enemy3[i].getAngle()) * 0.45);
								e_projectile.get(free).setVY(Math.sin(enemy3[i].getAngle()) * 0.45 * (-1.0));
								e_projectile.get(free).setState(ACTIVE);
								
								enemy3[i].setNextShot((long) (currentTime + 200));//ok
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
						
						powerup[i].setState(INACTIVE);
						powerup[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						powerup[i].setY(-10);

					}
					else {
					
						powerup[i].setX(powerup[i].getX() + powerup[i].getV() * Math.cos(powerup[i].getAngle()) * delta);//ok
						powerup[i].setY(powerup[i].getY() + powerup[i].getV() * Math.sin(powerup[i].getAngle()) * delta * (-1.0));//ok
						powerup[i].setAngle(powerup[i].getAngle()+ powerup[i].getRV() * delta); //ok
						
					}
				}
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lancados" */
			
			conta += delta;
			
			if(conta >= 1000) { //aqui define a velocidade de spawn
				conta = 0;
				id++;
				if(id>9) id=0;
				if(enemy1[id].getState() != EXPLODING) {
					enemy1[id].setState(ACTIVE);
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
			
			conta += delta;
			
			if(conta >= 1500) { //aqui define a velocidade de spawn
				conta = 0;
				id++;
				if(id>9) id=0;
				if(enemy3[id].getState() != EXPLODING) {
					enemy3[id].setState(ACTIVE);
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
					nextpowerup = currentTime + 30000;
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
				
				if(GameLib.iskeyPressed(GameLib.KEY_UP)) player.setY(player.getY() - (delta * player.getVY())); 
				if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player.setY(player.getY() + (delta * player.getVX())); 
				if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player.setX(player.getX() - (delta * player.getVX())); 
				if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player.setX(player.getX() + (delta * player.getVY())); 
				
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(currentTime > player.getNextShot()){
						
						int free = findFreeIndexProjectile(projectile);
												
						if(free < PROJECTILE_NUMBER){
							
							projectile.get(free).setX(player.getX());
							projectile.get(free).setY(player.getY() - 2 * player.getRadius());
							projectile.get(free).setVX(0.0);
							projectile.get(free).setVY(-1.0);
							projectile.get(free).setState(ACTIVE);
							player.setNextShot(currentTime + 100);
						}
					}	
				}
			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estÃ£o dentro */
			/* da tela de jogo apÃ³s processar entrada do usuÃ¡rio.      */
			

			if(player.getX() < 0.0) player.setX(0.0);
			if(player.getX() >= GameLib.WIDTH) player.setX(GameLib.WIDTH - 1);
			if(player.getY() < 25.0) player.setY(25.0);
			if(player.getY() >= GameLib.HEIGHT) player.setY(GameLib.HEIGHT -1);

			player.verificaPosicao(); //fun��es que checam a posi��o para o player n�oo sair da tela foram para a classe player


			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo distante */
			
			GameLib.setColor(Color.DARK_GRAY);
			background2_count += background2_speed * delta;
			
			for(int i = 0; i < background2_X.length; i++){
				
				GameLib.fillRect(background2_X[i], (background2_Y[i] + background2_count) % GameLib.HEIGHT, 2, 2);
			}
			
			/* desenhando plano de fundo prÃ³ximo */
			
			GameLib.setColor(Color.GRAY);
			background1_count += background1_speed * delta;
			
			for(int i = 0; i < background1_X.length; i++){
				
				GameLib.fillRect(background1_X[i], (background1_Y[i] + background1_count) % GameLib.HEIGHT, 3, 3);
			}
						
			/* desenhando player */
			
			if(player.getState() == EXPLODING){
				
				double alpha = (currentTime - player.getExpStart()) / (player.getExpEnd() - player.getExpStart());
				GameLib.drawExplosion(player.getX(), player.getY(), alpha);
			}
			else{
				
				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(player.getX(), player.getY(), player.getRadius());
			}
				
			/* deenhando projeteis (player) */
			
			for(int i = 0; i < PROJECTILE_NUMBER; i++){
				
				if(projectile.get(i).getState() == ACTIVE){
					
					GameLib.setColor(Color.GREEN);
					GameLib.drawLine(projectile.get(i).getX(), projectile.get(i).getY() - 5, projectile.get(i).getX(), projectile.get(i).getY() + 5);
					GameLib.drawLine(projectile.get(i).getX() - 1, projectile.get(i).getY() - 3, projectile.get(i).getX() - 1, projectile.get(i).getY() + 3);
					GameLib.drawLine(projectile.get(i).getX() + 1, projectile.get(i).getY() - 3, projectile.get(i).getX() + 1, projectile.get(i).getY() + 3);
				}
			}
			
			/* desenhando projeteis (inimigos) */
		
			for(int i = 0; i < E_PROJECTILE_NUMBER; i++){
				
				if(e_projectile.get(i).getState() == ACTIVE){
	
					GameLib.setColor(Color.RED);
					GameLib.drawCircle(e_projectile.get(i).getX(), e_projectile.get(i).getY(), e_projectile.get(i).getRadius());
				}
			}
			
			/* desenhando inimigos (tipo 1) */
		
			for(int i = 0; i < ENEMY_NUMBER; i++){
				
				if(enemy1[i].getState() == EXPLODING){
					
					double alpha = (currentTime - enemy1[i].getExplosion_start()) / (enemy1[i].getExplosion_end() - enemy1[i].getExplosion_start());
					GameLib.drawExplosion(enemy1[i].getX(), enemy1[i].getY(), alpha);
					//ACHO QUE AQUI TEMOS QUE SETAR PRA INACTIVE O STATE, POIS NOS CHECAMOS NO SPAWN SE STATE != EXPLODING, ou mudar la
				}
				
				if(enemy1[i].getState() == ACTIVE){
			
					GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemy1[i].getX(), enemy1[i].getY(), enemy1_radius);
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
			
			for(int i = 0; i < ENEMY_NUMBER; i++){
				
				if(enemy3[i].getState() == EXPLODING){
					
					double alpha = (currentTime - enemy3[i].getExplosion_start()) / (enemy3[i].getExplosion_end() - enemy3[i].getExplosion_start());
					GameLib.drawExplosion(enemy3[i].getX(), enemy3[i].getY(), alpha);
				}
				
				if(enemy3[i].getState() == ACTIVE){
			
					GameLib.setColor(Color.orange);
					GameLib.drawTriangle(enemy3[i].getX(), enemy3[i].getY(), enemy3_radius);
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

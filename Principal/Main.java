package Principal;
import java.awt.Color;
import java.util.ArrayList;

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
	public static final int PROJECTILE_NUMBER = 10;
	public static final int E_PROJECTILE_NUMBER = 200;
	
	
	
	
	
	
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
		ArrayList<Projectile> e_projectile = new ArrayList<Projectile>(E_PROJECTILE_NUMBER);
        ArrayList<Projectile> projectile = new ArrayList<Projectile>(PROJECTILE_NUMBER);
        for(int i = 0; i< E_PROJECTILE_NUMBER;i++) e_projectile.add(new Projectile(0,0.0,0.0,0.0,0.0,2));
        for(int i = 0; i< PROJECTILE_NUMBER;i++) projectile.add(new Projectile(0,0.0,0.0,0.0,0.0));
        
        
		int [] enemy1_states = new int[10];					// estados
		double [] enemy1_X = new double[10];					// coordenadas x
		double [] enemy1_Y = new double[10];					// coordenadas y
		double [] enemy1_V = new double[10];					// velocidades
		double [] enemy1_angle = new double[10];				// Ã¢ngulos (indicam direÃ§Ã£o do movimento)
		double [] enemy1_RV = new double[10];					// velocidades de rotaÃ§Ã£o
		double [] enemy1_explosion_start = new double[10];			// instantes dos inÃ­cios das explosÃµes
		double [] enemy1_explosion_end = new double[10];			// instantes dos finais da explosÃµes
		long [] enemy1_nextShoot = new long[10];				// instantes do prÃ³ximo tiro
		double enemy1_radius = 9.0;						// raio (tamanho do inimigo 1)
		long nextEnemy1 = currentTime + 2000;					// instante em que um novo inimigo 1 deve aparecer
		
		/* variÃ¡veis dos inimigos tipo 2 */
		
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
		
		/* inicializaÃ§Ãµes */
		for(int i = 0; i < PROJECTILE_NUMBER; i++) projectile.get(i).setState(INACTIVE);
		for(int i = 0; i < E_PROJECTILE_NUMBER; i++) e_projectile.get(i).setState(INACTIVE);
		for(int i = 0; i < enemy1_states.length; i++) enemy1_states[i] = INACTIVE;
		for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE;
		
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
				
				for(int i = 0; i < E_PROJECTILE_NUMBER; i++){
					
					double dx = e_projectile.get(i).getX() - player.getX();
					double dy = e_projectile.get(i).getY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					
					if(dist < (player.getRadius() + e_projectile.get(i).getRadius()) * 0.8){
						
						
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
			}
			
			/* colisÃµes projeteis (player) - inimigos */
			
			for(int k = 0; k < PROJECTILE_NUMBER; k++){
				
				for(int i = 0; i < enemy1_states.length; i++){
										
					if(enemy1_states[i] == ACTIVE){
					
						double dx = enemy1_X[i] - projectile.get(k).getX();
						double dy = enemy1_Y[i] - projectile.get(k).getY();
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
			}
				
			/***************************/
			/* AtualizaÃ§Ãµes de estados */
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
																							
							int free = findFreeIndexEProjectile(e_projectile);
							
							if(free < E_PROJECTILE_NUMBER){
								
								e_projectile.get(free).setX(enemy1_X[i]);
								e_projectile.get(free).setY(enemy1_Y[i]);
								e_projectile.get(free).setVX(Math.cos(enemy1_angle[i]) * 0.45);
								e_projectile.get(free).setVY(Math.sin(enemy1_angle[i]) * 0.45 * (-1.0));
								e_projectile.get(free).setState(ACTIVE);
								
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
			
			/* verificando se novos inimigos (tipo 1) devem ser "lanÃ§ados" */
			
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
			
			/* chamada a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execuÃ§Ã£o do laÃ§o do main loop demore aproximadamente 3 ms. */
			
			busyWait(currentTime + 3);
		}
		
		System.exit(0);
	}
}

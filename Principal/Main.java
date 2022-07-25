package Principal;
import java.awt.Color;
import java.util.ArrayList;

/***********************************************************************/
/*                                                                     */
/* Para jogar:                                                         */
/*                                                                     */
/*    - cima, baixo, esquerda, direita: movimento do player.        */
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
	public static long conta3 = 0;
	public static long contanuke = 0;
	public static long contablaster = 0;
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
public static int findFreeIndexEnemy2(ArrayList<Enemy> stateArray){
	
	int i;		
	for(i = 0; i < ENEMY_NUMBER; i++){
		
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
	
	/* Metodo principal */
	
	public static void main(String [] args){

		/* Indica que o jogo esta em execuÃ§Ã£o */

		boolean running = true;

		/* variaveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();
		
		//INSTANCIA O JOGADOR (ANTES ERA aqui embaixo QUE TA COMENTADAA AGORA)
		
		Player player = new Player(ACTIVE, GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, 0, 0, currentTime);

		/* variaveis dos inimigos tipo 1 */
		ArrayList<Projectile> e_projectile = new ArrayList<Projectile>(E_PROJECTILE_NUMBER);
        ArrayList<Projectile> projectile = new ArrayList<Projectile>(PROJECTILE_NUMBER);
        for(int i = 0; i< E_PROJECTILE_NUMBER;i++) {
        	e_projectile.add(new Projectile(0,0.0,0.0,0.0,0.0));
        	e_projectile.get(i).setRadius(2);
        }
        for(int i = 0; i< PROJECTILE_NUMBER;i++) projectile.add(new Projectile(0,0.0,0.0,0.0,0.0));
        
        Enemy[] enemy1 = new Enemy[ENEMY_NUMBER];
        Enemy[] enemy3 = new Enemy[ENEMY_NUMBER];
        Enemy[] nuke = new Enemy[ENEMY_NUMBER];
        Enemy[] megablaster = new Enemy[ENEMY_NUMBER];


		//Enemy[] enemy2 = new Enemy[ENEMY_NUMBER];
        ArrayList<Enemy> enemy22 = new ArrayList<Enemy>(ENEMY_NUMBER);
        for(int i = 0; i < ENEMY_NUMBER;i++) enemy22.add(new Enemy(INACTIVE,0.0,0.0,0.0,0.0,0.0,0L,12.0,currentTime + 7000,GameLib.WIDTH * 0.20,0));
        
		
		/* variaveis dos inimigos tipo 2 */
		
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
		
		/* Estrelas que formam o fundo de primeiro plano */
		Background primaryBackground = new Background(0.070, 0.0, 20);
		
		/* Estrelas que formam o fundo de segundo plano */
		Background secondaryBackground = new Background(0.045, 0.0, 20);

		
		/* inicializacoes */
		for(int i = 0; i < PROJECTILE_NUMBER; i++) projectile.get(i).setState(INACTIVE);
		for(int i = 0; i < E_PROJECTILE_NUMBER; i++) e_projectile.get(i).setState(INACTIVE);
		for (int i = 0; i < enemy1.length; i++) {
			enemy1[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 9, currentTime + 2000*i);
		}
		for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE;
		for (int i = 0; i < enemy3.length; i++)
		{
			enemy3[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 10, currentTime + 1000*i);
		}
		for (int i = 0; i < nuke.length; i++) {
			nuke[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 5, currentTime + 15000*i);
		}
		for (int i = 0; i < megablaster.length; i++) {
			megablaster[i] = new Enemy(INACTIVE, (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2,
					0, currentTime + 500, 5, currentTime + 1000*i);
		}

		// Inicializando plano de fundo primário (próximo)
		primaryBackground.startBackground();
		
		// Inicializando plano de fundo secundário (distante)
		secondaryBackground.startBackground();
						
		/* iniciado interface grÃ¡fica */
		
		GameLib.initGraphics();
		//GameLib.initGraphics_SAFE_MODE();  // chame esta versÃ£o do Metodo caso nada seja desenhado na janela do jogo.
		
		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/* -----------------                                                                             */
		/*                                                                                               */
		/* O main loop do jogo executa as seguintes operaÃ§Ãµes:                                           */
		/*                                                                                               */
		/* 1) Verifica se ha colisoes e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu entre a Ãºltima atualizaÃ§Ã£o     */
		/*    e o timestamp atual: posiÃ§Ã£o e orientaÃ§Ã£o, execuÃ§Ã£o de disparos de projeteis, etc.         */
		/*                                                                                               */
		/* 3) Processa entrada do usuario (teclado) e atualiza estados do player conforme a necessidade. */
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
							
				for(int i = 0; i < enemy1.length; i++){
					
					double dx = enemy1[i].getX() - player.getX();//ok
					double dy = enemy1[i].getY() - player.getY();//ok
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy1[i].getRadius()) * 0.8){
						
						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}
				
				for(int i = 0; i < ENEMY_NUMBER; i++){
					
					double dx = enemy22.get(i).getX() - player.getX();
					double dy = enemy22.get(i).getY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy22.get(0).getRadius()) * 0.8){
						
						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}

				for(int i = 0; i < enemy3.length; i++){
					
					double dx = enemy3[i].getX() - player.getX();
					double dy = enemy3[i].getY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy3[i].getRadius()) * 0.8){
						
						player.setState(EXPLODING);
						player.setExplosionStart(currentTime);
						player.setExplosionEnd(currentTime + 2000);
					}
				}



				/* colisoes player - Powerups */

				for(int i = 0; i < nuke.length; i++){
					
					double dx = nuke[i].getX() - player.getX();
					double dy = nuke[i].getY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + nuke[i].getRadius()) * 0.8){
						player.setState(ACTIVE);
						nuke[i].setState(INACTIVE);
						for (int q = 0; q < 10; q = q + 1){
							enemy1[q].setState(EXPLODING);
							enemy1[q].setExplosion_start(currentTime);
							enemy1[q].setExplosion_end(currentTime + 500);
							enemy2_states[i] = EXPLODING;
							enemy2_explosion_start[i] = currentTime;
							enemy2_explosion_end[i] = currentTime + 500;
							enemy3[q].setState(EXPLODING);
							enemy3[q].setExplosion_start(currentTime);
							enemy3[q].setExplosion_end(currentTime + 500);
						}
					}
				}

				for(int i = 0; i < megablaster.length; i++){
					
					double dx = megablaster[i].getX() - player.getX();
					double dy = megablaster[i].getY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + megablaster[i].getRadius()) * 0.8){
						player.setState(ACTIVE);
						megablaster[i].setState(INACTIVE);
						for (int q = 0; q < 10; q = q + 1){
						}
					}
				}

				
			}
			
			/* colisoes projeteis (player) - inimigos */
			
			for(int k = 0; k < PROJECTILE_NUMBER; k++){
				
				for(int i = 0; i < enemy1.length; i++){
										
					if(enemy1[i].getState() == ACTIVE){
					
						double dx = enemy1[i].getX() - projectile.get(k).getX();
						double dy = enemy1[i].getY() - projectile.get(k).getY();
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy1[i].getRadius()){
							
							
							enemy1[i].setState(EXPLODING);//ok
							enemy1[i].setExplosion_start(currentTime);
							enemy1[i].setExplosion_end(currentTime + 500);
						}
					}
				}
				
				for(int i = 0; i < ENEMY_NUMBER; i++){
					
					if(enemy22.get(i).getState() == ACTIVE){
						double dx = enemy22.get(i).getX() - projectile.get(k).getX(); //0
						double dy = enemy22.get(i).getY() - projectile.get(k).getY();//-10
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						
						if(dist < enemy22.get(0).getRadius()){
							enemy22.get(i).setState(EXPLODING);
							enemy22.get(i).setExplosion_start(currentTime);
							enemy22.get(i).setExplosion_end(currentTime + 500);
						}
					}
				}

				for(int i = 0; i < enemy3.length; i++){
					
					if(enemy3[i].getState() == ACTIVE){

						double dx = enemy3[i].getX() - projectile.get(k).getX();
						double dy = enemy3[i].getY() - projectile.get(k).getY();
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy3[i].getRadius()){
							
							
							enemy3[i].setState(EXPLODING);//ok
							enemy3[i].setExplosion_start(currentTime);
							enemy3[i].setExplosion_end(currentTime + 500);
						}
					}
				}

				for(int i = 0; i < enemy3.length; i++){
					
					if(enemy3[i].getState() == ACTIVE){

						double dx = enemy3[i].getX() - projectile.get(k).getX();
						double dy = enemy3[i].getY() - projectile.get(k).getY();
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy3[i].getRadius()){
							
							
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
			
			for(int i = 0; i < enemy1.length; i++){
				
				if(enemy1[i].getState() == EXPLODING){
					
					if(currentTime > enemy1[i].getExplosion_end()){
						
						//inimigo continua inativo, mas retorna pra posicao inicial, que � aleatoria em x
						enemy1[i].setState(INACTIVE);//ok
						enemy1[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						enemy1[i].setY(-10);
					}
				}
				
				if(enemy1[i].getState() == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemy1[i].getY() > GameLib.HEIGHT + 10) {
						
						//inimigo continua inativo, mas retorna pra posicao inicial, que � aleatoria em x
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
			
			for(int i = 0; i < ENEMY_NUMBER; i++){
				
				if(enemy22.get(i).getState() == EXPLODING){
					if(currentTime > enemy22.get(i).getExplosion_end()){
						enemy22.get(i).setState(INACTIVE);					}
				}
				
				if(enemy22.get(i).getState() == ACTIVE){
					/* verificando se inimigo saiu da tela */
					if(	enemy22.get(i).getX() < -10 || enemy22.get(i).getX() > GameLib.WIDTH + 10 ) {
						
						enemy22.get(i).setState(INACTIVE);

					}
					else {
						
						boolean shootNow = false;
						double previousY = enemy22.get(i).getY();	
						
						enemy22.get(i).setX(enemy22.get(i).getX() + enemy22.get(i).getV()* Math.cos(enemy22.get(i).getAngle())* delta); //0
						
						enemy22.get(i).setY(enemy22.get(i).getY() + (enemy22.get(i).getV()* Math.sin(enemy22.get(i).getAngle())* delta*(-1.0)));
						enemy22.get(i).setAngle(enemy22.get(i).getAngle() + enemy22.get(i).getRV() * delta);
						
						double threshold = GameLib.HEIGHT * 0.30;
						
						
						if(previousY < threshold && enemy22.get(i).getY() >= threshold) {
							
							if(enemy22.get(i).getX()< GameLib.WIDTH / 2) enemy22.get(i).setRV(0.003);
							else enemy22.get(i).setRV(-0.003);
						}
						
						if(enemy22.get(i).getRV() > 0 && Math.abs(enemy22.get(i).getAngle() - 3 * Math.PI) < 0.05){
							enemy22.get(i).setRV(0.0);
							enemy22.get(i).setAngle(3 * Math.PI);
							shootNow = true;
						}
						
						if(enemy22.get(i).getRV() < 0 && Math.abs(enemy22.get(i).getAngle()) < 0.05){
							
							
							enemy22.get(i).setRV(0.0);
							enemy22.get(i).setAngle(0.0);
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
										
									e_projectile.get(free).setX(enemy22.get(i).getX());
									e_projectile.get(free).setY(enemy22.get(i).getY());
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
			
			for(int i = 0; i < enemy3.length; i++){
				
				if(enemy3[i].getState() == EXPLODING){
					
					if(currentTime > enemy3[i].getExplosion_end()){
						
						//inimigo continua inativo, mas retorna pra posicao inicial, que � aleatoria em x
						enemy3[i].setState(INACTIVE);//ok
						enemy3[i].setX((GameLib.WIDTH - 20.0) + 10.0);
						enemy3[i].setY(-5.0);
					}
				}
				
				if(enemy3[i].getState() == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemy3[i].getY() > GameLib.HEIGHT + 10) {
						
						//inimigo continua inativo, mas retorna pra posicao inicial, que � aleatoria em x
						enemy3[i].setState(INACTIVE);//OK
						enemy3[i].setX((GameLib.WIDTH - 20.0) + 10.0);
						enemy3[i].setY(-5.0);
					}
					else {
					
						enemy3[i].setX(enemy3[i].getX() + enemy3[i].getV() -1.2);//ok
						enemy3[i].setY(enemy3[i].getY() + enemy3[i].getV() * Math.sin(enemy3[i].getAngle()) * delta * (-0.5));//ok
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

			//nuke

			for(int i = 0; i < nuke.length; i++){
				
				if(nuke[i].getState() == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(nuke[i].getY() > GameLib.HEIGHT + 10) {
						
						nuke[i].setState(INACTIVE);
						nuke[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						nuke[i].setY(-10);

					}
					else {
					
						nuke[i].setX(nuke[i].getX() + nuke[i].getV() * Math.cos(nuke[i].getAngle()) * delta);//ok
						nuke[i].setY(nuke[i].getY() + nuke[i].getV() * Math.sin(nuke[i].getAngle()) * delta * (-1.0));//ok
						nuke[i].setAngle(nuke[i].getAngle()+ nuke[i].getRV() * delta); //ok
						
					}
				}
			}

			//megablaster

			for(int i = 0; i < megablaster.length; i++){
				
				if(megablaster[i].getState() == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(megablaster[i].getY() > GameLib.HEIGHT + 10) {
						
						megablaster[i].setState(INACTIVE);
						megablaster[i].setX(Math.random() * (GameLib.WIDTH - 20.0) + 10.0);
						megablaster[i].setY(-10);

					}
					else {
					
						megablaster[i].setX(megablaster[i].getX() + megablaster[i].getV() * Math.cos(megablaster[i].getAngle()) * delta);//ok
						megablaster[i].setY(megablaster[i].getY() + megablaster[i].getV() * Math.sin(megablaster[i].getAngle()) * delta * (-1.0));//ok
						megablaster[i].setAngle(megablaster[i].getAngle()+ megablaster[i].getRV() * delta); //ok
						
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
				
				int free = findFreeIndexEnemy2(enemy22);
								
				if(free < ENEMY_NUMBER){
					enemy22.get(free).setX(480*0.8);
					
					enemy22.get(free).setY(-10.0);
					enemy22.get(free).setV(0.42);
					enemy22.get(free).setAngle((3 * Math.PI) / 2);
					enemy22.get(free).setRV(0.0);
					enemy22.get(free).setState(ACTIVE);

					enemy22.get(0).setCount(enemy22.get(0).getCount() + 1);
					
					if(enemy22.get(0).getCount() < 10){
						
						enemy22.get(0).setNextEnemy(currentTime + 120);
					}
					else {
						
						enemy22.get(0).setCount(0);
						enemy22.get(0).setenemy2SpawnX(480*0.2);
						enemy22.get(0).setNextEnemy((long) (currentTime + 3000 + Math.random() * 3000));
					}
				}
			}

			/* verificando se novos inimigos (tipo 3) devem ser "lancados" */
			
			conta3 += delta;
			
			if(conta3 >= 1500) { //aqui define a velocidade de spawn
				conta3 = 0;
				id++;
				if(id>9) id=0;
				if(enemy3[id].getState() != EXPLODING) {
					enemy3[id].setState(ACTIVE);
				}
			}


			/* verificando se novas nukes devem ser "lancados" */
			
			contanuke += delta;
			
			if(contanuke >= 15000) { //aqui define a velocidade de spawn
				contanuke = 0;
				id++;
				if(id>9) id=0;
				if(nuke[id].getState() != EXPLODING) {
					nuke[id].setState(ACTIVE);
				}
			}

			/* verificando se novas  devem ser "lancados" */
			
			contablaster += delta;
			
			if(contablaster >= 15000) { //aqui define a velocidade de spawn
				contablaster = 0;
				id++;
				if(id>9) id=0;
				if(megablaster[id].getState() != EXPLODING) {
					megablaster[id].setState(ACTIVE);
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
			/* Verificando entrada do usuario (teclado) */
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
			/* da tela de jogo apÃ³s processar entrada do usuario.      */
			

			if(player.getX() < 0.0) player.setX(0.0);
			if(player.getX() >= GameLib.WIDTH) player.setX(GameLib.WIDTH - 1);
			if(player.getY() < 25.0) player.setY(25.0);
			if(player.getY() >= GameLib.HEIGHT) player.setY(GameLib.HEIGHT -1);

			player.verificaPosicao(); //fun��es que checam a posicao para o player n�oo sair da tela foram para a classe player


			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo secundário (distante) */
			secondaryBackground.drawBackgroundSecondary(delta);
			
			/* desenhando plano de fundo primário (próximo) */
			primaryBackground.drawBackgroundPrimary(delta);
				
			/* desenhando player */
			
			if(player.getState() == EXPLODING){
				
				double alpha = (currentTime - player.getExpStart()) / (player.getExpEnd() - player.getExpStart());
				GameLib.drawExplosion(player.getX(), player.getY(), alpha);
			}
			else{
				
				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(player.getX(), player.getY(), player.getRadius());
			}
				
			/* desenhando projeteis (player) */
			
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
		
			for(int i = 0; i < enemy1.length; i++){
				
				if(enemy1[i].getState() == EXPLODING){
					
					double alpha = (currentTime - enemy1[i].getExplosion_start()) / (enemy1[i].getExplosion_end() - enemy1[i].getExplosion_start());
					GameLib.drawExplosion(enemy1[i].getX(), enemy1[i].getY(), alpha);
					//ACHO QUE AQUI TEMOS QUE SETAR PRA INACTIVE O STATE, POIS NOS CHECAMOS NO SPAWN SE STATE != EXPLODING, ou mudar la
				}
				
				if(enemy1[i].getState() == ACTIVE){
			
					GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemy1[i].getX(), enemy1[i].getY(), enemy1[i].getRadius());
				}
			}
			
			/* desenhando inimigos (tipo 2) */
			
			for(int i = 0; i < ENEMY_NUMBER; i++){
				
				if(enemy22.get(i).getState() == EXPLODING){
					
					double alpha = (currentTime - enemy22.get(i).getExplosion_start()) / (enemy22.get(i).getExplosion_end() - enemy22.get(i).getExplosion_start());
					GameLib.drawExplosion(enemy22.get(i).getX(), enemy22.get(i).getY(), alpha);
				}
				
				if(enemy22.get(i).getState() == ACTIVE){
					
			
					GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemy22.get(i).getX(), enemy22.get(i).getY(), enemy22.get(i).getRadius());
				}
			}

			/* desenhando inimigos (tipo 3) */
			
			for(int i = 0; i < enemy3.length; i++){
				
				if(enemy3[i].getState() == EXPLODING){
					
					double alpha = (currentTime - enemy3[i].getExplosion_start()) / (enemy3[i].getExplosion_end() - enemy3[i].getExplosion_start());
					GameLib.drawExplosion(enemy3[i].getX(), enemy3[i].getY(), alpha);
				}
				
				if(enemy3[i].getState() == ACTIVE){
			
					GameLib.setColor(Color.orange);
					GameLib.drawTriangle(enemy3[i].getX(), enemy3[i].getY(), enemy3[i].getRadius());
				}
			}


			/* desenhando nuke */
			
			for(int i = 0; i < nuke.length; i++){
								
				if(nuke[i].getState() == ACTIVE){
			
					GameLib.setColor(Color.PINK);
					GameLib.drawCircle(nuke[i].getX(), nuke[i].getY(), nuke[i].getRadius());
				}
            }

			/* desenhando megablaster */
			
			for(int i = 0; i < megablaster.length; i++){
								
				if(megablaster[i].getState() == ACTIVE){
			
					GameLib.setColor(Color.WHITE);
					GameLib.drawCircle(megablaster[i].getX(), megablaster[i].getY(), megablaster[i].getRadius());
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

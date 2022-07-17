import java.awt.Color;

public class Powerup{	
        /* variaveis do powerup */
		
		int [] powerup_states = new int[10];					// estados
		double [] powerup_X = new double[10];					// coordenadas x
		double [] powerup_Y = new double[10];					// coordenadas y
		double [] powerup_V = new double[10];					// velocidades
		double [] powerup_angle = new double[10];				// Ã¢ngulos (indicam direÃ§Ã£o do movimento)
		double [] powerup_RV = new double[10];					// velocidades de rotaÃ§Ã£o
		double powerup_radius = 5.0;						// raio (tamanho do inimigo 1)
		long nextpowerup = currentTime + 20000;					// instante em que um novo inimigo 1 deve aparecer


        for(int i = 0; i < powerup_states.length; i++) enemy2_states[i] = INACTIVE;

				/* colisÃµes player - inimigos */
							
				for(int i = 0; i < powerup_states.length; i++){
					
					double dx = powerup_X[i] - player.getX();
					double dy = powerup_Y[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + powerup_radius) * 0.8){
                        powerup_states[i] = INACTIVE;
						player.setState(ACTIVE);
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
    
                
			/* Powerup */
			
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

			/* verificando se novos Powerups devem ser "lanÃ§ados" */
			
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

			/* desenhando powerup */
			
			for(int i = 0; i < powerup_states.length; i++){
								
				if(powerup_states[i] == ACTIVE){
			
					GameLib.setColor(Color.PINK);
					GameLib.drawCircle(powerup_X[i], powerup_Y[i], powerup_radius);
				}
            }
        }
}

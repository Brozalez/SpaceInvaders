package Principal;

public interface Keyboard {
	public abstract void moverCima(long delta);
	public abstract void moverBaixo(long delta);
	public abstract void moverDireita(long delta);
	public abstract void moverEsquerda(long delta);
	public abstract void atirar(long delta);
}

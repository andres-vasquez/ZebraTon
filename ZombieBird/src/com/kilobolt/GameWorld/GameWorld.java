package com.kilobolt.GameWorld;

import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.kilobolt.GameObjects.Bird;
import com.kilobolt.GameObjects.ScrollHandler;
import com.kilobolt.ZBHelpers.AssetLoader;

public class GameWorld {

	private Bird bird;
	private ScrollHandler scroller;
	private Rectangle ground;
	private int score = 0;
	private float runTime = 0;
	private int midPointY;
	private GameRenderer renderer;
	private GameState currentState;
	private String[] mensajes;
	private String[] mensajes_total;
	private int minAnterior=-99;
	
	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE, MENSAJE
	}

	/*
	 * Inicializa el juego, pantalla de inicio
	 */
	public GameWorld(int midPointY) 
	{
		this.mensajes_total=new String[]{
				"Cuida tu vida",
				"Tu seguridad dependede|de ti",
				"Contra el mal por el|bien de todos",
				"Cuando salgas distribuye| tu dinero",
				"Evita salir con|demasiado dinero",
				"En caso de asalto|no te resistas",
				"Hoga iluminado repele|delincuentes",
				"No uses tu celular en|la calle",
				"No te distraigas al|caminar en la calle"};
		currentState = GameState.MENU;
		this.midPointY = midPointY;
		bird = new Bird(33, midPointY - 5, 17, 12);
		scroller = new ScrollHandler(this, midPointY + 66);
		ground = new Rectangle(0, midPointY + 66, 137, 11);
	}

	public void update(float delta) 
	{
		runTime += delta;
		switch (currentState) {
		case READY:
		case MENU:
			updateReady(delta);
			break;
		case RUNNING:
			updateRunning(delta);
			break;
		case MENSAJE:
		default:
			break;
		}
	}

	private void updateReady(float delta) 
	{
		bird.updateReady(runTime);
		scroller.updateReady(delta);
	}

	public void updateRunning(float delta) 
	{
		//TODO: delta=delta/1.5f;  ACA se cambia la velocidad
		if (delta > .15f) {
			delta = .15f;
		}

		bird.update(delta);
		scroller.update(delta);

		//Control de Score
		if(scroller.getScore()>0 && scroller.getScore()>minAnterior && scroller.getScore()%10==0){
			String mensaje_seleccionado=this.mensajes_total[randInt(0, this.mensajes_total.length-1)];
			this.mensajes=mensaje_seleccionado.split("\\|");
			
			scroller.stop();
			bird.die();
			mensaje();
			minAnterior=scroller.getScore();
		}

		//Si se choca con un obstaculo
		if (scroller.collides(bird) && bird.isAlive()) 
		{
			scroller.stop();
			bird.die();
			AssetLoader.dead.play();
			renderer.prepareTransition(255, 255, 255, .3f);
			AssetLoader.fall.play();
		}

		//Si se cae al piso
		if (Intersector.overlaps(bird.getBoundingCircle(), ground)) 
		{
			if (bird.isAlive()) 
			{
				AssetLoader.dead.play();
				renderer.prepareTransition(255, 255, 255, .3f);
				bird.die();
			}

			scroller.stop();
			bird.decelerate();
			currentState = GameState.GAMEOVER;

			if (score > AssetLoader.getHighScore()) 
			{
				AssetLoader.setHighScore(score);
				currentState = GameState.HIGHSCORE;
			}
		}
	}

	public Bird getBird() {
		return bird;

	}

	public int getMidPointY() {
		return midPointY;
	}

	public ScrollHandler getScroller() {
		return scroller;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int increment) {
		score += increment;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}
	
	public void mensaje() {
		currentState = GameState.MENSAJE;
	}

	public void ready() {
		currentState = GameState.READY;
		renderer.prepareTransition(0, 0, 0, 1f);
	}

	public void restart(int ScoreAnterior) {
		score = ScoreAnterior;
		bird.onRestart(midPointY - 5);
		scroller.onRestart();
		ready();
	}

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}

	public boolean isHighScore() {
		return currentState == GameState.HIGHSCORE;
	}

	public boolean isMenu() {
		return currentState == GameState.MENU;
	}

	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}

	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}
	
	public boolean isMensaje(){
		return currentState == GameState.MENSAJE;
	}
	
	private int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public String[] getMensajes(){
		return this.mensajes;
	}
	
	public void setMensajes(String[] mensajes){
		this.mensajes=mensajes;
	}

	public int getMinAnterior() {
		return minAnterior;
	}

	public void setMinAnterior(int minAnterior) {
		this.minAnterior = minAnterior;
	}
}

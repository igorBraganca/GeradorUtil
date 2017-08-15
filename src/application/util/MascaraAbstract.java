package application.util;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public abstract class MascaraAbstract <A, B> implements EventHandler<KeyEvent> {
	/**
	 * Formata a String de acordo com a mascara.
	 */
	abstract public A format(B b);
	
	/**
	 * Retorna o valor original sem mascara.
	 */
	abstract public B parse(A a);

}

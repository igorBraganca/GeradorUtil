package application.util;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MascaraUtil {

	private MascaraUtil() {

	}

	public final static MascaraAbstract<String, String> MASCARA_NIP = new MascaraSimples("##.####.##");
	public final static MascaraAbstract<String, String> MASCARA_CPF = new MascaraSimples("###.###.###-##");
	public final static MascaraAbstract<String, String> MASCARA_CNPJ = new MascaraSimples("###.###.###-##");
	public final static MascaraAbstract<String, String> MASCARA_CPF_CNPJ = new MascaraDupla("###.###.###-##", "##.###.###/####-##");
	public final static MascaraAbstract<String, String> MASCARA_TELEFONE = new MascaraDupla("(##) ####-####", "(##) #####-####");
	public final static MascaraAbstract<String, Number> MASCARA_DECIMAL = new MascaraDecimal(Integer.valueOf(15), Integer.valueOf(2));
	public final static MascaraAbstract<String, Integer> MASCARA_INTEIRO = new MascaraInteiro(Integer.MAX_VALUE);
	public final static MascaraAbstract<String, Integer> MASCARA_INTEIRO_10 = new MascaraInteiro(10);

	static private class MascaraSimples extends MascaraAbstract<String, String> {
		private Logger logger = LogManager.getLogger();

		private String mascara = "";
		private Integer maxLenght = 0;
		private MaskFormatter mask = null;

		public MascaraSimples(String mascara) {
			try {
				mask = new MaskFormatter(mascara);
				mask.setValueContainsLiteralCharacters(false);

				this.mascara = mascara;
				this.maxLenght = StringUtils.countMatches(this.mascara, '#');
			} catch (ParseException e) {
				logger.catching(e);

				this.maxLenght = 0;
			}
		}

		public void handle(KeyEvent e) {
			if (e.getSource() instanceof TextField) {
				TextField tf = (TextField) e.getSource();

				if (tf.getText() != null) {
					// limita os caracteres
					String text = tf.getText().replaceAll("[^0-9]", "");

					// limita o tamanho
					if (text.length() > maxLenght) {
						text = text.substring(0, maxLenght);
					}

					// formata
					if (text.length() == maxLenght) {
						if (mask != null) {
							try {
								text = mask.valueToString(text);
							} catch (ParseException exception) {
								logger.catching(exception);
							}
						}
					}

					tf.setText(text);
					tf.positionCaret(text.length());
				}
			}
		}

		@Override
		public String format(String s) {
			if (s == null) {
				return null;
			}

			try {
				return mask.valueToString(s.replaceAll("[^0-9]", ""));
			} catch (ParseException exception) {
				logger.catching(exception);

				return null;
			}
		}

		@Override
		public String parse(String s) {
			if (s == null) {
				return null;
			}

			return s.replaceAll("[^0-9]", "");
		}
	}

	static private class MascaraDupla extends MascaraAbstract<String, String> {
		private Logger logger = LogManager.getLogger();

		private int maxLenght = 0;
		private int minLenght = 0;
		private MaskFormatter maskLower = null;
		private MaskFormatter maskBigger = null;

		private MascaraDupla(String mascara1, String mascara2) {
			try {
				if (mascara1.length() >= mascara2.length()) {
					maskLower = new MaskFormatter(mascara2);
					maskLower.setValueContainsLiteralCharacters(false);

					maskBigger = new MaskFormatter(mascara1);
					maskBigger.setValueContainsLiteralCharacters(false);

					this.minLenght = StringUtils.countMatches(mascara2, '#');
					this.maxLenght = StringUtils.countMatches(mascara1, '#');
				} else {
					maskLower = new MaskFormatter(mascara1);
					maskLower.setValueContainsLiteralCharacters(false);

					maskBigger = new MaskFormatter(mascara2);
					maskBigger.setValueContainsLiteralCharacters(false);

					this.minLenght = StringUtils.countMatches(mascara1, '#');
					this.maxLenght = StringUtils.countMatches(mascara2, '#');
				}

			} catch (ParseException e) {
				logger.catching(e);

				this.maxLenght = 0;
			}
		}

		public void handle(KeyEvent e) {
			if (e.getSource() instanceof TextField) {
				TextField tf = (TextField) e.getSource();

				if (tf.getText() != null) {
					// limita os caracteres
					String text = tf.getText().replaceAll("[^0-9]", "");

					// limita o tamanho
					if (text.length() > maxLenght) {
						text = text.substring(0, maxLenght);
					}

					// formata
					if (text.length() == minLenght && maskLower != null) {
						try {
							text = maskLower.valueToString(text);
						} catch (ParseException exception) {
							logger.catching(exception);
						}
					} else if (text.length() > minLenght && text.length() < maxLenght) {
						text = tf.getText().replaceAll("[^0-9]", "");
					} else if (text.length() == maxLenght && maskBigger != null) {
						try {
							text = maskBigger.valueToString(text);
						} catch (ParseException exception) {
							logger.catching(exception);
						}
					}

					tf.setText(text);
					tf.positionCaret(text.length());
				}
			}
		}

		public String format(String s) {
			if (s == null) {
				return null;
			}

			try {
				s = s.replaceAll("[^0-9]", "");

				if (s.length() == this.minLenght) {
					return maskLower.valueToString(s);
				} else if (s.length() == this.maxLenght) {
					return maskBigger.valueToString(s);
				} else {
					return s;
				}
			} catch (ParseException exception) {
				logger.catching(exception);

				return null;
			}
		}

		@Override
		public String parse(String s) {
			if (s == null) {
				return null;
			}

			return s.replaceAll("[^0-9]", "");
		}
	}

	static private class MascaraDecimal extends MascaraAbstract<String, Number> {
		private Logger logger = LogManager.getLogger();
		
		private final static Integer DEFAULT_INTEGER_LENGHT = new Integer(9);
		private final static Integer DEFAULT_DECIMAL_LENGHT = new Integer(2);

		private String createPattern(int integerLenght, int decimalLenght) {
			StringBuilder pattern = new StringBuilder();

			for (int i = 0; i < integerLenght; i++) {
				if ((i + 1) == integerLenght) {
					pattern.append("0");
				} else {
					pattern.append("#");
				}
			}

			for (int i = pattern.length() - 3; i > 0; i = i - 3) {
				pattern.insert(i, ",");
			}

			if (decimalLenght > 0) {
				pattern.append(".");

				for (int i = 0; i < decimalLenght; i++) {
					if (i < 2) {
						pattern.append("0");
					} else {
						pattern.append("#");
					}
				}
			}

			return pattern.toString();
		}

		private DecimalFormat decimalFormat = null;
		private Integer maxLenght = null;

		private MascaraDecimal(Integer integerLenght, Integer decimalLenght) {
			if (integerLenght == null) {
				integerLenght = MascaraDecimal.DEFAULT_INTEGER_LENGHT;
			}

			if (decimalLenght == null) {
				decimalLenght = MascaraDecimal.DEFAULT_DECIMAL_LENGHT;
			}

			String pattern = this.createPattern(integerLenght, decimalLenght);

			this.decimalFormat = new DecimalFormat(pattern);
			this.maxLenght = integerLenght + decimalLenght;
		}

		public void handle(KeyEvent e) {
			if (e.getSource() instanceof TextField) {
				TextField tf = (TextField) e.getSource();

				if (tf.getText() != null) {
					String text = tf.getText().replaceAll("[^0-9]", "");

					if (StringUtils.isBlank(text)) {
						text = "0";
					} else {
						if (text.length() > maxLenght) {
							text = text.substring(0, maxLenght);
						}
					}

					Double valor = Double.parseDouble(text) / 100;

					text = decimalFormat.format(valor);

					tf.setText(text);
					tf.positionCaret(text.length());
				}
			}
		}

		@Override
		public String format(Number number) {
			if (number == null) {
				return null;
			}

			return this.decimalFormat.format(number);
		}

		@Override
		public Number parse(String string) {
			if (string == null) {
				return null;
			}

			try {
				return this.decimalFormat.parse(string);
			} catch (ParseException e) {
				logger.catching(e);

				return null;
			}
		}
	}

	static private class MascaraInteiro extends MascaraAbstract<String, Integer> {
		private Integer maxLenght;

		public MascaraInteiro(int maxLenght) {
			this.maxLenght = maxLenght;
		}

		public void handle(KeyEvent e) {
			if (e.getSource() instanceof TextField) {
				TextField tf = (TextField) e.getSource();

				if (tf.getText() != null) {
					// limita os caracteres somente numericos
					String text = tf.getText().replaceAll("[^0-9]", "");

					// limita o tamanho
					if (text.length() > maxLenght) {
						text = text.substring(0, maxLenght);
					}

					tf.setText(text);
					tf.positionCaret(text.length());
				}
			}
		}

		@Override
		public String format(Integer i) {
			if (i == null) {
				return null;
			}

			return i.toString();
		}

		@Override
		public Integer parse(String s) {
			if (s == null) {
				return null;
			}

			return Integer.valueOf(s.replaceAll("[^0-9]", ""));
		}
	}
}

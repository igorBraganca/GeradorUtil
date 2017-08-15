package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

import application.util.CPF;
import application.util.MascaraUtil;
import application.util.NIP;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class GeradorValidadorController {

	@FXML private ResourceBundle resources;
	@FXML private URL location;

	@FXML private TextField nipGerado;
	@FXML private TextField cpfGerado;
	@FXML private TextField nipValidar;
	@FXML private TextField cpfValidar;
	@FXML private Label nipValido;
	@FXML private Label nipInvalido;
	@FXML private Label cpfValido;
	@FXML private Label cpfInvalido;

	private BooleanProperty isNIPValido = new SimpleBooleanProperty(false);
	private BooleanProperty isCPFValido = new SimpleBooleanProperty(false);

	final KeyCombination keyCombinationCtrlC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);

	@FXML
	void initialize() {
		assert nipGerado != null : "fx:id=\"nipGerado\" was not injected: check your FXML file 'Untitled'.";
		assert cpfGerado != null : "fx:id=\"cpfGerado\" was not injected: check your FXML file 'Untitled'.";
		assert nipValidar != null : "fx:id=\"nipValidar\" was not injected: check your FXML file 'Untitled'.";
		assert cpfValidar != null : "fx:id=\"cpfValidar\" was not injected: check your FXML file 'Untitled'.";
		assert nipValido != null : "fx:id=\"nipValido\" was not injected: check your FXML file 'GeradorValidador.fxml'.";
		assert nipInvalido != null : "fx:id=\"nipInvalido\" was not injected: check your FXML file 'GeradorValidador.fxml'.";
		assert cpfValido != null : "fx:id=\"cpfValido\" was not injected: check your FXML file 'GeradorValidador.fxml'.";
		assert cpfInvalido != null : "fx:id=\"cpfInvalido\" was not injected: check your FXML file 'GeradorValidador.fxml'.";

		nipValido.managedProperty().bind(nipValido.visibleProperty());
		nipInvalido.managedProperty().bind(nipInvalido.visibleProperty());
		cpfValido.managedProperty().bind(cpfValido.visibleProperty());
		cpfInvalido.managedProperty().bind(cpfInvalido.visibleProperty());

		nipValido.visibleProperty().bind(isNIPValido.and(nipValidar.textProperty().isNotEmpty()));
		nipInvalido.visibleProperty().bind(isNIPValido.not().and(nipValidar.textProperty().isNotEmpty()));

		cpfValido.visibleProperty().bind(isCPFValido.and(cpfValidar.textProperty().isNotEmpty()));
		cpfInvalido.visibleProperty().bind(isCPFValido.not().and(cpfValidar.textProperty().isNotEmpty()));

		setNovoNIP();
		setNovoCPF();

		cpfValidar.textProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null && !newValue.isEmpty()) {
					isCPFValido.set(CPF.isValid(MascaraUtil.MASCARA_CPF.parse(newValue)));
				}
			}

		});

		nipValidar.textProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null && !newValue.isEmpty()) {
					isNIPValido.set(NIP.isValid(MascaraUtil.MASCARA_NIP.parse(newValue)));
				}
			}

		});

		cpfValidar.setOnKeyReleased(MascaraUtil.MASCARA_CPF);
		nipValidar.setOnKeyReleased(MascaraUtil.MASCARA_NIP);

		cpfGerado.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				if (keyCombinationCtrlC.match(event)) {
					System.out.println("CTRL + C");

					Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
					Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

					systemClipboard.setContents(new StringSelection(MascaraUtil.MASCARA_CPF.parse(cpfGerado.textProperty().getValue())), null);
				}
			}
		});

		nipGerado.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				if (keyCombinationCtrlC.match(event)) {
					System.out.println("CTRL + C");

					Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
					Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

					systemClipboard.setContents(new StringSelection(MascaraUtil.MASCARA_NIP.parse(nipGerado.textProperty().getValue())), null);
				}
			}
		});
	}

	@FXML
	void gerarNovoCPF(ActionEvent event) {
		setNovoCPF();
	}

	@FXML
	void gerarNovoNIP(ActionEvent event) {
		setNovoNIP();
	}

	@FXML
	void validarCPF(ActionEvent event) {
		if (!cpfValidar.textProperty().getValue().isEmpty()) {
			isCPFValido.set(CPF.isValid(MascaraUtil.MASCARA_CPF.parse(cpfValidar.textProperty().getValue())));
		}
	}

	@FXML
	void validarNIP(ActionEvent event) {
		if (!nipValidar.textProperty().getValue().isEmpty()) {
			isNIPValido.set(NIP.isValid(MascaraUtil.MASCARA_NIP.parse(nipValidar.textProperty().getValue())));
		}
	}

	private void setNovoNIP() {
		nipGerado.textProperty().setValue(MascaraUtil.MASCARA_NIP.format(NIP.create()));
	}

	private void setNovoCPF() {
		cpfGerado.textProperty().setValue(MascaraUtil.MASCARA_CPF.format(CPF.create()));
	}
}

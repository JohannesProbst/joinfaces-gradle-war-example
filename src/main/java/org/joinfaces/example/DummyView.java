package org.joinfaces.example;


import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.omnifaces.cdi.ViewScoped;

/**
 * @author Lars Grefer
 */
@Named
@ViewScoped
public class DummyView implements Serializable {

	public String getText() {
		return "Hello from JSF: " + LocalDateTime.now();
	}
}

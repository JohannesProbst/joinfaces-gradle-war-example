package org.joinfaces.example;


import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.omnifaces.cdi.ViewScoped;

@Named
@ViewScoped
public class DummyView implements Serializable {


	private final transient HttpSession session;

	@Inject
	public DummyView(HttpSession session) {
		this.session = session;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getText() {
		return "Hello from JSF: " + LocalDateTime.now() + " session: " + session.getId();
	}
}

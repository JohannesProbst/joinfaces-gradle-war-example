package org.joinfaces.example;

import com.sun.faces.cdi.CdiUtils;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.joinfaces.example.cdi.SpringBeanPicker;
import org.omnifaces.util.Messages;

@FacesConverter(value = "exampleConverter")
public class ExampleConverter implements Converter<String> {

	private final DummyBean dummyBean = SpringBeanPicker.getContext().getBean(DummyBean.class);

	private final DummyView dummyView = CdiUtils.getBeanInstance(DummyView.class, false);

	@Override
	public String getAsObject(final FacesContext context, final UIComponent component,
			final String value) {
		Messages.addInfo(component.getClientId(context), "Converter ''{0}''", this.hashCode());
		Messages.addInfo(component.getClientId(context), "Injected CDI ''{0}''", dummyView.hashCode());
		Messages.addInfo(component.getClientId(context), "Injected Spring ''{0}''", dummyBean.hashCode());
		return value;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component,
			final String value) {
		return value != null ? value : "";
	}
}

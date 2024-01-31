package org.joinfaces.example.cdi;

import jakarta.annotation.Priority;
import java.nio.file.Path;
import org.jboss.weld.environment.deployment.discovery.BeanArchiveBuilder;
import org.jboss.weld.environment.deployment.discovery.FileSystemBeanArchiveHandler;

/**
 * Bei der Entwicklung in IntelliJ stimmt der Pfad der Klassen im out Verzeichnis
 * nicht mit dem Pfad der Ressourcen überein (Klassen liegen unter out/classes aber
 * die Resourcen unter out/resources).
 * Daher kann Weld zwar die beans.xml finden aber dort keine Klassen.
 * Beim Entwickeln in der IDE liefert dieser Service-Provider den korrekten Pfad.
 */
@Priority(Integer.MAX_VALUE)
public class IntelliJFileSystemBeanArchivHandler extends FileSystemBeanArchiveHandler {
	@Override
	public BeanArchiveBuilder handle(String path) {
		Path testPath = null;
		try {
			testPath = Path.of(path);
		} catch (Exception e) {
			return null;
		}
		return super.handle(testPath.getParent().resolve(Path.of("classes")).toString());
	}
}

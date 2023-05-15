package de.nikolas.einsteinmedia.commons.httpserver.http.annotation;

import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nikolas Rummel
 * @since 06.10.2021
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpMapping {

  String[] path();

  HttpMethod[] method();
}

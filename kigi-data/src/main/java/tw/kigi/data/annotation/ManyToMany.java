package tw.kigi.data.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
	String schema();
	String relation();
	String foreignKey();
	String conditions();
	String[] properties() default {};
	OrderBy[] sort() default {};
}

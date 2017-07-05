package com.bowlong.net.proto.gen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface B2Field {
	// 备注
	String remark() default "";

	// 默认值
	String def() default "";

	// excel表中列队(纵队) index
	String column() default "";
}

package com.bowlong.net.proto.gen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface B2Class {
	String type() default B2G.DATA; // Server, Client, Data

	String namespace() default "";

	String remark() default "";

	boolean isXls() default true; // [true:表示excel的后缀名为xls,false:表示excel的后缀名为xlsx]
	
	String sheetName() default "";
	
	String sheetCName() default ""; //根据sheet创建Class

	String sheetType() default B2G.SHEET_NONE; // "", Sheet, SheetRow

	boolean constant() default false;
}

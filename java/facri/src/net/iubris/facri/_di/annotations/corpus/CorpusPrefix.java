package net.iubris.facri._di.annotations.corpus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD }) 
@Retention(RetentionPolicy.RUNTIME)
public @interface CorpusPrefix {}
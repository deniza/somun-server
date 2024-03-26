package gss.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

// This annotation is used to mark methods that can be called from the client.
// The method must be public and have the following signature:
// void methodName(int parameter1, String parameter2, ..., GssConnection con)
// The method can have any number of parameters, but the last one must be a GssConnection object.
// Method must not return any value.
// The class that contains the method should implement GssCallable interface and be registered in GssServer.

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GssCallable {
}

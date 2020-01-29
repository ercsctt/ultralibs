package uk.co.ericscott.ultralibs.sql.callback;

public interface QueryCallback<V extends Object, T extends Throwable> {
    void call(V result, T thrown);
}
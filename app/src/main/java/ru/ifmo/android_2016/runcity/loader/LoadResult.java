package ru.ifmo.android_2016.runcity.loader;

/**
 * Created by -- on 17.12.2016.
 */

public class LoadResult<T> {
    public final ResultType resultType;

    public final T data;

    public LoadResult(ResultType resultType, T data) {
        this.resultType = resultType;
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoadResult(resultType=" + resultType
                + ", data=" + data
                + ")";
    }
}

package com.carlt.doride.http.retrofitnet;

public interface IApiService {

    <T> T getService(final Class<T> service);

}

package com.example.rxmvp.domain.common;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;

public class Transaction<T> {
  private T body;
  private Throwable error;

  public Transaction(T body, Throwable error) {
    this.body = body;
    this.error = error;
  }

  public static <T> Transaction<T> success(T body) {
    return new Transaction<>(body, null);
  }

  public static <T> Transaction<T> error(Throwable error) {
    return new Transaction<>(null, error);
  }

  public static <T> Transaction<T> create(Result<T> result) {
    if (result.isError()) {
      return error(result.error());
    } else if (!result.response().isSuccessful()) {
      return error(new ApiException(result.response().errorBody()));
    } else {
      return success(result.response().body());
    }
  }

  public T body() {
    return body;
  }

  public Throwable error() {
    return error;
  }

  public boolean isSuccessful() {
    return error == null;
  }

  public boolean isError() {
    return !isSuccessful();
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Transaction<?> that = (Transaction<?>) o;

    if (body != null ? !body.equals(that.body) : that.body != null) return false;
    return error != null ? error.equals(that.error) : that.error == null;
  }

  @Override public int hashCode() {
    int result = body != null ? body.hashCode() : 0;
    result = 31 * result + (error != null ? error.hashCode() : 0);
    return result;
  }

  public static class ApiException extends Exception {
    private ResponseBody errorBody;

    public ApiException(ResponseBody errorBody) {
      this.errorBody = errorBody;
    }

    public ResponseBody errorBody() {
      return errorBody;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ApiException that = (ApiException) o;

      return errorBody != null ? errorBody.equals(that.errorBody) : that.errorBody == null;
    }

    @Override public int hashCode() {
      return errorBody != null ? errorBody.hashCode() : 0;
    }
  }
}

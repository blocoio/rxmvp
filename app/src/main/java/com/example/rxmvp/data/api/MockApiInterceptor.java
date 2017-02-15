package com.example.rxmvp.data.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.inject.Inject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.example.rxmvp.common.di.PerApplication;

@PerApplication public class MockApiInterceptor implements Interceptor {

  @Inject public MockApiInterceptor() {
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    MockResponse mockResponse = getResponse(request);

    return new Response.Builder().request(request)
        .code(mockResponse.code)
        .protocol(Protocol.HTTP_2)
        .body(ResponseBody.create(MediaType.parse("text/json"), mockResponse.body))
        .build();
  }

  private MockResponse getResponse(Request request) {
    String requestUrl = request.url().encodedPath();

    if (requestUrl.matches(".*/vehicles/?")) {
      return new MockResponse(VEHICLES);
    } else {
      return new MockResponse("{}");
    }
  }

  private class MockResponse {
    int code;
    String body;

    private MockResponse(String body) {
      this(HttpURLConnection.HTTP_OK, body);
    }

    private MockResponse(int code, String body) {
      this.code = code;
      this.body = body;
    }
  }

  private static final String VEHICLES = "{"
      + "  \"total\": 2,"
      + "  \"limit\": 10,"
      + "  \"page\": 0,"
      + "  \"list\": ["
      + "    {"
      + "      \"id\": 100,"
      + "      \"description\": \"Honda NSX\","
      + "      \"status\": \"ACTIVE\","
      + "      \"troubles\": ["
      + "        {"
      + "          \"id\": 100,"
      + "          \"code\": \"P0055\","
      + "          \"description\": \"HO2S Heater Resistance (Bank 1 Sensor 3)\","
      + "          \"startDate\": \"2017-01-18 22:34:09\""
      + "        }"
      + "      ]"
      + "    },"
      + "    {"
      + "      \"id\": 101,"
      + "      \"description\": \"BMW X3\","
      + "      \"status\": \"INACTIVE\","
      + "      \"troubles\": ["
      + "        {"
      + "          \"id\": 101,"
      + "          \"code\": \"P0503\","
      + "          \"description\": \"Vehicle Speed Sensor Intermittent/Erratic/High\","
      + "          \"startDate\": \"2017-01-18 22:34:09\""
      + "        },"
      + "        {"
      + "          \"id\": 102,"
      + "          \"code\": \"B1236\","
      + "          \"description\": \"Window Feedback Loss of Signal\","
      + "          \"startDate\": \"2017-01-18 22:34:09\""
      + "        }"
      + "      ]"
      + "    }"
      + "  ]"
      + "}";
}
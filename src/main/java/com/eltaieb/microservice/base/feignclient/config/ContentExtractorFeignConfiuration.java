package com.eltaieb.microservice.base.feignclient.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import com.eltaieb.microservice.base.config.BaseServiceConstant;
import com.eltaieb.microservice.base.exception.LocalizedServiceException;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

@Configuration
public class ContentExtractorFeignConfiuration {

	@Autowired
	ObjectFactory<HttpMessageConverters> messageConverters;
	Gson gson =  new Gson();
	@Bean
	public Decoder feignDecoder() {
		return new Decoder() {
			private SpringDecoder springDecoder = new SpringDecoder(messageConverters);;

			@Override
			public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {

				if (Boolean.FALSE == type.getClass().equals(ServiceResponse.class)) {

					try {

						return extractContent(response, type);

					} catch (LocalizedServiceException ex) {
						// circuit breaker or global exception handler intercepter will take care of
						throw ex;
					} catch (Exception ex) {
						// just ignore ,, spring decoder will take care of
						
						System.out.println();
					}
				}

				return springDecoder.decode(response, type);

			}

			private Object extractContent(Response response, Type type) throws Exception {
				InputStream inputStream = response.body().asInputStream();
				String responseBodyAsAstring = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(responseBodyAsAstring);
				JsonObject jsonObject = jsonElement.getAsJsonObject();
 				if (null != jsonObject) {
 					JsonElement codeJsonElement = jsonObject.get("code");
 					JsonElement messageJsonElement = jsonObject.get("message");
 					
					if (null != codeJsonElement) {
						if (BaseServiceConstant.GLOBAL_SUCCESS_CODE.equals(codeJsonElement.getAsString())) {
							JsonElement content = jsonObject.get("content");
							return gson.fromJson(content, type);
						} else {
							throw new LocalizedServiceException(codeJsonElement !=null ? codeJsonElement.getAsString():"undefined",
									messageJsonElement!=null ? messageJsonElement.getAsString():"undefined");
						}
					}
				}
				// intentionally throwing exception .. let spring decoder handle it
				throw new Exception();

			}
		};
	}
}

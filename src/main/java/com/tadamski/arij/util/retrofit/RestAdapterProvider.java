package com.tadamski.arij.util.retrofit;

import com.google.gson.*;
import com.tadamski.arij.account.service.LoginInfo;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tmszdmsk on 02.07.13.
 */
public class RestAdapterProvider {

    public static <T> T get(Class<T> clazz, LoginInfo loginInfo) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new ISODateAdapter()).create();
        return new RestAdapter.Builder().setConverter(new GsonConverter(gson)).setDebug(true).setServer(loginInfo.getBaseURL()).setRequestInterceptor(new AuthorizationInterceptor(loginInfo)).build().create(clazz);
    }

    static class ISODateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        private final DateFormat iso8601Format;

        ISODateAdapter() {
            this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        }

        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            String dateFormatAsString = iso8601Format.format(src);
            return new JsonPrimitive(dateFormatAsString);
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }
            Date date = deserializeToDate(json);
            if (typeOfT == Date.class) {
                return date;
            } else if (typeOfT == Timestamp.class) {
                return new Timestamp(date.getTime());
            } else if (typeOfT == java.sql.Date.class) {
                return new java.sql.Date(date.getTime());
            } else {
                throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
            }
        }

        private Date deserializeToDate(JsonElement json) {
            try {
                return iso8601Format.parse(json.getAsString());
            } catch (ParseException e) {
                throw new JsonSyntaxException(json.getAsString(), e);
            }
        }
    }

}

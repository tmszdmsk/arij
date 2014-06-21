package com.tadamski.arij.account.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 03.06.13
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
@EBean
public class LoginService {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public Response checkCredentials(LoginInfo loginInfo) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(loginInfo.getBaseURL())
                .setConverter(new GsonConverter(gson))
                .setClient(new RestAdapterProvider.UrlConnectionClient(loginInfo.isSecureHttps()))
                .build();
        LoginResource loginResource = restAdapter.create(LoginResource.class);
        Response response = loginResource.checkCredentials(loginInfo);
        return response;
    }
}

package tutorial.androdev.apisoundcloudplayer;

import retrofit.RestAdapter;

/**
 * Created by Administrator on 25-Feb-16.
 * we’ll be creating a RestAdapter and
 * SCService each time a request is made and
 * those are expensive operations, so overall performance will be affected.
 * We’ll therefore use the Singleton pattern so that the RestAdapter and SCService are created only once
 * and used whenever needed. Add the following class to the project.
 */
public class SoundCloud {
    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder().setEndpoint(Config.API_URL).build();
    private static final InterfaceAPISoundcloud SERVICE = REST_ADAPTER.create(InterfaceAPISoundcloud.class);

    /**
     * This creates the rest adapter and service as we had done previously (MainActivity.class),
     * then it includes a function that will return the service.
     * Since REST_ADAPTER and SERVICE are final and static,
     * they will only be created once and reused each time a SoundCloud object is created.
     */
    public static InterfaceAPISoundcloud getService(){
        return SERVICE;
    }
}

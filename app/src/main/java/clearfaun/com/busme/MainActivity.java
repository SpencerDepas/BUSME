package clearfaun.com.busme;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {



    private static LocationManager locationManager;
    private static String provider;
    static EditText editText;
    static EditText editTextTwo;
    static EditText editTextThree;

    final static public String API_KEY = "05a5c2c8-432a-47bd-8f50-ece9382b4b28";


    static String testLat = "40.6455520";
    static String testLng = "-73.9829084";


    static double latatude;
    static double longitude;
    static int radiusForBusStop = 125;
    static List<Address> addresses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }


    static Geocoder geocoder;

    private static class GpsToAddress extends AsyncTask<Location, Void, String> {



        @Override
        protected String doInBackground(Location... params) {

            geocoder = new Geocoder(mContext, Locale.ENGLISH);
            String result = null;
            int bob = 0;



            try
            {
                bob++;
                addresses = geocoder.getFromLocation(latatude, longitude, 1);
                StringBuilder str = new StringBuilder();



                if (addresses != null && addresses.size() > 0 ){
                    bob++;

                    Address returnAddress = addresses.get(0);
                    returnAddress.toString();

                    String localityString = returnAddress.getAddressLine(0);
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    return localityString;
                }
                else
                {
                    return "Cock";
                }
            } catch (IOException e) {
                bob+= 55;
            }



            return bob + "";
        }

        @Override
        protected void onPostExecute(String address) {

            toaster(address + "");
            //editText.setText(address);
            if(String.valueOf(longitude).length() > 8){

                //MTAParseStopInfo.PlaceholderFragment.startTask(20);
                //MTAParseStopInfo.PlaceholderFragment.startTask(40);
                //MyTask myTask = new MyTask(this);


                MTAParseStopInfo.TechCrunchTask downloadTaskOne = new MTAParseStopInfo.TechCrunchTask();
                downloadTaskOne.stopRadius = 20;
                downloadTaskOne.execute();

                MTAParseStopInfo.TechCrunchTask downloadTaskTwo = new MTAParseStopInfo.TechCrunchTask();
                downloadTaskTwo.stopRadius = 40;
                downloadTaskTwo.execute();

                MTAParseStopInfo.TechCrunchTask downloadTaskThree = new MTAParseStopInfo.TechCrunchTask();
                downloadTaskThree.stopRadius = 150;
                downloadTaskThree.execute();

                //MTAParseStopInfo.PlaceholderFragment.startTask(150);

            }

        }

    }

    public static void onLocationChanged(Location location) {
        latatude =  location.getLatitude();
        longitude =  location.getLongitude();
        editText.setText(String.valueOf(latatude) + "\n" + String.valueOf(longitude));


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    static Context mContext;
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mContext = container.getContext();


            Button button = (Button) rootView.findViewById(R.id.button);
            editText = (EditText) rootView.findViewById(R.id.editText);
            editTextTwo = (EditText) rootView.findViewById(R.id.editTextTwo);
            editTextThree = (EditText) rootView.findViewById(R.id.editTextThree);





            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                   // (Context.LOCATION_SERVICE);

                    LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    boolean enabled = service
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);

                    // check if enabled and if not send user to the GSP settings
                    // Better solution would be to display a dialog and suggesting to



                 /*   // go to the settings
                    if (!enabled) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }*/



                    // Get the location manager
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    // Define the criteria how to select the locatioin provider -> use
                    // default
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    provider = locationManager.getBestProvider(criteria, true);
                    Location location = locationManager.getLastKnownLocation(provider);

                    // Initialize the location fields
                    if (location != null) {

                        onLocationChanged(location);
                    } else {
                        editText.setText("Location not available for test");

                    }

                    //get adress location
                    if(!editText.getText().toString().equals("Location not available")){

                        //String fakeLat = "40.64563681";
                        //String testLng = "-73.982776";

                        GpsToAddress task = new GpsToAddress();
                        task.execute();



                    }

                }
            });






            return rootView;
        }
    }


    static void toaster(String string){

        Toast.makeText(mContext,
                string, Toast.LENGTH_SHORT).show();

    }

}

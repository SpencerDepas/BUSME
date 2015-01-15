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
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
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
    String testLat = "40.645552099999996";
    String testLng = "-73.9829084";
    static double x;
    static double y;
    static List<Address> addresses;
    static EditText editTextTwo;


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
                addresses = geocoder.getFromLocation(x, y, 1);
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
            MTAParseFragment.PlaceholderFragment.startTask();
        }

    }

    public static void onLocationChanged(Location location) {
        double lat =  location.getLatitude();
        double lng =  location.getLongitude();
        editText.setText(String.valueOf(lat) + "\n" + String.valueOf(lng));
        x = lat;
        y = lng;

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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





            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                   // (Context.LOCATION_SERVICE);

                    LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    boolean enabled = service
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);

                    // check if enabled and if not send user to the GSP settings
                    // Better solution would be to display a dialog and suggesting to
                    // go to the settings
                    if (!enabled) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }



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

package io.github.lily_wittle.whereamiagain;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import java.util.List;

public class SensorLocatorDecode extends AsyncTask<Location,Void,String> {

    private Context theContext;
    private Activity theActivity;

    SensorLocatorDecode(Context context,Activity activity) {
        theContext = context;
        theActivity = activity;
    }

    protected String doInBackground(Location... location) {
        return(androidGeodecode(location[0]));
    }

    protected void onPostExecute(String result) {
        // speak and update text view with new location
        ((MainActivity)theActivity).speak(result);
        ((MainActivity)theActivity).setTextField(result);
    }

    private String androidGeodecode(Location thisLocation) {
        String addressLine;
        String locationName;

        if (Geocoder.isPresent()) {
            Geocoder androidGeocoder = new Geocoder(theContext);
            try {
                List<Address> addresses = androidGeocoder.getFromLocation(
                        thisLocation.getLatitude(),thisLocation.getLongitude(),1);
                if (addresses.isEmpty()) {
                    return("ERROR: Unknown location");
                } else {
                    Address firstAddress = addresses.get(0);
                    int index = 0;
                    locationName = "";
                    // get each line of address
                    while ((addressLine = firstAddress.getAddressLine(index)) != null) {
                        locationName += addressLine + ", ";
                        index++;
                    }
                    // strip off trailing comma
                    locationName = locationName.substring(0, locationName.length() - 2);

                    return (locationName);
                }
            } catch (Exception e) {
                return("ERROR: " + e.getMessage());
            }
        } else {
            return("ERROR: No Geocoder available");
        }
    }

}



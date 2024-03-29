package clearfaun.com.busme;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by spencer on 1/15/2015.
 */

public class MTAParseStopInfo {





    public static class TechCrunchTask extends AsyncTask<Void, Void, Void> {

        int stopRadius;


        @Override
        protected void onPreExecute() {



            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {
            //do your work here

            String stringLatatude = MainActivity.latatude + "";
            String stringlongitude = MainActivity.longitude + "";



            stringLatatude = stringLatatude.substring(0,9);
            stringlongitude = stringlongitude.substring(0,9);

            String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.xml?key=" + MainActivity.API_KEY + "&radius=" + stopRadius + "&lat=" +
                    stringLatatude + "&lon=" + stringlongitude;

            /*String downloadURL = "http://bustime.mta.info/api/where/stops-for-location.xml?key=" + MainActivity.API_KEY + "&radius=125&lat=" +
                    MainActivity.testLat + "&lon=" + MainActivity.testLng;*/

            try {
                URL url = new URL(downloadURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                processXML(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }

        Element rootElement;
        boolean returnedStop = false;


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);



            //editText.setText(rootElement.getTagName());
            //editText.setText(currentItem.getNodeName() + ": " + currentChild.getTextContent());

            if( MainActivity.busInfo.getBusRadiusTaskNumber() == 0 && tempBusCode != 0) {

                MainActivity.busInfo.busRadiusTaskNumber(stopRadius);
            }



            if(tempBusCode != 0 && stopRadius <= MainActivity.busInfo.getBusRadiusTaskNumber()) {


                MainActivity.busInfo.busCode(tempBusCode);
                MainActivity.busInfo.busName(tempBusName);
                MainActivity.busInfo.busRadiusTaskNumber(stopRadius);

                //MainActivity.editTextTwo.setText(MainActivity.busInfo.getBusName() + ": " + MainActivity.busInfo.getBusCode() + " I am radius " + stopRadius);

                MainActivity.editTextTwo.setText(MainActivity.busInfo.getBusName() + ": " + MainActivity.busInfo.getBusCode() + " I am radius " + MainActivity.busInfo.getBusRadiusTaskNumber());

                MTAParseDistance.PlaceholderFragment.startTask();


            }



            //ToastMe(rootElement.toString());
            // do something with data here-display it or send to mainactivity
        }

        Node currentItem;
        NodeList itemChildren;
        Node currentChild;
        int tempBusCode;
        String tempBusName;



        public void processXML(InputStream inputStream) throws Exception{

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document xmlDocument = documentBuilder.parse(inputStream);
            rootElement = xmlDocument.getDocumentElement();


            NodeList itemsList = rootElement.getElementsByTagName("stop");
            currentItem = null;
            itemChildren = null;
            currentChild = null;

            for(int i = 0; i < itemsList.getLength(); i++){

                currentItem = itemsList.item(i);
                itemChildren = currentItem.getChildNodes();

                for(int j = 0; j < itemChildren.getLength(); j++){
                    currentChild = itemChildren.item(j);
                    if(currentChild.getNodeName().equalsIgnoreCase("code")){
                        tempBusCode = Integer.parseInt(currentChild.getTextContent());
                        //busInfo.busCode(Integer.parseInt(currentChild.getTextContent()));
                    }

                }
            }


            itemsList = rootElement.getElementsByTagName("route");
            for(int i = 0; i < itemsList.getLength(); i++){
                currentItem = itemsList.item(i);
                itemChildren = currentItem.getChildNodes();

                for(int j = 0; j < itemChildren.getLength(); j++){
                    currentChild = itemChildren.item(j);
                    if(currentChild.getNodeName().equalsIgnoreCase("shortname")){
                        tempBusName = currentChild.getTextContent();

                        //busInfo.busName(currentChild.getTextContent());
                    }
                }
            }



        }



    }








}

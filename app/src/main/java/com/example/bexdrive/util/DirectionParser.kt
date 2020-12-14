package com.example.bexdrive.util

import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class DirectionParser {

    public fun parse(jObject : JSONObject) : List<List<HashMap<String, String>>>{
        var routes = ArrayList<List<HashMap<String, String>>>()
        var jRoutes: JSONArray? = null
        var jLegs: JSONArray? = null
        var jSteps: JSONArray? = null

        try {
            jRoutes = jObject.getJSONArray("routes");

            for (i in 0..jRoutes.length()){
                jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")
                var path = ArrayList<HashMap<String, String>>()

                for (j in 0..jLegs.length()){
                    jSteps = (jLegs.get(j) as JSONObject).getJSONArray("steps")

                    for (k in 0..jSteps.length()){
                        var polyline = ""
                        polyline = ((jSteps.get(k) as JSONObject).get("polyline") as JSONObject).get("points") as String
                        val list : List<LatLng> = decodePoly(polyline)

                        for (l in 0..list.size){
                            var hm = HashMap<String, String>()
                            hm.put("lat", java.lang.Double.toString((list.get(l) as LatLng).latitude))
                            hm.put("lat", java.lang.Double.toString((list.get(l) as LatLng).longitude))
                            path.add(hm)
                        }
                    }
                    routes.add(path)
                }
            }
        }catch (e : JSONException){
            e.printStackTrace()
        }catch (e : Exception){
        }
        return routes
    }

    private fun decodePoly(encoded : String) : List<LatLng> {
        var poly: ArrayList<LatLng> = ArrayList()
        var index = 0
        var len: Int = encoded.length
        var lat = 0
        var lng = 0

        while (index < len){
            var b : Int
            var shift = 0
            var result = 0

            do {
                b = (encoded.toCharArray()[index++] - 63).toInt()
                result = result or (b and 0x1f shl shift)
                shift += 5;
            } while (b >= 0x20)
            var dlat = (if ((result and 1) !== 0) (result shr 1).inv() else (result shr 1))
            lat += dlat

            shift = 0
            result = 0

            do {
                b = (encoded.toCharArray()[index++] - 63).toInt()
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            var dlng = (if ((result and 1) !== 0) (result shr 1).inv() else (result shr 1))
            lat += dlng
            val p = LatLng(((lat / 1E5)), ((lng / 1E5)));
            poly.add(p)
        }
        return poly
    }
}
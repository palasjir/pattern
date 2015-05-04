/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

/**
 * Porovides serialization and deserialization for {@link MatOfPoint}.
 * 
 * @author palas
 */
public class MatOfPointUtil {
    
    /**
     * Constructs serializer for {@link MatOfPoint}.
     * @return serializer for {@link MatOfPoint}
     */
    public static JsonSerializer<MatOfPoint> getSerializer(){
        return new MatOfPointSerializer();
    }
    
    /**
     * Constructs deserializer for {@link MatOfPoint}.
     * @return 
     */
    public static JsonDeserializer<MatOfPoint> getDeserializer(){
        return new MatOfPointDeserializer();
    }
    
    /**
     * Provides serialization logic for {@link MatOfPoint}.
     */
    private static class MatOfPointSerializer implements JsonSerializer<MatOfPoint>{

        @Override
        public JsonElement serialize(MatOfPoint t, Type type, JsonSerializationContext jsc) {
            List<Point> listOfPoint = new ArrayList<>(t.toList());
            JsonArray arr = new JsonArray();
            for (Point point : listOfPoint) {
                arr.add(jsc.serialize(point));
            }
            return arr;
        }
    }
    
    /**
     * Provides deserialization logic for {@link MatOfPoint}.
     */
    private static class MatOfPointDeserializer implements JsonDeserializer<MatOfPoint> {

        @Override
        public MatOfPoint deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            JsonArray array = je.getAsJsonArray();
            List<Point> listOfPoint = new ArrayList<>(array.size());
            for (JsonElement element : array) {
                Point point = jdc.deserialize(element, org.opencv.core.Point.class);
                listOfPoint.add(point);
            }
            
            MatOfPoint matOfPoint = new MatOfPoint();
            matOfPoint.fromList(listOfPoint);
            return matOfPoint;
        }

    }
    
}

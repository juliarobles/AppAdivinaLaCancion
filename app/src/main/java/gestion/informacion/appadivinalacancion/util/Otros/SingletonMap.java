package gestion.informacion.appadivinalacancion.util.Otros;

import java.util.HashMap;
import java.util.Map;

public class SingletonMap {
    private static Map<String, Object> singletonMap;

    private SingletonMap(){}

    public static Map<String, Object> getInstancia(){
        if(singletonMap == null){
            singletonMap = new HashMap<>();
        }
        return singletonMap;
    }
}

package com.example.inf325_apirest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView pResultado;
    EditText pNombre;
    EditText pTelefono;
    EditText pID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicializando las variables
        pResultado = findViewById(R.id.textViewRespuesta);
        pNombre = findViewById(R.id.editTextNombre);
        pTelefono = findViewById(R.id.editTextTelefono);
        pID = findViewById(R.id.editTextID);

    }

    public void listarContactos(View vista) {
        pResultado.setText("");
        String url = "http://192.168.0.11:3050/contactos";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonobject = response.getJSONObject(i);
                                String id = jsonobject.getString("id");
                                String con = jsonobject.getString("contacto");
                                String tel = jsonobject.getString("telefono");
                                pResultado.append(id + " - " + con + " - " + tel + "\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error de respuesta", error.toString());
            }
        });
        queue.add(getRequest);
    }

    //=============Agregar un nuevo contacto
    public void agregarContacto(View vista) {
        pResultado.setText("");
        String url = "http://192.168.0.11:3050/contactos";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //================Convertir a json
        JSONObject json = new JSONObject();
        try {
            json.put("contacto", pNombre.getText().toString());
            json.put("telefono", pTelefono.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String requestBody = json.toString();
        //================Convertir a json fin
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pResultado.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Mensaje de error
                pResultado.setText(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    pResultado.setText(" Error en reques body");
                    return null;
                }
            }
        };

        queue.add(request);

    }
    //Obtener un contacto por el ID
    public void obtenerContactoById(View v){
        pResultado.setText("");
        String id = pID.getText().toString();
        String url = "http://192.168.0.11:3050/contactos/"+id;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonobject = response.getJSONObject(i);
                                String id = jsonobject.getString("id");
                                String con = jsonobject.getString("contacto");
                                String tel = jsonobject.getString("telefono");
                                pResultado.append(id + " - " + con + " - " + tel + "\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error de respuesta", error.toString());
            }
        });
        queue.add(getRequest);
    }
    //Editar un contacto
    public void editarContacto(View v){
        pResultado.setText("");
        String id = pID.getText().toString();
        String url = "http://192.168.0.11:3050/contactos/"+id;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //================Convertir a json
        JSONObject json = new JSONObject();
        try {
            json.put("contacto", pNombre.getText().toString());
            json.put("telefono", pTelefono.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String requestBody = json.toString();
        //================Convertir a json fin
        StringRequest request = new StringRequest(Request.Method.PUT, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pResultado.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Mensaje de error
                pResultado.setText(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    pResultado.setText(" Error en reques body");
                    return null;
                }
            }
        };

        queue.add(request);
    }
    //ELIMINAR UN CONTACTO
    public void eliminarContacto(View v){
        pResultado.setText("");
        String id = pID.getText().toString();
        String url = "http://192.168.0.11:3050/contactos/"+id;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //
        StringRequest request = new StringRequest(Request.Method.DELETE, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pResultado.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Mensaje de error
                pResultado.setText(error.toString());
            }
        }) ;

        queue.add(request);
    }
}
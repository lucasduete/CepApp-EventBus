package io.github.lucasduete.eventbus.cepapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyService extends Service {

    private static final String TAG ="CEPAPP";

    public MyService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            Log.d("CEPAPP", "ALooooooooo");

            String cep = intent.getStringExtra("cep");

            final String stringUrl = String.format("https://viacep.com.br/ws/%s/json", cep);

            try {
                URL urlRequest = new URL(stringUrl);

                HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                connection.connect();

                InputStream inputStream = connection.getInputStream();
                if (inputStream == null) {
                    Log.d(TAG, "inputStream null");
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String linha;
                StringBuffer buffer = new StringBuffer();
                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }

                Log.d(TAG, buffer.toString());
                try {
                    JSONObject object = new JSONObject(buffer.toString());
                    Log.d(TAG, "Objeto:");
                    Log.d(TAG, object.toString());

                    EventBus.getDefault().post(new Evento(object));

                } catch (Exception ex) {
                    Log.d(TAG, "Deu pau na conversao de json");
                    Log.d(TAG, ex.toString());
                }

                if (buffer.length() == 0) {
                    Log.d(TAG, "Buffet length 0");
                }
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Erro fechando o stream", e);
                    }
                }

            } catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }

        }).start();

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

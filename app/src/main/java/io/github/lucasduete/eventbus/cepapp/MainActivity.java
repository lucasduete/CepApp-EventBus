package io.github.lucasduete.eventbus.cepapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CEPAPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.editTextCep);
        final Context context = this;

        Button button = findViewById(R.id.button);
        button.setOnClickListener((view) -> {
            Intent intent = new Intent(context, MyService.class);
            intent.putExtra("cep", editText.getText().toString());
            context.startService(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Evento evento) {

        TextView textView;
        JSONObject object = (JSONObject) evento.getJsonObject();

        try {

            textView = (TextView) findViewById(R.id.editTextLogradouro);
            textView.setText(object.getString("logradouro"));

            textView = (TextView) findViewById(R.id.editTextComplemento);
            textView.setText(object.getString("complemento"));

            textView = (TextView) findViewById(R.id.editTextBairro);
            textView.setText(object.getString("bairro"));

            textView = (TextView) findViewById(R.id.editTextCidade);
            textView.setText(object.getString("localidade"));

            textView = (TextView) findViewById(R.id.editTextEstado);
            textView.setText(object.getString("uf"));

        } catch (Exception ex) {
            Log.d(TAG, "Deu ruim ao preencher");
        }
    }

}

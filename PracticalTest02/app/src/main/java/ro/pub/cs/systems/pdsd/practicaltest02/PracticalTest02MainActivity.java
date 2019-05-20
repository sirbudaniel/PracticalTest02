package ro.pub.cs.systems.pdsd.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText server_port, client_address, client_port, info_editText;
    TextView response_textView;
    Button connect, get_info;

    ServerThread serverThread;
    ClientThread clientThread;


    private ConnectButtonOnClickListener connect_button = new ConnectButtonOnClickListener();
    private class ConnectButtonOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String serverPort = server_port.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private GetInfoListener get_button = new GetInfoListener();
    private class GetInfoListener implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            String clientAddress = client_address.getText().toString();
            String clientPort = client_port.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String info = info_editText.getText().toString();


            if (info == null || info.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            response_textView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), info, response_textView
            );
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        server_port = (EditText) findViewById(R.id.server_port_editText);
        client_address = (EditText) findViewById(R.id.client_address_editText);
        client_port = (EditText) findViewById(R.id.client_port_editText);
        info_editText = (EditText) findViewById(R.id.info_editText);
        response_textView = (TextView) findViewById(R.id.response_textView);

        connect = (Button) findViewById(R.id.connect_button);
        get_info = (Button) findViewById(R.id.client_get_button);
        get_info.setOnClickListener(get_button);
        connect.setOnClickListener(connect_button);


    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}

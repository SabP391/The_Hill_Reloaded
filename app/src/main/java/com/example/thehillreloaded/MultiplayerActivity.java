package com.example.thehillreloaded;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MultiplayerActivity extends AppCompatActivity {

    private int myScore = 0;
    private int receivedScore = -1;


    // Elementi del layout
    private Button bottoneHost, bottoneJoin;
    private ListView devicesList;
    private TextView matchResultBox, connectionStatus, titoloDeviceList, titoloResultBox;
    private boolean isClient;

    private BluetoothAdapter bluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices;
    private BTDeviceListAdapter mDeviceListAdapter;
    private SendReceive sendReceive;
    private IntentFilter discoverDevicesIntent;
    private ClientClass clientClass;
    private ServerClass serverClass;

    private static final String APP_NAME = "TheHillReloaded";
    private static final UUID MY_UUID= UUID.fromString("c1d8f695-0874-443f-9dec-754f5cafe6a4");
    private static final int STATE_LISTENING = 1;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTION_FAILED = 4;
    private static final int STATE_MESSAGE_RECEIVED = 5;
    private static final String SMARTPHONE_CODE = "5a020c";

    int REQUEST_ENABLE_BLUETOOTH = 1;
    int DISCOVERABLE_ENABLED = 1;

    private static final int MP_GAME_ACTIVITY_REQUEST_CODE = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1000);

        discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        // Tutte le findViewById per gli elementi nell'activity
        bottoneHost = (Button) findViewById(R.id.bottone_hostMatch);
        bottoneJoin = (Button) findViewById(R.id.bottone_joinMatch);

        titoloDeviceList = (TextView) findViewById(R.id.titiolo_lista_host);
        titoloDeviceList.setVisibility(View.INVISIBLE);
        devicesList = (ListView) findViewById(R.id.lista_host);
        devicesList.setVisibility(View.INVISIBLE);

        titoloResultBox = (TextView) findViewById(R.id.titolo_risultato_partita);
        titoloResultBox.setVisibility(View.INVISIBLE);
        matchResultBox = (TextView) findViewById(R.id.box_risultato_match);
        matchResultBox.setPadding(10,6,10,6);
        matchResultBox.setVisibility(View.INVISIBLE);
        connectionStatus = (TextView) findViewById(R.id.stato_connessione);

        // Inizializzo l'ArrayList dei dispositivi bluetooth
        mBTDevices = new ArrayList<BluetoothDevice>();
        // Inizializzo il BluetoothAdapter per la gestione del BT
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Inizializzo il LocationManager per gestire il GPS
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Se il BT non è attivo chiede all'utente se vuole attivarlo
        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        // Se il GPS non è attivo chiede al'utente se vuole attivarlo
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Il GPS è attivo", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }

        /*
            Genero un punteggio casuale.
            Qui dovrebbe essere avviato il metodo che fa
            iniziare la partita e restituisce un intero.
         */
        myScore = (int) (Math.random() * 201);

        // IMPLEMENTAZIONE LISTENER DEI BOTTONI-----------------------------------------------------
        /*
            ---Pulsante per la ricerca dei dispositivi---
            Avvia la ricerca dei dispositivi tramite bluetooth
            e chiama il BroadcastReceiver.
         */
        bottoneJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isDiscovering()){
                    devicesList.setVisibility(View.VISIBLE);
                    titoloDeviceList.setVisibility(View.VISIBLE);
                    bluetoothAdapter.cancelDiscovery();
                    Log.d("Log BT:", "Canceling discovery");
                    mBTDevices.clear();
                    bluetoothAdapter.startDiscovery();
                    Log.d("Log BT:", "Starting discovery");
                    registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
                    Log.d("Log BT:", "Calling broadcast receiver");
                }
                if (!bluetoothAdapter.isDiscovering()){
                    devicesList.setVisibility(View.VISIBLE);
                    titoloDeviceList.setVisibility(View.VISIBLE);
                    mBTDevices.clear();
                    bluetoothAdapter.startDiscovery();
                    Log.d("Log BT:", "Starting discovery");
                    registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
                    Log.d("Log BT:", "Calling broadcast receiver");
                }
            }
        });

        /*
            ---Pulsante per Hostare una partita---
            Il pulsante rende il dispositivo visibile agli altri
            per 60 secondi e crea un oggetto di classe serverClass per
            poter gestire la comunicazione.
         */
        bottoneHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicesList.setVisibility(View.INVISIBLE);
                titoloDeviceList.setVisibility(View.INVISIBLE);
                Intent discoverableIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                startActivityForResult(discoverableIntent, DISCOVERABLE_ENABLED);
                isClient = false;
                serverClass = new ServerClass();
                serverClass.start();
            }
        });

        /*
            ---Tap su uno dei dispositivi mostrati nella lista---
            Crea un oggetto di classe clientClass che serve
            per gestire la comunicazione con il server
         */
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isClient = true;
                clientClass = new ClientClass(mBTDevices.get(i));
                clientClass.start();
                connectionStatus.setText("Connecting");
            }
        });
        // FINE LISTENER DEI BOTTONI----------------------------------------------------------------
    }

    /*
        BroadcastReceiver è responsabile di recepire un cambiamento nel bluetooth.
        In questo caso nel caso in cui viene trovato un nuovo dispositivo disponibile
        lo aggiunge all'ArrayList dei dispositivi bluetooth a cui è possibile connettersi.
        Infine chiama il DeviceListAdapter per mostrare nella ListView i dispositivi trovati.
     */
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass deviceClass = device.getBluetoothClass();
                if(deviceClass.toString().equals(SMARTPHONE_CODE) && !mBTDevices.contains(device)) {
                    Log.d("Dispositivo trovato: ", device.getName() + " " + deviceClass.toString() + " " + device.getAddress());
                    mBTDevices.add(device);
                }
                mDeviceListAdapter = new BTDeviceListAdapter(getApplicationContext(), R.layout.device_adapter_view, mBTDevices);
                devicesList.setAdapter(mDeviceListAdapter);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MP_GAME_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                myScore = data.getIntExtra("Result", 0);
                if(!isClient){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String string = String.valueOf(myScore);
                            sendReceive.write(string.getBytes());}
                    }, 2000);

                }

            }
        }
    }

    /*
            Metodo che mostra la finestra di attivazione del GPS.
         */
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.messaggio_gps)
                .setCancelable(false)
                .setPositiveButton(R.string.vai_impostazioni,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(R.string.annulla,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    // Metodo che confronta i risultati della partita
    public String matchResult(int punteggioP1, int punteggioP2){
        if(punteggioP1 > punteggioP2){
            return "Hai vinto";
        }else if (punteggioP2 > punteggioP1){
            return "Hai perso";
        }else{
            return "Pareggio";
        }
    }

    // Handler del server per cambiare il messaggio di stato a schermo (Connesso, Non connesso, ecc...)
    Handler handlerServer = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTENING:
                    connectionStatus.setText(R.string.connection_listening);
                    break;
                case STATE_CONNECTING:
                    connectionStatus.setText(R.string.connection_connecting);
                    break;
                case STATE_CONNECTED:
                    connectionStatus.setText(R.string.connection_connected);
                    Intent startMPGame = new Intent(getApplicationContext(), MultiplayerGameActivity.class);
                    startActivityForResult(startMPGame, MP_GAME_ACTIVITY_REQUEST_CODE);
                    break;
                case STATE_CONNECTION_FAILED:
                    connectionStatus.setText(R.string.connection_failed);
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tempMsg = new String(readBuffer, 0, msg.arg1);
                    receivedScore = Integer.parseInt(tempMsg);
                    String risultato = matchResult(myScore, receivedScore);
                    matchResultBox.setVisibility(View.VISIBLE);
                    matchResultBox.setText(risultato + "\n" + "Tu: " + myScore + "\n" + "Avversario: " + receivedScore);
                    closeConnection(isClient);
                    break;
            }

            return true;
        }
    });

    private void closeConnection(boolean flag) {
        if(flag){
            try {
                sendReceive.join(3000);
                clientClass.socket.close();
                clientClass.join(3000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                sendReceive.join(3000);
                serverClass.serverSocket.close();
                serverClass.join(3000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        connectionStatus.setText("Connessione terminata");
    }
    // Fine handler server--------------------------------------------------------------------------

    // Handler del client per cambiare il messaggio di stato a schermo (Connesso, Non connesso, ecc...)
    Handler handlerClient = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTENING:
                    connectionStatus.setText(R.string.connection_listening);
                    break;
                case STATE_CONNECTING:
                    connectionStatus.setText(R.string.connection_connecting);
                    break;
                case STATE_CONNECTED:
                    connectionStatus.setText(R.string.connection_connected);
                    connectionStatus.setText(R.string.connection_connected);
                    Intent startMPGame = new Intent(getApplicationContext(), MultiplayerGameActivity.class);
                    startActivityForResult(startMPGame, MP_GAME_ACTIVITY_REQUEST_CODE);
                    break;
                case STATE_CONNECTION_FAILED:
                    connectionStatus.setText(R.string.connection_failed);
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tempMsg = new String(readBuffer, 0, msg.arg1);
                    receivedScore = Integer.parseInt(tempMsg);
                    String risultato = matchResult(myScore, receivedScore);
                    matchResultBox.setVisibility(View.VISIBLE);
                    matchResultBox.setText(risultato + "\n" + "Tu: " + myScore + "\n" + "Avversario: " + receivedScore);

                    String string = String.valueOf(myScore);
                    sendReceive.write(string.getBytes());
                    closeConnection(isClient);
                    break;
            }

            return true;
        }
    });
    // Fine handler client--------------------------------------------------------------------------


    // Inizio classe Server-------------------------------------------------------------------------
    private class ServerClass extends Thread{
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            BluetoothSocket socket = null;

            // Finchè non c'è una connessione resta in ascolto per nuove connessioni
            while (socket == null){
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handlerServer.sendMessage(message);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handlerServer.sendMessage(message);
                }

                if(socket != null){
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handlerServer.sendMessage(message);

                    sendReceive = new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }
    // Fine classe Server---------------------------------------------------------------------------

    // Inizio classe Client-------------------------------------------------------------------------
    private class ClientClass extends Thread{
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1){
            device = device1;

            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void  run(){
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handlerClient.sendMessage(message);

                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handlerClient.sendMessage(message);
            }
        }
    }
    // Fine classe Client---------------------------------------------------------------------------

    // Inizio classe per inviare e ricevere dati----------------------------------------------------
    private class SendReceive extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket){
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("SendReceive:", "connection lost");
            }

            inputStream = tempIn;
            outputStream = tempOut;

        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (true){
                try {
                    bytes = inputStream.read(buffer);
                    if(isClient) {
                        handlerClient.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                    }else {
                        handlerServer.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    if(receivedScore == -1) {
                        matchResultBox.setVisibility(View.VISIBLE);
                        matchResultBox.setText("Il tuo avversario ha abbandonato la partita");
                    }
                }

            }
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Fine classe per inviare e ricevere dati------------------------------------------------------
}
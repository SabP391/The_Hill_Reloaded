package com.example.thehillreloaded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.content.pm.PackageManager;
import android.location.LocationManager;
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

    int punteggio;

    // Elementi del layout
    private Button host, connect, send;
    private ListView devicesList;
    private TextView messBox, status, titoloHost;
    private BluetoothAdapter bluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices;
    private BTDeviceListAdapter mDeviceListAdapter;
    private SendReceive sendReceive;
    private IntentFilter discoverDevicesIntent;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;
    int DISCOVERABLE_ENABLED = 1;

    private static final String APP_NAME = "TheHillReloaded";
    private static final UUID MY_UUID= UUID.fromString("c1d8f695-0874-443f-9dec-754f5cafe6a4");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 69);
        }

        discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        // Tutte le findViewById per gli elementi nell'activity
        host = (Button) findViewById(R.id.bottone_hostMatch);
        connect = (Button) findViewById(R.id.bottone_joinMatch);
        //send = (Button) findViewById(R.id.button_send);
        devicesList = (ListView) findViewById(R.id.lista_host);
        messBox = (TextView) findViewById(R.id.risultato_match);
        status = (TextView) findViewById(R.id.stato_connessione);
        titoloHost = (TextView) findViewById(R.id.questa_lista_host);

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
        punteggio = (int) (Math.random() * 201);

        // IMPLEMENTAZIONE LISTENER DEI BOTTONI-----------------------------------------------------

        if(connect == null)
        Log.d("Connect:", "è NULL");

        if(host == null)
            Log.d("Host:", "è NULL");
        /*
            ---Pulsante per la ricerca dei dispositivi---
            Avvia la ricerca dei dispositivi tramite bluetooth
            e chiama il BroadcastReceiver.
         */
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isDiscovering()){
                    bluetoothAdapter.cancelDiscovery();
                    Log.d("Log BT:", "Canceling discovery");

                    mBTDevices.clear();
                    bluetoothAdapter.startDiscovery();
                    titoloHost.setText("Lista host");
                    Log.d("Log BT:", "Starting discovery");

                    registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
                }
                if (!bluetoothAdapter.isDiscovering()){
                    mBTDevices.clear();
                    bluetoothAdapter.startDiscovery();
                    titoloHost.setText("Lista host");
                    Log.d("Log BT:", "Starting discovery");
                    registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
                }
            }
        });

        /*
            ---Pulsante per Hostare una partita---
            Il pulsante rende il dispositivo visibile agli altri
            per 60 secondi e crea un oggetto di classe serverClass per
            poter gestire la comunicazione.
         */
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent discoverableIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                startActivityForResult(discoverableIntent, DISCOVERABLE_ENABLED);
                ServerClass serverClass = new ServerClass();
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
                ClientClass clientClass = new ClientClass(mBTDevices.get(i));
                clientClass.start();
                status.setText("Connecting");
            }
        });

        /*
            ---Pulsante per inviare il punteggio---
            Servendosi della classe sendReceive invia il proprio punteggio
            al dispositivo con cui si è giocata la partita
         */
        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int punteggioGenerato = punteggio;
                String string = String.valueOf(punteggioGenerato);
                sendReceive.write(string.getBytes());
            }
        });*/
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
            Log.d("Sono il broadcast receiver e ", "sto facendo cose");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass deviceClass = device.getBluetoothClass();
                if(deviceClass.toString().equals("5a020c") && !mBTDevices.contains(device)) {
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

    /*
        Metodo che mostra la finestra di attivazione del GPS.
     */
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Il GPS è disattivato. Vuoi attivarlo?")
                .setCancelable(false)
                .setPositiveButton("Abilita GPS nelle impostazioni",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Annulla",
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
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tempMsg = new String(readBuffer, 0, msg.arg1);
                    int punteggioP1 = punteggio;
                    int punteggioP2 = Integer.parseInt(tempMsg);
                    String risultato = matchResult(punteggioP1, punteggioP2);
                    messBox.setText(risultato + "\n" + "P1: " + punteggioP1 + "\n" + "P2: " + punteggioP2);
                    break;
            }

            return true;
        }
    });
    // Fine handler server--------------------------------------------------------------------------

    // Handler del client per cambiare il messaggio di stato a schermo (Connesso, Non connesso, ecc...)
    Handler handlerClient = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tempMsg = new String(readBuffer, 0, msg.arg1);
                    int punteggioP2 = punteggio;
                    int punteggioP1 = Integer.parseInt(tempMsg);
                    String risultato = matchResult(punteggioP1, punteggioP2);
                    messBox.setText(risultato + "\n" + "P1: " + punteggioP1 + "\n" + "P2: " + punteggioP2);
                    break;
            }

            return true;
        }
    });
    // Fine handler server--------------------------------------------------------------------------

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

                    sendReceive = new SendReceive(socket, 0);
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

                sendReceive = new SendReceive(socket, 1);
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
        private int isClient;

        public SendReceive(BluetoothSocket socket, int flag){
            isClient = 0;
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
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
                    if(isClient == 1) {
                        handlerClient.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                    }else {
                        handlerServer.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
package com.example.sarthakj.home_automation_mark1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;

    private BluetoothAdapter bluetoothAdapter = null;
    private String address;
    private ProgressDialog progressDialog;
    private boolean isConnected = false;
    private BluetoothSocket bluetoothSocket = null;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Switch[] switches = new Switch[8];
    LinearLayout mainContainer;
    LinearLayout errorContainer;
    Button retryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mainContainer = (LinearLayout) findViewById(R.id.main_container);
        mainContainer.setVisibility(GONE);
        errorContainer = (LinearLayout) findViewById(R.id.error_layout);
        errorContainer.setVisibility(GONE);
        retryButton = (Button) findViewById(R.id.btn_retry);
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth Device not available", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent turnOnBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnBT, 1);
        }

        if (sharedPreferences.getString("device_address",null) == null){
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();
        } else{
            new ConnectDevice().execute();
        }


        switches[0] = (Switch) findViewById(R.id.switch1);
        switches[1] = (Switch) findViewById(R.id.switch2);
        switches[2] = (Switch) findViewById(R.id.switch3);
        switches[3] = (Switch) findViewById(R.id.switch4);
        switches[4] = (Switch) findViewById(R.id.switch5);
        switches[5] = (Switch) findViewById(R.id.switch6);
        switches[6] = (Switch) findViewById(R.id.switch7);
        switches[7] = (Switch) findViewById(R.id.switch8);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor e = sharedPreferences.edit();
                e.putString("device_address", null);
                e.commit();
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class ConnectDevice extends AsyncTask<Void, Void, Boolean> {

        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute() {
            mainContainer.setVisibility(GONE);
            errorContainer.setVisibility(GONE);
            progressDialog = ProgressDialog.show(MainActivity.this, "Connecting...", "Please Wait!");
            address = sharedPreferences.getString("device_address", null);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (bluetoothSocket == null && address != null || !isConnected && address != null) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(address);
                    bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
                return true;
            } catch (IOException e) {
                connectSuccess = false;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (!connectSuccess) {
                isConnected = false;
                errorContainer.setVisibility(View.VISIBLE);
                mainContainer.setVisibility(GONE);
            } else {
                isConnected = true;
                mainContainer.setVisibility(View.VISIBLE);
                errorContainer.setVisibility(GONE);
                if (bluetoothSocket != null) {
                    try {
                        bluetoothSocket.getOutputStream().write("z".toString().getBytes());
                        InputStream inputStream = bluetoothSocket.getInputStream();
                        int val = inputStream.read();
                        boolean[] bits = new boolean[8];
                        for (int i = 0; i < 8; i++) {
                            bits[i] = (val & (1 << i)) != 0;
                        }
                        for (int i = 0; i < 8; i++) {
                            switches[i].setChecked(bits[i]);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                switches[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("0".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("1".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                switches[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("2".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("3".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });


                switches[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("4".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("5".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                switches[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("6".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("7".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                switches[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("8".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("9".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                switches[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("a".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("b".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                switches[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("c".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("d".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                switches[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("e".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (bluetoothSocket != null) {
                                try {
                                    bluetoothSocket.getOutputStream().write("f".toString().getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        Disconnect();
        super.onBackPressed();
    }

    private void Disconnect() {
        if (bluetoothSocket != null) //If the btSocket is busy
        {
            try {
                bluetoothSocket.close(); //close connection
                finish();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

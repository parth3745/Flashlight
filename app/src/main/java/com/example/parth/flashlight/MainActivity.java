package com.example.parth.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.ads.mediation.MediationServerParameters;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.security.Policy;


public class MainActivity extends AppCompatActivity {

    Switch stable;
    Switch blink;
    Switch fast;

    String cameraId = null; // Usually back camera is at 0 position.
    CameraManager camManager;
    Camera.Parameters p;

    private Camera camera;
    private boolean hasFlash;
    CountDownTimer timer;
    Double infinity = Double.POSITIVE_INFINITY;

    public void turnOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);   //Turn ON
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        else {
            if (!hasFlash) {
                Toast.makeText(getApplicationContext(),"Device doesn't support flash!",Toast.LENGTH_SHORT).show();
            }
            else {
                camera = Camera.open();
                p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
            }

        }
    }

    public void turnOff() throws CameraAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            camManager.setTorchMode(cameraId, false);
        }
        else {
            camera = Camera.open();
            Camera.Parameters p = camera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stable = (Switch) findViewById(R.id.stable);
        blink = (Switch) findViewById(R.id.blink);
        fast = (Switch) findViewById(R.id.fast);

        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        stable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                blink.setChecked(false);
                fast.setChecked(false);
                if (b == true) {
                    turnOn();
                }
                else if (b == false) {
                    try {
                        turnOff();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        blink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            int i = 0;
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                stable.setChecked(false);
                fast.setChecked(false);
                if (b == true) {
                    timer = new CountDownTimer(infinity.intValue(),1000) {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onTick(long l) {
                            turnOn();
                            i++;
                            if (i % 2 == 0) {
                                try {
                                    turnOff();
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onFinish() {
                            try {
                                turnOff();
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    timer.start();
                }
                else if (b == false) {
                    timer.cancel();
                    try {
                        turnOff();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        fast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            int j = 0;
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                stable.setChecked(false);
                blink.setChecked(false);
                if (b == true) {
                    timer = new CountDownTimer(infinity.intValue(),50) {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onTick(long l) {
                            turnOn();
                            j++;
                            if (j % 1 == 0) {
                                try {
                                    turnOff();
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onFinish() {
                            try {
                                turnOff();
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    timer.start();
                }
                else if (b == false) {
                    timer.cancel();
                    try {
                        turnOff();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        }
}
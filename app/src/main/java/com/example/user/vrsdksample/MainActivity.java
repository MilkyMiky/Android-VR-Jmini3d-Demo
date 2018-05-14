package com.example.user.vrsdksample;


import android.os.Bundle;
import android.os.Vibrator;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import jmini3d.Color4;
import jmini3d.JMini3d;
import jmini3d.Object3d;
import jmini3d.Scene;
import jmini3d.Vector3;
import jmini3d.android.Renderer3d;
import jmini3d.android.ResourceLoader;
import jmini3d.geometry.BoxGeometry;
import jmini3d.light.AmbientLight;
import jmini3d.light.PointLight;
import jmini3d.material.Material;
import jmini3d.material.PhongMaterial;

public class MainActivity extends GvrActivity implements GvrView.StereoRenderer {

    private float[] camera;
    private float[] view;
    private float[] headView;
    private float[] modelViewProjection;
    private float[] modelView;
    private float[] modelFloor;

    private float[] tempPosition;
    private float[] headRotation;

    private Vibrator mVibrator;

    private static final float CAMERA_Z = 0.01f;

    Object3d cube;
    private Scene scene;
    private Renderer3d renderer;
    private VREyeRender eyeRender = new VREyeRender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        scene = new Scene();
        scene.camera.setPosition(0, 0, 0);
        scene.camera.setTarget(0, 0, -1);
        scene.camera.setUpAxis(0, 1, 0);
        scene.camera.updateViewMatrix();

        Color4 white = new Color4(255, 255, 255);
        Color4 red = new Color4(255, 128, 128);

        AmbientLight light = new AmbientLight(white, 0.5f);
        scene.addLight(light);

        PointLight light2 = new PointLight(new Vector3(3, 0.5f, 1), white, 0.5f);
        scene.addLight(light2);

        BoxGeometry geometry = new BoxGeometry(0.5f);
        Material material = new PhongMaterial(red, white, white);
        cube = new Object3d(geometry, material);
        cube.setPosition(0, 0, -5);

        scene.addChild(cube);
    }

    private void init() {
        setContentView(R.layout.activity_main);
        JMini3d.useOpenglAxisSystem();
        GvrView gvrView = (GvrView) findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);
        gvrView.setDistortionCorrectionEnabled(true);

        // This line is really important! It's what enables the low-latency
        // VR experience. Without it, you'll have a headache after five minutes.
        gvrView.setAsyncReprojectionEnabled(true);

        AndroidCompat.setSustainedPerformanceMode(this, true);
        setGvrView(gvrView);
        renderer = new Renderer3d(new ResourceLoader(this));
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
//        Vector3 direction = new Vector3(1,1,1);
//        Vector3 up = new Vector3();
//        Vector3 side = new Vector3();
//        cube.setRotationMatrix(direction, up, side);
    }

    @Override
    public void onDrawEye(Eye eye) {
        eyeRender.render(scene, eye, renderer);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        renderer.setViewPort(width, height);
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        renderer.reset();
    }

    @Override
    public void onRendererShutdown() {

    }
}

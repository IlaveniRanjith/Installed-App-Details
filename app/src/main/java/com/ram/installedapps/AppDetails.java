package com.ram.installedapps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

public class AppDetails extends AppCompatActivity {
    String pkgName, applicationName;
    TextView tvAppName, tvPkgName, tvCertFingerprint, tvCertSubject, tvCertIssuer, tvAppCount, tvPermissions, tvPermissionsGranted;

    String appIcon;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);


        pkgName = getIntent().getStringExtra("PackageName");
        applicationName = getIntent().getStringExtra("AppName");
        appIcon = getIntent().getStringExtra("AppIcon");
//        Log.d("TAG", "onCreate: " + pkgName);

        //Bind the views of Textviews
        tvAppName = findViewById(R.id.tv_appName);
        tvPkgName = findViewById(R.id.tv_pkgName);
        tvCertFingerprint = findViewById(R.id.tv_certfingerprint);
        tvCertSubject = findViewById(R.id.tv_certdetails);
        tvCertIssuer = findViewById(R.id.tv_certissuer);
//        tvAppCount = findViewById(R.id.tv_app_count);
        tvPermissions = findViewById(R.id.tv_permissions);
        tvPermissionsGranted = findViewById(R.id.tv_permissions_granted);


        //calling all the methods onStart()
        getCertificateDetails();
        getPermissions();
        getGrantedPermissions();
    }

    private void getPermissions() {
        StringBuilder permissionDetails = new StringBuilder();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);

            int c =1;

            for (String perm : pInfo.requestedPermissions) {
                if (perm != null) {
                    permissionDetails.append(c).append(". ").append(perm).append("\n");

                } else {
                    permissionDetails.append("null\n");
                }
                c++;
            }
            tvPermissions.setText(permissionDetails);




        } catch (Exception e) {

        }
    }

    void getGrantedPermissions() {
        StringBuilder permissionGranted = new StringBuilder();

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);
            int count = 1;

            for (int i = 0; i< pi.requestedPermissions.length; i++){
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0){
                    permissionGranted.append(count).append(". ").append(pi.requestedPermissions[i]).append("\n");
                    count++;
                }

            }
            tvPermissionsGranted.setText(permissionGranted);
        } catch (Exception e) {
        }
    }

    @SuppressLint("SetTextI18n")
    private void getCertificateDetails() {
        StringBuilder certificateDetails = new StringBuilder();

        // Get the list of installed packages
        try {
            PackageInfo info = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNING_CERTIFICATES);
            String pkgName = info.packageName;

            SigningInfo sings = info.signingInfo;

            certificateDetails.append("Package Name: ").append(pkgName).append("\n");
            tvAppName.setText(applicationName);
            tvPkgName.setText(pkgName);

            Signature[] signaturesNew = sings.getSigningCertificateHistory();



            for (Signature signatureNew : signaturesNew) {
                String certificateFingerprint = getCertificateFingerprint(signatureNew);

                try {
                    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                    X509Certificate x509Certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signatureNew.toByteArray()));

                    X500Principal issuerPrincipal = x509Certificate.getIssuerX500Principal();
                    X500Principal subjectPrincipal = x509Certificate.getSubjectX500Principal();

                    String certificateIssuerName = issuerPrincipal.toString();
                    String certificateSubject = subjectPrincipal.toString();

                    Log.d("TAG", "PackageName: " + pkgName + " Certificate Info: " + certificateFingerprint);
                    Log.d("TAG", "Certificate Issuer: " + certificateIssuerName);
                    Log.d("TAG", "Certificate Subject: " + certificateSubject);

                    // Display package name and certificate details
                    certificateDetails.append("------------------------------------------------------\n");
                    certificateDetails.append("Certificate Fingerprint: \n").append(certificateFingerprint).append("\n");
                    tvCertFingerprint.setText(certificateFingerprint);
                    certificateDetails.append("Certificate Issuer: \n").append(certificateIssuerName).append("\n");
                    tvCertIssuer.setText(certificateIssuerName);
                    certificateDetails.append("Certificate Subject: \n").append(certificateSubject).append("\n");
                    tvCertSubject.setText(certificateSubject);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        count = getIntent().getIntExtra("AppCount", 0);
//        tvAppCount.setText("Total Applications: " + count);

    }


    private String getCertificateFingerprint(Signature signature) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(signature.toByteArray());
            byte[] digest = md.digest();
            return bytesToHex(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "Error calculating fingerprint";
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

}
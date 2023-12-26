package com.ram.installedapps;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

public class AppDetails extends AppCompatActivity {
    String pkgName, applicationName;
    TextView tvAppName, tvPkgName, tvCertFingerprint, tvCertSubject, tvCertIssuer, tvSigningKey, tvAppCount, tvPermissions;

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
//        tvSigningKey = findViewById(R.id.tv_signingKey);
//        tvAppCount = findViewById(R.id.tv_app_count);
        tvPermissions = findViewById(R.id.tv_permissions);


        getCertificateDetails();
        getPermissions();
    }

    private void getPermissions() {
        StringBuilder permissionDetails = new StringBuilder();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);

//            try {
//                PackageInfo info = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);
//                if (info.requestedPermissions != null) {
//                    for (String p : info.requestedPermissions) {
//                        p.toString();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }



//            PermissionInfo[] permissions = ;

            for (String perm : pInfo.requestedPermissions) {
//                Log.d("Ranjith", "getPermissions: "+ perm.name);
                if (perm != null) {
                    permissionDetails.append(perm).append("\n");

                } else {
                    permissionDetails.append("null\n");
                }
            }
            if (permissionDetails != null){
                tvPermissions.setText(permissionDetails);
            } else {
                tvPermissions.setText("null");
            }

        } catch (Exception e) {

        }
    }

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

            // SHA256 hash values of the our CUSTOM KEYS
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("8254612C959B728164CC5AF92EF35EE06B9805DA184BECAE96C0888F0DD9C90D", "platform");
            keyMap.put("C34DBFB68257BEF3D194214CD5740B943B822FEBEDC4B8C14F0295FF30A70C7D", "testkey");
            keyMap.put("6A0A29227198DEF14B5019E565AAFEAF3C388DF39ABF04FBB2031DF56B513357", "bluetooth");
            keyMap.put("7F466460D65720E29934F08A5B88B39FAE875597AEB35CC90334F6AAB3C2F016", "shared");
            keyMap.put("2F4690EDFD79D1E754B7CA92F1E3374FB2994FF53E5F979218CD689108874A30", "networkstack");
            keyMap.put("25F299C89BB5A334A44147217A51F9ECE974CB6A26A01E812DB972FD7AC3246C", "sdk_sandbox");
            keyMap.put("E959A262F8E4AE9200421B088150011C696E396017A0467210B194116BCEC236", "verity");
            keyMap.put("1EFBABC3A9C0A5694C4D5B1EBD5DC49A84B7CBD9227F88DAC8964986C8418706", "media");
            keyMap.put("1132F5FED67D1135A31516CA6526B07834ED22F8465EB5DE84E590998BC7E089", "cts_uicc_2021");

            for (Signature signatureNew : signaturesNew) {
                String certificateNew = getCertificateFingerprint(signatureNew);

                try {
                    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                    X509Certificate x509Certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signatureNew.toByteArray()));

                    X500Principal issuerPrincipal = x509Certificate.getIssuerX500Principal();
                    X500Principal subjectPrincipal = x509Certificate.getSubjectX500Principal();

                    String certificateName = issuerPrincipal.toString();
                    String certificateSubject = subjectPrincipal.toString();

                    Log.d("TAG", "PackageName: " + pkgName + " Certificate Info: " + certificateNew);
                    Log.d("TAG", "Certificate Issuer: " + certificateName);
                    Log.d("TAG", "Certificate Subject: " + certificateSubject);

                    // Display package name and certificate details
                    certificateDetails.append("------------------------------------------------------\n");
                    certificateDetails.append("Certificate Fingerprint: \n").append(certificateNew).append("\n");
                    tvCertFingerprint.setText(certificateNew);
                    certificateDetails.append("Certificate Issuer: \n").append(certificateName).append("\n");
                    tvCertIssuer.setText(certificateName);
                    certificateDetails.append("Certificate Subject: \n").append(certificateSubject).append("\n");
                    tvCertSubject.setText(certificateSubject);

                    String keyHash = keyMap.get(certificateNew);
//                    if (keyHash != null) {
//                        certificateDetails.append("------------------------------------------------------");
//                        certificateDetails.append("\nThis App is Signed with ").append(keyHash).append(" KEY\n");
//                        tvSigningKey.setText("This App is signed with " + keyHash + " Key");
//                    } else {
//                        certificateDetails.append("------------------------------------------------------");
//                        certificateDetails.append("\nThis App is Not Signed with any of the AOSP keys\n");
//                        tvSigningKey.setText("This App is Not Signed with any of the AOSP keys");
//                    }
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